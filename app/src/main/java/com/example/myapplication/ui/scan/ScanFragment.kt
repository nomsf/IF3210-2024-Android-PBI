package com.example.myapplication.ui.scan

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentScanBinding
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.TransactionRepository
import com.example.myapplication.util.SecretPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executor

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var cameraId: String
    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureRequestBuilder : CaptureRequest.Builder
    private lateinit var mainExecutor : Executor
    private var cameraPermission: Boolean = false
    private lateinit var fileImage: File
    private lateinit var imagePath : String
    private lateinit var imageByteArray : ByteArray

    private lateinit var authRepository : AuthRepository
    private lateinit var transactionRepository: TransactionRepository

    private val stateCallBack = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startCamera()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice.close()
        }
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            Log.i("Development", "Surface texture available")
            startCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            // Ignored, Camera does all the work for us
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            // Ignored
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepository = AuthRepository(SecretPreference(requireContext()))
        transactionRepository = TransactionRepository(requireContext())

        authRepository.billResponseLiveData.observe(this, Observer {
            for (item in it.items) {
                Log.i("Development", "Item: ${item.name}")
                val transaction = TransactionEntity(
                    title = item.name,
                    nominal = item.price,
                    kategori = "Bill_Scan",
                    lokasi = null,
                    tanggal = null
                )

                lifecycleScope.launch {
                    transactionRepository.insertTransaction(transaction)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentScanBinding.inflate(inflater, container, false)

        cameraPermissionCheck()

        // preparing laert dialog for image preview
        val builder = AlertDialog.Builder(requireContext())
        val dialogInflater = layoutInflater
        val dialogLayout = dialogInflater.inflate(R.layout.dialog_scan_confirmation, null)
        val buttonRetake = dialogLayout.findViewById<Button>(R.id.retake_button_dialog)
        val buttonSave = dialogLayout.findViewById<Button>(R.id.save_button_dialog)

        // creating imageReader for the camera
        val imageReader = ImageReader.newInstance(290, 420, ImageFormat.JPEG, 1)

        imageReader.setOnImageAvailableListener({ reader ->
            Log.i("Development", "image listener called")
            reader.acquireNextImage().use { image ->
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                imageByteArray = bytes
                buffer.get(bytes)
//                saveImage(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val imageView = dialogLayout.findViewById<ImageView>(R.id.bill_image)

                imageView.setImageBitmap(bitmap)
            }
        }, null)

        val alertDialog = builder.create()
        buttonRetake.setOnClickListener {
            alertDialog.dismiss()
        }

        buttonSave.setOnClickListener {
            saveImage(imageByteArray)
            if(::imagePath.isInitialized) {
                CoroutineScope(Dispatchers.IO).launch {
                    val uri = Uri.parse(imagePath)
                    val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
                    val file = File.createTempFile("upload", null, requireContext().cacheDir)
                    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)

                    authRepository.uploadBillRequest(file)

                    parcelFileDescriptor?.close()
                }
            } else {
                Log.w("Development", "Image path not initialized")
            }

            alertDialog.dismiss()
        }

        binding.buttonScan.setOnClickListener {
            takePicture(imageReader)
            alertDialog.setView(dialogLayout)
            alertDialog.show()
        }

//        binding.buttonSend.setOnClickListener {
//            if(::imagePath.isInitialized) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    val uri = Uri.parse(imagePath)
//                    val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
//                    val file = File.createTempFile("upload", null, requireContext().cacheDir)
//                    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
//                    val outputStream = FileOutputStream(file)
//                    inputStream.copyTo(outputStream)
//
//                    authRepository.uploadBillRequest(file)
//
//                    parcelFileDescriptor?.close()
//                }
//            } else {
//                Log.w("Development", "Image path not initialized")
//            }
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileImage = File(requireContext().externalMediaDirs.first(), "\"${System.currentTimeMillis()}.jpg\"" )
    }

    override fun onResume() {
        super.onResume()
        if(cameraPermission) {
            setupCamera()
            binding.cameraPreview.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // CAMERA
    private fun cameraPermissionCheck() {
        Log.i("Development", "Checking camera permission")
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED) {
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted
                    Log.i("Development", "Camera permission granted")
                    cameraPermission = true
                } else {
                    // Permission is denied
                    Log.w("Development", "Camera permission DENIED")
                }
            }.launch(Manifest.permission.CAMERA)

        } else {
            cameraPermission = true
        }

        if(cameraPermission) {
            Log.i("Development", "Camera permission granted")
        }
    }

    private fun setupCamera() {
        Log.i("Development", "Setting up camera")
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            val cameraManager =
                requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraId = cameraManager.cameraIdList[0]
            Log.i("Development", "Camera ID: $cameraId")
            cameraManager.openCamera(cameraId, stateCallBack, null)
            Log.i("Development", "Camera opened")
        }
    }
    @SuppressLint("Recycle")
    private fun startCamera() {
        val texture = binding.cameraPreview.surfaceTexture
        if (texture != null) {
            texture.setDefaultBufferSize(290, 420)
        } else {
            Log.e("Development", "Texture is null")
        }
        Log.i("Development", "Camera starting")

        val surface = Surface(texture)
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        cameraDevice.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                session.setRepeatingRequest(captureRequestBuilder.build(), null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.w("Development", "Camera configuration failed")
            }
        }, null)

//        val outputConfig : OutputConfiguration = OutputConfiguration(surface)
//        mainExecutor = ContextCompat.getMainExecutor(requireContext())
//
//        val sessionConfig : SessionConfiguration = SessionConfiguration(
//            SessionConfiguration.SESSION_REGULAR,
//            listOf(outputConfig),
//            mainExecutor,
//            cameraCaptureStateCallback)
//
//        cameraDevice.createCaptureSession(sessionConfig)

    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    private fun takePicture(imageReader: ImageReader) {
        val captureRequestBuilderTake = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//        val imageReader = ImageReader.newInstance(290, 420, ImageFormat.JPEG, 1)

//        imageReader.setOnImageAvailableListener({ reader ->
//            Log.i("Development", "image listener called")
//            reader.acquireNextImage().use { image ->
//                val buffer = image.planes[0].buffer
//                val bytes = ByteArray(buffer.remaining())
//                buffer.get(bytes)
//                saveImage(bytes)
//                Log.i("Development", "Image saved")
//            }
//        }, null)
        captureRequestBuilderTake.addTarget(imageReader.surface)

        val outputConfigReader = OutputConfiguration(imageReader.surface)

        val sessionConfig = SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
            listOf(outputConfigReader), ContextCompat.getMainExecutor(requireContext()), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    session.capture(captureRequestBuilderTake.build(), null, null)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Log.e("Development", "Capture session configuration failed")
                }
            })

        cameraDevice.createCaptureSession(sessionConfig)
    }

    private fun saveImage(file: ByteArray) {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(file)
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

            }
            imagePath = it.toString()
            Log.i("Development", "Image saved to $it")
        }

    }


    val cameraCaptureStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            session.setRepeatingRequest(captureRequestBuilder.build(), null, null)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.w("Development", "Camera configuration failed")
        }
    }
}
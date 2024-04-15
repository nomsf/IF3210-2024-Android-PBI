package com.example.myapplication.ui.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingsBinding
import kotlin.random.Random

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val connected: MutableLiveData<Boolean> = MutableLiveData(false)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainActivity = activity as? MainActivity

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = mainActivity?.getEmail()
        }

        val button = root.findViewById<Button>(R.id.send)
        val logoutButton = root.findViewById<Button>(R.id.logout_button)
        val saveButton = root.findViewById<Button>(R.id.save_transaction)
        val randomizeButton = root.findViewById<Button>(R.id.randomize_transaction)
        val content = root.findViewById<EditText>(R.id.content).text.toString()
        val emails = arrayOf(mainActivity?.getEmail())

//        settingsViewModel.broadcastedConnection.observe(viewLifecycleOwner, Observer { value ->
//            Log.d("tes1", "onCreateView: ${value}")
//            connected.value = value
//        })

//        mainActivity?.getConnectionStatus()

        button.setOnClickListener {
            Log.d("MyFragment", "Received new value: ${connected.value}")
                if (mainActivity?.getConnectionStatus() == true) {
                    println("sending")
                    composeEmail(emails, "Latest transaction", content)
                } else {
                    val connectionLostBuilder: AlertDialog.Builder = AlertDialog.Builder(it.context)
                    connectionLostBuilder
                        .setMessage("No connection")
                        .setTitle("Can't send email")

                    val connectionLostDialog: AlertDialog = connectionLostBuilder.create()
                    connectionLostDialog.show()
                }
        }

        logoutButton.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.logout()
        }

        saveButton.setOnClickListener {

        }

        val titleList = listOf("Makan", "Minum", "Date", "Halo", "Cincin")

        val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        randomizeButton.setOnClickListener {
            val map = mapOf("title" to titleList[Random.nextInt(0, 4 + 1)],
                            "nominal" to Random.nextInt(1,1000000 + 1))
            val intent = Intent("com.example.myapplication.ui.transactions.RANDOMIZE")
            intent.putExtra("map", HashMap(map))
            localBroadcastManager.sendBroadcast(intent)
        }

        return root
    }

    private fun composeEmail(addresses: Array<String?>, subject: String, content: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

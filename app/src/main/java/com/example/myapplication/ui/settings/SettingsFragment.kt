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
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingsBinding

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

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val button = root.findViewById<Button>(R.id.send)
        val subject = root.findViewById<EditText>(R.id.subject).text.toString()
        val content = root.findViewById<EditText>(R.id.content).text.toString()
        val email = root.findViewById<EditText>(R.id.send_to).text.toString()
        val emails = arrayOf(email)

//        settingsViewModel.broadcastedConnection.observe(viewLifecycleOwner, Observer { value ->
//            Log.d("tes1", "onCreateView: ${value}")
//            connected.value = value
//        })

//        mainActivity?.getConnectionStatus()

        button.setOnClickListener {
            Log.d("MyFragment", "Received new value: ${connected.value}")
            val mainActivity = activity as? MainActivity
                if (mainActivity?.getConnectionStatus() == true) {
                    println("sending")
                    composeEmail(emails, subject, content)
                } else {
                    val connectionLostBuilder: AlertDialog.Builder = AlertDialog.Builder(it.context)
                    connectionLostBuilder
                        .setMessage("No connection")
                        .setTitle("Can't send email")

                    val connectionLostDialog: AlertDialog = connectionLostBuilder.create()
                    connectionLostDialog.show()
                }
        }

        return root
    }

    private fun composeEmail(addresses: Array<String>, subject: String, content: String) {
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

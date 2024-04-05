package com.example.myapplication.ui.transactions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentListTransactionBinding
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.adapter.TransactionAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.home.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListTransactionFragment : Fragment() {
    lateinit var idList: Array<Int>
    lateinit var titleList: Array<String>
    lateinit var dateList: Array<String>
    lateinit var amountList: Array<String>
    lateinit var categoryList: Array<String>
    lateinit var locationList: Array<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<TransactionEntity>

    private var _binding: FragmentListTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val transactionViewModel =
            ViewModelProvider(this).get(TransactionViewModel::class.java)
        _binding = FragmentListTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textListTransaction
        transactionViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        idList = arrayOf(
            1, 2
        )

        titleList = arrayOf(
            "aku", "adalah"
        )

        amountList = arrayOf(
            "anak", "gembala"
        )

        categoryList = arrayOf(
            "selalu", "riang"
        )

        locationList = arrayOf(
            "serta", "gembira"
        )

        dateList = arrayOf(
            "mamak", "bapak"
        )

        recyclerView = binding.recyclerViewTransactions
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<TransactionEntity>()
        getData()


        val addTransactionButton = root.findViewById<FloatingActionButton>(R.id.addTransactionButton)
        addTransactionButton.setOnClickListener {
            val addTransactionIntent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(addTransactionIntent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        for (i in titleList.indices) {
            val dataClass = TransactionEntity(idList[i], titleList[i], amountList[i], categoryList[i], locationList[i], dateList[i])
            this.dataList.add(dataClass)
        }
        recyclerView.adapter = TransactionAdapter(this.dataList)
    }
}
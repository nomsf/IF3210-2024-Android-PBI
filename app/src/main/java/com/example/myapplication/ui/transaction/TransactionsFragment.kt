package com.example.myapplication.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Transaction
import com.example.myapplication.data.TransactionAdapter

class TransactionsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private val transactionList = mutableListOf<Transaction>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_transactions, container, false)

        recyclerView = root.findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(transactionList)
        recyclerView.adapter = transactionAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simulate loading transactions from a data source
        loadTransactions()
    }

    private fun loadTransactions() {
        transactionViewModel.allTransactions.observe(viewLifecycleOwner, { transactions ->
            transactionList.clear()
            transactionList.addAll(transactions.map { Transaction(it.title, it.nominal) })
            transactionAdapter.notifyDataSetChanged()
        })
    }
}
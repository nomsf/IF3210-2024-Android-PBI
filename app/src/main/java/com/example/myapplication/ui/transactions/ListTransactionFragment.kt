package com.example.myapplication.ui.transactions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EditTransactionActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.TransactionAdapter
import com.example.myapplication.databinding.FragmentListTransactionBinding
import com.example.myapplication.entities.TransactionEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.myapplication.TransactionActivity


class ListTransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel
    private var _binding: FragmentListTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<TransactionEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerViewTransactions
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf()

        val addTransactionButton = root.findViewById<FloatingActionButton>(R.id.addTransactionButton)
        addTransactionButton.setOnClickListener {
            val addTransactionIntent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(addTransactionIntent)
        }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TransactionAdapter(dataList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object: TransactionAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // Toast.makeText(requireContext(), "You clicked on item no. $position", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), EditTransactionActivity::class.java)
                intent.putExtra("title", dataList[position].title)
                intent.putExtra("nominal", dataList[position].nominal)
                intent.putExtra("kategori", dataList[position].kategori)
                intent.putExtra("tanggal", dataList[position].tanggal)
                intent.putExtra("lokasi", dataList[position].lokasi)
                intent.putExtra("id", dataList[position].id)
                startActivity(intent)
            }

        })

        val textView: TextView = binding.textListTransaction

        val factory = TransactionViewModelFactory(requireActivity().application)
        transactionViewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)
        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            dataList.clear()
            // Menambahkan data dummy
            dataList.add(TransactionEntity(1, "Pembelian Buku", "100000000", "Pengeluaran", "Toko Buku", "2024-04-07"))
            dataList.add(TransactionEntity(2, "Makan Siang", "50000000", "Pengeluaran", "Restoran", "2024-04-06"))
            dataList.add(TransactionEntity(3, "Pengisian Bensin", "20000000", "Pengeluaran", "SPBU", "2024-04-05"))

            dataList.addAll(transactions)
            dataList.reverse()
            adapter.notifyDataSetChanged()
        }

        transactionViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.addTransactionButton.setOnClickListener {
            // Membuat Intent untuk membuka AddTransactionActivity
            val intent = Intent(requireContext(), TransactionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up bindings
        _binding = null
    }
}
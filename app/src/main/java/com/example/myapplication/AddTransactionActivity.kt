package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAddTransactionBinding
import com.example.myapplication.repository.TransactionRepository
import com.example.myapplication.ui.transactions.TransactionViewModel

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private var transactionViewModel = TransactionViewModel(application)
    private lateinit var transactionRepository: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()


    }

    fun onAddTransactionSuccess() {

    }

    fun onAddTransactionFailure() {

    }
}
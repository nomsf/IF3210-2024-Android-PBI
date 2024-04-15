package com.example.myapplication.adapter

import com.example.myapplication.R
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.entities.TransactionEntity
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val dataList: ArrayList<TransactionEntity>): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transactions, parent, false)
        return TransactionViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return (dataList.size)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.transactionTitle.text = currentItem.title
        holder.transactionDate.text = currentItem.tanggal
        holder.transactionAmount.text = currentItem.nominal
        holder.transactionCategory.text = currentItem.kategori
        holder.transactionLocation.text = currentItem.lokasi
    }

    class TransactionViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val transactionTitle:TextView = itemView.findViewById(R.id.transaction_title)
        val transactionDate:TextView = itemView.findViewById(R.id.transaction_date)
        val transactionAmount:TextView = itemView.findViewById(R.id.transaction_amount)
        val transactionCategory:TextView = itemView.findViewById(R.id.transaction_category)
        val transactionLocation:TextView = itemView.findViewById(R.id.transaction_location)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }
}
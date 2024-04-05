package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.util.PercentageConverter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val piechart = root.findViewById<PieChart>(R.id.pie_chart)

        // temporary datas
        val dataValues = arrayOf(10000, 20000, 30000, 40000, 100000, 50000)
        val labels = arrayOf("Pembelian", "Pengeluaran", "Pengeluaran", "Pembelian", "Pengeluaran", "Pengeluaran")

        val labelMap = mutableMapOf<String, Float>()
        for (i in labels.indices) {
            val label = labels[i]
            val value = dataValues[i].toFloat()
            labelMap[label] = labelMap.getOrDefault(label, 0f) + value
        }

        val total = dataValues.sum()
        val entries = labelMap.map { (label, value) -> PieEntry(value/total, label + ": ${value}") }

        val piedataset = PieDataSet(entries, "Pie Chart Data")
        piedataset.colors = ColorTemplate.COLORFUL_COLORS.asList()
        piedataset.valueTextSize = 12f

        val data = PieData(piedataset)

        piechart.data = data

        val dataSet = piechart.data.dataSets[0]
        dataSet.valueFormatter = PercentageConverter()

        piechart.invalidate()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
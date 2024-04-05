package com.example.myapplication.util

import com.github.mikephil.charting.formatter.ValueFormatter

class PercentageConverter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value * 100}%"
    }
}
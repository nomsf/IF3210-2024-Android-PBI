package com.example.myapplication.model.bill

import com.google.gson.annotations.SerializedName

data class BillResponse (
    @SerializedName("items")
    val items: Items
) {
    data class Items (
        @SerializedName("items")
        val items: List<Item>
    ) {
        data class Item (
            @SerializedName("name")
            val name: String,
            @SerializedName("qty")
            val quantity: Int,
            @SerializedName("price")
            val price: Double
        )
    }
}
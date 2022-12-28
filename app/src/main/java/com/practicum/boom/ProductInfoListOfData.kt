package com.practicum.boom

import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductInfoListOfData (
    @SerializedName("shopping_results")

    @Expose
    val productList: List<Product>? = null
)
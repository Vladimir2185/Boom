package com.practicum.boom.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(tableName = "product_list")
data class Product(
    @SerializedName("thumbnail")
    @Expose
    val imageURL: String,

    @SerializedName("extracted_price")
    @Expose
    val price: Float,

    @SerializedName("price")
    @Expose
    val priceWithSymbol: String,

    @SerializedName("title")
    @Expose
    val title: String,
    @PrimaryKey
    @SerializedName("product_id")
    @Expose
    val productID: String,

    var type:String="general",
    var favorite: Boolean = false,
    var rating: Float = 0.0f,
    var sale: Int = NO_SALE

) {
    companion object {
        const val NO_SALE = 0
    }
}


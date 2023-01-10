package com.practicum.boom.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopInfo_list")
data class ShopInfo(
    @PrimaryKey
    val id: String,
    var title: String = "",
    var shortDescription: String = "",
    var longDescription: String = "",
    var url: String = ""
) {}
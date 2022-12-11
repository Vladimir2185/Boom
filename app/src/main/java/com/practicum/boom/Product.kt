package com.practicum.boom

data class Product(
    val imageURL: String,
    val price: Int,
    val favorite: Boolean = false,
    val sale: Int = NO_SALE

) {
    companion object {
        const val NO_SALE = 0
    }
}


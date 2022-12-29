package com.practicum.boom.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.boom.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_list WHERE type == :typeOfProduct ORDER BY sale DESC")
    fun getProductList(typeOfProduct: String): LiveData<List<Product>>

    @Query("SELECT * FROM product_list WHERE productID==:prodID ")
    fun getProductItem(prodID: String): LiveData<Product>

    @Query("UPDATE product_list SET favorite=:favorProduct WHERE productID==:prodID")
    fun updateProduct(favorProduct: Boolean, prodID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductList(productList: List<Product>)


}

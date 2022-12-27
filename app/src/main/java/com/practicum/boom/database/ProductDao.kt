package com.practicum.boom.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.boom.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_list WHERE type == :typeOfProduct ")
    fun getProductList(typeOfProduct: String): LiveData<List<Product>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductList(productList: List<Product>)
}

package com.practicum.boom.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.boom.api.Product
import com.practicum.boom.api.ShopInfo

@Dao
interface ShopInfoDao {
    @Query("SELECT * FROM shopInfo_list WHERE longDescription != 'NOT_MAIN' ORDER BY url ASC")
    fun getAllShopInfoList(): LiveData<List<ShopInfo>>

    @Query("SELECT * FROM shopInfo_list WHERE shortDescription == :typeOfInfo  ORDER BY url ASC")
    fun getInfoByType(typeOfInfo: String): LiveData<List<ShopInfo>>


    @Insert(entity = ShopInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertShopInfoList(shopInfoList: List<ShopInfo>)
}
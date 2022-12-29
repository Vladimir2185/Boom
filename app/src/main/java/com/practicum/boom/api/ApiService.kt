package com.practicum.boom.api

import com.practicum.boom.MainViewModel.Companion.type
import com.practicum.boom.ProductInfoListOfData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    fun getProductInfo(
        @Query(QUERY_PARAM_Q) q: String =type,
        @Query(QUERY_PARAM_API_KEY) api_key: String = "6d4e3257b39c07d92be462d0f3f9bf04b59a56e97607314271d9f017760075ea",
        @Query(QUERY_PARAM_ENGINE) engine: String = "google",
        @Query(QUERY_PARAM_NUM) num: Int = 50,
        @Query(QUERY_PARAM_TBM) tbm: String = "shop",
        @Query(QUERY_PARAM_DEVICE) device: String = "desktop"

    ): Single<ProductInfoListOfData>


    companion object {

        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_Q = "q"
        private const val QUERY_PARAM_NUM = "num"
        private const val QUERY_PARAM_TBM = "tbm"
        private const val QUERY_PARAM_ENGINE = "engine"
        private const val QUERY_PARAM_DEVICE = "device"



    }
}
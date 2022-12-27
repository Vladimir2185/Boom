package com.practicum.boom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.boom.api.ApiFactory
import com.practicum.boom.api.ApiService
import com.practicum.boom.database.AppDatabase
import com.practicum.boom.fragments.FragmentHome1
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


open class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val type = "shoes"
    }
    val liveScrollLock: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val compositeDisposable = CompositeDisposable()
    private val db = AppDatabase.getInstance(application)

    var productArray = listOf<Product>()

    init {
        loadData()
    }

    fun getListOfProducts(typeOfProduct: String): LiveData<List<Product>> {
        return db.productInfoDao().getProductList(typeOfProduct)

    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getProductInfo()
            .subscribeOn(Schedulers.io())
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.productList?.let {
                productArray = it }
                Log.i("test4", "size " + productArray.size)
                for (p in productArray) {
                    p.type = type
                    Log.i("test4", "on success " + p.title)
                }
                db.productInfoDao().insertProductList(productArray)
            },
                {
                    Log.i("test4", "on throw " + it.toString())
                })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
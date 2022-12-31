package com.practicum.boom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.boom.api.ApiFactory
import com.practicum.boom.database.AppDatabase
import io.reactivex.Flowable.empty
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


open class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val type ="tires" //"tires"
    }

    val liveScrollStatus: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    private val compositeDisposable = CompositeDisposable()
    private val db = AppDatabase.getInstance(application)

    var productArray = listOf<Product>()

    init {
        loadData()
    }
    fun getAllListOfProducts(): LiveData<List<Product>> {
        return db.productInfoDao().getAllProductList()
    }

    fun getListOfProductsByType(typeOfProduct: String): LiveData<List<Product>> {
        return db.productInfoDao().getProductListByType(typeOfProduct)
    }

    fun getItemProduct(prodID: String): LiveData<Product> {
        return db.productInfoDao().getProductItem(prodID)
    }

    fun productUpdate(favorProduct: Boolean, prodID: String) {
        val disposable2 = Observable.just(Unit)
            .subscribeOn(Schedulers.io())
            .subscribe({
                return@subscribe db.productInfoDao().updateProduct(favorProduct, prodID)
            }, {

            })
        compositeDisposable.add(disposable2)

    }

    private fun loadData() {


        val disposable = ApiFactory.apiService.getProductInfo()
            .subscribeOn(Schedulers.io())
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.productList?.let {
                    productArray = it
                }
                Log.i("test4", "size " + productArray.size)
                for (p in productArray) {
                    p.type = type
                    p.rating = ((1..50).random()).toFloat() / 10
                    p.sale = (30..70).random()

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
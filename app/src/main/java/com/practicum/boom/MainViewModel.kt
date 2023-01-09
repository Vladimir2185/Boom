package com.practicum.boom

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.practicum.boom.api.ApiFactory
import com.practicum.boom.api.Product
import com.practicum.boom.database.AppDatabase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


open class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val type = "tires" //"tires"
        var urlFB = ""
    }

    val liveScrollStatus: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }


    private val compositeDisposable = CompositeDisposable()
    private val db = AppDatabase.getInstance(application)
    private val dbFB = Firebase.firestore
    private val storageFB = Firebase.storage
    private val storageRef = storageFB.reference
    var gsReference = storageFB.getReferenceFromUrl("gs://boom-3e705.appspot.com/sale/sale2.jpeg")

    var productArray = listOf<Product>()

    init {
        loadData()
    }

    fun readFromFirebase() {
        dbFB.collection("sale")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    Log.i("test4", "${document.id} => ${document.data["url"]}")
                    gsReference = storageFB.getReferenceFromUrl(document.data["url"].toString())
                    // Log.i("test4" ,"${document.id} => ${gsReference.downloadUrl.}")
                    gsReference.downloadUrl
                        .addOnSuccessListener { result ->
                            urlFB = result.toString()
                            Log.i("test4", "" + result)
                            Log.i("test4", "" + urlFB)
                            //Log.i("test4" ,""+gsReference.downloadUrl)

                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)

            }

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
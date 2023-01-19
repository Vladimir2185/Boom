package com.practicum.boom.myCustomClasses

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.practicum.boom.api.ShopInfo
import com.practicum.boom.boom.home.DetailProductInfoFragment
import com.practicum.boom.boom.home.DetailSaleFragment


abstract class GeneralAdapterRV(
    private val context: Context,
    protected val NUMBER_OF_PROMO: Int
) :
    RecyclerView.Adapter<GeneralAdapterRV.CustomViewHolder>() {
    var shopInfoList = listOf<ShopInfo>()
        set(value) {
            field = value
            notifyItemChanged(positionUpdate, Unit)
        }
    var productList = listOf<Product>()
        set(value) {
            field = value
            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
            imageButtonUpdate?.let {
                favoriteSwitch(
                    productList[positionUpdate - NUMBER_OF_PROMO],
                    it
                )
            }
        }
    private var positionUpdate = 0
    private var imageButtonUpdate: ImageButton? = null
    protected val marginBetweenIcon = 8
    protected val screenInfo = ScreenInfo()
    private var detailInfo: DetailProductInfoFragment? = null
    private var detailSale: DetailSaleFragment? = null

    var onFragmentListener: OnFragmentListener? = null

    interface OnFragmentListener {
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
        fun onPromoStart(holder: CustomViewHolder)
    }

    protected fun favoriteSwitch(product: Product, imageButton: ImageButton) {
        if (product.favorite == false)
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        else
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        imageButtonUpdate = null
    }

    protected fun onFavoriteClick(offsetPosition: Int, imageButton: ImageButton) {
        imageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val product = productList[offsetPosition]
                onFragmentListener?.onFavoriteSwitch(!product.favorite, product.productID)
                positionUpdate = offsetPosition + NUMBER_OF_PROMO
                imageButtonUpdate = imageButton
            }
        })
        favoriteSwitch(productList[offsetPosition], imageButton)
    }

    protected fun onDetailClick(offsetPosition: Int, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val product = productList[offsetPosition]
                if (detailInfo == null) {

                    detailInfo = DetailProductInfoFragment(offsetPosition, product)

                    detailInfo!!.setStyle(STYLE_NORMAL, R.style.Theme_Boom)
                    detailInfo!!.show(
                        (context as FragmentActivity).supportFragmentManager, "detailInfo"
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        detailInfo = null
                    }, (500))

                    detailInfo!!.onFavoriteClickListener =
                        object : DetailProductInfoFragment.OnFavoriteClickListener {
                            override fun onFavorClick(position: Int, imageButton: ImageButton) {
                                onFavoriteClick(position, imageButton)
                            }

                            override fun onFavoriteSwitch(
                                product: Product,
                                imageButton: ImageButton
                            ) {
                                favoriteSwitch(product, imageButton)
                            }
                        }
                }
            }
        })
    }

    protected fun onCategoryClick(offsetPosition: Int, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val shopInfo = shopInfoList[offsetPosition]
                if (detailSale == null) {
                    detailSale = DetailSaleFragment(offsetPosition, shopInfo)
                    detailSale!!.setStyle(STYLE_NORMAL, R.style.Theme_Boom)
                    detailSale!!.show((context as FragmentActivity).supportFragmentManager, "Tag")
                    Handler(Looper.getMainLooper()).postDelayed({
                        detailSale = null
                    }, (500))
                }
            }
        })


    }

    protected fun sale(product: Product, textView: TextView) {

        if (product.sale < 50)
            textView.visibility = View.INVISIBLE
        else {
            textView.visibility = View.VISIBLE
            textView.text = " -${product.sale}% "
        }
    }


    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    abstract override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    abstract override fun getItemCount(): Int


}
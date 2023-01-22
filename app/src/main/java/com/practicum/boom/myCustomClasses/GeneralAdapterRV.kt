package com.practicum.boom.myCustomClasses

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.practicum.boom.api.ShopInfo


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
    private var lockClick = false


    var onFragmentListener: OnFragmentListener? = null

    interface OnFragmentListener {
        fun onFavoriteSwitchInterface(favorProduct: Boolean, prodID: String)
        fun onPromoStartInterface(holder: CustomViewHolder)
        fun onDetailClickInterface(offsetPosition: Int, product: Product)
        fun onCategoryClickInterface(offsetPosition: Int, shopInfo: ShopInfo)
        fun onTypeClickInterface(offsetPosition: Int, shopInfo: ShopInfo)

    }

    protected fun favoriteSwitch(product: Product, imageButton: ImageButton) {
        if (product.favorite == false)
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        else
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        imageButtonUpdate = null
    }

    fun onFavoriteClick(offsetPosition: Int, imageButton: ImageButton) {
        imageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val product = productList[offsetPosition]
                onFragmentListener?.onFavoriteSwitchInterface(!product.favorite, product.productID)
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
                if (!lockClick) {
                    lockClick = true
                    onFragmentListener?.onDetailClickInterface(offsetPosition, product)

                    Handler(Looper.getMainLooper()).postDelayed({
                        lockClick = false
                    }, (500))
                }
            }
        })
    }

    protected fun onCategoryClick(offsetPosition: Int, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val shopInfo = shopInfoList[offsetPosition]
                if (!lockClick) {
                    lockClick = true
                    onFragmentListener?.onCategoryClickInterface(offsetPosition, shopInfo)
                    Handler(Looper.getMainLooper()).postDelayed({
                        lockClick = false
                    }, (500))
                }
            }
        })
    }

    protected fun onTypeClick(offsetPosition: Int, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val shopInfo = shopInfoList[offsetPosition]
                if (!lockClick) {
                    lockClick = true
                    onFragmentListener?.onTypeClickInterface(offsetPosition, shopInfo)
                    Handler(Looper.getMainLooper()).postDelayed({
                        lockClick = false
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

    override fun onViewAttachedToWindow(holder: CustomViewHolder) {

        if (holder.absoluteAdapterPosition == 0 && NUMBER_OF_PROMO > 0) {
            onFragmentListener?.onPromoStartInterface(holder)
        }

        super.onViewAttachedToWindow(holder)
    }

    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    abstract override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    abstract override fun getItemCount(): Int


}
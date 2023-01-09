package com.practicum.boom.myCustomClasses

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.practicum.boom.home.DetailProductInfoFragment
import com.practicum.boom.home.best.ProductAdapter.Companion.NUMBER_OF_PROMO

abstract class GeneralAdapterRV(
    private val context: Context,
) :
    RecyclerView.Adapter<GeneralAdapterRV.CustomViewHolder>() {

    var productList = listOf<Product>()
        set(value) {
            field = value
            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
            imageButtonUpdate?.let { favoriteSwitch(productList[positionUpdate - 1], it) }
        }
    private var positionUpdate = 0
    private var imageButtonUpdate: ImageButton? = null
    protected val marginBetweenIcon = 8
    protected val screenInfo = ScreenInfo()

    var onFragmentListener: OnFragmentListener? = null

    interface OnFragmentListener {
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
        fun onPromoStart(holder: CustomViewHolder)
    }


    fun onFavoriteClick(position: Int, imageButton: ImageButton) {
        imageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val product = productList[position]
                onFragmentListener?.onFavoriteSwitch(!product.favorite, product.productID)
                positionUpdate = position + NUMBER_OF_PROMO
                imageButtonUpdate = imageButton
            }
        })
        favoriteSwitch(productList[position], imageButton)
    }

    protected fun onDetailClick(position: Int, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val product = productList[position]
                val detailInfo = DetailProductInfoFragment(position, product)
                detailInfo.show((context as FragmentActivity).supportFragmentManager, "Tag")

                detailInfo.onFavoriteClickListener =
                    object : DetailProductInfoFragment.OnFavoriteClickListener {
                        override fun onFavorClick(position: Int, imageButton: ImageButton) {
                            onFavoriteClick(position, imageButton)
                        }

                        override fun onFavoriteSwitch(product: Product, imageButton: ImageButton) {
                            favoriteSwitch(product, imageButton)
                        }
                    }
            }
        })
    }

    protected fun favoriteSwitch(product: Product, imageButton: ImageButton) {
        if (product.favorite == false)
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        else
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        imageButtonUpdate = null
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
        if (holder.absoluteAdapterPosition == 0)
            (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                (marginBetweenIcon * screenInfo.screenDensity).toInt()
        super.onViewAttachedToWindow(holder)
    }

    open class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    abstract override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    abstract override fun getItemCount(): Int


}
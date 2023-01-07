package com.practicum.boom.myCustomClasses

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.practicum.boom.home.DetailProductInfoFragment
import com.practicum.boom.home.best.ProductAdapter

abstract class CustomAdapterRV(
    private val context: Context,
) :
    RecyclerView.Adapter<CustomAdapterRV.CustomViewHolder>() {

    var productList = listOf<Product>()
        set(value) {
            field = value
            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
        }
    private var positionUpdate = 0


    var onFragmentListener: OnFragmentListener? = null

    interface OnFragmentListener {
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
    }

    protected fun onFavoriteClick(holder: CustomViewHolder, product: Product, view: View) {
        view.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
                onFragmentListener?.onFavoriteSwitch(!product.favorite, product.productID)
                positionUpdate = holder.absoluteAdapterPosition
            }
        })
    }

    protected fun onDetailClick(product: Product, view: View) {
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val detailInfo = DetailProductInfoFragment(product)
                detailInfo.show((context as FragmentActivity).supportFragmentManager, "Tag")
            }
        })
    }

    protected fun favoriteSwitch(product: Product, imageButton: ImageButton) {
        if (product.favorite)
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
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
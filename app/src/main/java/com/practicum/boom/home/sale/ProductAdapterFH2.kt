package com.practicum.boom.home.sale

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.boom.MainViewModel.Companion.urlFB
import com.practicum.boom.R
import com.practicum.boom.home.best.ProductAdapterFH1
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_sale.view.*

class ProductAdapterFH2(
    private val context: Context,
) :
    GeneralAdapterRV(context) {

    companion object {
        const val NUMBER_OF_PROMO = 0
        const val MAX_POOL_SIZE = 5

        const val VIEW_TYPE_PROMO = 0
    }


    class SaleViewHolder(itemView: View) : CustomViewHolder(itemView) {
        val ivSale = itemView.imageViewSale_itemSale
    }

    override fun getItemViewType(position: Int): Int {

        return if (NUMBER_OF_PROMO > 0 && position in 0 until NUMBER_OF_PROMO)
            VIEW_TYPE_PROMO
        else
            VIEW_TYPE_PROMO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        //Log.i("test3", "onCreateViewHolder " + ++count)

        val layout = when (viewType) {
            VIEW_TYPE_PROMO -> R.layout.item_sale
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view =
            LayoutInflater.from(context).inflate(layout, parent, false)

        return SaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val offsetPosition = position - NUMBER_OF_PROMO
        holder as SaleViewHolder
        with(holder) {
            if (offsetPosition >= 0) {
                //val sale = adapterList[offsetPosition]
                Picasso.get()
                    //.load(R.drawable.sale1)
                    .load(urlFB)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivSale)
            }
        }
    }

    override fun getItemCount(): Int {
        return (5 + NUMBER_OF_PROMO)
    }
}
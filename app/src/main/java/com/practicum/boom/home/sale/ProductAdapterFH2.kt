package com.practicum.boom.home.sale

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.boom.R
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_sale.view.*


class ProductAdapterFH2(
    private val context: Context, NUMBER_OF_PROMO: Int,
) :
    GeneralAdapterRV(context, NUMBER_OF_PROMO) {

    companion object {

        const val MAX_POOL_SIZE = 5

        const val VIEW_TYPE_PROMO = 0
        const val VIEW_TYPE_MAIN = 1
    }


    class SaleViewHolder(itemView: View) : CustomViewHolder(itemView) {
        val ivSale = itemView.imageViewSale_itemSale
        val title = itemView.textViewTitle
        val shortDescr = itemView.textViewShortDescription
        val headlineColor = itemView.textViewColorTop
    }

    override fun getItemViewType(position: Int): Int {

        return if (NUMBER_OF_PROMO > 0 && position in 0 until NUMBER_OF_PROMO)
            VIEW_TYPE_PROMO
        else
            VIEW_TYPE_MAIN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        //Log.i("test3", "onCreateViewHolder " + ++count)

        val layout = when (viewType) {
            VIEW_TYPE_PROMO -> R.layout.item_promo
            VIEW_TYPE_MAIN -> R.layout.item_sale

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
                val shopInfo = shopInfoList[offsetPosition]

                title.text = shopInfo.title
                shortDescr.text = shopInfo.shortDescription

                Picasso.get()
                    .load(shopInfo.url)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivSale)
                val disposable = Observable.just(Unit)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val bitmap = Picasso.get()
                            .load(shopInfo.url)
                            .error(android.R.drawable.ic_menu_report_image)
                            .get()
                        headlineColor.setBackgroundColor(headlineSameColor(bitmap))

                    }, {
                    })

            }
        }
    }

    private fun headlineSameColor(bitmap: Bitmap): Int {
        val pixel = bitmap.getPixel(0, 0)
        val red: Int = Color.red(pixel)
        val green: Int = Color.green(pixel)
        val blue: Int = Color.blue(pixel)

        return Color.rgb(red, green, blue)
    }

    override fun getItemCount(): Int {
        return (shopInfoList.size + NUMBER_OF_PROMO)
    }

    override fun onViewAttachedToWindow(holder: CustomViewHolder) {

        if (holder.absoluteAdapterPosition == 0 && NUMBER_OF_PROMO > 0) {
            onFragmentListener?.onPromoStart(holder)
        }
        super.onViewAttachedToWindow(holder)
    }
}
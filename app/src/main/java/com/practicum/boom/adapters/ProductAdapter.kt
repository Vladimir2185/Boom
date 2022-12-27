package com.practicum.boom.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.Product
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.view.*

class ProductAdapter(
    private val context: Context,
    private val screenInfo: ScreenInfo,
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var onFragmentClickListener: OnFragmentClickListener? = null
    var productList = listOf<Product>()
    private var i = 0
    private var count = 0

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct = itemView.imageView_itemProduct
        val cvInner1 = itemView.cardInner_itemProduct
        val cv1 = itemView.root_item_product
        val cl1 = itemView.conLayout_itemProduct
        val button = itemView.button_itemProduct
        val textViewPrice=itemView.textViewPrice_itemProduct
        val textViewSale=itemView.textViewSale_itemProduct
        val imageButtonFavorite=itemView.imageButtonFavorite_itemProduct


    }

    interface OnFragmentClickListener {
        fun onFragmentClick()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_product_info, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        with(holder) {
            //Log.i("test", "position"+bindingAdapterPosition)
            itemView.setOnClickListener() {
                onFragmentClickListener?.onFragmentClick()
                // notifyDataSetChanged()
            }
            if (position == 0) {
                button.visibility = View.GONE
                textViewPrice.visibility = View.GONE
                textViewSale.visibility = View.GONE
                imageButtonFavorite.visibility = View.GONE

                Picasso.get() //загрузка изображений с помощью Picasso
                    //.load(R.drawable.christmas_trees)
                    //.load(file)
                    .load(R.drawable.promo01)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivProduct)
            } else if (position > 0) {
                button.text = "position " + position
                textViewPrice.text=productList[position].priceWithSymbol
                textViewSale.text=" -65% "

                fragment1LayoutDrawing(holder)
            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return (productList.size + 1)//
    }


    private fun fragment1LayoutDrawing(holder: ProductViewHolder) {
        count++
        with(holder) {

            i++
            (cv1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 16
            if (i == 1) {
                (cvInner1.layoutParams as ViewGroup.MarginLayoutParams).marginStart = -35
                (cl1.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 35

            } else if (i == screenInfo.columnCount()) {
                (cvInner1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = -35
                (cl1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 35
                i = 0
            }
            cv1.layoutParams.height = screenInfo.heightOfProductIcon()

            Picasso.get() //загрузка изображений с помощью Picasso
                //.load(R.drawable.christmas_trees)
                //.load(file)
                .load(productList[position].imageURL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivProduct)
        }
    }
}
package com.practicum.boom.adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.Product
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.*
import kotlinx.android.synthetic.main.item_product_info.view.*

class ProductAdapter(
    private val context: Context,
    private val screenInfo: ScreenInfo,
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var onFragmentClickListener: OnFragmentClickListener? = null
    var productList = listOf<Product>()
        set(value) {
            field = value

            notifyItemChanged(positionUpdate, Unit)
        }
    private var i = 0
    private var count = 0
    private val numOfPromo = 1
    private var positionUpdate = 0


    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct = itemView.imageView_itemProduct
        val cl1 = itemView.conLayout_itemProduct
        val button = itemView.button_itemProduct
        val textViewPrice = itemView.textViewPrice_itemProduct
        val textViewSale = itemView.textViewSale_itemProduct
        val imageButtonFavorite = itemView.imageButtonFavorite_itemProduct
        val guideline = itemView.guideline_itemProduct


    }

    interface OnFragmentClickListener {
        fun onFragmentClick()
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_product_info, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val offsetPosition = position - numOfPromo
        with(holder) {
            itemView.imageButtonFavorite_itemProduct.setOnClickListener(object :
                View.OnClickListener {
                override fun onClick(v: View?) {
                    onFragmentClickListener?.onFavoriteSwitch(
                        !productList[offsetPosition].favorite,
                        productList[offsetPosition].productID
                    )

                    positionUpdate = offsetPosition + numOfPromo
                }

            })

            if (position == 0) {
                button.visibility = View.GONE
                textViewPrice.visibility = View.GONE
                textViewSale.visibility = View.GONE
                imageButtonFavorite.visibility = View.GONE
                guideline.setGuidelinePercent(1f)

                Picasso.get() //загрузка изображений с помощью Picasso
                    //.load(R.drawable.christmas_trees)
                    //.load(file)
                    .load(R.drawable.promo01)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivProduct)
            } else if (position > 0) {
                button.text = "position " + (offsetPosition)
                textViewPrice.text = productList[offsetPosition].priceWithSymbol
                textViewSale.text = " -65% "

                favoriteSwitch(holder, offsetPosition)
                fragment1LayoutDrawing(holder, offsetPosition)

            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return (productList.size + numOfPromo)
    }

    private fun favoriteSwitch(holder: ProductViewHolder, offsetPosition: Int) {
        with(holder) {
            if (productList[offsetPosition].favorite)
                imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun fragment1LayoutDrawing(holder: ProductViewHolder, offsetPosition: Int) {
        count++
        with(holder) {

//            i++
//            (cv1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 16
//            if (i == 1) {
//                (cvInner1.layoutParams as ViewGroup.MarginLayoutParams).marginStart = -35
//                (cl1.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 35
//
//            } else if (i == screenInfo.columnCount()) {
//                (cvInner1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = -35
//                (cl1.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 35
//                i = 0
//            }
//            cv1.layoutParams.height = screenInfo.heightOfProductIcon()

            Picasso.get()
                .load(productList[offsetPosition].imageURL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivProduct)

            ivProduct.shapeAppearanceModel= ShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED,50.0f)
                .build()
        }
    }
}
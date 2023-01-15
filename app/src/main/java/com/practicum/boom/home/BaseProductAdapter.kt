package com.practicum.boom.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.R

import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*

class BaseProductAdapter(
    private val context: Context, NUMBER_OF_PROMO: Int,
) :
    GeneralAdapterRV(context, NUMBER_OF_PROMO) {

    private val HIGHT_OF_PRODUCT_ICON = 1.35
    private val cornerSize = 15f


    companion object {

        const val MAX_POOL_SIZE = 5

        const val VIEW_TYPE_PROMO = 0
        const val VIEW_TYPE_UNEVEN = 1
        const val VIEW_TYPE_EVEN = 2
    }


    class ProductViewHolder(itemView: View) : CustomViewHolder(itemView) {
        val ivProduct = itemView.image_itemProduct
        val button = itemView.button_itemProduct
        val textViewPrice = itemView.textPrice_itemProduct
        val textViewSale = itemView.textSale_itemProduct
        val imageButtonFavorite = itemView.imageButtonFavorite_itemProduct
        val constraintLayout = itemView.conLayout_itemProduct
        val rating = itemView.textRating_itemProduct


    }

    override fun getItemViewType(position: Int): Int {


        return if (NUMBER_OF_PROMO > 0 && position in 0 until NUMBER_OF_PROMO)
            VIEW_TYPE_PROMO
        else if (position % 2 != 0)
            VIEW_TYPE_EVEN
        else
            VIEW_TYPE_UNEVEN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //Log.i("test3", "onCreateViewHolder " + ++count)

        val layout = when (viewType) {
            VIEW_TYPE_PROMO -> R.layout.item_promo
            VIEW_TYPE_UNEVEN -> R.layout.item_product
            VIEW_TYPE_EVEN -> R.layout.item_product
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view =
            LayoutInflater.from(context).inflate(layout, parent, false)
        return ProductViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val offsetPosition = position - NUMBER_OF_PROMO

        holder as ProductViewHolder
        with(holder) {
            // Log.i("test4", "position " + position)

            if (offsetPosition >= 0) {
                val product = productList[offsetPosition]

                button.text = "position " + (offsetPosition)
                textViewPrice.text = product.priceFormatted()

                onFavoriteClick(offsetPosition, imageButtonFavorite)
                onDetailClick(offsetPosition, constraintLayout)

                sale(product, textViewSale)
                rating.text = product.ratingDotToComma()
                fragment1LayoutDrawing(holder)

            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return if (productList.size > 0) productList.size + NUMBER_OF_PROMO else 0

    }

    private fun fragment1LayoutDrawing(holder: ProductViewHolder) {
        val offsetPosition = holder.absoluteAdapterPosition - NUMBER_OF_PROMO
        val product = productList[offsetPosition]
        with(holder) {
            with(constraintLayout) {

                if (offsetPosition % 2 == 0) {
                    layoutParams.height = screenInfo.heightOfProductIcon(HIGHT_OF_PRODUCT_ICON)

                    (layoutParams as ViewGroup.MarginLayoutParams).marginEnd =
                        (marginBetweenIcon * screenInfo.screenDensity).toInt()

                    ivProduct.shapeAppearanceModel = ShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * screenInfo.screenDensity
                        )
                        .build()
                } else {
                    layoutParams.height = screenInfo.heightOfProductIcon(HIGHT_OF_PRODUCT_ICON)

                    background = context.getDrawable(R.drawable.rounded_corner_left)

                    ivProduct.shapeAppearanceModel = ShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * screenInfo.screenDensity
                        )
                        .build()
                }

                Picasso.get()
                    .load(product.imageURL)
//                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivProduct)
            }
        }
    }


    override fun onViewAttachedToWindow(holder: CustomViewHolder) {

        if (holder.absoluteAdapterPosition == 0 && NUMBER_OF_PROMO > 0) {
            onFragmentListener?.onPromoStart(holder)

        }
        super.onViewAttachedToWindow(holder)
    }
}
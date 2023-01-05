package com.practicum.boom.home.best


import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.api.Product
import com.practicum.boom.R
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.MainViewModel
import com.practicum.boom.home.DetailProductInfoFragment
import com.practicum.boom.home.promo.Promo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.view.*

open class ProductAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var productList = listOf<Product>()
        set(value) {
            field = value

            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
        }
    var onFragmentListener: OnFragmentListener? = null

    private val HIGHT_OF_PRODUCT_ICON = 1.35
    private var positionUpdate = 0
    private val cornerSize = 15f
    private val marginBetweenIcon = 8
    private val screenInfo = ScreenInfo()
    private val promo = Promo()


    companion object {
        const val NUMBER_OF_PROMO = 1
        const val MAX_POOL_SIZE = 5

        const val VIEW_TYPE_PROMO = 0
        const val VIEW_TYPE_UNEVEN = 1
        const val VIEW_TYPE_EVEN = 2
    }

    interface OnFragmentListener {
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)

    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct = itemView.imageView_itemProduct
        val button = itemView.button_itemProduct
        val textViewPrice = itemView.textViewPrice_itemProduct
        val textViewSale = itemView.textViewSale_itemProduct
        val imageButtonFavorite = itemView.imageButtonFavorite_itemProduct
        val constraintLayout = itemView.conLayout_itemProduct
        val rating = itemView.textRating_itemProduct


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //Log.i("test3", "onCreateViewHolder " + ++count)

        val layout = when (viewType) {
            VIEW_TYPE_PROMO -> R.layout.item_promo
            VIEW_TYPE_UNEVEN -> R.layout.item_product_info
            VIEW_TYPE_EVEN -> R.layout.item_product_info
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view =
            LayoutInflater.from(context).inflate(layout, parent, false)

        return ProductViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val offsetPosition = position - NUMBER_OF_PROMO
        with(holder) {
            // Log.i("test4", "position " + position)

            if (offsetPosition >= 0) {

                button.text = "position " + (offsetPosition)
                textViewPrice.text = productList[offsetPosition].priceFormatted()
                onDetailClick(holder, offsetPosition)
                onFavoriteClick(holder, offsetPosition)
                sale(holder, offsetPosition)
                rating.text = productList[offsetPosition].ratingDotToComma()
                favoriteSwitch(holder, offsetPosition)
                fragment1LayoutDrawing(holder, offsetPosition)

            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return (productList.size + NUMBER_OF_PROMO)
    }


    override fun getItemViewType(position: Int): Int {

        return if (NUMBER_OF_PROMO > 0 && position in 0 until NUMBER_OF_PROMO)
            VIEW_TYPE_PROMO
        else if (position % 2 != 0)
            VIEW_TYPE_EVEN
        else
            VIEW_TYPE_UNEVEN
    }

    private fun onDetailClick(holder: ProductViewHolder, offsetPosition: Int) {
        holder.constraintLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val detailInfo = DetailProductInfoFragment(productList[offsetPosition])
                detailInfo.show((context as FragmentActivity).supportFragmentManager, "Tag")
            }
        })
    }

    private fun onFavoriteClick(holder: ProductViewHolder, offsetPosition: Int) {
        with(holder) {
            imageButtonFavorite.setOnClickListener(object :
                View.OnClickListener {
                override fun onClick(v: View?) {

                    onFragmentListener?.onFavoriteSwitch(
                        !productList[offsetPosition].favorite,
                        productList[offsetPosition].productID
                    )
                    positionUpdate = offsetPosition + NUMBER_OF_PROMO
                }
            }
            )
        }
    }

    private fun sale(holder: ProductViewHolder, offsetPosition: Int) {
        with(holder) {
            if (productList[offsetPosition].sale < 50)
                textViewSale.visibility = View.INVISIBLE
            else {
                textViewSale.visibility = View.VISIBLE
                textViewSale.text = " -${productList[offsetPosition].sale}% "
            }
        }
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

        with(holder) {
            with(constraintLayout) {

                if (position % 2 != 0) {
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
                    .load(productList[offsetPosition].imageURL)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivProduct)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ProductViewHolder) {

        if (holder.absoluteAdapterPosition == 0 && NUMBER_OF_PROMO > 0) {
            promo.promoStart(holder, context)

            val promo2 = Promo()
            promo2.promoStart(holder, context)

        }

        super.onViewAttachedToWindow(holder)
    }


}
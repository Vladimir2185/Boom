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
import kotlinx.android.synthetic.main.fragment_home1.view.*
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

            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
        }
    private val numOfPromo = 1
    private var positionUpdate = 0
    private val cornerSize = 15f
    private val marginBetweenIcon = 8
    private var count = 0

    companion object {
        const val MAX_POOL_SIZE = 5

        const val VIEW_TYPE_PROMO = 0
        const val VIEW_TYPE_UNEVEN = 1
        const val VIEW_TYPE_EVEN = 2
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct = itemView.imageView_itemProduct
        val button = itemView.button_itemProduct
        val textViewPrice = itemView.textViewPrice_itemProduct
        val textViewSale = itemView.textViewSale_itemProduct
        val imageButtonFavorite = itemView.imageButtonFavorite_itemProduct
        val constraintLayout = itemView.conLayout_itemProduct


    }

    interface OnFragmentClickListener {
        fun onFragmentClick()
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        Log.i("test3", "onCreateViewHolder " + ++count)
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
        val offsetPosition = position - numOfPromo
        with(holder) {

            if (position > 0) {
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

                button.text = "position " + (offsetPosition)
                textViewPrice.text = productList[offsetPosition].priceWithSymbol
                if (productList[offsetPosition].sale < 40)
                    textViewSale.visibility = View.INVISIBLE
                //textViewSale.text = " -${productList[offsetPosition].sale}% "
                else
                    textViewSale.visibility = View.VISIBLE
                textViewSale.text = " -${productList[offsetPosition].sale}% "

                favoriteSwitch(holder, offsetPosition)
                fragment1LayoutDrawing(holder, offsetPosition)

            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return (productList.size + numOfPromo )
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0)
            VIEW_TYPE_PROMO
        else if (position % 2 != 0)
            VIEW_TYPE_EVEN
        else
            VIEW_TYPE_UNEVEN
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
                    layoutParams.height = screenInfo.heightOfProductIcon()

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
                    layoutParams.height = screenInfo.heightOfProductIcon()

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
}
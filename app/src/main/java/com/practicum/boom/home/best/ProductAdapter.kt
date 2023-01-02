package com.practicum.boom.home.best


import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.api.Product
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.view.*
import kotlinx.android.synthetic.main.item_promo.view.*
import java.util.concurrent.TimeUnit

open class ProductAdapter(
    private val context: Context,
    private val screenInfo: ScreenInfo,
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var productList = listOf<Product>()
        set(value) {
            field = value

            notifyItemChanged(positionUpdate, Unit)//Unit or Any() param gives NO Animation
        }
    var onFragmentListener: OnFragmentListener? = null

    private var positionUpdate = 0
    private val cornerSize = 15f
    private val marginBetweenIcon = 8


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
                textViewPrice.text = productList[offsetPosition].priceWithSymbol

                onFavoriteClick(holder, offsetPosition)
                sale(holder, offsetPosition)
                rating.text = dotToComma(productList[offsetPosition].rating)
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

        return if (position == 0)
            VIEW_TYPE_PROMO
        else if (position % 2 != 0)
            VIEW_TYPE_EVEN
        else
            VIEW_TYPE_UNEVEN
    }


    private fun onFavoriteClick(holder: ProductViewHolder, offsetPosition: Int) {
        with(holder) {
            itemView.imageButtonFavorite_itemProduct.setOnClickListener(object :
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
    private fun dotToComma(rating: Float): String {
        return rating.toString().replace('.', ',')
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

    override fun onViewAttachedToWindow(holder: ProductViewHolder) {

        if (holder.absoluteAdapterPosition == 0) {
            promo.promoStart(holder)
        }

        super.onViewAttachedToWindow(holder)
    }


    private inner class Promo {

        private var timer: CountDownTimer? = null
        private val durationOfPromo: Long = 24


        fun promoStart(holder: ProductViewHolder) {
            snowFlakeAnimation(holder)
            timeUntilPromoEnd(holder)
        }

        private fun timeUntilPromoEnd(holder: ProductViewHolder) {
            val milliseconds: Long = TimeUnit.HOURS.toMillis(durationOfPromo)

            if (timer == null) {
                timer = object : CountDownTimer(milliseconds, 1000) {
                    override fun onTick(mSeconds: Long) {
                        val seconds = mSeconds / 1000
                        with(holder.itemView) {
                            textViewSecI_itemPromo.text = (seconds % 10).toString()
                            textViewSecII_itemPromo.text = (seconds % 60 / 10).toString()
                            textViewMinI_itemPromo.text = (seconds / 60 % 10).toString()
                            textViewMinII_itemPromo.text = (seconds / 60 % 60 / 10).toString()
                            textViewHourI_itemPromo.text = (seconds / 3600 % 10).toString()
                            textViewHourII_itemPromo.text = (seconds / 3600 % 24 / 10).toString()
                        }
                    }

                    override fun onFinish() {}
                }

                timer?.start()
            }

        }

        fun snowFlakeAnimation(holder: ProductViewHolder) {
            with(holder.itemView) {
                imageSnow1.layoutParams.width = (screenInfo.widthInPixels * 0.2).toInt()
                imageSnow2.layoutParams.width = (screenInfo.widthInPixels * 0.5).toInt()
                imageSnow3.layoutParams.width = (screenInfo.widthInPixels * 0.9).toInt()
                imageSnow4.layoutParams.width = (screenInfo.widthInPixels * 1.2).toInt()

                val snowflakeAnimation =
                    AnimationUtils.loadAnimation(context, R.anim.snowflake_animation)
                val snowflakeAnimation2 =
                    AnimationUtils.loadAnimation(context, R.anim.snowflake_animation2)
                val snowflakeAnimation3 =
                    AnimationUtils.loadAnimation(context, R.anim.snowflake_animation)
                val snowflakeAnimation4 =
                    AnimationUtils.loadAnimation(context, R.anim.snowflake_animation2)

                snowflakeAnimation2.startOffset = 1300
                snowflakeAnimation2.duration = 4500
                snowflakeAnimation3.startOffset = 200
                snowflakeAnimation3.duration = 5000
                snowflakeAnimation4.startOffset = 1100
                snowflakeAnimation4.duration = 4300

                // Подключаем анимацию к нужному View
                imageSnow1.startAnimation(snowflakeAnimation)
                imageSnow2.startAnimation(snowflakeAnimation2)
                imageSnow3.startAnimation(snowflakeAnimation3)
                imageSnow4.startAnimation(snowflakeAnimation4)
            }
        }
    }
}
package com.practicum.boom.adapters


import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.Product
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_info.view.*
import kotlinx.android.synthetic.main.item_promo.view.*
import java.util.concurrent.TimeUnit

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
    private var holderPromo: ProductViewHolder? = null
    private var promoID: Int? = null
    private var current0PosID: Int? = null
    private var lockPosID = false


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
        val rating = itemView.textRating_itemProduct
        val snow1 = itemView.imageSnow1
        val snow2 = itemView.imageSnow2
        val snow3 = itemView.imageSnow3
        val snow4 = itemView.imageSnow4


    }

    interface OnFragmentClickListener {
        fun onFragmentClick()
        fun onFavoriteSwitch(favorProduct: Boolean, prodID: String)
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
        val offsetPosition = position - numOfPromo
        with(holder) {
            // Log.i("test4", "position " + position)

            if (position == 0) {
                holderPromo = holder
                promoID = itemView.id
                lockPosID = false
                holderPromo?.let { snowflakeAnimation(it) }
                timeToPromoEnd()
            }

            if (position > 0) {

                if (!lockPosID && promoID != current0PosID && current0PosID != null) {
                    holderPromo?.let { snowflakeAnimation(it) }
                    lockPosID = true
                }
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

                if (productList[offsetPosition].sale < 50)
                    textViewSale.visibility = View.INVISIBLE
                else {
                    textViewSale.visibility = View.VISIBLE
                    textViewSale.text = " -${productList[offsetPosition].sale}% "
                }

                rating.text = dotToComma(productList[offsetPosition].rating)
                favoriteSwitch(holder, offsetPosition)
                fragment1LayoutDrawing(holder, offsetPosition)

            }
        }
    }

    //помещает в метод кол-во элентов массива productList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return (productList.size + numOfPromo)
    }

    fun current0PosID(id: Int) {
        current0PosID = id
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0)
            VIEW_TYPE_PROMO
        else if (position % 2 != 0)
            VIEW_TYPE_EVEN
        else
            VIEW_TYPE_UNEVEN
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


    var timer: CountDownTimer? = null

    private fun timeToPromoEnd() {
        val milliseconds: Long = TimeUnit.HOURS.toMillis(24)

        if (timer == null) {
            timer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(mSeconds: Long) {
                    val seconds = mSeconds / 1000
                    holderPromo?.itemView?.let {
                        it.textViewSecI_itemPromo.text = (seconds % 10).toString()
                        it.textViewSecII_itemPromo.text = (seconds % 60 / 10).toString()
                        it.textViewMinI_itemPromo.text = (seconds / 60 % 10).toString()
                        it.textViewMinII_itemPromo.text = (seconds / 60 % 60 / 10).toString()
                        it.textViewHourI_itemPromo.text = (seconds / 3600 % 10).toString()
                        it.textViewHourII_itemPromo.text = (seconds / 3600 % 24 / 10).toString()
                    }
                }

                override fun onFinish() {}
            }

            timer?.start()
        }

    }


    fun snowflakeAnimation(holder: ProductViewHolder) {
        with(holder) {
            snow1.layoutParams.width = (screenInfo.widthInPixels * 0.2).toInt()
            snow2.layoutParams.width = (screenInfo.widthInPixels * 0.5).toInt()
            snow3.layoutParams.width = (screenInfo.widthInPixels * 0.9).toInt()
            snow4.layoutParams.width = (screenInfo.widthInPixels * 1.2).toInt()

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
            snow1.startAnimation(snowflakeAnimation)
            snow2.startAnimation(snowflakeAnimation2)
            snow3.startAnimation(snowflakeAnimation3)
            snow4.startAnimation(snowflakeAnimation4)
        }
    }
}
package com.practicum.boom.boom.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.boom.home.BaseProductAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_for_hor_sv.view.*
import kotlinx.android.synthetic.main.lin_lay.view.*
import kotlinx.android.synthetic.main.main_home_fragment.*

class TypeCategoryAdapter(private val context: Context, NUMBER_OF_PROMO: Int) :
    BaseProductAdapter(context, NUMBER_OF_PROMO) {

    private val numHLayItems = 4
    private var unfolded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val layout = when (viewType) {
            VIEW_TYPE_PROMO -> R.layout.lin_lay
            VIEW_TYPE_UNEVEN -> R.layout.item_product
            VIEW_TYPE_EVEN -> R.layout.item_product
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view =
            LayoutInflater.from(context).inflate(layout, parent, false)

        Handler(Looper.getMainLooper()).postDelayed({
            linLayCategory(view, viewType)
        }, (50))

        return ProductViewHolder(view)
    }

    private fun linLayCategory(view: View, viewType: Int) {
        if (viewType == VIEW_TYPE_PROMO) {

            var item = 0
            val linLayV = view.linLayV_linLay

            var size = shopInfoList.size
            var numLines = (size -1)/ numHLayItems

            if (shopInfoList.size > numHLayItems * 2 && !unfolded) {
                numLines = 1
                size = numHLayItems * 2
            }

            for (horLay in 0..numLines) {
                val linLayH = LinearLayout(context)
                linLayH.orientation = LinearLayout.HORIZONTAL

                for (i in 1..numHLayItems) {

                    val linLayItem = LayoutInflater.from(context)
                        .inflate(R.layout.item_for_hor_sv, linLayH, false)

                    linLayH.addView(linLayItem)
                    with(linLayItem) {

//                onCategoryClick(item, linLayItem.imageButton_itemForSV)
                        layoutParams.width =
                            (screenInfo.widthInPixels / 4)

                        var bitmap = R.color.purple_500
                        if (item <= size - 1) {
                            val shopInfo = shopInfoList[item]
                            textTitle_itemForSV.text = shopInfo.title
                            textTitle_itemForSV.textSize = 12F
                            bitmap = R.drawable.coat

                            Picasso.get()
                                .load(shopInfo.url)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(imageButton_itemForSV)
                            Log.i("test4", "Picasso " + view)
                            if (item == size - 1 && !unfolded && shopInfoList.size > numHLayItems * 2) {
                                imageButton_itemForSV.setImageResource(R.drawable.baseline_keyboard_arrow_down_8)
                                imageButton_itemForSV.setPadding(35 * ScreenInfo().screenDensity.toInt())
                                textTitle_itemForSV.text = ""
                                imageButton_itemForSV.setOnClickListener(object :
                                    View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        unfolded = true
                                        linLayV.removeAllViews()
                                        linLayCategory(view, viewType)
                                        //notifyItemChanged(0, Unit)
                                    }
                                })
                            }

                        } else
                            linLayItem.imageHoleBorder_itemForSV.foreground =
                                context.getDrawable(R.color.white)
                    }
                    item++
                }
                linLayV.addView(linLayH)
            }
        }
    }


    override fun onViewAttachedToWindow(holder: CustomViewHolder) {
    }
}
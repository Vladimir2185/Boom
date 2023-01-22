package com.practicum.boom.boom.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.practicum.boom.R
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_type.view.*

class SearchAdapter(
    private val context: Context, NUMBER_OF_PROMO: Int,
) :
    GeneralAdapterRV(context, NUMBER_OF_PROMO) {

    private val cornerSize = 15f


    companion object {
        const val VIEW_TYPE_LEFT = 1
        const val VIEW_TYPE_MIDDLE = 2
        const val VIEW_TYPE_RIGHT = 3

    }


    class TypeViewHolder(itemView: View) : CustomViewHolder(itemView) {
        val imageType = itemView.image_itemType
        val title = itemView.textTitle_itemType
        val constraintLayout = itemView.conLayout_itemType

    }

    override fun getItemViewType(position: Int): Int {
        val offsetPosition = position + 1

        return if ((offsetPosition + 3) % 3 == 1)
            VIEW_TYPE_LEFT
        else if ((offsetPosition + 3) % 3 == 2)
            VIEW_TYPE_MIDDLE
        else
            VIEW_TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        //Log.i("test4", "onCreateViewHolder " + ++count)

        val layout = when (viewType) {
            VIEW_TYPE_LEFT -> R.layout.item_type
            VIEW_TYPE_MIDDLE -> R.layout.item_type
            VIEW_TYPE_RIGHT -> R.layout.item_type
            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view =
            LayoutInflater.from(context).inflate(layout, parent, false)
        return TypeViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val offsetPosition = position - NUMBER_OF_PROMO

        holder as TypeViewHolder
        with(holder) {
            // Log.i("test4", "position " + position)

            if (offsetPosition >= 0) {
                val shopInfo = shopInfoList[offsetPosition]

                onTypeClick(offsetPosition, constraintLayout)
                title.text = shopInfo.title
                fragment1LayoutDrawing(holder)

            }
        }
    }

    //помещает в метод кол-во элентов массива shopInfoList т.е. сколько будет в RecyclerView
    override fun getItemCount(): Int {
        return if (shopInfoList.size > 0) shopInfoList.size + NUMBER_OF_PROMO else 0

    }

    private fun fragment1LayoutDrawing(holder: TypeViewHolder) {
        val offsetPosition = holder.absoluteAdapterPosition + 1
        val sd = screenInfo.screenDensity
        val shopInfo = shopInfoList[holder.absoluteAdapterPosition]
        with(holder) {
            with(constraintLayout) {

                if ((offsetPosition + 3) % 3 == 1) {
                    layoutParams.height = screenInfo.widthInPixels / 3

                    (constraintLayout.layoutParams as ViewGroup.MarginLayoutParams).marginEnd =
                        (marginBetweenIcon * sd).toInt() / 2 + (2 * sd).toInt()

                    imageType.shapeAppearanceModel = ShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * sd
                        )
                        .build()
                } else if ((offsetPosition + 3) % 3 == 2) {
                    layoutParams.height = screenInfo.widthInPixels / 3
                    (layoutParams as ViewGroup.MarginLayoutParams).marginEnd =
                        (marginBetweenIcon * sd).toInt() / 4 + (0 * sd).toInt()
                    (layoutParams as ViewGroup.MarginLayoutParams).marginStart =
                        (marginBetweenIcon * sd).toInt() / 4 + (0 * sd).toInt()
                    background = context.getDrawable(R.drawable.rounded_corner_middle)

                    imageType.shapeAppearanceModel = ShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * sd
                        )
                        .setTopLeftCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * sd
                        )
                        .build()
                } else {
                    layoutParams.height = screenInfo.widthInPixels / 3
                    (layoutParams as ViewGroup.MarginLayoutParams).marginStart =
                        (marginBetweenIcon * sd).toInt() / 2 + (2 * sd).toInt()
                    background = context.getDrawable(R.drawable.rounded_corner_left)

                    imageType.shapeAppearanceModel = ShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(
                            CornerFamily.ROUNDED,
                            cornerSize * sd
                        )
                        .build()
                }


                Picasso.get()
                    .load(shopInfo.url)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imageType)
            }
        }
    }

}
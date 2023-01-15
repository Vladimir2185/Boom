package com.practicum.boom.home

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.practicum.boom.myCustomClasses.GeneralDetailFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_detail.*
import kotlinx.android.synthetic.main.fragment_detail_product_info.*


class DetailProductInfoFragment(private val offsetPosition: Int, private val product: Product) :
    GeneralDetailFragment() {

    var onFavoriteClickListener: OnFavoriteClickListener? = null

    interface OnFavoriteClickListener {
        fun onFavorClick(position: Int, imageButton: ImageButton)
        fun onFavoriteSwitch(product: Product, imageButton: ImageButton)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutInflater.inflate(
            R.layout.fragment_detail_product_info,
            this.conLayoutAttachTo_baseDetail, true
        )

        onFavoriteClickListener?.onFavoriteSwitch(product, imageButtonFavorite_detailInfo)
        onFavoriteClickListener?.onFavorClick(offsetPosition, imageButtonFavorite_detailInfo)



        textTitle_detailInfo.text = product.title
        textPrice_detailInfo.text = String.format("from %s", product.priceFormatted())
        textRating_detailInfo.text = product.ratingDotToComma()

        val n = (product.price / ((100 - product.sale).toFloat() / 100))
        textFullPrice_detailInfo.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = String.format("from %s", product.priceFormatted(n))
        }

        Picasso.get()
            .load(product.imageURL)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(image_detailInfo)


    }


    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    //    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        val dialog = BottomSheetDialog(requireContext(), theme)
//        dialog.setOnShowListener {
//
//            val bottomSheetDialog = it as BottomSheetDialog
//            bottomSheetDialog.behavior.isDraggable=false
//            val parentLayout =
//                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            parentLayout?.let { it ->
//                val behaviour = BottomSheetBehavior.from(it)
//                //setupFullHeight(it)
//                //behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//
//            }
//        }
//        return dialog
//    }
}
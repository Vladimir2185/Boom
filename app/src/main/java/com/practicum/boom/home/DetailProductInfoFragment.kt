package com.practicum.boom.home

import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.practicum.boom.R
import com.practicum.boom.api.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_product_info.*


class DetailProductInfoFragment(private val position: Int, private val product: Product) :
    BottomSheetDialogFragment() {

    var onFavoriteClickListener: OnFavoriteClickListener? = null

    interface OnFavoriteClickListener {
        fun onFavorClick(position: Int, imageButton: ImageButton)
        fun onFavoriteSwitch(product: Product, imageButton: ImageButton)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_detail_product_info, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onFavoriteClickListener?.onFavoriteSwitch(product, imageButtonFavorite_detailInfo)
        onFavoriteClickListener?.onFavorClick(position, imageButtonFavorite_detailInfo)

        imageButtonBack_detailInfo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                this@DetailProductInfoFragment.dismiss()
            }
        })

        textViewTitle_detailInfo.text = product.title
        textViewPrice_detailInfo.text = String.format("from %s", product.priceFormatted())
        textRating_detailInfo.text = product.ratingDotToComma()

        val n = (product.price / ((100 - product.sale).toFloat() / 100))
        textViewFullPrice_detailInfo.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = String.format("from %s", product.priceFormatted(n))
        }

        Picasso.get()
            .load(product.imageURL)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(imageView_detailInfo)

        dialog?.window?.let { it.attributes.windowAnimations = R.style.SideSheetDialogAnimation }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onDestroy() {
        Log.i("test4", "onDestroy()")
        super.onDestroy()
    }
}
package com.practicum.boom.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.practicum.boom.R
import com.practicum.boom.api.ShopInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_product_info.*
import kotlinx.android.synthetic.main.fragment_detail_sale.*

class DetailSaleFragment(private val offsetPosition: Int, private val shopInfo: ShopInfo) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.let { it.attributes.windowAnimations = R.style.SideSheetDialogAnimation }

        Picasso.get()
            .load(shopInfo.url)
            .error(android.R.drawable.ic_menu_report_image)
            .into(image_fragment_detail_sale)


    }
}
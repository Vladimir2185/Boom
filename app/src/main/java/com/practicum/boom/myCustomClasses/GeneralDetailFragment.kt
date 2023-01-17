package com.practicum.boom.myCustomClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.practicum.boom.R
import kotlinx.android.synthetic.main.base_detail.*

open class GeneralDetailFragment :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.base_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonBack_detailInfo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
        dialog?.window?.let { it.attributes.windowAnimations = R.style.SideSheetDialogAnimation }
    }
    override fun onDestroy() {

        Log.i("test4", "onDestroy()")
        super.onDestroy()
    }
}
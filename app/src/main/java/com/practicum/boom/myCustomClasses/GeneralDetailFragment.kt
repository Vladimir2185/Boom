package com.practicum.boom.myCustomClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.practicum.boom.MainActivity
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import kotlinx.android.synthetic.main.base_detail.*

open class GeneralDetailFragment :
    DialogFragment() {
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected var scrollStatus = MainActivity.SCROLL_STATUS_DOWN
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.base_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.liveScrollStatus.observe(viewLifecycleOwner) {
                scrollStatus = it
            }

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
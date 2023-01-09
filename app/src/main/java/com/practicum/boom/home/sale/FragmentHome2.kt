package com.practicum.boom.home.sale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity
import com.practicum.boom.MainViewModel
import com.practicum.boom.MainViewModel.Companion.type
import com.practicum.boom.R
import com.practicum.boom.home.MainHomeFragment
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import kotlinx.android.synthetic.main.fragment_home2.*


class FragmentHome2 : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome2()
    }

    private val mainViewModel: MainViewModel by activityViewModels()
    private var scrollStatus = MainHomeFragment.SCROLL_STATUS_DOWN
    private val screenInfo = MainActivity.ScreenInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView_fragmentHome2) {
            val productAdapterFH2 = ProductAdapterFH2(requireActivity())

            adapter = productAdapterFH2
            mainViewModel.readFromFirebase()
            mainViewModel.getListOfProductsByType(type).observe(viewLifecycleOwner, {
                productAdapterFH2.adapterList = it
            })
            with(mainViewModel.liveScrollStatus) {
                observe(viewLifecycleOwner, {
                    scrollStatus = it
                })


                //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
                val layoutManager =
                    CustomGridLayoutManager(requireContext(), 1, true)

                recyclerView_fragmentHome2.layoutManager = layoutManager

                addOnItemTouchListener(object :
                    RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                        value?.let { layoutManager.scrollStatus = it }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                })
            }
        }
    }
}
package com.practicum.boom.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.api.ShopInfo
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.practicum.boom.myCustomClasses.GeneralDetailFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_detail.*
import kotlinx.android.synthetic.main.fragment_detail_sale.*
import kotlinx.android.synthetic.main.fragment_base_general.*

class DetailSaleFragment(private val offsetPosition: Int, private val shopInfo: ShopInfo) :
    GeneralDetailFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var scrollStatus = MainHomeFragment.SCROLL_STATUS_DOWN
    private val screenInfo = MainActivity.ScreenInfo()
    private var viewHolder: GeneralAdapterRV.CustomViewHolder? = null

    private val NUMBER_OF_PROMO = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.base_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutInflater.inflate(
            R.layout.fragment_detail_sale, conLayoutAttachTo_baseDetail, true
        )
        layoutInflater.inflate(R.layout.fragment_base_general, frame_fragmentDetailSale, true)

        Picasso.get()
            .load(shopInfo.url)
            .error(android.R.drawable.ic_menu_report_image)
            .into(image_fragmentDetailSale)

        with(recycler_base) {
            val baseProductAdapter = BaseProductAdapter(requireActivity(), NUMBER_OF_PROMO)
            with(BaseProductAdapter) {

                adapter = baseProductAdapter
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_PROMO, NUMBER_OF_PROMO)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_UNEVEN, MAX_POOL_SIZE)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_EVEN, MAX_POOL_SIZE)

                mainViewModel.getListOfProductsByType("shoes")
                    .observe(viewLifecycleOwner, {
                        baseProductAdapter.productList = it
                    })
                with(mainViewModel.liveScrollStatus) {
                    observe(viewLifecycleOwner, {
                        scrollStatus = it
                    })


                    //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
                    val layoutManager =
                        CustomGridLayoutManager(requireContext(), screenInfo.columnCount(), true)

                    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            // 5 is the sum of items in one repeated section
                            return if (NUMBER_OF_PROMO > 0) {
                                when (position) {
                                    in 0 until NUMBER_OF_PROMO -> screenInfo.columnCount()
                                    else -> 1
                                }
                            } else 1
                        }
                    }
                    recycler_base.layoutManager = layoutManager

                    addOnItemTouchListener(object :
                        RecyclerView.OnItemTouchListener {
                        override fun onInterceptTouchEvent(
                            rv: RecyclerView,
                            e: MotionEvent
                        ): Boolean {

                            value?.let { layoutManager.scrollStatus = it }
                            return false
                        }

                        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                    })


                    baseProductAdapter.onFragmentListener =
                        object : GeneralAdapterRV.OnFragmentListener {
                            override fun onFavoriteSwitch(favorProduct: Boolean, prodID: String) {
                                mainViewModel.productUpdate(favorProduct, prodID)
                            }

                            override fun onPromoStart(holder: GeneralAdapterRV.CustomViewHolder) {

                            }
                        }
                }

            }
        }
    }

}
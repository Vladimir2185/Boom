package com.practicum.boom.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_DOWN
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_MIDDLE
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_UP
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.api.ShopInfo
import com.practicum.boom.myCustomClasses.CustomScrollView
import com.practicum.boom.myCustomClasses.GeneralBaseFragment
import com.practicum.boom.myCustomClasses.GeneralDetailFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_detail.*
import kotlinx.android.synthetic.main.fragment_detail_sale.*
import java.lang.Exception


class DetailSaleFragment(private val offsetPosition: Int, private val shopInfo: ShopInfo) :
    GeneralDetailFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var triggerBorder = 0
    private var scrollStatus = SCROLL_STATUS_DOWN
    private val smoothCount = 20  // responsible for smoothness of closer
    private val closingTime = 8

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mainViewModel.liveScrollStatus) {
            observe(viewLifecycleOwner, {
                scrollStatus = it

            })
            layoutInflater.inflate(
                R.layout.fragment_detail_sale, conLayoutAttachTo_baseDetail, true
            )

            val type:String = when (offsetPosition) {
                0 -> "dress"
                1 -> "calendars"
                2 -> "thermos flasks"
                3 -> "bags purses belts"
                4 -> "women's fur coats"
                else -> {"shoes"}
            }
            val fragment = GeneralBaseFragment(type)


            textTitle_fragmentDetailSale.text = shopInfo.title
            textShortDescr_fragmentDetailSale.text = shopInfo.shortDescription
            Picasso.get()
                .load(shopInfo.url)
                .error(android.R.drawable.ic_menu_report_image)
                .into(image_fragmentDetailSale, object : Callback {
                    override fun onSuccess() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            childFragmentManager.beginTransaction()
                                .replace(R.id.frame_fragmentDetailSale, fragment)
                                .commit()
                        }, (10))
                    }

                    override fun onError(e: Exception?) {

                    }
                })

            image_fragmentDetailSale.doOnLayout {
                triggerBorder = it.height
            }
            requireActivity().conLayoutMain.doOnLayout {
                val h = it.height
                conLayout_fragmentDetailSale.doOnLayout {

                    frame_fragmentDetailSale.layoutParams.height =
                        h - it.height + triggerBorder + fragment.marginTopRV() + 1
                }
            }
            scrollView_fragmentDetailSale.setOnScrollChangeListener(object :
                View.OnScrollChangeListener {
                override fun onScrollChange(
                    v: View?,
                    scrollX: Int,
                    scrollY: Int,
                    oldScrollX: Int,
                    oldScrollY: Int
                ) {
//                    textTitle_fragmentDetailSale.doOnLayout { it.scrollX = -scrollY / 3 }
//                    textShortDescr_fragmentDetailSale.doOnLayout { it.scrollX = -scrollY / 3 }
//                    image_fragmentDetailSale.doOnLayout {
//                        it.scrollY = scrollY / 3
//                        it.alpha = (triggerBorder - scrollY) / triggerBorder.toFloat()
//                    }
                }
            })


            scrollView_fragmentDetailSale.onDispatchTouchEvent =
                object : CustomScrollView.OnDispatchTouchEvent {
                    override fun onDispatchTouch(action_up: Boolean): Int {
                        with(scrollView_fragmentDetailSale) {
                            if (scrollY <= 0)
                                value = SCROLL_STATUS_DOWN
                            else if (scrollY >= triggerBorder) {
                                value = SCROLL_STATUS_UP
                                scrollY = triggerBorder
                            } else
                                value = SCROLL_STATUS_MIDDLE

                            hideImage(scrollY)
                        }

                        if (action_up)
                            closer()
                        return scrollStatus
                    }
                }
        }
    }

    fun hideImage(scrollY: Int) {
        textTitle_fragmentDetailSale.doOnLayout { it.scrollX = -scrollY / 3 }
        textShortDescr_fragmentDetailSale.doOnLayout { it.scrollX = -scrollY / 3 }
        image_fragmentDetailSale.doOnLayout {
            it.scrollY = scrollY / 3
            it.alpha = (triggerBorder - scrollY) / triggerBorder.toFloat()
        }
    }

    fun closer() {
        with(scrollView_fragmentDetailSale) {
            if (scrollY > triggerBorder * 0.55 && scrollY < triggerBorder) {
                val temp = (triggerBorder - scrollY) / smoothCount
                for (i in 1..smoothCount) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (i == smoothCount && scrollY != triggerBorder - 1) scrollY =
                            triggerBorder - 1
                        else scrollY += temp
                        hideImage(scrollY)
                    }, (closingTime * i).toLong())
                }
            }
            if (scrollY <= triggerBorder * 0.55) {
                val temp = scrollY / smoothCount
                for (i in 1..smoothCount) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (i == smoothCount && scrollY != 1) scrollY = 1
                        else scrollY -= temp
                        hideImage(scrollY)
                    }, (closingTime * i).toLong())
                }
            }

        }
    }
}
package com.practicum.boom.home.promo

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import com.practicum.boom.MainActivity
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.home.best.ProductAdapter
import kotlinx.android.synthetic.main.item_promo.view.*
import java.util.concurrent.TimeUnit

class Promo {

    private var timer: CountDownTimer? = null
    private val durationOfPromo: Long = 24
    private val screenInfo = ScreenInfo()


    fun promoStart(holder: ProductAdapter.ProductViewHolder, context: Context) {
        snowFlakeAnimation(holder)
        timeUntilPromoEnd(holder)
        onPromoClick(holder, context)
    }

    private fun onPromoClick(holder: ProductAdapter.ProductViewHolder, context: Context) {
        holder.itemView.cl_itemPromo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val promoBotSheet = PromoBottomSheetFragment()
                promoBotSheet.show((context as FragmentActivity).supportFragmentManager, "Tag")

            }
        })
    }

    private fun timeUntilPromoEnd(holder: ProductAdapter.ProductViewHolder) {
        val milliseconds: Long = TimeUnit.HOURS.toMillis(durationOfPromo)

        if (timer == null) {
            timer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(mSeconds: Long) {
                    val seconds = mSeconds / 1000
                    with(holder.itemView) {
                        textViewSecI_itemPromo.text = (seconds % 10).toString()
                        textViewSecII_itemPromo.text = (seconds % 60 / 10).toString()
                        textViewMinI_itemPromo.text = (seconds / 60 % 10).toString()
                        textViewMinII_itemPromo.text = (seconds / 60 % 60 / 10).toString()
                        textViewHourI_itemPromo.text = (seconds / 3600 % 10).toString()
                        textViewHourII_itemPromo.text = (seconds / 3600 % 24 / 10).toString()
                    }
                }

                override fun onFinish() {}
            }

            timer?.start()
        }

    }

    fun snowFlakeAnimation(holder: ProductAdapter.ProductViewHolder) {
        with(holder.itemView) {
            imageSnow1.layoutParams.width = (screenInfo.widthInPixels * 0.2).toInt()
            imageSnow2.layoutParams.width = (screenInfo.widthInPixels * 0.5).toInt()
            imageSnow3.layoutParams.width = (screenInfo.widthInPixels * 0.9).toInt()
            imageSnow4.layoutParams.width = (screenInfo.widthInPixels * 1.2).toInt()

            val snowflakeAnimation =
                AnimationUtils.loadAnimation(context, R.anim.snowflake_animation)
            val snowflakeAnimation2 =
                AnimationUtils.loadAnimation(context, R.anim.snowflake_animation2)
            val snowflakeAnimation3 =
                AnimationUtils.loadAnimation(context, R.anim.snowflake_animation)
            val snowflakeAnimation4 =
                AnimationUtils.loadAnimation(context, R.anim.snowflake_animation2)

            snowflakeAnimation2.startOffset = 1300
            snowflakeAnimation2.duration = 4500
            snowflakeAnimation3.startOffset = 200
            snowflakeAnimation3.duration = 5000
            snowflakeAnimation4.startOffset = 1100
            snowflakeAnimation4.duration = 4300

            // Подключаем анимацию к нужному View
            imageSnow1.startAnimation(snowflakeAnimation)
            imageSnow2.startAnimation(snowflakeAnimation2)
            imageSnow3.startAnimation(snowflakeAnimation3)
            imageSnow4.startAnimation(snowflakeAnimation4)
        }
    }
}
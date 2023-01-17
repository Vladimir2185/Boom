package com.practicum.boom.boom.home.promo

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import kotlinx.android.synthetic.main.item_promo.view.*
import java.util.concurrent.TimeUnit

object Promo {


    private val durationOfPromo: Long = 24
    private val screenInfo = ScreenInfo()

    private object Lock {
        var lock = false
        var sec: Long = 0
    }

    fun lock(): Boolean {
        return Lock.lock
    }

    fun promoStart(holder: GeneralAdapterRV.CustomViewHolder, context: Context) {
        snowFlakeAnimation(holder)
        timeUntilPromoEnd(holder, context)
        onPromoClick(holder, context)

    }

    private fun onPromoClick(holder: GeneralAdapterRV.CustomViewHolder, context: Context) {
        holder.itemView.conLayout_itemPromo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val promoBotSheet = PromoBottomSheetFragment()
                promoBotSheet.show((context as FragmentActivity).supportFragmentManager, "Tag")

            }
        })
    }

    private fun timeUntilPromoEnd(holder: GeneralAdapterRV.CustomViewHolder, context: Context) {
        val milliseconds: Long = TimeUnit.HOURS.toMillis(durationOfPromo)

        if (Lock.lock) {

            val timer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(mSeconds: Long) {
                    val seconds = Lock.sec
                    with(holder.itemView) {

                        textSecI_itemPromo.text = (seconds % 10).toString()
                        textSecII_itemPromo.text = (seconds % 60 / 10).toString()
                        textMinI_itemPromo.text = (seconds / 60 % 10).toString()
                        textMinII_itemPromo.text = (seconds / 60 % 60 / 10).toString()
                        textHourI_itemPromo.text = (seconds / 3600 % 10).toString()
                        textHourII_itemPromo.text = (seconds / 3600 % 24 / 10).toString()
                        //Log.i("test4", "timer2 " + mSeconds)
                    }
                }

                override fun onFinish() {}
            }

            timer.start()
        } else {
            val timer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(mSeconds: Long) {
                    Lock.sec = mSeconds / 1000
                    val seconds = Lock.sec
                    if (!Lock.lock) {
                        Lock.lock = true
                        with(holder.itemView) {
                            textSecI_itemPromo.text = (seconds % 10).toString()
                            textSecII_itemPromo.text = (seconds % 60 / 10).toString()
                            textMinI_itemPromo.text = (seconds / 60 % 10).toString()
                            textMinII_itemPromo.text = (seconds / 60 % 60 / 10).toString()
                            textHourI_itemPromo.text = (seconds / 3600 % 10).toString()
                            textHourII_itemPromo.text = (seconds / 3600 % 24 / 10).toString()
                        }
                    }

                }

                override fun onFinish() {}
            }
            timer.start()
            Handler(Looper.getMainLooper()).postDelayed({
                promoStart(holder, context)
            }, (50))

        }
    }

    private fun snowFlakeAnimation(holder: GeneralAdapterRV.CustomViewHolder) {
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
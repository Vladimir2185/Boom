package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import


import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.practicum.boom.home.MainHomeFragment
import com.practicum.boom.home.sale.FragmentHome2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private var animStop = false

    companion object {
        private val displayMetrics = DisplayMetrics()
    }

    //intercept all events if you need
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.visibility = View.INVISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            bottomNavigationView.visibility = View.VISIBLE
            imageViewLogo.visibility = View.INVISIBLE
            animationLayout.visibility = View.INVISIBLE
            animStop = true
        }, (2000))
        loadingAnimation()

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        //creation of main navigation fragments
        val listOfMainFragments = listOf(
            MainHomeFragment.newInstance(),
            FragmentHome2.newInstance()
        )


        val bottomNavView = bottomNavigationView
        //lambda of attaching main navigation fragments to activity
        val frag = { it: Fragment ->
            supportFragmentManager.beginTransaction().replace(R.id.mainWindow, it).commit()
        }

        //loading of start main navigation fragment
        frag(listOfMainFragments[0])
        //selector of loading fragments by bottom navigation view
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_1 -> frag(listOfMainFragments[0])
                R.id.item_2 -> frag(listOfMainFragments[1])
                R.id.item_3 -> it
                R.id.item_4 -> it
                R.id.item_5 -> bottomNavView.background = getDrawable(R.color.purple_200)
            }
            true
        }

    }

    private fun loadingAnimation() {
        val loadingAnimation1 = AnimationUtils.loadAnimation(this, R.anim.loading_animation)
        val loadingAnimation2 = AnimationUtils.loadAnimation(this, R.anim.loading_animation)
        val loadingAnimation3 = AnimationUtils.loadAnimation(this, R.anim.loading_animation)

        loadDot1.startAnimation(loadingAnimation1)

        loadingAnimation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                loadDot1.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            }

            override fun onAnimationEnd(animation: Animation?) {
                loadDot1.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.semi_transparent))
                if (!animStop)
                    loadDot2.startAnimation(loadingAnimation2)

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        loadingAnimation2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                loadDot2.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            }

            override fun onAnimationEnd(animation: Animation?) {
                loadDot2.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.semi_transparent))
                if (!animStop)
                    loadDot3.startAnimation(loadingAnimation3)

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        loadingAnimation3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                loadDot3.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            }

            override fun onAnimationEnd(animation: Animation?) {
                loadDot3.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.semi_transparent))
                if (!animStop)
                    loadDot1.startAnimation(loadingAnimation1)

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    class ScreenInfo {
        val heightInPixels = displayMetrics.heightPixels
        val widthInPixels = displayMetrics.widthPixels
        val screenDensity = displayMetrics.density

        fun widthScreen(): Int {
            return (widthInPixels / screenDensity).toInt()
        }

        //return height of product icon at recycler view
        fun heightOfProductIcon(heightProductIcon: Double): Int {
            return (widthInPixels * heightProductIcon / columnCount()).toInt()
        }

        //return number of recycler view columns depending by screen resolution
        fun columnCount(): Int {
            return if (widthScreen() / 250 > 2) widthScreen() / 250 else 2 //если больше 2, то 250/2, иначе 2
        }
    }


/*object : MyFrameLayout(requireContext()){
            override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                ev?.let { it.action=MotionEvent.ACTION_DOWN}
                return super.dispatchTouchEvent(ev)
            }
        }*/
    //getDrawable(R.color.purple_200)

}
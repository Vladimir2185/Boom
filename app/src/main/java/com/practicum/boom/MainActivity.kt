package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import


import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.practicum.boom.home.MainHomeFragment
import com.practicum.boom.home.sale.FragmentHome2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
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

        mainViewModel.liveShopInfo.value=mainViewModel.readFromFirebase()
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
            return if (widthScreen() / 250 > 2) widthScreen() / 250 else 2 //???????? ???????????? 2, ???? 250/2, ?????????? 2
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
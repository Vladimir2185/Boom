package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.practicum.boom.home.sale.FragmentHome2
import com.practicum.boom.home.MainHomeFragment


import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

companion object {
    private val displayMetrics = DisplayMetrics()
}
    val liveScrollStatus: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    //intercept all events if you need
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

     class  ScreenInfo {


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
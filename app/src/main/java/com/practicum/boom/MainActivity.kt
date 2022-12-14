package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.practicum.boom.fragments.FragmentHome2
import com.practicum.boom.fragments.MainHomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_home_fragment.*


class MainActivity : AppCompatActivity() {
    //relative to width of icon
    private val HIGHT_OF_PRODUCT_ICON = 1.3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //find-out screen resolution of current device and place it into screenInfo class
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenInfo =
            ScreenInfo(displayMetrics.widthPixels, displayMetrics.density, HIGHT_OF_PRODUCT_ICON)

        //creation of main navigation fragments
        val listOfMainFragments = listOf(
            MainHomeFragment.newInstance(screenInfo),
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


}
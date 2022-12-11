package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.adapters.VP2Adapter
import com.practicum.boom.fragments.FragmentHome1
import com.practicum.boom.fragments.FragmentHome2
import com.practicum.boom.fragments.FragmentHome3
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var textFragTitle = mutableListOf<String>()
    private var heightProduct=0
    private val HIGHT_OF_PRODUCT_ICON=1.3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = listOf(
            FragmentHome1.newInstance(getColumnCount(),heightProduct/getColumnCount()),
            FragmentHome2.newInstance(),
            FragmentHome3.newInstance()
        )




        for (i in 0 until fragList.size) {
            textFragTitle.add(tabLayoutHome.getTabAt(i)?.text.toString())
        }

        val vp2Adapter = VP2Adapter(this, fragList)
        vp2Home.adapter = vp2Adapter
        TabLayoutMediator(tabLayoutHome, vp2Home) { tab, pos ->
            tab.text = textFragTitle[pos]

        }.attach()

        ivIconTrees.visibility = View.VISIBLE

        tabLayoutHome.getTabAt(2)?.customView = ivIconTrees


    }


    private fun getColumnCount(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        heightProduct=(displayMetrics.widthPixels*HIGHT_OF_PRODUCT_ICON).toInt()
        val widthScreen = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (widthScreen / 250 > 2) widthScreen / 250 else 2 //если больше 2, то 250/2, иначе 2
    }

}
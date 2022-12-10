package com.practicum.boom
//'kotlin-android-extensions'
// Ctrl+Alt+O    clear empty import
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.fragments.FragmentHome1
import com.practicum.boom.fragments.FragmentHome2
import com.practicum.boom.fragments.FragmentHome3
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var textFragTitle = mutableListOf<String>()

    private val fragList = listOf(
        FragmentHome1.newInstance(),
        FragmentHome2.newInstance(),
        FragmentHome3.newInstance()
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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


}
package com.practicum.boom
//'kotlin-android-extensions'
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        ivIconTrees.visibility= View.VISIBLE
        tabLayout.getTabAt(2)?.customView=ivIconTrees


    }
}
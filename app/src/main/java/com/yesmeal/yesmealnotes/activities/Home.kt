package com.yesmeal.yesmealnotes.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.fragments.RecentOrders
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                var fragment  = RecentOrders()
                var transaction  = fragmentManager.beginTransaction()
                transaction.add(R.id.fg,fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true

            }


        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fab.setOnClickListener {

            startActivity(Intent(this@Home, NewOrder::class.java))
        }
    }
}

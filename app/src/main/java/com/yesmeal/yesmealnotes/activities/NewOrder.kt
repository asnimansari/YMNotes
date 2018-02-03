package com.yesmeal.yesmealnotes.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.yesmeal.yesmealnotes.R
import kotlinx.android.synthetic.main.activity_new_order.*

class NewOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        toggleButton.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                updateOrderForm(isChecked)

            }

        })
    }


    fun updateOrderForm(isYesMealOrder:Boolean){
        landmark.visibility = if(isYesMealOrder) View.VISIBLE else View.GONE
        mobileNumber.visibility = landmark.visibility
    }
}

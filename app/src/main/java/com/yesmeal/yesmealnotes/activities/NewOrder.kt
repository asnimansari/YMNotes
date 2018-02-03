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
        updateOrderForm(false)


        toggleButton.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                updateOrderForm(isChecked)
            }
        })
        saveButton.setOnClickListener {
            var shopName = shopName.text.toString().trim()
            var location = location.text.toString().trim()
            var landmark = landmark.text.toString().trim()
            var mobileNumber = mobileNumber.text.toString().trim()
            var serviceChargePayedLater  = serviceChargePayedLater.isChecked
            var collectServiceChargeFromShop  = collectServiceChargeFromShop.isChecked


        }
    }


    fun updateOrderForm(isYesMealOrder:Boolean){
        landmark.isFocusable = isYesMealOrder
        landmark.isClickable =  isYesMealOrder
        mobileNumber.isFocusable = isYesMealOrder
        mobileNumber.isClickable = isYesMealOrder
    }
}

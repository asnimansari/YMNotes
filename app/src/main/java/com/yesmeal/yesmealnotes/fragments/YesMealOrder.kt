package com.yesmeal.yesmealnotes.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.models.Shop
import com.yesmeal.yesmealnotes.ymutils.Constants
import com.yesmeal.yesmealnotes.ymutils.CusUtils


import kotlinx.android.synthetic.main.fragment_new_yesmeal_order.*

import java.util.ArrayList

/**
 * Created by asnimansari on 03/02/18.
 */


class YesMealOrder :Fragment(){

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewsAndAutoComplete()

        saveOrder.setOnClickListener {
            var areInputFieldValid =  validateAllInputFields()
            if(areInputFieldValid){
                saveShopToDB()
            }
            else{
                Toast.makeText(context,"Please Enter All Fields",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun saveShopToDB() {
        var database  = MySqlHelper.getInstance(context)
        database.addNewOrder(shopName.text.toString(),
                location.text.toString(),
                landmark.text.toString(),mobileNumber.text.toString(),
                serviceCharge.text.toString(),
                if(serviceChargePaidLater.isChecked) 1  else 0,
                if(collectServiceChargeFromShop.isChecked) 1  else 0,
                "C"
        )

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_new_yesmeal_order,container,false)
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){

            fragmentManager.beginTransaction().detach(this).attach(this).commit()
        }
    }
    private fun initializeViewsAndAutoComplete() {
        val shopsList  = ArrayList<String>()
        var databaseReference = CusUtils.getDatabase().reference.child(Constants.SHOPS)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                shopsList.clear()
                for (each_shop  in p0!!.children){
                    var shop: Shop? = each_shop.getValue(Shop::class.java)

                    shopsList.add(shop?.shopname!!)
                }
                var shopAdapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,shopsList)
                shopName.setAdapter(shopAdapter)
            }
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(context,"Error in Connecting to  Database", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun validateAllInputFields():Boolean {
        return (shopName.text.toString().length!= 0) && (location.text.toString().length!=0)&& (serviceCharge.text.toString().length !=0) && (landmark.text.toString().length!=0) && (mobileNumber.text.toString().length !=0)
    }


}
package com.yesmeal.yesmealnotes.adapters


import android.app.Fragment
import android.app.FragmentManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout


import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R

import com.yesmeal.yesmealnotes.models.Order
import java.util.*


/**
 * Created by asnimansari on 21/10/17.
 */

class RecentOrderAdapter(context: Context, internal var objects: List<Order>, fragmentManager: FragmentManager, fg:Fragment) : ArrayAdapter<Order>(context, R.layout.row_recent_orders, objects) {
    val myDatabase = MySqlHelper.getInstance(context)
    var fragm: Fragment?=null
    var fragManager: FragmentManager? = null
    val RUPEES = "₹ "
    init {
        fragManager = fragmentManager
        fragm = fg

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val order = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_recent_orders, parent, false)
        }
        val shopName = convertView!!.findViewById<View>(R.id.shopName) as TextView
        val orderID = convertView.findViewById<View>(R.id.orderID) as TextView
        val location = convertView.findViewById<View>(R.id.location) as TextView
        val staffName = convertView.findViewById<View>(R.id.staffName) as TextView
        val mobile = convertView.findViewById<View>(R.id.mobile) as TextView
        val serviceCharge = convertView.findViewById<View>(R.id.serviceCharge) as TextView
        val orderType = convertView.findViewById<View>(R.id.orderType) as TextView
        val serviceChargePayedBy = convertView.findViewById<View>(R.id.serviceChargePayedBy) as TextView
        val options = convertView.findViewById<View>(R.id.options) as ImageView

        val  ord = getItem(position)

        shopName.text = ord.shopName
        orderID.text = ord.uuid
        location.text = ord.orderLocation
//        staffName.text = ord.staffName
        mobile.text = ord.orderMobile
        serviceCharge.text = ord.orderServiceCharge
        orderType.text = ord.orderType


        options.setOnClickListener {

            val popup = PopupMenu(context, options)

            popup.inflate(R.menu.recent_order)
            //adding click listener
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.getItemId()) {


                    }
                    return false
                }
            })

            popup.show()
        }
        return convertView
    }



    override fun getCount(): Int {
        return objects.size
    }


    fun reloadThisFragment(){
            val  t = fragManager?.beginTransaction()
            t?.detach(fragm)?.attach(fragm)?.commit()
    }
}


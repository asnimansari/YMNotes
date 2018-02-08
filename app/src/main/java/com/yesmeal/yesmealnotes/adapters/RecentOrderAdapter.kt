package com.yesmeal.yesmealnotes.adapters


import android.app.AlertDialog
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher


import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.R.id.orderStaffName

import com.yesmeal.yesmealnotes.models.Order
import kotlinx.android.synthetic.main.frag_new_shop_order.*


/**
 * Created by asnimansari on 21/10/17.
 */

class RecentOrderAdapter(context: Context, internal var objects: List<Order>, fragmentManager: FragmentManager, fg:Fragment) : ArrayAdapter<Order>(context, R.layout.row_recent_orders, objects) {
    val myDatabase = MySqlHelper.getInstance(context)
    val allStaffNames = myDatabase.getAllStaffNames()
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
        val allotBtn = convertView.findViewById<View>(R.id.allotBtn) as Button

        val  ord = getItem(position)

        shopName.text = ord.shopName
        orderID.text = ord.uuid
        location.text = ord.orderLocation
        staffName.text = ord.orderStaff
        mobile.text = ord.orderStaffMobile
        serviceCharge.text = ord.orderServiceCharge
        orderType.text = ord.orderType

        allotBtn.text = if (order.orderStaff!=null && order.orderStaff.length!=0) "ALLOTED"  else "ALLOT"



        options.setOnClickListener {
            val popup = PopupMenu(context, options)
            popup.inflate(R.menu.recent_order)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.getItemId()) {

                    }
                    return false
                }
            })

            popup.show()
        }

//       ALLOT CLICK
        allotBtn.setOnClickListener {

            var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var popupLayouteorSelectingStaffs = inflater.inflate(R.layout.alert_zone_staff_allocation,null)
            var orderStaffName = popupLayouteorSelectingStaffs.findViewById<AutoCompleteTextView>(R.id.orderStaffName)
            var orderStaffMobile = popupLayouteorSelectingStaffs.findViewById<EditText>(R.id.orderStaffMobile)

            var allotStaff = popupLayouteorSelectingStaffs.findViewById<Button>(R.id.allotStaff)
            var dismissStaff = popupLayouteorSelectingStaffs.findViewById<Button>(R.id.dissmissStaff)

            var staffNames = ArrayAdapter(context,android.R.layout.simple_list_item_1,allStaffNames)
            orderStaffName.setAdapter(staffNames)

            orderStaffName.setOnItemClickListener(object :AdapterView.OnItemClickListener{
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Toast.makeText(context,getMobileNumberofStaffWithName(orderStaffName.text.toString().trim()),Toast.LENGTH_SHORT).show()
                    orderStaffMobile.setText(getMobileNumberofStaffWithName(orderStaffName.text.toString().trim()))
                }

            })

            var alertDialog = AlertDialog.Builder(context)
                    .setView(popupLayouteorSelectingStaffs)
                    .create()
            orderStaffName.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    orderStaffMobile.setText("")
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            })

            allotStaff.setOnClickListener({
                if(orderStaffName.text.length!=0 &&  orderStaffMobile.text.length!=0){
                    myDatabase.allotOrder(ord.id,orderStaffName.text.toString(),orderStaffMobile.text.toString())
                    alertDialog.dismiss()


                }
                else{
                    Toast.makeText(context,"Error In Staff Allocation",Toast.LENGTH_SHORT).show()

                }
            })
            dismissStaff.setOnClickListener {
                alertDialog.dismiss()
            }




            alertDialog.show()

        }
        if (order.orderStaff!=null && order.orderStaff.length!=0) allotBtn.setOnClickListener(null)

        return convertView
    }



    override fun getCount(): Int {
        return objects.size
    }


    fun reloadThisFragment(){
            val  t = fragManager?.beginTransaction()
            t?.detach(fragm)?.attach(fragm)?.commit()
    }

    fun getMobileNumberofStaffWithName(staffName:String):String{
        return myDatabase.getMobileNumberofStaffWithName(staffName)
    }
}


package com.yesmeal.yesmealnotes.adapters


import android.app.AlertDialog
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher


import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R

import com.yesmeal.yesmealnotes.models.Order


/**
 * Created by asnimansari on 21/10/17.
 */

class RecentOrderAdapter(context: Context, internal var objects: List<Order>, fragmentManager: FragmentManager, fg:Fragment) : ArrayAdapter<Order>(context, R.layout.row_recent_orders, objects) {
    val myDatabase = MySqlHelper.getInstance(context)
    val allStaffNames = myDatabase.getAllStaffNames()
    var fragm: Fragment?=null
    var fragManager: FragmentManager? = null
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

        val  current_order = getItem(position)

        shopName.text = current_order.shopName
        orderID.text = current_order.uuid
        location.text = current_order.orderLocation
        staffName.text = current_order.orderStaff
        mobile.text = current_order.orderStaffMobile
        serviceCharge.text = current_order.orderServiceCharge
        orderType.text = current_order.orderType

        allotBtn.text = if (order.orderStaff!=null && order.orderStaff.length!=0) "ALLOTED"  else "ALLOT"




        options.setOnClickListener {
            val popup = PopupMenu(context, options)

            if (order.orderStaff!=null && order.orderStaff.length!=0) popup.inflate(R.menu.recent_order_alloted_menu) else popup.inflate(R.menu.recent_order_unalloted)


            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.getItemId()) {

                        R.id.change_allot->{
                            orderAllocationMenu(current_order)
                        }
                        R.id.order_remarks->{
                            orderRemarksMenu(current_order)
                        }

                    }
                    return false
                }
            })

            popup.show()
        }


//       ALLOT CLICK
        allotBtn.setOnClickListener {

            orderAllocationMenu(current_order)


        }
        if (order.orderStaff!=null && order.orderStaff.length!=0) allotBtn.setOnClickListener(null)

        return convertView
    }


    fun orderRemarksMenu(ord: Order){
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupLayouteorSelectingStaffs = inflater.inflate(R.layout.alert_box_order_remarks,null)
        var orderRemarks = popupLayouteorSelectingStaffs.findViewById<EditText>(R.id.orderRemarks)
        var save = popupLayouteorSelectingStaffs.findViewById<Button>(R.id.save)
        var dismiss = popupLayouteorSelectingStaffs.findViewById<Button>(R.id.dismiss)

        orderRemarks.setText(ord.orderRemarks)



        var alertDialog = AlertDialog.Builder(context)
                .setView(popupLayouteorSelectingStaffs)
                .create()
        dismiss.setOnClickListener { alertDialog.dismiss() }
        save.setOnClickListener {
            if (orderRemarks.text.toString().trim().length==0){
                Toast.makeText(context,"Please Add Order Notes",Toast.LENGTH_SHORT).show()
            }
            else{
                myDatabase.updateOrderRemarks(ord.id,orderRemarks.text.toString())
                alertDialog.dismiss()
                reloadThisFragment()

            }
        }
        alertDialog.show()


    }

    fun orderAllocationMenu(ord: Order){
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupLayouteorSelectingStaffs = inflater.inflate(R.layout.alert_order_staff_allocation,null)
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
                reloadThisFragment()

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


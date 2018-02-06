package com.yesmeal.yesmealnotes.adapters

import android.app.AlertDialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R

/**
 * Created by asnimansari on 04/02/18.
 */


class ParentZoneAdapter(context: Context, @LayoutRes resource: Int, internal var objects: List<String>):ArrayAdapter<String>(context, resource, objects){
    var completeStaffList = MySqlHelper.getInstance(context).getAllStaffs()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val zone = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_staff_board_child, parent, false)
        }
        var zoneName = convertView?.findViewById<TextView>(R.id.zoneName)
        var staffsInZone  = convertView?.findViewById<LinearLayout>(R.id.staffsInZone)
        var addStaffToZone  = convertView?.findViewById<ImageView>(R.id.addStaffToZone)



        addStaffToZone?.setOnClickListener {
            Toast.makeText(context,zone + completeStaffList.size,Toast.LENGTH_SHORT).show()
            var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var popupLayoutForSelectingStaffs = inflater.inflate(R.layout.alert_staff_with_checkbox,null)
            var staffSelectionListLayout = popupLayoutForSelectingStaffs.findViewById<ListView>(R.id.staffListCheckBox)
            var allot = popupLayoutForSelectingStaffs.findViewById<Button>(R.id.allot)
            var dismiss = popupLayoutForSelectingStaffs.findViewById<Button>(R.id.dismiss)
            var staffAllocationAdapter=  StaffAllocationAdapter(context,R.layout.row_recent_orders,completeStaffList)
            staffSelectionListLayout.adapter = staffAllocationAdapter
            var staffAllocationAlertDialog = AlertDialog.Builder(context)
                    .setView(popupLayoutForSelectingStaffs)
                    .create()
            staffAllocationAlertDialog.show()

            allot.setOnClickListener {
                Toast.makeText(context,zone+ staffAllocationAdapter.selectedStaffID.size.toString(),Toast.LENGTH_SHORT).show()
                MySqlHelper.getInstance(context).allotStaffsToZones(zone,staffAllocationAdapter.selectedStaffID)
                staffAllocationAlertDialog.dismiss()

            }
            dismiss.setOnClickListener {
                staffAllocationAlertDialog.dismiss()
            }


        }
        zoneName?.text = zone

        var staffList =  MySqlHelper.getInstance(context).getStaffsInZone(zone)
//        var arrayAdapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,staffList)
//        staffsInZone?.adapter  = arrayAdapter

        for  (each  in staffList){
            var tv = TextView(context)
            tv.text =  each
            staffsInZone?.addView(tv)
        }

        return  convertView!!

    }

    override fun getCount(): Int {
        return objects.size
    }
}
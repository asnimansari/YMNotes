package com.yesmeal.yesmealnotes.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.models.Staff


/**
 * Created by asnimansari on 04/02/18.
 */


class StaffAllocationAdapter(context: Context, @LayoutRes resource: Int, internal var objects: List<Staff>): ArrayAdapter<Staff>(context, resource, objects){
    public var selectedStaffID =  ArrayList<Int>()
    init {
        selectedStaffID.clear()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val staff = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_staff_with_check_box, parent, false)
        }
        var staffName = convertView?.findViewById<CheckBox>(R.id.staffName)
        staffName?.text = staff.staffName

        staffName?.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if  (isChecked){
                    selectedStaffID.add(staff.staffID)
                }
                else{
                    selectedStaffID.remove(staff.staffID)
                }
            }
        })
        return  convertView!!
    }

    override fun getCount(): Int {
        return objects.size
    }
}
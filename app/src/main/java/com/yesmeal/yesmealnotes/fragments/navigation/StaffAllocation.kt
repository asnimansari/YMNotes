package com.yesmeal.yesmealnotes.fragments.navigation

import android.app.AlertDialog
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.adapters.StaffAllocationAdapter
import com.yesmeal.yesmealnotes.ymutils.StaffExpandableListAdapter

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by asnimansari on 03/02/18.
 */

class StaffAllocation : Fragment(){
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var database = MySqlHelper.getInstance(context)
        var recentOrdersList = database.selectRecentOrders()
        val fragmentManger: FragmentManager = fragmentManager
        val expandableListAdapter: ExpandableListAdapter
        val expandableListTitle: List<String>
        val expandableListDetail: HashMap<String, List<String>>
        val expandableListView = view?.findViewById<View>(R.id.expandableListView)  as ExpandableListView

        expandableListDetail = MySqlHelper.getInstance(context).getZoneAndStaff()
        expandableListTitle = ArrayList(expandableListDetail.keys)
        expandableListAdapter = StaffExpandableListAdapter(context, expandableListTitle, expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
//

        val count = expandableListAdapter.getGroupCount()
        for (i in 0 until count)
            expandableListView.expandGroup(i)


        expandableListView.setOnGroupClickListener(object :ExpandableListView.OnGroupClickListener{
            var completeStaffList = MySqlHelper.getInstance(context).getAllStaffs()




            override fun onGroupClick(p0: ExpandableListView?, p1: View?, p2: Int, p3: Long): Boolean {
                var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var popupLayoutForSelectingStaffs = inflater.inflate(R.layout.alert_zone_staff_allocation,null)
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
                    Toast.makeText(context, expandableListTitle.get(p2)+ staffAllocationAdapter.selectedStaffID.size.toString(),Toast.LENGTH_SHORT).show()
                    MySqlHelper.getInstance(context).allotStaffsToZones(expandableListTitle.get(p2),staffAllocationAdapter.selectedStaffID)
                    staffAllocationAlertDialog.dismiss()

                    fragmentManager.beginTransaction().detach(this@StaffAllocation).attach(this@StaffAllocation).commit()

                }
                dismiss.setOnClickListener {
                    staffAllocationAlertDialog.dismiss()
                }
                return  true
            }
        })




    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frag_manage_staffs,container,false)
    }
}
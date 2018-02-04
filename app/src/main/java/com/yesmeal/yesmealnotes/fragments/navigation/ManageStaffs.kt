package com.yesmeal.yesmealnotes.fragments.navigation

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.adapters.ParentZoneAdapter
import com.yesmeal.yesmealnotes.models.Zone
import kotlinx.android.synthetic.main.fragment_staff_board_parent.*

/**
 * Created by asnimansari on 03/02/18.
 */


class ManageStaffs:Fragment(){
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var db = MySqlHelper.getInstance(context)
        var zonelList = db.getAllZoneList()  as List<String>

        var arraAdapter = ParentZoneAdapter(context,android.R.layout.simple_spinner_item,zonelList)
        staff_parent_list.adapter = arraAdapter


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_staff_board_parent,container,false)!!
    }
}
package com.yesmeal.yesmealnotes.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.yesmeal.yesmealnotes.R
import com.yesmeal.yesmealnotes.fragments.navigation.RecentOrders
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yesmeal.yesmealnotes.fragments.navigation.ManageStaffs
import com.yesmeal.yesmealnotes.models.Staff
import com.yesmeal.yesmealnotes.models.Zone
import com.yesmeal.yesmealnotes.ymutils.Constants.*
import com.yesmeal.yesmealnotes.ymutils.CusUtils


class Home : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                var fragment  = RecentOrders()
                var transaction  = fragmentManager.beginTransaction()
                transaction.replace(R.id.fg,fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_notifications->{
                var fragment  = ManageStaffs()
                var transaction  = fragmentManager.beginTransaction()
                transaction.replace(R.id.fg,fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true

            }


        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fab.setOnClickListener {

            startActivity(Intent(this@Home, NewOrder::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.pull_zone_staff ->{
                var zoneDb = CusUtils.getDatabase().reference.child(ZONES)
                zoneDb.addValueEventListener(object :  ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        Toast.makeText(this@Home,"Cancelled Zone Download",Toast.LENGTH_SHORT).show()
                    }
                    override fun onDataChange(zoneSnaps: DataSnapshot?) {

                        var zoneList = ArrayList<String>()
                        for(each_zone in zoneSnaps!!.children){
                            var zone = each_zone.getValue(Zone::class.java)
                            if (zone!!.zoneName.length!=0)
                                zoneList.add(zone!!.zoneName)
                        }
                        if  (zoneList.size == 0){
                            Toast.makeText(this@Home,"Error in Fetching Zone List",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            var db = MySqlHelper.getInstance(this@Home)
                            db.flushTable(TABLE_ZONES)
                            db.insertZones(zoneList)
                            zoneList.clear()
                            Toast.makeText(this@Home,"Synced Zones",Toast.LENGTH_SHORT).show()
                        }
                    }
                })

                var staffDb = CusUtils.getDatabase().reference.child(STAFFS)
                staffDb.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        Toast.makeText(this@Home,"Cancelled Staff Download",Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(staffSnaps: DataSnapshot?) {
                        var staffList = ArrayList<Staff>()
                        for(each_staff in staffSnaps!!.children){
                            var staff = each_staff.getValue(Staff::class.java)
                            Log.e("Staffs",staff?.staffName)
                            staffList.add(staff!!)
                        }
                        if  (staffList.size == 0){
                            Toast.makeText(this@Home,"Error in Fetching Staff List",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            var db = MySqlHelper.getInstance(this@Home)
                            db.flushTable(TABLE_STAFF)
                            db.insertStaffs(staffList)
                            staffList.clear()
                            Toast.makeText(this@Home,"Synced Staffs",Toast.LENGTH_SHORT).show()
                        }
                    }

                })
            }
            else -> {
            }
        }
        return true
    }

}

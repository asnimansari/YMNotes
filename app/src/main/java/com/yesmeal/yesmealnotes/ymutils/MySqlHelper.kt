import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.yesmeal.yesmealnotes.models.Order
import com.yesmeal.yesmealnotes.models.Shop
import com.yesmeal.yesmealnotes.models.Staff
import com.yesmeal.yesmealnotes.ymutils.Constants.*
import org.jetbrains.anko.db.*
import java.util.HashMap

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {

    companion object {
        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_ZONES,true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                ZONE_NAME to TEXT)

        db.createTable(TABLE_STAFF,true,
                ID to INTEGER + PRIMARY_KEY +AUTOINCREMENT,
                STAFF_NAME to TEXT + NOT_NULL,
                STAFF_MOBILE to TEXT + NOT_NULL,
                STAFF_EMAIL to TEXT + NOT_NULL
                )

        db.createTable(TABLE_SHOPS,true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                SHOP_NAME  to TEXT + NOT_NULL
        )
        db.createTable(TABLE_ORDERS,true,
                ID to INTEGER+ PRIMARY_KEY + AUTOINCREMENT,
                ORDER_SHOP_NAME to TEXT,
                ORDER_LOCATION to TEXT,
                ORDER_LANDMARK to TEXT,
                ORDER_MOBILE to TEXT,
                ORDER_SERVICE_CHARGE to TEXT,
                ORDER_SERVICE_CHARGE_PAID_LATER to INTEGER,
                ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP to INTEGER,
                ORDER_UNIQUE_ID to TEXT + UNIQUE,
                ORDER_TYPE to TEXT,
                ORDER_IS_ALLOTED to INTEGER,
                ORDER_ALLOTED_TO to TEXT
                )
        db.createTable(TABLE_STAFF_ZONES,true,
                ID to INTEGER+ PRIMARY_KEY + AUTOINCREMENT,
                STAFF_ZONE_ZONE_NAME to TEXT + NOT_NULL,
                STAFF_ZONE_STAFF_NAME to TEXT + NOT_NULL,
                STAFF_ZONE_STAFF_MOBILE to TEXT + NOT_NULL


        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
    fun addNewOrder(shopName:String,orderLocation:String,orderLandmark:String?,orderMobile:String?,orderServiceCharge:String,orderServiceChargePaidLater:Int,orderServiceChargeCollectedFromShop:Int,orderType:String){
        var database  = this.writableDatabase
        database.insert(TABLE_ORDERS,
                ORDER_SHOP_NAME to shopName,
                ORDER_LOCATION to  orderLocation,
                ORDER_LANDMARK  to orderLandmark,
                ORDER_MOBILE to orderMobile,
                ORDER_SERVICE_CHARGE  to orderServiceCharge,
                ORDER_SERVICE_CHARGE_PAID_LATER to orderServiceChargePaidLater,
                ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP to orderServiceChargeCollectedFromShop,
                ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP to ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP,
                ORDER_UNIQUE_ID to "YM"+Math.round(Math.random()*1000000).toString(),
                ORDER_TYPE to orderType
                )
        database.close()

    }

    fun selectRecentOrders():ArrayList<Order>{
        var database = this.readableDatabase
        var cursor =  database.rawQuery("SELECT * FROM " + TABLE_ORDERS,null);
        cursor.moveToFirst()
        var orderList = ArrayList<Order>()
        while (!cursor.isAfterLast){
            var order = Order()
            order.shopName = cursor.getString(cursor.getColumnIndex(ORDER_SHOP_NAME))
            order.orderLocation = cursor.getString(cursor.getColumnIndex(ORDER_LOCATION))
            order.orderLandmark = cursor.getString(cursor.getColumnIndex(ORDER_LANDMARK))
            order.orderMobile = cursor.getString(cursor.getColumnIndex(ORDER_MOBILE))
            order.orderServiceCharge = cursor.getString(cursor.getColumnIndex(ORDER_SERVICE_CHARGE))
            order.orderServiceChargePaidLater = cursor.getInt(cursor.getColumnIndex(ORDER_SERVICE_CHARGE_PAID_LATER))  ==1
            order.orderServiceChargeCollectedFromShop = cursor.getInt(cursor.getColumnIndex(ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP))  ==1
            order.uuid = cursor.getString(cursor.getColumnIndex(ORDER_UNIQUE_ID))
            order.orderType = cursor.getString(cursor.getColumnIndex(ORDER_TYPE))
            orderList.add(order)
            cursor.moveToNext()
        }
        database.close()
        return orderList
    }

    fun flushTable(tableName:String){
        var db = this.writableDatabase
        db.execSQL("DELETE FROM "+tableName)
        db.close()
    }
    fun insertZones(zonesList:ArrayList<String>){
        var db = this.writableDatabase;
        for (each in zonesList){
            var t = db.insert(
                    TABLE_ZONES,
                    ZONE_NAME to each
            )
            Log.e("ZONE INSTERD",t.toString())

        }
        db.close()
    }
    fun insertStaffs(staffList:ArrayList<Staff>){
        var db = this.writableDatabase
        for(each in  staffList){
            var t  = db.insert(TABLE_STAFF,
                    STAFF_NAME  to each.staffName,
                    STAFF_MOBILE to each.staffMobile,
                    STAFF_EMAIL  to each.staffEmail
                    )
                    Log.e("STAFF INSTERD",t.toString())
        }
        db.close()

    }
    fun getAllZoneList():ArrayList<String>{
        var db = this.readableDatabase
        var cursor =  db.rawQuery("SELECT * FROM " + TABLE_ZONES,null);
        var zoneList = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            zoneList.add(cursor.getString(cursor.getColumnIndex(ZONE_NAME)))
            cursor.moveToNext()
        }
        cursor.close()
//        db.close()
        return zoneList


    }


    fun getZoneAndStaff():HashMap<String, List<String>>{
        var zoneAndStaff : HashMap<String, List<String>>  = HashMap<String, List<String>>()
        var allZones = getAllZoneList()
        for (eachZone in allZones){
            zoneAndStaff.put(eachZone,getStaffsInZone(eachZone))
        }
        return zoneAndStaff

    }


    fun getStaffsInZone(zone:String):List<String>{
        var db = this.readableDatabase
        var cursor =  db.rawQuery("SELECT * FROM " + TABLE_STAFF_ZONES + " WHERE " + STAFF_ZONE_ZONE_NAME + " IN ('"+zone+"')",null);
        var staffList = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            var staff = Staff()
            staff.staffName =  cursor.getString(cursor.getColumnIndex(STAFF_ZONE_STAFF_NAME))
            staff.staffMobile = cursor.getString(cursor.getColumnIndex(STAFF_ZONE_STAFF_MOBILE))
            staffList.add(staff.staffName)
            Log.e("ALL STAFF"+zone,staff.staffName+staff.staffMobile)

            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return staffList
    }


    fun getAllStaffs():List<Staff>{
        var  db = this.readableDatabase
        var staffList = ArrayList<Staff>()
        var cursor =  db.rawQuery("SELECT * FROM " + TABLE_STAFF ,null);
        cursor.moveToFirst()

        while (!cursor.isAfterLast){
            var staff = Staff()
            staff.staffName  = cursor.getString(cursor.getColumnIndex(STAFF_NAME))
            staff.staffID  = cursor.getInt(cursor.getColumnIndex(ID))
            staffList.add(staff)
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return staffList
    }
    fun getStaffDetailsForStaffWithID(staffID:Int):Staff{
        var db1 = this.readableDatabase
        var cursor =  db1.rawQuery("SELECT * FROM " + TABLE_STAFF + " WHERE "+ ID +" IN ("+staffID+")" ,null);
        cursor.moveToFirst()
        var staff = Staff()
        staff.staffName  = cursor.getString(cursor.getColumnIndex(STAFF_NAME))
        staff.staffMobile = cursor.getString(cursor.getColumnIndex(STAFF_MOBILE))

        cursor.close()

        return staff

    }
    fun allotStaffsToZones(zone: String,staffList: ArrayList<Int>){
        var db = this.writableDatabase


        db.execSQL("DELETE FROM "+ TABLE_STAFF_ZONES)
        for (eachStaffID in staffList){
            var staff = this.getStaffDetailsForStaffWithID(eachStaffID)
           var i=  db.insert(TABLE_STAFF_ZONES,
                    STAFF_ZONE_ZONE_NAME to zone,
                    STAFF_ZONE_STAFF_NAME to staff.staffName,
                    STAFF_ZONE_STAFF_MOBILE to staff.staffMobile
                    )
        }
        db.close()
    }
}


// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.yesmeal.yesmealnotes.models.Order
import com.yesmeal.yesmealnotes.ymutils.Constants.*
import org.jetbrains.anko.db.*

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
                STAFF_PHONE to TEXT + NOT_NULL,
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
}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)
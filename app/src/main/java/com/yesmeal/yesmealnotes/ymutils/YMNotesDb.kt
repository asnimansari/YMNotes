import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {
    var TABLE_ZONES  = "zones";
    var TABLE_STAFF  =  "staffs";
    var TABLE_SHOPS = "shops";
    var TABLE_ORDERS = "orders"

//    COMMIND
    var ID = "_id"

//    ZONE  TABLE
    var ZONE_NAME = "zoneName"
//    STAFF TABLE
    var STAFF_NAME = "staffName";
    var STAFF_EMAIL = "staffEmail"
    var STAFF_PHONE = "staffMobile"
//shop table
    var SHOP_NAME  = "shopName"
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
                ID to INTEGER + PRIMARY_KEY,
                ZONE_NAME to TEXT)

        db.createTable(TABLE_STAFF,true,
                ID to INTEGER + PRIMARY_KEY,
                STAFF_NAME to TEXT + NOT_NULL,
                STAFF_PHONE to TEXT + NOT_NULL,
                STAFF_EMAIL to TEXT + NOT_NULL
                )

        db.createTable(TABLE_SHOPS,true,
                ID to INTEGER + PRIMARY_KEY,
                SHOP_NAME  to TEXT + NOT_NULL
        )
        Log.e("DB","CREATED  ZONE  TABLE")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)
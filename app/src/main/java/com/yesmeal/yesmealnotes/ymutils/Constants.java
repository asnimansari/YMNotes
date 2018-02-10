package com.yesmeal.yesmealnotes.ymutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by asnimansari on 03/02/18.
 */

public class Constants {
    public static String ZONES = "zones";
    public static String SHOPS = "shops";
    public static String STAFFS = "staffs";
    public static String ORDERS = "orders";


    public static String TABLE_ZONES  = "zones";
    public static String TABLE_STAFF_ZONES  = "staffs_zone";
    public static String TABLE_STAFF  =  "staffs";
    public static String TABLE_SHOPS = "shops";
    public static String TABLE_ORDERS = "orders";

    //    COMMIND
    public static String ID = "_id";

    //    ZONE  TABLE
    public static String ZONE_NAME = "zoneName";
    //    STAFF TABLE
    public static String STAFF_NAME = "staffName";
    public static String STAFF_EMAIL = "staffEmail";
    public static String STAFF_MOBILE = "staffMobile";
    public static String STAFF_ALLOTED_TIME = "staffAllotedTime";

    //shop table
    public static String SHOP_NAME  = "shopName";

    public static String ORDER_SHOP_NAME  = "shopName";
    public static String ORDER_LOCATION  = "orderLocation";
    public static String ORDER_LANDMARK  = "orderLandmark";
    public static String ORDER_MOBILE  = "orderMobile";
    public static String ORDER_SERVICE_CHARGE  = "orderServiceCharge";
    public static String ORDER_SERVICE_CHARGE_PAID_LATER  = "orderServiceChargePaidLater";
    public static String ORDER_COLLECT_SERVICE_CHARGE_FROM_SHOP  = "orderServiceChargeCollectedFromShop";
    public static String ORDER_UNIQUE_ID  = "uuid";
    public static String ORDER_TYPE  = "orderType";
    public static String ORDER_REMARKS  = "remarks";
    public static String ORDER_STATUS  = "orderStatus";
    public static String ORDER_PASSED_AT  = "orderPassedAt";
    public static String ORDER_SYNCED  = "orderSynced";
    public static String ORDER_CREATED_DATE  = "orderCreatedDate";

    public static String STATUS_PASSED = "PASSED";
    public static String STATUS_CANCELLED = "CANCELLED";
    public static String STATUS_ALLOTED = "ALLOTED";


    public static String PREV_ALLOTED_STAFF_NAME = "prevStaffName";
    public static String PREV_ALLOTED_STAFF_MOBILE = "prevStaffMobile";

//    staffzone table
    public static String STAFF_ZONE_ZONE_NAME  = "zoneName";
    public static String STAFF_ZONE_STAFF_NAME  = "staffName";
    public static String STAFF_ZONE_STAFF_MOBILE = "staffMobile";


    public static String NOTES_APP = "notesApp";


    public static String RUPEES = "₹ ";

    public static DateFormat  dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");






}

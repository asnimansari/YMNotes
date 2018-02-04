package com.yesmeal.yesmealnotes.models;

/**
 * Created by asnimansari on 03/02/18.
 */

public class Staff {
    public Staff(String staffName, String staffMobile, String staffEmail) {
        this.staffName = staffName;
        this.staffMobile = staffMobile;
        this.staffEmail = staffEmail;
    }

    public String staffName;
    public String staffMobile;
    public String staffEmail;
    public int  staffID;

    public Staff() {
    }
}

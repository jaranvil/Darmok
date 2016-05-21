package com.nscc.jared.biz;


import android.content.Context;
import android.content.SharedPreferences;

import com.nscc.jared.data.UserDataAccess;

public class XPTriggers {

    private int RECUIT_AMT = 10;
    private int SUPPORT_AMT = 25;

    private int user_id;
    private int level;

    public XPTriggers(int user_id, int level)
    {
        this.user_id = user_id;
        this.level = level;
    }

    public void recruit()
    {
        UserDataAccess db = new UserDataAccess();
        db.updateUser(user_id, RECUIT_AMT, level*1000);
    }

    public void addSupport()
    {
        UserDataAccess db = new UserDataAccess();
        db.updateUser(user_id, SUPPORT_AMT, level*1000);
    }
}


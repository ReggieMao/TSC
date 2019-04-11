package com.ebei.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebei.library.base.BaseApp;

/**
 * Created by MaoLJ on 2018/7/18.
 * 本地保存
 */

public class UserPreference {

    public static final String SESSION_ID = "session_id";
    public static final String SECRET = "secret";
    public static final String ACCOUNT = "account";
    public static final String ADDRESS = "address";
    public static final String TOUCH_ID_STATUS = "touch_id_status";
    public static final String MINER_COUNT = "miner_count";

    public static String sp_name;

    private static SharedPreferences getSharePreferences() {
        return BaseApp.getAppContext().getSharedPreferences(sp_name, Context.MODE_PRIVATE);
    }

    public static void putInt(String key, int value) {
        getSharePreferences().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int value){
        return getSharePreferences().getInt(key,value);
    }

    public static void putString(String key, String value) {
        getSharePreferences().edit().putString(key, value).apply();
    }

    public static String getString(String key, String def) {
        return getSharePreferences().getString(key, def);
    }

}

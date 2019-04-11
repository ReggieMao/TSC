package com.ebei.tsc.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import com.ebei.library.base.BaseApp;
import com.ebei.library.util.UserPreference;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by MaoLJ on 2018/7/18.
 * 应用
 */

public class MyApplication extends BaseApp {

    private static final String TAG = "MyApplication";
    private static MyApplication mApplication;
    private static Context sContext;
    private static final String APP_ID = "wxc2cbbc53158048bc";
    private IWXAPI mIWXAPI;

    // 为适配Android5.0以下而加入
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        UserPreference.sp_name = "ct_test";

        //Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("TSC.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        //JPush
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //WeChat
        mIWXAPI = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mIWXAPI.registerApp(APP_ID);
        setIWXAPI(mIWXAPI);

        mApplication = this;
    }

    public IWXAPI getIWXAPI() {
        return mIWXAPI;
    }

    public void setIWXAPI(IWXAPI iwxApi) {
        mIWXAPI = iwxApi;
    }

    public static MyApplication getMyApplicationInstance() {
        return mApplication;
    }

}

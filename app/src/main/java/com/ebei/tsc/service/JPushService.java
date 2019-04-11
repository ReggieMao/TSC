package com.ebei.tsc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ebei.library.base.RxManage;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.RealmTransfer;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.activity.NoticeInfoActivity;
import com.ebei.tsc.activity.RemainInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by MaoLJ on 2018/8/1.
 * 极光推送的服务
 */

public class JPushService extends BroadcastReceiver {

    private static final String TAG = "JPushService";
    private Realm mRealm = Realm.getDefaultInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        String mType;
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            mType = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            if ("到账通知".equals(mType)) {
                if (!Util.isEmpty(extra) && !extra.equals("{}")) {
                    try {
                        JSONObject object = new JSONObject(extra);
                        RealmTransfer realmTransfer = new RealmTransfer();
                        realmTransfer.setTimeStamp(System.currentTimeMillis());
                        realmTransfer.setHasRead(false);
                        realmTransfer.setBalance(Double.parseDouble(object.getString("balance")));
                        realmTransfer.setInAddress(object.getString("fromAddress"));
                        realmTransfer.setFee(Double.parseDouble(object.getString("fee")));
                        realmTransfer.setId(object.getString("tixd"));
                        realmTransfer.setHeight(object.getString("blockHeight"));
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(realmTransfer);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new RxManage().post(Event.TRANS_UNREAD, null);
            } else
                new RxManage().post(Event.SYSTEM_UNREAD, null);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            mType = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            if ("到账通知".equals(mType)) {
                if (!Util.isEmpty(extra) && !extra.equals("{}")) {
                    String tradeId = "";
                    try {
                        JSONObject object = new JSONObject(extra);
                        Intent intent1 = new Intent(context, RemainInfoActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra(RemainInfoActivity.FROM_PUSH, true);
                        intent1.putExtra(RemainInfoActivity.COUNT, String.valueOf(Double.parseDouble(object.getString("balance"))));
                        intent1.putExtra(RemainInfoActivity.ADDRESS, object.getString("fromAddress"));
                        intent1.putExtra(RemainInfoActivity.FEE, String.valueOf(Double.parseDouble(object.getString("fee"))));
                        intent1.putExtra(RemainInfoActivity.HEIGHT, object.getString("blockHeight"));
                        intent1.putExtra(RemainInfoActivity.TRADE_ID, object.getString("tixd"));
                        tradeId = object.getString("tixd");
                        context.startActivity(intent1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String finalTradeId = tradeId;
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).equalTo("id", finalTradeId).findAll();
                            if (results.size() > 0) {
                                for (RealmTransfer realmTransfer : results) {
                                    realmTransfer.setHasRead(true);
                                }
                            }
                        }
                    });
                }
            } else {
                try {
                    JSONObject object = new JSONObject(extra);
                    String url = object.getString("url") + "&appSessionId=" + UserPreference.getString(UserPreference.SESSION_ID, "");
                    Intent intent1 = new Intent(context, NoticeInfoActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra(NoticeInfoActivity.URL, url);
                    context.startActivity(intent1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}

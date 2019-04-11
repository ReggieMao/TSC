package com.ebei.tsc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ebei.library.util.ActivityUtil;
import com.ebei.library.util.Util;
import com.ebei.tsc.application.MyApplication;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by MaoLJ on 2018/9/29.
 * 接收微信
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.immersiveStatus(this, true);
        ActivityUtil.add(this);
        api = MyApplication.getMyApplicationInstance().getIWXAPI();
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d(TAG, "onResp OK");
                if (baseResp instanceof SendAuth.Resp) {
                    SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
                    //获取微信传回的code
                    String code = newResp.code;
                    Log.d(TAG, "onResp code = " + code);
                    ActivityUtil.finishAll();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.d(TAG, "onResp ERR_USER_CANCEL ");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d(TAG, "onResp ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.d(TAG, "onResp default errCode " + baseResp.errCode);
                //发送返回
                break;
        }
    }

}

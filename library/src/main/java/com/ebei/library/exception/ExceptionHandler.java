package com.ebei.library.exception;

import android.content.Context;
import android.util.Log;

import com.ebei.library.R;
import com.ebei.library.base.RxManage;
import com.ebei.library.constant.DPCode;
import com.ebei.library.constant.DPStrings;
import com.ebei.library.constant.Event;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.Util;

/**
 * Created by MaoLJ on 2018/7/18.
 * 对ApiException异常进行统一处理
 */

public class ExceptionHandler {

    private static final String TAG = "ExceptionHandler";
    /** 上一次发生异常处理的时间 */
    private static long mLastHandlerStartTime = 0L;
    /** 异常处理的触发间隔时间 */
    public static long TRIGER_INTERVAL = 5000L;

    public static void handleException(Context context, ApiException e) {

        Log.d(TAG, "code == " + e.getCode());
        mLastHandlerStartTime = System.currentTimeMillis();

        if (!Util.isNetworkOpen(context)) {
            ToastUtil.toast(context, context.getString(R.string.net_error));
            return;
        }
        switch (e.getCode()) {
            case DPCode.PARSE_ERROR:
                ToastUtil.toast(context, context.getString(R.string.rob_logout));
                break;

            case DPCode.EMPTY_DATA:
                ToastUtil.toast(context, DPStrings.EMPTY_DATA);
                break;

            case DPCode.BUSSINESS_EXCEPTION:
                ToastUtil.toast(context, e.getMessage());
                if (e.getMessage().equals(context.getString(R.string.rob_logout)) || e.getMessage().equals("请重新登录"))
                    new RxManage().post(Event.RE_LOGIN, null);
                break;

            case DPCode.OUTDATE_OF_SESSION:
                ToastUtil.toast(context, DPStrings.OUTDATE_OF_SESSION);
                break;

            case DPCode.FOURCE_OUT_OF_SESSION:
                ToastUtil.toast(context, DPStrings.FOURCE_OUT_OF_SESSION);
                break;

            case DPCode.SECURITY_ERROR:
                ToastUtil.toast(context, DPStrings.SECURITY_ERROR);
                break;

            case DPCode.AUTHORIZATION_FAILURE:
                ToastUtil.toast(context, DPStrings.AUTHORIZATION_FAILURE);
                break;

            case DPCode.SERVER_INTERNAL_ERROR:
                ToastUtil.toast(context, DPStrings.SERVER_INTERNAL_ERROR);
                break;

            case DPCode.NETWORD_ERROR:
                ToastUtil.toast(context, context.getString(R.string.net_error));
                break;

            default:
                ToastUtil.toast(context, DPStrings.SYSTEM_MAINTAINING);
                break;
        }
    }

    public static long getmLastHandlerStartTime() {
        return mLastHandlerStartTime;
    }

}

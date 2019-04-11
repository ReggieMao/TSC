package com.ebei.library.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebei.library.R;
import com.ebei.library.base.RxManage;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.view.PasswordInputEdt;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MaoLJ on 2018/7/18.
 * 提示框工具类
 */

public class DialogUtil {

    private static final String TAG = "DialogUtil";

    /**
     * 回调方法的接口0
     */
    public interface OnResultListener0 {
        public void onOK();
    }

    /**
     * 回调方法的接口1
     */
    public interface OnResultListener1 {
        public void select1();

        public void select2();

        public void select3();
    }

    /**
     * 回调方法的接口2
     */
    public interface OnResultListener2 {
        public void onOk(String... params);
    }

    /**
     * 回调方法的接口3
     */
    public interface OnResultListener3 {
        public void select1();

        public void select2();

        public void select3();

        public void select4();

        public void select5();
    }

    /**
     * 回调方法的接口4
     */
    public interface OnResultListener4 {
        public void select1();

        public void select2();
    }

    /**
     * 交易提示
     */
    public static void transactionDialog(Context context, final OnResultListener2 onResListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_transaction, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        LinearLayout sub = (LinearLayout) view.findViewById(R.id.sub_layout);
        PasswordInputEdt pwd = (PasswordInputEdt) view.findViewById(R.id.et_pwd);
        ImageView close = (ImageView) view.findViewById(R.id.img_close);
        TextView forget = (TextView) view.findViewById(R.id.tv_forget);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) pwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(pwd, 0);
            }
        }, 200);

        pwd.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                if (text.length() == 6) {
                    onResListener.onOk(new String[]{text});
                    mPopupWindow.dismiss();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxManage().post(Event.TO_RESET_PWD, null);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click sub layout");
            }
        });
    }

    /**
     * 退出登录提示
     */
    public static void logoutDialog(Context context, int type, String email, final OnResultListener4 onResListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);

        if (type == 1)
            title.setText(context.getString(R.string.logout_sure));
        else
            title.setText(context.getString(R.string.email_sure) + "\n" + email + "？");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select1();
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 分享提示
     */
    public static void shareDialog(Context context, final OnResultListener3 onResListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.ll1);
        LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.ll2);
        LinearLayout layout3 = (LinearLayout) view.findViewById(R.id.ll3);
        LinearLayout layout4 = (LinearLayout) view.findViewById(R.id.ll4);
        LinearLayout layout5 = (LinearLayout) view.findViewById(R.id.ll5);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select1();
                mPopupWindow.dismiss();
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select2();
                mPopupWindow.dismiss();
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select3();
                mPopupWindow.dismiss();
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select4();
                mPopupWindow.dismiss();
            }
        });
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.select5();
                mPopupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 版本更新提示
     */
    public static void versionUpdateDialog(Context context, String updateS, String contentS, final OnResultListener0 onResListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_version_update, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView update = (TextView) view.findViewById(R.id.tv_update_version);
        TextView now = (TextView) view.findViewById(R.id.tv_now_version);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);

        update.setText(context.getString(R.string.update_version) + updateS);
        now.setText(context.getString(R.string.now_version) + Constants.APP_VERSION);
        content.setText(contentS);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResListener.onOK();
                mPopupWindow.dismiss();
            }
        });
    }

}

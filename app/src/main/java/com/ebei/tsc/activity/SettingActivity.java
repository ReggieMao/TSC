package com.ebei.tsc.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/30.
 * 设置页面
 */

public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";
    @Bind(R.id.img_touch)
    ImageView mImgTouch;
    private int mIsOpen;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        mIsOpen = UserPreference.getInt(UserPreference.TOUCH_ID_STATUS, 0);
        if (mIsOpen == 0)
            mImgTouch.setImageResource(R.mipmap.btn_blue);
        else
            mImgTouch.setImageResource(R.mipmap.btn_gray);
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.LOGOUT, new Action1() {
            @Override
            public void call(Object o) {
                UserPreference.putString(UserPreference.SESSION_ID, "");
                UserPreference.putString(UserPreference.SECRET, "");
                UserPreference.putString(UserPreference.ACCOUNT, "");
                ToastUtil.toast(SettingActivity.this, getString(R.string.toast_logout_success));
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });
    }

    @OnClick({R.id.img_back, R.id.ll_login_pwd, R.id.ll_pay_pwd, R.id.img_touch, R.id.tv_logout})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_login_pwd:
                startActivity(new Intent(this, UpdateLoginPwdActivity.class));
                break;
            case R.id.ll_pay_pwd:
                startActivity(new Intent(this, UpdatePayPwdActivity.class));
                break;
            case R.id.img_touch:
                if (mIsOpen == 0) {
                    mImgTouch.setImageResource(R.mipmap.btn_gray);
                    UserPreference.putInt(UserPreference.TOUCH_ID_STATUS, 1);
                    mIsOpen = 1;
                } else {
                    mImgTouch.setImageResource(R.mipmap.btn_blue);
                    UserPreference.putInt(UserPreference.TOUCH_ID_STATUS, 0);
                    mIsOpen = 0;
                }
                break;
            case R.id.tv_logout:
                DialogUtil.logoutDialog(this, 1, "", new DialogUtil.OnResultListener4() {
                    @Override
                    public void select1() {
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        TSCApi.getInstance().logout(UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
                    }

                    @Override
                    public void select2() {

                    }
                });
                break;
        }
    }

}

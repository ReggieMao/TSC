package com.ebei.tsc.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Login;
import com.ebei.library.pojo.Register;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.library.view.PasswordInputEdt;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/3.
 * 交易密码页面2
 */

public class PayPwd2Activity extends BaseActivity {

    private static final String TAG = "PayPwd2Activity";
    public static String MOBILE = "mobile";
    public static String CODE = "code";
    public static String LOGIN_PWD = "login_pwd";
    public static String PAY_PWD = "pay_pwd";
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.et_pwd)
    PasswordInputEdt mEtPwd;
    private String mPayPwd = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_pwd2;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        mEtPwd.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                if (text.length() == 6) {
                    mPayPwd = text;
                    mTvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
                } else
                    mTvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.REGISTER, new Action1<Register>() {
            @Override
            public void call(Register o) {
                String sign = Constants.SALT_CIPHER + "loginAccount=" + getIntent().getStringExtra(MOBILE) + "&password=" + Util.encrypt(getIntent().getStringExtra(LOGIN_PWD))
                        + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().userLogin(getIntent().getStringExtra(MOBILE), Util.encrypt(getIntent().getStringExtra(LOGIN_PWD)), sign, Util.getNowTime());
            }
        });

        rxManage.on(Event.LOGIN_PWD, new Action1<Login>() {
            @Override
            public void call(Login o) {
                UserPreference.putString(UserPreference.SESSION_ID, o.getSessionId());
                UserPreference.putString(UserPreference.SECRET, o.getSecret());
                UserPreference.putString(UserPreference.ACCOUNT, o.getLoginAccount());
                ToastUtil.toast(PayPwd2Activity.this, getString(R.string.toast_register_login_success));
                startActivity(new Intent(PayPwd2Activity.this, MainActivity.class));
            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_confirm:
                if (mPayPwd.length() != 6) {
                    ToastUtil.toast(this, getString(R.string.toast_pay_pwd));
                    return;
                }
                if (!mPayPwd.equals(getIntent().getStringExtra(PAY_PWD))) {
                    ToastUtil.toast(this, getString(R.string.toast_pay_pwd1));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "bindMobile=" + getIntent().getStringExtra(MOBILE) + "&passWord=" + Util.encrypt(getIntent().getStringExtra(LOGIN_PWD))
                        + "&payPassWord=" + Util.encrypt(mPayPwd) + "&submitTime=" + Util.getNowTime() + "&verifyCode=" + getIntent().getStringExtra(CODE)
                        + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().register(getIntent().getStringExtra(MOBILE), Util.encrypt(getIntent().getStringExtra(LOGIN_PWD)), Util.encrypt(mPayPwd),
                        getIntent().getStringExtra(CODE), sign, Util.getNowTime());
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            mPayPwd = "";
            mTvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
        }
        return super.onKeyDown(keyCode, event);
    }

}

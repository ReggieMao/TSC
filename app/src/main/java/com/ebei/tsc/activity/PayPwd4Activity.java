package com.ebei.tsc.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.util.ActivityUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.Util;
import com.ebei.library.view.PasswordInputEdt;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/3.
 * 交易密码页面4
 */

public class PayPwd4Activity extends BaseActivity {

    private static final String TAG = "PayPwd4Activity";
    public static String MOBILE = "mobile";
    public static String CODE = "code";
    public static String PAY_PWD = "pay_pwd";
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.et_pwd)
    PasswordInputEdt mEtPwd;
    private String mPayPwd = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_pwd4;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        ActivityUtil.add(this);
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
        rxManage.on(Event.REST_PAY_PWD, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(PayPwd4Activity.this, getString(R.string.toast_pay_pwd_success));
                ActivityUtil.finishAll();
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
                String sign = Constants.SALT_CIPHER + "confirmPassword=" + Util.encrypt(mPayPwd) + "&loginAccount=" + getIntent().getStringExtra(MOBILE)
                        + "&password=" + Util.encrypt(getIntent().getStringExtra(PAY_PWD)) + "&pwdType=" + 2 + "&submitTime=" + Util.getNowTime() + "&verifyCode="
                        + getIntent().getStringExtra(CODE) + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().restPwd(getIntent().getStringExtra(MOBILE), Util.encrypt(getIntent().getStringExtra(PAY_PWD)), Util.encrypt(mPayPwd),
                        2, getIntent().getStringExtra(CODE), sign, Util.getNowTime());
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

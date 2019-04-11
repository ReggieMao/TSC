package com.ebei.tsc.activity;

import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.util.CheckUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/3.
 * 修改登录密码页面
 */

public class UpdateLoginPwdActivity extends BaseActivity {

    private static final String TAG = "UpdateLoginPwdActivity";
    @Bind(R.id.view)
    View mView;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_pwd1)
    EditText mEtPwd1;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    private boolean mobileYes = false;
    private boolean pwdYes = false;
    private boolean pwd1Yes = false;
    private boolean codeYes = false;
    private TimeCount time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_login_pwd;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        time = new TimeCount(60000, 1000);
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 11) {
                    mobileYes = true;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
                } else {
                    mobileYes = false;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
                }
                canLogin(mobileYes && pwdYes && pwd1Yes && codeYes);
            }
        });
        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                codeYes = editable.length() == 4;
                canLogin(mobileYes && pwdYes && pwd1Yes && codeYes);
            }
        });
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pwdYes = editable.length() > 7;
                canLogin(mobileYes && pwdYes && pwd1Yes && codeYes);
            }
        });
        mEtPwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pwd1Yes = editable.length() > 7;
                canLogin(mobileYes && pwdYes && pwd1Yes && codeYes);
            }
        });
        mEtMobile.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyboardIsShow();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.GET_AUTH_CODE, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(UpdateLoginPwdActivity.this, getString(R.string.toast_code_success));
                time.start();
            }
        });

        rxManage.on(Event.REST_LOGIN_PWD, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(UpdateLoginPwdActivity.this, getString(R.string.toast_login_pwd_success));
                finish();
            }
        });
    }

    private void keyboardIsShow() {
        final Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        final int heightDifference = screenHeight - rect.bottom;
        boolean visible = heightDifference > screenHeight / 3;
        if (!visible)
            mView.setVisibility(View.VISIBLE);
        else
            mView.setVisibility(View.GONE);
    }

    // 倒计时任务
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvSend.setClickable(false);
            mTvSend.setText(millisUntilFinished / 1000 + "s");
            mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
        }

        @Override
        public void onFinish() {
            mTvSend.setText(R.string.send);
            mTvSend.setClickable(true);
            mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        }
    }

    private void canLogin(boolean can) {
        if (can)
            mTvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        else
            mTvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
    }

    @OnClick({R.id.img_back, R.id.et_mobile, R.id.tv_send, R.id.tv_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_mobile:
                mEtMobile.setCursorVisible(true);
                break;
            case R.id.tv_send:
                if (mEtMobile.getText().toString().length() != 11) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "mobile=" + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().getAuthCode(mEtMobile.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_confirm:
                if (mEtMobile.getText().toString().length() != 11) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (mEtCode.getText().toString().length() != 4) {
                    ToastUtil.toast(this, getString(R.string.toast_code));
                    return;
                }
                if (mEtPwd.getText().toString().length() < 8) {
                    ToastUtil.toast(this, getString(R.string.toast_pwd));
                    return;
                }
                if (mEtPwd1.getText().toString().length() < 8) {
                    ToastUtil.toast(this, getString(R.string.toast_pwd1));
                    return;
                }
                if (!mEtPwd.getText().toString().equals(mEtPwd1.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_pwd2));
                    return;
                }
                String sign1 = Constants.SALT_CIPHER + "confirmPassword=" + Util.encrypt(mEtPwd1.getText().toString()) + "&loginAccount=" + mEtMobile.getText().toString()
                        + "&password=" + Util.encrypt(mEtPwd.getText().toString()) + "&pwdType=" + 1 + "&submitTime=" + Util.getNowTime() + "&verifyCode="
                        + mEtCode.getText().toString() + Constants.SALT_CIPHER;
                sign1 = Util.encrypt(sign1);
                TSCApi.getInstance().restPwd(mEtMobile.getText().toString(), Util.encrypt(mEtPwd.getText().toString()), Util.encrypt(mEtPwd1.getText().toString()),
                        1, mEtCode.getText().toString(), sign1, Util.getNowTime());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

}

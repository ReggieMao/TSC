package com.ebei.tsc.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.util.CheckUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/29.
 * 注册页面
 */

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.img_logo)
    ImageView mImgLogo;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_pwd1)
    EditText mEtPwd1;
    @Bind(R.id.img_close)
    RelativeLayout mImgClose;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.ll_mobile)
    LinearLayout mLlMobile;
    @Bind(R.id.ll_code)
    LinearLayout mLlCode;
    @Bind(R.id.ll_pwd)
    LinearLayout mLlPwd;
    @Bind(R.id.ll_pwd1)
    LinearLayout mLlPwd1;
    private boolean mobileYes = false;
    private boolean pwdYes = false;
    private boolean pwd1Yes = false;
    private boolean codeYes = false;
    private TimeCount time;
    private boolean hasRegister = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
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
                if (editable.length() != 0)
                    mImgClose.setVisibility(View.VISIBLE);
                else
                    mImgClose.setVisibility(View.GONE);
                if (editable.length() == 11) {
                    String sign = Constants.SALT_CIPHER + "loginAccount=" + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                    sign = Util.encrypt(sign);
                    TSCApi.getInstance().checkLoginAccount(mEtMobile.getText().toString(), sign, Util.getNowTime());
                } else {
                    mobileYes = false;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
                    mLlMobile.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
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
                if (editable.length() == 4) {
                    codeYes = true;
                    mLlCode.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue));
                } else {
                    codeYes = false;
                    mLlCode.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
                }
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
                if (editable.length() > 7) {
                    pwdYes = true;
                    mLlPwd.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue));
                } else {
                    pwdYes = false;
                    mLlPwd.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
                }
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
                if (editable.length() > 7) {
                    pwd1Yes = true;
                    mLlPwd1.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue));
                } else {
                    pwd1Yes = false;
                    mLlPwd1.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
                }
                canLogin(mobileYes && pwdYes && pwd1Yes && codeYes);
            }
        });
        mEtPwd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyboardIsShow();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.CHECK_ACCOUNT, new Action1<String>() {
            @Override
            public void call(String o) {
                if (o.equals("用户名已存在")) {
                    ToastUtil.toast(RegisterActivity.this, getString(R.string.check_account));
                    hasRegister = true;
                    mobileYes = false;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
                    mLlMobile.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
                } else if (o.equals("用户名不存在")) {
                    hasRegister = false;
                    mobileYes = true;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
                    mLlMobile.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue));
                }
            }
        });

        rxManage.on(Event.GET_AUTH_CODE, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(RegisterActivity.this, getString(R.string.toast_code_success));
                time.start();
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
            mImgLogo.setVisibility(View.VISIBLE);
        else
            mImgLogo.setVisibility(View.GONE);
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
            mTvNext.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        else
            mTvNext.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
    }

    @OnClick({R.id.img_back, R.id.et_mobile, R.id.img_close, R.id.tv_send, R.id.tv_next, R.id.tv_service})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_mobile:
                mEtMobile.setCursorVisible(true);
                break;
            case R.id.img_close:
                mEtMobile.setText("");
                break;
            case R.id.tv_send:
//                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
//                    ToastUtil.toast(this, getString(R.string.toast_mobile));
//                    return;
//                }
                if (hasRegister) {
                    ToastUtil.toast(this, getString(R.string.check_account));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "mobile=" + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().getAuthCode(mEtMobile.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_next:
                if (mEtMobile.getText().toString().length() != 11) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (hasRegister) {
                    ToastUtil.toast(this, getString(R.string.check_account));
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
                Intent intent = new Intent(this, PayPwd1Activity.class);
                intent.putExtra(PayPwd1Activity.MOBILE, mEtMobile.getText().toString());
                intent.putExtra(PayPwd1Activity.CODE, mEtCode.getText().toString());
                intent.putExtra(PayPwd1Activity.LOGIN_PWD, mEtPwd.getText().toString());
                startActivity(intent);
                break;
            case R.id.tv_service:
                startActivity(new Intent(this, UserServiceActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

}

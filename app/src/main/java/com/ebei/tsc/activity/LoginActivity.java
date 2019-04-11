package com.ebei.tsc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseApp;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Login;
import com.ebei.library.util.CheckUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/28.
 * 登录页面
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @Bind(R.id.img_logo)
    ImageView mImgLogo;
    @Bind(R.id.tv_pwd)
    TextView mTvPwd;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.view_pwd)
    View mViewPwd;
    @Bind(R.id.view_code)
    View mViewCode;
    @Bind(R.id.ll_pwd1)
    LinearLayout mLlPwd;
    @Bind(R.id.ll_code1)
    LinearLayout mLlCode;
    @Bind(R.id.img_close)
    RelativeLayout mImgClose;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.ll_mobile)
    LinearLayout mLlMobile;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    private TimeCount time;
    private boolean mobileYes = false;
    private boolean pwdYes = false;
    private boolean codeYes = false;
    private int loginType = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
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
                    mobileYes = true;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
                    mLlMobile.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue));
                } else {
                    mobileYes = false;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
                    mLlMobile.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray));
                }
                if (loginType == 1)
                    canLogin(mobileYes && pwdYes);
                else
                    canLogin(mobileYes && codeYes);
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
                if (loginType == 1)
                    canLogin(mobileYes && pwdYes);
                else
                    canLogin(mobileYes && codeYes);
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
                if (loginType == 1)
                    canLogin(mobileYes && pwdYes);
                else
                    canLogin(mobileYes && codeYes);
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
        rxManage.on(Event.LOGIN_PWD, new Action1<Login>() {
            @Override
            public void call(Login o) {
                login(o);
            }
        });

        rxManage.on(Event.LOGIN_CODE, new Action1<Login>() {
            @Override
            public void call(Login o) {
                login(o);
            }
        });

        rxManage.on(Event.GET_AUTH_CODE, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(LoginActivity.this, getString(R.string.toast_code_success));
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

    private void login(Login o) {
        UserPreference.putString(UserPreference.SESSION_ID, o.getSessionId());
        UserPreference.putString(UserPreference.SECRET, o.getSecret());
        UserPreference.putString(UserPreference.ACCOUNT, o.getLoginAccount());
        ToastUtil.toast(this, getString(R.string.toast_login_success));
        startActivity(new Intent(this, MainActivity.class));
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
            mTvLogin.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        else
            mTvLogin.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
    }

    @OnClick({R.id.ll_pwd, R.id.ll_code, R.id.img_close, R.id.et_mobile, R.id.tv_send, R.id.tv_login, R.id.tv_register, R.id.tv_forget})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_pwd:
                loginType = 1;
                mTvPwd.setTextColor(getColor(R.color.textBlue));
                mTvPwd.setTextSize(17);
                mViewPwd.setVisibility(View.VISIBLE);
                mTvCode.setTextColor(getColor(R.color.textGray1));
                mTvCode.setTextSize(16);
                mViewCode.setVisibility(View.GONE);
                mLlPwd.setVisibility(View.VISIBLE);
                mLlCode.setVisibility(View.GONE);
                break;
            case R.id.ll_code:
                loginType = 2;
                mTvPwd.setTextColor(getColor(R.color.textGray1));
                mTvPwd.setTextSize(16);
                mViewPwd.setVisibility(View.GONE);
                mTvCode.setTextColor(getColor(R.color.textBlue));
                mTvCode.setTextSize(17);
                mViewCode.setVisibility(View.VISIBLE);
                mLlPwd.setVisibility(View.GONE);
                mLlCode.setVisibility(View.VISIBLE);
                break;
            case R.id.img_close:
                mEtMobile.setText("");
                break;
            case R.id.et_mobile:
                mEtMobile.setCursorVisible(true);
                break;
            case R.id.tv_send:
//                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
//                    ToastUtil.toast(this, getString(R.string.toast_mobile));
//                    return;
//                }
                String sign = Constants.SALT_CIPHER + "mobile=" + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                TSCApi.getInstance().getAuthCode(mEtMobile.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_login:
                if (mEtMobile.getText().toString().length() != 11) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (loginType == 1) {
                    if (mEtPwd.getText().toString().length() < 8) {
                        ToastUtil.toast(this, getString(R.string.toast_pwd));
                        return;
                    }
                } else {
                    if (mEtCode.getText().toString().length() != 4) {
                        ToastUtil.toast(this, getString(R.string.toast_code));
                        return;
                    }
                }
                if (loginType == 1) {
                    String sign1 = Constants.SALT_CIPHER + "loginAccount=" + mEtMobile.getText().toString() + "&password=" + Util.encrypt(mEtPwd.getText().toString())
                            + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                    sign1 = Util.encrypt(sign1);
                    TSCApi.getInstance().userLogin(mEtMobile.getText().toString(), Util.encrypt(mEtPwd.getText().toString()), sign1, Util.getNowTime());
                } else {
                    String sign2 = Constants.SALT_CIPHER + "loginAccount=" + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + "&verifyCode="
                            + mEtCode.getText().toString() + Constants.SALT_CIPHER;
                    sign2 = Util.encrypt(sign2);
                    TSCApi.getInstance().userLoginByVerifyCode(mEtMobile.getText().toString(), mEtCode.getText().toString(), sign2, Util.getNowTime());
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            List<Activity> activities = ((BaseApp) getApplication()).getActivities();
            for (Activity activity : activities) {
                activity.finish();
            }
        }
        return false;
    }

}

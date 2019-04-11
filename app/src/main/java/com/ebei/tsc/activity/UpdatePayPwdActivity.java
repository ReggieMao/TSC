package com.ebei.tsc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.util.ActivityUtil;
import com.ebei.library.util.CheckUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/3.
 * 修改交易密码页面
 */

public class UpdatePayPwdActivity extends BaseActivity {

    private static final String TAG = "UpdatePayPwdActivity";
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    private boolean mobileYes = false;
    private boolean codeYes = false;
    private TimeCount time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_pay_pwd;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        ActivityUtil.add(this);
//        Util.addLayoutListener(mLlMain, mTvNext);
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
                codeYes = editable.length() == 4;
                canLogin(mobileYes && codeYes);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.GET_AUTH_CODE, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(UpdatePayPwdActivity.this, getString(R.string.toast_code_success));
                time.start();
            }
        });
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

    @OnClick({R.id.img_back, R.id.et_mobile, R.id.tv_send, R.id.tv_next})
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
            case R.id.tv_next:
                if (mEtMobile.getText().toString().length() != 11) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (mEtCode.getText().toString().length() != 4) {
                    ToastUtil.toast(this, getString(R.string.toast_code));
                    return;
                }
                Intent intent = new Intent(this, PayPwd3Activity.class);
                intent.putExtra(PayPwd3Activity.MOBILE, mEtMobile.getText().toString());
                intent.putExtra(PayPwd3Activity.CODE, mEtCode.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

}

package com.ebei.tsc.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.MineralAsset;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/31.
 * 转出挖矿页面
 */

public class MinerOutActivity extends BaseActivity {

    private static final String TAG = "MinerOutActivity";
    @Bind(R.id.view)
    View mView;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    @Bind(R.id.et_count)
    EditText mEtCount;
    @Bind(R.id.ll_count)
    LinearLayout mLlCount;
    @Bind(R.id.tv_can_pay)
    TextView mTvCanPay;
    private double beforeCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_miner_out;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, false);
        Util.setPoint(mEtCount);
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().mineralAsset(2, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    mLlCount.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue2));
                    canTransfer(true);
                } else {
                    mLlCount.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray1));
                    canTransfer(false);
                }
            }
        });
        mEtCount.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyboardIsShow();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.MINERAL_ASSET2, new Action1<MineralAsset>() {
            @Override
            public void call(MineralAsset o) {
                beforeCount = o.getTotalIncomeInf();
                mTvCanPay.setText(getString(R.string.can_pay) + Util.point(beforeCount, 6) + getString(R.string.tsc));
            }
        });

        rxManage.on(Event.TRANSFER_TO_ORE1, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(MinerOutActivity.this, getString(R.string.toast_miner_out_success));
                mEtCount.setText("");
                mTvSure.setClickable(true);
                String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                sign = Util.encrypt(sign);
                TSCApi.getInstance().mineralAsset(2, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
            }
        });

        rxManage.on(Event.TO_RESET_PWD, new Action1() {
            @Override
            public void call(Object o) {
                startActivity(new Intent(MinerOutActivity.this, UpdatePayPwdActivity.class));
            }
        });
    }

    private void keyboardIsShow() {
        final Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        final int heightDifference = screenHeight - rect.bottom;
        boolean visible = heightDifference > screenHeight / 3;
        if (!visible) {
            mView.setVisibility(View.VISIBLE);
        } else {
            mView.setVisibility(View.GONE);
        }
    }

    private void canTransfer(boolean can) {
        if (can)
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        else
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
    }

    @OnClick({R.id.img_back, R.id.et_count, R.id.tv_all, R.id.tv_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_count:
                mEtCount.setCursorVisible(true);
                break;
            case R.id.tv_all:
                mEtCount.setText(Util.point(beforeCount, 6));
                break;
            case R.id.tv_sure:
                if (Util.isEmpty(mEtCount.getText().toString()) || beforeCount < 0 ||
                        beforeCount == 0 || Double.parseDouble(mEtCount.getText().toString()) > beforeCount) {
                    ToastUtil.toast(this, getString(R.string.toast_count_error));
                    return;
                }
                DialogUtil.transactionDialog(this, new DialogUtil.OnResultListener2() {
                    @Override
                    public void onOk(String... params) {
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "operateType=2&payPassword=" + Util.encrypt(params[0]) + "&submitTime=" +
                                Util.getNowTime() + "&transferAmount=" + mEtCount.getText().toString() + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        TSCApi.getInstance().transferToOre(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""), mEtCount.getText().toString(),
                                Util.encrypt(params[0]), 2);
                        mTvSure.setClickable(false);
                    }
                });
                break;
        }
    }

}

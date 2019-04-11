package com.ebei.tsc.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.ConstantCode;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Book;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.yanzhenjie.permission.AndPermission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/31.
 * TSC转账页面
 */

public class TransferActivity extends BaseActivity {

    private static final String TAG = "TransferActivity";
    private int REQUEST_CODE_SCAN = 1001;
    @Bind(R.id.view)
    View mView;
    @Bind(R.id.view1)
    View mView1;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    @Bind(R.id.et_address)
    EditText mEtAddress;
    @Bind(R.id.et_count)
    EditText mEtCount;
    @Bind(R.id.ll_address)
    LinearLayout mLlAddress;
    @Bind(R.id.ll_count)
    LinearLayout mLlCount;
    @Bind(R.id.tv_can_pay)
    TextView mTvCanPay;
    private boolean addressYes = false;
    private boolean countYes = false;
    private double beforeCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, false);
        Util.setPoint(mEtCount);
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().getUserBook(1, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    mLlAddress.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_blue2));
                    addressYes = true;
                } else {
                    mLlAddress.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray1));
                    addressYes = false;
                }
                canTransfer(addressYes && countYes);
            }
        });
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
                    countYes = true;
                } else {
                    mLlCount.setBackground(getResources().getDrawable(R.drawable.bg_round_edit_gray1));
                    countYes = false;
                }
                canTransfer(addressYes && countYes);
            }
        });
        mEtAddress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyboardIsShow();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.USER_BOOK1, new Action1<Book>() {
            @Override
            public void call(Book o) {
                beforeCount = o.getBookBalance();
                mTvCanPay.setText(getString(R.string.can_pay) + Util.point(beforeCount, 6) + getString(R.string.tsc));
            }
        });

        rxManage.on(Event.TRANSFER_TO_OTHERS, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(TransferActivity.this, getString(R.string.toast_transfer_success));
                mEtAddress.setText("");
                mEtCount.setText("");
                mTvSure.setClickable(true);
                String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                sign = Util.encrypt(sign);
                TSCApi.getInstance().getUserBook(1, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
            }
        });

        rxManage.on(Event.TO_RESET_PWD, new Action1() {
            @Override
            public void call(Object o) {
                startActivity(new Intent(TransferActivity.this, UpdatePayPwdActivity.class));
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
            mView1.setVisibility(View.VISIBLE);
        } else {
            mView.setVisibility(View.GONE);
            mView1.setVisibility(View.GONE);
        }
    }

    private void canTransfer(boolean can) {
        if (can)
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_blue));
        else
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray));
    }

    @OnClick({R.id.img_back, R.id.et_address, R.id.img_sweep, R.id.tv_all, R.id.tv_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_address:
                mEtAddress.setCursorVisible(true);
                break;
            case R.id.img_sweep:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 先判断是否有权限
                    if (AndPermission.hasPermission(TransferActivity.this, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 有权限
                        Intent intent = new Intent(TransferActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    } else {
                        // 申请权限
                        AndPermission.with(TransferActivity.this).requestCode(ConstantCode.REQUEST_CODE_OF_CAMERA).callback(TransferActivity.this)
                                .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).start();
                    }
                } else {
                    Intent intent = new Intent(TransferActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
                break;
            case R.id.tv_all:
                mEtCount.setText(Util.point(beforeCount, 6));
                break;
            case R.id.tv_sure:
                if (Util.isEmpty(mEtAddress.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.hint_address));
                    return;
                }
                if (Util.isEmpty(mEtCount.getText().toString()) || beforeCount < 0 ||
                        beforeCount == 0 || Double.parseDouble(mEtCount.getText().toString()) > beforeCount) {
                    ToastUtil.toast(this, getString(R.string.toast_count_error));
                    return;
                }
                DialogUtil.transactionDialog(this, new DialogUtil.OnResultListener2() {
                    @Override
                    public void onOk(String... params) {
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "outAddress=" + mEtAddress.getText().toString() + "&payPassword=" + Util.encrypt(params[0])
                                + "&submitTime=" + Util.getNowTime() + "&transferAmount=" + mEtCount.getText().toString() + "&userWalletType=2" + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        TSCApi.getInstance().transferToOthers(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""), "2", mEtAddress.getText()
                                .toString(), mEtCount.getText().toString(), Util.encrypt(params[0]));
                        mTvSure.setClickable(false);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                if (content.startsWith("tsc:")) {
                    if (!content.contains("?"))
                        content = content.substring(4, content.length());
                    else {
                        if (!content.contains("amount="))
                            content = content.substring(4, content.indexOf("?"));
                        else
                            content = content.substring(4, content.indexOf("?")) + "-" + content.substring(content.indexOf("=") + 1, content.indexOf(".") + 7);
                    }
                }
                String address;
                String count = "";
                if (content.contains("-")) {
                    address = content.substring(0, content.indexOf("-"));
                    count = content.substring(content.indexOf("-") + 1, content.length());
                } else {
                    address = content;
                }
                mEtAddress.setText(address);
                mEtCount.setText(count);
            }
        }
    }

}

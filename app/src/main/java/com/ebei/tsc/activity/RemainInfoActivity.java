package com.ebei.tsc.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.pojo.Balance;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/9/5.
 * 交易详情页面
 */

public class RemainInfoActivity extends BaseActivity {

    private static final String TAG = "RemainInfoActivity";
    public static String FROM_PUSH = "from_push";
    public static String BALANCE = "balance";
    public static String COUNT = "count";
    public static String ADDRESS = "address";
    public static String HEIGHT = "height";
    public static String FEE = "fee";
    public static String TRADE_ID = "trade_id";
    @Bind(R.id.img)
    ImageView mImgView;
    @Bind(R.id.tv_out_in)
    TextView mTvOutIn;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.state_success)
    RelativeLayout mSuccess;
    @Bind(R.id.state_sure)
    TextView mSureIng;
    @Bind(R.id.tv_in_address)
    TextView mTvInAddress;
    @Bind(R.id.tv_out_address)
    TextView mTvOutAddress;
    @Bind(R.id.tv_fee)
    TextView mTvFee;
    @Bind(R.id.tv_height)
    TextView mTvHeight;
    @Bind(R.id.tv_trade_code)
    TextView mTvTradeCode;
    @Bind(R.id.tv_trade_type)
    TextView mTvTradeType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_remain_info;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        if (!getIntent().getBooleanExtra(FROM_PUSH, false)) {
            Balance balance = (Balance) getIntent().getSerializableExtra(BALANCE);
            mTvCount.setText(Util.point(balance.getChangeValue(), 6) + " TSC");
            if (balance.getReceiveAddress().equals(UserPreference.getString(UserPreference.ADDRESS, ""))) { // 收款
                mImgView.setImageResource(R.mipmap.shou_icon);
                mTvOutIn.setText(getString(R.string.collect1));
            } else { // 转账
                mImgView.setImageResource(R.mipmap.zhuan_icon);
                mTvOutIn.setText(getString(R.string.currency1));
            }
            if (balance.getBlockHeight() != 0) {
                mSuccess.setVisibility(View.VISIBLE);
                mSureIng.setVisibility(View.GONE);
            } else {
                mSuccess.setVisibility(View.GONE);
                mSureIng.setVisibility(View.VISIBLE);
            }
            mTvInAddress.setText(balance.getReceiveAddress());
            mTvOutAddress.setText(balance.getSendAddress());
            mTvFee.setText(Util.point(balance.getServiceFee(), 6) + "TSC");
            mTvHeight.setText(balance.getBlockHeight() + "");
            mTvTradeCode.setText(balance.getTransationId());
            mTvTradeType.setText(balance.getChangeAction());
        } else {
            mImgView.setImageResource(R.mipmap.shou_icon);
            mTvOutIn.setText(getString(R.string.collect1));
            mTvCount.setText(Util.point(Double.parseDouble(getIntent().getStringExtra(COUNT)), 6) + " TSC");
            mSuccess.setVisibility(View.VISIBLE);
            mSureIng.setVisibility(View.GONE);
            mTvInAddress.setText(UserPreference.getString(UserPreference.ADDRESS, ""));
            mTvOutAddress.setText(getIntent().getStringExtra(ADDRESS));
            mTvFee.setText(Util.point(Double.parseDouble(getIntent().getStringExtra(FEE)), 6) + "TSC");
            mTvHeight.setText(getIntent().getStringExtra(HEIGHT));
            mTvTradeCode.setText(getIntent().getStringExtra(TRADE_ID));
            mTvTradeType.setText(getString(R.string.currency1));
        }
    }

    @OnClick({R.id.img_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}

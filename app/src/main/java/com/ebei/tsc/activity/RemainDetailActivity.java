package com.ebei.tsc.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Balance;
import com.ebei.library.pull.AutoPullAbleGridView;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.adapter.BalanceAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/31.
 * 余额明细页面
 */

public class RemainDetailActivity extends BaseActivity {

    private static final String TAG = "RemainDetailActivity";
    @Bind(R.id.gv_remain_list)
    AutoPullAbleGridView mListView;
    @Bind(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Bind(R.id.pb)
    ProgressBar mPb;
    private BalanceAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_remain_detail;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        getRemainList();
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.BALANCE_LIST, new Action1<List<Balance>>() {
            @Override
            public void call(List<Balance> o) {
                showBalanceList(o);
            }
        });
    }

    private void showBalanceList(List<Balance> list) {
        mPb.setVisibility(View.GONE);
        if (null != list && list.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (null == mAdapter) {
                mAdapter = new BalanceAdapter(this, list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setItems(list);
            }
        } else {
            mLlNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            if (null != mAdapter) mAdapter.setItems(list);
        }
    }

    private void getRemainList() {
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + "&userWalletType=2" + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().findBalanceList(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""), "2");
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

package com.ebei.tsc.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Miner;
import com.ebei.library.pull.AutoPullAbleGridView;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.adapter.IncomeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/31.
 * 矿池明细页面
 */

public class TradeDetailActivity extends BaseActivity {

    private static final String TAG = "TradeDetailActivity";
    @Bind(R.id.gv_income_list)
    AutoPullAbleGridView mListView;
    @Bind(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Bind(R.id.pb)
    ProgressBar mPb;
    private IncomeAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_detail;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        getIncomeList();
    }

    @Override
    protected void initData() {
        super.initData();
//        rxManage.on(Event.INCOME_LIST, new Action1<List<Income>>() {
//            @Override
//            public void call(List<Income> o) {
//                showIncomeList(o);
//            }
//        });

        rxManage.on(Event.MINER_DETAIL, new Action1<List<Miner>>() {
            @Override
            public void call(List<Miner> o) {
                showIncomeList(o);
            }
        });
    }

    private void showIncomeList(List<Miner> list) {
        mPb.setVisibility(View.GONE);
        if (null != list && list.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (null == mAdapter) {
                mAdapter = new IncomeAdapter(this, list);
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

    private void getIncomeList() {
//        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
//        sign = Util.encrypt(sign);
//        TSCApi.getInstance().getIncomeList(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""));
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().minerDetail(0, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
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

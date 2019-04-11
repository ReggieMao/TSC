package com.ebei.tsc.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseFragment;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Miner;
import com.ebei.library.pojo.MineralAsset;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.MinerInActivity;
import com.ebei.tsc.activity.MinerOutActivity;
import com.ebei.tsc.activity.TradeDetailActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/29.
 * 矿池页面
 */

public class MineralFragment extends BaseFragment {

    private static final String TAG = "MineralFragment";
    private BaseActivity mBaseActivity;
    private int mPosition = -1;
    @Bind(R.id.tv_all_asset)
    TextView mTvAllAsset;
    @Bind(R.id.tv_yesterday)
    TextView mTvYesterday;
    @Bind(R.id.tv_all)
    TextView mTvAll;
    @Bind(R.id.img_circle)
    ImageView mImgCircle;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mineral;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onChangeFragment(int position) {
        super.onChangeFragment(position);
        if (mPosition == -1) {
            mPosition = position;
            initViewDelayedLoading();
        }
    }

    private void initViewDelayedLoading() {
        mBaseActivity = (BaseActivity) getActivity();
//        if (UserPreference.getInt(UserPreference.HAS_MINER_IN, 0) == 1) {
//            if (!Util.getNowDate().equals(UserPreference.getString(UserPreference.NOW_DATE, ""))) {
//                mImgCircle.setVisibility(View.VISIBLE);
//            } else {
//                mImgCircle.setVisibility(View.GONE);
//            }
//        }
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().minerDetail(1, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        TSCApi.getInstance().mineralAsset(1, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        initDataDelayedLoading();
    }

    private void initDataDelayedLoading() {
        mBaseActivity.rxManage.on(Event.MINERAL_ASSET1, new Action1<MineralAsset>() {
            @Override
            public void call(MineralAsset o) {
                mTvYesterday.setText(Util.point(o.getOreAmount(), 6));
                mTvAll.setText(Util.point(o.getTotalOreAmount(), 6));
                mTvAllAsset.setText(Util.point(o.getTotalIncomeInf(), 6));
            }
        });

        mBaseActivity.rxManage.on(Event.MINER_DETAIL1, new Action1<List<Miner>>() {
            @Override
            public void call(List<Miner> o) {
                if (o.size() != UserPreference.getInt(UserPreference.MINER_COUNT, 0))
                    mImgCircle.setVisibility(View.VISIBLE);
                else
                    mImgCircle.setVisibility(View.GONE);
                UserPreference.putInt(UserPreference.MINER_COUNT, o.size());
            }
        });
    }

    @OnClick({R.id.rl_detail, R.id.ll_in, R.id.ll_out})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_detail:
                startActivity(new Intent(getActivity(), TradeDetailActivity.class));
                break;
            case R.id.ll_in:
                Intent intent = new Intent(getActivity(), MinerInActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_out:
                startActivity(new Intent(getActivity(), MinerOutActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewDelayedLoading();
    }

}

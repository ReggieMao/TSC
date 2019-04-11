package com.ebei.tsc.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseFragment;
import com.ebei.library.base.RxManage;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.util.UserPreference;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.AboutUsActivity;
import com.ebei.tsc.activity.INFBackActivity;
import com.ebei.tsc.activity.InviteFriendActivity;
import com.ebei.tsc.activity.SettingActivity;
import com.ebei.tsc.activity.VoteActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/8/29.
 * 我的页面
 */

public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";
    private BaseActivity mBaseActivity;
    private int mPosition = -1;
    @Bind(R.id.tv_id)
    TextView mTvId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
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
        mTvId.setText("ID: " + UserPreference.getString(UserPreference.ACCOUNT, ""));
        initDataDelayedLoading();
    }

    private void initDataDelayedLoading() {

    }

    @OnClick({R.id.ll_vote, R.id.ll_invalid, R.id.ll_setting, R.id.ll_about})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_vote:
//                new RxManage().post(Event.TO_TAB2, null);
                Intent intent = new Intent(getActivity(), VoteActivity.class);
                intent.putExtra(VoteActivity.URL, Constants.VOTE_ADDRESS + "?appSessionId=" + UserPreference.getString(UserPreference.SESSION_ID, "") +
                    "&secret=" + UserPreference.getString(UserPreference.SECRET, ""));
                startActivity(intent);
                break;
            case R.id.ll_invalid:
//                new RxManage().post(Event.TO_TAB1, null);
//                startActivity(new Intent(getActivity(), InviteFriendActivity.class));
                Intent intent1 = new Intent(getActivity(), INFBackActivity.class);
                intent1.putExtra(VoteActivity.URL, Constants.INF_ADDRESS + "?appSessionId=" + UserPreference.getString(UserPreference.SESSION_ID, "") +
                        "&secret=" + UserPreference.getString(UserPreference.SECRET, ""));
                startActivity(intent1);
                break;
            case R.id.ll_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }
    }

}

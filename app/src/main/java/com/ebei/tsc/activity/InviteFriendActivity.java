package com.ebei.tsc.activity;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Invite;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/27.
 * 邀请好友页面
 */

public class InviteFriendActivity extends BaseActivity {

    private static final String TAG = "InviteFriendActivity";
    @Bind(R.id.tv_url)
    TextView mTvUrl;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.tv_amount)
    TextView mTvAmount;
    @Bind(R.id.tv_rule)
    TextView mTvRule;
    private String mUrl = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        mTvRule.setMovementMethod(ScrollingMovementMethod.getInstance());
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().getInviteUrl(UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        TSCApi.getInstance().inviteQuery(UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.GET_INVITE_URL, new Action1<String>() {
            @Override
            public void call(String o) {
                mUrl = o;
                mTvUrl.setText(o);
            }
        });

        rxManage.on(Event.INVITE_QUERY, new Action1<Invite>() {
            @Override
            public void call(Invite o) {
                mTvCount.setText(o.getInvite_count() + getString(R.string.people));
                mTvAmount.setText(Util.point(o.getInvite_amount(), 6) + getString(R.string.tsc));
            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_invite})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_invite:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mUrl);
                ToastUtil.toast(this, getString(R.string.toast_copy_success));
                break;
        }
    }

}

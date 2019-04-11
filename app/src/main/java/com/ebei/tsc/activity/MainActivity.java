package com.ebei.tsc.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseApp;
import com.ebei.library.base.BaseFragment;
import com.ebei.library.constant.Constants;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.AppVersion;
import com.ebei.library.pojo.Market;
import com.ebei.library.pojo.RealmTransfer;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.library.view.SwipeViewPager;
import com.ebei.tsc.R;
import com.ebei.tsc.adapter.FragmentAdapter;
import com.ebei.tsc.fragment.MessageFragment;
import com.ebei.tsc.fragment.MineFragment;
import com.ebei.tsc.fragment.MineralFragment;
import com.ebei.tsc.fragment.WalletFragment;

import java.util.List;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/29.
 * 主页面
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.view_pager)
    SwipeViewPager mSwipeViewPager;
    @Bind(R.id.radio_group)
    RadioGroup mRadioGroup;
    private BaseFragment mBaseFragment;
    private MineralFragment mMineralFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;
    @Bind(R.id.img_message)
    ImageView mImgMessage;
    private Realm mRealm = Realm.getDefaultInstance();
    private boolean hasNoticeUnread = false;
    private boolean hasTraderUnread = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mImgMessage.getLayoutParams());
        int left = Util.getScreenWidth(this) / 8 * 5 + Util.dp2Px(this, 6);
        lp.setMargins(left, 3, 0, 0);
        mImgMessage.setLayoutParams(lp);
        WalletFragment mWalletFragment = new WalletFragment();
        mMineralFragment = new MineralFragment();
        mMessageFragment = new MessageFragment();
        mMineFragment = new MineFragment();
        mSwipeViewPager.setSwipeEnable(false);
        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAdapter.addFragment(mWalletFragment);
        mAdapter.addFragment(mMineralFragment);
        mAdapter.addFragment(mMessageFragment);
        mAdapter.addFragment(mMineFragment);
        mSwipeViewPager.setAdapter(mAdapter);
        mRadioGroup.setOnCheckedChangeListener(radioGroupListener);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "audienceId=" + JPushInterface.getRegistrationID(this) + "&submitTime=" +
                Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().saveId(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""), JPushInterface.getRegistrationID(this));
        String sign1 = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign1 = Util.encrypt(sign1);
        TSCApi.getInstance().findNewsList(0, Util.getNowTime(), sign1, UserPreference.getString(UserPreference.SESSION_ID, ""));
        String sign2 = UserPreference.getString(UserPreference.SECRET, "") + "appSystem=android&submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign2 = Util.encrypt(sign2);
        TSCApi.getInstance().getLatestAppVersion(UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign2, "android");
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.RE_LOGIN, new Action1() {
            @Override
            public void call(Object o) {
                UserPreference.putString(UserPreference.SESSION_ID, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        rxManage.on(Event.SYSTEM_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                mImgMessage.setVisibility(View.VISIBLE);
            }
        });

        rxManage.on(Event.TRANS_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                mImgMessage.setVisibility(View.VISIBLE);
            }
        });

        rxManage.on(Event.ALL_READ, new Action1() {
            @Override
            public void call(Object o) {
                mImgMessage.setVisibility(View.GONE);
            }
        });

        rxManage.on(Event.SAVE_ID, new Action1() {
            @Override
            public void call(Object o) {
                Log.d(TAG, "registerId上传成功！");
            }
        });

        rxManage.on(Event.FIND_NEWS_LIST, new Action1<Market>() {
            @Override
            public void call(Market o) {
                RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).equalTo("hasRead", false).findAll();
                hasTraderUnread = results.size() > 0;
                hasNoticeUnread = o.getUnreadNum() != 0;
                if (hasNoticeUnread || hasTraderUnread)
                    mImgMessage.setVisibility(View.VISIBLE);
                else
                    mImgMessage.setVisibility(View.GONE);
            }
        });

        rxManage.on(Event.APP_VERSION, new Action1<AppVersion>() {
            @Override
            public void call(AppVersion o) {
                if (!o.getAppVersion().equals(Constants.APP_VERSION)) {
                    DialogUtil.versionUpdateDialog(MainActivity.this, o.getAppVersion(), o.getIntroduce(), new DialogUtil.OnResultListener0() {
                        @Override
                        public void onOK() {
                            Intent intent = new Intent(MainActivity.this, AppDownloadActivity.class);
                            intent.putExtra(AppDownloadActivity.URL, o.getLoadUrl());
                            startActivity(intent);
                        }
                    });
                }
            }
        });

//        rxManage.on(Event.TO_TAB1, new Action1() {
//            @Override
//            public void call(Object o) {
//                mSwipeViewPager.setCurrentItem(0, false);
//                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
//            }
//        });

//        rxManage.on(Event.TO_TAB2, new Action1() {
//            @Override
//            public void call(Object o) {
//                mSwipeViewPager.setCurrentItem(1, false);
//                ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(true);
//            }
//        });
    }

    RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_wallet: // 钱包
                    if (null != mSwipeViewPager) {
                        mSwipeViewPager.setCurrentItem(0, false);
                    }
                    break;
                case R.id.rb_mineral: // 矿池
                    if (null != mSwipeViewPager) {
                        mSwipeViewPager.setCurrentItem(1, false);
                    }
                    mBaseFragment = mMineralFragment;
                    break;
                case R.id.rb_message: // 消息
                    if (null != mSwipeViewPager) {
                        mSwipeViewPager.setCurrentItem(2, false);
                    }
                    mBaseFragment = mMessageFragment;
                    break;
                case R.id.rb_mine: // 我的
                    if (null != mSwipeViewPager) {
                        mSwipeViewPager.setCurrentItem(3, false);
                    }
                    mBaseFragment = mMineFragment;
                    break;
            }
            if (mBaseFragment != null) {
                mBaseFragment.onChangeFragment(checkedId);
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        String sign1 = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign1 = Util.encrypt(sign1);
        TSCApi.getInstance().findNewsList(0, Util.getNowTime(), sign1, UserPreference.getString(UserPreference.SESSION_ID, ""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
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

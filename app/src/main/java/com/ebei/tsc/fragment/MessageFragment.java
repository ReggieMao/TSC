package com.ebei.tsc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseFragment;
import com.ebei.library.base.RxManage;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Market;
import com.ebei.library.pojo.RealmTransfer;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.NoticeListActivity;
import com.ebei.tsc.activity.RemainInfoActivity;
import com.ebei.tsc.adapter.MessageAdapter;
import com.ebei.tsc.manager.SwipeMenu;
import com.ebei.tsc.manager.SwipeMenuCreator;
import com.ebei.tsc.manager.SwipeMenuItem;
import com.ebei.tsc.view.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/29.
 * 消息页面
 */

public class MessageFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";
    private BaseActivity mBaseActivity;
    private int mPosition = -1;
    @Bind(R.id.gv_message_list)
    SwipeMenuListView mListView;
    @Bind(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Bind(R.id.pb)
    ProgressBar mPb;
    @Bind(R.id.layout)
    LinearLayout mLlHead;
    @Bind(R.id.img_circle)
    ImageView mImgCircle;
    private ImageView mImgCircle1;
    private MessageAdapter mAdapter;
    private List<RealmTransfer> mList = new ArrayList<>();
    private Realm mRealm = Realm.getDefaultInstance();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
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
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.view_message_head, null);
        LinearLayout layout = mView.findViewById(R.id.layout);
        mImgCircle1 = mView.findViewById(R.id.img_circle);
        getNoticeList();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NoticeListActivity.class));
            }
        });
        if (mListView.getHeaderViewsCount() == 0)
            mListView.addHeaderView(mView);
        SwipeMenuCreator menuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                deleteItem.setWidth(220);
                deleteItem.setIcon(R.mipmap.news_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(menuCreator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).equalTo("id", mList.get(position).getId()).findAll();
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                results.deleteAllFromRealm();
                            }
                        });
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        if (mList.size() == 0) {
                            mLlNoData.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                            mLlHead.setVisibility(View.VISIBLE);
                        } else {
                            mLlNoData.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            mLlHead.setVisibility(View.GONE);
                        }
                        break;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), RemainInfoActivity.class);
                intent.putExtra(RemainInfoActivity.FROM_PUSH, true);
                intent.putExtra(RemainInfoActivity.COUNT, String.valueOf(mList.get(i - 1).getBalance()));
                intent.putExtra(RemainInfoActivity.ADDRESS, mList.get(i - 1).getInAddress());
                intent.putExtra(RemainInfoActivity.FEE, String.valueOf(mList.get(i - 1).getFee()));
                intent.putExtra(RemainInfoActivity.HEIGHT, mList.get(i - 1).getHeight());
                intent.putExtra(RemainInfoActivity.TRADE_ID, mList.get(i - 1).getId());
                getActivity().startActivity(intent);
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).equalTo("id", mList.get(i - 1).getId()).findAll();
                        if (results.size() > 0) {
                            for (RealmTransfer realmTransfer : results) {
                                realmTransfer.setHasRead(true);
                            }
                        }
                    }
                });
            }
        });
        getTransList();
        initDataDelayedLoading();
    }

    private void initDataDelayedLoading() {
        mBaseActivity.rxManage.on(Event.FIND_NEWS_LIST2, new Action1<Market>() {
            @Override
            public void call(Market o) {
                if (o.getUnreadNum() != 0) {
                    mImgCircle.setVisibility(View.VISIBLE);
                    mImgCircle1.setVisibility(View.VISIBLE);
                } else {
                    mImgCircle.setVisibility(View.GONE);
                    mImgCircle1.setVisibility(View.GONE);
                }
            }
        });

        mBaseActivity.rxManage.on(Event.SYSTEM_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                mImgCircle.setVisibility(View.VISIBLE);
                mImgCircle1.setVisibility(View.VISIBLE);
            }
        });

        mBaseActivity.rxManage.on(Event.TRANS_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                mImgCircle.setVisibility(View.VISIBLE);
                mImgCircle1.setVisibility(View.VISIBLE);
                getTransList();
            }
        });

        mBaseActivity.rxManage.on(Event.MAKE_ALL_READ, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(getActivity(), getString(R.string.all_read_success));
                mImgCircle.setVisibility(View.GONE);
                mImgCircle1.setVisibility(View.GONE);
                new RxManage().post(Event.ALL_READ, null);
            }
        });
    }

    private void getTransList() {
        mList.clear();
        RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).findAll();
        for (int i = results.size() - 1; i > -1; i --) {
            mList.add(results.get(i));
        }
//        for (RealmTransfer transfer : results) {
//            mList.add(transfer);
//        }
        showTransList(mList);
    }

    private void showTransList(List<RealmTransfer> list) {
        mPb.setVisibility(View.GONE);
        if (null != list && list.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mLlHead.setVisibility(View.GONE);
            if (null == mAdapter) {
                mAdapter = new MessageAdapter(getActivity(), list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setItems(list);
            }
        } else {
            mLlNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mLlHead.setVisibility(View.VISIBLE);
            if (null != mAdapter) mAdapter.setItems(list);
        }
    }

    private void getNoticeList() {
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().findNewsList(2, Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""));
    }

    @OnClick({R.id.layout, R.id.tv_all})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.layout:
                startActivity(new Intent(getActivity(), NoticeListActivity.class));
                break;
            case R.id.tv_all:
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<RealmTransfer> results = mRealm.where(RealmTransfer.class).findAll();
                        if (results.size() > 0) {
                            for (RealmTransfer realmTransfer : results) {
                                realmTransfer.setHasRead(true);
                            }
                        }
                    }
                });
                String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                sign = Util.encrypt(sign);
                TSCApi.getInstance().markAllRead(sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getNoticeList();
        getTransList();
    }

}

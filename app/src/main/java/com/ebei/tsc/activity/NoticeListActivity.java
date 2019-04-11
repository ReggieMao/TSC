package com.ebei.tsc.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Market;
import com.ebei.library.pojo.Notice;
import com.ebei.library.pull.AutoPullAbleGridView;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.adapter.NoticeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/9/11.
 * 公告列表页面
 */

public class NoticeListActivity extends BaseActivity {

    private static final String TAG = "NoticeListActivity";
    @Bind(R.id.gv_notice_list)
    AutoPullAbleGridView mListView;
    @Bind(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Bind(R.id.pb)
    ProgressBar mPb;
    private NoticeAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_list;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        getNoticeList();
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.SYSTEM_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                getNoticeList();
            }
        });

        rxManage.on(Event.FIND_NEWS_LIST1, new Action1<Market>() {
            @Override
            public void call(Market o) {
                showNoticeList(o.getNoticeList());
            }
        });
    }

    private void showNoticeList(List<Notice> list) {
        mPb.setVisibility(View.GONE);
        if (null != list && list.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (null == mAdapter) {
                mAdapter = new NoticeAdapter(this, list);
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

    private void getNoticeList() {
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().findNewsList(1, Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""));
    }

    @OnClick({R.id.img_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNoticeList();
    }

}

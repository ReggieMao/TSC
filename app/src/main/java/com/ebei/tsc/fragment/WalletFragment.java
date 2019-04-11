package com.ebei.tsc.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebei.library.api.TSCApi;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.base.BaseFragment;
import com.ebei.library.constant.Event;
import com.ebei.library.pojo.Book;
import com.ebei.library.pojo.MineralAsset;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.CollectActivity;
import com.ebei.tsc.activity.RemainDetailActivity;
import com.ebei.tsc.activity.TransferActivity;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/8/29.
 * 钱包页面
 */

public class WalletFragment extends BaseFragment {

    private static final String TAG = "WalletFragment";
    private BaseActivity mBaseActivity;
    @Bind(R.id.tv_asset)
    TextView mTvAsset;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_yesterday)
    TextView mTvYesterday;
    @Bind(R.id.img_point)
    ImageView mImgPoint;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    public void initView() {
        mBaseActivity = (BaseActivity) getActivity();
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        TSCApi.getInstance().getUserBook(0, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
        TSCApi.getInstance().mineralAsset(0, UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign);
    }

    @Override
    protected void initData() {
        super.initData();
        mBaseActivity.rxManage.on(Event.USER_BOOK, new Action1<Book>() {
            @Override
            public void call(Book o) {
                UserPreference.putString(UserPreference.ADDRESS, o.getWalletAddress());
                mTvAsset.setText(Util.point(o.getBookBalance(), 6));
                mTvAddress.setText(o.getWalletAddress());
                if (o.isNewRecord())
                    mImgPoint.setVisibility(View.VISIBLE);
                else
                    mImgPoint.setVisibility(View.GONE);
            }
        });

        mBaseActivity.rxManage.on(Event.MINERAL_ASSET, new Action1<MineralAsset>() {
            @Override
            public void call(MineralAsset o) {
                String s = "";
                if (o.getOreAmount() > 0)
                    s = "+";
                mTvYesterday.setText(getString(R.string.yesterday) + s + Util.point(o.getOreAmount(), 6));
            }
        });
    }

    @OnClick({R.id.rl_collect, R.id.rl_transfer, R.id.rl_detail, R.id.img_copy})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_collect:
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                intent.putExtra(CollectActivity.ADDRESS, mTvAddress.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_transfer:
                Intent intent1 = new Intent(getActivity(), TransferActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_detail:
                Intent intent2 = new Intent(getActivity(), RemainDetailActivity.class);
                startActivity(intent2);
                break;
            case R.id.img_copy:
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mTvAddress.getText().toString());
                ToastUtil.toast(getActivity(), getString(R.string.toast_copy_success));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

}

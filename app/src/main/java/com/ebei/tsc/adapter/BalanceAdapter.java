package com.ebei.tsc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.pojo.Balance;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;
import com.ebei.library.view.CommonVH;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.RemainInfoActivity;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 余额明细适配器
 */

public class BalanceAdapter extends CommonListViewAdapter<Balance> {

    private static final String TAG = "BalanceAdapter";
    private Activity mActivity;
    public BalanceAdapter(Activity activity, List<Balance> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_balance, position);

        LinearLayout layout = holder.getView(R.id.layout);
        View view = holder.getView(R.id.view);
        TextView outIn = holder.getView(R.id.tv_out_in);
        TextView time = holder.getView(R.id.tv_time);
        TextView remain = holder.getView(R.id.tv_balance);

        Balance balance = mDatas.get(position);

        if (!balance.getReceiveAddress().equals(UserPreference.getString(UserPreference.ADDRESS, ""))) {
            view.setBackgroundResource(R.drawable.bg_round_text_green);
            remain.setTextColor(mActivity.getColor(R.color.green));
            remain.setText("-" + Util.point(balance.getChangeValue(), 6));
            outIn.setText(mActivity.getString(R.string.tsc_out));
        } else {
            view.setBackgroundResource(R.drawable.bg_round_text_blue);
            remain.setTextColor(mActivity.getColor(R.color.textBlue));
            remain.setText("+" + Util.point(balance.getChangeValue(), 6));
            outIn.setText(mActivity.getString(R.string.tsc_in));
        }
        time.setText(balance.getUpdateDate());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, RemainInfoActivity.class);
                intent.putExtra(RemainInfoActivity.BALANCE, balance);
                mActivity.startActivity(intent);
            }
        });

        return holder.getConvertView();
    }

}

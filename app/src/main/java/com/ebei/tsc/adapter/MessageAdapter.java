package com.ebei.tsc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.pojo.Notice;
import com.ebei.library.pojo.RealmTransfer;
import com.ebei.library.util.Util;
import com.ebei.library.view.CommonVH;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.NoticeInfoActivity;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 转账消息适配器
 */

public class MessageAdapter extends CommonListViewAdapter<RealmTransfer> {

    private static final String TAG = "MessageAdapter";
    private Activity mActivity;
    public MessageAdapter(Activity activity, List<RealmTransfer> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_message, position);

        TextView time = holder.getView(R.id.tv_time);
        TextView content = holder.getView(R.id.tv_content);
        ImageView circle = holder.getView(R.id.img_circle);

        RealmTransfer transfer = mDatas.get(position);

        time.setText(Util.getDateToString(transfer.getTimeStamp()));
        content.setText("+ " + Util.point(transfer.getBalance(), 6) + "TSC");
        if (transfer.isHasRead())
            circle.setVisibility(View.GONE);
        else
            circle.setVisibility(View.VISIBLE);

        return holder.getConvertView();
    }

}

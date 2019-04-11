package com.ebei.tsc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.pojo.Notice;
import com.ebei.library.view.CommonVH;
import com.ebei.tsc.R;
import com.ebei.tsc.activity.NoticeInfoActivity;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 公告适配器
 */

public class NoticeAdapter extends CommonListViewAdapter<Notice> {

    private static final String TAG = "NoticeAdapter";
    private Activity mActivity;
    public NoticeAdapter(Activity activity, List<Notice> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_notice, position);

        LinearLayout layout = holder.getView(R.id.layout);
        TextView time = holder.getView(R.id.tv_time);
        TextView content = holder.getView(R.id.tv_content);
        ImageView circle = holder.getView(R.id.img_circle);

        Notice notice = mDatas.get(position);

        time.setText(notice.getBeginTime());
        content.setText(notice.getNoticeTitle());
        if (notice.isRead())
            circle.setVisibility(View.GONE);
        else
            circle.setVisibility(View.VISIBLE);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NoticeInfoActivity.class);
                intent.putExtra(NoticeInfoActivity.URL, notice.getNoticeUrl());
                mActivity.startActivity(intent);
            }
        });

        return holder.getConvertView();
    }

}

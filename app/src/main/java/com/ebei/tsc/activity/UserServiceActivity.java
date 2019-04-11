package com.ebei.tsc.activity;

import android.view.View;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/9/12.
 * 用户服务协议页面
 */

public class UserServiceActivity extends BaseActivity {

    private static final String TAG = "UserServiceActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_service;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
    }

    @OnClick({R.id.img_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}

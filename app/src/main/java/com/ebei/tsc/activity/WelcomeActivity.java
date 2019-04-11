package com.ebei.tsc.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ebei.tsc.R;
import com.ebei.tsc.application.MyApplication;
import com.ebei.library.base.BaseActivity;
import com.ebei.library.util.UserPreference;
import com.ebei.library.util.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MaoLJ on 2018/8/15.
 * 启动页
 */

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";
    Timer timer = new Timer();
    private int recLen = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        Util.immersiveStatus(this, true);
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    recLen --;
                    if (recLen == 0) {
                        timer.cancel();
                        jumpOtherPage();
                    }
                }
            });
        }
    };

    private void jumpOtherPage() {
        if (!Util.isEmpty(UserPreference.getString(UserPreference.SESSION_ID, ""))) {
            startActivity(new Intent(this, MainActivity.class));
        } else
            startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

}

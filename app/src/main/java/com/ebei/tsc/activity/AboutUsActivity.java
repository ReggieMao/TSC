package com.ebei.tsc.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.constant.Constants;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/9/3.
 * 关于我们页面
 */

public class AboutUsActivity extends BaseActivity {

    private static final String TAG = "AboutUsActivity";
    @Bind(R.id.tv_version)
    TextView mTvVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        mTvVersion.setText("V " + Constants.APP_VERSION);
    }

    @OnClick({R.id.img_back, R.id.ll_email, R.id.ll_web})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_email:
                DialogUtil.logoutDialog(this, 2, getString(R.string.email1), new DialogUtil.OnResultListener4() {
                    @Override
                    public void select1() {
                        Uri uri = Uri.parse("mailto:" + getString(R.string.email1));
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
                        startActivity(Intent.createChooser(intent, getString(R.string.select_email)));
                    }

                    @Override
                    public void select2() {

                    }
                });
                break;
            case R.id.ll_web:
                Intent intent = new Intent(this, OfficeWebActivity.class);
                intent.putExtra(OfficeWebActivity.URL, getString(R.string.office_web1));
                startActivity(intent);
                break;
        }
    }

}

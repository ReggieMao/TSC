package com.ebei.tsc.activity;

import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/9/7.
 * INF返还页面
 */

public class INFBackActivity extends BaseActivity {

    private static final String TAG = "INFBackActivity";
    public static String URL = "url";
    @Bind(R.id.web)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inf_back;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        String url = getIntent().getStringExtra(URL);
        mWebView.clearCache(true);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDomStorageEnabled(false);
        mWebView.getSettings().setDatabaseEnabled(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        mWebView.clearCache(true);
                        mWebView.loadUrl(url);
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        mWebView.getSettings().setAllowFileAccess(true);
                        mWebView.getSettings().setSupportZoom(true);
                        mWebView.getSettings().setBuiltInZoomControls(true);
                        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                        mWebView.getSettings().setDomStorageEnabled(false);
                        mWebView.getSettings().setDatabaseEnabled(false);
                        mWebView.getSettings().setLoadWithOverviewMode(true);
                        mWebView.getSettings().setUseWideViewPort(true);
                        mWebView.getSettings().setAppCacheEnabled(false);
                        return true;
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });
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

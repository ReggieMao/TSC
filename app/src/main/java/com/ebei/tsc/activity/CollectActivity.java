package com.ebei.tsc.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebei.library.base.BaseActivity;
import com.ebei.library.util.DialogUtil;
import com.ebei.library.util.ToastUtil;
import com.ebei.library.util.Util;
import com.ebei.tsc.R;
import com.ebei.tsc.application.MyApplication;
import com.google.zxing.WriterException;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.yzq.zxinglibrary.encode.CodeCreator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/8/31.
 * 收款页面
 */

public class CollectActivity extends BaseActivity {

    private static final String TAG = "CollectActivity";
    public static String ADDRESS = "address";
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.img_code)
    ImageView mImgCode;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.et_money)
    EditText mEtMoney;
    @Bind(R.id.view)
    View mView;
    private String mAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        Util.addLayoutListener(mLlMain, mView);
        Util.setPoint(mEtMoney);
        mAddress = getIntent().getStringExtra(ADDRESS) + "-";
        Bitmap bitmap = null;
        try {
            bitmap = CodeCreator.createQRCode(mAddress.substring(0, mAddress.length() - 1), 400, 400, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mImgCode.setImageBitmap(bitmap);
        mTvAddress.setText(mAddress.substring(0, mAddress.length() - 1));
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newAddress = mAddress + editable;
                if (Util.isEmpty(editable.toString()))
                    newAddress = newAddress.substring(0, newAddress.length() - 1);
                Bitmap bitmap = null;
                try {
                    bitmap = CodeCreator.createQRCode(newAddress, 400, 400, null);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                mImgCode.setImageBitmap(bitmap);
            }
        });
    }

    public void sendToWeChat(int where) {
        mImgCode.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(mImgCode.getDrawingCache());
        mImgCode.setDrawingCacheEnabled(false);

        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (where == 0) { // 发到微信好友
            req.scene = SendMessageToWX.Req.WXSceneSession;
            MyApplication.getMyApplicationInstance().getIWXAPI().sendReq(req);
        } else { // 发到朋友圈
            if (MyApplication.getMyApplicationInstance().getIWXAPI().getWXAppSupportAPI() >= Build.TIMELINE_SUPPORTED_SDK_INT) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                MyApplication.getMyApplicationInstance().getIWXAPI().sendReq(req);
            } else
                ToastUtil.toast(this, getString(R.string.can_not_share));
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @OnClick({R.id.img_back, R.id.img_code, R.id.et_money, R.id.img_share})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_code:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mTvAddress.getText().toString());
                ToastUtil.toast(this, getString(R.string.toast_copy_success));
                break;
            case R.id.et_money:
                mEtMoney.setCursorVisible(true);
                break;
            case R.id.img_share:
                DialogUtil.shareDialog(this, new DialogUtil.OnResultListener3() {
                    @Override
                    public void select1() {
                        if (!MyApplication.getMyApplicationInstance().getIWXAPI().isWXAppInstalled()) {
                            ToastUtil.toast(CollectActivity.this, getString(R.string.no_wechat));
                            return;
                        }
                        sendToWeChat(0);
                    }

                    @Override
                    public void select2() {
                        if (!MyApplication.getMyApplicationInstance().getIWXAPI().isWXAppInstalled()) {
                            ToastUtil.toast(CollectActivity.this, getString(R.string.no_wechat));
                            return;
                        }
                        sendToWeChat(1);
                    }

                    @Override
                    public void select3() {

                    }

                    @Override
                    public void select4() {

                    }

                    @Override
                    public void select5() {

                    }
                });
                break;
        }
    }

}

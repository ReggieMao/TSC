package com.ebei.library.api;

import android.util.Log;

import com.ebei.library.base.RxManage;
import com.ebei.library.constant.Event;
import com.ebei.library.exception.ApiExceptionFunc;
import com.ebei.library.exception.ServerResultFunc;
import com.ebei.library.pojo.AppVersion;
import com.ebei.library.pojo.Balance;
import com.ebei.library.pojo.Book;
import com.ebei.library.pojo.Income;
import com.ebei.library.pojo.Invite;
import com.ebei.library.pojo.Login;
import com.ebei.library.pojo.Market;
import com.ebei.library.pojo.Miner;
import com.ebei.library.pojo.MineralAsset;
import com.ebei.library.pojo.Register;
import com.ebei.library.rxjava.DPSubscriber;

import java.util.List;

import rx.schedulers.Schedulers;

/**
 * Created by MaoLj on 2018/7/26.
 * 具体api
 */

public class TSCApi {

    private static final String TAG = "TSCApi";
    private TSCApi() {}
    private static TSCApi tscApi = new TSCApi();
    public static TSCApi getInstance() {
        return tscApi;
    }

    // 注册
    public void register(String bindMobile, String passWord, String payPassWord, String verifyCode, String sign, String submitTime) {
        Api.getInstance().service.register(bindMobile, passWord, payPassWord, sign, submitTime, verifyCode)
                .map(new ServerResultFunc<Register>())
                .onErrorResumeNext(new ApiExceptionFunc<Register>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Register>() {
                    @Override
                    public void onNext(Register data) {
                        new RxManage().post(Event.REGISTER, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 发送验证码
    public void getAuthCode(String mobile, String sign, String submitTime) {
        Api.getInstance().service.getAuthCode(mobile, sign, submitTime)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.GET_AUTH_CODE, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 验证码登录
    public void userLoginByVerifyCode(String mobile, String code, String sign, String submitTime) {
        Api.getInstance().service.userLoginByVerifyCode(mobile, code, sign, submitTime)
                .map(new ServerResultFunc<Login>())
                .onErrorResumeNext(new ApiExceptionFunc<Login>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Login>() {
                    @Override
                    public void onNext(Login data) {
                        new RxManage().post(Event.LOGIN_CODE, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 账号密码登录
    public void userLogin(String mobile, String password, String sign, String submitTime) {
        Api.getInstance().service.userLogin(mobile, password, sign, submitTime)
                .map(new ServerResultFunc<Login>())
                .onErrorResumeNext(new ApiExceptionFunc<Login>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Login>() {
                    @Override
                    public void onNext(Login data) {
                        new RxManage().post(Event.LOGIN_PWD, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 修改密码（支付密码和登录密码）
    public void restPwd(String mobile, String password, String confirmPassword, int pwdType, String verifyCode, String sign, String submitTime) {
        Api.getInstance().service.restPwd(mobile, password, confirmPassword, pwdType, verifyCode, sign, submitTime)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        if (pwdType == 1)
                            new RxManage().post(Event.REST_LOGIN_PWD, data);
                        else
                            new RxManage().post(Event.REST_PAY_PWD, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 矿池总资产（昨日收益）
    public void mineralAsset(int where, String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.mineralAsset(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<MineralAsset>())
                .onErrorResumeNext(new ApiExceptionFunc<MineralAsset>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<MineralAsset>() {
                    @Override
                    public void onNext(MineralAsset data) {
                        if (where == 0)
                            new RxManage().post(Event.MINERAL_ASSET, data);
                        else if (where == 1)
                            new RxManage().post(Event.MINERAL_ASSET1, data);
                        else
                            new RxManage().post(Event.MINERAL_ASSET2, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 总资产
    public void getUserBook(int where, String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.getUserBook(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<Book>())
                .onErrorResumeNext(new ApiExceptionFunc<Book>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Book>() {
                    @Override
                    public void onNext(Book data) {
                        if (where == 0)
                            new RxManage().post(Event.USER_BOOK, data);
                        else
                            new RxManage().post(Event.USER_BOOK1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 余额明细接口+详情
    public void findBalanceList(String sign, String submitTime, String appSessionId, String userWalletType) {
        Api.getInstance().service.findBalanceList(sign, submitTime, appSessionId, userWalletType)
                .map(new ServerResultFunc<List<Balance>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Balance>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<List<Balance>>() {
                    @Override
                    public void onNext(List<Balance> data) {
                        new RxManage().post(Event.BALANCE_LIST, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 转币
    public void transferToOthers(String sign, String submitTime, String appSessionId, String userWalletType, String outAddress, String transferAmount, String payPassword) {
        Api.getInstance().service.transferToOthers(sign, submitTime, appSessionId, userWalletType, outAddress, transferAmount, payPassword)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.TRANSFER_TO_OTHERS, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 转入/出矿池
    public void transferToOre(String sign, String submitTime, String appSessionId, String transferAmount, String payPassword, int operateType) {
        Api.getInstance().service.transferToOre(sign, submitTime, appSessionId, transferAmount, payPassword, operateType)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        if (operateType == 1)
                            new RxManage().post(Event.TRANSFER_TO_ORE, data);
                        else
                            new RxManage().post(Event.TRANSFER_TO_ORE1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 收益明细
    public void getIncomeList(String sign, String submitTime, String appSessionId) {
        Api.getInstance().service.getIncomeList(sign, submitTime, appSessionId)
                .map(new ServerResultFunc<List<Income>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Income>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<List<Income>>() {
                    @Override
                    public void onNext(List<Income> data) {
                        new RxManage().post(Event.INCOME_LIST, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 退出
    public void logout(String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.logout(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.LOGOUT, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // app登录id保存
    public void saveId(String sign, String submitTime, String appSessionId, String audienceId) {
        Api.getInstance().service.saveId(sign, submitTime, appSessionId, audienceId)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.SAVE_ID, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 公告列表
    public void findNewsList(int where, String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.findNewsList(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Market>())
                .onErrorResumeNext(new ApiExceptionFunc<Market>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Market>() {
                    @Override
                    public void onNext(Market data) {
                        if (where == 0)
                            new RxManage().post(Event.FIND_NEWS_LIST, data);
                        else if (where == 1)
                            new RxManage().post(Event.FIND_NEWS_LIST1, data);
                        else
                            new RxManage().post(Event.FIND_NEWS_LIST2, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 全部已读
    public void markAllRead(String sign, String submitTime, String appSessionId) {
        Api.getInstance().service.markAllRead(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.MAKE_ALL_READ, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 判断手机号是否已注册
    public void checkLoginAccount(String mobile, String sign, String submitTime) {
        Api.getInstance().service.checkLoginAccount(mobile, submitTime, sign)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        new RxManage().post(Event.CHECK_ACCOUNT, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 获取邀请链接
    public void getInviteUrl(String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.getInviteUrl(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        new RxManage().post(Event.GET_INVITE_URL, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 查询邀请人数和金额
    public void inviteQuery(String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.inviteQuery(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<Invite>())
                .onErrorResumeNext(new ApiExceptionFunc<Invite>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<Invite>() {
                    @Override
                    public void onNext(Invite data) {
                        new RxManage().post(Event.INVITE_QUERY, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 矿池明细
    public void minerDetail(int where, String appSessionId, String submitTime, String sign) {
        Api.getInstance().service.minerDetail(appSessionId, submitTime, sign)
                .map(new ServerResultFunc<List<Miner>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Miner>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<List<Miner>>() {
                    @Override
                    public void onNext(List<Miner> data) {
                        if (where == 0)
                            new RxManage().post(Event.MINER_DETAIL, data);
                        else
                            new RxManage().post(Event.MINER_DETAIL1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 获取最新版本
    public void getLatestAppVersion(String appSessionId, String submitTime, String sign, String appSystem) {
        Api.getInstance().service.getLatestAppVersion(appSessionId, submitTime, sign, appSystem)
                .map(new ServerResultFunc<AppVersion>())
                .onErrorResumeNext(new ApiExceptionFunc<AppVersion>())
                .subscribeOn(Schedulers.io())
                .subscribe(new DPSubscriber<AppVersion>() {
                    @Override
                    public void onNext(AppVersion data) {
                        new RxManage().post(Event.APP_VERSION, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

}

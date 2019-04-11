package com.ebei.library.api;

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
import com.ebei.library.pojo.RxResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by MaoLJ on 2018/7/18.
 * 接口服务类
 */

public interface ApiService {

    // 注册
    @GET("d-app/API/register")
    Observable<RxResult<Register>> register(@Query("bindMobile") String bindMobile, @Query("passWord") String passWord, @Query("payPassWord") String payPassWord, @Query("sign") String sign, @Query("submitTime") String submitTime, @Query("verifyCode") String verifyCode);

    // 发送验证码
    @GET("d-app/API/getAuthCode")
    Observable<RxResult<Object>> getAuthCode(@Query("mobile") String mobile, @Query("sign") String sign, @Query("submitTime") String submitTime);

    // 验证码登录
    @GET("d-app/API/userLoginByVerifyCode")
    Observable<RxResult<Login>> userLoginByVerifyCode(@Query("loginAccount") String loginAccount, @Query("verifyCode") String verifyCode, @Query("sign") String sign, @Query("submitTime") String submitTime);

    // 账号密码登录
    @GET("d-app/API/userLogin")
    Observable<RxResult<Login>> userLogin(@Query("loginAccount") String loginAccount, @Query("password") String password, @Query("sign") String sign, @Query("submitTime") String submitTime);

    // 修改密码（支付密码和登录密码）
    @GET("d-app/API/restPwd")
    Observable<RxResult<Object>> restPwd(@Query("loginAccount") String loginAccount, @Query("password") String password, @Query("confirmPassword") String confirmPassword, @Query("pwdType") int pwdType, @Query("verifyCode") String verifyCode, @Query("sign") String sign, @Query("submitTime") String submitTime);

    // 矿池总资产（昨日收益）
    @GET("d-app/inf-income/getIncome.json")
    Observable<RxResult<MineralAsset>> mineralAsset(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 总资产
    @GET("d-app/API/book/getUserBook")
    Observable<RxResult<Book>> getUserBook(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 余额明细接口+详情
    @GET("d-app/coins/income/findBalanceList.json")
    Observable<RxResult<List<Balance>>> findBalanceList(@Query("sign") String sign, @Query("submitTime") String submitTime, @Query("appSessionId") String appSessionId, @Query("userWalletType") String userWalletType);

    // 转币
    @GET("d-app/coins/Ore/transferToOthers.do")
    Observable<RxResult<Object>> transferToOthers(@Query("sign") String sign, @Query("submitTime") String submitTime, @Query("appSessionId") String appSessionId, @Query("userWalletType") String userWalletType, @Query("outAddress") String outAddress, @Query("transferAmount") String transferAmount, @Query("payPassword") String payPassword);

    // 转入/出矿池
    @GET("d-app/ore/tranferToOre.do")
    Observable<RxResult<Object>> transferToOre(@Query("sign") String sign, @Query("submitTime") String submitTime, @Query("appSessionId") String appSessionId, @Query("transferAmount") String transferAmount, @Query("payPassword") String payPassword, @Query("operateType") int operateType);

    // 收益明细
    @GET("d-app/inf-income/getIncomeList.json")
    Observable<RxResult<List<Income>>> getIncomeList(@Query("sign") String sign, @Query("submitTime") String submitTime, @Query("appSessionId") String appSessionId);

    // 退出
    @GET("d-app/API/logout")
    Observable<RxResult<Object>> logout(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // app登录id保存
    @GET("d-app/API/savaAudieanceId")
    Observable<RxResult<Object>> saveId(@Query("sign") String sign, @Query("submitTime") String submitTime, @Query("appSessionId") String appSessionId, @Query("audienceId") String audienceId);

    // 公告列表
    @GET("notice/findnewsList.json")
    Observable<RxResult<Market>> findNewsList(@Query("submitTime") String submitTime, @Query("sign") String sign, @Query("appSessionId") String appSessionId);

    // 全部已读
    @GET("notice/markAllRead.do")
    Observable<RxResult<Object>> markAllRead(@Query("submitTime") String submitTime, @Query("sign") String sign, @Query("appSessionId") String appSessionId);

    // 判断手机号是否已注册
    @GET("d-app/API/checkLoginAccount")
    Observable<RxResult<String>> checkLoginAccount(@Query("loginAccount") String loginAccount, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 获取邀请链接
    @GET("invite/getInviteUrl.json")
    Observable<RxResult<String>> getInviteUrl(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 查询邀请人数和金额
    @GET("invite/query.json")
    Observable<RxResult<Invite>> inviteQuery(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 矿池明细
    @GET("d-app/API/InfMid/findPooltransferList")
    Observable<RxResult<List<Miner>>> minerDetail(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign);

    // 获取最新版本
    @GET("d-app/getLastestAppVersion.json")
    Observable<RxResult<AppVersion>> getLatestAppVersion(@Query("appSessionId") String appSessionId, @Query("submitTime") String submitTime, @Query("sign") String sign, @Query("appSystem") String appSystem);

}



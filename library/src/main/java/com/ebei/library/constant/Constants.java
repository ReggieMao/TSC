package com.ebei.library.constant;

import com.ebei.library.util.Util;

/**
 * Created by MaoLJ on 2018/7/18.
 *
 */

public class Constants {

    /** 版本类型（1为测试版，2为正式版）*/
//    public static final int VERSION_TYPE = 1;
    public static final int VERSION_TYPE = 2;

    public static final boolean LOG_DEBUG = true;
    /** api返回成功code为10000*/
    public static final String API_SUCCESS = "10000";
    /** 服务器连接地址*/
    public static String BASE_URL = Util.getPropertiesURL("serverUrl", VERSION_TYPE);
    /** 加盐密码*/
    public static String SALT_CIPHER = "52a4b68bc7b11dd04e4a6e3da4bbf6ca";
    /** 竞选投票页面H5地址*/
    public static String VOTE_ADDRESS = BASE_URL + "vote/index.html";
    /** 投票记录页面H5地址*/
    public static String VOTE_RECORD_ADDRESS = BASE_URL + "vote/voteRecord.html";
    /** INF返还页面H5地址*/
    public static String INF_ADDRESS = BASE_URL + "return/index.html";
    /** App版本*/
    public static String APP_VERSION = "1.0.3";

}

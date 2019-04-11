package com.ebei.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/4.
 * 总资产
 */

@Data
public class Book implements Serializable {

    private String walletAddress;

    private double bookBalance;

    private boolean newRecord;

    private String bookCode;

    private int bookId;

    private String createTime;

    private double freezBalance;

    private int itemType;

    private int userId;

}

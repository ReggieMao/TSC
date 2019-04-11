package com.ebei.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/4.
 * 余额明细
 */

@Data
public class Balance implements Serializable {

    private double beforeChangeBalance;

    private double blockConformations;

    private int blockHeight;

    private String bookCode;

    private String changeAction;

    private double changeValue;

    private String contractAddress;

    private String createDate;

    private int id;

    private String receiveAddress;

    private String remark;

    private String sendAddress;

    private double serviceFee;

    private int status;

    private String transationId;

    private String updateDate;

    private String userId;

}

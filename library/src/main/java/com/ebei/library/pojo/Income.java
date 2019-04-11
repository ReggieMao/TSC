package com.ebei.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/4.
 * 收益
 */

@Data
public class Income implements Serializable {

    private String beginTime;

    private String createDate;

    private String endTime;

    private int id;

    private int incomeDate;

    private double incomeInf;

    private String loginAccount;

    private String modifyDate;

    private double oreAmount;

    private double realTotalOreAmount;

    private int status;

    private double totalIncomeInf;

    private double totalOreAmount;

    private int userId;

    private String userInfAddress;

}

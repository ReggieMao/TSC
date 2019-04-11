package com.ebei.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/29.
 * 矿池明细
 */

@Data
public class Miner implements Serializable {

    private double changeValue;

    private String createDate;

    private long incomeDate;

    private int withdrawInfStatus;

}

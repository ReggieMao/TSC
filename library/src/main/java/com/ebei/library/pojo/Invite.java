package com.ebei.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/28.
 * 邀请
 */

@Data
public class Invite implements Serializable {

    private int invite_count;

    private double invite_amount;

}

package com.ebei.library.pojo;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by MaoLJ on 2018/8/6.
 * 转账通知
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class RealmTransfer extends RealmObject implements Serializable {

    @PrimaryKey
    private long timeStamp;

    private double balance;

    private String inAddress;

    private boolean hasRead;

    private String height;

    private double fee;

    private String id;

}

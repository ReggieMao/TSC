package com.ebei.library.exception;

import com.ebei.library.constant.Constants;
import com.ebei.library.constant.DPCode;
import com.ebei.library.pojo.RxResult;

import rx.functions.Func1;

/**
 * Created by MaoLJ on 2018/7/18.
 *
 */

public class ServerResultFunc<T> implements Func1<RxResult<T>, T> {
    @Override
    public T call(RxResult<T> tRxResult) {
        if (!tRxResult.getErrCode().equals(Constants.API_SUCCESS)) {
            throw new ServerException(DPCode.BUSSINESS_EXCEPTION, tRxResult.getErrDesc());
        }
        return tRxResult.getResult();
    }
}


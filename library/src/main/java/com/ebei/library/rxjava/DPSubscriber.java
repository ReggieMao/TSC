package com.ebei.library.rxjava;

import android.util.Log;

import com.ebei.library.constant.Event;
import com.ebei.library.exception.ApiException;
import com.ebei.library.base.RxManage;

import rx.Subscriber;

/**
 * Created by MaoLJ on 2018/7/18.
 *
 */

public abstract class DPSubscriber<T> extends Subscriber<T>{

    private final static String TAG = DPSubscriber.class.getSimpleName();

    @Override
    public void onCompleted() {
        Log.d(TAG, "Subscriber onCompleted!");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "Subscriber onError == " + e.getMessage());
        new RxManage().post(Event.SIMPLE_EXCEPTION_HANDLE, (ApiException)e);
    }

}

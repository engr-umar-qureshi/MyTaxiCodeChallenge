package com.umarqureshi.api.network.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseApi {

    protected void executeApiCall(Observable observable, Consumer consumer, Consumer errorHandler) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, errorHandler);
    }
}

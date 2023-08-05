package com.tiny.cash.loan.card.utils;

import io.reactivex.observers.DisposableObserver;

/**
 * Default {@link DisposableObserver} base class to be used whenever you want default error handling.
 *
 * @author rainking
 */
public class DefObserver<T> extends DisposableObserver<T> {

    public DefObserver() {
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }

    @Override
    public void onComplete() {
        // no-op by default.
    }


    @Override
    public void onError(Throwable exception) {

    }


}

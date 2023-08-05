package com.tiny.cash.loan.card.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Default {@link DisposableObserver} base class to be used whenever you want default error handling.
 *
 * @author rainking
 */
public class NetObserver<T> extends DisposableObserver<T> implements INetException {

    public NetObserver() {
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
        exception.printStackTrace();
        if (exception instanceof HttpException) {
            //   HTTP错误
            onException(new ResponseException(exception, ((HttpException) exception).code(), "HTTP error"));
        } else if (exception instanceof ConnectException
                || exception instanceof UnknownHostException
                || exception instanceof SocketException) {
            //   连接错误
            onNetConnectErr(exception);
        } else if (exception instanceof InterruptedIOException) {
            //  连接超时
            onException(new ResponseException(exception, "Connection timeout"));
        } else if (exception instanceof JsonParseException
                || exception instanceof JSONException
                || exception instanceof ParseException) {
            //  解析错误
            onException(new ResponseException(exception, "Parse error"));
        } else {
            onException(new ResponseException(exception, "An unknown error"));
        }
    }


    @Override
    public void onException(ResponseException netException) {
    }

    public void onNetConnectErr(Throwable exception) {
        onException(new ResponseException(exception, "Unable to connect to server"));
    }
}

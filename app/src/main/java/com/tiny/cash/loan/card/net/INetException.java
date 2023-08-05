package com.tiny.cash.loan.card.net;

/**
 * 异常处理接口
 *
 * @author rainking
 */
public interface INetException {

    /**
     * 异常处理
     *
     * @param netException NetException
     */
    void onException(ResponseException netException);

}

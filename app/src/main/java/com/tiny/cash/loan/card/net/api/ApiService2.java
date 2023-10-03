package com.tiny.cash.loan.card.net.api;

import com.chocolate.moudle.scan.bean.UploadFileSignResponse;
import com.tiny.cash.loan.card.bean.TextInfoResponse;
import com.tiny.cash.loan.card.bean.bank.AccessCodeResponseBean;
import com.tiny.cash.loan.card.bean.bank.BankAccountResponseBean;
import com.tiny.cash.loan.card.bean.bank.BankResponseBean;
import com.tiny.cash.loan.card.bean.bank.CardResponseBean;
import com.tiny.cash.loan.card.bean.loan.PayStackResponseBean;
import com.tiny.cash.loan.card.bean.loan.RepayLoanResponseBean;
import com.tiny.cash.loan.card.bean.repay.MonifyResponseBean;
import com.tiny.cash.loan.card.bean.upload.InstallRequest;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.response.data.order.PayStackResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService2 {

    @POST("/v1/loan/get/reserved/account")
    Observable<Response<MonifyResponseBean>> queryMonify2(@Query("accountId") String accountId);

    @POST("v1/account/access/code")
    Observable<Response<AccessCodeResponseBean>> fetchAccessCode2(@Query("accountId") String accountId);

    @POST("v1/text/info")
    Observable<Response<TextInfoResponse>> QueryAppConfig2();

    @POST("v1/account/card/list")
    Observable<Response<CardResponseBean>> queryBankCardList2(@Query("accountId") String accountId);

    @POST("v1/account/bank/list")
    Observable<Response<BankResponseBean>> queryBankList2();

    @POST("v1/account/bankaccount/check")
    Observable<Response<BankAccountResponseBean>> checkBankaccount2(@Query("accountId") String accountId, @Query("bankCode") String bankCode, @Query("bankName") String bankName, @Query("bankAccountNumber") String bankAccountNumber, @Query("bvn") String bvn);

    @POST("v1/loan/repay")
    Observable<Response<RepayLoanResponseBean>> SubmitOrderRepay2(@Query("accountId") String accountId, @Query("orderId") String orderId, @Query("amount") String amount, @Query("cardNumber") String cardNumber);

    @POST("/v1/loan/repay/paystack")
    Observable<Response<PayStackResponseBean>> getPayStackUrl2(@Query("accountId") String accountId, @Query("orderId") String orderId);

    @POST("/v1/loan/repay/paystack/result")
    Observable<Response<PayStackResult>> payStackResult(@Query("accountId") String accountId, @Query("orderId") String orderId, @Query("reference") String reference);

    @POST("/v1/loan/get/reserved/account")
    Observable<Response<MonifyResponseBean>> queryMonify(@Query("accountId") String accountId);

    @POST("/v1/account/install")
    Observable<Response> recordInstall(@Body InstallRequest request);

    @GET("/v1/account/file/sign")
    Observable<Response<UploadFileSignResponse>> uploadFileGetSign
            (@Query("token") String token, @Query("imageType") String imageType);

}


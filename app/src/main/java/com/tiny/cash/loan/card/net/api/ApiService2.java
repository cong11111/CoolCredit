package com.tiny.cash.loan.card.net.api;

import com.tiny.cash.loan.card.bean.ServerLiveBean;
import com.tiny.cash.loan.card.bean.TextInfoResponse;
import com.tiny.cash.loan.card.bean.bank.AccessCodeResponseBean;
import com.tiny.cash.loan.card.bean.bank.BankAccountResponseBean;
import com.tiny.cash.loan.card.bean.bank.BankResponseBean;
import com.tiny.cash.loan.card.bean.bank.CardResponseBean;
import com.tiny.cash.loan.card.bean.bank.UploadCardResponseBean;
import com.tiny.cash.loan.card.bean.loan.DiscountAmountBean;
import com.tiny.cash.loan.card.bean.loan.DiscountRequest;
import com.tiny.cash.loan.card.bean.loan.PayStackResponseBean;
import com.tiny.cash.loan.card.bean.loan.ProductResponseBean;
import com.tiny.cash.loan.card.bean.loan.RepayLoanResponseBean;
import com.tiny.cash.loan.card.bean.loan.TrialResponseBean;
import com.tiny.cash.loan.card.bean.login2.RegLoginBean;
import com.tiny.cash.loan.card.bean.login2.UssdBean;
import com.tiny.cash.loan.card.bean.login2.VerifySmsCodeBean;
import com.tiny.cash.loan.card.bean.repay.FlutterwareResponse1Bean;
import com.tiny.cash.loan.card.bean.repay.FlutterwareResponse2Bean;
import com.tiny.cash.loan.card.bean.repay.FlutterwareResultBean;
import com.tiny.cash.loan.card.bean.repay.MonifyResponseBean;
import com.tiny.cash.loan.card.net.request.params.ApplyParams;
import com.tiny.cash.loan.card.net.request.params.AuthParams;
import com.tiny.cash.loan.card.net.request.params.BankCardParams;
import com.tiny.cash.loan.card.net.request.params.ContactInfoParams;
import com.tiny.cash.loan.card.net.request.params.FlutterWaveParams;
import com.tiny.cash.loan.card.net.request.params.OtherInfoParams;
import com.tiny.cash.loan.card.net.request.params.UserInfoParams;
import com.tiny.cash.loan.card.net.response.BaseResponse;
import com.tiny.cash.loan.card.net.response.HardwareResponseBean;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.response.data.bean.AppConfigFile;
import com.tiny.cash.loan.card.net.response.data.bean.AuthResult;
import com.tiny.cash.loan.card.net.response.data.bean.BankBoundResult;
import com.tiny.cash.loan.card.net.response.data.bean.BankDetail;
import com.tiny.cash.loan.card.net.response.data.bean.BankList;
import com.tiny.cash.loan.card.net.response.data.bean.BankNameList;
import com.tiny.cash.loan.card.net.response.data.bean.BankResult;
import com.tiny.cash.loan.card.net.response.data.bean.CheckCode;
import com.tiny.cash.loan.card.net.response.data.bean.CheckMobile;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.data.bean.DeviceCheck;
import com.tiny.cash.loan.card.net.response.data.bean.FcmTokenResult;
import com.tiny.cash.loan.card.net.response.data.bean.MessageResult;
import com.tiny.cash.loan.card.net.response.data.bean.MonifyResult;
import com.tiny.cash.loan.card.net.response.data.bean.RedoclyBean;
import com.tiny.cash.loan.card.net.response.data.bean.RegistrationBO;
import com.tiny.cash.loan.card.net.response.data.bean.StateAreaMap;
import com.tiny.cash.loan.card.net.response.data.bean.UpLoadAPPVersion;
import com.tiny.cash.loan.card.net.response.data.bean.UserAddressDetail;
import com.tiny.cash.loan.card.net.response.data.bean.UserContact;
import com.tiny.cash.loan.card.net.response.data.bean.UserContactDetail;
import com.tiny.cash.loan.card.net.response.data.bean.UserInfo;
import com.tiny.cash.loan.card.net.response.data.bean.UserProfile;
import com.tiny.cash.loan.card.net.response.data.bean.UserProfileDetail;
import com.tiny.cash.loan.card.net.response.data.order.FlutterWaveBean;
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;
import com.tiny.cash.loan.card.net.response.data.order.LoanTrial;
import com.tiny.cash.loan.card.net.response.data.order.MessageBean;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.response.data.order.PayStackBean;
import com.tiny.cash.loan.card.net.response.data.order.PayStackResult;
import com.tiny.cash.loan.card.net.response.data.order.ProductList;
import com.tiny.cash.loan.card.net.response.data.order.QueryOrderId;
import com.tiny.cash.loan.card.net.response.data.order.VerifyFlutterBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
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
}


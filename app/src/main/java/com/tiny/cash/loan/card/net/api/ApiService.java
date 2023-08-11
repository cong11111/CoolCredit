package com.tiny.cash.loan.card.net.api;

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

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /***
     * 账号登录
     * @return
     */
    @POST("v1/account/login")
    Observable<Response<UserInfo>> login(@Query("mobile") String mobile, @Query("password") String password, @Query("authCode") String authCode);

    /***
     * 验证手机号码是否注册
     * @return
     */
    @POST("v1/account/mobile/check")
    Observable<Response<CheckMobile>> hasRegister(@Query("mobile") String mobile);

    /***
     * 退出登录
     * @return
     */
    @POST("v1/account/logout")
    Observable<BaseResponse> logout(@Query("accountId") String accountId);

    /***
     * 发送验证码
     * @return
     */
    @POST("v1/account/captcha")
    Observable<Response> sendSmsCode(@Query("mobile") String mobile, @Query("captchaType") String captchaType);

    /***
     * 发送语音验证码
     * @return
     */
    @POST("v1/account/captcha/voice")
    Observable<Response> sendVoiceCode(@Query("mobile") String mobile, @Query("captchaType") String captchaType);


    /***
     * 验证码检查
     * @return
     */
    @POST("v1/account/captcha/check")
    Observable<Response<CheckCode>> checkSmsCode(@Query("mobile") String mobile, @Query("captchaCode") String captchaCode);

    /***
     * 验证码检查
     * @return
     */
    @POST("v1/account/modify/password")
    Observable<Response<RegistrationBO>> resetPassword(@Query("mobile") String mobile, @Query("newPassword") String newPassword);

    /***
     * 手机号快速注册
     * @return
     */
    @POST("v1/account/register")
    Observable<Response<RegistrationBO>> registration( @Query("password") String password,@Query("mobile") String mobile);

    /***
     * 谷歌FCM上报token
     * @return
     */
    @POST("v1/account/fcmtoken")
    Observable<Response<FcmTokenResult>> uploadFcmToken(@Query("fcmToken") String fcmToken);

    /***
     * 验证设备是否更换
     * @return
     */
    @POST("v1/account/device/check")
    Observable<Response<DeviceCheck>> deviceCheck(@Query("mobile") String mobile);

    /***
     * 发送更换设备验证码
     * @return
     */
    @POST("v1/account/device/captcha")
    Observable<Response<DeviceCheck>> deviceCaptcha(@Query("mobile") String mobile,@Query("captchaType") String captchaType);


    /***
     * 基本信息填写(包括修改)
     * @return
     */
    @POST("v1/account/profile")
    Observable<Response<UserProfile>> uploadUserInfo(@Body UserInfoParams userInfoParams);

    /***
     *填写联系人资料(包括修改)
     * @return
     */
    @POST("v1/account/contact")
    Observable<Response<UserContact>> upContactInfoBo(@Body ContactInfoParams refereeInfoParams);

    /***
     * 工作信息填写(包括修改)
     * @return
     */
    @POST("v1/account/other")
    Observable<Response<UserProfile>> UpAddressInfo(@Body OtherInfoParams otherInfoParams);

    /***
     *通用字典接口
     */
    @POST("v1/dict/detail")
    Observable<Response<Common>> queryCommon(@Query("dictType") String dictType);

    /***
     *通用字典州地区地址联动接口
     */
    @POST("v1/dict/detail")
    Observable<Response<StateAreaMap>> queryStateArea(@Query("dictType") String dictType);

    /***
     * 查询个人资料
     * @return
     */
    @POST("v1/account/profile/detail")
    Observable<Response<UserProfileDetail>> profileDetail(@Query("accountId") String accountId);

    /***
     * 查询联系人资料
     * @return
     */
    @POST("v1/account/contact/detail")
    Observable<Response<UserContactDetail>> contactDetail(@Query("accountId") String accountId);

    /***
     * 查询用户工作信息
     * @return
     */
    @POST("v1/account/other/detail")
    Observable<Response<UserAddressDetail>>otherDetail(@Query("accountId") String accountId);

    /***
     * 生成银行卡订单接口AccessCode
     * @return
     */
    @POST("v1/account/access/code")
    Observable<Response<BankResult>>fetchAccessCode(@Query("accountId") String accountId);


    /***
     * 验证银行卡交易是否成功
     * @return
     */
    @POST("/v1/account/card/upload")
    Observable<Response<BankResult>>verifyReference(@Body BankCardParams bankCardParams);


    /***
     * 获取银行列表
     * @return
     */
    @POST("v1/account/bank/list")
    Observable<Response<BankNameList>>queryBankList();


    /***
     * 绑定银行账号
     * @return
     */

    @POST("v1/account/bankaccount/check")
    Observable<Response<BankResult>>checkBankaccount(@Query("accountId") String accountId, @Query("bankCode") String bankCode, @Query("bankName") String bankName, @Query("bankAccountNumber") String bankAccountNumber,@Query("bvn") String bvn);

    /***
     * 银行账号和银行卡信息
     * @return
     */

    @POST("v1/account/bank/detail")
    Observable<Response<BankDetail>>queryBankDetail(@Query("accountId") String accountId);


    /***
     * app更新检查
     * @return
     */
    @POST("v1/start/detail")
    Observable<Response<UpLoadAPPVersion>>checkUpVersion();

    /***
     * 授权信息上传
     * @return
     */
    @POST("v1/account/auth/upload")
    Observable<Response<AuthResult>>upLoadAuthInfo(@Body AuthParams body);

    /***
     * 产品列表
     * @return
     */
    @POST("v1/loan/products")
    Observable<Response<ProductList>>productList(@Query("accountId") String accountId);//不传用户ID；为最高金额产品；预览页使用

    /***
     * 产品试算
     * @return
     */
    @POST("v1/loan/trial")
    Observable<Response<LoanTrial>>loanTrial(@Query("prodId") String prodId, @Query("loanAmount") String loanAmount);


    /***
     * 获取订单号
     * @return
     */
    @POST("v1/loan/check")
    Observable<Response<QueryOrderId>>QueryOrderId();

    /***
     * 还款
     * @return
     */
    @POST("v1/loan/repay")
    Observable<Response<OrderStatus>>SubmitOrderRepay(@Query("accountId") String accountId, @Query("orderId") String orderId, @Query("amount") String amount, @Query("cardNumber") String cardNumber);

    /***
     * 获取借款信息
     * @return
     */
    @POST("v1/loan/detail")
    Observable<Response<LoanOrderDetail>>QueryOrderDetail(@Query("accountId") String accountId);

    /***
     * 申请贷款
     * @return
     */
    @POST("v1/loan/apply")
    Observable<Response<OrderStatus>>SubmitOrderApply(@Body ApplyParams params);


    /***
     * 基本配置信息
     * @return
     */
    @POST("v1/text/info")
    Observable<Response<AppConfigFile>>QueryAppConfig();

    /***
     * 无验证码登录
     * @return
     */
    @POST("v1/account/ussd/check")
    Observable<Response<OrderStatus>>USSDLogin(@Query("mobile") String mobile);

    /***
     * 添加多张银行卡
     * @return
     */
    @POST("v1/account/card/verify")
    Observable<Response<BankResult>>addVerifyCard(@Body BankCardParams bankCardParams);

    /***
     * 获取银行卡列表
     * @return
     */
    @POST("v1/account/card/list")
    Observable<Response<BankList>>queryBankCardList(@Query("accountId") String accountId);



    /***
     * 是否已经绑定过银行卡
     * @return
     */
    @POST("/v1/account/card/exist")
    Observable<Response<BankResult>>existBankCard(@Query("account_id") String account_id,@Query("token") String token);

    /***
     * 没有绑卡的通知
     * @return
     */
    @POST("/v1/account/card/bindnotify")
    Observable<Response<BankResult>>bindCardNotify(@Query("account_id") String account_id,@Query("token") String token);

    /***
     * 获取Flutterwave参数
     *  chargeType // 1 绑卡 2 主动还款
     */
    @POST("/v1/loan/getTxRef")
    Observable<Response<FlutterWaveBean>>getFlutterwaveTxRef(@Query("account_id") String account_id, @Query("token") String token, @Query("orderId") String orderId,
                                                             @Query("chargeType") String chargeType);

    /***
     * 上传后端校验Flutterwave
     * @return
     */
    @POST("/v1/loan/uploadJson")
    Observable<Response<VerifyFlutterBean>>uploadJson(@Body FlutterWaveParams params);


    /***
     * 消息
     * @return
     */
    @POST("/v1/station/list")
    Observable<Response<MessageResult>>getListMessage(@Query("account_id") String account_id, @Query("page") String page, @Query("pageSize") String pageSize);

    /**
     * 查询flutter订单状态
     * @param accountId
     * @param orderId
     * @param txRef
     * @return
     */
    @POST("/v1/loan/getFlutterwaveResult")
    Observable<Response<OrderStatus>>getFlutterStatus(@Query("accountId") String accountId, @Query("orderId") String orderId,@Query("txRef") String txRef);


    /***
     * 获取 url
     * @return
     */
    @POST("/v1/loan/repay/paystack")
    Observable<Response<PayStackBean>>getPayStackUrl(@Query("accountId") String accountId, @Query("orderId") String orderId);



    /***
     * 查询paystck还款结果
     * @return
     */
    @POST("/v1/loan/repay/paystack/result")
    Observable<Response<PayStackResult>>payStackResult(@Query("accountId") String accountId, @Query("orderId") String orderId , @Query("reference") String reference);



    /***
     * 获取 未读短信个数
     * @return
     */
    @POST("/v1/station/unread")
    Observable<Response<MessageBean>>getMessageUnread(@Query("accountId") String accountId);


    /***
     * 验证是否绑卡
     * @return
     */
    @POST("/v1/account/card/bound")
    Observable<Response<BankBoundResult>>queryCardBound(@Query("accountId") String accountId, @Query("cardNumber") String cardNumber);



    /***
     * Monify虚拟账号
     * @return
     */
    @POST("/v1/loan/get/reserved/account")
    Observable<Response<MonifyResult>>queryMonify(@Query("accountId") String accountId);

    @POST("/v1/account/hardware")
    Observable<Response<HardwareResponseBean>>hareware(@Body JSONObject jsonObject);

    /***
     * redocly
     * @return
     */
    @POST("/v1/loan/get/redocly/repay/page")
    Observable<Response<RedoclyBean>>queryRedocly(@Query("accountId") String accountId, @Query("orderId") String orderId, @Query("amount") String amount);

}


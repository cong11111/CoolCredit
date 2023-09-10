package com.tiny.cash.loan.card.global

import android.text.TextUtils
import android.util.Log
import android.util.Pair
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.TextInfoResponse
import com.tiny.cash.loan.card.bean.bank.BankResponseBean
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.bean.bank.UploadCardResponseBean
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.bean.Common
import com.tiny.cash.loan.card.ui.card.BindNewCardActivity
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.FirebaseUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ConfigMgr {

    companion object {
        private val TAG = "ConfigMgr"

        val mDebtList = ArrayList<Pair<String, String>>()
        val mEducationList = ArrayList<Pair<String, String>>()
        val mSalaryList = ArrayList<Pair<String, String>>()
        val mMaritalList = ArrayList<Pair<String, String>>()
        val mRelationShipList = ArrayList<Pair<String, String>>()
        val mWorkList = ArrayList<Pair<String, String>>()
        val mAreaMap = HashMap<Pair<String, String>, ArrayList<Pair<String, String>>>()

        val mBankList = ArrayList<BankResponseBean.Bank>()

        fun getAllConfig() {
            mDebtList.clear()
            mDebtList.add(Pair("yes", "0"))
            mDebtList.add(Pair("no", "1"))

            if (mEducationList.size > 0) {
                Log.i(TAG, " has request education")
            } else {
                //学历等级，
                getItemConfig("education", object : CallBack {
                    override fun onGetData(list: ArrayList<Pair<String, String>>) {
                        mEducationList.clear()
                        mEducationList.addAll(list)
                    }
                })
            }

            if (mSalaryList.size > 0) {
                Log.i(TAG, " has request salary")
            } else {
                //工资区间,
                getItemConfig("salary", object : CallBack {
                    override fun onGetData(list: ArrayList<Pair<String, String>>) {
                        mSalaryList.clear()
                        mSalaryList.addAll(list)
                    }
                })
            }
            if (mMaritalList.size > 0) {
                Log.i(TAG, " has request marital")
            } else {
            //婚姻状况，
                getItemConfig("marital", object : CallBack {
                    override fun onGetData(list: ArrayList<Pair<String, String>>) {
                        mMaritalList.clear()
                        mMaritalList.addAll(list)
                    }
                })
            }
            if (mRelationShipList.size > 0) {
                Log.i(TAG, " has request relationship")
            } else {
                //关系，，
                getItemConfig("relationship", object : CallBack {
                    override fun onGetData(list: ArrayList<Pair<String, String>>) {
                        mRelationShipList.clear()
                        mRelationShipList.addAll(list)
                    }
                })
            }
            if (mWorkList.size > 0) {
                Log.i(TAG, " has request work")
            } else {
                getItemConfig("work", object : CallBack{
                    override fun onGetData(list : ArrayList<Pair<String, String>>) {
                        mWorkList.clear()
                        mWorkList.addAll(list)
                    }
                })
            }
            if (!mAreaMap.isEmpty()){
                Log.i(TAG, " has request area .")
            } else {
                getCityData()
            }

            if (Constant.textInfoResponse == null){
                getTextInfo(null)
            }
        }

        fun getCityData(){
            //            stateArea:州地区地址联动,    state:州， area:地区，
            getCityConfig("stateArea", object : CallBack2{
                override fun onGetData(map: HashMap<Pair<String, String>, ArrayList<Pair<String, String>>>) {
                    mAreaMap.clear()
                    mAreaMap.putAll(map)
                }
            })
        }

        private var dict1Observer: NetObserver<Response<Common>>? = null
        private fun getItemConfig(key : String, callBack : CallBack){
//            val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//            try {
//                jsonObject.put("dictType", key)
//                jsonObject.put("parentId", "")
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
            val observable: Observable<Response<Common>> =
                NetManager.getApiService().queryCommon(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            CommonUtils.disposable(dict1Observer)
            dict1Observer = object : NetObserver<Response<Common>>() {
                override fun onNext(response: Response<Common>) {
                    var responseBean : Common? = response.body
                    if (responseBean == null){
                        return
                    }
                    var list = parseItem(key, GsonUtils.toJson(responseBean))
                    if (list.size > 0) {
                        callBack.onGetData(list)
                    }
                }

                override fun onException(netException: ResponseException) {
                    Log.e(TAG, "get config failure = " + netException.msg)
                }
            }
            observable.subscribeWith(dict1Observer)
        }

        private var dict2Observer: NetObserver<Response<Common>>? = null
        private fun getCityConfig(key : String, callBack : CallBack2){
//            val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//            try {
//                jsonObject.put("dictType", key)
//                jsonObject.put("parentId", "")
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
            val observable: Observable<Response<Common>> =
                NetManager.getApiService().queryCommon(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            CommonUtils.disposable(dict2Observer)
            dict2Observer = object : NetObserver<Response<Common>>() {
                override fun onNext(response: Response<Common>) {
                    var responseBean : Common? = response.body
                    if (responseBean == null){
                        return
                    }
                    var map : HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> = parseCityItem(responseBean)
                    if (map.size > 0) {
                        callBack.onGetData(map)
                    }
                }

                override fun onException(netException: ResponseException) {
                    Log.e(TAG, "get config failure = " + netException.msg)
                }
            }
            observable.subscribeWith(dict2Observer)
        }

        private fun parseCityItem(obj: Any): HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> {
            val map: HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> = HashMap<Pair<String, String>, ArrayList<Pair<String, String>>>()
            try {
                val tempObj = JSONObject(GsonUtils.toJson(obj))
                val dictMap =  tempObj.optJSONObject("dictMap")
                //州
                val stateArray = dictMap.optJSONArray("state")
                val areaObject = dictMap.optJSONObject("area")

                for (i in 0 until stateArray.length()) {
                    val jsonObject: JSONObject = stateArray.optJSONObject(i)
                    val key = jsonObject.optString("key")
                    val value = jsonObject.optString("val")
                    var pair = Pair(value, key)
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        var tempList : ArrayList<Pair<String, String>> = ArrayList<Pair<String, String>>()

                        var tempArray:JSONArray =  areaObject.optJSONArray(key)
                        for (i in 0 until tempArray.length()) {
                            val itemJsonObject: JSONObject = tempArray.optJSONObject(i)
                            val itemKey = itemJsonObject.optString("key")
                            val itemValue = itemJsonObject.optString("val")
                            tempList.add(Pair(itemValue, itemKey))
                        }
                        map.put(pair, tempList)
//                        list.add(Pair(value, key))
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return map
        }

        private fun parseItem(key : String, obj: Any): ArrayList<Pair<String, String>> {
            val list: ArrayList<Pair<String, String>> = ArrayList<Pair<String, String>>()
            try {
                val tempObj = JSONObject(GsonUtils.toJson(obj))
                val dictMap =  tempObj.optJSONObject("dictMap")
                val jsonArray = dictMap.optJSONArray(key)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.optJSONObject(i)
                    val key = jsonObject.optString("key")
                    val value = jsonObject.optString("val")
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        list.add(Pair(value, key))
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Collections.sort(list, object : Comparator<Pair<String, String>> {
                override fun compare(t1: Pair<String, String>, t2: Pair<String, String>): Int {
                    val first = t1.second
                    val second = t2.second
                    var firstInt = 0
                    var secondInt = 0
                    try {
                        if (!TextUtils.isEmpty(first)) {
                            firstInt = first.toInt()
                        }
                        if (!TextUtils.isEmpty(second)) {
                            secondInt = second.toInt()
                        }
                    } catch (e: Exception) {
                    }
                    return firstInt - secondInt
                }
            })
            return list
        }


        private var dict3Observer: NetObserver<Response<TextInfoResponse>>? = null
        fun getTextInfo(callBack3: CallBack3?){
            if (Constant.textInfoResponse != null){
                callBack3?.onGetData(Constant.textInfoResponse)
                return
            }
            try {
                val observable: Observable<Response<TextInfoResponse>> =
                    NetManager.getApiService2().QueryAppConfig2()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                CommonUtils.disposable(dict3Observer)
                dict3Observer = object : NetObserver<Response<TextInfoResponse>>() {
                    override fun onNext(response: Response<TextInfoResponse>) {
                        val textInfo: TextInfoResponse? = response.body

                        if (textInfo != null) {
                            Constant.textInfoResponse = textInfo
                            callBack3?.onGetData(Constant.textInfoResponse)
                        }
                    }

                    override fun onException(netException: ResponseException) {
                        callBack3?.onGetData(null)
                    }
                }
                observable.subscribeWith(dict3Observer)

            } catch (e : Exception) {

            }
        }

        private var dict4Observer: NetObserver<Response<CardResponseBean>>? = null
        fun getBankList(callBack4: CallBack4) {
            if (!Constant.bankList.isEmpty()){
                callBack4?.onGetData(Constant.bankList)
                return
            }

            val observable: Observable<Response<CardResponseBean>> =
                NetManager.getApiService2().queryBankCardList2(Constant.mAccountId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            CommonUtils.disposable(dict4Observer)
            dict4Observer = object : NetObserver<Response<CardResponseBean>>() {
                override fun onNext(response: Response<CardResponseBean>) {
                    val bankBean: CardResponseBean? = response.body
                    if (bankBean == null || bankBean.cardlist == null) {
                        callBack4?.onGetData(Constant.bankList)
                        return
                    }
                    if (!bankBean.cardlist!!.isEmpty()){
                        Constant.bankList.clear()
                        Constant.bankList.addAll(bankBean.cardlist!!)
                    }
                    callBack4?.onGetData(Constant.bankList)
                }

                override fun onException(netException: ResponseException) {
                    callBack4?.onGetData(Constant.bankList)
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " get bank list error =  ." + netException.msg)
                    }
                }
            }
            observable.subscribeWith(dict4Observer)
        }
    }



     private interface CallBack {
        fun onGetData(list : ArrayList<Pair<String, String>>)
    }

    private interface CallBack2 {
        fun onGetData(map : HashMap<Pair<String, String>, ArrayList<Pair<String, String>>>)
    }

   interface CallBack3 {
        fun onGetData(textInfoResponse: TextInfoResponse?)
    }

    interface CallBack4 {
        fun onGetData(bankList : ArrayList<CardResponseBean.Bank>)
    }
}
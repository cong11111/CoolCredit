package com.tiny.cash.loan.card

import com.tiny.cash.loan.card.kudicredit.BuildConfig

object Constants {
    const val ZERO = "0"
    const val ONE = "1"
    const val TWO = "2"
    const val THREE = "3"
    const val FOUR = "4"
    const val FIVE = "5"
    const val SIX = "6"
    const val BASE_URL = BuildConfig.HOST
    const val FIRST_PHONE = "234"
    const val ICREDIT_AGREE_URL = "https://www.kudicredit.com/all.html"

    /**
     * 字典类型
     * education:学历等级，
     * salary:工资区间,
     * marital:婚姻状况，
     * relationship:关系，
     * work:工作情况,
     * stateArea:州地区地址联动,
     * state:州，
     * area:地区，
     */
    const val EDUCATION = "education"
    const val SALARY = "salary"
    const val MARITAL = "marital"
    const val RELATIONSHIP = "relationship"
    const val WORK = "work"
    const val STATEAREA = "stateArea"
    const val STATE = "state"
    const val AREA = "area"
    const val KEY_CHANNEL = "source_channel"
}
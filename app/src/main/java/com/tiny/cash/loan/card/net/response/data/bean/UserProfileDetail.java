package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class UserProfileDetail implements Serializable {

    /**
     * 电话号码
     * 身份证号
     * 身份证识别是否通过 0未通过 1通过
     * 生日：1990-01-24
     * 性别代码 0未知 1男 2女
     * 性别描述 Male  or Female
     * 用户姓名
     * 名字
     * 中间名
     * 姓
     * 邮箱地址
     * 身份证识别是否通过
     * 资料填写结果
     * 是否可以修改
     * 更新时间
     */
    /**
     * birthday : 2008-08-08
     * edit : false
     * email : xxxx@gmail.com
     * firstName : Tgig
     * fullName : TGIG MARY MARIA
     * gender : 1
     * genderLabel : Male
     * lastName : maria
     * middleName : mary
     * mobile : 234888888888
     * nationalChecked : true
     * nationalId : 3453454564
     * result : 49152
     * utime : 2020-11-13 10:00:10
     */

    private String birthday;
    private boolean edit;
    private String email;
    private String firstName;
    private String fullName;
    private int gender;
    private String genderLabel;
    private String lastName;
    private String middleName;
    private String mobile;
    private boolean bvnChecked;
    private String bvn;
    private int result;
    private String utime;

    private String homeArea;
    private String homeAreaLabel;
    private String homeState;
    private String homeStateLabel;
    private String homeAddress;





    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGenderLabel() {
        return genderLabel;
    }

    public void setGenderLabel(String genderLabel) {
        this.genderLabel = genderLabel;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getHomeArea() {
        return homeArea;
    }

    public void setHomeArea(String homeArea) {
        this.homeArea = homeArea;
    }

    public String getHomeAreaLabel() {
        return homeAreaLabel;
    }

    public void setHomeAreaLabel(String homeAreaLabel) {
        this.homeAreaLabel = homeAreaLabel;
    }

    public String getHomeStateLabel() {
        return homeStateLabel;
    }

    public void setHomeStateLabel(String homeStateLabel) {
        this.homeStateLabel = homeStateLabel;
    }

    public String getHomeState() {
        return homeState;
    }

    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }


    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

}

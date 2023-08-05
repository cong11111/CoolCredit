package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class UserAddressDetail implements Serializable {

    /**
     * 是否可编辑
     * 个人婚姻状况
     * 个人婚姻状况描述
     * 个人工作情况
     * 个人工作情况描述
     * 薪水区间
     * 薪水区间描述
     * 是否有外债
     * 公司名称
     * 公司地址
     */
    /**
     * companyAddress : Ola State magens area No.234
     * companyName : SKY star Ltd.;
     * edit : true
     * hasOutstandingLoan : 1
     * marital : 1
     * maritalLabel : Single
     * result : 61440
     * salary : 1
     * salaryLabel : 0-1000
     * utime : 2020-11-23 12:26:57
     * work : 1
     * workLabel : Employed
     */

    private String companyAddress;
    private String companyName;
    private boolean edit;
    private int hasOutstandingLoan;
    private String marital;
    private String maritalLabel;
    private int result;
    private String salary;
    private String salaryLabel;
    private String utime;
    private String work;
    private String workLabel;
    private String education;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public int getHasOutstandingLoan() {
        return hasOutstandingLoan;
    }

    public void setHasOutstandingLoan(int hasOutstandingLoan) {
        this.hasOutstandingLoan = hasOutstandingLoan;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getMaritalLabel() {
        return maritalLabel;
    }

    public void setMaritalLabel(String maritalLabel) {
        this.maritalLabel = maritalLabel;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSalaryLabel() {
        return salaryLabel;
    }

    public void setSalaryLabel(String salaryLabel) {
        this.salaryLabel = salaryLabel;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getWorkLabel() {
        return workLabel;
    }

    public void setWorkLabel(String workLabel) {
        this.workLabel = workLabel;
    }
}

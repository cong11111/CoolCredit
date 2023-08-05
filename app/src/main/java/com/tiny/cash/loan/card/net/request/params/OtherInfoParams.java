package com.tiny.cash.loan.card.net.request.params;

import java.io.Serializable;

/**
 * 用户唯一标识
 * 个人婚姻状况
 * 个人工作情况
 * 薪水区间
 * 是否有外债 0无，1有
 * 工作单位名称
 * 工作地址-手填详细地址
 */
public class OtherInfoParams implements Serializable {
    private String accounId;
    private String marital;
    private String work;
    private String salary;
    private String hasOutstandingLoan;
    private String companyName;
    private String companyAddress;
    private String education;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAccounId() {
        return accounId;
    }

    public void setAccounId(String accounId) {
        this.accounId = accounId;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getHasOutstandingLoan() {
        return hasOutstandingLoan;
    }

    public void setHasOutstandingLoan(String hasOutstandingLoan) {
        this.hasOutstandingLoan = hasOutstandingLoan;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
}

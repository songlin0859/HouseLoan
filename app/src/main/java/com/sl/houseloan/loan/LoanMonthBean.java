package com.sl.houseloan.loan;

import java.math.BigDecimal;

public class LoanMonthBean {

    private int month; // 第几个月份
    private String date;
    private long dateMills;
    private BigDecimal repayment; // 该月还款额
    private BigDecimal payPrincipal; // 该月所还本金
    private BigDecimal interest; // 该月利息
    private BigDecimal remainTotal; // 剩余贷款
    private BigDecimal remainPrincipal; // 剩余总本金

    private int year; // 第几年
    private int monthInYear; // 年里的第几月

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getRepayment() {
        return repayment;
    }

    public void setRepayment(BigDecimal repayment) {
        this.repayment = repayment;
    }

    public BigDecimal getPayPrincipal() {
        return payPrincipal;
    }

    public void setPayPrincipal(BigDecimal payPrincipal) {
        this.payPrincipal = payPrincipal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getRemainTotal() {
        return remainTotal;
    }

    public void setRemainTotal(BigDecimal remainTotal) {
        this.remainTotal = remainTotal;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthInYear() {
        return monthInYear;
    }

    public void setMonthInYear(int monthInYear) {
        this.monthInYear = monthInYear;
    }

    @Override
    public String toString() {
        return (date==null?"":"日期:" + date )+ " 总第" + String.format("%3d", month) + "月" + " 该月还款额=" + repayment
                + "\n所还本金=" + payPrincipal + ", 所还利息=" + interest
                + "\n剩余贷款=" + remainTotal + ", 剩余总本金=" + remainPrincipal;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDateMills() {
        return dateMills;
    }

    public void setDateMills(long dateMills) {
        this.dateMills = dateMills;
    }
}

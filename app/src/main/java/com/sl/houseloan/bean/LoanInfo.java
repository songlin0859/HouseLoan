package com.sl.houseloan.bean;

/**
 * Created by 36128 on 2017/12/9.
 */

public class LoanInfo {
    public static final int TYPE_DEBJ = 0;
    public static final int TYPE_DEBX = 1;
    private double totalMoney;
    private int totalLength;
    private double rate;
    private double rateDiscount;
    private int loanType=TYPE_DEBJ;//0：等额本金，1:等额本息
    private long firstPayTime;//首次还款日期时间戳

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getRateDiscount() {
        return rateDiscount;
    }

    public void setRateDiscount(double rateDiscount) {
        this.rateDiscount = rateDiscount;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

    public long getFirstPayTime() {
        return firstPayTime;
    }

    public void setFirstPayTime(long firstPayTime) {
        this.firstPayTime = firstPayTime;
    }
}

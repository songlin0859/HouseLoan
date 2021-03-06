package com.sl.loanlibrary;

import java.math.BigDecimal;
import java.util.List;

/**
 */
public class LoanResult {

    private BigDecimal totalLoanMoney; //贷款总额
    private int totalMonth; //还款月份
    private double loanRate; //贷款年利率

    private BigDecimal totalInterest; // 总利息数
    private BigDecimal totalRepayment; // 还款总额
    private BigDecimal firstRepayment; // 首月还款额
    private BigDecimal avgRepayment; // 月均还款额
    private BigDecimal monthDec;//每月减少

    private List<LoanMonthBean> allLoans; // 所有月份的还款情况

    public BigDecimal getTotalLoanMoney() {
        return totalLoanMoney;
    }

    public void setTotalLoanMoney(BigDecimal totalLoanMoney) {
        this.totalLoanMoney = totalLoanMoney;
    }

    public int getTotalMonth() {
        return totalMonth;
    }

    public void setTotalMonth(int totalMonth) {
        this.totalMonth = totalMonth;
    }

    public double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(double loanRate) {
        this.loanRate = loanRate;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public BigDecimal getFirstRepayment() {
        return firstRepayment;
    }

    public void setFirstRepayment(BigDecimal firstRepayment) {
        this.firstRepayment = firstRepayment;
    }

    public BigDecimal getAvgRepayment() {
        return avgRepayment;
    }

    public void setAvgRepayment(BigDecimal avgRepayment) {
        this.avgRepayment = avgRepayment;
    }

    public List<LoanMonthBean> getAllLoans() {
        return allLoans;
    }

    public void setAllLoans(List<LoanMonthBean> allLoans) {
        this.allLoans = allLoans;
    }

	@Override
	public String toString() {
		return "LoanResult [贷款总额=" + totalLoanMoney + ", 还款月份="
				+ String.format("%3d", totalMonth) + ", 贷款年利率=" + loanRate + ", 总利息数="
				+ totalInterest + ", 还款总额=" + totalRepayment
				+ ", 首月还款额=" + firstRepayment + ", 月均还款额="
				+ avgRepayment + ", 所有月份的还款情况=" + allLoans + "]";
	}


    public BigDecimal getMonthDec() {
        return monthDec;
    }

    public void setMonthDec(BigDecimal monthDec) {
        this.monthDec = monthDec;
    }
}

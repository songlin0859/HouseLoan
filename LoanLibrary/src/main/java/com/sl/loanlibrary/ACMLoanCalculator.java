package com.sl.loanlibrary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 等额本金还款法
 */
public class ACMLoanCalculator implements ILoanCalculator {
	/**
	 * @param totalLoanMoney 总金额
	 * @param totalMonth 总月数
	 * @param loanRate 贷款利率
	 * @param rateType 年利率/月利率
	 */
    @Override
    public LoanResult calLoan(BigDecimal totalLoanMoney, int totalMonth, double loanRate, RateType rateType) {
        LoanResult loanResult = new LoanResult();
        /**
         * 月利率=年利率/12
         * 日利率=年利率/360=月利率/30
         */
        //月利率
        BigDecimal loanRateMonth = rateType == RateType.RATE_TYPE_YEAR ? new BigDecimal(loanRate / 100 / 12) : new BigDecimal(loanRate / 100);
        //总月数
        loanResult.setTotalMonth(totalMonth);
        //总贷款额
        loanResult.setTotalLoanMoney(totalLoanMoney);
        //平均每个月还款本金
        BigDecimal payPrincipal = totalLoanMoney.divide(new BigDecimal(totalMonth), 2, BigDecimal.ROUND_HALF_UP);
        //每月减少
        BigDecimal dec = payPrincipal.multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
        //System.out.println("每月减少："+dec);
        loanResult.setMonthDec(dec);
        
        BigDecimal totalPayedPrincipal = new BigDecimal(0);//累积所还本金
        BigDecimal totalInterest = new BigDecimal(0); //总利息
        BigDecimal totalRepayment = new BigDecimal(0); // 已还款总数
        List<LoanMonthBean> loanMonthBeanList = new ArrayList<LoanMonthBean>();

        int year = 0;
        int monthInYear = 0;
        for (int month = 0; month < totalMonth; month++) {
            LoanMonthBean loanMonthBean = new LoanMonthBean();
            loanMonthBean.setMonth(month + 1);
            loanMonthBean.setYear(year + 1);
            loanMonthBean.setMonthInYear(++monthInYear);
            if ((month + 1) % 12 == 0) {
                year++;
                monthInYear = 0;
            }
            //计算当月利息： 利息=剩余本金*月利率 （保留两位有效数字）
            BigDecimal interest = totalLoanMoney.subtract(totalPayedPrincipal).multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
            loanMonthBean.setInterest(interest);
            totalInterest = totalInterest.add(interest);
            totalPayedPrincipal = totalPayedPrincipal.add(payPrincipal);
            loanMonthBean.setPayPrincipal(payPrincipal);
            loanMonthBean.setRepayment(payPrincipal.add(interest));
            if (month == 0) {
                loanResult.setFirstRepayment(loanMonthBean.getRepayment());
            }
            totalRepayment = totalRepayment.add(loanMonthBean.getRepayment());
            loanMonthBean.setRemainPrincipal(totalLoanMoney.subtract(totalPayedPrincipal));
            loanMonthBeanList.add(loanMonthBean);
        }
        loanResult.setTotalRepayment(totalRepayment);
        loanResult.setAvgRepayment(totalRepayment.divide(new BigDecimal(totalMonth), 2, BigDecimal.ROUND_HALF_UP));
        loanResult.setTotalInterest(totalInterest);
        BigDecimal totalPayedRepayment = new BigDecimal(0);
        for (LoanMonthBean loanMonthBean : loanMonthBeanList) {
            totalPayedRepayment = totalPayedRepayment.add(loanMonthBean.getRepayment());
            loanMonthBean.setRemainTotal(totalRepayment.subtract(totalPayedRepayment));
        }
        loanResult.setAllLoans(loanMonthBeanList);
        return loanResult;
    }

}

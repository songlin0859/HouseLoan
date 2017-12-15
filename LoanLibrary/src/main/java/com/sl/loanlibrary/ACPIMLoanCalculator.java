package com.sl.loanlibrary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 等额本息还款法
 * 每月还款数额=[贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1];
 */
public class ACPIMLoanCalculator implements ILoanCalculator {
	/**
	 * @param totalLoanMoney 总金额
	 * @param totalMonth 总月数
	 * @param loanRate 贷款利率
	 * @param rateType 年利率/月利率
	 */
    @Override
    public LoanResult calLoan(BigDecimal totalLoanMoney, int totalMonth, double loanRate, RateType rateType) {
        LoanResult loanResult = new LoanResult();
        BigDecimal loanRateMonth = rateType == RateType.RATE_TYPE_YEAR ? new BigDecimal(loanRate / 100 / 12) : new BigDecimal(loanRate / 100);
        BigDecimal factor = new BigDecimal(Math.pow(1 + loanRateMonth.doubleValue(), totalMonth));
        //每月还款数额=[贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1];
        BigDecimal avgRepayment = totalLoanMoney.multiply(loanRateMonth).multiply(factor).divide(factor.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
        loanResult.setLoanRate(loanRate);
        loanResult.setTotalLoanMoney(totalLoanMoney);
        loanResult.setTotalMonth(totalMonth);
        loanResult.setAvgRepayment(avgRepayment);
        loanResult.setTotalRepayment(avgRepayment.multiply(new BigDecimal(totalMonth)));
        loanResult.setFirstRepayment(avgRepayment);

        BigDecimal totalPayedPrincipal = new BigDecimal(0);//累积所还本金
        BigDecimal totalInterest = new BigDecimal(0); //总利息
        BigDecimal totalRepayment = new BigDecimal(0); // 已还款总数
        List<LoanMonthBean> loanMonthBeanList = new ArrayList<LoanMonthBean>();
        int year = 0;
        int monthInYear = 0;
        for (int i = 0; i < totalMonth; i++) {
            LoanMonthBean loanMonthBean = new LoanMonthBean();
            BigDecimal remainPrincipal = totalLoanMoney.subtract(totalPayedPrincipal);
            BigDecimal interest = remainPrincipal.multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
            totalInterest = totalInterest.add(interest);
            BigDecimal principal = loanResult.getAvgRepayment().subtract(interest);
            totalPayedPrincipal = totalPayedPrincipal.add(principal);
            loanMonthBean.setMonth(i + 1);
            loanMonthBean.setYear(year + 1);
            loanMonthBean.setMonthInYear(++monthInYear);
            if ((i + 1) % 12 == 0) {
                year++;
                monthInYear = 0;
            }
            loanMonthBean.setInterest(interest);
            loanMonthBean.setPayPrincipal(principal);
            loanMonthBean.setRepayment(loanResult.getAvgRepayment());
            totalRepayment = totalRepayment.add(loanMonthBean.getRepayment());
            loanMonthBean.setRemainPrincipal(remainPrincipal);
            loanMonthBean.setRemainTotal(loanResult.getTotalRepayment().subtract(totalRepayment));
            loanMonthBeanList.add(loanMonthBean);
        }
        loanResult.setTotalInterest(totalInterest);
        loanResult.setAllLoans(loanMonthBeanList);
        return loanResult;
    }

}

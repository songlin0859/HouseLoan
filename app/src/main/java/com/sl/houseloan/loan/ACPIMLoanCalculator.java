package com.sl.houseloan.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 等额本息还款法
 * 每月还款数额=[贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1];
 */
public class ACPIMLoanCalculator extends LoanCalculatorAdapter {
	/**
	 * @param totalLoanMoney 总金额
	 * @param totalMonth 总月数
	 * @param loanRate 贷款利率
	 * @param rateType 年利率/月利率
	 */
    @Override
    public Loan calLoan(BigDecimal totalLoanMoney, int totalMonth, double loanRate, int rateType) {
        Loan loan = new Loan();
        BigDecimal loanRateMonth = rateType == LoanUtil.RATE_TYPE_YEAR ? new BigDecimal(loanRate / 100 / 12) : new BigDecimal(loanRate / 100);
        BigDecimal factor = new BigDecimal(Math.pow(1 + loanRateMonth.doubleValue(), totalMonth));
        //每月还款数额=[贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1];
        BigDecimal avgRepayment = totalLoanMoney.multiply(loanRateMonth).multiply(factor).divide(factor.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
        loan.setLoanRate(loanRate);
        loan.setTotalLoanMoney(totalLoanMoney);
        loan.setTotalMonth(totalMonth);
        loan.setAvgRepayment(avgRepayment);
        loan.setTotalRepayment(avgRepayment.multiply(new BigDecimal(totalMonth)));
        loan.setFirstRepayment(avgRepayment);

        BigDecimal totalPayedPrincipal = new BigDecimal(0);//累积所还本金
        BigDecimal totalInterest = new BigDecimal(0); //总利息
        BigDecimal totalRepayment = new BigDecimal(0); // 已还款总数
        List<LoanByMonth> loanByMonthList = new ArrayList<LoanByMonth>();
        int year = 0;
        int monthInYear = 0;
        for (int i = 0; i < totalMonth; i++) {
            LoanByMonth loanByMonth = new LoanByMonth();
            BigDecimal remainPrincipal = totalLoanMoney.subtract(totalPayedPrincipal);
            BigDecimal interest = remainPrincipal.multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
            totalInterest = totalInterest.add(interest);
            BigDecimal principal = loan.getAvgRepayment().subtract(interest);
            totalPayedPrincipal = totalPayedPrincipal.add(principal);
            loanByMonth.setMonth(i + 1);
            loanByMonth.setYear(year + 1);
            loanByMonth.setMonthInYear(++monthInYear);
            if ((i + 1) % 12 == 0) {
                year++;
                monthInYear = 0;
            }
            loanByMonth.setInterest(interest);
            loanByMonth.setPayPrincipal(principal);
            loanByMonth.setRepayment(loan.getAvgRepayment());
            totalRepayment = totalRepayment.add(loanByMonth.getRepayment());
            loanByMonth.setRemainPrincipal(remainPrincipal);
            loanByMonth.setRemainTotal(loan.getTotalRepayment().subtract(totalRepayment));
            loanByMonthList.add(loanByMonth);
        }
        loan.setTotalInterest(totalInterest);
        loan.setAllLoans(loanByMonthList);
        return loan;
    }

}

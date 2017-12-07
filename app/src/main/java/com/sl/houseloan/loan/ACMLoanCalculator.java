package com.sl.houseloan.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 等额本金还款法
 */
public class ACMLoanCalculator extends LoanCalculatorAdapter {
	/**
	 * @param totalLoanMoney 总金额
	 * @param totalMonth 总月数
	 * @param loanRate 贷款利率
	 * @param rateType 年利率/月利率
	 */
    @Override
    public Loan calLoan(BigDecimal totalLoanMoney, int totalMonth, double loanRate, int rateType) {
        Loan loan = new Loan();
        /**
         * 月利率=年利率/12
         * 日利率=年利率/360=月利率/30
         */
        //月利率
        BigDecimal loanRateMonth = rateType == LoanUtil.RATE_TYPE_YEAR ? new BigDecimal(loanRate / 100 / 12) : new BigDecimal(loanRate / 100);
        //总月数
        loan.setTotalMonth(totalMonth);
        //总贷款额
        loan.setTotalLoanMoney(totalLoanMoney);
        //平均每个月还款本金
        BigDecimal payPrincipal = totalLoanMoney.divide(new BigDecimal(totalMonth), 2, BigDecimal.ROUND_HALF_UP);
        //每月减少
        BigDecimal dec = payPrincipal.multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("每月减少："+dec);
        
        BigDecimal totalPayedPrincipal = new BigDecimal(0);//累积所还本金
        BigDecimal totalInterest = new BigDecimal(0); //总利息
        BigDecimal totalRepayment = new BigDecimal(0); // 已还款总数
        List<LoanByMonth> loanByMonthList = new ArrayList<LoanByMonth>();

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,2016);
        calendar.set(Calendar.MONTH,8);//一月是0，所以九月是8
        calendar.set(Calendar.DAY_OF_MONTH,12);

        int year = 0;
        int monthInYear = 0;
        for (int month = 0; month < totalMonth; month++) {
            LoanByMonth loanByMonth = new LoanByMonth();
            loanByMonth.setDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)/*0~11  +1-->1~12*/+"-"+calendar.get(Calendar.DAY_OF_MONTH));
            loanByMonth.setDateMills(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH,1);
            loanByMonth.setMonth(month + 1);
            loanByMonth.setYear(year + 1);
            loanByMonth.setMonthInYear(++monthInYear);
            if ((month + 1) % 12 == 0) {
                year++;
                monthInYear = 0;
            }
            //计算当月利息： 利息=剩余本金*月利率 （保留两位有效数字）
            BigDecimal interest = totalLoanMoney.subtract(totalPayedPrincipal).multiply(loanRateMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
            loanByMonth.setInterest(interest);
            totalInterest = totalInterest.add(interest);
            totalPayedPrincipal = totalPayedPrincipal.add(payPrincipal);
            loanByMonth.setPayPrincipal(payPrincipal);
            loanByMonth.setRepayment(payPrincipal.add(interest));
            if (month == 0) {
                loan.setFirstRepayment(loanByMonth.getRepayment());
            }
            totalRepayment = totalRepayment.add(loanByMonth.getRepayment());
            loanByMonth.setRemainPrincipal(totalLoanMoney.subtract(totalPayedPrincipal));
            loanByMonthList.add(loanByMonth);
        }
        loan.setTotalRepayment(totalRepayment);
        loan.setAvgRepayment(totalRepayment.divide(new BigDecimal(totalMonth), 2, BigDecimal.ROUND_HALF_UP));
        loan.setTotalInterest(totalInterest);
        BigDecimal totalPayedRepayment = new BigDecimal(0);
        for (LoanByMonth loanByMonth : loanByMonthList) {
            totalPayedRepayment = totalPayedRepayment.add(loanByMonth.getRepayment());
            loanByMonth.setRemainTotal(totalRepayment.subtract(totalPayedRepayment));
        }
        loan.setAllLoans(loanByMonthList);
        return loan;
    }

}

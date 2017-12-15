package com.sl.loanlibrary;

import java.math.BigDecimal;

public class LoanCalculatorUtil {
	/**
	 * 等额本息计算
	 * @param totalLoanMoney 总贷款金额
	 * @param totalMonth 总贷款月数
	 * @param loanRate 贷款利率
	 * @param rateType 利率类型(年利率/月利率)
	 * @return LoanResult
	 */
	public static LoanResult calculatorACPI(BigDecimal totalLoanMoney, int totalMonth, double loanRate, RateType rateType){
		return new ACPIMLoanCalculator().calLoan(totalLoanMoney, totalMonth, loanRate, rateType);
	}
	/**
	 * 等额本金计算
	 * @param totalLoanMoney 总贷款金额
	 * @param totalMonth 总贷款月数
	 * @param loanRate 贷款利率
	 * @param rateType 利率类型(年利率/月利率)
	 * @return LoanResult
	 */
	public static LoanResult calculatorAC(BigDecimal totalLoanMoney, int totalMonth, double loanRate, RateType rateType){
		return new ACMLoanCalculator().calLoan(totalLoanMoney, totalMonth, loanRate, rateType);
	}
}

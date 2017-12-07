package com.sl.houseloan.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class LoanCalculatorAdapter implements ILoanCalculator {

	public abstract Loan calLoan(BigDecimal totalLoanMoney, int totalMonth, double loanRate, int rateType);
}

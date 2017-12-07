package com.sl.houseloan.loan;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * Created by WangGenshen on 1/14/16.
 */
public class LoanCalculatorTest{

    private int totalMonth;
    private BigDecimal totalMoney;
    private double percent;
    private double rate;
    private double rateDiscount;

    public LoanCalculatorTest(){
    	totalMonth = 20*12;
        totalMoney = new BigDecimal(390000);
        percent = 0;
        rate = 4.9;
        rateDiscount = 0.9;
    }
    
    public static void main(String[] args) {
		new LoanCalculatorTest().testACPIMCalculate();
		new LoanCalculatorTest().testACMCalculate();
	}

    public void testACPIMCalculate() {
        Loan loan = LoanCalculatorUtil.calculatorACPI(totalMoney, totalMonth, rate*0.9, LoanUtil.RATE_TYPE_YEAR);
        try {
			File file = new File("D://等额本息.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(loan.toString().getBytes());
		} catch (Exception e) {
		}
    }

    public void testACMCalculate() {
        Loan loan = LoanCalculatorUtil.calculatorAC(totalMoney, totalMonth, rate*0.9, LoanUtil.RATE_TYPE_YEAR);
        try {
			File file = new File("D://等额本金.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(loan.toString().getBytes());
		} catch (Exception e) {
		}
    }

}

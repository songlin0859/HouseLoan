package com.sl.houseloan.loan;

import java.math.BigDecimal;

/**
 * Created by WangGenshen on 1/23/16.
 */
public class LoanUtil {

    public static final int RATE_TYPE_YEAR = 10;
    public static final int RATE_TYPE_MONTH = 11;

    /*public static BigDecimal totalMoney(double area, BigDecimal price, double discount) {
        return price.multiply(new BigDecimal(area)).multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }*/
    /**
     * @param totalMoney
     * @param percent 什么鬼？默认是0
     * @return
     */
    public static BigDecimal totalLoanMoney(BigDecimal totalMoney, double percent) {
        return totalMoney.multiply(new BigDecimal(1 - percent)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
   /* public static BigDecimal totalLoanMoney(double area, BigDecimal price, double discount, double percent) {
        return totalLoanMoney(totalMoney(area, price, discount), percent);
    }*/
    /**
     * 计算最终利率
     * @param rate 原利率
     * @param discount 折扣
     * @return
     */
    public static double rate(double rate, double discount) {
        return rate * discount;
    }
    /**
     * 计算总月数
     * @param year 年数
     * @return
     */
    public static int totalMonth(int year) {
        return 12 * year;
    }

}

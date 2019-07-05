package com.example;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分期详情信息
 *
 * @author jinrun.xie
 * @date 2019/7/3
 **/
public class LoanSeparateDetail {
    /**
     * 分期，表示第多少期，比如第1期，第2期
     */
    private Integer period;
    /**
     * 本金，表示本期应该还的本金金额，保存小数点后2位
     */
    private BigDecimal principal;
    /**
     * 利息，表示本期应该还的利息，保存小数点后2位
     */
    private BigDecimal interest;

    /**
     * 本期应还金额,包括应还本金和利息的总和
     */
    private BigDecimal totalRepay;
    /**
     * 还款日期，表示本期最后的还款日
     */
    private Date repayDay;

    /**
     * 还完本期之后，还剩余应还的本金
     */
    private BigDecimal remainRepayPrincipal;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getTotalRepay() {
        return totalRepay;
    }

    public void setTotalRepay(BigDecimal totalRepay) {
        this.totalRepay = totalRepay;
    }

    public Date getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
    }

    public BigDecimal getRemainRepayPrincipal() {
        return remainRepayPrincipal;
    }

    public void setRemainRepayPrincipal(BigDecimal remainRepayPrincipal) {
        this.remainRepayPrincipal = remainRepayPrincipal;
    }
}

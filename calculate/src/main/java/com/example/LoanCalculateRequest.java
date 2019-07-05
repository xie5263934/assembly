package com.example;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 贷款分期计算请求对象
 *
 * @author jinrun.xie
 * @date 2019/7/3
 **/
public class LoanCalculateRequest {
    /**
     * 贷款总金额，单位元，在创建的时候，就保留两位小数，
     * 在计算之前，计算工厂会再次强制设置精度，
     * 如果因为精度不正确，造成最后计算分期的数额不正确，请提前检查开始计算之前贷款总额度和精度是否正确
     */
    private BigDecimal totalPrincipal;
    /**
     * 贷款年化利率，保留6位小数，在计算之前，计算工厂会再次强制设置精度,这里的利率是百分号前面的数字，不包含百分号，例如利率12.7654%，但是这里的值是12.7654
     */
    private BigDecimal rate;

    /**
     * 贷款日期
     */
    private Date startLoanDay;

    /**
     * 计息类型，按日或者是按月计息，如果不填写，默认为按照月
     * 1:按月，2:按日，3:季度，4:按年
     */
    private Integer rateType;

    /**
     * 贷款类型计息类型
     * 1:等额本息
     * 2:等本等息
     * 3:随借随还
     * 4:先息后本
     * 5:等额本金
     */
    private Integer loanType;

    /**
     * 贷款分期期数，比如按月分期，或者是按日计息的贷款天数等
     */
    private Integer period;

    /**
     * 免息日，可以在还款出账之后，再减免多少天不计息的日期，最后才是真正的最后还款日期，
     * 一般用在信用卡上，例如招行每个月5号出账，但是18天免息日，最后还款日是23号，都不算逾期
     */
    private Integer freeDay;

    /**
     * 尾差处理方式
     * 1:第一期补尾差
     * 2:最后一期补尾差
     * 3:每期补尾差(每期补尾差算法，最后会导致所有分期本金的总和大于本金)
     * 如果不传递该值，默认为1，补第一期尾差
     */
    private Integer tailWay;
    /**
     * 手续费收取方式,如果为空，默认位置为1，第一期收取
     * 1：放到第一期收取
     * 2：放到最后一期收取
     */
    private Integer serviceFeeType;

    /**
     * 手续费,如果手续费小于0，默认设置为0
     */
    private BigDecimal serviceFee;


    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getStartLoanDay() {
        return startLoanDay;
    }

    public void setStartLoanDay(Date startLoanDay) {
        this.startLoanDay = startLoanDay;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public Integer getFreeDay() {
        return freeDay;
    }

    public void setFreeDay(Integer freeDay) {
        this.freeDay = freeDay;
    }

    public Integer getLoanType() {
        return loanType;
    }

    public void setLoanType(Integer loanType) {
        this.loanType = loanType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getTailWay() {
        return tailWay;
    }

    public void setTailWay(Integer tailWay) {
        this.tailWay = tailWay;
    }

    public Integer getServiceFeeType() {
        return serviceFeeType;
    }

    public void setServiceFeeType(Integer serviceFeeType) {
        this.serviceFeeType = serviceFeeType;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }
}

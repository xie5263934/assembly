package com.example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 计算贷款的总工厂，所有计算贷款分期都只需要引入这个贷款工厂，
 * 然后调用其中的方法，由贷款工厂自动根据参数选择对应的算法进行计算，并且返回结果
 *
 * @author jinrun.xie
 * @date 2019/7/3
 **/
public class LoanCalculateFactory {

    /**
     * 计算分期
     *
     * @param loanCalculateRequest
     * @return
     */
    public List<LoanSeparateDetail> calculate(LoanCalculateRequest loanCalculateRequest) {
        setScala(loanCalculateRequest);
        CalculateUtil calculateUtil = null;
        switch (loanCalculateRequest.getLoanType()) {
            case 1:
                calculateUtil = EquivalentPrincipalAndInterestUtil.getInstance();
                break;
            case 2:
                calculateUtil = EquivalentPrincipalAndEquivalentInterestUtil.getInstance();
                break;
            case 4:
                calculateUtil = InterestAheadAndPrincipalAfterUtil.getInstance();
                break;
            case 5:
                calculateUtil = EquivalentPrincipalUtil.getInstance();
        }
        return calculateUtil.calculateSeparate(loanCalculateRequest);
    }

    /**
     * 强制精度设置，将贷款总额度设置为两位小数
     * 利息设置为八位小数
     * 检查计息方式，如果为空，就设置为默认按月计息
     * 检查免息日，如果为空，就设置为0天
     * 尾差处理方式，如果为空，默认就设置为1
     *
     * @param loanCalculateRequest
     */
    private void setScala(LoanCalculateRequest loanCalculateRequest) {
        loanCalculateRequest.getTotalPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP);
        loanCalculateRequest.getRate().setScale(8, BigDecimal.ROUND_HALF_UP);
        Date loanDay = loanCalculateRequest.getStartLoanDay();
        if (loanDay == null) {
            loanCalculateRequest.setStartLoanDay(new Date());
        }
        Integer rateType = loanCalculateRequest.getRateType();
        if (rateType == null || rateType < 1 || rateType > 4) {
            loanCalculateRequest.setRateType(1);
        }
        Integer freeDay = loanCalculateRequest.getFreeDay();
        if (freeDay == null || freeDay < 0) {
            loanCalculateRequest.setFreeDay(0);
        }
        Integer tailWay = loanCalculateRequest.getTailWay();
        if (tailWay == null || tailWay < 1 || tailWay > 3) {
            loanCalculateRequest.setTailWay(1);
        }
        Integer serviceFeeType = loanCalculateRequest.getServiceFeeType();
        if (serviceFeeType == null || serviceFeeType < 1 || serviceFeeType > 2) {
            loanCalculateRequest.setServiceFeeType(1);
        }
    }

}

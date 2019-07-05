package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 等额本金计算工具
 * 等额本金是指一种贷款的还款方式，是在还款期内把贷款数总额等分，每月偿还同等数额的本金和剩余贷款在该月所产生的利息，这样由于每月的还款本金额固定，
 * 而利息越来越少，借款人起初还款压力较大，但是随时间的推移每月还款数也越来越少。
 *
 * @author jinrun.xie
 * @date 2019/7/4
 **/
public class EquivalentPrincipalUtil extends CalculateUtil {

    private static volatile EquivalentPrincipalUtil instance = null;

    private EquivalentPrincipalUtil() {

    }

    /**
     * 单利模式获取对象
     *
     * @return
     */
    public static EquivalentPrincipalUtil getInstance() {
        if (instance == null) {
            synchronized (EquivalentPrincipalAndInterestUtil.class) {
                if (instance == null) {
                    instance = new EquivalentPrincipalUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 真正计算分期的方法
     *
     * @param loanCalculateRequest
     * @return
     */
    public List<LoanSeparateDetail> calculateSeparate(LoanCalculateRequest loanCalculateRequest) {
        List<LoanSeparateDetail> separateDetailList = modifyTail(loanCalculateRequest);
        for (int i = 0; i < loanCalculateRequest.getPeriod(); i++) {
            LoanSeparateDetail detail = separateDetailList.get(i);
            BigDecimal lastRemainPrincipal = detail.getRemainRepayPrincipal().add(detail.getPrincipal());
            //设置利息
            BigDecimal interest = calculateInterest(loanCalculateRequest, i, lastRemainPrincipal);
            detail.setInterest(interest);
            //设置应还本金
            BigDecimal principal = calculatePrincipal(loanCalculateRequest, i, lastRemainPrincipal);
            detail.setPrincipal(principal);
            //设置应还本息
            BigDecimal total = calculateTotalRepayAmount(loanCalculateRequest, i, lastRemainPrincipal);
            detail.setTotalRepay(total);
            //设置还款日期
            Date date = calculateRepayDay(loanCalculateRequest, i);
            detail.setRepayDay(date);
        }
        return separateDetailList;
    }

    private List<LoanSeparateDetail> modifyTail(LoanCalculateRequest loanCalculateRequest) {
        List<LoanSeparateDetail> loanSeparateDetailList = new ArrayList<>();
        //剩余应还本金
        BigDecimal remainRepayPrincipal = loanCalculateRequest.getTotalPrincipal();
        //分期每期的本金
        BigDecimal separatePrincipal = remainRepayPrincipal.divide(BigDecimal.valueOf(loanCalculateRequest.getPeriod()), 3, BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i <= loanCalculateRequest.getPeriod(); i++) {
            LoanSeparateDetail detail = new LoanSeparateDetail();
            detail.setPeriod(i);
            detail.setPrincipal(separatePrincipal);
            remainRepayPrincipal = remainRepayPrincipal.subtract(separatePrincipal);
            detail.setRemainRepayPrincipal(remainRepayPrincipal);
            loanSeparateDetailList.add(detail);
        }
        BigDecimal separateTotalPrincipal = separatePrincipal.multiply(BigDecimal.valueOf(loanCalculateRequest.getPeriod()));
        //最后计算贷款总金额a和所有分期金额总和的差值b
        //如果a>b,顺尾差，那么对应分期需要加上尾差
        //如果a<b逆尾差，那么对应分期需要减去尾差
        BigDecimal tail = separateTotalPrincipal.subtract(loanCalculateRequest.getTotalPrincipal());
        modifyTailByCondition(loanCalculateRequest, loanSeparateDetailList, tail);
        //处理手续费
        handleServiceFee(loanCalculateRequest, loanSeparateDetailList);
        return loanSeparateDetailList;
    }


    /**
     * 计算每个月应还的本息
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @return
     */
    @Override
    protected BigDecimal calculateTotalRepayAmount(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        BigDecimal totalRePay = BigDecimal.ZERO;
        totalRePay = totalRePay.add(calculatePrincipal(loanCalculateRequest, currentPeriod, remainRepayPrincipal)).add(calculateInterest(loanCalculateRequest, currentPeriod, remainRepayPrincipal));
        totalRePay = totalRePay.setScale(2, BigDecimal.ROUND_HALF_UP);
        return totalRePay;
    }

    /**
     * 计算分期的应还本金
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @return
     */
    @Override
    protected BigDecimal calculatePrincipal(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        //每月本金=贷款总金额/贷款期数
        return loanCalculateRequest.getTotalPrincipal().divide(BigDecimal.valueOf(loanCalculateRequest.getPeriod()), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算分期应还利息
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @return
     */
    @Override
    protected BigDecimal calculateInterest(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        //每个月利息=剩余本金*月利率
        BigDecimal monthRate = getMonthRate(loanCalculateRequest.getRate());
        BigDecimal interest = remainRepayPrincipal.multiply(monthRate);
        interest = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
        return interest;
    }
}

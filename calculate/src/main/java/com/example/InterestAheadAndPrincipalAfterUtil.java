package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 先息后本计算工具
 * 先息后本还款，是指前N-1期只需要还一定的利息，最后一期需要归还全部本金的贷款方式
 * 可以分为按月还利息，按季度还利息，按年度还利息等方式
 *
 * @author jinrun.xie
 * @date 2019/7/5
 **/
public class InterestAheadAndPrincipalAfterUtil extends CalculateUtil {

    private static volatile InterestAheadAndPrincipalAfterUtil instance = null;

    private InterestAheadAndPrincipalAfterUtil() {

    }

    /**
     * 单利模式获取对象
     *
     * @return
     */
    public static InterestAheadAndPrincipalAfterUtil getInstance() {
        if (instance == null) {
            synchronized (EquivalentPrincipalAndInterestUtil.class) {
                if (instance == null) {
                    instance = new InterestAheadAndPrincipalAfterUtil();
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
    @Override
    public List<LoanSeparateDetail> calculateSeparate(LoanCalculateRequest loanCalculateRequest) {
        List<LoanSeparateDetail> loanSeparateDetailList = new ArrayList<>(loanCalculateRequest.getPeriod());
        BigDecimal amount = loanCalculateRequest.getTotalPrincipal();
        BigDecimal interest = calculateInterest(loanCalculateRequest, 0, BigDecimal.ZERO);
        for (int i = 1; i <= loanCalculateRequest.getPeriod(); i++) {
            LoanSeparateDetail detail = new LoanSeparateDetail();
            //TODO
            //因为目前规则未定，那么就设置为前面的N-1期还利息，最后第N期只还本金,这样就会出现一种极端情况，用户只分一期，那么直接那一期只收本金，收不到利息了
            if (i < loanCalculateRequest.getPeriod()) {
                detail.setPrincipal(BigDecimal.ZERO);
                detail.setTotalRepay(interest);
                detail.setInterest(interest);
                detail.setRemainRepayPrincipal(amount);
                detail.setPeriod(i);
                detail.setRepayDay(calculateRepayDay(loanCalculateRequest, i));
            } else {
                detail.setPrincipal(amount);
                detail.setTotalRepay(amount);
                detail.setInterest(BigDecimal.ZERO);
                detail.setRemainRepayPrincipal(BigDecimal.ZERO);
                detail.setPeriod(i);
                detail.setRepayDay(calculateRepayDay(loanCalculateRequest, i));
            }
            loanSeparateDetailList.add(detail);
        }
        //处理手续费
        handleServiceFee(loanCalculateRequest, loanSeparateDetailList);
        return loanSeparateDetailList;
    }

    /**
     * 计算每个月应还的本息
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @param remainRepayPrincipal
     * @return
     */
    @Override
    protected BigDecimal calculateTotalRepayAmount(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        return null;
    }

    /**
     * 计算分期的应还本金
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @param remainRepayPrincipal
     * @return
     */
    @Override
    protected BigDecimal calculatePrincipal(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        return null;
    }

    /**
     * 计算分期应还利息
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @param remainRepayPrincipal
     * @return
     */
    @Override
    protected BigDecimal calculateInterest(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        BigDecimal interest = BigDecimal.ZERO;
        //按月计息
        if (loanCalculateRequest.getRateType() == 1) {
            interest = loanCalculateRequest.getTotalPrincipal().multiply(getMonthRate(loanCalculateRequest.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
            //按季度计息
        } else if (loanCalculateRequest.getRateType() == 3) {
            interest = loanCalculateRequest.getTotalPrincipal().multiply(loanCalculateRequest.getRate().divide(BigDecimal.valueOf(400), 8, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
            //按年计息
        } else if (loanCalculateRequest.getRateType() == 4) {
            interest = loanCalculateRequest.getTotalPrincipal().multiply(loanCalculateRequest.getRate().divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return interest;
    }

    /**
     * 计算还款日期
     *
     * @param loanCalculateRequest
     * @param currentPeriod
     * @return
     */
    @Override
    protected Date calculateRepayDay(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod) {
        Calendar calendar = Calendar.getInstance();
        //设置贷款日期
        calendar.setTime(loanCalculateRequest.getStartLoanDay());
        //按月计息
        if (loanCalculateRequest.getRateType() == 1) {
            //月数加上第N期个月数，表示第N期还款日期
            calendar.add(Calendar.MONTH, currentPeriod);
        }
        //按季度计息
        else if (loanCalculateRequest.getRateType() == 3) {
            //月数加上第N期个月数*3，表示按季度的第N期还款日期
            calendar.add(Calendar.MONTH, currentPeriod * 3);
            //按年计息
        } else if (loanCalculateRequest.getRateType() == 4) {
            //年数加上第N期个年数，表示第N期还款日期
            calendar.add(Calendar.YEAR, currentPeriod);
        }
        //如果有免息天数，需要加上免息天数
        if (loanCalculateRequest.getFreeDay() > 0) {
            calendar.add(Calendar.DATE, loanCalculateRequest.getFreeDay());
        }
        return calendar.getTime();
    }
}

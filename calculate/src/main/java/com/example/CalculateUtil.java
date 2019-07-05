package com.example;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author jinrun.xie
 * @date 2019/7/3
 **/
public abstract class CalculateUtil {
    /**
     * 真正计算分期的方法
     *
     * @param loanCalculateRequest
     * @return
     */
    public abstract List<LoanSeparateDetail> calculateSeparate(LoanCalculateRequest loanCalculateRequest);

    /**
     * 计算每个月应还的本息
     *
     * @return
     */
    protected abstract BigDecimal calculateTotalRepayAmount(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal);

    /**
     * 计算分期的应还本金
     *
     * @return
     */
    protected abstract BigDecimal calculatePrincipal(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal);

    /**
     * 计算分期应还利息
     *
     * @return
     */
    protected abstract BigDecimal calculateInterest(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal);

    /**
     * 计算还款日期
     *
     * @return
     */
    protected Date calculateRepayDay(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod) {
        Calendar calendar = Calendar.getInstance();
        //设置贷款日期
        calendar.setTime(loanCalculateRequest.getStartLoanDay());
        //月数加上第N期个月数，表示第N期还款日期
        calendar.add(Calendar.MONTH, currentPeriod);
        if (loanCalculateRequest.getFreeDay() > 0) {
            calendar.add(Calendar.DATE, loanCalculateRequest.getFreeDay());
        }
        return calendar.getTime();
    }

    /**
     * 获取月利率
     * 计算方式=年利率/12个月/100
     * 保留8位的小数精度
     *
     * @param rate
     * @return
     */
    protected BigDecimal getMonthRate(BigDecimal rate) {
        BigDecimal monthRate = BigDecimal.ZERO;
        monthRate = rate.divide(BigDecimal.valueOf(1200), 8, BigDecimal.ROUND_HALF_UP);
        return monthRate;
    }


    /**
     * 尾差处理
     *
     * @param loanCalculateRequest
     * @param loanSeparateDetailList
     * @param tail
     */
    protected void modifyTailByCondition(LoanCalculateRequest loanCalculateRequest, List<LoanSeparateDetail> loanSeparateDetailList, BigDecimal tail) {
        //如果顺尾差，那么表示贷款总额度大于所有贷款分期本金的总和，那么需要给对应分期加上尾差
        if (tail.compareTo(BigDecimal.ZERO) > 0) {
            //尾差补在第一期
            if (loanCalculateRequest.getTailWay() == 1) {
                BigDecimal principal = loanSeparateDetailList.get(0).getPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(0).setPrincipal(principal);
                BigDecimal remainPrincipal = loanSeparateDetailList.get(0).getRemainRepayPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(0).setRemainRepayPrincipal(remainPrincipal);
            } else if (loanCalculateRequest.getTailWay() == 2) {
                //尾差补在最后一期
                BigDecimal principal = loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).getPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).setPrincipal(principal);
                BigDecimal remainPrincipal = loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).getRemainRepayPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).setRemainRepayPrincipal(remainPrincipal);
            } else {
                //尾差补在每一期
                loanSeparateDetailList.stream().forEach(item -> {
                    BigDecimal principal = item.getPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                    item.setPrincipal(principal);
                    BigDecimal remainPrincipal = item.getRemainRepayPrincipal().add(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                    item.setRemainRepayPrincipal(remainPrincipal);
                });
            }

            //如果逆尾差，表示贷款总额度小于所有贷款分期本金总的总和，那么对应分期需要减去尾差
        } else if (tail.compareTo(BigDecimal.ZERO) < 0) {
            //尾差补在第一期
            if (loanCalculateRequest.getTailWay() == 1) {
                BigDecimal principal = loanSeparateDetailList.get(0).getPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(0).setPrincipal(principal);
                BigDecimal remainPrincipal = loanSeparateDetailList.get(0).getRemainRepayPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(0).setRemainRepayPrincipal(remainPrincipal);
            } else if (loanCalculateRequest.getTailWay() == 2) {
                //尾差补在最后一期
                BigDecimal principal = loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).getPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).setPrincipal(principal);
                BigDecimal remainPrincipal = loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).getRemainRepayPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).setRemainRepayPrincipal(remainPrincipal);
            } else {
                //尾差补在每一期
                loanSeparateDetailList.stream().forEach(item -> {
                    BigDecimal principal = item.getPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                    item.setPrincipal(principal);
                    BigDecimal remainPrincipal = item.getRemainRepayPrincipal().subtract(tail).setScale(2, BigDecimal.ROUND_HALF_UP);
                    item.setRemainRepayPrincipal(remainPrincipal);
                });
            }
        }
        //不管需不需要补尾差，最后都再设置一下精度
        loanSeparateDetailList.stream().forEach(item -> {
            BigDecimal principal = item.getPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP);
            item.setPrincipal(principal);
            BigDecimal remainPrincipal = item.getRemainRepayPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP);
            item.setRemainRepayPrincipal(remainPrincipal);
        });
    }

    /**
     * 手续费处理
     *
     * @param loanCalculateRequest
     * @param loanSeparateDetailList
     */
    protected void handleServiceFee(LoanCalculateRequest loanCalculateRequest, List<LoanSeparateDetail> loanSeparateDetailList) {
        if (loanSeparateDetailList != null && loanSeparateDetailList.size() > 1) {
            BigDecimal serviceFee = loanCalculateRequest.getServiceFee();
            if (serviceFee != null) {
                //手续费加在首期上
                if (loanCalculateRequest.getServiceFeeType() == 1) {
                    loanSeparateDetailList.get(0).getTotalRepay().add(serviceFee);
                    //手续费加在尾期上
                } else {
                    loanSeparateDetailList.get(loanSeparateDetailList.size() - 1).getTotalRepay().add(serviceFee);
                }
            }
        }
    }
}

package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 等本等息计算工具
 * 等本等息是指一种贷款的还款方式，是在还款期内把贷款数总额等分，每月偿还同等数额的本金
 * 但是在计算每个月的利息的时候，还是按照最初的贷款总额进行计算，所以每个月要还的利息也是相同的
 * 最后每个月要还的本息是一样多的，这是一种比较流氓的贷款方式，因为计算利息比较恶心,最后实际的年化利率远远大于名义上的年化利率
 *
 * @author jinrun.xie
 * @date 2019/7/4
 **/
public class EquivalentPrincipalAndEquivalentInterestUtil extends CalculateUtil {

    private static volatile EquivalentPrincipalAndEquivalentInterestUtil instance = null;

    private EquivalentPrincipalAndEquivalentInterestUtil() {

    }

    /**
     * 单利模式获取对象
     *
     * @return
     */
    public static EquivalentPrincipalAndEquivalentInterestUtil getInstance() {
        if (instance == null) {
            synchronized (EquivalentPrincipalAndInterestUtil.class) {
                if (instance == null) {
                    instance = new EquivalentPrincipalAndEquivalentInterestUtil();
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
        //每个月应还利息都是一样的，用最初的贷款金额*月利率
        BigDecimal interest = loanCalculateRequest.getTotalPrincipal().multiply(getMonthRate(loanCalculateRequest.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
        for (int i = 0; i < loanCalculateRequest.getPeriod(); i++) {
            LoanSeparateDetail detail = separateDetailList.get(i);
            detail.setInterest(interest);
            //每月应还本息=利息+本金
            BigDecimal total = interest.add(detail.getPrincipal()).setScale(2, BigDecimal.ROUND_HALF_UP);
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
        BigDecimal tail = loanCalculateRequest.getTotalPrincipal().subtract(separateTotalPrincipal);
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
        return null;
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
        return null;
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
        return null;
    }
}

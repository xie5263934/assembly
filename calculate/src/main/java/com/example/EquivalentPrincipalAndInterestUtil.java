package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 等额本息计算工具
 * 等额本息还款，也称定期付息，即借款人每月按相等的金额偿还贷款本息，其中每月贷款利息按月初剩余贷款本金计算并逐月结清。把按揭贷款的本金总额与利息总额相加，
 * 然后平均分摊到还款期限的每个月中。作为还款人，每个月还给银行固定金额，但每月还款额中的本金比重逐月递增、利息比重逐月递减。
 *
 * @author jinrun.xie
 * @date 2019/7/3
 **/
public class EquivalentPrincipalAndInterestUtil extends CalculateUtil {

    private static volatile EquivalentPrincipalAndInterestUtil instance = null;

    private EquivalentPrincipalAndInterestUtil() {

    }

    /**
     * 单利模式获取对象
     *
     * @return
     */
    public static EquivalentPrincipalAndInterestUtil getInstance() {
        if (instance == null) {
            synchronized (EquivalentPrincipalAndInterestUtil.class) {
                if (instance == null) {
                    instance = new EquivalentPrincipalAndInterestUtil();
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
        List<LoanSeparateDetail> loanSeparateDetailList = new ArrayList<>();
        //剩余应还本金
        BigDecimal remainRepayPrincipal = loanCalculateRequest.getTotalPrincipal();
        //所有分期已还本金的总和
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = 1; i <= loanCalculateRequest.getPeriod(); i++) {
            LoanSeparateDetail detail = new LoanSeparateDetail();
            //设置期数
            detail.setPeriod(i);
            //设置利息
            BigDecimal interest = calculateInterest(loanCalculateRequest, i, remainRepayPrincipal);
            detail.setInterest(interest);
            //设置应还本金
            BigDecimal principal = calculatePrincipal(loanCalculateRequest, i, remainRepayPrincipal);
            detail.setPrincipal(principal);
            //将每期已还的本金累计起来
            amount = amount.add(principal);
            //设置应还本息
            BigDecimal total = calculateTotalRepayAmount(loanCalculateRequest, i, remainRepayPrincipal);
            detail.setTotalRepay(total);
            //设置还款日期
            Date date = calculateRepayDay(loanCalculateRequest, i);
            detail.setRepayDay(date);
            //设置剩余应还本金
            remainRepayPrincipal = remainRepayPrincipal.subtract(principal);
            BigDecimal remain = remainRepayPrincipal.setScale(3, BigDecimal.ROUND_HALF_UP);
            detail.setRemainRepayPrincipal(remain);
            loanSeparateDetailList.add(detail);
        }
        modifyTail(loanCalculateRequest, loanSeparateDetailList, amount);
        //处理手续费
        handleServiceFee(loanCalculateRequest, loanSeparateDetailList);
        return loanSeparateDetailList;
    }

    /**
     * 调整尾差
     *
     * @param loanCalculateRequest
     * @param loanSeparateDetailList
     * @param amount
     */
    private void modifyTail(LoanCalculateRequest loanCalculateRequest, List<LoanSeparateDetail> loanSeparateDetailList, BigDecimal amount) {
        BigDecimal tail = loanCalculateRequest.getTotalPrincipal().subtract(amount);
        //最后计算贷款总金额a和所有分期金额总和的差值b
        //如果a>b,顺尾差，那么对应分期需要加上尾差
        //如果a<b逆尾差，那么对应分期需要减去尾差
        modifyTailByCondition(loanCalculateRequest, loanSeparateDetailList, tail);
    }


    /**
     * 计算每个月应还的本息
     * 等额本息计算获取还款方式为等额本息的每月偿还本金和利息
     * a*i(1+i)^N/[(1+i)^N-1]
     * a贷款总额,i贷款月利率,N贷款月数
     * 公式：每月偿还本息= 贷款本金×月利率×[(1＋月利率)＾还款月数]÷[(1＋月利率)＾还款月数-1]
     *
     * @return
     */
    @Override
    protected BigDecimal calculateTotalRepayAmount(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        BigDecimal totalRepay;
        BigDecimal monthRate = getMonthRate(loanCalculateRequest.getRate());
        //a*i
        totalRepay = loanCalculateRequest.getTotalPrincipal().multiply(monthRate);
        //(1+i)^N
        BigDecimal tmp1 = BigDecimal.ONE.add(monthRate).pow(loanCalculateRequest.getPeriod());
        //(1+i)^N-1
        BigDecimal tmp2 = BigDecimal.ONE.add(monthRate).pow(loanCalculateRequest.getPeriod()).subtract(BigDecimal.ONE);
        totalRepay = totalRepay.multiply(tmp1).divide(tmp2, 2, BigDecimal.ROUND_HALF_UP);
        return totalRepay;
    }

    /**
     * 计算分期的应还本金
     * 等额本息本金还款方式为等额本息的每月偿还本金
     * a*i[(1+i)^(n-1)]/[(1+i)^N-1]
     * a贷款总额,i贷款月利率,N贷款月数,n还款月序号
     * 公式：每月偿还利息=贷款本金×月利率×[（1+月利率）^（还款月序号-1）] ÷ [(1+月利率)^还款月数-1]
     *
     * @return
     */
    @Override
    protected BigDecimal calculatePrincipal(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        BigDecimal totalRepay;
        BigDecimal monthRate = getMonthRate(loanCalculateRequest.getRate());
        //a*i
        totalRepay = loanCalculateRequest.getTotalPrincipal().multiply(monthRate);
        //(1+i)^(n-1)
        BigDecimal tmp1 = BigDecimal.ONE.add(monthRate).pow(currentPeriod - 1);
        //(1+i)^N-1
        BigDecimal tmp2 = BigDecimal.ONE.add(monthRate).pow(loanCalculateRequest.getPeriod()).subtract(BigDecimal.ONE);
        totalRepay = totalRepay.multiply(tmp1).divide(tmp2, 3, BigDecimal.ROUND_HALF_UP);
        return totalRepay;
    }

    /**
     * 计算分期应还利息
     * a*i*[(1+i)^N-(1+i)^(n-1)]/[(1+i)^N-1]
     * a贷款总额,i贷款月利率,N贷款月数,n还款月序号
     * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @return
     */
    @Override
    protected BigDecimal calculateInterest(LoanCalculateRequest loanCalculateRequest, Integer currentPeriod, BigDecimal remainRepayPrincipal) {
        BigDecimal totalRepay;
        BigDecimal monthRate = getMonthRate(loanCalculateRequest.getRate());
        //a*i
        totalRepay = loanCalculateRequest.getTotalPrincipal().multiply(monthRate);
        //(1+i)^N
        BigDecimal tmp1 = BigDecimal.ONE.add(monthRate).pow(loanCalculateRequest.getPeriod());
        //(1+i)^(n-1)
        BigDecimal tmp2 = BigDecimal.ONE.add(monthRate).pow(currentPeriod - 1);
        BigDecimal tmp3 = tmp1.subtract(tmp2);
        //(1+i)^N-1
        BigDecimal tmp4 = BigDecimal.ONE.add(monthRate).pow(loanCalculateRequest.getPeriod()).subtract(BigDecimal.ONE);
        totalRepay = totalRepay.multiply(tmp3).divide(tmp4, 2, BigDecimal.ROUND_HALF_UP);
        return totalRepay;
    }
}

package com.example;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author jinrun.xie
 * @date 2019/7/4
 **/
public class CalculateTest {
    /**
     * 等额本息测试
     *
     * @throws ParseException
     */
    @Test
    public void test1() throws ParseException {
        LoanCalculateFactory loanCalculateFactory = new LoanCalculateFactory();
        LoanCalculateRequest loanCalculateRequest = new LoanCalculateRequest();
        loanCalculateRequest.setTotalPrincipal(BigDecimal.valueOf(1000000));
        loanCalculateRequest.setPeriod(240);
        loanCalculateRequest.setRate(BigDecimal.valueOf(4.9));
        loanCalculateRequest.setRateType(1);
        loanCalculateRequest.setLoanType(1);
        loanCalculateRequest.setFreeDay(0);
        loanCalculateRequest.setTailWay(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loanCalculateRequest.setStartLoanDay(simpleDateFormat.parse("2019-03-31 22:04:25"));
        List<LoanSeparateDetail> list = loanCalculateFactory.calculate(loanCalculateRequest);
        for (LoanSeparateDetail tmp : list) {
            System.out.println(JSON.toJSONString(tmp));
        }
    }

    /**
     * 等额本金测试
     *
     * @throws ParseException
     */
    @Test
    public void test2() throws ParseException {
        LoanCalculateFactory loanCalculateFactory = new LoanCalculateFactory();
        LoanCalculateRequest loanCalculateRequest = new LoanCalculateRequest();
        loanCalculateRequest.setTotalPrincipal(BigDecimal.valueOf(1000000));
        loanCalculateRequest.setPeriod(240);
        loanCalculateRequest.setRate(BigDecimal.valueOf(4.9));
        loanCalculateRequest.setRateType(1);
        loanCalculateRequest.setLoanType(5);
        loanCalculateRequest.setFreeDay(0);
        loanCalculateRequest.setTailWay(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loanCalculateRequest.setStartLoanDay(simpleDateFormat.parse("2019-03-31 22:04:25"));
        List<LoanSeparateDetail> list = loanCalculateFactory.calculate(loanCalculateRequest);
        for (LoanSeparateDetail tmp : list) {
            System.out.println(JSON.toJSONString(tmp));
        }
    }

    /**
     * 等本等息测试
     *
     * @throws ParseException
     */
    @Test
    public void test3() throws ParseException {
        LoanCalculateFactory loanCalculateFactory = new LoanCalculateFactory();
        LoanCalculateRequest loanCalculateRequest = new LoanCalculateRequest();
        loanCalculateRequest.setTotalPrincipal(BigDecimal.valueOf(1000000));
        loanCalculateRequest.setPeriod(240);
        loanCalculateRequest.setRate(BigDecimal.valueOf(4.9));
        loanCalculateRequest.setRateType(1);
        loanCalculateRequest.setLoanType(2);
        loanCalculateRequest.setFreeDay(0);
        loanCalculateRequest.setTailWay(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loanCalculateRequest.setStartLoanDay(simpleDateFormat.parse("2019-03-31 22:04:25"));
        List<LoanSeparateDetail> list = loanCalculateFactory.calculate(loanCalculateRequest);
        for (LoanSeparateDetail tmp : list) {
            System.out.println(JSON.toJSONString(tmp));
        }
    }

    /**
     * 先息后本测试-按照季度
     *
     * @throws ParseException
     */
    @Test
    public void test4() throws ParseException {
        LoanCalculateFactory loanCalculateFactory = new LoanCalculateFactory();
        LoanCalculateRequest loanCalculateRequest = new LoanCalculateRequest();
        loanCalculateRequest.setTotalPrincipal(BigDecimal.valueOf(1000000));
        loanCalculateRequest.setPeriod(12);
        loanCalculateRequest.setRate(BigDecimal.valueOf(4.9));
        loanCalculateRequest.setRateType(3);
        loanCalculateRequest.setLoanType(4);
        loanCalculateRequest.setFreeDay(0);
        loanCalculateRequest.setTailWay(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loanCalculateRequest.setStartLoanDay(simpleDateFormat.parse("2019-03-31 22:04:25"));
        List<LoanSeparateDetail> list = loanCalculateFactory.calculate(loanCalculateRequest);
        for (LoanSeparateDetail tmp : list) {
            System.out.println(JSON.toJSONString(tmp));
        }
    }

    /**
     * 先息后本测试-按照年
     *
     * @throws ParseException
     */
    @Test
    public void test5() throws ParseException {
        LoanCalculateFactory loanCalculateFactory = new LoanCalculateFactory();
        LoanCalculateRequest loanCalculateRequest = new LoanCalculateRequest();
        loanCalculateRequest.setTotalPrincipal(BigDecimal.valueOf(1000000));
        loanCalculateRequest.setPeriod(6);
        loanCalculateRequest.setRate(BigDecimal.valueOf(4.9));
        loanCalculateRequest.setRateType(4);
        loanCalculateRequest.setLoanType(4);
        loanCalculateRequest.setFreeDay(0);
        loanCalculateRequest.setTailWay(2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loanCalculateRequest.setStartLoanDay(simpleDateFormat.parse("2019-03-31 22:04:25"));
        List<LoanSeparateDetail> list = loanCalculateFactory.calculate(loanCalculateRequest);
        for (LoanSeparateDetail tmp : list) {
            System.out.println(JSON.toJSONString(tmp));
        }
    }

}

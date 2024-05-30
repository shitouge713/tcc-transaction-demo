package org.pankai.tcctransaction.sample.order.domain.service;

import org.pankai.tcctransaction.Compensable;
import org.pankai.tcctransaction.sample.external.dto.CapitalTradeOrderDto;
import org.pankai.tcctransaction.sample.external.dto.RedPacketTradeOrderDto;
import org.pankai.tcctransaction.sample.external.service.CapitalTradeOrderService;
import org.pankai.tcctransaction.sample.external.service.RedPacketTradeOrderService;
import org.pankai.tcctransaction.sample.order.domain.entity.Order;
import org.pankai.tcctransaction.sample.order.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Service
public class PaymentService {

    @Autowired
    private CapitalTradeOrderService capitalTradeOrderService;

    @Autowired
    private RedPacketTradeOrderService redPacketTradeOrderService;

    @Autowired
    private OrderRepository orderRepository;

    @Compensable(confirmMethod = "confirmMakePayment", cancelMethod = "cancelMakePayment")
    @Transactional
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        System.out.println("order服务执行try阶段逻辑");

        order.pay(redPacketPayAmount, capitalPayAmount);
        orderRepository.updateOrder(order);

        /**
         * 疑问1：为什么关联业务的record方法，还要标记@Compensable呢
         * 这样，提交或者回滚时，不需要通过主事务再次调用分支事务
         * 疑问2：如何知道是调用confirm还是cancel呢
         * TccCompensableAspect 拦截器执行原理
         * 疑问3：如果分支业务失败，不影响主业务和其他分支业务的正常提交，失败的分支业务重试n次，如何实现
         * 1. 分支业务重试，如果分支业务不影响主业务，不要对外抛异常，分支业务内部进行重试
         * 2. 主业务调用分支业务，分支业务返回异常时，主业务对其重试，重试后如果依然异常，该异常trycatch住不向外抛
         */
        capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order));
        redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order));
    }

    public void confirmMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

        System.out.println("order服务执行confirm阶段逻辑");
        order.confirm();

        orderRepository.updateOrder(order);
    }

    public void cancelMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

        System.out.println("order服务执行cancel阶段逻辑");

        order.cancelPayment();

        orderRepository.updateOrder(order);
    }

    private CapitalTradeOrderDto buildCapitalTradeOrderDto(Order order) {

        CapitalTradeOrderDto tradeOrderDto = new CapitalTradeOrderDto();
        tradeOrderDto.setAmount(order.getCapitalPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }

    private RedPacketTradeOrderDto buildRedPacketTradeOrderDto(Order order) {
        RedPacketTradeOrderDto tradeOrderDto = new RedPacketTradeOrderDto();
        tradeOrderDto.setAmount(order.getRedPacketPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }

}

package org.mengyun.tcctransaction.sample.service;

import org.apache.commons.lang3.tuple.Pair;
import org.mengyun.tcctransaction.sample.dao.ShopDao;
import org.mengyun.tcctransaction.sample.entity.Order;
import org.mengyun.tcctransaction.sample.entity.Shop;
import org.pankai.tcctransaction.CancellingException;
import org.pankai.tcctransaction.ConfirmingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Service
public class PlaceOrderService {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    public String placeOrder(long payerUserId, long shopId, List<Pair<Long, Integer>> productQuantities, BigDecimal redPacketPayAmount) {
        Shop shop = shopDao.findById(shopId);
        Order order = orderService.createOrder(payerUserId, shop.getOwnerUserId(), productQuantities);
        try {
            paymentService.makePayment(order, redPacketPayAmount, order.getTotalAmount().subtract(redPacketPayAmount));
        } catch (ConfirmingException e) {
            //exception throws with the tcc transaction status is CONFIRMING,
            //when tcc transaction is confirming status,
            // the tcc transaction recovery will try to confirm the whole transaction to ensure eventually consistent.
        } catch (CancellingException e) {
            //exception throws with the tcc transaction status is CANCELLING,
            //when tcc transaction is under CANCELLING status,
            // the tcc transaction recovery will try to cancel the whole transaction to ensure eventually consistent.
        } catch (Throwable e) {
            //other exceptions throws at TRYING stage.
            //you can retry or cancel the operation.
            throw new RuntimeException(e);
        }
        return order.getMerchantOrderNo();
    }


}

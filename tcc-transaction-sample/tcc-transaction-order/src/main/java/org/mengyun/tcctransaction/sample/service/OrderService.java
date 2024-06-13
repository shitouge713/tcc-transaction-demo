package org.mengyun.tcctransaction.sample.service;

import org.apache.commons.lang3.tuple.Pair;
import org.mengyun.tcctransaction.sample.dao.OrderDao;
import org.mengyun.tcctransaction.sample.dao.OrderLineDao;
import org.mengyun.tcctransaction.sample.entity.Order;
import org.mengyun.tcctransaction.sample.entity.OrderLine;
import org.mengyun.tcctransaction.sample.domain.OrderDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineDao orderLineDao;

    @Autowired
    private OrderDomain orderFactory;

    @Transactional
    public Order createOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {
        Order order = orderFactory.buildOrder(payerUserId, payeeUserId, productQuantities);
        orderDao.insert(order);
        for (OrderLine orderLine : order.getOrderLines()) {
            orderLineDao.insert(orderLine);
        }
        return order;
    }


}

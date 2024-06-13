package org.mengyun.tcctransaction.sample.domain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.mengyun.tcctransaction.sample.dao.OrderDao;
import org.mengyun.tcctransaction.sample.dao.ProductDao;
import org.mengyun.tcctransaction.sample.entity.Order;
import org.mengyun.tcctransaction.sample.entity.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Component
public class OrderDomain extends ServiceImpl<OrderDao, Order> implements IService<Order> {

    @Autowired
    private ProductDao productDao;

    public Order buildOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {

        Order order = new Order(payerUserId, payeeUserId);

        for (Pair<Long, Integer> pair : productQuantities) {
            long productId = pair.getLeft();
            order.addOrderLine(new OrderLine(productId, pair.getRight(), productDao.findById(productId).getPrice()));
        }

        return order;
    }

}

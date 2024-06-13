package org.mengyun.tcctransaction.sample.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mengyun.tcctransaction.sample.dao.TradeOrderDao;
import org.mengyun.tcctransaction.sample.entity.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Component
public class TradeOrderDomain extends ServiceImpl<TradeOrderDao, TradeOrder> implements IService<TradeOrder> {

    @Autowired
    private TradeOrderDao tradeOrderDao;

    public void insert(TradeOrder tradeOrder) {
        tradeOrderDao.insert(tradeOrder);
    }

    public void update(TradeOrder tradeOrder) {
        tradeOrderDao.update(tradeOrder);
    }

    public TradeOrder findByMerchantOrderNo(String merchantOrderNo) {
        return tradeOrderDao.findByMerchantOrderNo(merchantOrderNo);
    }
}

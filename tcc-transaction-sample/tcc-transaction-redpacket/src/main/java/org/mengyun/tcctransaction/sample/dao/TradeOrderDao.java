package org.mengyun.tcctransaction.sample.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mengyun.tcctransaction.sample.entity.RedPacketAccount;
import org.mengyun.tcctransaction.sample.entity.TradeOrder;

/**
 * Created by twinkle.zhou on 16/11/14.
 */
public interface TradeOrderDao extends BaseMapper<TradeOrder> {

    int insert(TradeOrder tradeOrder);

    int update(TradeOrder tradeOrder);

    TradeOrder findByMerchantOrderNo(String merchantOrderNo);
}

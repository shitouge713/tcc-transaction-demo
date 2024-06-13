package org.mengyun.tcctransaction.sample.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.TradeOrder;

/**
 * Created by twinkle.zhou on 16/11/14.
 */
@Mapper
public interface TradeOrderDao extends BaseMapper<TradeOrder> {

    int insert(TradeOrder tradeOrder);

    int update(TradeOrder tradeOrder);

    TradeOrder findByMerchantOrderNo(String merchantOrderNo);
}

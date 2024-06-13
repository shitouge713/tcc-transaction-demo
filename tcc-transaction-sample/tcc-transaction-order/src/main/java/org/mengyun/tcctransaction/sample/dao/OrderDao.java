package org.mengyun.tcctransaction.sample.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.Order;

/**
 * Created by changming.xie on 4/1/16.
 */
@Mapper
public interface OrderDao extends BaseMapper<Order> {

    int insert(Order order);

    int update(Order order);

    Order findByMerchantOrderNo(String merchantOrderNo);
}

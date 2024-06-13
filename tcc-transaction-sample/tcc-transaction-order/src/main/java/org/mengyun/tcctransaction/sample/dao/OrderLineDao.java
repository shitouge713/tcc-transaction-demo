package org.mengyun.tcctransaction.sample.dao;


import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.OrderLine;

/**
 * Created by changming.xie on 4/1/16.
 */
@Mapper
public interface OrderLineDao {
    void insert(OrderLine orderLine);
}

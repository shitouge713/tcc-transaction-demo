package org.mengyun.tcctransaction.sample.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.Shop;
import org.springframework.stereotype.Repository;

/**
 * Created by changming.xie on 4/1/16.
 */
@Mapper
public interface ShopDao extends BaseMapper<Shop> {
    Shop findById(long id);
}

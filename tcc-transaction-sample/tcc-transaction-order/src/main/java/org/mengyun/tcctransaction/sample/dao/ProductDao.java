package org.mengyun.tcctransaction.sample.dao;



import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.Product;

import java.util.List;

/**
 * Created by twinkle.zhou on 16/11/10.
 */
@Mapper
public interface ProductDao {

    Product findById(long productId);

    List<Product> findByShopId(long shopId);
}

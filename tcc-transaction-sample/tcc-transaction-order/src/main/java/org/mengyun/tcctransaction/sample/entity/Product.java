package org.mengyun.tcctransaction.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pktczwd on 2016/12/16.
 */
@Data
@EqualsAndHashCode
@TableName("ord_product")
public class Product implements Serializable {
    private long productId;

    private long shopId;

    private String productName;

    private BigDecimal price;

    public Product() {
    }

    public Product(long productId, long shopId, String productName, BigDecimal price) {
        this.productId = productId;
        this.shopId = shopId;
        this.productName = productName;
        this.price = price;
    }
}

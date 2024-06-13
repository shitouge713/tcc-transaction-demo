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
@TableName("ord_order_line")
public class OrderLine implements Serializable {

    private long id;

    private long productId;

    private int quantity;

    private BigDecimal unitPrice;

    public OrderLine() {

    }

    public OrderLine(Long productId, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

}

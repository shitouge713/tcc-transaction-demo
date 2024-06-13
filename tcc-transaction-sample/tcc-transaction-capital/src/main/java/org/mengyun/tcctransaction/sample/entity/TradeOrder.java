package org.mengyun.tcctransaction.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Data
@EqualsAndHashCode
@TableName("cap_trade_order")
public class TradeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private long selfUserId;

    private long oppositeUserId;

    private String merchantOrderNo;

    private BigDecimal amount;

    private String status = "DRAFT";

    private long version = 1L;
    public TradeOrder() {
    }

    public TradeOrder(long selfUserId, long oppositeUserId, String merchantOrderNo, BigDecimal amount) {
        this.selfUserId = selfUserId;
        this.oppositeUserId = oppositeUserId;
        this.merchantOrderNo = merchantOrderNo;
        this.amount = amount;
    }

    public void confirm() {
        this.status = "CONFIRM";
    }

    public void cancel() {
        this.status = "CANCEL";
    }

    public void updateVersion() {
        this.version = version + 1;
    }
}

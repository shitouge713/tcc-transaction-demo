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
@TableName("cap_capital_account")
public class CapitalAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private long userId;

    private BigDecimal balanceAmount;

    public void transferFrom(BigDecimal amount) {
        this.balanceAmount = this.balanceAmount.subtract(amount);

        if (BigDecimal.ZERO.compareTo(this.balanceAmount) > 0) {
            throw new RuntimeException("账户余额不足.");
        }
    }
    public void transferTo(BigDecimal amount) {
        this.balanceAmount = this.balanceAmount.add(amount);
    }

    public void cancelTransfer(BigDecimal amount) {
        transferTo(amount);
    }
}

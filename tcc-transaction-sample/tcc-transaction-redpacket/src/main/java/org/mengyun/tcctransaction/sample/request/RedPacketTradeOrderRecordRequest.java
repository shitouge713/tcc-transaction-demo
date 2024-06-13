package org.mengyun.tcctransaction.sample.request;

import org.mengyun.tcctransaction.sample.dto.RedPacketTradeOrderDto;
import org.pankai.tcctransaction.api.TransactionContext;

/**
 * Created by pktczwd on 2016/12/26.
 */
public class RedPacketTradeOrderRecordRequest {

    private TransactionContext transactionContext;
    private RedPacketTradeOrderDto redPacketTradeOrderDto;

    public TransactionContext getTransactionContext() {
        return transactionContext;
    }

    public void setTransactionContext(TransactionContext transactionContext) {
        this.transactionContext = transactionContext;
    }

    public RedPacketTradeOrderDto getRedPacketTradeOrderDto() {
        return redPacketTradeOrderDto;
    }

    public void setRedPacketTradeOrderDto(RedPacketTradeOrderDto redPacketTradeOrderDto) {
        this.redPacketTradeOrderDto = redPacketTradeOrderDto;
    }
}

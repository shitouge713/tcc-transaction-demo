package org.mengyun.tcctransaction.sample.request;

import org.mengyun.tcctransaction.sample.dto.CapitalTradeOrderDto;
import org.pankai.tcctransaction.api.TransactionContext;

/**
 * Created by pktczwd on 2016/12/22.
 */
public class CapitalTradeOrderRecordRequest {

    private TransactionContext transactionContext;
    private CapitalTradeOrderDto capitalTradeOrderDto;

    public TransactionContext getTransactionContext() {
        return transactionContext;
    }

    public void setTransactionContext(TransactionContext transactionContext) {
        this.transactionContext = transactionContext;
    }

    public CapitalTradeOrderDto getCapitalTradeOrderDto() {
        return capitalTradeOrderDto;
    }

    public void setCapitalTradeOrderDto(CapitalTradeOrderDto capitalTradeOrderDto) {
        this.capitalTradeOrderDto = capitalTradeOrderDto;
    }
}

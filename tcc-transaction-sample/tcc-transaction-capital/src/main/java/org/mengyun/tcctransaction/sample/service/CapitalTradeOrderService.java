package org.mengyun.tcctransaction.sample.service;

import org.mengyun.tcctransaction.sample.entity.CapitalAccount;
import org.pankai.tcctransaction.Compensable;
import org.mengyun.tcctransaction.sample.entity.TradeOrder;
import org.mengyun.tcctransaction.sample.repository.CapitalAccountDomain;
import org.mengyun.tcctransaction.sample.repository.TradeOrderDomain;
import org.mengyun.tcctransaction.sample.dto.CapitalTradeOrderDto;
import org.pankai.tcctransaction.api.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Service
public class CapitalTradeOrderService {

    @Autowired
    private CapitalAccountDomain capitalAccountDomain;

    @Autowired
    private TradeOrderDomain tradeOrderRepository;

    @Compensable(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord")
    @Transactional
    public String record(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        System.out.println("capital服务执行try阶段逻辑");

        //创建订单之前就检查用户账户是否存在.用户账户存在才允许创建订单.
        CapitalAccount transferFromAccount = capitalAccountDomain.findByUserId(tradeOrderDto.getSelfUserId());
        if (transferFromAccount == null) {
            throw new RuntimeException("指定用户的账户信息不存在.");
        }

        TradeOrder tradeOrder = new TradeOrder(
                tradeOrderDto.getSelfUserId(),
                tradeOrderDto.getOppositeUserId(),
                tradeOrderDto.getMerchantOrderNo(),
                tradeOrderDto.getAmount()
        );

        tradeOrderRepository.insert(tradeOrder);

        //在trying阶段,就进行业务前置条件检查,然后直接扣减金额.
        transferFromAccount.transferFrom(tradeOrderDto.getAmount());

        capitalAccountDomain.save(transferFromAccount);
        return "success";
    }

    @Transactional
    public void confirmRecord(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        System.out.println("capital服务执行confirm阶段逻辑");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        tradeOrder.confirm();
        tradeOrderRepository.update(tradeOrder);

        //在confirming阶段,应该仅仅改变订单状态就ok了,不必再操作账户金额.这里进行加回是为了方便测试.
        CapitalAccount transferToAccount = capitalAccountDomain.findByUserId(tradeOrderDto.getOppositeUserId());

        transferToAccount.transferTo(tradeOrderDto.getAmount());

        capitalAccountDomain.save(transferToAccount);
    }

    @Transactional
    public void cancelRecord(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        System.out.println("capital服务执行cancel阶段逻辑");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            tradeOrderRepository.update(tradeOrder);

            CapitalAccount capitalAccount = capitalAccountDomain.findByUserId(tradeOrderDto.getSelfUserId());
            //这里从代码层面可能存在空指针异常,但是从业务层面不应该出现.因为在trying阶段已经限制了账户存在才能创建订单.
            capitalAccount.cancelTransfer(tradeOrderDto.getAmount());
            capitalAccountDomain.save(capitalAccount);


        }
    }

}

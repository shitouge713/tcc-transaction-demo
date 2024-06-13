package org.mengyun.tcctransaction.sample.service;

import org.pankai.tcctransaction.Compensable;
import org.mengyun.tcctransaction.sample.entity.RedPacketAccount;
import org.mengyun.tcctransaction.sample.repository.RedPacketAccountDomain;
import org.mengyun.tcctransaction.sample.repository.TradeOrderDomain;
import org.mengyun.tcctransaction.sample.dto.RedPacketTradeOrderDto;
import org.mengyun.tcctransaction.sample.entity.TradeOrder;
import org.pankai.tcctransaction.api.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by pktczwd on 2016/12/26.
 */
@Service
public class RedPacketTradeOrderService {

    @Autowired
    private RedPacketAccountDomain redPacketAccountRepository;

    @Autowired
    private TradeOrderDomain tradeOrderRepository;

    /**
     * 如果该业务逻辑异常，是否自动重试
     * @param transactionContext
     * @param redPacketTradeOrderDto
     * @return
     */
    //trying阶段,新建draft状态订单,扣除账户金额.
    @Compensable(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord")
    @Transactional
    public String record(TransactionContext transactionContext, RedPacketTradeOrderDto redPacketTradeOrderDto) {
        System.out.println("red服务执行try阶段逻辑");

        RedPacketAccount transferFromAccount = redPacketAccountRepository.findByUserId(redPacketTradeOrderDto.getSelfUserId());
        if (transferFromAccount == null) {
            throw new RuntimeException("指定用户没有红包账户.");
        }
        //模拟try阶段失败会怎样，是否影响主业务完成情况
        int a = 10;
        int b = 0;
        int c = a / b;
        TradeOrder tradeOrder = new TradeOrder(redPacketTradeOrderDto.getSelfUserId(), redPacketTradeOrderDto.getOppositeUserId(), redPacketTradeOrderDto.getMerchantOrderNo(), redPacketTradeOrderDto.getAmount());

        tradeOrderRepository.insert(tradeOrder);

        transferFromAccount.transferFrom(redPacketTradeOrderDto.getAmount());

        redPacketAccountRepository.save(transferFromAccount);

        return "success";
    }

    @Transactional
    public void confirmRecord(TransactionContext transactionContext, RedPacketTradeOrderDto redPacketTradeOrderDto) {
        System.out.println("red服务执行confirm阶段逻辑");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(redPacketTradeOrderDto.getMerchantOrderNo());
        if (tradeOrder == null) {
            throw new RuntimeException("指定的订单不存在,订单号:" + redPacketTradeOrderDto.getMerchantOrderNo());
        }

        tradeOrder.confirm();
        tradeOrderRepository.update(tradeOrder);

        //出于测试目的,将红包余额加回去.
        RedPacketAccount transferToAccount = redPacketAccountRepository.findByUserId(redPacketTradeOrderDto.getOppositeUserId());
        transferToAccount.transferTo(redPacketTradeOrderDto.getAmount());

        redPacketAccountRepository.save(transferToAccount);
        //模拟confirm提交失败会怎样，是否影响主业务完成情况
        /*int a = 10;
        int b = 0;
        int c = a / b;*/

    }

    @Transactional
    public void cancelRecord(TransactionContext transactionContext, RedPacketTradeOrderDto redPacketTradeOrderDto) {
        System.out.println("red服务执行cancel阶段逻辑");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(redPacketTradeOrderDto.getMerchantOrderNo());

        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            tradeOrderRepository.update(tradeOrder);

            RedPacketAccount capitalAccount = redPacketAccountRepository.findByUserId(redPacketTradeOrderDto.getSelfUserId());

            capitalAccount.cancelTransfer(redPacketTradeOrderDto.getAmount());

            redPacketAccountRepository.save(capitalAccount);
        }
        //模拟cancel提交失败会怎样，是否影响主业务完成情况
        /*int a = 10;
        int b = 0;
        int c = a / b;*/

    }
}

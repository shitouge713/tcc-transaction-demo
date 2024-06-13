package org.pankai.tcctransaction.recover;

import org.pankai.tcctransaction.Transaction;
import org.pankai.tcctransaction.TransactionRepository;
import org.pankai.tcctransaction.api.TransactionStatus;
import org.pankai.tcctransaction.support.TransactionConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 事务恢复器.当事务在指定的时间内未有状态变化时,会根据其上下文来决定提交,回滚或者是需要人工干预.
 * 可以在这里拓展需要人工干预时的通知.
 * 通过@Bean的方式注册到spring容器中
 * Created by pktczwd on 2016/12/7.
 */
public class TransactionRecovery {

    private final Logger logger = LoggerFactory.getLogger(TransactionRecovery.class);

    private TransactionConfigurator transactionConfigurator;

    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    /**
     * 通过定时任务的方式调用开启
     */
    public void startRecover() {
        logger.debug("TransactionRecovery startRecover.................................");
        List<Transaction> transactions = loadErrorTransactions();
        logger.debug("发现 " + transactions.size() + " 异常的事务, 尝试通过重试恢复.");
        //恢复异常的事务
        recoverTransactions(transactions);
    }

    private List<Transaction> loadErrorTransactions() {
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        List<Transaction> transactions = transactionRepository.findAllUnmodifiedSince(new Date(currentTimeInMillis - transactionConfigurator.getRecoverConfig().getRecoverDuration() * 1000));
        List<Transaction> recoverTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            int result = transactionRepository.update(transaction);
            if (result > 0) {
                recoverTransactions.add(transaction);
            }
        }
        return recoverTransactions;
    }

    private void recoverTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            //事务重试次数超过最大次数，默认重试30次，不再重试
            if (transaction.getRetriedCount() > transactionConfigurator.getRecoverConfig().getMaxRetryCount()) {
                logger.error(String.format("重试失败的事务超过最大次数, 不会再重试. 需要人工介入，txid:%s, status:%s, retried count:%d", transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount()));
                continue;
            }
            try {
                transaction.addRetriedCount();
                //需要提交
                if (transaction.getStatus().equals(TransactionStatus.CONFIRMING)) {
                    transaction.changeStatus(TransactionStatus.CONFIRMING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.commit();
                } else {
                    //需要回滚
                    transaction.changeStatus(TransactionStatus.CANCELLING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.rollback();
                }
                //删除已完成的事务
                transactionConfigurator.getTransactionRepository().delete(transaction);
            } catch (Throwable e) {
                logger.warn(String.format("txid:%s, status:%s, retried count:%d, 恢复事务异常, e:", transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount()), e);
            }
        }


    }
}

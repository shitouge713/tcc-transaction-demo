package org.pankai.tcctransaction;

import org.pankai.tcctransaction.api.TransactionContext;
import org.pankai.tcctransaction.api.TransactionStatus;
import org.pankai.tcctransaction.common.TransactionType;
import org.pankai.tcctransaction.support.TransactionConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pktczwd on 2016/12/7.
 */
public class TransactionManager {

    private final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    private TransactionConfigurator transactionConfigurator;

    public TransactionManager(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    private ThreadLocal<Transaction> threadLocalTransaction = new ThreadLocal<Transaction>();

    public void begin() {
        //logger.info("try阶段,生成事务语句-start-");
        Transaction transaction = new Transaction(TransactionType.ROOT);
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        transactionRepository.create(transaction);
        threadLocalTransaction.set(transaction);
        //logger.info("try阶段,生成事务语句-end-");
    }

    public void propagationNewBegin(TransactionContext transactionContext) {
        Transaction transaction = new Transaction(transactionContext);
        //在创建阶段持久化
        transactionConfigurator.getTransactionRepository().create(transaction);
        threadLocalTransaction.set(transaction);
    }

    public void propagationExistBegin(TransactionContext transactionContext) throws NoExistedTransactionException {
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        Transaction transaction = transactionRepository.findByXid(transactionContext.getXid());
        if (transaction != null) {
            transaction.changeStatus(TransactionStatus.valueOf(transactionContext.getStatus()));
            threadLocalTransaction.set(transaction);
        } else {
            throw new NoExistedTransactionException();
        }
    }

    public void commit() {
        logger.info("commit阶段,删除事务语句-start-");
        Transaction transaction = getCurrentTransaction();
        transaction.changeStatus(TransactionStatus.CONFIRMING);
        transactionConfigurator.getTransactionRepository().update(transaction);
        try {
            transaction.commit();
            transactionConfigurator.getTransactionRepository().delete(transaction);
            logger.info("commit阶段,删除事务语句-end-");
        } catch (Throwable commitException) {
            logger.error("Compensable transaction confirm failed.", commitException);
            throw new ConfirmingException(commitException);
        }
    }

    public Transaction getCurrentTransaction() {
        return threadLocalTransaction.get();
    }

    public void rollback() {
        logger.info("cancel阶段,删除事务语句-start-");
        Transaction transaction = getCurrentTransaction();
        transaction.changeStatus(TransactionStatus.CANCELLING);
        transactionConfigurator.getTransactionRepository().update(transaction);
        try {
            transaction.rollback();
            transactionConfigurator.getTransactionRepository().delete(transaction);
            logger.info("cancel阶段,删除事务语句-end-");
        } catch (Throwable rollbackException) {
            logger.error("Compensable transaction rollback failed.", rollbackException);

        }
    }


}

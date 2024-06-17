package org.pankai.tcctransaction.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.pankai.tcctransaction.NoExistedTransactionException;
import org.pankai.tcctransaction.OptimistLockException;
import org.pankai.tcctransaction.Transaction;
import org.pankai.tcctransaction.api.TransactionContext;
import org.pankai.tcctransaction.api.TransactionStatus;
import org.pankai.tcctransaction.common.MethodType;
import org.pankai.tcctransaction.support.TransactionConfigurator;
import org.pankai.tcctransaction.utils.CompensableMethodUtils;
import org.pankai.tcctransaction.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 先执行
 * 对@Compensable  AOP事务增强
 * Created by pktczwd on 2016/12/14.
 */
public class CompensableTransactionInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(CompensableTransactionInterceptor.class);

    private TransactionConfigurator transactionConfigurator;

    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    /**
     * 根据方法中的TransactionContext来确定需要执行的方法.
     */
    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        //获取事务上下文 transactionContext
        TransactionContext transactionContext = CompensableMethodUtils.getTransactionContextFromArgs(pjp.getArgs());
        logger.debug("Compensable注解切面,获取事务上下文.");
        if (transactionContext != null) {
            logger.debug("Transaction ID:" + transactionContext.getXid().toString());
            logger.debug("Transaction status:" + transactionContext.getStatus());
        } else {
            //ROOT首次走到这时，transactionContext为空
            logger.debug("Transaction Context is null.");
        }
        MethodType methodType = CompensableMethodUtils.calculateMethodType(transactionContext, true);
        logger.debug("Compensable注解切面,Method Type:" + methodType);
        switch (methodType) {
            case ROOT:
                //根业务生成一条事务记录
                return rootMethodProceed(pjp);
            case PROVIDER:
                //分支业务生成一条事务记录
                return providerMethodProceed(pjp, transactionContext);
            default:
                return pjp.proceed();
        }
    }

    /**
     * 调用方自动TCC型事务.
     * 发生异常，此处调用回滚操作
     * 未发生异常，此处调用提交操作
     */
    private Object rootMethodProceed(ProceedingJoinPoint pjp) throws Throwable {
        //向数据库中插入一条事务sql记录，不管后续是回滚还是删除，只要服务没有宕机，该事务记录都会被删除
        transactionConfigurator.getTransactionManager().begin();
        Transaction transaction = transactionConfigurator.getTransactionManager().getCurrentTransaction();
        logger.debug("root-try阶段-创建事务完成,Transaction ID：{}",transaction.getXid().toString());
        Object returnValue;
        try {
            //执行真实的业务逻辑
            returnValue = pjp.proceed();
        } catch (OptimistLockException e) {
            //do not rollback, waiting for recovery job
            throw e;
        } catch (Throwable tryingException) {
            logger.error("Compensable 事务trying异常，触发回滚操作，e:", tryingException);
            //在Trying阶段发生了异常,要求rollback事务.
            //二阶段回滚 or 提交可以设计成异步操作
            transactionConfigurator.getTransactionManager().rollback();
            throw tryingException;
        }
        //在Trying阶段全部正常执行了,则执行commit操作.
        //二阶段回滚 or 提交可以设计成异步操作
        transactionConfigurator.getTransactionManager().commit();
        return returnValue;
    }

    /**
     * 服务提供方自动TCC型事务.
     */
    private Object providerMethodProceed(ProceedingJoinPoint pjp, TransactionContext transactionContext) throws Throwable {
        switch (TransactionStatus.valueOf(transactionContext.getStatus())) {
            //服务提供方收到trying请求时,开启事务,并且执行trying逻辑.
            case TRYING:
                //分支业务生成事务sql（try阶段）
                transactionConfigurator.getTransactionManager().propagationNewBegin(transactionContext);
                return pjp.proceed();
            //服务提供方收到confirming请求时,从本地事务库中读取事务,由事务中保存的信息反射调用confirming逻辑.
            case CONFIRMING:
                try {
                    //根据调用方传递来的transactionContext找出本地的分支事务,执行commit逻辑
                    transactionConfigurator.getTransactionManager().propagationExistBegin(transactionContext);
                    transactionConfigurator.getTransactionManager().commit();
                } catch (NoExistedTransactionException exception) {
                    //the transaction has been commit, ignore it.
                    logger.warn("尝试commit分支事务执行时，未找到分支事务，说明分支事务已提交，transactionContext：{}",transactionContext);
                }
                break;
            //服务提供方收到cancelling请求时,从本地事务库中读取事务,由事务中保存的信息反射调用cancelling逻辑.
            case CANCELLING:
                try {
                    transactionConfigurator.getTransactionManager().propagationExistBegin(transactionContext);
                    transactionConfigurator.getTransactionManager().rollback();
                } catch (NoExistedTransactionException exception) {
                    //the transaction has been rollback, ignore it.
                    logger.warn("尝试rollback分支事务执行时，未找到分支事务，说明分支事务已回滚，transactionContext：{}",transactionContext);
                }
        }

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        return ReflectionUtils.getNullValue(method.getReturnType());
    }
}

package org.pankai.tcctransaction.support;

import org.pankai.tcctransaction.TransactionManager;
import org.pankai.tcctransaction.TransactionRepository;
import org.pankai.tcctransaction.recover.RecoverConfig;

/**
 * 事务配置接口
 * Created by pktczwd on 2016/12/7.
 */
public interface TransactionConfigurator {

    /**
     * 获取事务管理器
     * @return
     */
    TransactionManager getTransactionManager();

    /**
     * 获取事务存储层操作类
     * @return
     */
    TransactionRepository getTransactionRepository();

    /**
     * 获取事务配置（重试次数、恢复时间等）
     * @return
     */
    RecoverConfig getRecoverConfig();
}

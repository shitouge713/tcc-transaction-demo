package org.pankai.tcctransaction.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.pankai.tcctransaction.interceptor.CompensableTransactionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by pktczwd on 2016/12/16.
 */
@Aspect
public class TccCompensableAspect implements Ordered {

    //根据Ordered的文档显示,值越大而优先级越低,所以此类应该在TccTransactionContextAspect之前
    private int order = Ordered.HIGHEST_PRECEDENCE;

    private CompensableTransactionInterceptor compensableTransactionInterceptor;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setCompensableTransactionInterceptor(CompensableTransactionInterceptor compensableTransactionInterceptor) {
        this.compensableTransactionInterceptor = compensableTransactionInterceptor;
    }

    /**
     * 执行拦截的地方
     * 1.有@Compensable注解的方法
     */
    @Pointcut("@annotation(org.pankai.tcctransaction.Compensable)")
    public void compensableService() {

    }

    @Around("compensableService()")
    public Object InterceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        return compensableTransactionInterceptor.interceptCompensableMethod(pjp);
    }


}

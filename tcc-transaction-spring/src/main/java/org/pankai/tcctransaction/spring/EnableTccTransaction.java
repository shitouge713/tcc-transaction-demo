package org.pankai.tcctransaction.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义注解，启动tcc
 * Created by pktczwd on 2016/12/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TccTransactionAutoConfiguration.class)
public @interface EnableTccTransaction {
}

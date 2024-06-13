package org.mengyun.tcctransaction.sample;

import org.pankai.tcctransaction.spring.EnableTccTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by pktczwd on 2016/12/19.
 */
@EnableTccTransaction
@EnableAspectJAutoProxy
@EnableTransactionManagement
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RedPacketServiceApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(RedPacketServiceApplication.class, args);
        }catch(Exception e){
            System.out.println(e);
        }

    }
}

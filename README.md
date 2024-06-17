## 简介
- tcc-transaction是TCC型事务java实现
- 原项目请参见[tcc-transaction](https://github.com/changmingxie/tcc-transaction)

## 改动
- 添加了对实现原理的注释.
- 事务恢复器移除了对quartz的依赖,改写为spring schedule驱动.
- 提升spring版本到4.
- 日志由log4j切换为logback.
- 将xml方式配置改写为了java code方式.
- 使用方式发生了变化.在配置类上加入@EnableTccTransaction来启用tcc-transaction.
- dubbo示例改写为restful示例.
- 略微调整了示例的逻辑.

## 示例
- 使用springboot对示例进行了改写.
- 示例使用http+json的方式进行交互.
- tcc-transaction-order为订单服务.端口号7000.(入口)
- tcc-transaction-capital为账户服务.端口号7001.
- tcc-transaction-redpacket为红包服务.端口号7002.
- 运行示例时指定redis或者mysql作为TransactionRepository
```
--spring.profiles.active=redis
```
```
--spring.profiles.active=db
```
- redis连接或mysql连接需自行改动.

## 访问请求（post）（值不要动，这些值是有数据的）
http://localhost:7000/placeorder?redPacketPayAmount=10&shopId=1&payerUserId=1000&productId=1

## 不足
该版本tcc属于简练版本，暂不支持异步操作

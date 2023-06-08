package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.service.BusinessOrderService;
import me.sibyl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author dyingleaf3213
 * @Classname TransactionalController
 * @Description TODO
 * @Create 2023/06/08 20:25
 */
@RestController
@RequestMapping("transactional")
@Slf4j
public class TransactionalController {

    @Resource
    private UserService userService;
    @Resource
    private BusinessOrderService businessOrderService;

    /**
     * 全局事务：整个方法事务
     */
    @GetMapping("/annotation")
    @Transactional
    public Response annotation() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        userService.updateTimeEqNow();
        userService.updateAccount();
        int i = 1 / 0;
        return Response.success();
    }

    /**
     * 1、TransactionDefinition.PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
     * 2、TransactionDefinition.PROPAGATION_REQUIRES_NEW：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
     * 3、TransactionDefinition.PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
     * 4、TransactionDefinition.PROPAGATION_NOT_SUPPORTED：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
     * 5、TransactionDefinition.PROPAGATION_NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。
     * 6、TransactionDefinition.PROPAGATION_MANDATORY：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
     * 7、TransactionDefinition.PROPAGATION_NESTED：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
     */

    @GetMapping("/propagation/error")
    @Transactional(propagation = Propagation.REQUIRED)
    public Response propagation() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        userService.updateTimeEqNow();

        try {
            userService.updateAccountError();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.success();
    }

    /**
     * MySQL默认：REPEATABLE_READ ；Oracle 默认为 READ_COMMITTED
     * 1、TransactionDefinition.ISOLATION_DEFAULT：这是默认值，表示使用底层数据库的默认隔离级别。对大部分数据库而言，通常这值就是TransactionDefinition.ISOLATION_READ_COMMITTED。
     * 2、TransactionDefinition.ISOLATION_READ_UNCOMMITTED：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据。该级别不能防止脏读和不可重复读，因此很少使用该隔离级别。
     * 3、TransactionDefinition.ISOLATION_READ_COMMITTED：该隔离级别表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。
     * 4、TransactionDefinition.ISOLATION_REPEATABLE_READ：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询，并且每次返回的记录都相同。即使在多次查询之间有新增的数据满足该查询，这些新增的记录也会被忽略。该级别可以防止脏读和不可重复读。
     * 5、TransactionDefinition.ISOLATION_SERIALIZABLE：所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。
     * <p>
     * <p>
     * DEFAULT：Spring 中默认的事务隔离级别，以连接的数据库的事务隔离级别为准；
     * READ_UNCOMMITTED：读未提交，也叫未提交读，该隔离级别的事务可以看到其他事务中未提交的数据。该隔离级别因为可以读取到其他事务中未提交的数据，而未提交的数据可能会发生回滚，因此我们把该级别读取到的数据称之为脏数据，把这个问题称之为脏读；
     * READ_COMMITTED：读已提交，也叫提交读，该隔离级别的事务能读取到已经提交事务的数据，因此它不会有脏读问题。但由于在事务的执行中可以读取到其他事务提交的结果，所以在不同时间的相同 SQL 查询中，可能会得到不同的结果，这种现象叫做不可重复读；
     * REPEATABLE_READ：可重复读，它能确保同一事务多次查询的结果一致。但也会有新的问题，比如此级别的事务正在执行时，另一个事务成功的插入了某条数据，但因为它每次查询的结果都是一样的，所以会导致查询不到这条数据，自己重复插入时又失败（因为唯一约束的原因）。明明在事务中查询不到这条信息，但自己就是插入不进去，这就叫幻读 （Phantom Read）；
     * SERIALIZABLE：串行化，最高的事务隔离级别，它会强制事务排序，使之不会发生冲突，从而解决了脏读、不可重复读和幻读问题，但因为执行效率低，所以真正使用的场景并不多。
     */
    @GetMapping("/isolation/error")
    @Transactional
    public Response isolation() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());

        return Response.success();
    }
    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private DataSource dataSource;//数据源

    @GetMapping("programming/dataSource")
    public void programming() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        System.err.println(platformTransactionManager);
        PlatformTransactionManager pm = null;
        TransactionStatus ts = null;
        try {
            pm = new DataSourceTransactionManager(dataSource);
            TransactionDefinition td = new DefaultTransactionDefinition();
            ts = pm.getTransaction(td);
            userService.updateTimeEqNowError();
            pm.commit(ts);

        } catch (Exception e) {
            e.printStackTrace();
            if (pm != null) {
                pm.rollback(ts);
            }
        }
    }

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @GetMapping("programming/Definition")
    public void programmingDefinition() {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED); // 设置传播行为
        defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED); // 设置隔离级别
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        try {
            userService.updateTimeEqNow();
            dataSourceTransactionManager.commit(transactionStatus); // 事务提交
        }catch (Exception e){
            e.printStackTrace();
            dataSourceTransactionManager.rollback(transactionStatus); // 事务提交
        }finally {

        }

    }

    @Resource
    private TransactionTemplate transactionTemplate;

    @GetMapping("transactionTemplate")
    public void transactionTemplate() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
//        transactionTemplate.setIsolationLevel();
//        transactionTemplate.setPropagationBehavior();
//        transactionTemplate.setReadOnly();
//        transactionTemplate.setTimeout();
        transactionTemplate.execute(status -> {
            System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
            System.err.println(status);
            int result = 0;
            try {
                userService.updateTimeEqNowError();
            } catch (Exception e) {
                System.err.println("rollback");
                e.printStackTrace();
                status.setRollbackOnly();
            }
            return result;
        });

    }
}

package me.sibyl.controller;

import me.sibyl.dao.MysqlMapper;
import me.sibyl.entity.UserAccount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dyingleaf3213
 * @Classname MysqlController
 * @Description TODO
 * @Create 2023/06/17 21:55
 */
@RestController
@RequestMapping("/mysql")
public class MysqlController {

    @Resource
    private MysqlMapper mysqlMapper;


    /**
     *   乐观锁 适用场景
     *   1.低冲突
     *   2.读多写少
     *   3.短事务
     *   4.分布式
     *   5.互联网应用
     *
     *   悲观锁 ...for update ... for share mode
     *
     *   select * from user_account where id='3213' for update ;
     *   select * from user_account where id='3213' lock in share mode ;
     *
     *   1.写操作多
     *   2.并发冲突高
     *   3.业务需要强一致
     */
    @GetMapping("/")
    public void test01() {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());
        UserAccount account = mysqlMapper.queryUserAccount();
        System.err.println(account);
    }
}

package me.sibyl.dao;

import me.sibyl.entity.UserAccount;
import org.springframework.stereotype.Repository;

/**
 * @author dyingleaf3213
 * @Classname MysqlMapper
 * @Description TODO
 * @Create 2023/06/17 21:55
 */
@Repository
public interface MysqlMapper {
    UserAccount queryUserAccount();
}

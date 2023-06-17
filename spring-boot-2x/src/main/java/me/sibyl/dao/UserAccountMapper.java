package me.sibyl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.sibyl.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    int consume(@Param("uid") String uid, @Param("amount") BigDecimal amount);
    UserAccount queryByIdWithForUpdate(String id);
}

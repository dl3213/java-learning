package me.sibyl.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import me.sibyl.entity.UserAccount;
import me.sibyl.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/01/22 22:48
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public List<User> listAll() {
        return this.getBaseMapper().selectList(null);
    }

    @Override
    @DS("slave_1")
    public User detail(String id) {
        User user = this.getBaseMapper().queryById(Long.valueOf(id));
        return user;
    }

    @Override
    public boolean updateTimeEqNow() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        User user = this.getById(3213L);
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional
    public boolean updateTimeEqNowError() {
        System.err.println(" inner = " + TransactionSynchronizationManager.getCurrentTransactionName());
        User user = this.getById(3213L);
        user.setUpdateTime(LocalDateTime.now());
        boolean update = this.updateById(user);
        int i = 1 / 0;
        return update;
    }

    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public boolean updateAccount() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        UserAccount account = userAccountMapper.selectOne(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, 3213L)
                        .eq(UserAccount::getState, "01")
        );
        BigDecimal balance = (account.getBalance());
        account.setBalance(balance.subtract(BigDecimal.ONE));
        account.setUpdateTime(LocalDateTime.now());
        return userAccountMapper.updateById(account) == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean updateAccountError() {
        System.err.println(TransactionSynchronizationManager.getCurrentTransactionName());
        UserAccount account = userAccountMapper.selectOne(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, 3213L)
                        .eq(UserAccount::getState, "01")
        );
        BigDecimal balance = (account.getBalance());
        account.setBalance(balance.subtract(BigDecimal.ONE));
        account.setUpdateTime(LocalDateTime.now());
        boolean update = userAccountMapper.updateById(account) == 1;
        int i = 1 / 0;
        return update;
    }

    @Override
    @DS("mater")
    public void updateUser(User detail) {
        this.getBaseMapper().updateById(detail);
    }
}

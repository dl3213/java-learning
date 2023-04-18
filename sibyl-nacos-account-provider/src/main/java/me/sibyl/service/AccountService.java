package me.sibyl.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname AccountService
 * @Description TODO
 * @Create 2023/03/26 03:07
 */
@Service
@Slf4j
public class AccountService {

    @Resource
    private UserAccountMapper userAccountMapper;

    public UserAccount queryByUserId(Long userId) {
        List<UserAccount> userAccounts = userAccountMapper.selectList(
                Wrappers.lambdaQuery(UserAccount.class)
                        .eq(UserAccount::getUserId, userId)
        );

        return CollectionUtils.isEmpty(userAccounts) ? null : userAccounts.get(0);
    }

}

package me.sibyl.cas.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.sibyl.base.entity.User;
import me.sibyl.cas.domain.LoginUser;
import me.sibyl.cas.mapper.MenuMapper;
import me.sibyl.cas.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname UserDetailService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 10:37
 */
@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            //
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername,username);
            User user = userMapper.selectOne(queryWrapper);

            Assert.notNull(user,"查询用户不存在");

            //System.err.println(user);

            //权限
            //List<String> arr = new ArrayList<>(Arrays.asList("system:dept:list","system:test:list"));
            List<String> strings = menuMapper.selectKeysByUserId(user.getId());

            return new LoginUser(user,strings);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}

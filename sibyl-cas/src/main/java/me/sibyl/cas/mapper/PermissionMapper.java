package me.sibyl.cas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.sibyl.base.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectKeysByUserId(String uid);
}

package me.sibyl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
@Mapper
public interface BusinessOrderMapper extends BaseMapper<BusinessOrder> {

    User queryById(String id);
}

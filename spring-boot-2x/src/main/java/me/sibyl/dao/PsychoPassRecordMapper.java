package me.sibyl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.entity.User;
import me.sibyl.vo.PsychoPassQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname PsychoPassRecordMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
@Mapper
public interface PsychoPassRecordMapper extends BaseMapper<PsychoPassRecord> {
    List<PsychoPassRecord> query(PsychoPassQueryRequest queryRequest);
}

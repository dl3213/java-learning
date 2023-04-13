package me.sibyl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.sibyl.dao.PsychoPassRecordMapper;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.service.PsychoPassService;
import me.sibyl.vo.PsychoPassQueryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname PsychoPassServiceImpl
 * @Description TODO
 * @Create 2023/04/05 01:31
 */
@Service
public class PsychoPassServiceImpl extends ServiceImpl<PsychoPassRecordMapper, PsychoPassRecord> implements PsychoPassService {
    @Override
    public List<PsychoPassRecord> list(String userId) {
        return this.getBaseMapper().selectList(
                Wrappers.lambdaQuery(new PsychoPassRecord())
                        .eq(PsychoPassRecord::getUid, userId)
        );
    }

    @Override
    public Object queryPage(PsychoPassQueryRequest queryRequest) {
//        PageHelper.startPage(queryRequest.getPageNum(), queryRequest.getPageSize());
        List<PsychoPassRecord> query = this.getBaseMapper().query(queryRequest);
        System.err.println("query.size() = " + query.size());
        PageInfo<PsychoPassRecord> pageInfo = new PageInfo<>(query);
        return pageInfo;
    }

    @Override
    public Object queryList(PsychoPassQueryRequest queryRequest) {
        queryRequest.setPageNum(null);
        queryRequest.setPageSize(null);
        List<PsychoPassRecord> query = this.getBaseMapper().query(queryRequest);
        System.err.println("query.size() = " + query.size());
        return query;
    }
}

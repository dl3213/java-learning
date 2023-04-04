package me.sibyl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.sibyl.dao.BusinessOrderMapper;
import me.sibyl.dao.PsychoPassRecordMapper;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.service.BusinessOrderService;
import me.sibyl.service.PsychoPassService;
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
}

package me.sibyl.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.BusinessOrderMapper;
import me.sibyl.entity.BusinessOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname OrderService
 * @Description TODO
 * @Create 2023/03/26 03:19
 */
@Service
@Slf4j
public class OrderService {

    @Resource
    private BusinessOrderMapper businessOrderMapper;

    public List<BusinessOrder> queryList(String userId) {
        return businessOrderMapper.selectList(
                Wrappers.lambdaQuery(BusinessOrder.class)
                        .eq(BusinessOrder::getLinkId, userId)
        );
    }
}

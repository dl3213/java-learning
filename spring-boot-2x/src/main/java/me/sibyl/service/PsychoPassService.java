package me.sibyl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.vo.PsychoPassQueryRequest;

import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname PsychoPassService
 * @Description TODO
 * @Create 2023/04/05 01:30
 */

public interface PsychoPassService extends IService<PsychoPassRecord> {
    List<PsychoPassRecord> list(String userId);

    Object queryPage(PsychoPassQueryRequest psychoPassQueryRequest);

    Object queryList(PsychoPassQueryRequest psychoPassQueryRequest);

    Page wrapperPage(PsychoPassQueryRequest psychoPassQueryRequest);

}

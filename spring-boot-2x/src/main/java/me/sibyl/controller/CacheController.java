package me.sibyl.controller;

import me.sibyl.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 缓存击穿(刚好过期刚好没有缓存就高频访问)：1.加锁排队2.根据访问量加长过期时间或不过期
 * 缓存穿透(查询不存在的数据)：1.校验参数2.布隆过滤器
 * 缓存雪崩(大量缓存突然过期不可用)：1.缓存不过期2.随机过期时间3.定时延长缓存时间4.redis高可用
 *
 */
@RestController
@RequestMapping("/cache/")
public class CacheController {

    @Resource
    private UserService userService;


}

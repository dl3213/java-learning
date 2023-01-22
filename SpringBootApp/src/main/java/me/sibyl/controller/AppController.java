package me.sibyl.controller;

import lombok.SneakyThrows;
import me.sibyl.entity.User;
import me.sibyl.service.AppService;
import me.sibyl.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
public class AppController {
//
//    @Resource
//    private UserMapper userMapper;
    @Resource
    private AppService appService;
    @Resource
    private AsyncService asyncService;

    @SneakyThrows
    @PostMapping("/user/put")
    public void put(User user){
        // 目的 == 需求 == 出发点
        //System.err.println(Thread.currentThread().getName());
        //System.err.println(user.getId());
        System.err.println(user.getId());
        appService.save(user);//保存数据库
        System.err.println(user.getId());

        Thread.sleep(10000);
        //System.err.println(" end ");
        //System.out.printf("test");
    }

    @GetMapping("/hello")
    public String hello(){
        // 目的 == 需求 == 出发点
        return "hello";
    }




//    @GetMapping("/test")
//    public ResponseVO test(){
////        throw new RuntimeException();
//        return new ResponseVO(200, "test",userMapper.selectList(null));
//    }
}


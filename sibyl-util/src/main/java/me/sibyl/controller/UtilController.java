package me.sibyl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UtilController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/16 20:58
 */
@RestController
@RequestMapping("/api/v1")
public class UtilController {

    @GetMapping("/test")
    public void test(){

    }
}

package me.sibyl.service;

import cn.hutool.core.util.ServiceLoaderUtil;

import java.util.ServiceLoader;

/**
 * @author dyingleaf3213
 * @Classname Test
 * @Description TODO
 * @Create 2023/04/19 22:23
 */

public class SPITest {

    public static void main(String[] args) {
        ServiceLoader<TestService> testServices = ServiceLoader.load(TestService.class);
        System.err.println(testServices);
        for (TestService service : testServices) {
            System.err.println(service.getClass());
        }
    }
}

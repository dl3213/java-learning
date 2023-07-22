package org.example;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class IotServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IotServer.class)
                //  WebApplicationType.REACTIVE + spring-boot-starter-webflux
                //  WebApplicationType.SERVLET + spring-boot-starter-web
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
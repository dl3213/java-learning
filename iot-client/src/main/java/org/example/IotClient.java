package org.example;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class IotClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IotClient.class)
                //  WebApplicationType.REACTIVE + spring-boot-starter-webflux
                //  WebApplicationType.SERVLET + spring-boot-starter-web
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
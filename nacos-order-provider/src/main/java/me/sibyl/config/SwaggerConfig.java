package me.sibyl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket buildDocker() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(builder())
                .select()
                .apis(RequestHandlerSelectors.basePackage("me.sibyl.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo builder() {
        Contact contact = new Contact("开发者", "1", "2");
        return new ApiInfoBuilder()
                .title("订单")
                .description("")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}

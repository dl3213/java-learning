package code.sibyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * 启动类
     * @param args
     */
    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebApplicationType(WebApplicationType.REACTIVE);
        application.run(args);
    }

}

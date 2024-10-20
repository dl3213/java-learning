package code.sibyl.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.MalformedURLException;

@Configuration
public class CommonConfig {

    @Bean
    public RouterFunction<ServerResponse> staticResources() throws MalformedURLException {
        return RouterFunctions.resources("/pixiv/**", new FileSystemResource("E:/4me/pixez/"))
                .and(RouterFunctions.resources("/video/**", new FileSystemResource("E:/4me/video/")))
                .and(RouterFunctions.resources("/r-18/**", new FileSystemResource("E:/4me/r-18/")))
//                .and(RouterFunctions.resources("/images/**", new FileSystemResource("file:/path/to/images/")))
//                .and(RouterFunctions.resources("/other/**", new FileSystemResource("file:/another/path/to/resources/")))
                ;
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}

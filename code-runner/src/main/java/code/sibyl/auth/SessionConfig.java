package code.sibyl.auth;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class SessionConfig extends WebSessionServerSecurityContextRepository {

    // 所有security生成的session的name都是SPRING_SECURITY_CONTEXT
    public static final String DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME = "SPRING_SECURITY_CONTEXT";

    private String springSecurityContextAttrName = DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

    @Override
    public void setSpringSecurityContextAttrName(String springSecurityContextAttrName) {
        super.setSpringSecurityContextAttrName(springSecurityContextAttrName);
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return exchange.getSession()
                .doOnNext(session -> {
//                    System.err.println("WebSessionServerSecurityContextRepository");
//                    System.err.println(session.getAttributes());
//                    System.err.println(session.getId());
                    if (context == null) {
                        session.getAttributes().remove(this.springSecurityContextAttrName);
                    } else {
                        session.getAttributes().put(this.springSecurityContextAttrName, context);
                        // 在这里设置过期时间 单位使用Duration类中的定义  有秒、分、天等
                        session.setMaxIdleTime(Duration.ofSeconds(24 * 60 * 60));
                    }
                })
                .flatMap(session -> session.changeSessionId());
    }

    // 通过该方法可以获取到所有的session
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return super.load(exchange);
    }
}

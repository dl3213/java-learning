//package code.sibyl.auth.v1.sys;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.core.log.LogMessage;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.web.server.DefaultServerRedirectStrategy;
//import org.springframework.util.Assert;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//
//@Slf4j
//public class SysServerRedirectStrategy extends DefaultServerRedirectStrategy {
//
//    private HttpStatus httpStatus;
//    private boolean contextRelative;
//    private String msg;
//
//    public SysServerRedirectStrategy(String msg) {
//        this.httpStatus = HttpStatus.FOUND;
//        this.contextRelative = true;
//        this.msg = msg;
//    }
//
//    @Override
//    public Mono<Void> sendRedirect(ServerWebExchange exchange, URI location) {
//        Assert.notNull(exchange, "exchange cannot be null");
//        Assert.notNull(location, "location cannot be null");
//        return exchange.getSession().flatMap(webSession -> Mono.fromRunnable(() -> {
//            webSession.getAttributes().put("redirect-msg", msg);
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(this.httpStatus);
//            URI newLocation = this.createLocation(exchange, location);
//            response.getHeaders().setLocation(newLocation);
//        }));
//    }
//
//    private URI createLocation(ServerWebExchange exchange, URI location) {
//        if (!this.contextRelative) {
//            return location;
//        } else {
//            String url = location.toASCIIString();
//            if (url.startsWith("/")) {
//                String context = exchange.getRequest().getPath().contextPath().value();
//                return URI.create(context + url);
//            } else {
//                return location;
//            }
//        }
//    }
//}

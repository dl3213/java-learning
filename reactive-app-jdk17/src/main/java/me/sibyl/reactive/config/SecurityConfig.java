//package me.sibyl.reactive.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authorization.AuthorizationDecision;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
//import reactor.core.publisher.Mono;
//
///**
// * @author dyingleaf3213
// * @Classname SecurityConfig
// * @Description TODO
// * @Create 2023/04/02 20:37
// */
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public ReactiveUserDetailsService userDetailsService() {
//        var admin = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("admin").build();
//        var guest = User.withDefaultPasswordEncoder().username("guest").password("guest").roles("guest").build();
//        return new MapReactiveUserDetailsService(admin, guest);
//    }
//
////    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
//        return httpSecurity
//                .authorizeExchange()
//                .pathMatchers(HttpMethod.PUT, "/api/v1/router/**")
//                .hasRole("admin")
//                .matchers(
//                        ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/v1/router/**"),
//                        ServerWebExchangeMatchers.pathMatchers(HttpMethod.DELETE, "/api/v1/router/**")
//
//                ).hasRole("admin")
//                .pathMatchers("/api/v1/user/**")
//                // 编程式
//                .access((authMono, context) ->
//                    authMono.map(theAuth -> {
//                        HttpMethod method = context.getExchange().getRequest().getMethod();
//                        var granted = false;
//                        if(method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE){
//                            granted = theAuth.getAuthorities().stream()
//                                    .map(GrantedAuthority::getAuthority)
//                                    .anyMatch("ROLE_admin"::equals);
//                        }else {
//                            granted = theAuth.isAuthenticated();
//                        }
//                        return new AuthorizationDecision(granted);
//                    }).switchIfEmpty(Mono.just(new AuthorizationDecision(false)))
//                ).anyExchange().authenticated()
//                .and().httpBasic()
//                .and().csrf().disable().build();
//    }
//}

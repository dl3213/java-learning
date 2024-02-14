package code.sibyl.auth;

import code.sibyl.auth.rest.*;
import code.sibyl.auth.sys.SysAuthEntryPoint;
import code.sibyl.auth.sys.SysAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

/**
 * spring security webflux版配置
 */

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class SecurityConfig {


    /**
     * 前后端分离
     *
     * @param http
     * @return
     */
    @Order(0)
    @Bean
    public SecurityWebFilterChain restSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/rest/**"))
                .authorizeExchange((authorize) ->
                        authorize
                                //.pathMatchers("/login","/logout").permitAll()
                                .anyExchange().authenticated());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(e -> e.disable());
        http.headers(header -> header.frameOptions(f->f.disable()));
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .accessDeniedHandler(new RestAccessDeniedHandler()) // 权限处理
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint()) // 未认证的处理,返回json
        );
        http
                .formLogin(form-> form
                                .loginPage("/login-view")
                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/rest/login"))// 注意请求方法及Content-Type,Content-Type默认x-www-form-urlencoded
//                        .authenticationEntryPoint(new RestServerAuthenticationEntryPoint())// todo 限制到 post + json
//                        .authenticationManager(new RestReactiveAuthenticationManager())
                        .authenticationSuccessHandler(new RestAuthSuccessHandler())
                        .authenticationFailureHandler(new RestAuthFailureHandler())
                );
        http.logout(logout->logout.logoutUrl("/api/rest/logout"));
        // @formatter:on
        return http.build();
    }

    /**
     * 前后端不分离
     *
     * @param http
     * @return
     */
    @Order(1)
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .authorizeExchange((authorize) -> authorize
                                .pathMatchers("/admin/**").hasAnyAuthority("admin:api")
                                .pathMatchers("/user/**").hasAnyAuthority("user:api")
                        .pathMatchers(
//                                "/login",
                                "/login-view",
                                "/css/**",
                                "/js/**",
                                "/font/**",
                                "/img/**"
                        ).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated()
                );
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(e -> e.disable());
        http.headers(header -> header.frameOptions(f->f.disable()));
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(new SysAuthEntryPoint("/login-view")) // 未认证的处理,跳转到登录页面
                        .accessDeniedHandler(new RestAccessDeniedHandler()) //权限失败 处理
        );
        http
                .formLogin((form)-> form
                        .loginPage("/login-view")
                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/login"))
//                                .authenticationManager(new SysAuthenticationManager()) //  todo
//                        .authenticationSuccessHandler(new RestAuthSuccessHandler()) // 前后端不分离需要跳转到首页
//                        .authenticationFailureHandler(new RestAuthFailureHandler()) // 前后端不分离需要跳转到登录页并加提示 todo
                );
//        http.addFilterAt(new LoginFilter, ); // 自定义登录操作 ??? 邮箱手机登录可以在ReactiveUserDetailsService中完成
        http.logout(logout->logout.logoutUrl("/logout"));
        //http.securityContextRepository(customServerSecurityContextRepository());

        // @formatter:on
        return http.build();
    }

    /**
     * 临时
     *
     * @return
     */
//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        // @formatter:off
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .passwordEncoder(passwordEncoder()::encode)
//                .authorities("admin:api","user:api")
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .passwordEncoder(passwordEncoder()::encode)
//                .authorities("user:api")// 角色-菜单  权限-接口
//                .build();
//        // @formatter:on
//        return new MapReactiveUserDetailsService(admin, user);
//    }

    /**
     *  todo
     * @return
     */
//    @Bean
//    public ServerSecurityContextRepository customServerSecurityContextRepository() {
//        return NoOpServerSecurityContextRepository.getInstance();
//    }

    /**
     * 密码加密解密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        NoPasswordEncoder encoder = new NoPasswordEncoder();
        return encoder;
    }
}

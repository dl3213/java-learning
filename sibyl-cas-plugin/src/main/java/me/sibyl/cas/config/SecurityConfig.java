package me.sibyl.cas.config;

import lombok.RequiredArgsConstructor;
import me.sibyl.cas.annotation.AnonymousAuth;
import me.sibyl.cas.filter.SibylTokenAuthenticationFilter;
import me.sibyl.cas.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname SecurityConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:03
 */
@EnableWebSecurity
// 开启注解权限资源控制
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 自定义认证处理器 (重点：额外添加的类，用于获取用户信息，保存用户信息。并且处理登录等操作)
     */
    //private final AdminAuthenticationProvider adminAuthenticationProvider;

    /**
     * 登录成功处理器
     */
    private final AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    private final AdminAuthenticationFailureHandler adminAuthenticationFailureHandler;

    /**
     * 未登录情况下的处理类 (额外添加的类 这个类在下方有说明)
     */
    private final AdminAuthenticationEntryPoint adminAuthenticationEntryPoint;

    /**
     * Token 处理器 (额外添加的类 这个类在下方有说明)
     */
    private final SibylTokenAuthenticationFilter sibylTokenAuthenticationFilter;

    /**
     * 退出登录处理类 (我在这里处理了一下缓存的token退出的时候清除掉了，这个类下方也有说明)
     */
    //private final AdminLogoutHandler adminLogoutHandler;

    /**
     * 退出成功处理
     */
    private final AdminLogoutSuccessHandler adminLogoutSuccessHandler;

    // 上面是登录认证相关  下面为url权限相关 - ========================================================================================

    /**
     * 登陆过后无权访问返回处理类 (返回无权的时候的返回信息处理)
     */
    private final MyAccessDeniedHandler myAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }

    @Value("#{'${url.anonymous:}'.trim().split(',')}")
    private String[] anonymousList;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();

        // 标记只能在 服务器本地ip[127.0.0.1或者localhost] 访问`/home`接口，其他ip地址无法访问
        // registry.antMatchers("/").hasIpAddress("127.0.0.1");

        // 允许匿名的url - 可理解为放行接口 - 多个接口使用,分割
//        System.err.println(new Object(){}.getClass().getEnclosingMethod().getName());
//        System.err.println(Arrays.stream(anonymousList).collect(Collectors.toList()));
        registry
                .antMatchers(anonymousList)
//                .permitAll();
                .anonymous();
        registry.antMatchers("/user/login", "/druid/login.html")
                .anonymous();
        // 注解支持
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethods.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entry-> Objects.nonNull(entry.getValue().getMethodAnnotation(AnonymousAuth.class)))
                .map(Map.Entry::getKey)
                .map(RequestMappingInfo::getPatternsCondition)
                .flatMap(condition -> condition.getPatterns().stream())
                .forEach(pattern -> registry.antMatchers(pattern).anonymous());

        // OPTIONS(选项):查找适用于一个特定网址资源的通讯选择。 在不需执行具体的涉及数据传输的动作情况下， 允许客户端来确定与资源相关的选项以及 / 或者要求， 或是一个服务器的性能
        // registry.antMatchers(HttpMethod.OPTIONS, Constants.CONTEXT_PATH+"/**").denyAll();

        // 其余所有请求都需要认证
        registry.anyRequest().authenticated();

        // 禁用CSRF 开启跨域
        http.csrf().disable();

        // 开启CSRF 向前端发送 XSRF-TOKEN Cookie（上面设置了关闭，可以通过下述方式进行开启，但是swagger-ui的跨域403异常暂时找不到解决办法。无奈放弃了）
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        //配置HTTP基本身份认证
        http.httpBasic();

        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(adminAuthenticationEntryPoint);

        // 登陆过后无权访问返回
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        // 登录处理 - 前后端一体的情况下
        http.formLogin().loginProcessingUrl("/user/login")
                // 默认的登录成功返回url
                // .defaultSuccessUrl("/")
                // 登录成功处理
                .successHandler(adminAuthenticationSuccessHandler)
                .failureHandler(adminAuthenticationFailureHandler)
                // 自定义登陆用户名和密码属性名，默认为 username和password
                .usernameParameter("username")
                .passwordParameter("password")
                // 异常处理
                // .failureUrl(Constants.CONTEXT_PATH+"/login/error").permitAll()
                // 退出登录
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .permitAll()
                //.addLogoutHandler(adminLogoutHandler)
                .logoutSuccessHandler(adminLogoutSuccessHandler);

        // session创建规则  STATELESS 不使用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 防止iframe 造成跨域
        http.headers().frameOptions().disable();

        // 添加前置的过滤器 用于验证token
        http.addFilterBefore(sibylTokenAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        // ignoring 允许添加 RequestMatcher Spring Security 应该忽略的实例。

        return (web) -> {
            web.ignoring().antMatchers(HttpMethod.GET,
                    "/favicon.ico",
                    "/*.html",
                    "/**/*.css",
                    "/**/*.js");
            web.ignoring().antMatchers(HttpMethod.GET, "/swagger-resources/**");
            web.ignoring().antMatchers(HttpMethod.GET, "/webjars/**");
            web.ignoring().antMatchers(HttpMethod.GET, "/v2/api-docs");
            web.ignoring().antMatchers(HttpMethod.GET, "/v2/api-docs-ext");
            web.ignoring().antMatchers(HttpMethod.GET, "/configuration/ui");
            web.ignoring().antMatchers(HttpMethod.GET, "/configuration/security");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

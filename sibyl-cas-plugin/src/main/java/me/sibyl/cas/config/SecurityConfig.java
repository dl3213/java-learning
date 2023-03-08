package me.sibyl.cas.config;

import me.sibyl.cas.filter.MyTokenAuthenticationFilter;
import me.sibyl.cas.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Classname SecurityConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:03
 */
@EnableWebSecurity
// 开启注解权限资源控制
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
    private final MyTokenAuthenticationFilter myTokenAuthenticationFilter;

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

    public SecurityConfig(
            //AdminAuthenticationProvider adminAuthenticationProvider,
            AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler,
            AdminAuthenticationFailureHandler adminAuthenticationFailureHandler,
            AdminAuthenticationEntryPoint adminAuthenticationEntryPoint,
            MyTokenAuthenticationFilter myTokenAuthenticationFilter,
            AdminLogoutSuccessHandler adminLogoutSuccessHandler,
            MyAccessDeniedHandler myAccessDeniedHandler) {
        this.myTokenAuthenticationFilter = myTokenAuthenticationFilter;
        //this.adminAuthenticationProvider = adminAuthenticationProvider;
        this.adminAuthenticationSuccessHandler = adminAuthenticationSuccessHandler;
        this.adminAuthenticationFailureHandler = adminAuthenticationFailureHandler;
        this.adminAuthenticationEntryPoint = adminAuthenticationEntryPoint;
        //this.adminLogoutHandler = adminLogoutHandler;
        this.adminLogoutSuccessHandler = adminLogoutSuccessHandler;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MyPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();

        // 标记只能在 服务器本地ip[127.0.0.1或者localhost] 访问`/home`接口，其他ip地址无法访问
        // registry.antMatchers("/").hasIpAddress("127.0.0.1");

        // 允许匿名的url - 可理解为放行接口 - 多个接口使用,分割
        registry.antMatchers("/home").permitAll();
//        registry.antMatchers("/user/login", "/druid/login.html")
//                .anonymous();

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
        http.addFilterBefore(myTokenAuthenticationFilter, BasicAuthenticationFilter.class);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 自定义验证管理器
//        //auth.authenticationProvider(adminAuthenticationProvider);
//        // super.configure(auth);
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // ignoring 允许添加 RequestMatcher Spring Security 应该忽略的实例。
        web.ignoring().antMatchers(HttpMethod.GET,
                "/favicon.ico",
                "/*.html",
                "/**/*.css",
                "/**/*.js");
        web.ignoring().antMatchers(HttpMethod.GET,"/swagger-resources/**");
        web.ignoring().antMatchers(HttpMethod.GET,"/webjars/**");
        web.ignoring().antMatchers(HttpMethod.GET,"/v2/api-docs");
        web.ignoring().antMatchers(HttpMethod.GET,"/v2/api-docs-ext");
        web.ignoring().antMatchers(HttpMethod.GET,"/configuration/ui");
        web.ignoring().antMatchers(HttpMethod.GET,"/configuration/security");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

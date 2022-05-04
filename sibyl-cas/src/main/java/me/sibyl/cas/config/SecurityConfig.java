package me.sibyl.cas.config;

import me.sibyl.cas.filter.JwtAuthTokenFilter;
import me.sibyl.cas.handler.AccessDeniedHandlerImpl;
import me.sibyl.cas.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Classname SecurityConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:03
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MyPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/user/login","/css/**","/js/**","/index.html","/img/**","/fonts/**","/favicon.ico","/register/**","/register/");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //无效？？？

//        http
//                .csrf().disable()
//                //因为使用JWT，所以不需要HttpSession
//                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                //OPTIONS请求全部放行
//                .antMatchers( HttpMethod.OPTIONS, "/**").permitAll()
//                //登录接口放行
//                .antMatchers("/user/login").permitAll()
//                //其他接口全部接受验证
//                .anyRequest().authenticated();

        //接口放行
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/login").anonymous()
                //.antMatchers("/hello").hasAuthority("system:dept:list")
                .anyRequest().authenticated();
//
//        http.authorizeRequests()
//                .antMatchers("/user/login").permitAll()
//                .anyRequest().authenticated();

        //过滤器
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //http.headers().cacheControl();

        //异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

package me.sibyl.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //设置X-Frame-Options响应头为SAMEORIGIN
        http.headers().frameOptions().sameOrigin();
        //放行不用权限的资源（去登录页面当然不需要用权限，否则你都看不到登录界面，还怎么登录，所以去登录界面必须放行）
        http.authorizeRequests().antMatchers("/toLogin","/css/**","/js/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll();
        http.authorizeRequests().antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**","/doc.html").permitAll();
        //拦截需要权限的资源（拦截所有请求，要想访问，登录的账号必须拥有USER和ADMIN的角色才行）
        http.authorizeRequests().antMatchers("/**").hasAnyRole("USER", "ADMIN").anyRequest().authenticated();
        //设置自定义登录界面
        http.formLogin()//启用表单登录
                .loginPage("/login")//登录页面地址，只要你还没登录，默认就会来到这里
                .loginProcessingUrl("/loginProcess")//登录处理程序，Spring Security内置控制器方法
                .usernameParameter("username")//登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password")//登录表单form中密码框输入框input的name名，不修改的话默认是password
                .defaultSuccessUrl("/main")//登录认证成功后默认转跳的路径
                //.successForwardUrl("/main")//登录成功跳转地址，使用的是请求转发
                .failureForwardUrl("/error")//登录失败跳转地址，使用的是请求转发
                .permitAll();
        //这里默认为security登录页面，在后续需要关闭时进行
        // 添加 JWT 过滤器，JWT 过滤器在用户名密码认证过滤器之前
        http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.exceptionHandling();

        //使配置生效
        return http.build();
    }

    @Bean
    public JwtAuthenticationTokenFilter authFilter() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2", "/websocket/**");
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}

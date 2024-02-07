package code.sibyl.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers(
                                "/login",
                                "/login-view",
                                "/css/**",
                                "/js/**",
                                "/font/**",
                                "/img/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                );
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(e -> e.disable());
        http.headers(header -> header.frameOptions(f->f.disable()));
        http
                .formLogin((form)-> form
                        .loginPage("/login-view")
                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/login"))
                );

        // @formatter:on
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        // @formatter:off
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("admin")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("user")
                .build();
        // @formatter:on
        return new MapReactiveUserDetailsService(admin, user);
    }

}

package code.sibyl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class UserUtil {

    public UserDetails getCurrentMsg(@AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication){
        return userDetails;
    }

    /**
     * todo
     */
    public static Mono<UserDetails> getCurrentUser() throws ExecutionException, InterruptedException {
        System.err.println(" test => " + ReactiveSecurityContextHolder.getContext().toFuture().get());
        return ReactiveSecurityContextHolder.getContext()
                //.switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(UserDetails.class);
    }

}

//package code.sibyl.auth.v1.sys;
//
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import reactor.core.publisher.Mono;
//
///**
// *  todo
// */
//public class SysAuthenticationManager implements ReactiveAuthenticationManager {
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        System.err.println("ReactiveAuthenticationManager");
//        System.err.println(authentication.isAuthenticated());
//        UserDetails details = (UserDetails) authentication.getDetails();
//        System.err.println(details.getUsername());
//        System.err.println(details.getPassword());
//        return null;
//    }
//}

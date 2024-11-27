//package code.sibyl.model;
//
//
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@Data
//public class LoginUser implements UserDetails {
//
//    private Long id;
//    private String username;
//    private String nickname;
//    private String password;
//    private String phoneNumber;
//
//    private List<GrantedAuthority> authorityList;
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorityList;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}

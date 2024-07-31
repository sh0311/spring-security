package jwt.example.jwt_tutorial.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
//로그인 진행이 완료되면 시큐리티 session을 만들어준다 (Security ContextHolder라는 키 값에 세션 정보를 저장)
// 시큐리티 session에 들어갈 수 있는 오브젝트 타입 : Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 함
// User 오브젝트 타입 : UserDetails 타입 객체

// Security Session => Authentication => UserDetails(Principal Details)
// 유저 정보 얻고 싶다면 위의 세 과정을 거쳐서 얻어야 함

import jwt.example.jwt_tutorial.model.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private User user;
    public PrincipalDetails(User user) {
        this.user=user;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect=new ArrayList<>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

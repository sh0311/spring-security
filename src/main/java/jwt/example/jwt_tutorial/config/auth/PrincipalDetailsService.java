package jwt.example.jwt_tutorial.config.auth;


import jwt.example.jwt_tutorial.model.User;
import jwt.example.jwt_tutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 시큐리티 설정에서 loginProcessUrl("/login")으로 걸어놓음
// 따라서 /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadByUsername 함수가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session=Authentication=UserDetails
    // session(Authentication(UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity=userRepository.findByUsername(username);
        if(userEntity!=null) {
            return new PrincipalDetails(userEntity); // Authentication(UserDetails) 꼴
        }
        return null;
    }
}

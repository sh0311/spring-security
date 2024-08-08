package com.jwt.jwt.config.auth;

import com.jwt.jwt.model.User;
import com.jwt.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// http://localhost:8080/login 요청이 올 때 동작한다
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity=userRepository.findByUsername(username);
        System.out.println("userEntity="+userEntity);
        return new PrincipalDetails(userEntity);
    }
}

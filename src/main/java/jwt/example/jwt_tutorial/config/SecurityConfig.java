package jwt.example.jwt_tutorial.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)  //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

    // @Bean으로 등록해주면 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다 -> 아무데서나 쓸 수 있게됨
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth)->auth.disable()   // CSRF 비활성화
                )
                .authorizeHttpRequests((auth) -> auth   // 요청에 대한 권한 부여
                        .requestMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소
                        .requestMatchers("/manager/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER") //ROLE_ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()   //위의 세가지 경로 이외는 아무권한 없어도 접속 가능
                )
                .formLogin(login -> login  // 로그인 구성
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login")   // /login 주소가 호출되면 시크리티가 낚아채서 대신 로그인을 진행해준다
                        .defaultSuccessUrl("/"));  


        return http.build();
    }
}

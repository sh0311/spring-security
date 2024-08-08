package com.jwt.jwt.config;

import com.jwt.jwt.config.jwt.JwtAuthenticationFilter;
import com.jwt.jwt.filter.MyFilter1;
import com.jwt.jwt.filter.MyFilter2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration //설정파일. 빈으로 등록
@EnableWebSecurity
public class SecurityConfig {

    private final CorsFilter corsFilter; //빈으로 등록되어 있어서

    public SecurityConfig(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {


        http
                //.addFilterBefore(new MyFilter2(), SecurityContextPersistenceFilter.class)
                .csrf((auth)->auth.disable())
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Jwt를 사용하기 때문에 session을 stateless로 설정
                .addFilter(corsFilter)  //모든 요청은 이 필터를 거친다
                .formLogin((auth)->auth.disable())  //form 로그인 방식 disable
                .httpBasic((auth)->auth.disable()) //http basic 인증 방식 disable
                .addFilter(new JwtAuthenticationFilter(authenticationManager))  //AuthenticationManager 파라미터를 던져줘야 한다

                .authorizeRequests((auth)->auth
                        .requestMatchers("/api/v1/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .requestMatchers("/api/v1/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .requestMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll());
        return http.build();
    }

}

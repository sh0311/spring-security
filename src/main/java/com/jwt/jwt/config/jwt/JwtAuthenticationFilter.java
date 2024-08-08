package com.jwt.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.jwt.config.auth.PrincipalDetails;
import com.jwt.jwt.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;


//스프링 시큐리티에 UsernamePasswordAuthenticationFilter가 있음
// login 요청해서 username, password를 전송하면
// UsernamePasswordAuthenticationFilter가 동작한다
// but, 현재 formlogin.disable()을 해서 이 필터가 작동하지 않는다
// 따라서 이 필터를 작동시키기 위해 SecurityConfig에 jwtAuthenticationFilter를 등록해야 한다
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // AuthenticationManager를 이용해 로그인 진행한다
    private final AuthenticationManager authenticationManager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try{
//            BufferedReader br=req.getReader();
//            String input=null;
//            while((input=br.readLine())!=null){
//                System.out.println(input);
            ObjectMapper om = new ObjectMapper();
            User user=om.readValue(req.getReader(), User.class);  //user에 object를 담아줌
            System.out.println(user);

            //토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

            //AuthenticationManager에 토큰을 넣어서 던지면 인증해줌. PrincipalDetailService의 loadByUsername()함수가 실행된 후 정상이면 authentication이 리턴됨.
            //db에 있는 username, password와 일치한다.
            Authentication authentication=authenticationManager.authenticate(authenticationToken);

            //authentication에는 내 로그인한 정보가 담긴다
            PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();
            System.out.println("로그인 완료됨:"+principalDetails.getUser().getUsername()); //출력되면 로그인이 정상적으로 되었다는 뜻.
            System.out.println("1=====");
            //authentication 객체를 session 영역에 저장해야 하는데 그 방법이 authentication 객체를 return 해주는 것
            //리턴의 이유는 권한관리를 security가 대신 해주기 때문에 편하려고 하는 것이다
            //굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리 때문에 session을 넣어준다

            //JWT 토큰 만들기


            return authentication;
//            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // 2.정상인지 로그인 시도를 해본다. AuthenticationManager로 로그인 시도를 하면!
        // PrincipalDetailsService가 호출되고 loadUserByUsername()이 실행된다

        // 3.PrincipalDetails를 세션에 담고 (user, manager와 같은 권한 관리를 위해. 권한관리 안 할거면 불필요)

        // 4.Jwt토큰을 만들어서 응답해줌

        return null;
    }

    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
    //여기서 Jwt 토큰을 만들어서 request 요청한 사용자에게 Jwt 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        System.out.println("successfulAuthentication이 실행됨:인증이완료되었다는 뜻");
        //PrincipalDeatils 이용해서 토큰 만들기(라이브러리 이용)
        PrincipalDetails principalDetails=(PrincipalDetails)auth.getPrincipal();

        // Hash 암호방식 (RSA 아님) -> Hash 방식을 더 많이 쓴다
        String jwtToken= JWT.create()
                .withSubject("cos토큰")
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60000)*30)) //토큰 만료시간 (현재시간+1분*30)
                        .withClaim("id",principalDetails.getUser().getUsername())
                        .withClaim("username", principalDetails.getUser().getUsername())
                        .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer "+jwtToken);
    }
}

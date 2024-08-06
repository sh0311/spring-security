package com.jwt.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.LogRecord;

public class MyFilter2 implements Filter {

    // id, pw가 정상적으로 들어와서 로그인이 완료되면 -> 토큰을 만들어주고 그걸 response해준다
    // 요청할 때마다 header에 Authorization value값으로 토큰을 가져옴
    // 그때 토큰이 넘어오면 서버에서는 이 토큰이 내가 만든 토큰이 맞는지 검증(RSA/HS256)

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if(req.getMethod().equals("POST")) {
            String headerAuth=req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터2");
            if(headerAuth.equals("cos"))
                filterChain.doFilter(req, res);
            else{
                PrintWriter out=res.getWriter();
                out.println("인증안됨");
        }
            
    }
}
}

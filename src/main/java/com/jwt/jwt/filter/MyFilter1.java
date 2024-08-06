package com.jwt.jwt.filter;


import jakarta.servlet.*;

import java.io.IOException;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터");
        filterChain.doFilter(servletRequest, servletResponse);//프로세스를 끝내지 않고 계속 진행하게 하기 위해
    }
}

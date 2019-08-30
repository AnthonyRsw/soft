package com.itheima.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor{

    /**
     * 登录验证
     *      如果session有登录信息，放行
     *      如果session没有登录信息，拦截
     * 判断是否是登录请求，如果登录请求，直接放行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求路径
        String requestURI = request.getRequestURI();
        //判断是否是登录请求
        if(requestURI.contains("login")){
//            如果是登录请求，直接放行
            return true;
        }
        //从session中获取登录信息
        Object username = request.getSession().getAttribute("username");
        if(username != null){
            //session中有登录信息，放行
            return true;
        }else{
            //session没有登录信息，跳转到登录页面
            response.sendRedirect("/login.jsp");
            return false;
        }
    }
}

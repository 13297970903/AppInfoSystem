package com.jbit.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SysInterceptor implements HandlerInterceptor {

    /*进入请求方法之前*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session
        Object devuser = request.getSession().getAttribute("devuser");
        if (devuser!=null){
            return true;
        }
        response.sendRedirect("/jsp/devlogin.jsp");
        return false;
    }
}

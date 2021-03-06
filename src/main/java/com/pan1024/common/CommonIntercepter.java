package com.pan1024.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: CommonIntercepter
 * @Date: 2019/6/6
 * @describe: 加入项目路径
 **/
@Component
public class CommonIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if (!request.getRequestURI().equals("/login")){
            try {
                if (request.getSession().getAttribute("admin")==null){
                    response.sendRedirect("/login");
                }
            }catch (Exception e){
                response.sendRedirect("/login");
            }
            if (request.getRequestURI().matches(".*.(?:view)\\\\??.*")) {
                request.setAttribute("ctxPath",request.getContextPath());
                request.setAttribute("url",request.getRequestURI());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}

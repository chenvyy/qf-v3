package com.qianfeng.qfv3cartweb.intercepter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v3.common.constant.CookieConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.user.api.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CYY
 * @date 2019/10/31 0031 11:07
 */
@Component
public class AuthIntercepter implements HandlerInterceptor {
    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //校验是否登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //找到自己种植的cookie
                if (CookieConstant.USER_TOKEN.equals(cookie.getName())) {
                    String uuid = cookie.getValue();
                    //调用登录服务判断当前用户是否登录
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    if (resultBean.getCode() == 0) {
                        /**
                         * 放行
                         */
                        request.setAttribute("user", resultBean.getData());
                        return true;
                    }
                }
            }
        }
        return true;
    }
}

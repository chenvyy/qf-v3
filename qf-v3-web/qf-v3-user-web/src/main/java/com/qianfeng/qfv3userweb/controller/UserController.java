package com.qianfeng.qfv3userweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v3.common.constant.CookieConstant;
import com.qf.v3.common.constant.RedisConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.common.utils.HttpClientUtils;
import com.qf.v3.entity.TUser;
import com.qf.v3.user.api.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author CYY
 * @date 2019/10/28 0028 19:17
 */
@Controller
@RequestMapping("user")
public class UserController {
    /**
     * 引用dubbo中的IUserService服务
     */
    @Reference
    private IUserService userService;
    /**
     * 注入RedisTemplate
     */
    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("show/login")
    public String checkIsExists(){
        return "login";
    }

    @RequestMapping("login")
    public String login(TUser user, HttpServletResponse response,
                        @CookieValue(name = CookieConstant.USER_CART_TOKEN,required = false)String cartToken){
        //检查用户名是否存在
        TUser currentUser=userService.checkIsExists(user);
        if (currentUser==null){
            return "login";
        }
        //生成UUID
        String uuid= UUID.randomUUID().toString();
        //存到Redis中
        String userKey=new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        //currentUser密码是个密文redis不需要
        currentUser.setPassword(null);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(userKey,currentUser);
        //设置有效期
        redisTemplate.expire(userKey,30, TimeUnit.MINUTES);
        //将uuid放到cookie中
        Cookie cookie=new Cookie(CookieConstant.USER_TOKEN,uuid);
        cookie.setPath("/");
        //表示该cookie无法用客户端脚本拿到
        //只能后端程序拿到
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        //写合并购物车逻辑 不采用dubbo调用合并
        //准备cookie  Cookie:user_cart_token=121a-23322-assa;user_token=1231-23232-dss1
        Map<String,String> params=new HashMap<>();
        StringBuilder cookieValue=new StringBuilder(CookieConstant.USER_CART_TOKEN)
                .append("=")
                .append(cartToken)
                .append(";")
                .append(CookieConstant.USER_TOKEN)
                .append("=")
                .append(uuid);
        params.put("Cookie",cookieValue.toString());
        HttpClientUtils.doGetWithHeaders("http://localhost:9096/cart/merge",params);

        return "redirect:http://localhost:9092/index";
    }

    //检查当前是否登录
    @RequestMapping("check/isLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){

        //拿到cookie
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return new ResultBean(1,null,"当前用户未登录");
        }
        //查找cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieConstant.USER_TOKEN)){
                String uuid = cookie.getValue();
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                //拼接key
                String key=new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
                TUser user = (TUser) redisTemplate.opsForValue().get(key);
                if (user!=null){
                    //刷新redis有效期
                    redisTemplate.expire(key,30, TimeUnit.MINUTES);
                    return new ResultBean(0,user,"当前用户已登录");
                }
            }
        }


        return new ResultBean(1,null,"当前用户未登录");
    }

    //登出注销
    @RequestMapping("logout")
    public String logout(@CookieValue(value = CookieConstant.USER_TOKEN,required = false) String uuid,HttpServletResponse response){
        if (uuid==null){
            return "index";
        }
        String key=new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.delete(key);
        //删除cookie
        Cookie cookie=new Cookie(CookieConstant.USER_TOKEN,uuid);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:http://localhost:9092/index";
    }
}

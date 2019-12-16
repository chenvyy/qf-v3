package com.qianfeng.qfv3cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v3.cart.api.ICartService;
import com.qf.v3.common.constant.CookieConstant;
import com.qf.v3.common.constant.RedisConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author CYY
 * @date 2019/10/29 0029 19:46
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;


    @RequestMapping("addToCart/{productId}/{count}")
    @ResponseBody
    public ResultBean adToCart(@PathVariable("productId") Long productId,
                               @PathVariable("count") Integer count,
                               @CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid,
                               HttpServletResponse response,
                               HttpServletRequest request) {
        //校验已登录信息
        TUser user = (TUser) request.getAttribute("user");
        if (user != null) {
            //已登录
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.addToCart(uKey, productId, count);
            resetCookie(uuid, response);
            return resultBean;
        }

        //未登录
        if (uuid == null || uuid.equals("")) {
            //用户第一次添加购物车为其生产uuid
            uuid = UUID.randomUUID().toString();
        }
        String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.addToCart(uKey, productId, count);
        //重置cookie
        resetCookie(uuid, response);
        return resultBean;
    }

    private void resetCookie(@CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConstant.USER_CART_TOKEN, uuid);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    // 获取购物车，根据用户是否登录选择是获取未登录状态下的购物车还是登录状态下的购物车
    @RequestMapping("getCart")
    @ResponseBody
    public ResultBean getCart(@CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid,
                              HttpServletResponse response,
                              HttpServletRequest request) {
        //校验已登录信息
        TUser user = (TUser) request.getAttribute("user");
        if (user != null) {
            //已登录
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.getCart(uKey);
            resetCookie(uuid, response);
            return resultBean;
        }

        //用户未登录，判断该用户所在浏览器端的Cookie中是否有我们种植的key
        if (uuid == null || uuid.equals("")) {
            return new ResultBean(1, null, "用户没有购物车");
        }
        //使用我们在用户浏览器端种植的cookie的value与redis的前缀进行拼接，然后找到redis中对应的购物车
        String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.getCart(rKey);
        if (resultBean.getCode() == 1) {
            return new ResultBean(1, null, "用户没有购物车");
        }
        resetCookie(uuid, response);
        return resultBean;
    }


    //获取用户前端展示的购物车对象
    @RequestMapping("getCartVo")
    @ResponseBody
    public ResultBean getCartVo(@CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid, HttpServletResponse response) {
        if (uuid == null && uuid.equals("")) {
            return new ResultBean(1, null, "用户没有购物车");
        }
        ResultBean resultBean = cartService.getCartVo(uuid);
        if (resultBean.getCode() == 1) {
            return new ResultBean(1, null, "用户没有购物车");
        }
        resetCookie(uuid, response);
        return resultBean;
    }

    //修改未登录购物车中商品的数量
    @RequestMapping("updateCart/{productId}/{count}")
    @ResponseBody
    public ResultBean updateCart(@PathVariable("productId") Long productId,
                                 @PathVariable("count") Integer count,
                                 @CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid,
                                 HttpServletResponse response
    ) {
        if (count <= 0) {
            return new ResultBean(1, null, "更新购物车数量不能为0");
        }
        if (uuid == null || uuid.equals("")) {
            return new ResultBean(1, null, "用户不存在购物车");
        }
        ResultBean resultBean = cartService.resetCount(uuid, productId, count);
        resetCookie(uuid, response);
        return resultBean;
    }

    @RequestMapping("remove/{productId}")
    @ResponseBody
    public ResultBean remove(@PathVariable("productId") Long productId,
                             @CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid,
                             HttpServletResponse response) {
        if (uuid == null || uuid.equals("")) {
            return new ResultBean(1, null, "用户不存在购物车");
        }
        ResultBean resultBean = cartService.remove(uuid, productId);
        resetCookie(uuid, response);
        return resultBean;
    }

    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = CookieConstant.USER_CART_TOKEN, required = false) String uuid,
                            HttpServletRequest request,
                            HttpServletResponse response
    ) {
        if (uuid == null || uuid.equals("")) {
            return new ResultBean(1, null, "不存在未登录的购物车,不需要合并");
        }
        //获取登录key
        TUser user = (TUser) request.getAttribute("user");
        //拼接key
        String loginKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
        String noLoginKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.merge(noLoginKey, loginKey);
        resetCookie(uuid, response);
        return resultBean;
    }
}

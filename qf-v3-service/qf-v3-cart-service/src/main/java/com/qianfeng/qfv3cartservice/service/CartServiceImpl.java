package com.qianfeng.qfv3cartservice.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v3.cart.api.ICartService;
import com.qf.v3.cart.api.pojo.CartItem;
import com.qf.v3.cart.api.vo.CartItemVo;
import com.qf.v3.common.constant.RedisConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;
import com.qf.v3.mapper.TProductMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author CYY
 * @date 2019/10/29 0029 18:52
 */
@Component
@Service
public class CartServiceImpl implements ICartService {
    /**
     * 注入RedisTemplate对象
     */
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TProductMapper productMapper;

    //向购物车中添加商品
    @Override
    public ResultBean addToCart(String uKey, Long productId, Integer count) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<CartItem> cart = (List<CartItem>) redisTemplate.opsForValue().get(uKey);
        //说明用户第一次添加商品
        if (cart == null) {
            cart = new ArrayList<>();
            return getResultBean(productId, count, uKey, cart);
        }

        //说明用户有购物车,遍历购物车，如果购物车中原有商品编号与新添加商品的编号相同，则在原有商品数量上添加
        for (CartItem cartItem : cart) {
            if (cartItem.getProductId().longValue() == productId) {
                cartItem.setCount(cartItem.getCount() + count);
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.opsForValue().set(uKey, cart);
                redisTemplate.expire(uKey, 30, TimeUnit.DAYS);
                return new ResultBean(0, cart.size(), "添加购物车成功");
            }
        }

        //说明是个新商品
        return getResultBean(productId, count, uKey, cart);
    }

    //抽出的公共代码
    private ResultBean getResultBean(Long productId, Integer count, String uKey, List<CartItem> cart) {
        CartItem cartItem = new CartItem(productId, count, new Date());
        cart.add(cartItem);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(uKey, cart);
        redisTemplate.expire(uKey, 30, TimeUnit.DAYS);
        return new ResultBean(0, cart.size(), "添加购物车成功");
    }

    //通过uuid获取购物车
    @Override
    public ResultBean getCart(String rKey) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<CartItem> cart = (List<CartItem>) redisTemplate.opsForValue().get(rKey);
        //购物车为null与购物车中商品的数量为0时认定不存在此购物车
        if (cart == null || cart.size() == 0) {
            return new ResultBean(1, "", "用户不存在购物车");
        }
        redisTemplate.expire(rKey, 30, TimeUnit.DAYS);
        return new ResultBean(0, cart, "");
    }

    @Override
    public ResultBean getCartVo(String key) {
        ResultBean resultBean = getCart(key);
        if (resultBean.getCode() == 0) {
            List<CartItem> cart = (List<CartItem>) resultBean.getData();
            //准备一个集合用于存放传递给前端页面的购物车中的商品信息
            List<CartItemVo> cartItemVos = new ArrayList<>();
            //将cartItem转换成CartItemVo
            for (CartItem cartItem : cart) {
                CartItemVo cartItemVo = new CartItemVo();
                //完成item和vo的转换,先到缓存中取redis,没有再去数据库取
                String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(cartItem.getProductId()).toString();
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                TProduct product = (TProduct) redisTemplate.opsForValue().get(rKey);
                if (product == null) {
                    //说明缓存里面没有商品信息,去数据库查询
                    product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                    //存到Redis中,完成key的序列化
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(rKey, product);
                    redisTemplate.expire(rKey, 30, TimeUnit.MINUTES);
                }
                cartItemVo.setCount(cartItem.getCount());
                cartItemVo.setProduct(product);
                cartItemVo.setUpdateDate(cartItem.getUpdateTime());
                cartItemVos.add(cartItemVo);
            }
            return new ResultBean(0, cartItemVos, "");
        }
        return resultBean;
    }

    //重置购物车
    @Override
    public ResultBean resetCount(String uuid, Long productId, Integer count) {
        //获取购物车
        ResultBean resultBean = getCart(uuid);
        if (resultBean.getCode().longValue() == 0) {
            List<CartItem> cartItems = (List<CartItem>) resultBean.getData();
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProductId().longValue() == productId) {
                    cartItem.setCount(count);
                    String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(rKey, cartItems);
                    redisTemplate.expire(rKey, 30, TimeUnit.DAYS);
                    return new ResultBean(0, cartItems, "重置购物车成功");
                }
            }
        }
        return resultBean;
    }

    //从购物车中移除商品
    @Override
    public ResultBean remove(String uuid, Long productId) {
        //获取购物车
        ResultBean resultBean = getCart(uuid);
        //购物车不为空，遍历购物车
        if (resultBean.getCode().longValue() == 0) {
            List<CartItem> cartItems = (List<CartItem>) resultBean.getData();
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProductId().longValue() == productId) {
                    cartItems.remove(cartItem);
                    String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(rKey, cartItems);
                    redisTemplate.expire(rKey, 30, TimeUnit.DAYS);
                    return new ResultBean(0, cartItems, "购物车商品删除成功");
                }
            }
        }
        return resultBean;
    }

    //合并购物车
    @Override
    public ResultBean merge(String noLoginCartKey, String loginCartKey) {
        ResultBean noLoginCart = getCart(noLoginCartKey);
        ResultBean loginCart = getCart(loginCartKey);
        //已登录的购物车没有东西的时候将未登录的购物车数据添加到已登录
        if (loginCart.getCode().longValue() == 1) {
            List<CartItem> noLoginCartItems = (List<CartItem>) noLoginCart.getData();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set(loginCartKey, noLoginCartItems);
            redisTemplate.expire(loginCartKey, 30, TimeUnit.DAYS);
            redisTemplate.delete(noLoginCartKey);
            return new ResultBean(0, "合并成功", "");
        }
        //彼此都存在这时需要合并了
        HashMap<Long, CartItem> map = new HashMap<>();
        //先将未登录的购物车放到map里面
        List<CartItem> noLoginCartItems = (List<CartItem>) noLoginCart.getData();
        for (CartItem noLoginCartItem : noLoginCartItems) {
            map.put(noLoginCartItem.getProductId(), noLoginCartItem);
        }
        // 再将已登录的购物车放到map里面
        List<CartItem> loginCartItems = (List<CartItem>) loginCart.getData();
        for (CartItem loginCartItem : loginCartItems) {
            CartItem now = map.get(loginCartItem.getProductId());
            if (now == null) {
                map.put(loginCartItem.getProductId(), loginCartItem);
            } else {
                //存在相同的产品，进行数量的合并
                now.setCount(loginCartItem.getCount() + now.getCount());
                map.put(loginCartItem.getProductId(), now);
            }
        }
        //得到合并后的购物车
        Collection<CartItem> cartItems = map.values();
        List<CartItem> loginCart1 = new ArrayList<>(cartItems);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(loginCartKey, loginCart1);
        redisTemplate.expire(loginCartKey, 30, TimeUnit.DAYS);
        redisTemplate.delete(noLoginCartKey);
        return new ResultBean(0, "购物车合并成功", "");
    }
}

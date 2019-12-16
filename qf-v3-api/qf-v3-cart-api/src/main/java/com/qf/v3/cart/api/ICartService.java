package com.qf.v3.cart.api;

import com.qf.v3.common.pojo.ResultBean;
/**
 * @author CYY
 * @date 2019/10/29 0029 19:46
 */
public interface ICartService {

    /**
     * 添加商品到购物车
     * @param key           存储到redis中的key 就是uuid
     * @param productId     商品编号 通过商品编号可以查出商品详情 作为value存储到redis中
     * @param count         商品数量
     * @return
     */
    ResultBean addToCart(String key, Long productId, Integer count);
    //获取购物车
    ResultBean getCart(String key);
    //获取购物车VO
    ResultBean getCartVo(String key);
    //重置购物车
    ResultBean resetCount(String uuid, Long productId, Integer count);
    //删除购物车
    ResultBean remove(String uuid, Long productId);
    //合并购物车
    ResultBean merge(String noLoginKey,String loginKey);
}

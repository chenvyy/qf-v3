package com.qf.v3.cart.api.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CYY
 * @date 2019/10/29 0029 19:49
 */
public class CartItem implements Serializable {

    private Long        productId; //添加的商品
    private Integer     count;     //添加的商品数量
    private Date        updateTime;//添加的商品时间

    /**
     * 无参有参构造
     * @param
     */
    public CartItem() {
    }

    public CartItem(Long productId, Integer count, Date updateTime) {
        this.productId = productId;
        this.count = count;
        this.updateTime = updateTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }
}

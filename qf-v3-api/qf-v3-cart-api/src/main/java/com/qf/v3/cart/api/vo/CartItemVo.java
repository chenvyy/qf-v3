package com.qf.v3.cart.api.vo;

import com.qf.v3.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CYY
 * @date 2019/10/30 0030 14:15
 */
public class CartItemVo implements Serializable {
    private TProduct product; //添加的商品
    private Integer count;    //添加的商品数量
    private Date updateDate;  //添加的商品时间

    public CartItemVo() {
    }

    public CartItemVo(TProduct product, Integer count, Date updateDate) {
        this.product = product;
        this.count = count;
        this.updateDate = updateDate;
    }

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "CartItemVo{" +
                "product=" + product +
                ", count=" + count +
                ", updateDate=" + updateDate +
                '}';
    }
}

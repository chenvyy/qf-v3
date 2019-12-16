package com.qf.v3.product.api.dto;

import com.qf.v3.entity.TProduct;

import java.io.Serializable;

/**
 * @author CYY
 * @date 2019/10/9 0009 18:55
 */
public class ProductDto implements Serializable {
    private TProduct tProduct;  // 创建tProduct对象用来接收商品的信息（对应t_product表）
    private String productDesc; // 创建tProductDesc用来接收商品的详细信息（对应product_desc表）

    public ProductDto() {
    }

    public ProductDto(TProduct tProduct, String productDesc) {
        this.tProduct = tProduct;
        this.productDesc = productDesc;
    }

    public TProduct gettProduct() {
        return tProduct;
    }

    public TProduct deleteProduct(){
        return tProduct;
    }

    public void settProduct(TProduct tProduct) {
        this.tProduct = tProduct;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

}

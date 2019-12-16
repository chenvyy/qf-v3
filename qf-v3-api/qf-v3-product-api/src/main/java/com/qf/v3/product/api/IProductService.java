package com.qf.v3.product.api;

import com.qf.v3.common.IBaseService;
import com.qf.v3.entity.TProduct;
import com.qf.v3.product.api.dto.ProductDto;

/**
 * @author CYY
 * @date 2019/9/29 19:52
 */
public interface IProductService<T> extends IBaseService<T> {
    //添加商品
    Long save(ProductDto dto);

    TProduct selectTProduct(Long id);
    //编辑商品
    Long edit(ProductDto dto);
}

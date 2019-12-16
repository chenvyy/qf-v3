package com.qf.v3.mapper;

import com.qf.v3.common.IBaseDao;
import com.qf.v3.entity.TProduct;

public interface TProductMapper extends IBaseDao<TProduct> {
    TProduct selectTProduct(Long id);
}



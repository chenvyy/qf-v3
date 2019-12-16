package com.qf.v3.item.api;

import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;

import java.util.List;

/**
 * @author CYY
 * @date 2019/10/15 0015 17:43
 */
public interface ItemService {
    ResultBean createItemPages(TProduct product);

    ResultBean batchCreatePages(List<TProduct> products);
}

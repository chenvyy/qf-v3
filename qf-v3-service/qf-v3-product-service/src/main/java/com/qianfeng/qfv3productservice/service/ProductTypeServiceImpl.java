package com.qianfeng.qfv3productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v3.common.IBaseDao;
import com.qf.v3.common.IBaseServiceImpl;
import com.qf.v3.entity.TProductType;
import com.qf.v3.mapper.TProductTypeMapper;
import com.qf.v3.product.api.IProductTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CYY
 * @date 2019/10/10 0010 10:54
 */
@Component
@Service
public class ProductTypeServiceImpl extends IBaseServiceImpl<TProductType> implements IProductTypeService {

    @Resource
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao getIbaseDao() {
        return productTypeMapper;
    }
}

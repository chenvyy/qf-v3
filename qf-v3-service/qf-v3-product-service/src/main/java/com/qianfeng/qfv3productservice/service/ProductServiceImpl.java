package com.qianfeng.qfv3productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import com.qf.v3.common.IBaseDao;
import com.qf.v3.common.IBaseServiceImpl;
import com.qf.v3.entity.TProduct;
import com.qf.v3.entity.TProductDesc;
import com.qf.v3.mapper.TProductDescMapper;
import com.qf.v3.mapper.TProductMapper;
import com.qf.v3.product.api.IProductService;
import com.qf.v3.product.api.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CYY
 * @date 2019/9/29 20:15
 */
@Component
@Service
public class ProductServiceImpl extends IBaseServiceImpl<TProduct> implements IProductService<TProduct> {

    @Resource
    private TProductMapper productMapper;
    @Resource
    private TProductDescMapper productDescMapper;
    //当前类实现抽象类MyBaseServiceImpl必须要实现抽象方法getMyBaseDao
    @Override
    public IBaseDao getIbaseDao() {
        return productMapper;
    }

    // 调用查询所有商品的list方法
    public List<TProduct> list(){
        return productMapper.list();
    }


    @Override
    public Long save(ProductDto dto) {
        //添加商品到数据库
        TProduct product = dto.gettProduct();
        productMapper.insertSelective(product);
        //添加商品详情
        Long id = product.getId();
        TProductDesc desc=new TProductDesc();
        desc.setProductId(id);
        desc.setProductDesc(dto.getProductDesc());
        //添加商品详情到商品详情表
        productDescMapper.insertSelective(desc);
        return id;
    }

    @Override
    public TProduct selectTProduct(Long id) {
        TProduct product = productMapper.selectTProduct(id);
        return product;

    }

    @Override
    public Long edit(ProductDto dto) {
        //更新商品到数据库
        TProduct product = dto.gettProduct();
        productMapper.updateByPrimaryKey(product);
        //更新商品详情
        Long id = product.getId();
        TProductDesc desc=new TProductDesc();
        desc.setProductId(id);
        desc.setProductDesc(dto.getProductDesc());
        //更新商品详情到商品详情表
        productDescMapper.updateByPrimaryKey(desc);
        return id;
    }


}

package com.qf.v3.search.api;

import com.qf.v3.common.pojo.PageResultBean;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;

import java.util.List;

/**
 * @author CYY
 * @date 2019/10/12 0012 11:07
 */
public interface ISearchService {
    //同步数据库的数据到solr索引库
    ResultBean<String> synAllData();
    //添加商品成功后，发消息给searchService，让其同步该条数据到solr索引库中
    ResultBean<String> saveOrUpdate(TProduct product);
    // 通过关键词获取对应的商品记录
    List<TProduct> getProductByKeywords(String productKeywords);
    //通过搜索关键字、显示第几页、每页显示的条数查找商品信息并且完成分页
    PageResultBean<TProduct> getProductByKeywordsPage(String productKeywords,Integer pageIndex,Integer pageSize);

}

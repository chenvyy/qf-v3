package com.qianfeng.qfv3searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v3.common.pojo.PageResultBean;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;
import com.qf.v3.mapper.TProductMapper;
import com.qf.v3.search.api.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class SearchServiceImpl implements ISearchService {

    @Resource
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean<String> synAllData() {

        //step 1  查询数据库
        List<TProduct> productList = productMapper.list();
        System.out.println(productList);
        //step 2  遍历数据库
        try {
            for (TProduct product : productList) {
                //step 3  逐条同步到索引库
                SolrInputDocument document=new SolrInputDocument();
                document.addField("id",product.getId());
                document.addField("name",product.getName());
                document.addField("image",product.getImage());
                document.addField("price",product.getPrice());
                document.addField("sale_price",product.getSalePrice());
                document.addField("sale_point",product.getSalePoint());
                solrClient.add(document);
            }
            solrClient.commit();
        }catch (Exception e){
            e.printStackTrace();
            return ResultBean.errorResult("添加索引库失败");
        }


        return ResultBean.successResult("添加索引库成功");
    }

    @Override
    public ResultBean<String> saveOrUpdate(TProduct product) {
        SolrInputDocument document=new SolrInputDocument();
        document.addField("id",product.getId());
        document.addField("name",product.getName());
        document.addField("image",product.getImage());
        document.addField("price",product.getPrice());
        document.addField("sale_price",product.getSalePrice());
        document.addField("sale_point",product.getSalePoint());
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.errorResult("添加商品信息到索引库失败");
        }

        return ResultBean.successResult("添加商品信息到索引库成功");
    }

    @Override
    public List<TProduct> getProductByKeywords(String productKeywords) {
        List<TProduct> products=null;
        //根据参数查询索引库
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("product_keywords:"+productKeywords);
        //添加高亮信息
        solrQuery.setHighlight(true);
        //开始设置高亮信息的字段
        solrQuery.addHighlightField("name");
        //高亮的本质是html信息
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //执行索引库查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
            // 根据查询的响应获取索引库中对应记录的集合
            SolrDocumentList documentList = response.getResults();
            // 创建一个集合，用来装查询出来的商品信息记录
            products=new ArrayList<>(documentList.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            // 遍历记录集合
            for (SolrDocument entries : documentList) {
                // 创建TProduct对象 用来存放从索引库中获取到的数据
                TProduct product=new TProduct();
                product.setId(Long.parseLong(entries.getFieldValue("id").toString()));
                product.setImage(entries.getFieldValue("image").toString());
                product.setPrice(Long.parseLong(entries.getFieldValue("price").toString()));
                product.setSalePrice(Long.parseLong(entries.getFieldValue("sale_price").toString()));
                product.setSalePoint(entries.getFieldValue("sale_point").toString());
                //高亮处理
                Map<String, List<String>> highlightText = highlighting.get(entries.getFieldValue("id").toString());
                List<String> highList = highlightText.get("name");
                if(highList!=null){
                    product.setName(highList.get(0));
                }else {
                    product.setName(entries.getFieldValue("name").toString());
                }
                products.add(product); //将product添加进集合中
            }
            return products;
        } catch (SolrServerException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public PageResultBean<TProduct> getProductByKeywordsPage(String productKeywords,
                                                             Integer pageIndex,
                                                             Integer pageSize) {

        PageResultBean<TProduct> pageResultBean=new PageResultBean<>();

        //获取商品的信息
        List<TProduct> products=null;
        SolrQuery solrQuery=new SolrQuery();
        //设置查询条件
        solrQuery.setQuery("product_keywords:"+productKeywords);
        //分页的设置
        solrQuery.setStart((pageIndex-1)*pageSize);
        solrQuery.setRows(pageSize); // 一页上显示的商品记录的个数
        solrQuery.setHighlight(true); //通过solrQuery对象开启高亮功能，默认是不开启的
        solrQuery.addHighlightField("name"); // 添加高亮域
        solrQuery.setHighlightSimplePre("<font color='red'>"); // 高亮设置的具体样式
        solrQuery.setHighlightSimplePost("</font>");
        //执行查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
            //获取总记录数
            long total = response.getResults().getNumFound();
            //获取返回的查询响应对象中的所有商品记录
            SolrDocumentList documentList = response.getResults();
            products=new ArrayList<>(documentList.size());
            // 从返回的查询响应对象中获取进行高亮处理后的字段集合
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument entries : documentList) {
                TProduct product=new TProduct();
                product.setId(Long.parseLong(entries.getFieldValue("id").toString()));
                product.setImage(entries.getFieldValue("image").toString());
                product.setPrice(Long.parseLong(entries.getFieldValue("price").toString()));
                product.setSalePrice(Long.parseLong(entries.getFieldValue("sale_price").toString()));
                product.setSalePoint(entries.getFieldValue("sale_point").toString());
                //高亮字段的设置
                Map<String, List<String>> highLightingText = highlighting.get(entries.getFieldValue("id").toString());
                List<String> highLightList = highLightingText.get("name");
                if (highLightList!=null){ // 获取到了高亮字段，那么就为高亮字段设置样式
                    //设置高亮字段
                    product.setName(highLightList.get(0));
                }else {
                    product.setName(entries.getFieldValue("name").toString());
                }
                products.add(product);
            }
            pageResultBean.setList(products);  // 将搜索出来的商品信息设置进去
            pageResultBean.setPageNum(pageIndex); // 将显示第几页设置进去
            pageResultBean.setPageSize(pageSize); // 将一页多少条设置进去
            pageResultBean.setTotal(total); // 将搜索出来的商品记录总条数设置进去
            // 总页数设置进去 如果刚好可以分成整数页，就分成整数页，否则就添加一页
            pageResultBean.setPages((int) (total%pageSize==0?total/pageSize:total/pageSize+1));


        } catch (Exception e) {
            //记录日志
            return pageResultBean;
        }

        return pageResultBean;
    }
}

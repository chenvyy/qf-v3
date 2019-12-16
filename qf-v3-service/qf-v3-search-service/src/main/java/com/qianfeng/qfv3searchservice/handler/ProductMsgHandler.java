package com.qianfeng.qfv3searchservice.handler;

import com.qf.v3.common.constant.RabbitMqConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;
import com.qianfeng.qfv3searchservice.service.SearchServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CYY
 * @date 2019/10/18 0018 17:28
 */
@Component
public class ProductMsgHandler {
    @Resource
    private SearchServiceImpl searchService;

    @RabbitListener(queues= RabbitMqConstant.PRODUCT_ADD_QUEUE)
    public void processSaveOrUpdate(TProduct product){
        System.out.println("收到商品信息："+product.toString());
        ResultBean<String> resultBean = searchService.saveOrUpdate(product);
        System.out.println("添加商品到索引库成功："+resultBean.toString());
    }
}

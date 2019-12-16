package com.qianfeng.qfv3itemservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;
import com.qf.v3.item.api.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author CYY
 * @date 2019/10/15 0015 17:52
 */
@Component
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private Configuration configuration;

    //Executors.newSingleThreadExecutor();
    //Executors.newFixedThreadPool()
    //Executors.newScheduledThreadPool()
    //Executors.newCachedThreadPool()
    //以上四种线程池一个都不用  内存溢出
    //核心线程数的选择根据cpu的核数
    private static final int CORE_THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = new ThreadPoolExecutor(CORE_THREAD_COUNT, CORE_THREAD_COUNT * 2,
            60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));

    @Value("${template.path}")
    private String TEMPLATE_PATH;

    private CallBack1 callBack = new CallBack1() {
        @Override
        public void callback(Boolean isSuccess) {
            System.out.println("接口回调" + isSuccess);
        }
    };

    //生成单个商品详情页
    @Override
    public ResultBean createItemPages(TProduct product) {
        try {
            //模板
            Template template = configuration.getTemplate("product_detail.ftl");
            //数据
            Map<String, Object> data = new HashMap<>();
            data.put("product", product);
            //输出
            FileWriter out = new FileWriter(TEMPLATE_PATH + product.getId() + ".html");
            template.process(data, out);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.errorResult("生成模板失败");
        }

        return ResultBean.successResult("生成模板成功");
    }


    //批量生成商品详情页
    @Override
    public ResultBean batchCreatePages(List<TProduct> products) {

        for (TProduct product : products) {
            pool.submit(new MyTask1(product, callBack));
            //createItemPages(product);
        }
        return ResultBean.successResult("批量生成模板成功");
    }

    interface CallBack1 {
        void callback(Boolean isSuccess);
    }

    class MyTask1 implements Runnable {
        private TProduct product;
        private CallBack1 callback1;

        public MyTask1(TProduct product, CallBack1 callBack) {
            this.product = product;
            this.callback1 = callBack;
        }

        @Override
        public void run() {
            try {
                //模板
                Template template = configuration.getTemplate("product_detail.ftl");
                //数据
                HashMap<String, Object> data = new HashMap<>();
                data.put("product", product);
                //输出
                FileWriter out = new FileWriter(TEMPLATE_PATH + product.getId() + ".html");
                template.process(data, out);
                callback1.callback(true);

            } catch (Exception e) {
                e.printStackTrace();
                callback1.callback(false);
            }
        }
    }

    class MyTask2 implements Callable<Boolean> {
        private TProduct product;

        public MyTask2(TProduct product) {
            this.product = product;
        }

        @Override
        public Boolean call() throws Exception {
            System.out.println("Callable");
            return true;
        }
    }
}

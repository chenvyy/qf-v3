package com.qf.qfv3productweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.v3.common.constant.RabbitMqConstant;
import com.qf.v3.entity.TProduct;
import com.qf.v3.item.api.ItemService;
import com.qf.v3.product.api.IProductService;
import com.qf.v3.product.api.dto.ProductDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Reference
    private IProductService productService;

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Reference
    private ItemService itemService;

    @Value("${image.path}")
    private String IMAGE_PATH;

    @RequestMapping("product2")
    @ResponseBody
    public String index() {
        TProduct product = (TProduct) productService.selectByPrimaryKey(2L);
        return product.toString();
    }

    @RequestMapping("product1")
    @ResponseBody
    public String list1() {
        List<TProduct> products = productService.list();
        itemService.batchCreatePages(products);
        return "product";
    }

    @RequestMapping("product")
    public String list(Model model) {

        //查询数据库 在IProductService加查询商品列表的方法 list
        //list返回数据放到Model
        //Model将数据返回给前端
        List<TProduct> products = productService.list();
        itemService.batchCreatePages(products);
        model.addAttribute("products", products);
        return "product";
    }

    //分页
    @RequestMapping("product/page/{pageNum}/{pageSize}")
    public String listPage(Model model, @PathVariable("pageNum") int pageNum,
                           @PathVariable("pageSize") int pageSize) {

        PageInfo<TProduct> pageInfo = productService.getPageInfo(pageNum, pageSize);
        //System.out.println(pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("lists", pageInfo.getList());
        return "product";
    }

    //添加商品
    @RequestMapping("product/add")
    public String productAdd(ProductDto dto, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //开始上传并且拿到上传后的图片存储路径
        String fullPath = null;
        try {
            StorePath storePath = fileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), ext, null);
            fullPath = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将上传后的图片路径赋值给product对象中的image属性
        dto.gettProduct().setImage(IMAGE_PATH + "/" + fullPath);
        Long typeId = dto.gettProduct().getTypeId();
        if (typeId.longValue() == 1) {
            dto.gettProduct().setTypeName("手机数码");
        } else {
            dto.gettProduct().setTypeName("家用电器");
        }
        Long productId = productService.save(dto);
        TProduct product = dto.gettProduct();
        product.setId(productId);
        //创建商品的详情页
        itemService.createItemPages(product);
        //同步商品信息到索引库中 rabbitmq  发消息1ms   rpc调用 10ms
        //rabbitmq 发消息->searchService-->收到商品消息-->同步到索引库
        rabbitTemplate.convertAndSend(RabbitMqConstant.PRODUCT_EXCHANGE, "product.add", product);
        //发消息 给邮件服务 进行发邮件
        rabbitTemplate.convertAndSend(RabbitMqConstant.MAIL_EXCHANGE,"mail.send","邮件发送成功");
        return "redirect:/product/page/100/5";
    }

    //删除商品
    @RequestMapping("product/del")
    @ResponseBody
    public Object deleteProductById(Long id) {
        int i = productService.deleteByPrimaryKey(id);
        return "succeed";
    }

    //批量删除商品
    @RequestMapping("product/delAll")
    @ResponseBody
    public String delBatch(String[] ids) {
        int i = productService.deleteAllByPrimaryKey(ids);
//        String[] arr = ids.split(",");
        return "succeed";
    }
    //旧版本删除
//    public String productDelete(@PathVariable int pageNum, @PathVariable int pageSize, @PathVariable("id") Long id, Model model) {
//        productService.deleteByPrimaryKey(id);
//        PageInfo pageInfo = productService.getPageInfo(pageNum, pageSize);
//        model.addAttribute("pageInfo", pageInfo);
//        return "redirect:/product/page/1/5";
//    }

    //编辑商品
    @RequestMapping("product/edit")
    public String productEdit(ProductDto dto, MultipartFile editFile) {
//      Long id=dto.gettProduct().getId();
        String originalFilename = editFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //开始上传并且拿到上传后的图片存储路径
        String fullPath = null;
        try {
            StorePath storePath = fileStorageClient.uploadImageAndCrtThumbImage(editFile.getInputStream(), editFile.getSize(), ext, null);
            fullPath = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将上传后的图片路径赋值给product对象中的image属性
        dto.gettProduct().setImage(IMAGE_PATH + "/" + fullPath);
        Long typeId = dto.gettProduct().getTypeId();
        if (typeId.longValue() == 1) {
            dto.gettProduct().setTypeName("手机数码");
        } else {
            dto.gettProduct().setTypeName("家用电器");
        }
        Long productId = productService.edit(dto);
        TProduct product = dto.gettProduct();
        product.setId(productId);
        //创建商品的详情页
        itemService.createItemPages(product);
        //同步商品信息到索引库中 rabbitmq  发消息1ms   rpc调用 10ms
        //rabbitmq 发消息->searchService-->收到商品消息-->同步到索引库
        rabbitTemplate.convertAndSend(RabbitMqConstant.PRODUCT_EXCHANGE, "product.add", product);
        return "redirect:/product/page/100/5";
    }
}

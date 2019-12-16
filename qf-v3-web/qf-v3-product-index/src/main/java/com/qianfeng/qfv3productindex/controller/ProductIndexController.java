package com.qianfeng.qfv3productindex.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v3.common.constant.CookieConstant;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProductType;
import com.qf.v3.product.api.IProductTypeService;
import com.qf.v3.user.api.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author CYY
 * @date 2019/10/9 0009 21:49
 */
@Controller
public class ProductIndexController {

    @Reference
    private IProductTypeService productTypeService;

    @Reference
    private IUserService userService;

    @RequestMapping("index")
    public String showIndex(Model model){
        List<TProductType> productTypes = productTypeService.list();
        model.addAttribute("productTypes",productTypes);
        System.out.println(productTypes);
        return "index";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_TOKEN,required = false) String uuid){
        return userService.checkIsLogin(uuid);
    }
}

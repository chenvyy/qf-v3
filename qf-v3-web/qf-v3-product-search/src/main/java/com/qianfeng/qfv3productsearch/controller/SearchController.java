package com.qianfeng.qfv3productsearch.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v3.common.constant.CookieConstant;
import com.qf.v3.common.pojo.PageResultBean;
import com.qf.v3.common.pojo.ResultBean;
import com.qf.v3.entity.TProduct;
import com.qf.v3.search.api.ISearchService;
import com.qf.v3.user.api.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author CYY
 * @date 2019/10/12 0012 12:11
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;
    @Reference
    private IUserService userService;
    @RequestMapping("init_data")
    @ResponseBody
    public ResultBean synAllData() {

        ResultBean<String> resultBean = searchService.synAllData();
        return resultBean;
    }

    @RequestMapping("searchByKeywords")
    public String searchByKeywords(String productKeywords, Model model) {
        System.out.println(productKeywords);
        List<TProduct> productList = searchService.getProductByKeywords(productKeywords);
        System.out.println(productList);
        model.addAttribute("products", productList);
        return "show_list";
    }

    //实现搜索及搜索出的商品信息的分页功能
    @RequestMapping("searchByKeywords/{pageIndex}/{pageSize}")
    public String searchByKeywordsPage(String productKeywords,
                                       @PathVariable("pageIndex") Integer pageIndex,
                                       @PathVariable("pageSize") Integer pageSize,Model model){
        System.out.println(productKeywords);
        PageResultBean<TProduct> pageResultBean = searchService.getProductByKeywordsPage(productKeywords, pageIndex, pageSize);
        System.out.println(pageResultBean);
        // 设置分页导航条的固定页数
        pageResultBean.setNavigatePages(5);
        model.addAttribute("pageInfo",pageResultBean);
        model.addAttribute("productKeywords",productKeywords);
        return "show_list";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_TOKEN,required = false) String uuid){
        return userService.checkIsLogin(uuid);
    }
}

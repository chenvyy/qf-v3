package com.qianfeng.qfv3productsearch.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.apache.catalina.manager.Constants.CHARSET;
/**
 * @author CYY
 * @date 2019/11/7 0007 19:52
 */
@Controller
@RequestMapping("alipay")
public class AlipayControler {
    @RequestMapping("toPay")
    public void pay(HttpServletResponse httpResponse, String order_no) throws IOException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                "2016101700708580",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCsXSA4Z5aQH7nCZveumtr08pNVnHsR3stD1qwI+Ue9kCjKSxuO7e1MGh3wFlEhZ6iW2Iao+sBvCZdN0Cvf1wHP4/+8ZVT1Y6Lh7HyIVNxyRMXglZBv1dDIMl/kcdNrHwQV9t15NPxL0cS1Ls5zXKnUkg65BT1gJXofXrm0nZ6zQ37ClK6FBiWUzwW83vxRgGzKQMA5y3ka7Y0MYTrSmG3s8+dtkCteqlppq9cyPClsllFkSkxhY4F2ddNd6ShfHt3VlKUWwbmBDqCDLVJTon2EVB6Q22IIUsm6ISG1CLPy6oNk1+LRaBjim5LYfiOGrPBy+TuTs8hjkhHsnNGE1zRFAgMBAAECggEARCgzBHrZy7Xv45KJ4G51pKSDZDgTZlaeM/4gWxAPsULfDlcy14r36b7uFbENddMilDuczBcTnxTA0tE9eC7yFX3H83xyYvJ6RzIXcGV2Im1VfeBGlCKNg4x/5Lqp6lMCb5kFrNcp6OVTDn4qvu1mbpsu3TTYNzu6lcsETgqB5cpkum5XgtVQRZe/d6g/4IbpmUi3PqPGQtMFN8RGX1Hjb3xyc8/BrnInaPmdo6XBfMR113EwoAhMusB3jZntYIu170QGI0tmfPIj7JDoB2kKHZqQzH8DBiSM45y+mhdylsL9Cu8mwIhbNVZaIi7nCI0zT7F3LVCG1DyfnsGev78ppQKBgQDpWL8+hKIsk7Em9yOOXpqc/5Actrl2qH4hdj4xqfWo10u/AckGSkBQ7kLzNqeBK3ims5PpzVrhLo1Zk4Yv8+jiV2VxxJ7/gdZkMvpt1IvS0uUeHosalE1EwC1y0AhkwxwfLGNd0myq3p3M/yOQc5iLGNfQc7GaCALkA//n4xSbYwKBgQC9GMzVfbDERi09j94lnvt7178GwduKcA0/cRkB/bEOd8fZO5lBLAuKjXefhcGsCFrWhZzKEpPOUF87P9s7F/lko6xqa3BdCY5jB6IlzLuDXFLz7hAS7Y9xKJn+oAeGFEd2tDDQ03x7/yKskJa7bGsFeQ/qRdD3GZYnqWX7w9yGNwKBgGjSdRagfjmIHwxh+Jb76HieVR6Q9r9W8VIWy9mDf9dY363uH1+z+zBf17Np2dUssgYwpUFh/nEpeFXE74KbDwKeP/PbX5FnwedA/z5XPHh4RHRDRDmsZQl9Su1Ihny9KOTYC8sZu7cGSdzMwC0jwGK7xjbdwepvrty+7zY7zAYBAoGAbObOeJlFZxP/U/f5+PKhZIYwtoSImibECmho/ZOMF4f2KW5AaZ8jGm00mkNe8WNyZR9X16xxQR1aavXwnQzGjSYR0swDTyGExhWMVXad+x39jAvrZ+s2c/XtEg45pwcgzQUjI/DYPVUHNBilJlVfISzCdKo2mUg8CKVvXVpEM2sCgYBUsBpYHkcDXMkzpkoSqC3X3oBHst9ZZxgVzPddX8sD5zdIip+xLmugJ67rWw9MXrnh7ThhVaRnLza/ZhUeFYzmJGZUfSOk92aSsryFEmP9NtQBHlHKePEVD222jN1uFntt8NWodhaXTRJVMsheL7g/mcTO3LP9mwTaNxhyzvjLaw==",
                "json", CHARSET,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCuKzkRZ96oYtVuQBOsPxGth1inA9Kzk5PNXNDCehihk44CcEPGnqma+nm4R7ktLYqNqGd5Df9S/Xcf4OIJskvc5IK9VMpmG1hns79u8qbAjvqUd+q9LOtWWPabh2IQYQTs0Q51Lp/uqVS7H5xKAGmmuABuhCz1Li4FeIzC6VDIHNZSWO6o0dkOktmqLlCtU6HS8bOUN2QjhE1XfIlf6n2tSkUPJdUY5W9pqVBrt4RyOnko9b3RIQTUiA6rpg7EzDzirQCVNiiqaNCaJxsjgij83w5IQKpGvh02nlo9D/zVZuRnVL66Yv0Uxflbb3LrvNClEHqWkELNZD9YOMlQUZQIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://localhost:9092/index");
        alipayRequest.setNotifyUrl("http://localhost:9092/index");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+2019110229+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":9999.88," +
                "    \"subject\":\"苹果11 128G\"," +
                "    \"body\":\"苹果 128G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

}

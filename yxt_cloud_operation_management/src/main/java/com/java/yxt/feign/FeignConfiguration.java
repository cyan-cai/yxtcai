package com.java.yxt.feign;

import com.java.yxt.util.ValidateUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign请求拦截器
 * @author  zanglei
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String service;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(ValidateUtil.isEmpty(attributes)){
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        String token="access-token";
        String isNeedToken="isNeedToken";
        if(ValidateUtil.isNotEmpty(request.getHeader(token))){
            requestTemplate.header("access-token", request.getHeader(token));
        }
        if(request.getAttribute(isNeedToken)!=null){
            requestTemplate.header("isNeedToken",request.getAttribute(isNeedToken).toString());
        }
        requestTemplate.header("url","NO_AUTHENTICATION_REQUIRED");
        requestTemplate.header("SERVICE",service);
    }

}
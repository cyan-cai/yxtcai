package com.java.yxt.filter;

import com.alibaba.fastjson.JSONObject;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.logger.feign.entity.Resource;
import com.java.yxt.util.ValidateUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * SimpleCORSFilter
 *
 * @author zanglei
 * @version V1.0
 * @description 解决跨域访问
 * @Package com.java.yxt.filter
 * @date 2020/9/21
 */
@Component
public class SimpleCORSFilter implements Filter {

    @Autowiredr
    RedissonClient redissonUtils;

    public static ThreadLocal<String> getThreadLocalToken() {
        return threadLocalToken;
    }

    public static void setThreadLocalToken(ThreadLocal<String> threadLocalToken) {
        SimpleCORSFilter.threadLocalToken = threadLocalToken;
    }

    public static ThreadLocal<String> threadLocalToken = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "access-control-allow-origin, authority, content-type, version-info, X-Requested-With,url,access-token");

        String accessTokenKey=null;

        String token = request.getHeader("access-token");
        // 获取请求头的key信息,如果有access-token域,必须有value值,否则认为token失效跳到登录界面
        Enumeration heads = request.getHeaderNames();
        while (heads.hasMoreElements()){
           String headName =  (String)heads.nextElement();
           if(headName.equals("access-token")){
               accessTokenKey=headName;
           }
        }

        // 判断如果有access-token域，如果值为null,则返回未授权，前端跳到登录界面
        if(ValidateUtil.isNotEmpty(accessTokenKey) && token.equals("null")){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            try {
                writer.println("{\"code\":40316,\n\"msg\":\"请求未授权\",\n\"success\":false\n}");
                writer.flush();
            }finally {
                writer.close();
            }
            return;
        }
        // 判断资源菜单访问权限
        if(ValidateUtil.isNotEmpty(accessTokenKey) && !token.equals("null")){
            RBucket<String> bucket = redissonUtils.getBucket(token);
            // token失效判断
            if(ValidateUtil.isEmpty(bucket.get())){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                try {
                    writer.println("{\"code\":40316,\n\"msg\":\"请求未授权\",\n\"success\":false\n}");
                    writer.flush();
                }finally {
                    writer.close();
                }
                return;
            }
            Admin admin = JSONObject.parseObject(bucket.get(), Admin.class);
            request.getSession().setAttribute("adminInfo",admin);
            List<Resource> resources=admin.getResources();
            if(resources !=null && resources.size()>0 && !request.getHeader("url").equals("login")){
                Optional<Resource> optionalResource=resources.parallelStream().filter(resource->
                        resource.getUrl().contains(request.getHeader("url"))
                        ||"NO_AUTHENTICATION_REQUIRED".equals(request.getHeader("url"))).findAny();
                // 无菜单访问权限
                if(!optionalResource.isPresent()){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    try {
                        writer.println("{\"code\":40316,\n\"msg\":\"请求未授权\",\n\"success\":false\n}");
                        writer.flush();
                    }finally {
                        writer.close();
                    }
                    return;
                }
            }
            // 刷新token失效时间
            bucket.expire(30*60, TimeUnit.SECONDS);
        }

        threadLocalToken.set(token);
        filterChain.doFilter(servletRequest, servletResponse);
        threadLocalToken.remove();
    }

    @Override
    public void destroy() {
    }
}

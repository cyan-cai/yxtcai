package com.java.yxt.aop;

import com.alibaba.fastjson.JSONObject;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.filter.SimpleCORSFilter;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author zanglei
 * @version V1.0
 * @description 添加信息时设置创建人和修改人id
 * @Package com.java.yxt.aop
 * @date 2020/11/13
 */
@Aspect
@Component
@Slf4j
public class SetCreaterIdAndUpdaterId {

        @Autowired
        RedissonClient redissonUtils;

        /**
         * 定义注解切点，需要设置注解才生效
         */
        @Pointcut("@annotation(com.java.yxt.annotation.SetCreaterName)")
        void setCreaterAndUpdaterIdCut(){

        }

        /**
         * 添加创建人和创建人id,更新人id
         * @param joinPoint
         * @throws Throwable
         */
        @Around("setCreaterAndUpdaterIdCut()")
        public Object setCreaterIdAndUpdaterId(ProceedingJoinPoint joinPoint) throws Throwable {
            // 访问目标方法的参数：
            Object[] args = joinPoint.getArgs();
            Object object = args[0];
            Class c1 = object.getClass();

            // 获取基础父类的createrId,updaterId
            Field[] fields = c1.getSuperclass().getSuperclass().getDeclaredFields();
            String token = SimpleCORSFilter.getThreadLocalToken().get();
            RBucket<String> bucket = redissonUtils.getBucket(token);
            Admin admin = JSONObject.parseObject(bucket.get(), Admin.class);
            if(ValidateUtil.isEmpty(admin)){
                log.error("根据token:{},获取admin信息为空。",token);
                throw new MySelfValidateException(ValidationEnum.TOKEN_REDIS_ADMIN_EMPTY);
            }
            // 赋值
            for(Field field:fields){
                if(field.getName().equals("createrId") || field.getName().equals("updaterId")){
                    field.setAccessible(true);
                    field.set(object,String.valueOf(admin.getAdminId()));
                }
                if(field.getName().equals("createrName") || field.getName().equals("updaterName")){
                    field.setAccessible(true);
                    field.set(object,String.valueOf(admin.getRealname()));
                }
                if(field.getName().equals("tenantId")){
                    field.setAccessible(true);
                    field.set(object,String.valueOf(admin.getSiteId()));
                }
                if(field.getName().equals("orgId")){
                    field.setAccessible(true);
                    if(ValidateUtil.isEmpty(admin.getOrgId())){
                        field.set(object,null);
                    }else{
                        field.set(object,String.valueOf(admin.getOrgId()));
                    }

                }
            }
            // 执行被代理的方法
            return joinPoint.proceed();
        }
}

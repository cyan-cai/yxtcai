package com.java.yxt.feign;

import com.java.yxt.dto.YxtStaffQueryVo;
import com.java.yxt.feign.vo.AddOrgVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zanglei
 * @version V1.0
 * @description 同步添加组织回调
 * @Package com.java.yxt.feign
 * @date 2020/10/22
 */
@Component
@Slf4j
public class CustomerFeignFallbackImpl implements FallbackFactory<CustomerFeignService>{


    @Override
    public CustomerFeignService create(Throwable throwable) {
        return new CustomerFeignService() {
            @Override
            public ResultObject save(AddOrgVo addOrgVo) {
                log.error("同步添加组织失败入参：{}",addOrgVo.toString());
                return ResultObject.fail(throwable.getMessage());
            }

            @Override
            public ResultObject delete(Integer id) {
                log.error("同步添加组织失败入参：{}",id);
                return ResultObject.fail(throwable.getMessage());
            }

            @Override
            public ResultObject select(Long id) {
                log.error("同步添加组织失败入参：{}",id);
                return ResultObject.fail(throwable.getMessage());
            }

            @Override
            public ResultObject update(Integer id) {
                log.error("同步添加组织失败入参：{}",id);
                return ResultObject.fail(throwable.getMessage());
            }

            @Override
            public ResultObject getUserInfo(YxtStaffQueryVo yxtStaffQueryVo) {
                log.error("查询创建人信息失败入参：{}",yxtStaffQueryVo.toString());
                return ResultObject.fail(throwable.getMessage());
            }
        };
    }
}

package com.java.yxt.feign;

import com.java.yxt.dto.StaffResp;
import com.java.yxt.dto.YxtStaffQueryVo;
import com.java.yxt.feign.vo.AddOrgVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调用外部系统
 * @author zanglei
 */
@FeignClient(value = "hrsvc" ,fallbackFactory = CustomerFeignFallbackImpl.class)
public interface CustomerFeignService {
    /**
     * 添加组织
     * @param addOrgVo
     * @return
     */
    @PostMapping("service/114/orgApi")
    ResultObject save(@RequestBody AddOrgVo addOrgVo);

    /**
     * 删除组织
     * @param id
     * @return
     */
    @DeleteMapping("/service/114/orgApi/{id}")
    ResultObject delete(@PathVariable Integer id);

    /**
     * 查询组织
     * @param id
     * @return
     */
    @GetMapping("/service/114/orgApi/{id}")
    ResultObject select(@PathVariable Long id);

    /**
     * 启用组织
     * @param id
     * @return
     */
    @PutMapping("/service/114/orgApi/{id}")
    ResultObject update(@PathVariable Integer id);

    /**
     * 根据staffId查询用户信息
     * @param yxtStaffQueryVo
     * @return
     */
    @PostMapping("/service/114/staff/yxt/list")
    ResultObject<List<StaffResp>> getUserInfo(@RequestBody YxtStaffQueryVo yxtStaffQueryVo);

}

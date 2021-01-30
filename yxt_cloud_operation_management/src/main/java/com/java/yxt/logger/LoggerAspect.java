package com.java.yxt.logger;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.MyBaseMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.feign.StaffOpLogRedisFeign;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.logger.feign.entity.StaffOperationLog;
import com.java.yxt.util.StringUtil;
import com.java.yxt.util.ValidateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class LoggerAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private StaffOpLogRedisFeign staffOpLogRedisFeign;
	// @Value("${spring.datasource.url}")
	private String dbName = "";
	@Autowired
	private RedissonClient redisUtil;

	@Pointcut("execution(* com.java.yxt.controller.*.*(..))")
	public void page() {
	}

	@Pointcut(value = "@annotation(com.java.yxt.logger.SOLog)")
	public void logger() {
	}

	@Around("logger()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Object obj = null;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SOLog soLog = method.getAnnotation(SOLog.class);
		PostMapping post = method.getAnnotation(PostMapping.class);
		PutMapping put = method.getAnnotation(PutMapping.class);
		DeleteMapping del = method.getAnnotation(DeleteMapping.class);
//        if(post != null){
//
//        }else if(put!=null){
//
//        }else if(del !=null){
//
//        }else{
//
//        }
////        //修改传入的pageNum和pageSize的值避免为空，
////        if(args.length==3){
////            if(args[0]==null)
////                args[0]= 1;
////            if(args[1]==null)
////                args[1] = 10;
////        }

		StaffOperationLog log = new StaffOperationLog();
		// 从当前线程中获取到request
		HttpServletRequest request = getRequest();
		String token = request.getHeader("access-token");
//            token ="DbNkWjHOQdoFtqRJzrIYWeHPbSPWq/xVKZ4VO4t4eyruy6S6Nv1AYg==";
		if (StringUtil.isBlank(token)) {
			// 若消息头无法获取token，以参数形式获取
			Object object = args[0];
			cn.hutool.json.JSONObject json = JSONUtil.parseObj(object);
			token = json.getStr("token");
		}
		if (StringUtil.isNotBlank(token)) {
			RBucket<String> bucket = redisUtil.getBucket(token);
			Admin admin = JSONObject.parseObject(bucket.get(), Admin.class);
			if (ValidateUtil.isEmpty(admin)) {
				logger.error("token失效，token:{}", token);
				throw new MySelfValidateException(ValidationEnum.TOKEN_REDIS_ADMIN_EMPTY);
			}
			Long siteId = admin.getSiteId();
			log.setExtendName(admin.getName());
			log.setSiteid(siteId);
			log.setOrgId(admin.getOrgId());
			log.setStaffName(admin.getRealname());
			log.setDepartmentId(admin.getDepartmentId());
		}

		/**
		 * 处理带有@SOLog注解的方法
		 */
		// 修改操作需要保存旧值
		if (soLog != null && args.length > 0) {
			// 根据方法第一个参数查询旧值
			String paramKey = soLog.paramKey();
			if (soLog.type() == OperationType.put) {
				Object str = ((JSONObject) JSONObject.toJSON(args[0])).get(paramKey);
				log.setObjectid(Long.parseLong(String.valueOf(str)));
				try {
					// 从spring容器中获取到操作对应数据库的实例
//						String mapperStr = getMapperStr(soLog) + "Mapper";
					String mapperStr = soLog.mapperName();
//						Object ncDepartmentMapper = ((CommonMapper) ApplicationContextProvider.getBean(mapperStr)).selectByPrimaryKey((Long) str);
					//
					// 继承MyBaseMapper 接口，得到的一个实现类map,key为首字母小写的dao层面接口名，所以mapperStr为首字母小写，
					// 否则取不到走else部分
					Object ncDepartmentMapper = "";
					Map<String, MyBaseMapper> result = ApplicationContextProvider.getApplicationContext()
							.getBeansOfType(MyBaseMapper.class);
					MyBaseMapper myBaseMapper = result.get(mapperStr);
					if (ValidateUtil.isNotEmpty(myBaseMapper)) {
						ncDepartmentMapper = myBaseMapper.selectById(soLog.tableName(), (String) str);
					} else {
						if(str instanceof Long){
							ncDepartmentMapper = ((BaseMapper) ApplicationContextProvider.getBean(mapperStr))
									.selectById((Long) str);
						}
						if(str instanceof String){
							ncDepartmentMapper = ((BaseMapper) ApplicationContextProvider.getBean(mapperStr))
									.selectById(str.toString());
						}

					}
					String origanvalue = ((JSONObject) JSONObject.toJSON(ncDepartmentMapper)).toJSONString();
					log.setOriginValue(origanvalue);
				} catch (Exception e) {
					logger.error("未获取到对应的mapper对象,异常：",e);
				}
			}
		}
		// 执行代理的方法
		obj = joinPoint.proceed(args);
		if (soLog != null) {
			if (args.length > 0) {
				if (soLog.type() == OperationType.delete) {
					String paramKey = soLog.paramKey();
					if(ValidateUtil.isNotEmpty(paramKey)){
						Object str = ((JSONObject) JSONObject.toJSON(args[0])).get(paramKey);
						log.setObjectid(Long.valueOf(str.toString()));
					}

					if (args[0] instanceof List) {
						if(log.getObjectid()==null){
							log.setObjectid((Long) ((List) args[0]).get(0));
						}
					} else {
						if(log.getObjectid()==null){
							log.setObjectid((Long) args[0]);
						}
					}

				} else if (soLog.type() == OperationType.post) {
					try {
//							Object data = ((ResultObject) obj).getData();
//							if (data instanceof Long) {
//								log.setObjectid((Long) data);
//							} else if (data instanceof String) {
//								log.setObjectid(Long.valueOf((String) data));
//							} else if (data instanceof Integer) {
//								log.setObjectid(Long.valueOf((Integer) data));
//							}
					} catch (Exception e) {
						logger.error("新增类型日志未传输新增数据的主键id", e);
					}
				} else if (soLog.type() == OperationType.notDef) {
					args[0] = "{}";
				}

				// 根据第一个参数作为新值
				String str = JSONObject.toJSON(args[0]).toString();
				log.setNewValue(str);
			}
			OperationType type = soLog.type();
			String tableName = soLog.tableName();
			String remark = soLog.remark();
			log.setRemark(remark);
			log.setOperateType(type.getValue());
			log.setDbName(dbName);
//				log.setDbName(getDbName());
			log.setTableName(tableName);
			staffOpLogRedisFeign.postLog(log);
		}
		return obj;
	}

	private HttpServletRequest getRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		return sra.getRequest();
	}

//	private String getDbName() {
//		return dbName.split(":3306/")[1].split("[?]")[0];
//	}

	private String getMapperStr(SOLog soLog) {
		String[] split = soLog.tableName().split("[_]");
		String mapperStr = "";
		for (int i = 0; i < split.length; i++) {
			if (i == 0) {
				mapperStr += BeanNameTool.toLowerCaseFirstOne(split[0]);
			} else {
				mapperStr += BeanNameTool.toUpperCaseFirstOne(split[i]);
			}
		}
		return mapperStr;
	}

}

package com.hflw.vasp.framework.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.entity.BaseEntity;
import com.hflw.vasp.enums.RequestSaveMethod;
import com.hflw.vasp.framework.entity.SysLogEntity;
import com.hflw.vasp.utils.DateUtils;
import com.hflw.vasp.utils.IpUtils;
import com.hflw.vasp.utils.SessionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class SysLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Value("${project.package.prefix:com.glsx}")
    private String packagePrefix;

//    @Autowired
//    private MongoTemplate mongoTemplate;

    @Pointcut("@annotation(com.hflw.vasp.annotation.SysLog)")
    public void logPointCut() {
    }

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();


    @Before("logPointCut()")
    public void saveLog(JoinPoint joinPoint) {
        Object user = null;
        //saveSysLog(joinPoint, (BaseEntity) user);
    }

    /**
     * 供后台和基础服务调用系统日志保存
     *
     * @param joinPoint
     */
    public void saveSysLog(JoinPoint joinPoint, BaseEntity baseEntity) {
        HttpServletRequest request = SessionUtils.request();
        //获取方法信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object target = joinPoint.getTarget();

        SysLog sysLogMark = method.getAnnotation(SysLog.class);
        Map<String, String[]> parameterMap = request.getParameterMap();
        String modul = target.getClass().getName() + "." + methodSignature.getName();
        if (sysLogMark.printRequestParams()) {
            logger.info(modul + " 方法参数为:" + JSONObject.toJSONString(parameterMap));
        }

        String remortIP = IpUtils.getIpAddr(request);

        if (sysLogMark.saveLog()) {
            //创建日志类
            SysLogEntity sysLog = new SysLogEntity();
            try {
                sysLog.setCreateTime(new Date());
                sysLog.setIp(remortIP);

//                Integer userId = SessionUtil.getUserId(visitChannel);
                if (baseEntity != null) {
                    sysLog.setOperator(baseEntity.getId());
                }

                Signature signature = joinPoint.getSignature();
                sysLog.setModul(modul);

                //保存特定信息
                boolean saveRequest = sysLogMark.saveRequest();
                if (saveRequest) {
                    RequestSaveMethod requestSaveMethod = sysLogMark.saveRequestMethod();
                    if (requestSaveMethod == RequestSaveMethod.REQUEST) {
                        sysLog.setRequestData(JSONObject.toJSONString(parameterMap));
                    } else if (requestSaveMethod == RequestSaveMethod.REFLECT) {
                        //使用反射保存请求数据参数
                        Object[] args = joinPoint.getArgs();
                        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                        if (ArrayUtils.isNotEmpty(args)) {
                            JSONObject jsonObject = new JSONObject();
                            for (int i = 0; i < args.length; i++) {
                                Object arg = args[i];
                                String argName = parameterNames[i];
                                if (arg == null) {        //空参记录
                                    jsonObject.put(argName, null);
                                } else {
                                    //只取项目中的类或原始型和基本类型
                                    saveClassRequestData(jsonObject, arg, argName);
                                }
                            }
                            sysLog.setRequestData(jsonObject.toJSONString());
                        }
                    }

                }
            } catch (Exception e) {
                logger.error("保存日志出错", e);
            } finally {
                logger.info("记录日志,保存数据库 TODO :0" + sysLog);
//                sysLogMapper.insert(sysLog);
//                mongoTemplate.insert(sysLog);
            }
        }
    }

    /**
     * 打印方法执行时间
     */
    @Around("logPointCut()")
    public Object printExecutorTime(ProceedingJoinPoint jp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法,执行失败后会抛出异常,也就不会打印执行时间
        Object result = jp.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Object target = jp.getTarget();
        Method method = methodSignature.getMethod();
        //获取注解上的文字
        SysLog sysLog = method.getAnnotation(SysLog.class);
        String methodName = methodSignature.getName();
        if (sysLog.printSpendTime()) {
            logger.info(target.getClass().getName() + "." + methodName + " 执行耗时:" + DurationFormatUtils.formatDuration(time, "HH:mm:ss.S") + "; 精确时间为 :" + time + " ms");
        }
        return result;
    }

    /**
     * 只保存本类的,或原始型数据到请求数据
     *
     * @param jsonObject
     * @param arg
     * @param argName
     */
    private void saveClassRequestData(JSONObject jsonObject, Object arg, String argName) {
        Class<?> clazz = arg.getClass();
        boolean primitiveOrWrapper = org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper(clazz);
        if (primitiveOrWrapper || clazz == String.class) {
            jsonObject.put(argName, arg);
        } else if (clazz == Date.class) {
            Date date = (Date) arg;
            jsonObject.put(argName, DateUtils.formatDateTime(date));
        } else if (clazz.isArray() || (arg instanceof List) || (arg instanceof Map)) {
            if (clazz.isArray()) {
                Object[] array = (Object[]) arg;
                if (ArrayUtils.isEmpty(array)) {
                    jsonObject.put(argName, arg);
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                jsonObject.put(argName, jsonArray);

                //解析  jsonArray
                saveClassRequestData(jsonArray, array);
            } else if (arg instanceof List) {
                List<Object> list = (List<Object>) arg;
                if (CollectionUtils.isEmpty(list)) {
                    jsonObject.put(argName, arg);
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                jsonObject.put(argName, jsonArray);

                //解析  jsonArray
                saveClassRequestData(jsonArray, list.toArray());
            } else if (arg instanceof Map) {
                // map 不处理了,直接放进去 TODO
                jsonObject.put(argName, arg);
            }
        } else {
            String packageName = ClassUtils.getPackageName(clazz);
            if (packageName.startsWith(packagePrefix)) {
                //只记录本包下的参数数据
                jsonObject.put(argName, arg);
            }
        }
    }

    /**
     * 保存数组数据
     *
     * @param jsonArray
     * @param array
     */
    private void saveClassRequestData(JSONArray jsonArray, Object[] array) {
        if (ArrayUtils.isEmpty(array)) return;
        for (Object o : array) {
            if (o == null) {
                jsonArray.add(null);
                continue;
            }

            Class<?> clazz = o.getClass();
            boolean primitiveOrWrapper = org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper(clazz);
            if (primitiveOrWrapper || clazz == String.class) {
                jsonArray.add(o);
            } else if (clazz == Date.class) {
                Date date = (Date) o;
                jsonArray.add(DateUtils.formatDateTime(date));
            } else {
                String packageName = ClassUtils.getPackageName(clazz);
                if (packageName.startsWith(packagePrefix)) {
                    //只记录本包下的参数数据
                    jsonArray.add(o);
                }
            }
        }
    }

    /**
     * 判断参数是否需要记录
     *
     * @param clazz
     * @return
     */
    private boolean paramLogSupportClass(Class clazz) {
        boolean primitiveOrWrapper = org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper(clazz);
        //记录原始型
        if (primitiveOrWrapper) {
            return true;
        }
        //记录 String 和 Date 类型
        if (clazz == String.class || clazz == Date.class) {
            return true;
        }
        //记录本包的类型
        String packageName = ClassUtils.getPackageName(clazz);
        if (packageName.startsWith(packagePrefix)) {
            return true;
        }

        return false;
    }

}

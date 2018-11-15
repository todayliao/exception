
package com.exception.qms.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Aspect
@Component
@Slf4j
public class OperatorLogAspect {

    @Pointcut("@annotation(com.exception.qms.aspect.OperatorLog)")
    public void operatorLog() {
    }

    @Before("operatorLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            String description = getSystemLogDescription(joinPoint);

            log.info("============<执行开始>======== " + description + "请求参数" + getSystemParam(joinPoint) + "===========================");
        } catch (Exception e) {
            log.error("doBefore error {}", e);
        }
    }

    private String getSystemParam(JoinPoint joinPoint) {
        return JSON.toJSONString(joinPoint.getArgs());
    }


    @Around("operatorLog()")
    public Object doSurround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] objects = proceedingJoinPoint.getArgs();
        String name = proceedingJoinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();

        log.warn("当前类：{}, 方法名：{}", proceedingJoinPoint.getTarget().getClass().getName(), name);
        if (objects != null) {
            for (int i = 0; i < objects.length; i++) {
                if (null != objects[i]) {
                    log.warn("第" + (i + 1) + "个方法入参数{}： <{}>", objects[i].getClass().getName(), JSON.toJSONString(objects[i]));
                }
            }
        }
        log.warn("出参: <{}>", JSON.toJSONString(result));
        log.warn(name + "方法的执行时间为: {}", System.currentTimeMillis() - startTime + "毫秒");
        return result;
    }

    @After(value = "operatorLog()")
    public void doAfter(JoinPoint joinPoint) {
        try {
            log.info("=======操作调用" + getSystemLogDescription(joinPoint) + "===<执行完成>================");
        } catch (Exception e) {
            log.error("doAfter error {}", e);
        }
    }

    /**
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getSystemLogDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(OperatorLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }

}

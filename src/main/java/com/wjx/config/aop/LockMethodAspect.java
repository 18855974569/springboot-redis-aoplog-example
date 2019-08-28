package com.wjx.config.aop;

import com.wjx.config.annotation.RedisLock;
import com.wjx.config.common.Result;
import com.wjx.config.exception.BizErrorCodeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: dingguo
 * @Date: 2019/8/27 下午3:42
 */
@Aspect
@Component
@Order(2)
public class LockMethodAspect {
    private static final Logger logger = LoggerFactory.getLogger(LockMethodAspect.class);

    @Autowired
    private StringRedisTemplate lockRedisTemplate;

    // execution(public * *(..)) &&
    @Pointcut("@annotation(com.wjx.config.annotation.RedisLock)")
    public void controllerMethodPointcut() {
    }

    @Around("controllerMethodPointcut()")
    public ResponseEntity interceptor(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        RedisLock lock = method.getAnnotation(RedisLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key can't be null...");
        }
        final String lockKey = lock.prefix() + lock.name();

        Object result;
        boolean checkResult;

        //构建JSON
        try {
            //key不存在才能设置成功
            final Boolean success = lockRedisTemplate.opsForValue().setIfAbsent(lockKey, "");
            if (success != null && success) {
                checkResult = true;
                lockRedisTemplate.expire(lockKey, lock.timeExpire(), lock.timeUnit());
            } else {
                checkResult = false;
            }
            if (checkResult) {
                result = pjp.proceed();
            } else {
                result = new ResponseEntity(new Result(BizErrorCodeEnum.REDIS_HAD_LOCK), HttpStatus.OK);
            }
        } finally {
            //如果演示的话需要注释该代码;实际应该放开
            lockRedisTemplate.delete(lockKey);
        }
        return (ResponseEntity) result;
    }
}

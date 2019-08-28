package com.wjx.config.annotation;

import com.wjx.config.common.RedisLockConstant;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redis锁的注解
 * @Author: dingguo
 * @Date: 2019/8/27 下午3:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {

    /**
     * redis锁 key 的前缀
     *
     * @return
     */
    String prefix() default RedisLockConstant.REDIS_DEFAULT_PREFIX;

    /**
     * redis锁的名称
     *
     * @return
     */
    String name();

    /**
     * 过期的秒数，默认5秒
     *
     * @return
     */
    int timeExpire() default 5;

    /**
     * 超时时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * key的分隔符
     *
     * @return
     */
    String delimiter() default ":";
}

package com.wjx.config.aop;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Aspect
@Configuration//定义一个切面
@Order(1)
public class LogRecordAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);

    private static final List REQUEST_METHOD = Lists.newArrayList("POST", "PUT", "DEL");

    // 定义切点Pointcut
    @Pointcut("execution(public * com.wjx.web..*(..))")
    public void excudeService() {
    }


    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        // 非url的传入对象
        Object requestParams = null;
        //获取请求参数集合并进行遍历拼接
        if (args.length > 0) {
            if (REQUEST_METHOD.contains(method)) {
                Object object = args[0];
                requestParams = getKeyAndValue(object);
            }
        }

        long startTime = System.currentTimeMillis();
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        if (result instanceof ResponseEntity) {
            long endTime = System.currentTimeMillis();
            ResponseEntity responseEntity = (ResponseEntity) result;
            RequestMessage message = getAccessLog(method, uri, queryString, requestParams, response.getStatus(), (endTime - startTime), responseEntity.getBody());
            if (message.getInput() == null && message.getOutput() == null) {
                logger.info("{}", message.getRequest());
            } else if (message.getInput() == null) {
                logger.info("{}\nResponse: {}", message.getRequest(), message.getOutput());
            } else {
                logger.info("{}\nRequest: {}\nResponse: {}", message.getRequest(), message.getInput(), message.getOutput());
            }

        }
        return result;
    }

    private static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    private RequestMessage getAccessLog(String method, String url, String queryParams, Object requestParams, Integer status, long time, Object result) {
        RequestMessage message = new RequestMessage();
        StringJoiner join = new StringJoiner(" ");
        join.add(method);

        if (queryParams != null) {
            join.add(url + "?" + queryParams);
        } else {
            join.add(url);
        }

        join.add(String.valueOf(status));
        join.add(String.valueOf(time + "ms"));

        message.setRequest(join.toString());
        if (requestParams != null) {
            message.setInput(JSONObject.toJSONString(requestParams));
        }
        if (result != null) {
            message.setOutput(JSONObject.toJSONString(result));
        }

        return message;
    }

    class RequestMessage {
        private String request;
        private Object input;
        private Object output;

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public Object getInput() {
            return input;
        }

        public void setInput(Object input) {
            this.input = input;
        }

        public Object getOutput() {
            return output;
        }

        public void setOutput(Object output) {
            this.output = output;
        }
    }
}
package com.wjx.config.exception;

import com.alibaba.fastjson.JSONObject;
import com.wjx.config.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理器
 *
 * @author dingguo
 * @date 2019/08/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 Exception 异常
     *
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(Exception e) {
        logger.error("服务错误:", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * 处理 BusinessException 异常
     *
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ResponseEntity businessExceptionHandler(BizException e) {
        Result result = new Result(e.getErrorCode().getCode(), e.getErrorCode().getMsg(), null);
        logger.info(JSONObject.toJSONString(result));
        return new ResponseEntity(result, HttpStatus.EXPECTATION_FAILED);
    }
}

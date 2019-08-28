package com.wjx.config.exception;


/**
 * @Description: 自定义业务异常
 * @Author: dingguo
 * @Date: 2019/8/27 下午3:45
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -7864604160297181941L;

    /**
     * 错误码
     */
    private final BizErrorCodeEnum errorCode;


    /**
     * 无参默认构造UNSPECIFIED
     */
    public BizException() {
        super(BizErrorCodeEnum.UNSPECIFIED.getMsg());
        this.errorCode = BizErrorCodeEnum.UNSPECIFIED;
    }

    public BizException(BizErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailedMessage 详细描述
     */
    public BizException(final String detailedMessage) {
        super(detailedMessage);
        this.errorCode = BizErrorCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param t 导火索
     */
    public BizException(final Throwable t) {
        super(t);
        this.errorCode = BizErrorCodeEnum.UNSPECIFIED;
    }


    /**
     * 构造通用异常
     *
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public BizException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = BizErrorCodeEnum.UNSPECIFIED;
    }

    public BizErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
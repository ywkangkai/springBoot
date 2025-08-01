package com.weaver.accurate.common;


/**
 *
 */
public class BizException extends BaseException {
    public BizException(Code code) {
        super(code);
    }

    public BizException(Code code, Throwable e) {
        super(code, e);
    }

    public BizException(int code, String msg) {
        super(code, msg);
    }

    public BizException(int code, String msg, Throwable e) {
        super(code, msg, e);
    }
}

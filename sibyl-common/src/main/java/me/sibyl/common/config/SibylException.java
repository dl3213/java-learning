package me.sibyl.common.config;

import lombok.Data;
import me.sibyl.common.domain.BaseInfoInterface;

/**
 * @Classname SibylException
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/25 18:36
 */
@Data
public class SibylException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected int code;
    /**
     * 错误信息
     */
    protected String msg;

    public SibylException() {
        super();
    }

    public SibylException(BaseInfoInterface errorInfoInterface) {
        super(errorInfoInterface.msg());
        this.code = errorInfoInterface.code();
        this.msg = errorInfoInterface.msg();
    }

    public SibylException(BaseInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.msg(), cause);
        this.code = errorInfoInterface.code();
        this.msg = errorInfoInterface.msg();
    }

    public SibylException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SibylException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public SibylException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

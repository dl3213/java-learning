package me.sibyl.advice;

import lombok.Data;

@Data
public class RepeatSubmitException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected int code;
    /**
     * 错误信息
     */
    protected String msg;

    public RepeatSubmitException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


}

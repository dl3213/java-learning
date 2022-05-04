package me.sibyl.cas.base.domain;

/**
 * @Classname CommonEnum
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/25 18:39
 */

public enum CommonEnum implements BaseInfoInterface {
    // 数据操作错误定义
    SUCCESS(200, "成功"),
    BODY_NOT_MATCH(400,"请求的数据格式不符"),
    SIGNATURE_NOT_MATCH(401,"认证失败"),
    NOT_FOUND(404, "未找到该资源"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试")
    ;

    /** 错误码 */
    private int resultCode;

    /** 错误描述 */
    private String resultMsg;

    CommonEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public int code() {
        return resultCode;
    }

    @Override
    public String msg() {
        return resultMsg;
    }

}

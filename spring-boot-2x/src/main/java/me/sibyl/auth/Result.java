package me.sibyl.auth;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Objects;

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";
    public static final String success_Count = "successCount";
    public static final String fail_Count = "failCount";
    public static final String messCell_Count = "messCellCount";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功
         */
        SUCCESS(200),
        /**
         * 警告
         */
        WARN(300),

        /**
         * 失败
         */
        FAIL(400),

        /**
         * 错误
         */
        ERROR(500),

        /**
         * 权限不足
         */
        UNAUTH(600),

        /**
         * token认证失败
         */
        AUTH(700);

        private final int value;


        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     *
     * @param type
     * @param msg
     * @param successCount
     * @param failCount
     * @param messCellCount
     * @param data
     */
    public Result(Type type, String msg, int successCount, int failCount, int messCellCount, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
        super.put(success_Count, successCount);
        super.put(fail_Count, failCount);
        super.put(messCell_Count, messCellCount);

    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public Result(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Result(Type type, Object msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Result success() {
        return success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static Result success(Object data) {
        return success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Result success(String msg) {
        return success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static Result success(String msg, Object data) {
        return new Result(Type.SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg           返回内容
     * @param data          数据对象
     * @param successCount  成功数据集条数
     * @param failCount     失败条数条数
     * @param messCellCount 异常单元格条数
     * @return 成功消息
     */
    public static Result success(String msg, int successCount, int failCount, int messCellCount, Object data) {
        return new Result(Type.SUCCESS, msg, successCount, failCount, messCellCount, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg           返回内容
     * @param data          数据对象
     * @param successCount  成功数据集条数
     * @param failCount     失败条数条数
     * @param messCellCount 异常单元格条数
     * @return 失败消息
     */
    public static Result error(String msg, int successCount, int failCount, int messCellCount, Object data) {
        return new Result(Type.ERROR, msg, successCount, failCount, messCellCount, data);
    }


    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result warn(String msg) {
        return warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result warn(String msg, Object data) {
        return new Result(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Result error() {
        return error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result error(Object msg) {
        return error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result error(Object msg, Object data) {
        return new Result(Type.ERROR, msg, data);
    }

    /**
     * 返回权限不足消息
     *
     * @return
     */
    public static Result unauth() {
        return error("权限不足");
    }

    /**
     * 返回权限不足消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result unauth(String msg) {
        return error(msg, null);
    }

    /**
     * 返回无权限信息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result unauth(String msg, Object data) {
        return new Result(Type.UNAUTH, msg, data);
    }

    /**
     * 返回token认证失败消息
     *
     * @return
     */
    public static Result auth() {
        return error("token认证失败");
    }

    /**
     * 返回token认证失败消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result auth(String msg) {
        return error(msg, null);
    }

    /**
     * 返回token认证失败消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result auth(String msg, Object data) {
        return new Result(Type.AUTH, msg, data);
    }

    /**
     * 返回失败消息
     *
     * @return
     */
    public static Result fail() {
        return fail("操作失败");
    }

    /**
     * 返返回失败消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result fail(String msg) {
        return fail(msg, null);
    }

    /**
     * 返回失败消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result fail(String msg, Object data) {
        return new Result(Type.FAIL, msg, data);
    }

}
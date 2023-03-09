package me.sibyl.common.response;

import lombok.Data;
import me.sibyl.common.domain.CommonEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ResponseUtil
 * @Description TODO
 * @Date 2021/7/27 20:47
 * @Created by dyingleaf3213
 */
@Data
public class Response extends HashMap<String, Object> {

    private static String DATA_NAME = "data";
    private static String CODE_NAME = "code";
    private static String MSG_NAME = "msg";

    public Response() {
        put(CODE_NAME, 200);
        put(MSG_NAME, "操作成功");
        put(DATA_NAME, null);
    }

    public static Response success() {
        return new Response();
    }

//    public static Response success(String msg) {
//        Response responseVO = new Response();
//        responseVO.put(MSG_NAME, msg);
//        return responseVO;
//    }

    public static Response success(Object data) {
        Response responseVO = new Response();
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static Response success(int code, String msg) {
        Response responseVO = new Response();
        responseVO.put(MSG_NAME, msg);
        responseVO.put(CODE_NAME, code);
        return responseVO;
    }

    public static Response success(String msg, Object data) {
        Response responseVO = new Response();
        responseVO.put(MSG_NAME, msg);
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static Response success(Integer code, Object data) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME, 200);
        responseVO.put(MSG_NAME, "操作成功");
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static Response success(Map<String, Object> map) {
        Response responseVO = new Response();
        responseVO.putAll(map);
        return responseVO;
    }

    public static Response success(int i, String msg, Object data) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME,i);
        responseVO.put(MSG_NAME,msg);
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }


    public static Response error(CommonEnum internalServerError) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME, internalServerError.code());
        responseVO.put(MSG_NAME, internalServerError.msg());
        return responseVO;
    }

    public static Response error(String msg) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME, 404);
        responseVO.put(MSG_NAME, msg);
        return responseVO;
    }

    public static Response error(int code, String msg) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME, code);
        responseVO.put(MSG_NAME, msg);
        return responseVO;
    }

}
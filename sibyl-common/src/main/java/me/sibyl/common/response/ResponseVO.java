package me.sibyl.common.response;

import lombok.Data;
import me.sibyl.common.domain.CommonEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname ResponseUtil
 * @Description TODO
 * @Date 2021/7/27 20:47
 * @Created by dyingleaf3213
 */
@Data
public class ResponseVO extends HashMap<String, Object> {

    private static String DATA_NAME = "data";
    private static String CODE_NAME = "code";
    private static String MSG_NAME = "msg";

    public ResponseVO() {
        put(CODE_NAME, 200);
        put(MSG_NAME, "操作成功");
        put(DATA_NAME, null);
    }

    public static ResponseVO success() {
        return new ResponseVO();
    }

    public static ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(MSG_NAME, msg);
        return responseVO;
    }
    public static ResponseVO success(Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static ResponseVO success(int code, String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(MSG_NAME, msg);
        responseVO.put(CODE_NAME, code);
        return responseVO;
    }

    public static ResponseVO success(String msg, Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(MSG_NAME, msg);
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static ResponseVO success(Integer code, Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(CODE_NAME, 200);
        responseVO.put(MSG_NAME, "操作成功");
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static ResponseVO success(Map<String, Object> map) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.putAll(map);
        return responseVO;
    }

    public static ResponseVO success(int i, String msg, Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(CODE_NAME,i);
        responseVO.put(MSG_NAME,msg);
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }


    public static ResponseVO error(CommonEnum internalServerError) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(CODE_NAME, internalServerError.code());
        responseVO.put(MSG_NAME, internalServerError.msg());
        return responseVO;
    }

    public static ResponseVO error(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(CODE_NAME, 404);
        responseVO.put(MSG_NAME, msg);
        return responseVO;
    }

    public static ResponseVO error(int code,  String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.put(CODE_NAME, code);
        responseVO.put(MSG_NAME, msg);
        return responseVO;
    }

}
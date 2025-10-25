package code.sibyl.common;

import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 *  通用返回格式
 */
@Data
@ToString
public class Response extends HashMap<String, Object> {

    private static String DATA_NAME = "data";
    private static String CODE_NAME = "code";
    private static String MSG_NAME = "message";

    public Response() {
        put(CODE_NAME, 200);
        put(MSG_NAME, "success");
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
        responseVO.put(MSG_NAME, "success");
        responseVO.put(DATA_NAME, data);
        return responseVO;
    }

    public static Response successPage(long total, Object data, int pageNumber, int pageSize) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME, 200);
        responseVO.put(MSG_NAME, "success");
        responseVO.put(DATA_NAME, data);
        responseVO.put("total", total);
        responseVO.put("pageNumber", pageNumber);
        responseVO.put("pageSize", pageSize);
        return responseVO;
    }

//    public static Response success(Map<String, Object> map) {
//        Response responseVO = new Response();
//        responseVO.putAll(map);
//        return responseVO;
//    }

    public static Response success(int i, String msg, Object data) {
        Response responseVO = new Response();
        responseVO.put(CODE_NAME,i);
        responseVO.put(MSG_NAME,msg);
        responseVO.put(DATA_NAME, data);
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

    public static Mono<Response> successMono() {
        return Mono.just(success());
    }
}
package code.sibyl.config;


import code.sibyl.controller.rest.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Mono<ApiResponse<String>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error(
                "文件太大",
                "上传的文件大小超过限制"
        ));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<String>> handleIOException(IOException e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error(
                "文件IO错误",
                e.getMessage()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error(
                "参数错误",
                e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<String>> handleGeneralException(Exception e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error(
                "服务器内部错误",
                e.getMessage()
        ));
    }
}

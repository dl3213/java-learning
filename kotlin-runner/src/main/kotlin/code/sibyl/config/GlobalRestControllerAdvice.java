package code.sibyl.config;

import code.sibyl.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public Response exceptionHandler(WebExchangeBindException exception) {
        exception.printStackTrace();
        BindingResult bindingResult = exception.getBindingResult();

        String msg = null;
        if (bindingResult.hasErrors()) {
            msg = bindingResult.getAllErrors()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining(";"));
        }
        return Response.error(500, msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return Response.error(500, exception.getMessage());
    }
}

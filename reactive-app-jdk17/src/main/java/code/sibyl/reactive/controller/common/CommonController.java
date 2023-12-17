package code.sibyl.reactive.controller.common;

import code.sibyl.reactive.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    @GetMapping("/test")
    public Response test() {
        return Response.success();
    }
}

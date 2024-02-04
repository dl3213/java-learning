package code.sibyl.controller;

import code.sibyl.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/external")
@Slf4j
public class TestController {
    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Response test() {
        try {
            return Response.success(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}

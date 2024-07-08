package code.sibyl.controller;

import code.sibyl.common.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final AsyncTaskExecutor asyncTaskExecutor;

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Response test() {
        System.err.println(Thread.currentThread().isVirtual());

        asyncTaskExecutor.submitCompletable(()->{
            System.err.println("asyncTaskExecutor test");
            System.err.println(Thread.currentThread());
            System.err.println(Thread.currentThread().getName());
            System.err.println(Thread.currentThread().isVirtual());
        }).join();

        return Response.success("https://xn--0704-1yjs01cc-1o1uh94bqa577wbi6h.yjs11.cfd/?m=play&u=xnxx&k=187md9ba&p=");
    }
}

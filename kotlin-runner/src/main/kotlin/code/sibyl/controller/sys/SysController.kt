package code.sibyl.controller.sys

import code.sibyl.KotlinApplication
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

@Controller
@RequestMapping("/sys")
class SysController {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)


    @GetMapping("/main-view")
    fun index_view(model: Model): Mono<String?> {
        return Mono.create { monoSink: MonoSink<String?> ->
            monoSink.success(
                "default/sys/main-view"
            )
        }
    }


}
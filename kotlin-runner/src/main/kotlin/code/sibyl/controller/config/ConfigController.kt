package code.sibyl.controller.config

import code.sibyl.common.DataBaseTypeEnum
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

@Controller
@RequestMapping("/config")
class ConfigController {

    @GetMapping("/main-view")
    fun index_view(model: Model): Mono<String?> {
        return Mono.create { monoSink: MonoSink<String?> ->
            monoSink.success(
                "default/config/main-view"
            )
        }
    }


}
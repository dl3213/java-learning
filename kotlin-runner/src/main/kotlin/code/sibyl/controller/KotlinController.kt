package code.sibyl.controller

import code.sibyl.common.Response
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import kotlinx.coroutines.*

@RestController
@RequestMapping("/api/kotlin")
@Slf4j
@RequiredArgsConstructor
class KotlinController {

    @RequestMapping(value = ["/test"], method = [RequestMethod.GET, RequestMethod.POST])
    fun test(): Response {
        GlobalScope.launch(Dispatchers.IO) {
            val arg1 = sunpendF1()
            var arg2 = sunpendF2()
            System.err.println(Thread.currentThread().toString())
            println("suspend finish arg1:$arg1  arg2:$arg2  result:${arg1 + arg2}")
        }
        return Response.success(Thread.currentThread().toString())
    }
}

private suspend fun sunpendF1(): Int {
    delay(1000)
    println("suspend fun 1")
    System.err.println(Thread.currentThread().toString())
    return 2
}

private suspend fun sunpendF2(): Int {
    delay(1000)
    println("suspend fun 2")
    System.err.println(Thread.currentThread().toString())
    return 4
}


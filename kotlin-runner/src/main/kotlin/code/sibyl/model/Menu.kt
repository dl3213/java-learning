package code.sibyl.model

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

data class Menu(
    var id: Long?,
    var code: String?,
    var name: String?,
    var linkUrl: String?,
    var children: List<MenuDTO>?
) {
    constructor() : this(null, null, null, null, null)

    fun id(id: Long?): Menu {
        this.id = id;
        return this;
    }

    fun code(code: String?): Menu {
        this.code = code;
        return this;
    }

    fun name(name: String?): Menu {
        this.name = name;
        return this;
    }

    fun linkUrl(linkUrl: String?): Menu {
        this.linkUrl = linkUrl;
        return this;
    }

    fun children(children: List<MenuDTO>?): Menu {
        this.children = children;
        return this;
    }
}

fun main12() {
    var just = Flux.just(1, 2, 3, 4)
    just.publishOn(Schedulers.single()).map { e -> println(e) }.doOnComplete { println("end") }.subscribeOn(Schedulers.boundedElastic())
    just.publishOn(Schedulers.single()).map { e -> println(e) }.doOnComplete { println("end") }.subscribeOn(Schedulers.boundedElastic())
}
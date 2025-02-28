package code.sibyl.controller.rest

import cn.hutool.core.lang.Snowflake
import code.sibyl.common.Response
import code.sibyl.common.r
import code.sibyl.common.r.defaultUserId
import code.sibyl.common.r.fileBaseDir
import code.sibyl.common.r.getBean
import code.sibyl.common.r.systemName
import code.sibyl.common.r.yyyy_MM_dd
import code.sibyl.domain.base.BaseFile
import code.sibyl.domain.biz.Book
import code.sibyl.service.BookService
import com.alibaba.fastjson2.JSONObject
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.stream.Stream


@Controller
@RequestMapping("/api/rest/v1/book")
class BookController {

    private val log = LoggerFactory.getLogger(BookController::class.java)

    @Autowired
    @Qualifier("sibyl-postgresql")
    private val r2dbcEntityTemplate: R2dbcEntityTemplate? = null

    @GetMapping("/view.html")
    fun view(model: Model): Mono<String> {
        val welcome = "book/list-view"
        model.addAttribute("systemName", systemName())
        model.addAttribute("title", systemName())
        return Mono.create { monoSink: MonoSink<String> -> monoSink.success(welcome) }
    }

    @GetMapping("/detail/view.html")
    fun detailView(model: Model, @RequestParam("id") id: String?): Mono<String?> {
        System.err.println(id)
        val page = "book/detail-view"
        model.addAttribute("systemName", systemName())
        model.addAttribute("title", systemName())
        model.addAttribute("entityId", id)
        return Mono.create { monoSink: MonoSink<String?> -> monoSink.success(page) }
    }

    @PostMapping("/upload/{id}")
    @ResponseBody
    fun uploadFile(@RequestPart("file") filePartMono: Mono<FilePart>, @PathVariable id: String): Mono<Response> {
        return Mono.zip(
            r2dbcEntityTemplate!!.selectOne(Query.query(Criteria.where("id").`is`(id)), Book::class.java),
            filePartMono
        )
            .flatMap {
                log.info("[book-upload] {} ", it.t1.absolutePath)
                Mono.zip(
                    Mono.just(it),
                    it.t2.transferTo(Paths.get(it.t1.absolutePath + File.separator + it.t2.filename()))
                )
            }
            .then()
            .map { r.success() }
    }



    @GetMapping(value = ["/detail/first/{id}"], produces = [MediaType.IMAGE_JPEG_VALUE])
    @ResponseBody
    fun getFileImage(@PathVariable id: String): Flux<DataBuffer> {

        return r2dbcEntityTemplate!!.selectOne(Query.query(Criteria.where("id").`is`(id)), Book::class.java)
            .flatMap { book ->
                r.getAllFiles(book.absolutePath)
                    .take(1).next()
            }
            .flatMapMany {
                //println(it)
                Paths.get(it).normalize()
                DataBufferUtils.read(FileSystemResource(it), DefaultDataBufferFactory(), 1024)
                    .subscribeOn(Schedulers.boundedElastic())
            }

    }

    @PostMapping(value = ["/detail/page/{id}"])
    @ResponseBody
    fun detailPage(@RequestBody jsonObject: JSONObject, @PathVariable id: String): Mono<Response> {
        val pageNumber = jsonObject.getLong("pageNumber")
        val pageSize = jsonObject.getLong("pageSize")
        return r2dbcEntityTemplate!!.selectOne(Query.query(Criteria.where("id").`is`(id)), Book::class.java)
            .publishOn(Schedulers.boundedElastic())
            .flatMapMany { book ->
                r.getAllFiles(book.absolutePath).flatMap { path -> Mono.zip(Mono.just(book), Mono.just(path)) }
            }
            .skip((pageNumber - 1) * pageSize)  // 跳过前面页的数据
            .take(pageSize)             // 取当前页的数据量
            .map {
                //println("path -> $it")
                var realFile = File(it.t2.toString())
                //println("file -> ${realFile.name}")
                var file = BaseFile()
                file.id = id.toLong()
                file.fileName = realFile.name
                file.realName = realFile.name
                file.absolutePath = it.t2
                file.relativePath = it.t1.relativePath + file.fileName
                file
            }
            .collectList()
            .map { r.success(it) }
    }

    @PostMapping(value = ["/page"])
    @ResponseBody
    fun page(@RequestBody jsonObject: JSONObject): Mono<Response> {
        val isDeleted = jsonObject.getString("isDeleted")
        var criteria = Criteria.where("IS_DELETED").`is`(isDeleted) //.and("type").like("image%");

        val keyword = jsonObject.getString("keyword")
        if (StringUtils.isNotBlank(keyword)) {
            criteria = criteria.and(
                Criteria.empty().and("name").like("%$keyword%")
                    .or("type").like("%$keyword%")
            )
        }

        val pageNumber = jsonObject.getInteger("pageNumber")
        val pageSize = jsonObject.getInteger("pageSize")
        return Mono.zip(Mono.just(criteria), Mono.just(1L))
            .flatMap { tuple: Tuple2<Criteria, Long> ->
                val t1 = tuple.t1
                val sort = Sort.sort(
                    Book::class.java
                ).by(Book::createTime).ascending()
                val query = Query.query(t1)
                    .sort(sort)
                    .with(PageRequest.of(pageNumber - 1, pageSize)) // 0开始
                Mono.zip(
                    r2dbcEntityTemplate!!.count(query, Book::class.java),
                    r2dbcEntityTemplate.select(query, Book::class.java).collectList()
                )
            }
            .map { t: Tuple2<Long?, List<Book>?> ->
                Response.successPage(
                    t.t1!!, t.t2, pageNumber, pageSize
                )
            }
    }

    @PostMapping(value = ["/add"])
    @ResponseBody
    fun add(@RequestBody book: Book): Mono<Response> {

        return BookService.bean.insert(book).map { item: Book? -> Response.success(item) }
    }

    @DeleteMapping(value = ["/delete/{id}"])
    @ResponseBody
    fun delete(@PathVariable id: String): Mono<Response> {
        System.err.println("delete -> $id")
        return r2dbcEntityTemplate!!.selectOne(Query.query(Criteria.where("id").`is`(id)), Book::class.java)
            .switchIfEmpty(Mono.error(RuntimeException("${id}不存在")))
            .flatMap { e: Book ->
                //System.err.println(e.absolutePath)
                e.isDeleted = "1"
                e.updateTime = LocalDateTime.now()
                e.updateId = defaultUserId()
                r2dbcEntityTemplate.update(e)
            }
            .map { e: Book? -> Response.success(e) }
    }
}

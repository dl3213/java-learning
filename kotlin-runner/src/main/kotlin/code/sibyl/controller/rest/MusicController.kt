package code.sibyl.controller.rest

import code.sibyl.common.Response
import code.sibyl.common.r
import code.sibyl.domain.base.BaseFile
import code.sibyl.domain.biz.Book
import code.sibyl.service.sql.PostgresqlService
import com.alibaba.fastjson2.JSONObject
import com.mpatric.mp3agic.Mp3File
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneOffset


@Controller
@RequestMapping("/api/rest/v1/music")
class MusicController {

    private val log = LoggerFactory.getLogger(MusicController::class.java)

    @RequestMapping(
        value = ["/src"],
        method = [RequestMethod.GET, RequestMethod.POST],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun src(@RequestBody jsonObject: JSONObject, exchange: ServerWebExchange): Flux<DataBuffer> {
        return Mono.just(jsonObject)
            .map { it.getString("absolutePath") }
            .flatMapMany {
                Paths.get(it).normalize()
                DataBufferUtils.read(FileSystemResource(it), DefaultDataBufferFactory(), 1024)
                    .subscribeOn(Schedulers.boundedElastic())
            }

    }

    @RequestMapping(
        value = ["/fromFile/src/{id}"],
        method = [RequestMethod.GET, RequestMethod.POST],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun fromFileSrc(  exchange: ServerWebExchange, @PathVariable id: String): Flux<DataBuffer> {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").`is`(id)), BaseFile::class.java)
            .map { it.absolutePath }
            .flatMapMany {
                Paths.get(it).normalize()
                DataBufferUtils.read(FileSystemResource(it!!), DefaultDataBufferFactory(), 1024)
                    .subscribeOn(Schedulers.boundedElastic())
            }

    }

    @RequestMapping(
        value = ["/info"],
        method = [RequestMethod.GET, RequestMethod.POST],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun albumImage(@RequestBody jsonObject: JSONObject, exchange: ServerWebExchange): Mono<Void> {
        return Mono.just(jsonObject)
            .flatMap {
                var absolutePath = it.getString("absolutePath")
                var mp3audio = Mp3File(absolutePath)
                var id3v2Tag = mp3audio.id3v2Tag
                exchange.response.headers.add("Access-Control-Expose-Headers", "artist,title")
                exchange.response.headers.add("artist", r.urlDecode(id3v2Tag.artist));
                exchange.response.headers.add("title", r.urlDecode(id3v2Tag.title));
                exchange.response.headers.add("album", id3v2Tag.album);
                exchange.response.headers.add("year", id3v2Tag.year);
                exchange.response.headers.add("comment", id3v2Tag.comment);
                exchange.response.headers.add("genreDescription", id3v2Tag.genreDescription);
                exchange.response.headers.add("bitrate", mp3audio.bitrate.toString());
                exchange.response.headers.add("sampleRate", mp3audio.sampleRate.toString());
                exchange.response.headers.add("length", mp3audio.length.toString());
                val imageData = mp3audio.id3v2Tag.albumImage
                val buffer: DataBuffer = DefaultDataBufferFactory().wrap(imageData)
                exchange.response.writeWith(Mono.just(buffer))
            }

    }

    @RequestMapping(
        value = ["/fromFile/info/{id}"],
        method = [RequestMethod.GET, RequestMethod.POST],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun fromFileAlbumImage( exchange: ServerWebExchange, @PathVariable id: String): Mono<Void> {
        return  return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").`is`(id)), Book::class.java)
            .flatMap {
                var absolutePath = it.absolutePath
                var mp3audio = Mp3File(absolutePath)
                var id3v2Tag = mp3audio.id3v2Tag
                exchange.response.headers.add("Access-Control-Expose-Headers", "artist,title")
                exchange.response.headers.add("artist", r.urlDecode(id3v2Tag.artist));
                exchange.response.headers.add("title", r.urlDecode(id3v2Tag.title));
                exchange.response.headers.add("album", id3v2Tag.album);
                exchange.response.headers.add("year", id3v2Tag.year);
                exchange.response.headers.add("comment", id3v2Tag.comment);
                exchange.response.headers.add("genreDescription", id3v2Tag.genreDescription);
                exchange.response.headers.add("bitrate", mp3audio.bitrate.toString());
                exchange.response.headers.add("sampleRate", mp3audio.sampleRate.toString());
                exchange.response.headers.add("length", mp3audio.length.toString());
                val imageData = mp3audio.id3v2Tag.albumImage
                val buffer: DataBuffer = DefaultDataBufferFactory().wrap(imageData)
                exchange.response.writeWith(Mono.just(buffer))
            }

    }

    @PostMapping(value = ["/list"])
    @ResponseBody
    fun list(@RequestBody jsonObject: JSONObject): Flux<BaseFile> {

        return r.getAllFiles("E:\\sibyl-system\\file\\mymusic\\")
            .map {
                var realFile = File(it.toString())
                //println("file -> ${realFile.name}")
                var file = BaseFile()
                file.fileName = realFile.name
                file.realName = realFile.name
                file.size = realFile.length()
                file.absolutePath = realFile.absolutePath
                //file.relativePath = "/static-file/mymusic/" + realFile.name;// r.urlDecode(realFile.name)
                file.createTime = Instant.ofEpochMilli(realFile.lastModified()).atZone(ZoneOffset.UTC).toLocalDateTime()

                file
            }
    }

    @PostMapping(value = ["/fromFile/list"])
    @ResponseBody
    fun fromFileList(@RequestBody jsonObject: JSONObject): Mono<Response> {
        jsonObject.put("type","audio");
        return PostgresqlService.getBean().fileQuery(jsonObject)
            .map { tuple ->
                //System.err.println(tuple);
                val response = Response.successPage(tuple.t1, tuple.t2, tuple.t3, tuple.t4)
                response.put("prevUrl", r.staticFileBasePath.replace("**", ""))
                response
            }
    }

    @RequestMapping(
        value = ["/test"],
        method = [RequestMethod.GET, RequestMethod.POST],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun test(exchange: ServerWebExchange): Flux<DataBuffer> {
        return Mono.just(1L)
            .flatMapMany {
                var path = "E:\\sibyl-system\\file\\mymusic\\01.Embracing Me (Mirae & Dazbee).mp3"
                Paths.get(path).normalize()
                DataBufferUtils.read(FileSystemResource(path), DefaultDataBufferFactory(), 1024)
                    .subscribeOn(Schedulers.boundedElastic())
            }

    }

}

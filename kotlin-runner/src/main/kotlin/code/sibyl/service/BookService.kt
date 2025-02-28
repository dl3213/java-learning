package code.sibyl.service

import cn.hutool.core.lang.Snowflake
import code.sibyl.common.Response
import code.sibyl.common.r
import code.sibyl.common.r.getBean
import code.sibyl.common.r.walk
import code.sibyl.controller.rest.BookController
import code.sibyl.domain.biz.Book
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*

@Service
@Slf4j
@RequiredArgsConstructor
class BookService {

    private val log = LoggerFactory.getLogger(BookService::class.java)

    @Autowired
    @Qualifier("sibyl-postgresql")
    private val r2dbcEntityTemplate: R2dbcEntityTemplate? = null


    fun move_test(): Mono<Long?> {
        val str = "E:\\4me\\book"
        val parent = Paths.get(str)

        return walk(str, 1)
            .filter { e: Path -> e.parent == parent }
            .filter { e: Path -> e.toFile().isDirectory }
            .flatMap { item: Path ->
                val file = item.toFile()
                val files = file.listFiles()
                val fileName = file.name
                System.err.println(fileName + " -> " + files.size)
                Mono.zip(Mono.just(item), this.selectByName(fileName).switchIfEmpty(Mono.just(Book())))
            }
            .flatMap { tuple: Tuple2<Path, Book> ->
                val path = tuple.t1
                val book = tuple.t2
                System.err.println(path.toFile().name + " -> " + book.id)
                if (Objects.nonNull(book.id)) {

                    return@flatMap Mono.error(RuntimeException(path.toFile().name + " -> " + book.id + " 已存在"))
//                    return@flatMap Mono.zip(Mono.just(path), Mono.just(book));
                }
                book.name = path.toFile().name
                Mono.zip(Mono.just(path), this.insert(book))
            }
            .map {
                var absolutePath = it.t2.absolutePath
                var path = it.t1
                var listFiles = path.toFile().listFiles()
                System.err.println(path.toFile().name + " -> " + absolutePath)
                for (file in listFiles) {
                    if(file.name.equals(".thumb")) continue
                    if(file.name.equals(".ehviewer")) continue
                    try {
                        FileUtils.copyFile(file, File(absolutePath + file.name))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                absolutePath
            }
            .count()
            .map { count: Long? ->
                System.err.println("move count = $count")
                count
            }
    }

    private fun selectByName(name: String): Mono<Book> {
        return r2dbcEntityTemplate!!.selectOne(Query.query(Criteria.where("name").`is`(name)), Book::class.java)
    }

    fun insert(book: Book): Mono<Book> {
        book.isDeleted = "0"
        book.createTime = LocalDateTime.now()

        val id = getBean(Snowflake::class.java).nextId()
        val fileUniqueId = id.toString()
        log.info("[book add] fileUniqueId = {}", fileUniqueId)
        val absoluteDir = r.fileBaseDir() + "book" + "/" + r.yyyy_MM_dd() + "/"
        val absolutePath = absoluteDir + fileUniqueId + "/"
        log.info("[book add] absolutePath = {}", absolutePath)
        try {
            FileUtils.createParentDirectories(File(absolutePath))
        } catch (e: IOException) {
            return Mono.error(RuntimeException(e))
        }
        val realFile = File(absolutePath)
        try {
            realFile.mkdirs()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        book.id = id
        book.absolutePath = absolutePath
        book.relativePath = absolutePath.replace(r.fileBaseDir()!!, "")

        book.code = null
        book.serialNumber = null
        return r2dbcEntityTemplate!!.insert(book);
    }


    companion object {
        @JvmStatic
        val bean: BookService
            get() = getBean(BookService::class.java)
    }
}

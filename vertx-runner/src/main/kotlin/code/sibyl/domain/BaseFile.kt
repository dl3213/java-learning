package code.sibyl.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.vertx.codegen.annotations.DataObject
import io.vertx.codegen.format.QualifiedCase
import io.vertx.codegen.format.SnakeCase
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.templates.RowMapper
import io.vertx.sqlclient.templates.annotations.Column
import io.vertx.sqlclient.templates.annotations.ParametersMapped
import io.vertx.sqlclient.templates.annotations.RowMapped
import java.time.LocalDateTime

@DataObject
@RowMapped(formatter = SnakeCase::class)
@ParametersMapped(formatter = QualifiedCase::class)
open class BaseFile {

    open var id: Long? = null

    open var fileName: String? = null

    open var type: String? = null

    open var absolutePath: String? = null
    open var relativePath: String? = null

    open var size: Long? = null

    @Column(name = "sha256")
    open var sha256: String? = null

    open var suffix: String? = null

    open var serialNumber: String? = null

    open var code: String? = null
    open var width: Int? = null
    open var height: Int? = null

    open var isDeleted: String? = null

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    open var createTime: LocalDateTime? = null

    open var createId: Long? = null

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    open var updateTime: LocalDateTime? = null

    open var updateId: Long? = null

}
package code.sibyl.service.sql

import code.sibyl.common.SpringUtil
import code.sibyl.common.r
import code.sibyl.common.r.defaultUserId
import code.sibyl.common.r.getBean
import code.sibyl.domain.base.BaseFile
import code.sibyl.service.BookService
import com.alibaba.fastjson2.JSONObject
import com.mysql.cj.util.StringUtils
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.util.function.Tuple4
import java.time.Duration

@Service
@Slf4j
@RequiredArgsConstructor
class PostgresqlService {
    @Autowired
    @Qualifier("sibyl-postgresql")
    private val sibylPostgresqlTemplate: R2dbcEntityTemplate? = null

    fun template(): R2dbcEntityTemplate {
        return sibylPostgresqlTemplate!!
    }

    fun <T> selectById(id: Any, obj: T): Mono<out T> {
        return template().selectOne(Query.query(Criteria.where("id").`is`(id)), obj!!::class.java);
    }

    fun deleteById(id: Any, tableName: String): Mono<Long> {
        return template().databaseClient.sql("update ${tableName!!} set is_deleted = '1' where id = ${id!!}").fetch()
            .rowsUpdated()
    }

    companion object {

        @JvmStatic
        fun getBean(): PostgresqlService {
            return r.getBean(PostgresqlService::class.java)!!
        }
    }


    fun fileQuery(jsonObject: JSONObject): Mono<Tuple4<Long, List<BaseFile>, Int, Int>> {
        System.err.println(jsonObject)
        val currentUserId = defaultUserId()
        val entityType = "t_base_file"
        val pageNumber = jsonObject.getInteger("pageNumber") ?: 1
        val pageSize = jsonObject.getInteger("pageSize") ?: 12

        //var optionField = jsonObject.getString("optionField")

        var isDeleted = jsonObject.getString("isDeleted") ?: "0"
        var type = jsonObject.getString("type")
        var code = jsonObject.getString("code") ?: ""
        var keyword = jsonObject.getString("keyword")
        var hash = jsonObject.getString("hash") ?: "0"
        var heart = jsonObject.getString("heart") ?: "0"
        var tags = jsonObject.getString("tags") ?: ""
        var typeList = type?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
        var codeList = code.split(",").map { it.trim() }.filter { it.isNotBlank() }
        var orderField = jsonObject.getString("orderField")
        var orderDirection = jsonObject.getString("orderDirection")
        var sql = """
            select main.*,
            COALESCE(heart_count.count,0 ) as heart_count, 
            COALESCE(heart_by_current_user.count,0 ) as heart_by_current_user_count, 
            case when main.type like 'image%' or main.type like 'video%' then 'gallery' else 'card' end as html_template,
            1 as ret
            from T_BASE_FILE main
            left join (
                select entity_id, count(1) as count from t_biz_user_heart
                where is_deleted = '${isDeleted}'
                  and entity_type = '${entityType}'
                group by entity_id
            ) heart_count on heart_count.entity_id = main.id
            left join (
                select entity_id, count(1) as count from t_biz_user_heart
                where is_deleted = '${isDeleted}'
                  and entity_type = '${entityType}'
                  and user_id = '${currentUserId}'
                group by entity_id
            ) heart_by_current_user on heart_by_current_user.entity_id = main.id
            where IS_DELETED = '${isDeleted}' 
            ${if (!typeList.isNullOrEmpty()) "and (" + typeList.joinToString(" or ") { "type ilike '${it}%'" } + ")" else ""}
            ${if (codeList.isNotEmpty()) "and code in (" + codeList.joinToString(",") { "'${it}'" } + ")" else ""}
            ${
            if (!tags.isNullOrBlank()) """
                and main.id in (
                    select distinct entity_id from t_base_tag 
                    where is_deleted = '0' 
                    and entity_type = '${entityType}' 
                    and name in (${tags.split(",").filter { it.isNotBlank() }.joinToString(",") { "'${it.trim()}'" }})
                )
            """.trimIndent() else ""
        }
            ${if (!keyword.isNullOrBlank()) "and (real_name ilike '%${keyword}%' or type ilike '%${keyword}%' or file_name ilike '%${keyword}%' or code ilike '%${keyword}%' or cast(id as varchar) ilike '%${keyword}%') " else ""}
            ${
            if (hash == "1") """
                and sha256 in (
                    select sha256 from (
                    select sha256, count(1) as count from T_BASE_FILE
                    where is_deleted = '${isDeleted}' 
                    and sha256 is not null
                    ${if (codeList.isNotEmpty()) "and code in (" + codeList.joinToString(",") { "'${it}'" } + ")" else ""}
                    group by sha256
                    )t where count >=2 
                )
            """.trimIndent() else ""
        }
            ${
            if (heart == "1") """
                and id in (
                    select distinct entity_id from t_biz_user_heart 
                    where is_deleted = '${isDeleted}' 
                    and entity_type ='${entityType}' 
                    and user_id = '${currentUserId}' 
                )
            """.trimIndent() else ""
        }
            order by ${if (hash == "1") "sha256 asc," else ""} ${if (!orderField.isNullOrBlank()) orderField.camelToSnakeCase() else "create_time"} ${if (!orderDirection.isNullOrBlank()) orderDirection else "asc"}  NULLS LAST, create_time asc
        """.trimIndent()
        var countSql = "select count(1) as count from (${sql}) temp";
        println("countSql --->")
        println(countSql)
        println("countSql --->")
        println()
        var selectSql = "select * from (${sql}) temp LIMIT ${pageSize} OFFSET ${(pageNumber - 1) * pageSize}";
        println("selectSql --->")
        println(selectSql)
        println("selectSql --->")
        return Mono.zip(
            sibylPostgresqlTemplate!!.databaseClient?.sql(countSql)!!.fetch().first().map { it.get("count") as Long },
            sibylPostgresqlTemplate!!.databaseClient?.sql(selectSql)!!.mapProperties(BaseFile::class.java)
                .all()
                .map {
                    it.htmlTemplate = if (it.type?.startsWith("image", true) == true || it.type?.startsWith(
                            "video",
                            true
                        ) == true
                    ) "gallery" else "card"
                    it.gallery = if (it.type?.startsWith("image", true) == true) "data-fancybox='gallery'" else null
                    it.sizeDescription = fileSizeDescription(it.size)
                    it
                }
                .collectList(),
            Mono.just(pageNumber),
            Mono.just(pageSize)
        );
    }

    private fun fileSizeDescription(size: Long?): String? {
        if (size == null) return ""
        var sizeStr = size.toString()
        if (size < r._1kb) return sizeStr + "byte";
        if (size >= r._1kb && size <= r._1mb) return String.format("%.2f", (size / 1024.0)).toString() + "KB";
        if (size >= r._1mb && size <= r._1gb) return String.format("%.2f", size / 1024.0 / 1024.0).toString() + "MB";
        if (size >= r._1gb) return String.format("%.2f", size / 1024.0 / 1024.0 / 1024.0).toString() + "GB";
        return sizeStr
    }


    fun <T> bookQuery(jsonObject: JSONObject, clazz: Class<out T>): Mono<Tuple4<Long, List<T>, Int, Int>> {

        val currentUserId = defaultUserId()
        val entityType = "t_biz_book"
        val pageNumber = jsonObject.getInteger("pageNumber") ?: 1
        val pageSize = jsonObject.getInteger("pageSize") ?: 12
        var isDeleted = jsonObject.getString("isDeleted") ?: "0"
        var type = jsonObject.getString("type")
        var code = jsonObject.getString("code") ?: ""
        var keyword = jsonObject.getString("keyword")
        var hash = jsonObject.getString("hash") ?: "0"
        var heart = jsonObject.getString("heart") ?: "0"
        var orderField = jsonObject.getString("orderField")
        var orderDirection = jsonObject.getString("orderDirection")
        var sql = """
            select main.*, 
            COALESCE(heart_count.count,0 ) as heart_count, 
            COALESCE(heart_by_current_user.count,0 ) as heart_by_current_user_count,
            '1' as is_gallery,
            'book' as html_template,
            1 as ret
            from t_biz_book main
            left join (
                select entity_id, count(1) as count from t_biz_user_heart
                where is_deleted = '${isDeleted}'
                  and entity_type = '${entityType}'
                group by entity_id
            ) heart_count on heart_count.entity_id = main.id
            left join (
                select entity_id, count(1) as count from t_biz_user_heart
                where is_deleted = '${isDeleted}'
                  and entity_type = '${entityType}'
                  and user_id = '${currentUserId}'
                group by entity_id
            ) heart_by_current_user on heart_by_current_user.entity_id = main.id
            where IS_DELETED = '${isDeleted}'
          
            ${if (!keyword.isNullOrBlank()) "and (name ilike '%${keyword}%' or type ilike '%${keyword}%' or serial_number ilike '%${keyword}%' or code ilike '%${keyword}%' or cast(id as varchar) ilike '%${keyword}%') " else ""}
              
            order by ${if (!orderField.isNullOrBlank()) orderField.camelToSnakeCase() else "create_time"} ${if (!orderDirection.isNullOrBlank()) orderDirection else "asc"} 
        """.trimIndent()
        var countSql = "select count(1) as count from (${sql}) temp";
        println("countSql --->")
        println(countSql)
        println("countSql --->")
        println()
        var selectSql = "select * from (${sql}) temp LIMIT ${pageSize} OFFSET ${(pageNumber - 1) * pageSize}";
        println("selectSql --->")
        println(selectSql)
        println("selectSql --->")
        return Mono.zip(
            sibylPostgresqlTemplate!!.databaseClient?.sql(countSql)!!.fetch().first().map { it.get("count") as Long },
            sibylPostgresqlTemplate!!.databaseClient?.sql(selectSql)!!.mapProperties(clazz).all().collectList(),
            Mono.just(pageNumber),
            Mono.just(pageSize)
        );
    }

}

fun String.camelToSnakeCase(): String {
    // 使用正则匹配大写字母前的位置（非开头），插入下划线
    return this.replace(Regex("(?<!^)(?=[A-Z])"), "_").toLowerCase()
}

fun main(args: Array<String>) {
    println("123")
    println("createTime".camelToSnakeCase())
}
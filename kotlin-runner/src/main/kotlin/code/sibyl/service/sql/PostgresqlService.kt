package code.sibyl.service.sql

import code.sibyl.common.SpringUtil
import code.sibyl.common.r
import code.sibyl.common.r.defaultUserId
import code.sibyl.common.r.getBean
import code.sibyl.service.BookService
import com.alibaba.fastjson2.JSONObject
import com.mysql.cj.util.StringUtils
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
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

    companion object {

        @JvmStatic
        fun getBean(): PostgresqlService {
            return r.getBean(PostgresqlService::class.java)!!
        }
    }



    fun <T> fileQuery(jsonObject: JSONObject, clazz: Class<out T>): Mono<Tuple4<Long, List<T>, Int, Int>> {

        val currentUserId = defaultUserId()
        val entityType = "t_base_file"
        val pageNumber = jsonObject.getInteger("pageNumber")
        val pageSize = jsonObject.getInteger("pageSize")

        var isDeleted = jsonObject.getString("isDeleted")
        var type = jsonObject.getString("type")
        var keyword = jsonObject.getString("keyword")
        var hash = jsonObject.getString("hash")
        var heart = jsonObject.getString("heart")
        var orderField = jsonObject.getString("orderField")
        var orderDirection = jsonObject.getString("orderDirection")
        var sql = """
            select main.*, COALESCE(heart_count.count,0 ) as heart_count, COALESCE(heart_by_current_user.count,0 ) as heart_by_current_user_count
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
            ${if(!type.isNullOrBlank()) "and type like '${type}%'" else "" }
            ${if(!keyword.isNullOrBlank()) "and (real_name like '%${keyword}%' or sha256 like '%${keyword}%' or type like '%${keyword}%' or file_name like '%${keyword}%' or cast(id as varchar) like '%${keyword}%') " else "" }
            ${if(hash == "1") """
                and sha256 in (
                    select sha256 from (
                    select sha256, count(1) as count from T_BASE_FILE
                    where is_deleted = '${isDeleted}' and sha256 is not null
                    group by sha256
                    )t where count >=2 
                )
            """.trimIndent() else "" }
            ${if(heart == "1") """
                and id in (
                    select distinct entity_id from t_biz_user_heart 
                    where is_deleted = '${isDeleted}' 
                    and entity_type ='${entityType}' 
                    and user_id = '${currentUserId}' 
                )
            """.trimIndent() else "" }
            order by ${if(hash == "1") "sha256 asc," else ""} ${if(!orderField.isNullOrBlank()) orderField.camelToSnakeCase() else "create_time"} ${if(!orderDirection.isNullOrBlank()) orderDirection else "asc"} 
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
        ).delaySubscription(Duration.ofMillis(500));
    }

    fun <T> bookQuery(jsonObject: JSONObject, clazz: Class<out T>): Mono<Tuple4<Long, List<T>, Int, Int>> {

        val currentUserId = defaultUserId()
        val entityType = "t_biz_book"
        val pageNumber = jsonObject.getInteger("pageNumber")
        val pageSize = jsonObject.getInteger("pageSize")

        var isDeleted = jsonObject.getString("isDeleted")
        var type = jsonObject.getString("type")
        var keyword = jsonObject.getString("keyword")
        var hash = jsonObject.getString("hash")
        var heart = jsonObject.getString("heart")
        var orderField = jsonObject.getString("orderField")
        var orderDirection = jsonObject.getString("orderDirection")
        var sql = """
            select main.*, COALESCE(heart_count.count,0 ) as heart_count, COALESCE(heart_by_current_user.count,0 ) as heart_by_current_user_count
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
            ${if(!type.isNullOrBlank()) "and type like '${type}%'" else "" }
            ${if(!keyword.isNullOrBlank()) "and (name like '%${keyword}%' or type like '%${keyword}%' or serial_number like '%${keyword}%' or code like '%${keyword}%' or cast(id as varchar) like '%${keyword}%') " else "" }
            ${if(hash == "1") """
                and sha256 in (
                    select sha256 from (
                    select sha256, count(1) as count from T_BASE_FILE
                    where is_deleted = '${isDeleted}' and sha256 is not null
                    group by sha256
                    )t where count >=2 
                )
            """.trimIndent() else "" }
            ${if(heart == "1") """
                and id in (
                    select distinct entity_id from t_biz_user_heart 
                    where is_deleted = '${isDeleted}' 
                    and entity_type ='${entityType}' 
                    and user_id = '${currentUserId}' 
                )
            """.trimIndent() else "" }
            order by ${if(hash == "1") "sha256 asc," else ""} ${if(!orderField.isNullOrBlank()) orderField.camelToSnakeCase() else "create_time"} ${if(!orderDirection.isNullOrBlank()) orderDirection else "asc"} 
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
        ).delaySubscription(Duration.ofMillis(500));
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
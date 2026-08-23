package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.service.sql.PostgresqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 图片相似度检索(pHash 方案)
 *
 * 汉明距离下推到 SQL:PG 14+ 位串异或(#) + bit_count
 * ('x'||phash)::bit(64) 把 16 位十六进制转 64bit 位串
 */
@Service
@Slf4j
public class ImageSimilarityService {

    public static ImageSimilarityService getBean() {
        return r.getBean(ImageSimilarityService.class);
    }

    /**
     * 以目标指纹在全库(image 类型、已回填 phash)中搜相似图
     *
     * @param id        排除的目标图片 id
     * @param targetPhash 目标指纹(16 位十六进制;可为现场计算值,不必已落库)
     * @param threshold 汉明距离阈值(64bit 常用 ≤10)
     * @param limit     返回条数上限
     */
    public Mono<List<Map<String, Object>>> searchByPhash(Long id, String targetPhash, int threshold, int limit) {
        return PostgresqlService.getBean().template().getDatabaseClient()
                .sql("""
                        select b.*,
                               bit_count((('x'||b.phash)::bit(64)) # (('x'||:target)::bit(64))) as distance
                        from t_base_file b
                        where b.is_deleted = '0'
                          and b.type like 'image%'
                          and b.phash is not null
                          and b.id != :id
                          and bit_count((('x'||b.phash)::bit(64)) # (('x'||:target)::bit(64))) <= :threshold
                        order by distance asc
                        limit :limit
                        """)
                .bind("target", targetPhash)
                .bind("id", id)
                .bind("threshold", threshold)
                .bind("limit", limit)
                .fetch()
                .all()
                .collectList()
                .map(rows -> {
                    // 雪花 id 超出 JS Number 安全整数范围,序列化为字符串防止前端精度丢失
                    for (Map<String, Object> row : rows) {
                        Object idv = row.get("id");
                        if (idv instanceof Number) {
                            row.put("id", String.valueOf(idv));
                        }
                    }
                    return rows;
                });
    }
}

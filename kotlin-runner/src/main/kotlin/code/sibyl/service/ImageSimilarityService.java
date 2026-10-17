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

    /**
     * 多帧指纹(方案 B)检索相似视频:逐帧比汉明距离,统计命中帧数
     *
     * @param id             排除的目标视频 id
     * @param targetCsv      目标多帧指纹(逗号分隔的 16 位十六进制,来自 vhash 或现场计算)
     * @param frameThreshold 单帧汉明距离阈值(常用 6~10)
     * @param minHits        命中帧数下限(默认 12 帧里 ≥8 帧命中判相似)
     * @param limit          返回条数上限
     */
    public Mono<List<Map<String, Object>>> searchByVhashFrames(Long id, String targetCsv,
                                                               int frameThreshold, int minHits, int limit) {
        return PostgresqlService.getBean().template().getDatabaseClient()
                .sql("""
                        select * from (
                            select b.*, (
                                select count(*) from unnest(
                                    string_to_array(b.vhash, ','),
                                    string_to_array(:target, ',')
                                ) as u(h1, h2)
                                where bit_count((('x'||u.h1)::bit(64)) # (('x'||u.h2)::bit(64))) <= :ft
                            ) as hits
                            from t_base_file b
                            where b.is_deleted = '0'
                              and b.type like 'video%'
                              and b.vhash is not null
                              and b.vhash <> ''
                              and b.id != :id
                        ) s
                        where s.hits >= :minHits
                        order by s.hits desc, s.size desc
                        limit :limit
                        """)
                .bind("target", targetCsv)
                .bind("id", id)
                .bind("ft", frameThreshold)
                .bind("minHits", minHits)
                .bind("limit", limit)
                .fetch()
                .all()
                .collectList()
                .map(rows -> {
                    // 雪花 id 转字符串,避免前端精度丢失
                    for (Map<String, Object> row : rows) {
                        Object idv = row.get("id");
                        if (idv instanceof Number) {
                            row.put("id", String.valueOf(idv));
                        }
                    }
                    return rows;
                });
    }

    /**
     * 以视频封面指纹(vphash)在全库视频中搜相似视频(相似视频初筛)
     *
     * @param id          排除的目标视频 id
     * @param targetPhash 目标指纹(16 位十六进制;可为现场计算值)
     * @param threshold   汉明距离阈值(0~64,常用 ≤10)
     * @param limit       返回条数上限
     */
    public Mono<List<Map<String, Object>>> searchByVphash(Long id, String targetPhash, int threshold, int limit) {
        return PostgresqlService.getBean().template().getDatabaseClient()
                .sql("""
                        select b.*,
                               bit_count((('x'||b.vphash)::bit(64)) # (('x'||:target)::bit(64))) as distance
                        from t_base_file b
                        where b.is_deleted = '0'
                          and b.type like 'video%'
                          and b.vphash is not null
                          and b.id != :id
                          and bit_count((('x'||b.vphash)::bit(64)) # (('x'||:target)::bit(64))) <= :threshold
                        order by distance asc, b.size desc
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

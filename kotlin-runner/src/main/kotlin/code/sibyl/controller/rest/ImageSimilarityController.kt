package code.sibyl.controller.rest

import code.sibyl.common.Response
import code.sibyl.common.r
import code.sibyl.domain.base.BaseFile
import code.sibyl.dto.BatchEmbeddingRequest
import code.sibyl.dto.ImageEmbeddingRequest
import code.sibyl.dto.ImageSimilaritySearchRequest
import code.sibyl.service.ImageSimilarityService
import com.alibaba.fastjson2.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * 图片相似度 Controller
 * 
 * API 列表:
 * POST /api/rest/v1/image-similarity/embed - 单张图片入库
 * POST /api/rest/v1/image-similarity/batch-embed - 批量扫描目录入库
 * POST /api/rest/v1/image-similarity/search - 搜索相似图片 (按BaseFile ID)
 * GET  /api/rest/v1/image-similarity/match - 快速匹配两张图片 (按BaseFile ID)
 */
@Controller
@RequestMapping("/api/rest/v1/image-similarity")
class ImageSimilarityController {

    private val log = LoggerFactory.getLogger(ImageSimilarityController::class.java)

    /**
     * 单张图片入库 (生成向量描述)
     */
    @PostMapping("/embed")
    @ResponseBody
    fun embedImage(@RequestBody request: ImageEmbeddingRequest): Mono<Response> {
        return try {
            val service = r.getBean(ImageSimilarityService::class.java)
            service.embedImage(request)
                .map { baseFile ->
                    Response.success(mapOf(
                        "id" to baseFile.id,
                        "filePath" to baseFile.absolutePath,
                        "sha256" to baseFile.sha256,
                        "description" to baseFile.imageDescription,
                        "vectorDim" to baseFile.vectorDim
                    ))
                }
                .onErrorResume { e ->
                    log.error("[ImageSimilarity] embed failed", e)
                    Mono.just(Response.error("嵌入失败: ${e.message}"))
                }
        } catch (e: Exception) {
            Mono.just(Response.error("嵌入失败: ${e.message}"))
        }
    }

    /**
     * 批量扫描目录入库
     */
    @PostMapping("/batch-embed")
    @ResponseBody
    fun batchEmbed(@RequestBody request: BatchEmbeddingRequest): Mono<Response> {
        return try {
            val service = r.getBean(ImageSimilarityService::class.java)
            service.batchEmbedFromDirectory(request)
                .collectList()
                .map { results ->
                    Response.success(mapOf(
                        "totalProcessed" to results.size,
                        "results" to results.map { baseFile -> mapOf(
                            "id" to baseFile.id,
                            "filePath" to baseFile.absolutePath,
                            "status" to baseFile.similarityStatus
                        )}
                    ))
                }
                .onErrorResume { e ->
                    log.error("[ImageSimilarity] batch embed failed", e)
                    Mono.just(Response.error("批量嵌入失败: ${e.message}"))
                }
        } catch (e: Exception) {
            Mono.just(Response.error("批量嵌入失败: ${e.message}"))
        }
    }

    /**
     * 搜索相似图片 (按 BaseFile ID 查询)
     */
    @PostMapping("/search")
    @ResponseBody
    fun searchSimilar(@RequestBody request: ImageSimilaritySearchRequest): Mono<Response> {
        return try {
            val service = r.getBean(ImageSimilarityService::class.java)
            service.searchSimilarImagesById(request)
                .collectList()
                .map { results ->
                    Response.success(mapOf(
                        "total" to results.size,
                        "results" to results.mapIndexed { index, result ->
                            mapOf(
                                "rank" to (index + 1),
                                "id" to result.embeddingId,
                                "filePath" to result.filePath,
                                "similarity" to "%.4f".format(result.similarity),
                                "description" to result.imageDescription
                            )
                        }
                    ))
                }
                .onErrorResume { e ->
                    log.error("[ImageSimilarity] search failed", e)
                    Mono.just(Response.error("搜索失败: ${e.message}"))
                }
        } catch (e: Exception) {
            Mono.just(Response.error("搜索失败: ${e.message}"))
        }
    }

    /**
     * 快速匹配两张图片 (按 BaseFile ID 查询)
     * GET /api/rest/v1/image-similarity/match?id1=123&id2=456
     */
    @GetMapping("/match")
    @ResponseBody
    fun matchTwoImages(
        @RequestParam("id1") id1: Long,
        @RequestParam("id2") id2: Long
    ): Mono<Response> {
        return try {
            val service = r.getBean(ImageSimilarityService::class.java)
            service.matchTwoImagesById(id1, id2)
                .map { result ->
                    Response.success(result)
                }
                .onErrorResume { e ->
                    log.error("[ImageSimilarity] match failed", e)
                    Mono.just(Response.error("匹配失败: ${e.message}"))
                }
        } catch (e: Exception) {
            Mono.just(Response.error("匹配失败: ${e.message}"))
        }
    }
}

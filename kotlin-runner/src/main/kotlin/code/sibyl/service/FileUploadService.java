package code.sibyl.service;


import code.sibyl.controller.rest.UploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileUploadService {

    private String uploadBaseDir = "D:\\4test\\file";

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * 处理文件夹上传（响应式方式）
     */
    public Mono<UploadResult> uploadFolderReactive(List<FilePart> fileParts, List<String> paths, String folderName) {
        if (fileParts == null || fileParts.isEmpty() || paths == null || paths.isEmpty()) {
            return Mono.error(new IllegalArgumentException("文件和路径不能为空"));
        }

        if (fileParts.size() != paths.size()) {
            return Mono.error(new IllegalArgumentException("文件数量和路径数量不匹配"));
        }

        // 创建唯一的文件夹名
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String uniqueFolderName = folderName + "_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8);
        Path targetDir = Paths.get(uploadBaseDir, uniqueFolderName);

        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("无法创建目标目录: " + targetDir, e));
        }

        AtomicInteger processedCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        // 使用Flux处理所有文件上传
        return Flux.fromIterable(fileParts)
                .index()
                .flatMap(tuple -> {
                    long index = tuple.getT1();
                    FilePart filePart = tuple.getT2();
                    String relativePath = paths.get((int) index);

                    return saveFileReactive(filePart, targetDir, relativePath)
                            .doOnSuccess(saved -> processedCount.incrementAndGet());
                })
                .then(Mono.fromCallable(() -> {
                    long uploadTime = System.currentTimeMillis() - startTime;
                    long totalSize = calculateTotalSize(targetDir);

                    return new UploadResult(
                            processedCount.get(),
                            targetDir.toString(),
                            totalSize,
                            uploadTime
                    );
                }));
    }

    /**
     * 保存单个文件（响应式）
     */
    private Mono<Path> saveFileReactive(FilePart filePart, Path baseDir, String relativePath) {
        Path targetPath = baseDir.resolve(relativePath);

        try {
            // 确保父目录存在
            Files.createDirectories(targetPath.getParent());
        } catch (IOException e) {
            return Mono.error(new RuntimeException("无法创建目录: " + targetPath.getParent(), e));
        }

        // 使用DataBufferUtils写入文件
        return filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .collectList()
                .flatMap(bytesList -> {
                    try {
                        byte[] allBytes = new byte[bytesList.stream().mapToInt(b -> b.length).sum()];
                        int offset = 0;
                        for (byte[] bytes : bytesList) {
                            System.arraycopy(bytes, 0, allBytes, offset, bytes.length);
                            offset += bytes.length;
                        }
                        Files.write(targetPath, allBytes);
                        return Mono.just(targetPath);
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("无法写入文件: " + targetPath, e));
                    }
                });
    }

    /**
     * 处理文件夹上传（传统方式，兼容MultipartFile）
     */
    public Mono<UploadResult> uploadFolderTraditional(List<MultipartFile> files, List<String> paths, String folderName) {
        return Mono.fromCallable(() -> {
            if (files == null || files.isEmpty() || paths == null || paths.isEmpty()) {
                throw new IllegalArgumentException("文件和路径不能为空");
            }

            if (files.size() != paths.size()) {
                throw new IllegalArgumentException("文件数量和路径数量不匹配");
            }

            // 创建唯一的文件夹名
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String uniqueFolderName = folderName + "_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8);
            Path targetDir = Paths.get(uploadBaseDir, uniqueFolderName);

            Files.createDirectories(targetDir);

            int processedCount = 0;
            long totalSize = 0;
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String relativePath = paths.get(i);

                if (file.isEmpty()) {
                    continue;
                }

                Path targetPath = targetDir.resolve(relativePath);

                // 确保父目录存在
                Files.createDirectories(targetPath.getParent());

                // 保存文件
                file.transferTo(targetPath.toFile());
                processedCount++;
                totalSize += file.getSize();
            }

            long uploadTime = System.currentTimeMillis() - startTime;

            return new UploadResult(
                    processedCount,
                    targetDir.toString(),
                    totalSize,
                    uploadTime
            );
        });
    }

    /**
     * 计算文件夹总大小
     */
    private long calculateTotalSize(Path dir) {
        try {
            return Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            return 0;
        }
    }
}
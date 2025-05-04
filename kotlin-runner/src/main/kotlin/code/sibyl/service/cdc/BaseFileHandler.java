package code.sibyl.service.cdc;

import cn.hutool.crypto.SecureUtil;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.service.sql.MysqlService;
import code.sibyl.service.sql.PostgresqlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component(Handler.beanNamePrev + "t_base_file")
@Slf4j
@RequiredArgsConstructor
public class BaseFileHandler implements Handler<BaseFile> {

    public static BaseFileHandler getBean() {
        return r.getBean(BaseFileHandler.class);
    }

    @Override
    public Class getEntityClass() {
        return BaseFile.class;
    }

    @Override
    public Mono<BaseFile> insert(BaseFile afterEntity) {
        log.info("BaseFileHandler-insert: --> {}", afterEntity.getAbsolutePath());
        return Mono.zip(this.sha256(afterEntity), this.图片补充大小(afterEntity), this.视频文件补充thumbnail(afterEntity))
                .thenReturn(afterEntity);
    }

    public Mono<BaseFile> sha256(BaseFile item) {
        if (Objects.isNull(item)) return Mono.just(new BaseFile());
        if (StringUtils.isNotBlank(item.getSha256())) return Mono.just(item);
        if (StringUtils.isBlank(item.getAbsolutePath())) return Mono.just(item);
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        String absolutePath = item.getAbsolutePath();
        File realFile = new File(absolutePath);
        String sha256 = SecureUtil.sha256(realFile);
        item.setSha256(sha256);
        //log.info("[文件补充hash] [{}] {} --> sha256 = {}", Thread.currentThread().getName(), absolutePath, sha256);
        return client.sql("update T_BASE_FILE set SHA256=:sha256 where id=:id")
                .bind("id", item.getId())
                .bind("sha256", sha256)
                .fetch()
                .rowsUpdated()
                .then(Mono.just(item))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.just(item);
                })
                ;
    }

    public Mono<BaseFile> 图片补充大小(BaseFile item) {
        if (Objects.isNull(item)) return Mono.just(new BaseFile());
        if (StringUtils.isBlank(item.getAbsolutePath())) return Mono.just(item);
        if (StringUtils.isBlank(item.getType())) return Mono.just(item);
        if (!item.getType().startsWith("image")) return Mono.just(item);
        if (Objects.nonNull(item.getWidth()) && Objects.nonNull(item.getHeight())) return Mono.just(item);
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        String absolutePath = item.getAbsolutePath();
        File realFile = new File(absolutePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(realFile);
        } catch (IOException e) {
            System.err.println(STR."读取图片文件异常：\{absolutePath}");
            e.printStackTrace();
            //throw new RuntimeException(e);
            return Mono.error(e);
        }
        if (Objects.isNull(image)) {
            return Mono.empty();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        //log.info("[图片补充大小] [{}] {} --> width = {}, height = {}", Thread.currentThread().getName(), absolutePath, width, height);
        return client.sql("update T_BASE_FILE set height=:height, width=:width where id=:id")
                .bind("id", item.getId())
                .bind("height", height)
                .bind("width", width)
                .fetch()
                .rowsUpdated()
                .then(Mono.just(item))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.just(item);
                });
    }

    public Mono<BaseFile> 视频文件补充thumbnail(BaseFile item) {
        if (Objects.isNull(item)) return Mono.just(new BaseFile());
        if (StringUtils.isBlank(item.getAbsolutePath())) return Mono.just(item);
        if (StringUtils.isBlank(item.getType())) return Mono.just(item);
        if (!item.getType().startsWith("video")) return Mono.just(item);
        if (StringUtils.isNotBlank(item.getThumbnail())) return Mono.just(item);
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        String absolutePath = item.getAbsolutePath();
        File targetVideo = new File(absolutePath);
        String thumbnailPath = null;
        try {
            FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(absolutePath);
            ff.start();
            String rotate = ff.getVideoMetadata("rotate");
            Frame f;
            int i = 0;
            while (i < 1) {
                f = ff.grabImage();
                IplImage src;
                if (null != rotate && rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                    src = converter.convert(f);
                    f = converter.convert(rotate(src, Integer.parseInt(rotate)));
                }
                thumbnailPath = doExecuteFrame(f, targetVideo.getParentFile().getAbsolutePath(), item.getFileName());
                //System.err.println(thumbnailPath);
                i++;
            }
            ff.stop();

        } catch (FFmpegFrameGrabber.Exception e) {
            return Mono.error(new RuntimeException(e));
        }
        //log.info("[图片补充大小] [{}] {} --> thumbnailPath = {} ", Thread.currentThread().getName(), absolutePath, thumbnailPath);
        return client.sql("update T_BASE_FILE set thumbnail=:thumbnailPath where id=:id")
                .bind("id", item.getId())
                .bind("thumbnailPath", thumbnailPath)
                .fetch()
                .rowsUpdated()
                .then(Mono.just(item))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.just(item);
                });
    }

    /**
     * 进行旋转角度操作（为了保证截取到的第一帧图片与视频中的角度方向保持一致）
     */
    public IplImage rotate(IplImage src, int angle) {
        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core opencv_core = new opencv_core();
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }

    public String doExecuteFrame(Frame f, String targetFilePath, String targetFileName) {
        if (null == f || null == f.image) {
            return null;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        String fileName = targetFilePath + File.separator + targetFileName + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(fileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}

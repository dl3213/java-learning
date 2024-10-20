package code.sibyl.controller.file;

import cn.hutool.core.lang.Tuple;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.service.FileService;
import code.sibyl.service.QueryService;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    private final static String dir = "E:\\4me\\pixez\\";

    @Autowired
    FileService fileService;
    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @GetMapping({"/list-view"})
    public Mono<String> model(final Model model) {
        String s = "/photo/list-view";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @PostMapping(value = "/pixiv/page")
    @ResponseBody
    public Mono<Response> pixiv_page(@RequestBody JSONObject jsonObject) {
        Criteria criteria = Criteria.where("IS_DELETED").is("0");
        Query query = Query.query(criteria).with(PageRequest.of(jsonObject.getInteger("pageNumber"), jsonObject.getInteger("pageSize"))) // 1开始
                .sort(Sort.sort(code.sibyl.domain.base.BaseFile.class).by(BaseFile::getFileName));
        return Mono.zip(r2dbcEntityTemplate.count(query, BaseFile.class), r2dbcEntityTemplate.select(query, BaseFile.class).collectList())
                .map(t -> Response.successPage(t.getT1(), t.getT2(), jsonObject.getInteger("pageNumber"), jsonObject.getInteger("pageSize")));
    }

    @DeleteMapping(value = "/pixiv/delete/{id}")
    @ResponseBody
    public Mono<Response> pixiv_delete(@PathVariable String id) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    System.err.println(e);
                    e.setDeleted("1");
//                    File file = new File(e.getAbsolutePath());
//                    if (Objects.nonNull(file) && file.exists()) {
//                        file.delete();
//                    }
                    return r2dbcEntityTemplate.update(e);
                }).map(e -> Response.success(e));
    }

    @PostMapping("/pixiv/upload")
    @ResponseBody
    public Mono<Response> th_finance_collection_book_upload(@RequestPart("file") Mono<FilePart> filePartMono) {

        return filePartMono
                .flatMap(e -> Mono.zip(Mono.just(e.filename()), DataBufferUtils.join(e.content())))
                .map(t -> {
                    DataBuffer dataBuffer = t.getT2();
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    System.err.println(bytes.length);
                    return new Tuple(t.getT1(), bytes);
                })
                .flatMap(t -> {
                    String fileName = t.get(0);
                    byte[] bytes = t.get(1);
                    String detect = r.getBean(Tika.class).detect(bytes);
                    return Mono.zip(Mono.just(fileName), Mono.just(bytes), Mono.just(detect), ReactiveSecurityContextHolder.getContext(), QueryService.getBean().fileList(fileName, detect).collectList());
                })
                .flatMap(t -> {
                    String fileName = t.getT1();
                    System.err.println(fileName);
                    byte[] bytes = t.getT2();
                    System.err.println(bytes.length);
                    String fileType = t.getT3();
                    System.err.println(fileType);
                    User user = (User) t.getT4().getAuthentication().getPrincipal();
                    System.err.println(user);
                    List<BaseFile> baseFileList = t.getT5();
                    System.err.println(baseFileList);

                    if (!fileType.contains("image")) {
                        return Mono.error(new RuntimeException("上传失败：非图像文件"));
                    }
                    if (CollectionUtils.isNotEmpty(baseFileList)) {
                        return Mono.error(new RuntimeException("上传失败：同名图像文件已存在"));
                    }

                    BaseFile file = new BaseFile();
                    file.setFileName(fileName);
                    file.setType(fileType);
                    String dir = r.pixivBaseDir + r.yyyy_MM_dd() + File.separator;
                    file.setAbsolutePath(dir + fileName);
                    System.err.println(dir);
                    System.err.println(file.getAbsolutePath());
                    String hash = r.hashBytes(bytes);
                    file.setSha256(hash);
                    File realFile = new File(file.getAbsolutePath());
                    File parentFile = new File(dir);

                    try {
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        if (!realFile.exists()) {
                            realFile.createNewFile();
                        }

                        try (OutputStream outputStream = new FileOutputStream(realFile)) {
                            IOUtils.write(bytes, outputStream);
                        }
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    } finally {

                    }
                    file.setSize(realFile.length());
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    file.setSuffix(suffix);
                    file.setCode("pixiv");
                    file.setDeleted("0");
                    file.setCreateId(1L);
                    file.setCreateTime(LocalDateTime.now());

                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(realFile);
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                    if (file.getType().contains("image") && r.isImage(realFile) && Objects.nonNull(image)) {
                        int width = image.getWidth();
                        int height = image.getHeight();
                        file.setWidth(width);
                        file.setHeight(height);
                    }

                    return r2dbcEntityTemplate.insert(file);
                })
                .map(e -> Response.success(e))
                ;
    }
}

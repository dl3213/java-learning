package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.service.PsychoPassService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author dyingleaf3213
 * @Classname PsychoPassController
 * @Description TODO
 * @Create 2023/04/05 01:27
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/psycho/pass")
public class PsychoPassController {

    @Resource
    private PsychoPassService psychoPassService;

    @GetMapping("/list/{userId}")
    public Response list(@PathVariable String userId){
        List<PsychoPassRecord> list = psychoPassService.list(userId);
        System.err.println(list.size());
        return Response.success(list);
    }

    @GetMapping("/insert/{userId}")
    public Response insert(@PathVariable String userId){

        List<String> strings = Arrays.asList("sibyl", "test", "dev", "prod","dl3213","steam");

        PsychoPassRecord record = new PsychoPassRecord();

        record.setUid(userId);
        record.setPsychoPass(String.valueOf(RandomUtils.nextDouble(0.01d, 500.00d)));
        record.setType("0");
        record.setCreateId(userId);
        record.setFlag(String.valueOf(System.currentTimeMillis()%5));
        record.setState(String.valueOf(System.currentTimeMillis()%5));
        record.setCode(strings.get(RandomUtils.nextInt(0, strings.size() - 1)) + (UUID.randomUUID().toString().substring(0, 10)));
        record.setCreateTime(LocalDateTime.now());

        psychoPassService.save(record);
        return Response.success();
    }
}

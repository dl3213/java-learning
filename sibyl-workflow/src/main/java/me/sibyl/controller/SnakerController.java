package me.sibyl.controller;

import me.sibyl.common.response.ResponseVO;
import me.sibyl.snaker.SnakerEngineFacets;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.core.Execution;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname controller
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/05/04 13:54
 */
@RestController
public class SnakerController {

   @Resource
    private SnakerEngineFacets snakerEngineFacets;
    @Resource
    private SnakerEngine engine;

    @GetMapping("test")
    public ResponseVO test(){
        List<Process> processs = engine.process().getProcesss(null);
        System.err.println(processs);

        processs.forEach(item->{
            ProcessModel model = item.getModel();
            System.err.println(model);
        });
//        Task complete = engine.task().complete("41977ea4774d4df7b315340e7fd8c19f","snaker.admin");
//        System.err.println(complete);
        return ResponseVO.success(processs);
    }

    @GetMapping("get")
    public ResponseVO get(){
        List<String> allProcessNames = snakerEngineFacets.getAllProcessNames();
        System.err.println(allProcessNames);
        return new ResponseVO();
    }

    @GetMapping("start/{processId}")
    public ResponseVO start(@PathVariable String processId){
        Order order = snakerEngineFacets.startInstanceById(processId,"snaker.admin",null);
        System.err.println(order);
        return new ResponseVO();
    }

    @GetMapping("startAndExecute/{processId}")
    public ResponseVO startAndExecute(@PathVariable String processId){
        Order order = snakerEngineFacets.startAndExecute(processId,"snaker.admin",null);
        System.err.println(order);
        return new ResponseVO();
    }

    @GetMapping("execute/{taskId}")
    public ResponseVO execute(@PathVariable String taskId){
        List<Task> execute = snakerEngineFacets.execute(taskId, "snaker.admin", null);
        System.err.println(execute);
        return new ResponseVO();
    }

    @GetMapping("initFlows")
    public ResponseVO initFlows(){
        String s = snakerEngineFacets.initFlows();
        System.err.println(s);
        return new ResponseVO();
    }

    @GetMapping("getTasks/{orderId}")
    public ResponseVO getTasks(@PathVariable String orderId ){
        List<Task> tasks = snakerEngineFacets.getTasks(orderId);
        System.err.println(tasks);
        return new ResponseVO();
    }

}

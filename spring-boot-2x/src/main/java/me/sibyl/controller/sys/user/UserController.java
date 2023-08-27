package me.sibyl.controller.sys.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserMapper;
import me.sibyl.dto.response.UserExcel;
import me.sibyl.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/api/v1/sys/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("list")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView();
        view.setViewName("dashboard");
        return view;
    }

    @SneakyThrows
    @GetMapping("export")
    public void export(HttpServletResponse response, HttpServletRequest request){
        //Exs
        List<User> list = userMapper.selectList(null);
        List<UserExcel> data = list.stream().filter(Objects::nonNull).map(item -> {
            UserExcel excelResponse = new UserExcel();
            BeanUtils.copyProperties(item, excelResponse);
            return excelResponse;
        }).collect(Collectors.toList());

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), UserExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
}

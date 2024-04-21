package me.sibyl;

import com.alibaba.fastjson2.JSONObject;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class Main {


    public static void main123(String[] args) {
//        JSONObject get = get("http://localhost:80/noAuth/get?a=1");
//        System.err.println(get);
//        System.err.println(get.getString("code"));

        JSONObject request = new JSONObject();
        request.put("test", "1");
        JSONObject post = post("http://localhost:10001/noAuth/post", request);
        System.err.println(post);
        System.err.println(post.getString("code"));
    }

    public static JSONObject get(String url) {
        JSONObject ret = new JSONObject();
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        try {
            int statusCode = client.executeMethod(get);
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("get method failed: " + get.getStatusLine());
            }
            byte[] responseBody = get.getResponseBody();
            ret = JSONObject.parseObject(new String(responseBody, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
        }
        return ret;
    }

    public static JSONObject post(String url, JSONObject request) {
        JSONObject ret = new JSONObject();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        try {
            post.setRequestHeader("Content-type", "application/json;charset=UTF-8");
            RequestEntity requestEntity = new StringRequestEntity(request.toJSONString(), "application/json;charset=UTF-8", "UTF-8");
            post.setRequestEntity(requestEntity);
            int statusCode = client.executeMethod(post);
            InputStream in = post.getResponseBodyAsStream();
            String jsonString = convertStreamToString(in);
            ret = JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return ret;
    }

    public static String convertStreamToString(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1.递归
     * 2.枚举
     * 3.贪心
     * 4.回溯
     * 5.分治
     * 6.动态规划
     */

    public static void main(String[] args) {
        File dir = new File("D:\\4game\\steam\\steamapps\\workshop\\content\\431960");
        Date beginDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDateStr = simpleDateFormat.format(beginDate);
        System.out.println("开始时间：" + beginDateStr);
        printDirByRecursive(dir, 0, file -> {
            System.err.println(file + " = > " + file.getName());
            try {
                FileUtils.moveFile(file, new File("D:\\4pc\\dl3213\\" + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //searchDir(dir);
        Date endDate = new Date();
        String endDateStr = simpleDateFormat.format(endDate);
        System.out.println("结束时间：" + endDateStr);

    }

    public static void printDirByRecursive(File dir, int level, Consumer<File> consumer) {
        System.out.println(dir.getAbsolutePath());
        //输出层次数
        for (int i = 0; i < level; i++) {
            System.out.print("-");
        }
        //获取这个目录下所有的子文件和子目录的数组。
        File[] files = dir.listFiles();
        //遍历这个数组，取出每个File对象
        if (files != null) {
            for (File f : files) {
                //判断这个File是否是一个文件，是：
                if (f.isFile()) {
                    consumer.accept(f);
                } else {//否则就是一个目录，继续递归
                    //递归调用
                    printDirByRecursive(f, level + 1, consumer);
                }
            }
        }
    }

    public static void main22(String[] args) {
        int a = 0;
        while (a < 5) {
            switch (a) {
                case 0:
                    ;
                case 3:
                    a = a + 2;
                case 1:
                    ;
                case 2:
                    a = a + 3;
                default:
                    a = a + 5;
            }
        }
        System.err.println(a);

        System.err.println("3".compareTo("2"));
    }
}

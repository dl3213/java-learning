package code.sibyl.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname BilibiliTool
 * @Description 20230409
 * @Create 2023/04/08 21:28
 */

public class BilibiliTool {

    private static final WebClient webClient = webClient();

    public static void main(String[] args) {

//        List<String> strings = Arrays.asList(
//                "BV1BqXfYTEQq"
//        );
//        System.err.println(strings.size());
//        strings.stream().forEach(item -> {
//            try {
//                bilibiliDownloadVideo(item);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        allVideo();
    }

    public static void allVideo(){
        String upId = "3536997073291285";
        String url = "https://space.bilibili.com/3536997073291285";
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
            HttpGet get = new HttpGet(url);
            get.setHeader("cookie", getCookie());
            get.setHeader("Uaer-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = IOUtils.toString(content, Charset.forName("utf-8"));
            System.err.println("response => " + string);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static WebClient webClient() {
        // 创建不验证SSL的HttpClient
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)));
        // 使用HttpClient创建WebClient
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private static String getCookie() {

        return """
                buvid4=B0D8C4F1-19BA-C893-3C07-45A7F21D60B664363-023123112-o4ni5UDK5a3zSIFeUd70zQ%3D%3D; DedeUserID=11885873; DedeUserID__ckMd5=1c1d7cd5a933ec09; buvid_fp_plain=undefined; enable_web_push=DISABLE; go-back-dyn=1; historyviewmode=list; buvid3=B98504AD-F503-5AD6-B9B4-57D8CBF3986758338infoc; b_nut=1735560337; _uuid=310462B75-4A28-9E3F-1F102-849AFE6B148932264infoc; hit-dyn-v2=1; CURRENT_QUALITY=80; rpdid=|(k||lRJlRJu0J'u~J)YYYY~R; LIVE_BUVID=AUTO6017386770844761; enable_feed_channel=ENABLE; fingerprint=43b98cabb1ac2f4686e878b91a5e2d67; buvid_fp=43b98cabb1ac2f4686e878b91a5e2d67; header_theme_version=OPEN; theme-tip-show=SHOWED; theme-avatar-tip-show=SHOWED; home_feed_column=5; browser_resolution=2327-1228; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTI4MzQ2NzAsImlhdCI6MTc1MjU3NTQxMCwicGx0IjotMX0.wIpXpiRiQw7o6l7q7g_3V3WunF3CnOZ280JAM_aHV-w; bili_ticket_expires=1752834610; SESSDATA=7db5afbd%2C1768127470%2C99aa2%2A72CjA6eV8Z3c3Mxi78NM_gjCj1DesrLh1PVf01NGalzfaCoZThP5WzsBqZSy6P5Cn9CHMSVk1XQkFVSTU4ajNfTzBWMUhkeW5qUGpqc3lYRWN4XzlMa0tuZ25iX3M4THpHeDNXemN3T1ExRnZFYnhvWjdxakJ6WndvdHpsVEFoeUFVcnNGQlFVeEdnIIEC; bili_jct=f572e804e0596ae46510d5ece66cf208; PVID=1; sid=6fafps33; b_lsid=E1D10E2E9_198134BA500; bsource=search_baidu; CURRENT_FNVAL=4048; bp_t_offset_11885873=1090217297072619520
                """.toString().trim();
    }

    public static void close(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    private static void bilibiliDownloadVideo(String bvid) {
        if(StringUtils.isBlank(bvid)) return;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
            System.err.println("bvid => " + bvid);
            HttpGet get = new HttpGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid);
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = IOUtils.toString(content, Charset.forName("utf-8"));
            System.err.println("bvstring => " + string);
            close(content);
            JSONObject bvJson = JSONObject.parseObject(string);




            JSONObject data = bvJson.getJSONObject("data");
            String title = data.getString("title");
            //String cid = data.getString("cid");//
            JSONArray pages = data.getJSONArray("pages");

            for (int i = 0; i < pages.size(); i++) {
                JSONObject p = pages.getJSONObject(i);
                String cid = p.getString("cid");

                System.err.println("cid => " + cid);

                HttpGet vGet = new HttpGet("https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&qn=80");
//            vGet.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
//            vGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.ALL.getType());
//            vGet.setHeader("Referer", "https://www.bilibili.com"); // 设置协议
//            vGet.setHeader("Sec-Fetch-Mode", "no-cors");
//            vGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
                vGet.setHeader("cookie", getCookie());
                CloseableHttpResponse urlResponse = httpClient.execute(vGet);

                InputStream urlContent = urlResponse.getEntity().getContent();
                String urlStr = IOUtils.toString(urlContent, Charset.forName("utf-8"));
                System.err.println("urlStr => " + urlStr);
                close(urlContent);

                JSONObject urlJson = JSONObject.parseObject(urlStr);
                JSONObject urlData = urlJson.getJSONObject("data");
                JSONArray durls = urlData.getJSONArray("durl");
                JSONObject durl = durls.getJSONObject(0);
                String url = durl.getJSONArray("backup_url").getString(0);
                System.err.println("url => " + url);

                downloadNew(url, "D:\\" + bvid + "-" + cid + ".mp4");

                System.err.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadNew(String urlPath, String descFileName) {
        try {
            long begin = System.currentTimeMillis();
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Referer", "https://www.bilibili.com"); // 设置协议
            urlConnection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            urlConnection.setConnectTimeout(10 * 1000);

            System.err.println("共:" + (urlConnection.getContentLength() / 1024) + "Kb");
            System.err.println("开始下载...");
            //使用bufferedInputStream 缓存流的方式来获取下载文件，不然大文件会出现内存溢出的情况
            InputStream input = new BufferedInputStream(urlConnection.getInputStream());
            FileOutputStream imageOutput = new FileOutputStream(descFileName);
            byte[] buffer = new byte[1024 * 1024 * 5];// 5MB
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                imageOutput.write(buffer, 0, len);
            }
            //只适合小文件
//            byte[] bytes = ByteUtils.toByteArray(input);
//            FileOutputStream imageOutput = new FileOutputStream(descFileName);
//            imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
//            imageOutput.close();

            long end = System.currentTimeMillis();
            System.err.println("耗时：" + (end - begin) / 1000 + "秒");
            System.err.println("下载完成！");
        } catch (Exception e) {
            System.err.println("异常中止: " + e);
        }
    }

}

package code.sibyl.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.io.IOUtils;
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

        List<String> strings = Arrays.asList(

        );
        System.err.println(strings.size());
        strings.stream().forEach(item -> {
            try {
                bilibiliDownloadVideo(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
                buvid4=B0D8C4F1-19BA-C893-3C07-45A7F21D60B664363-023123112-o4ni5UDK5a3zSIFeUd70zQ%3D%3D; DedeUserID=11885873; DedeUserID__ckMd5=1c1d7cd5a933ec09; buvid_fp_plain=undefined; enable_web_push=DISABLE; header_theme_version=CLOSE; CURRENT_BLACKGAP=0; FEED_LIVE_VERSION=V_WATCHLATER_PIP_WINDOW; go-back-dyn=1; historyviewmode=list; buvid3=B98504AD-F503-5AD6-B9B4-57D8CBF3986758338infoc; b_nut=1735560337; _uuid=310462B75-4A28-9E3F-1F102-849AFE6B148932264infoc; hit-dyn-v2=1; CURRENT_QUALITY=80; enable_feed_channel=DISABLE; home_feed_column=5; rpdid=|(k||lRJlRJu0J'u~J)YYYY~R; LIVE_BUVID=AUTO6017386770844761; browser_resolution=2327-1228; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MzkzNTc0NzUsImlhdCI6MTczOTA5ODIxNSwicGx0IjotMX0.JoVNVHnrL_ELzdPZfu3uZVN3HgbXDtBlmpS1O19X0Vk; bili_ticket_expires=1739357415; CURRENT_FNVAL=4048; PVID=2; bsource=search_baidu; SESSDATA=7be150af%2C1754654392%2C43c05%2A22CjDNLuTvGjwgWhJtZGqMZ89WkWavSIBV-J1JwVQEDBPxzQ9ucv2MhHH80qGKs4TJSjESVjBBR1dUdGxIRlhUU3JnNDZjdGEyM0RWR0V0WlFhd2R2ZnhCbS16RkJ1WjdINUNVVnd1N1AxRTYxOUV1VC1GMmp3X3JkN3hObkV2VDEzdGU1VTVPcXl3IIEC; bili_jct=c0028f6886a232bd8564be627b4ffeb3; sid=pw7hvb4w; bp_t_offset_11885873=1031925988046405632; fingerprint=a1b14c043db0f76975753d85d58b71f6; buvid_fp=a1b14c043db0f76975753d85d58b71f6; b_lsid=44C17D8E_194EA9A874A
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

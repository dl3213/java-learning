package me.sibyl.util.bilibili;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.deepoove.poi.util.ByteUtils;
import me.sibyl.util.stream.StreamUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

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

    private static String getCookie() {
        return "buvid3=AF0A31A6-BC9A-05CC-89D3-1BF864F898BD50462infoc; i-wanna-go-back=-1; _uuid=BBF544A1-C62A-9F105-10F3D-8B8F6310510551050602infoc; buvid4=AA7A843C-D065-808C-C698-B3BDD5CE990D52798-023042618-+QGnwQAgbpquv09GHIJjKg==; b_nut=1682504553; DedeUserID=11885873; DedeUserID__ckMd5=1c1d7cd5a933ec09; hit-new-style-dyn=1; hit-dyn-v2=1; CURRENT_PID=735745f0-e41c-11ed-ae01-45e765b28087; rpdid=|(JRuY~muYYk0J'uY)kl|RYJ~; b_ut=5; LIVE_BUVID=AUTO4616825111741214; go-back-dyn=1; fingerprint=cf261abc460dde3f89a457c532225ffc; buvid_fp_plain=undefined; home_feed_column=5; CURRENT_QUALITY=80; nostalgia_conf=-1; CURRENT_FNVAL=4048; browser_resolution=1920-975; buvid_fp=cf261abc460dde3f89a457c532225ffc; bp_video_offset_11885873=791080938333274100; FEED_LIVE_VERSION=undefined; header_theme_version=undefined; innersign=1; SESSDATA=73bfdc9e,1698584838,697b4*52; bili_jct=8b286990b4fc56c5845b62346a58940f; sid=6eg3isbo; b_lsid=DA89EDE6_187DCA26E55; PVID=32";
    }

    private static void bilibiliDownloadVideo(String bvid) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
            System.err.println("bvid => " + bvid);
            HttpGet get = new HttpGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid);
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = StreamUtil.copyToString(content, Charset.forName("utf-8"));
            System.err.println("bvstring => " + string);
            StreamUtil.close(content);
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
                String urlStr = StreamUtil.copyToString(urlContent, Charset.forName("utf-8"));
                System.err.println("urlStr => " + urlStr);
                StreamUtil.close(urlContent);

                JSONObject urlJson = JSONObject.parseObject(urlStr);
                JSONObject urlData = urlJson.getJSONObject("data");
                JSONArray durls = urlData.getJSONArray("durl");
                JSONObject durl = durls.getJSONObject(0);
                String url = durl.getJSONArray("backup_url").getString(0);
                System.err.println("url => " + url);

                downloadNew(url, "D:\\"+ bvid + "-" + cid + ".mp4");

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

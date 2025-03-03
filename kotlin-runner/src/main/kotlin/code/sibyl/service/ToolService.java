package code.sibyl.service;

import code.sibyl.common.r;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToolService {

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public static ToolService getBean() {
        return r.getBean(ToolService.class);
    }

    public JSONObject bv_msg(String bvid) {

        String ret = "";

        try {
            HttpGet get = new HttpGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid);
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            ret = IOUtils.toString(content, Charset.forName("utf-8"));
            r.close(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return JSONObject.parseObject(ret);
    }

    public String bv_url(String bvid, String cookie) {

        String url = "";

        try {
            JSONObject bvJson = bv_msg(bvid);
            JSONObject data = bvJson.getJSONObject("data");
            String title = data.getString("title");
            JSONArray pages = data.getJSONArray("pages");
            for (int i = 0; i < pages.size(); i++) {
                JSONObject p = pages.getJSONObject(i);
                String cid = p.getString("cid");
                HttpGet vGet = new HttpGet("https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&qn=80");
                vGet.setHeader("cookie", cookie);
                CloseableHttpResponse urlResponse = httpClient.execute(vGet);

                InputStream urlContent = urlResponse.getEntity().getContent();
                String urlStr = IOUtils.toString(urlContent, Charset.forName("utf-8"));
                r.close(urlContent);
                JSONObject urlJson = JSONObject.parseObject(urlStr);
                JSONObject urlData = urlJson.getJSONObject("data");
                JSONArray durls = urlData.getJSONArray("durl");
                JSONObject durl = durls.getJSONObject(0);
                url = durl.getJSONArray("backup_url").getString(0);
                //url = "D:\\" + bvid + "-" + cid + ".mp4";
                //downloadNew(url, url);

                System.err.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return url;
    }

    public InputStream bv_download(String bvid, String cookie) {
        String urlPath = bv_url(bvid, cookie);
        InputStream input = null;
        try {
            //long begin = System.currentTimeMillis();
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Referer", "https://www.bilibili.com"); // 设置协议
            urlConnection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            urlConnection.setConnectTimeout(10 * 1000);

            //System.err.println("共:" + (urlConnection.getContentLength() / 1024) + "Kb");
            //System.err.println("开始下载...");
            //使用bufferedInputStream 缓存流的方式来获取下载文件，不然大文件会出现内存溢出的情况
            input = new BufferedInputStream(urlConnection.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}

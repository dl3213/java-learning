package code.sibyl.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.InputStream;
import java.nio.charset.Charset;

public class SteamService {

    //https://api.steamapis.com/steam/profile/76561199483616674?api_key=0E93DCE684003E07C471D6729E36E9F9
    public static void main(String[] args) {
        test();
    }

    public static void close(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    private static void test( ) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {

            HttpGet get = new HttpGet("https://api.steampowered.com/<界面>/<方法>/v<版本>/");
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = IOUtils.toString(content, Charset.forName("utf-8"));
            System.err.println("string => " + string);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

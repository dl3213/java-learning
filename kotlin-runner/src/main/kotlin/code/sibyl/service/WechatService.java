package code.sibyl.service;

import code.sibyl.cache.LocalCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WechatService {

    private final LocalCache localCache;

    public String authorize() {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + localCache.get("work-weixin-id") + "&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_privateinfo&state=STATE&agentid=" + localCache.get("work-weixin-agent-id") + "#wechat_redirect";
        return null;
    }

}

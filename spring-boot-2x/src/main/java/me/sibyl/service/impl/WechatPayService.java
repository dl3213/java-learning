package me.sibyl.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.service.PayAdapter;
import me.sibyl.util.SpringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("WECHAT_PAY")
@Slf4j
public class WechatPayService implements PayAdapter {
    public boolean support(String type) {
        return SpringUtil.getBean(type) instanceof WechatPayService;
    }

    @Override
    public String pay(BigDecimal amount, String type) {
        return "WECHAT" + System.currentTimeMillis();
    }
}

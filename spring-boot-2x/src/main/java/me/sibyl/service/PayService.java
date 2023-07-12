package me.sibyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayService {
    private final List<PayAdapter> payAdapterList;

    public String pay(String type) {
        System.err.println(payAdapterList);
        log.info(type);
        for (PayAdapter payAdapter : payAdapterList) {
            if (payAdapter.support(type)) {
                return payAdapter.pay(null, type);
            }
        }
        throw new RuntimeException("无此支付方式");
    }
}

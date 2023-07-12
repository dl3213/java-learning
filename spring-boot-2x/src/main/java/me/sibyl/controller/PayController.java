package me.sibyl.controller;

import lombok.RequiredArgsConstructor;
import me.sibyl.service.PayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("test")
    public String test(String type) {
        return payService.pay(type);
    }
}

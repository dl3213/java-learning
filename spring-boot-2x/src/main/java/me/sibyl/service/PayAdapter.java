package me.sibyl.service;

import java.math.BigDecimal;

public interface PayAdapter {

    boolean support(String type);

    String pay(BigDecimal amount, String type);
}

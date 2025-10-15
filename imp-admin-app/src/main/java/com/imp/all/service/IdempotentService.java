package com.imp.all.service;

/**
 * @author Longlin
 * @date 2021/4/23 15:15
 * @description
 */
public interface IdempotentService {

    String generateIdempotentToken(String value);

    boolean validIdempotentToken(String token, String value);
}


package com.central.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 幂等性异常
 *
 * @author zlt
 */
@Slf4j
public class IdempotencyException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    public IdempotencyException(String message) {
        super(message);
        log.warn("用户名已存在");
    }
}

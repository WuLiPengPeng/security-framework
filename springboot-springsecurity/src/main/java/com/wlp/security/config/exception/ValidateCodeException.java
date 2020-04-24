package com.wlp.security.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常，用于抛出特定的验证码异常
 */
public class ValidateCodeException  extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}

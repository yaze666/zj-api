package com.atuinfo.exception;

/**
 * 错误信息异常
 * @author dp
 * @version 1.0.0
 * @date 2019-08-01 10:50
 */
public class ErrorMassageException extends RuntimeException {
    public ErrorMassageException(String message) {
        super(message);
    }
    public ErrorMassageException() {
        super();
    }
}

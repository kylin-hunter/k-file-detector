package com.kylinhunter.plat.file.detector.exception;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-21 00:25
 **/
public class DetectException extends RuntimeException {
    public DetectException(String message) {
        super(message);
    }

    public DetectException(String message, Throwable cause) {
        super(message, cause);
    }
}

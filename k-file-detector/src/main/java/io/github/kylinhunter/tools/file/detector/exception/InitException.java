package io.github.kylinhunter.tools.file.detector.exception;

/**
 * @author BiJi'an
 * @description custom exception for detect
 * @date 2022-10-21 00:25
 **/
public class InitException extends DetectException {
    public InitException(String message) {
        super(message);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }
}

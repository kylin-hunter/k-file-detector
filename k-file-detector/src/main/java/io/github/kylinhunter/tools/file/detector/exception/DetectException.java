package io.github.kylinhunter.tools.file.detector.exception;

import io.github.kylinhunter.commons.exception.embed.biz.BizException;

/**
 * @author BiJi'an
 * @description custom exception for detect
 * @date 2022-10-21 00:25
 **/
public class DetectException extends BizException {
    public DetectException(String message) {
        super(message);
    }

    public DetectException(String message, Throwable cause) {
        super(message, cause);
    }
}

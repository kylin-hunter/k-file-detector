package io.github.kylinhunter.tools.file.detector.common.component;

import java.lang.reflect.Constructor;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-25 23:17
 **/
@Data
class CConstructor {
    private Class<?> clazz;
    private Constructor<?> constructor;
    private int depLevel;

    public CConstructor(Class<?> clazz, Constructor<?> constructor) {
        this.clazz = clazz;
        this.constructor = constructor;
    }

}

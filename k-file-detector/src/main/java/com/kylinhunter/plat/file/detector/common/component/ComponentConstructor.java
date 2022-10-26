package com.kylinhunter.plat.file.detector.common.component;

import java.lang.reflect.Constructor;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-25 23:17
 **/
@Data
public class ComponentConstructor implements Comparable<ComponentConstructor> {
    private Class<?> clazz;
    private int parameterCount;
    private Constructor<?> constructor;

    public ComponentConstructor(Class<?> clazz, Constructor<?> constructor) {
        this.clazz = clazz;
        this.constructor = constructor;
        this.parameterCount = constructor.getParameterCount();
    }

    @Override
    public int compareTo(ComponentConstructor o) {
        return this.parameterCount - o.parameterCount;
    }
}

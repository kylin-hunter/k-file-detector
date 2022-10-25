package com.kylinhunter.plat.file.detector.manager;

import java.lang.reflect.Constructor;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-25 23:17
 **/
@Data
public class ServiceConstructor implements Comparable<ServiceConstructor> {
    private Service service;
    private int parameterCount;
    private Constructor<?> constructor;

    public ServiceConstructor(Service service, Constructor<?> constructor) {
        this.service = service;
        this.constructor = constructor;
        this.parameterCount = constructor.getParameterCount();
    }

    @Override
    public int compareTo(ServiceConstructor o) {
        return this.parameterCount - o.parameterCount;
    }
}

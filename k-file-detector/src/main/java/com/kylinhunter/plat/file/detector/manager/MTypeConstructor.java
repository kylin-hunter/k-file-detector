package com.kylinhunter.plat.file.detector.manager;

import java.lang.reflect.Constructor;

import lombok.Data;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-25 23:17
 **/
@Data
public class MTypeConstructor implements Comparable<MTypeConstructor> {
    private MType mType;
    private int parameterCount;
    private Constructor<?> constructor;

    public MTypeConstructor(MType mType, Constructor<?> constructor) {
        this.mType = mType;
        this.constructor = constructor;
        this.parameterCount = constructor.getParameterCount();
    }

    @Override
    public int compareTo(MTypeConstructor o) {
        return this.parameterCount - o.parameterCount;
    }
}

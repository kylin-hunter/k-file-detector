package com.kylinhunter.plat.file.detector.manager;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.exception.InitException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class Managers {
    private static final Map<MType, Object> ALL_MANGERS = Maps.newHashMap();

    static {
        try {
            List<MTypeConstructor> allMTypeConstructors = Arrays.stream(MType.values()).map(mType -> {
                Class<?> clazz = mType.getClazz();
                Constructor<?>[] constructors = clazz.getConstructors();
                List<MTypeConstructor> mTypeConstructors = Lists.newArrayList();
                for (Constructor<?> c : constructors) {
                    mTypeConstructors.add(new MTypeConstructor(mType, c));
                }
                return mTypeConstructors;

            }).flatMap(Collection::stream).sorted().collect(Collectors.toList());

            Map<Class<?>, Object> clazzObjects = Maps.newHashMap();
            for (MTypeConstructor mTypeConstructor : allMTypeConstructors) {
                MType mType = mTypeConstructor.getMType();
                if (ALL_MANGERS.get(mType) == null) {
                    Constructor<?> constructor = mTypeConstructor.getConstructor();
                    int parameterCount = constructor.getParameterCount();
                    if (parameterCount == 0) {
                        Object obj = constructor.newInstance();
                        ALL_MANGERS.put(mType, obj);
                        clazzObjects.put(mType.getClazz(), obj);
                    } else {

                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameterObj = new Object[parameterCount];
                        for (int i = 0; i < parameterCount; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            Object obj = clazzObjects.get(parameterType);
                            if (obj != null) {
                                parameterObj[i] = obj;
                            } else {
                                parameterObj = null;
                                break;
                            }
                        }
                        if (parameterObj != null) {
                            Object obj = constructor.newInstance(parameterObj);

                            ALL_MANGERS.put(mType, obj);
                            clazzObjects.put(mType.getClazz(), obj);
                        }

                    }

                }
            }
            if (ALL_MANGERS.size() != MType.values().length) {
                throw new InitException("no all mtype manager be initialized ");

            }

        } catch (Exception e) {
            throw new InitException("init managers error", e);
        }
    }

    public static <T> T get(MType mType) {
        return get(mType, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(MType mType, boolean required) {
        Objects.requireNonNull(mType, "mType can't be null");
        Object manager = ALL_MANGERS.get(mType);
        if (manager == null && required) {
            throw new DetectException("no manager for :" + mType);
        }
        return (T) manager;
    }

}

package com.kylinhunter.plat.file.detector.common.component;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.exception.InitException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class ComponentFactory {
    private static final Map<Class<?>, Object> ALL_SERVICES = Maps.newHashMap();

    static {

        Reflections
                reflections = new Reflections("com.kylinhunter.plat.file.detector.*", Scanners.TypesAnnotated);
        Set<Class<?>> allComponents = reflections.getTypesAnnotatedWith(Component.class);

        try {

            List<ComponentConstructor> allComponentConstructors = allComponents.stream().map(clazz -> {
                Constructor<?>[] constructors = clazz.getConstructors();
                List<ComponentConstructor> componentConstructors = Lists.newArrayList();
                for (Constructor<?> c : constructors) {
                    componentConstructors.add(new ComponentConstructor(clazz, c));
                }
                return componentConstructors;

            }).flatMap(Collection::stream).sorted().collect(Collectors.toList());

            for (ComponentConstructor componentConstructor : allComponentConstructors) {
                Class<?> clazz = componentConstructor.getClazz();
                if (ALL_SERVICES.get(clazz) == null) {
                    Constructor<?> constructor = componentConstructor.getConstructor();
                    int parameterCount = constructor.getParameterCount();
                    if (parameterCount == 0) {
                        try {
                            ALL_SERVICES.put(clazz, constructor.newInstance());
                        } catch (Throwable e) {
                            throw new DetectException("init constructor error=>" + clazz.getName(), e);
                        }

                    } else {

                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameterObj = new Object[parameterCount];
                        for (int i = 0; i < parameterCount; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            Object obj = ALL_SERVICES.get(parameterType);
                            if (obj != null) {
                                parameterObj[i] = obj;
                            } else {
                                parameterObj = null;
                                break;
                            }
                        }
                        if (parameterObj != null) {

                            try {
                                ALL_SERVICES.put(clazz, constructor.newInstance(parameterObj));
                            } catch (Throwable e) {
                                throw new DetectException("init constructor error=>" + clazz.getName(), e);
                            }

                        }

                    }

                }
            }
            if (ALL_SERVICES.size() != allComponents.size()) {
                throw new InitException("no all  component be initialized ");

            }

        } catch (Throwable e) {
            throw new InitException("init managers error", e);
        }
    }

    public static <T, S extends T> T get(Class<S> clazz) {
        return get(clazz, true);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends T> T get(Class<S> clazz, boolean required) {
        Objects.requireNonNull(clazz, "clazz can't be null");
        Object manager = ALL_SERVICES.get(clazz);
        if (manager == null && required) {
            throw new DetectException("no component for :" + clazz);
        }
        return (T) manager;
    }

}

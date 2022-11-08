package com.kylinhunter.plat.file.detector.common.component;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kylinhunter.plat.file.detector.exception.InitException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class CF {
    private static final Map<Class<?>, Object> ALL_COMPONENTS = Maps.newHashMap();
    private static final Map<Class<?>, CConstructor> ALL_CCONSTRUCTORS = Maps.newHashMap();
    private static final Map<Class<?>, Set<Class<?>>> ALL_DEPENDENCIES = Maps.newHashMap();

    private static final String DEFAULT_PACKAGE = "com.kylinhunter.plat";

    static {
        init();
    }

    /**
     * @return void
     * @title init
     * @description
     * @author BiJi'an
     * @date 2022-11-08 21:38
     */
    private static void init() {

        Reflections reflections = new Reflections(DEFAULT_PACKAGE, Scanners.TypesAnnotated);
        Set<Class<?>> allComponentClazzes = reflections.getTypesAnnotatedWith(C.class);

        try {
            allComponentClazzes.forEach(c -> ALL_CCONSTRUCTORS.put(c, new CConstructor(c, c.getConstructors()[0])));
            for (Map.Entry<Class<?>, CConstructor> entry : ALL_CCONSTRUCTORS.entrySet()) {
                CConstructor CConstructor = entry.getValue();
                calDependencies(CConstructor);
            }

            List<CConstructor> sortedCConstructors = ALL_CCONSTRUCTORS.values().stream()
                    .sorted(Comparator.comparingInt(CConstructor::getDepLevel)).collect(Collectors.toList());

            for (CConstructor cconstructor : sortedCConstructors) {
                Constructor<?> constructor = cconstructor.getConstructor();
                Class<?> clazz = cconstructor.getClazz();
                int parameterCount = constructor.getParameterCount();
                if (parameterCount <= 0) {

                    try {
                        ALL_COMPONENTS.put(clazz, constructor.newInstance());
                    } catch (Exception e) {
                        throw new InitException("init constructor error:" + clazz.getName(), e);
                    }
                } else {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] parameterObj = new Object[parameterCount];
                    for (int i = 0; i < parameterCount; i++) {
                        Class<?> parameterType = parameterTypes[i];
                        Object obj = ALL_COMPONENTS.get(parameterType);
                        if (obj != null) {
                            parameterObj[i] = obj;
                        } else {
                            throw new InitException("no component:" + clazz.getName() + "/" + parameterType.getName());
                        }
                    }

                    try {
                        ALL_COMPONENTS.put(clazz, constructor.newInstance(parameterObj));
                    } catch (Exception e) {
                        throw new InitException("init constructor error:" + clazz.getName(), e);
                    }
                }

            }

            if (ALL_COMPONENTS.size() != allComponentClazzes.size()) {
                throw new InitException("no all  component be initialized ");

            }

        } catch (Throwable e) {
            throw new InitException("init component factory error", e);
        }

    }

    /**
     * @param cconstructor cconstructor
     * @return void
     * @title calDependencies
     * @description
     * @author BiJi'an
     * @date 2022-11-08 20:06
     */
    private static void calDependencies(CConstructor cconstructor) {

        Class<?> clazz = cconstructor.getClazz();
        calDependencies(clazz, clazz);
        cconstructor.setDepLevel(ALL_DEPENDENCIES.get(clazz).size());
    }

    /**
     * @param oriClazz oriClazz
     * @param depClazz depClazz
     * @return void
     * @title calDependencies
     * @description
     * @author BiJi'an
     * @date 2022-11-08 20:21
     */
    private static void calDependencies(Class<?> oriClazz, Class<?> depClazz) {
        CConstructor cconstructor = ALL_CCONSTRUCTORS.get(depClazz);
        if (cconstructor == null) {
            throw new InitException("no cconstructor for :" + depClazz.getName());
        }
        Constructor<?> constructor = cconstructor.getConstructor();
        ALL_DEPENDENCIES.compute(oriClazz, (k, v) -> {
            if (v == null) {
                v = Sets.newHashSet();
            }
            v.add(depClazz);
            return v;
        });
        if (constructor.getParameterCount() > 0) {
            for (Class<?> paramClazz : constructor.getParameterTypes()) {
                calDependencies(oriClazz, paramClazz);
            }
        }
    }

    /**
     * @param clazz clazz
     * @return T
     * @title get
     * @description
     * @author BiJi'an
     * @date 2022-11-08 20:06
     */
    public static <T, S extends T> T get(Class<S> clazz) {
        return get(clazz, true);
    }

    /**
     * @param clazz    clazz
     * @param required required
     * @return T
     * @title get
     * @description
     * @author BiJi'an
     * @date 2022-11-08 20:06
     */
    @SuppressWarnings("unchecked")
    public static <T, S extends T> T get(Class<S> clazz, boolean required) {
        Objects.requireNonNull(clazz, "clazz can't be null");
        Object manager = ALL_COMPONENTS.get(clazz);
        if (manager == null && required) {
            throw new InitException("no component for :" + clazz);
        }
        return (T) manager;
    }

}

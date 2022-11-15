package io.github.kylinhunter.tools.file.detector.common.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.kylinhunter.tools.file.detector.exception.InitException;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-22 01:48
 **/
public class CF {
    private static final Map<Class<?>, Object> ALL_COMPONENTS = Maps.newHashMap();
    private static final Map<Class<?>, Set<Object>> ALL_I_COMPONENTS = Maps.newHashMap();
    private static final Map<Class<?>, CConstructor> ALL_CCONSTRUCTORS = Maps.newHashMap();
    private static final Map<Class<?>, Set<CConstructor>> ALL_I_CCONSTRUCTORS = Maps.newHashMap();
    private static final Map<Class<?>, Set<Class<?>>> ALL_DEPENDENCIES = Maps.newHashMap();

    private static final String DEFAULT_PACKAGE = "io.github.kylinhunter.tools.file";

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
            allComponentClazzes.forEach(c -> {
                CConstructor cconstructor = new CConstructor(c, c.getConstructors()[0]);
                ALL_CCONSTRUCTORS.put(c, cconstructor);
                Class<?>[] interfaces = c.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (anInterface.getName().startsWith(DEFAULT_PACKAGE)) {
                        ALL_I_CCONSTRUCTORS.compute(anInterface, (k, v) -> {
                            if (v == null) {
                                v = Sets.newHashSet();
                            }
                            v.add(cconstructor);
                            return v;
                        });
                    }

                }
            });
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
                    Type[] genericParameterTypes = constructor.getGenericParameterTypes();
                    Object[] parameterObj = new Object[parameterCount];
                    for (int i = 0; i < parameterCount; i++) {
                        Class<?> parameterType = parameterTypes[i];
                        Object obj = ALL_COMPONENTS.get(parameterType);
                        if (obj != null) {
                            parameterObj[i] = obj;
                        } else {
                            Type type = genericParameterTypes[i];
                            if (type instanceof ParameterizedType) {
                                //真实的参数类型
                                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                                for (Type actualTypeArgument : actualTypeArguments) {
                                    Set<CConstructor> allCConstructors = ALL_I_CCONSTRUCTORS.get(actualTypeArgument);
                                    List<Object> objs = Lists.newArrayList();
                                    if (allCConstructors != null) {
                                        for (CConstructor tmpCConstructor : allCConstructors) {
                                            Object tmpObj = ALL_COMPONENTS.get(tmpCConstructor.getClazz());
                                            if (tmpObj != null) {
                                                objs.add(tmpObj);
                                            }
                                        }
                                    }
                                    if(objs.size()>0){
                                        parameterObj[i] = objs;
                                    }

                                }
                            }

                            if (parameterObj[i] == null) {
                                throw new InitException(
                                        "no component:" + clazz.getName() + "/" + parameterType.getName());

                            }

                        }
                    }

                    try {
                        ALL_COMPONENTS.put(clazz, constructor.newInstance(parameterObj));
                    } catch (Exception e) {
                        throw new InitException("init constructor error:" + clazz.getName(), e);
                    }
                }

            }

            for (Class<?> componentClazz : allComponentClazzes) {
                Object component = ALL_COMPONENTS.get(componentClazz);
                if (component != null) {
                    Class<?>[] interfaces = componentClazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        if (i.getPackage().getName().startsWith(DEFAULT_PACKAGE)) {
                            ALL_I_COMPONENTS.compute(i, (k, v) -> {
                                if (v == null) {
                                    v = Sets.newHashSet();
                                }
                                v.add(component);
                                return v;
                            });

                        }
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
        calDependencies(clazz, clazz, null);
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
    private static void calDependencies(Class<?> oriClazz, Class<?> depClazz, Type type) {
        Set<CConstructor> allCConstructors = Sets.newHashSet();
        CConstructor existCConstructor = ALL_CCONSTRUCTORS.get(depClazz);
        if (existCConstructor != null) {
            allCConstructors.add(existCConstructor);

        } else {
            if (type instanceof ParameterizedType) {
                //真实的参数类型
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    allCConstructors = ALL_I_CCONSTRUCTORS.get(actualTypeArgument);
                    if (allCConstructors != null) {
                        break;
                    }
                }
            }
            if (allCConstructors == null) {
                throw new InitException("no existCConstructor for :" + depClazz.getName());

            }
        }

        for (CConstructor cconstructor : allCConstructors) {

            ALL_DEPENDENCIES.compute(oriClazz, (k, v) -> {
                if (v == null) {
                    v = Sets.newHashSet();
                }
                v.add(cconstructor.getClazz());
                return v;
            });
            Constructor<?> constructor = cconstructor.getConstructor();

            if (constructor.getParameterCount() > 0) {
                Type[] genericParameterTypes = constructor.getGenericParameterTypes();

                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    calDependencies(oriClazz, parameterTypes[i], genericParameterTypes[i]);

                }

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

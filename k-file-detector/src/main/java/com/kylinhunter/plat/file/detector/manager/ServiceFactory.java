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
public class ServiceFactory {
    private static final Map<Service, Object> ALL_SERVICES = Maps.newHashMap();

    static {
        try {
            List<ServiceConstructor> allServiceConstructors = Arrays.stream(Service.values()).map(mType -> {
                Class<?> clazz = mType.getClazz();
                Constructor<?>[] constructors = clazz.getConstructors();
                List<ServiceConstructor> serviceConstructors = Lists.newArrayList();
                for (Constructor<?> c : constructors) {
                    serviceConstructors.add(new ServiceConstructor(mType, c));
                }
                return serviceConstructors;

            }).flatMap(Collection::stream).sorted().collect(Collectors.toList());

            Map<Class<?>, Object> clazzObjects = Maps.newHashMap();
            for (ServiceConstructor serviceConstructor : allServiceConstructors) {
                Service service = serviceConstructor.getService();
                if (ALL_SERVICES.get(service) == null) {
                    Constructor<?> constructor = serviceConstructor.getConstructor();
                    int parameterCount = constructor.getParameterCount();
                    if (parameterCount == 0) {
                        Object obj = constructor.newInstance();
                        ALL_SERVICES.put(service, obj);
                        clazzObjects.put(service.getClazz(), obj);
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

                            ALL_SERVICES.put(service, obj);
                            clazzObjects.put(service.getClazz(), obj);
                        }

                    }

                }
            }
            if (ALL_SERVICES.size() != Service.values().length) {
                throw new InitException("no all mtype manager be initialized ");

            }

        } catch (Exception e) {
            throw new InitException("init managers error", e);
        }
    }

    public static <T> T get(Service service) {
        return get(service, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Service service, boolean required) {
        Objects.requireNonNull(service, "service can't be null");
        Object manager = ALL_SERVICES.get(service);
        if (manager == null && required) {
            throw new DetectException("no manager for :" + service);
        }
        return (T) manager;
    }

}

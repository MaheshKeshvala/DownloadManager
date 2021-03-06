package com.mk.library.Manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DownloadFactory {
    private static Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

    public static <T> T getService(Class<T> tClass) {
        return (T) serviceMap.get(tClass);
    }

    public static <T> void addService(Class<T> serviceClass, T service) {
        serviceMap.put(serviceClass, service);
    }

    public static int getServiceCount() {
        return serviceMap.size();
    }
}

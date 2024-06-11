package com.arthur.common.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拓展Dubbo的SPI机制ExtensionLoader，简化功能保留核心代码
 *
 * @author DearYang
 * @date 2022-07-15
 * @since 1.0
 */
public class ExtensionLoader<T> {

    public static final String DEFAULT_SPI_FILE_PATH = "META-INF/arthur/";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();
    private final Map<String, Object> cacheInstances = new ConcurrentHashMap<>();
    private final Class<T> type;

    public ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }
        if (!withAnnotation(type)) {
            throw new IllegalArgumentException("type must annotated @SPI");
        }

        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            loader = new ExtensionLoader<>(type);
            EXTENSION_LOADERS.putIfAbsent(type, loader);
        }
        return loader;
    }

    public static <T> boolean withAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name == null");
        }

        Object instance = cacheInstances.get(name);
        if (instance == null) {
            synchronized (cacheInstances) {
                instance = cacheInstances.get(name);
                if (instance == null) {
                    instance = createExtension(name);
                    cacheInstances.putIfAbsent(name, instance);
                }
            }
        }

        return (T) instance;
    }

    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            EXTENSION_LOADERS.remove(type);
            throw new IllegalStateException("not found " + name);
        }

        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                instance = (T) clazz.getDeclaredConstructor().newInstance();
                EXTENSION_INSTANCES.put(clazz, instance);
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Extension instance (name: " + name + ", class: " +
                    type + ") couldn't be instantiated: " + e.getMessage(), e);
        }
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cacheClasses.get();
        if (classes == null) {
            synchronized (cacheClasses) {
                classes = cacheClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cacheClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        Map<String, Class<?>> extendClasses = new HashMap<>(10);
        loadDirectory(extendClasses, type.getName());
        return extendClasses;
    }

    private void loadDirectory(Map<String, Class<?>> extendClasses, String type) {
        String fileName = DEFAULT_SPI_FILE_PATH + type;
        ClassLoader classLoader = findClassLoader();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResource(extendClasses, url, classLoader);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadResource(Map<String, Class<?>> extendClasses, URL url, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 过滤#注释信息
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String name = null, clazz;
                int indexOfName = line.indexOf("=");
                if (indexOfName > 0) {
                    name = line.substring(0, indexOfName);
                    clazz = line.substring(indexOfName + 1);
                } else {
                    clazz = line;
                }
                if (clazz.length() > 0) {
                    loadClass(extendClasses, Class.forName(clazz, true, classLoader), name);
                }
            }
        }
    }

    private void loadClass(Map<String, Class<?>> extendClasses, Class<?> clazz, String name) {
        Class<?> aClass = extendClasses.get(name);
        if (aClass == null) {
            extendClasses.put(name, clazz);
        } else if (aClass != clazz) {
            throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + aClass.getName() + " and " + clazz.getName());
        }
    }

    private ClassLoader findClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (loader != null) {
            return loader;
        }

        loader = ExtensionLoader.class.getClassLoader();
        if (loader == null) {
            try {
                loader = ClassLoader.getSystemClassLoader();
            } catch (Throwable ignored) {
            }
        }
        return loader;
    }

    public static class Holder<T> {

        private volatile T value;

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

    }

}

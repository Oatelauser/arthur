package com.arthur.common.spi;

import java.util.Optional;

/**
 * create {@link ExtensionLoader} instance
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
public class StandardExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(String key, Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(loader -> loader.getExtension(key))
                .orElse(null);
    }

    @Override
    public <T> T getDefaultExtension(Class<T> clazz) {
        return null;
    }

}

package com.arthur.boot.env;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Spring的配置加载器拓展
 *
 * @author DearYang
 * @date 2022-08-03
 * @see PropertySourceLoader
 * @since 1.0
 */
public abstract class AbstractPropertySourceLoader implements PropertySourceLoader {

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        if (!canLoad(name, resource)) {
            return Collections.emptyList();
        }

        return loadSource(name, resource);
    }

    /**
     * 是否可以加载配置
     *
     * @param name     果加载了多个文档，则应在每个加载源的名称中添加一个附加后缀
     * @param resource 要加载的资源
     * @return 是否可以加载资源
     */
    @SuppressWarnings("unused")
    protected boolean canLoad(String name, Resource resource) {
        return true;
    }

    /**
     * 加载资源源
     *
     * @param name     资源名
     * @param resource 加载的资源
     * @return {@link PropertySource}
     * @throws IOException IO异常
     */
    protected abstract List<PropertySource<?>> loadSource(String name, Resource resource) throws IOException;

}

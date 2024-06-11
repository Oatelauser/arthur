package com.arthur.boot.env;

import com.arthur.common.utils.CollectionUtils;
import com.arthur.common.utils.JsonUtils;
import com.arthur.common.utils.MapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * JSON配置文件加载器
 *
 * @author DearYang
 * @date 2022-08-03
 * @since 1.0
 */
public class JsonPropertySourceLoader extends AbstractPropertySourceLoader {

    private static final CollectionType collectionType;

    static {
        ObjectMapper objectMapper = JsonUtils.getMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        MapType constructMapType = typeFactory.constructMapType(LinkedHashMap.class, String.class, Object.class);
        collectionType = typeFactory.constructCollectionType(ArrayList.class, constructMapType);
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"json"};
    }

    @Override
    protected List<PropertySource<?>> loadSource(String name, Resource resource) throws IOException {
        List<Map<String, Object>> configDataList = JsonUtils.parseObject(resource.getInputStream(), collectionType);
        if (CollectionUtils.isEmpty(configDataList)) {
            return Collections.emptyList();
        }

        ArrayList<PropertySource<?>> sources = new ArrayList<>(configDataList.size());
        for (Map<String, Object> configData : configDataList) {
            Map<String, Object> result = MapUtils.flattenedMap(configData);
            sources.add(new OriginTrackedMapPropertySource(name, Collections.unmodifiableMap(result), true));

        }
        return sources;
    }

}

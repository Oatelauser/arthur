package com.arthur.lang.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.arthur.lang.ArthurLangConstants.DOT;


/**
 * Map工具
 *
 * @author DearYang
 * @date 2022-08-03
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class MapUtils {

    /**
     * 将嵌套的Map转换为单层key的Map
     * <p>
     * {data.address[2]=3, data.address[1]=2, data.address[0]=1, data.name=Alice}
     *
     * @param dataMap 需要转换为单层key的嵌套Map
     */
    public static Map<String, Object> flattenedMap(Map<String, Object> dataMap) {
        Map<String, Object> result = new LinkedHashMap<>(32);
        flattenedMap(result, dataMap, null);
        return result;
    }

    /**
     * 将嵌套的Map转换为单层key的Map
     *
     * @param result    单纯key结构的Map
     * @param dataMap   需要转换为单层key的嵌套Map
     * @param parentKey 上级key
     */
    @SuppressWarnings("unchecked")
    public static void flattenedMap(Map<String, Object> result, Map<String, Object> dataMap, String parentKey) {
        if (dataMap == null || dataMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String fullKey = StringUtils.isEmpty(parentKey) ? key : key.startsWith("[") ?
                    parentKey.concat(key) : parentKey.concat(DOT).concat(key);

            if (value instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) value;
                flattenedMap(result, map, fullKey);
                continue;
            } else if (value instanceof Collection) {
                int count = 0;
                Collection<Object> collection = (Collection<Object>) value;
                for (Object object : collection) {
                    flattenedMap(result, Collections.singletonMap("[" + (count++) + "]", object), fullKey);
                }
                continue;
            }

            result.put(fullKey, value);
        }
    }

    /**
     * 判断Map是否为空
     *
     * @param map {@link Map}
     * @return 是否为空
     */
    public static boolean isEmpty(Map map) {
        return map != null && map.isEmpty();
    }

}

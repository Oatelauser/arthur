package com.arthur.common.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.arthur.common.constant.BaseConstants.DOT;

public class MapUtilsTest {

    protected static Map<String, Object> reloadMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>(map);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.contains(DOT)) {
                int idx = key.lastIndexOf(DOT);
                String suffix = key.substring(idx + 1);
                if ("value".equalsIgnoreCase(suffix)) {
                    result.put(key.substring(0, idx), entry.getValue());
                }
            }
        }
        return result;
    }

    @Test
    public void flattenedMapTest() {
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        map.put("value", "123456");
        map.put("address", Arrays.asList(1, 2, 3));
        root.put("data", map);

        Map<String, Object> result = MapUtils.flattenedMap(root);
        result = reloadMap(result);
        System.out.println(result);
    }

}

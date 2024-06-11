package com.arthur.boot.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * MybatisPlus 自动填充配置
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        fillIfNullByName("createTime", now, metaObject, false);
        fillIfNullByName("updateTime", now, metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     * @param fieldName 属性名
     * @param field 属性值
     * @param metaObject MetaObject
     * @param isCover 是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillIfNullByName(String fieldName, Object field, MetaObject metaObject, boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }

        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = ObjectUtils.nullSafeToString(userSetValue);
        if (!StringUtils.hasText(setValueStr) && !isCover) {
            return;
        }

        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, field)) {
            metaObject.setValue(fieldName, field);
        }
    }

}

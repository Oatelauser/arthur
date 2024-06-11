package com.arthur.common.utils;


public class ClassUtilsTest {

    public void isPrimitiveTest() {
        Assert.isTrue(!ClassUtils.isPrimitive(Boolean.class), "Boolean not primitive class");
        Assert.isTrue(ClassUtils.isPrimitive(int.class), "int is primitive class");
        Assert.isTrue(!ClassUtils.isPrimitive(String.class), "String not primitive class");
    }

}

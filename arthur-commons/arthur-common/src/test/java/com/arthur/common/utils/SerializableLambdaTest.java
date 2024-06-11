package com.arthur.common.utils;

public class SerializableLambdaTest {


    static interface Function2<R, T1, T2> extends SerializableLambda {
        R apply(T1 t1, T2 t2);
    }

    static class A {
        public A(Integer num, String str) {

        }
    }

    public static void main(String[] args) {
        testSerializableLambda((Integer a, Double b) -> a + "," + b);
        testSerializableLambda(SerializableLambdaTest::div);
        testSerializableLambda(A::new);
    }

    private static <R, T1, T2> void testSerializableLambda(Function2<R, T1, T2> lambda) {
        System.out.println("参数类型：" + lambda.getParameterTypes());
        System.out.println("返回值类型：" + lambda.getReturnType());
    }

    private static double div(int a, int b) {
        return a * 1.0 / b;
    }



}

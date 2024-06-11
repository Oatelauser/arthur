package com.arthur.lang;

import com.arthur.lang.invoke.Lookups;
import com.arthur.lang.lambda.LambdaBridger;
import com.arthur.lang.utils.IOUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static java.lang.invoke.MethodType.methodType;

/**
 * 高性能字符串，只支持JDK11+
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-25
 * @since 1.0
 */
public abstract class FastString {

    public static final byte LATIN1 = 0;
    public static final byte UTF16 = 1;
    public static final ToIntFunction<String> STRING_CODER;
    public static final Function<String, byte[]> STRING_VALUE;
    public static final BiFunction<byte[], Charset, String> STRING_CREATOR;
    public static final BiFunction<byte[], Byte, String> STRING_CONSTRUCTOR;

    static {
        ToIntFunction<String> stringCoder;
        Function<String, byte[]> stringValue;
        BiFunction<byte[], Charset, String> stringCreator;
        BiFunction<byte[], Byte, String> stringConstructor;

        Lookup lookup = Lookups.trustedLookup(String.class);
        try {
            stringValue = getStringValue(lookup);
            stringCoder = getStringCoder(lookup);
            stringCreator = getStringCreator(lookup);
            stringConstructor = getStringConstructor(lookup);
        } catch (Throwable t) {
            stringCreator = null;
            stringValue = null;
            stringCoder = null;
            stringConstructor = null;
        }
        STRING_VALUE = stringValue;
        STRING_CODER = stringCoder;
        STRING_CREATOR = stringCreator;
        STRING_CONSTRUCTOR = stringConstructor;
    }

    /**
     * 高性能UTF8编码字符串
     *
     * @param src 原始字符串
     * @return UTF8编码字符串
     * @see #encodeUTF8(byte[])
     */
    public static String encodeUTF8(String src) {
        return STRING_CODER.applyAsInt(src) == 0 ? src : encodeUTF8(STRING_VALUE.apply(src));
    }

    /**
     * 高性能UTF8编码字节数组
     *
     * @param buf 字节数组
     * @return UTF8编码字符串
     */
    public static String encodeUTF8(byte[] buf) {
        byte[] bytes = new byte[buf.length * 3];
        int len = IOUtils.encodeUTF8(buf, 0, buf.length, bytes, 0);
        bytes = Arrays.copyOf(bytes, len);
        return STRING_CREATOR.apply(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 解码UTF8字节数组
     *
     * @param buf 字节数组
     * @return 解码UTF8字符串
     */
    public static String decodeUTF8(byte[] buf) {
        byte[] bytes = new byte[buf.length << 1];
        int len = IOUtils.decodeUTF8(buf, 0, buf.length, bytes);
        bytes = Arrays.copyOf(bytes, len);
        return STRING_CREATOR.apply(bytes, StandardCharsets.US_ASCII);
    }

    @SuppressWarnings("unchecked")
    private static BiFunction<byte[], Charset, String> getStringCreator(Lookup lookup) throws Throwable {
        MethodHandle handle = lookup.findStatic(String.class, "newStringNoRepl1",
                methodType(String.class, byte[].class, Charset.class));
        return (BiFunction<byte[], Charset, String>) LambdaBridger.createLambda(BiFunction.class, lookup, handle)
                .getTarget().invokeExact();
    }

    @SuppressWarnings("unchecked")
    private static Function<String, byte[]> getStringValue(Lookup lookup) throws Throwable {
        MethodHandle handle = lookup.findSpecial(String.class, "value",
                methodType(byte[].class), String.class);
        return (Function<String, byte[]>) LambdaBridger.createLambda(Function.class, lookup, handle)
                .getTarget().invokeExact();
    }

    @SuppressWarnings("unchecked")
    private static ToIntFunction<String> getStringCoder(Lookup lookup) throws Throwable {
        MethodHandle handle = lookup.findSpecial(String.class, "coder",
                methodType(byte.class), String.class);
        return (ToIntFunction<String>) LambdaBridger.createLambda(ToIntFunction.class, lookup, handle)
                .getTarget().invokeExact();
    }

    @SuppressWarnings("unchecked")
    private static BiFunction<byte[], Byte, String> getStringConstructor(Lookup lookup) throws Throwable {
        MethodHandle handle = lookup.findConstructor(String.class, methodType(void.class, byte[].class, byte.class));
        return (BiFunction<byte[], Byte, String>) LambdaBridger.createLambda(BiFunction.class, lookup, handle)
                .getTarget().invokeExact();
    }

}

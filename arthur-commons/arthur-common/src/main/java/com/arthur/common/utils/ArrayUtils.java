package com.arthur.common.utils;

/**
 * array utils
 *
 * @author DearYang
 * @date 2022-07-13
 * @since 1.0
 */
public class ArrayUtils {

    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     * @return true-success,fail-empty
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }


}

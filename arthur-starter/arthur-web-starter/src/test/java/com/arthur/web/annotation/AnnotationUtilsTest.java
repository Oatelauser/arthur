package com.arthur.web.annotation;

import com.arthur.boot.utils.AnnotationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-18
 * @since 1.0
 */
public class AnnotationUtilsTest {

    @ExceptionResponse(value = RuntimeException.class, code = "C1")
    @ExceptionResponse(code = "C2")
    @ExceptionResponse(value = Throwable.class, code = "C3")
    static class A {

        //@ExceptionResponse(value = RuntimeException.class, code = "M1", msg = "M1")
        //@ExceptionResponse(code = "M2")
        //@ExceptionResponse(value = Throwable.class, code = "M3")
        @ExceptionResponses({
                @ExceptionResponse(value = RuntimeException.class, code = "M1", msg = "M1"),
                @ExceptionResponse(code = "M2"),
                @ExceptionResponse(value = Throwable.class, code = "M3")
        })
        public void a() {

        }

    }

    @Test
    void t() throws Exception {
        Method method = A.class.getMethod("a");
        Set<ExceptionResponse> repeatableAnnotations = AnnotationUtils.findMethodMergedRepeatableAnnotations(
                method, ExceptionResponse.class, ExceptionResponses.class);
        Set<ExceptionResponse> repeatableAnnotations1 = AnnotationUtils.findClassMergedRepeatableAnnotations(
                method.getDeclaringClass(), ExceptionResponse.class, ExceptionResponses.class);
        repeatableAnnotations.addAll(repeatableAnnotations1);

        int a = 1;
    }

}

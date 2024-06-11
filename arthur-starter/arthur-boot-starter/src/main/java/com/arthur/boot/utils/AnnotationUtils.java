package com.arthur.boot.utils;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.core.annotation.AnnotatedElementUtils.*;
import static org.springframework.core.annotation.AnnotationUtils.*;

/**
 * <ul>拓展{@link org.springframework.core.annotation.AnnotationUtils}新增注解解析方法
 *     <li>1.提供针对{@link MethodParameter}的注解解析</li>
 *     <li>2.基于多个相同的注解融合属性</li>
 *     <li>3.基于注解范围{@link ElementType}，拓展方法注解的查找范围：方法 -> 方法所在类</li>
 *     <li>4.拓展方法注解的融合查找，融合范围拓展：方法 + 类</li>
 * </ul>
 *
 * @author DearYang
 * @date 2022-09-30
 * @see org.springframework.core.annotation.AnnotationUtils
 * @see org.springframework.core.annotation.AnnotatedElementUtils
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AnnotationUtils {

	/**
	 * 基于find语义查找方法参数的注解
	 *
	 * @param annotationClass 注解类型
	 * @param parameter       方法参数{@link MethodParameter}
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T findMethodParameterAnnotation(
			Class<T> annotationClass, MethodParameter parameter) {
		T annotation = parameter.getParameterAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		// 遍历当前方法参数上所有的注解
		for (Annotation searchAnnotation : parameter.getParameterAnnotations()) {
			annotation = findAnnotation(searchAnnotation.annotationType(), annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}

		return null;
	}

	/**
	 * 基于get语义查找方法参数的注解
	 *
	 * @param annotationClass 注解类型
	 * @param parameter       方法参数{@link MethodParameter}
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T getMethodParameterAnnotation(Class<T> annotationClass,
			MethodParameter parameter) {
		T annotation = parameter.getParameterAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		// 遍历当前方法参数上所有的注解
		for (Annotation searchAnnotation : parameter.getParameterAnnotations()) {
			annotation = getAnnotation(searchAnnotation.annotationType(), annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}

		return null;
	}

	/**
	 * 基于find语义查找方法的注解
	 * <p>
	 * 1.查找当前方法
	 * 2.查找方法所在的类
	 *
	 * @param annotationClass 注解类型
	 * @param returnType      方法返回对象的{@link MethodParameter}
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
			MethodParameter returnType) {
		T annotation = returnType.getMethodAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		Method method = returnType.getMethod();
		Assert.notNull(method, "Current MethodParameter has not of method: " + returnType);
		annotation = findAnnotation(method, annotationClass);
		if (annotation != null) {
			return annotation;
		}

		if (!isTypeScope(annotationClass)) {
			return null;
		}
		return findAnnotation(returnType.getContainingClass(), annotationClass);
	}

	/**
	 * 基于get语义查找方法的注解
	 * <p>
	 * 1.查找当前方法
	 * 2.查找方法所在的类
	 *
	 * @param annotationClass 注解类型
	 * @param returnType      方法返回对象的{@link MethodParameter}
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T getMethodAnnotation(Class<T> annotationClass,
			MethodParameter returnType) {
		T annotation = returnType.getMethodAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		Method method = returnType.getMethod();
		Assert.notNull(method, "Current MethodParameter has not of method: " + returnType);
		annotation = getAnnotation(method, annotationClass);
		if (annotation != null) {
			return annotation;
		}

		if (!isTypeScope(annotationClass)) {
			return null;
		}
		return getAnnotation(returnType.getContainingClass(), annotationClass);
	}

	/**
	 * 基于find语义和注解作用范围查找方法的注解
	 * <p>
	 * 1.查找当前方法
	 * 2.查找方法所在的类
	 *
	 * @param annotationClass 注解类型
	 * @param method      	  方法
	 * @param targetClass     方法所在的类
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T findMethodAnnotation(Method method, Class<T> annotationClass,
			@Nullable Class<?> targetClass) {
		T annotation = findAnnotation(method, annotationClass);
		if (annotation != null) {
			return annotation;
		}
		if (!isTypeScope(annotationClass)) {
			return null;
		}

		targetClass = targetClass == null ? method.getDeclaringClass() : targetClass;
		return findAnnotation(targetClass, annotationClass);
	}

	/**
	 * 基于get语义查找方法的注解
	 * <p>
	 * 1.查找当前方法
	 * 2.查找方法所在的类
	 *
	 * @param annotationClass 注解类型
	 * @param method      	  方法
	 * @param targetClass     方法所在的类
	 * @param <T>             注解泛型
	 * @return 查找的注解对象，可能为null
	 */
	@Nullable
	public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annotationClass,
			@Nullable Class<?> targetClass) {
		T annotation = getAnnotation(method, annotationClass);
		if (annotation != null) {
			return annotation;
		}
		if (!isTypeScope(annotationClass)) {
			return null;
		}

		targetClass = targetClass == null ? method.getDeclaringClass() : targetClass;
		return getAnnotation(targetClass, annotationClass);
	}

	/**
	 * 基于find语义，查找所有（方法+类）的重复注解
	 *
	 * @param method         注解所在的方法
	 * @param annotationType 重复注解类型
	 * @param containerType  重复注解容器
	 * @param <T>            重复注解的类型泛型
	 * @return 重复注解集合
	 */
	public static <T extends Annotation> Set<T> findAllMergedRepeatableAnnotations(Method method,
   			Class<T> annotationType, Class<? extends Annotation> containerType) {
		// 方法上查找
		Set<T> annotations = findAllMergedAnnotations(method, annotationType);
		Set<T> repeatableAnnotations = findMergedRepeatableAnnotations(method, annotationType, containerType);
		annotations.addAll(repeatableAnnotations);

		// 判断注解的范围
		if (!isTypeScope(annotationType)) {
			return annotations;
		}

		// 类上查找
		Class<?> declaringClass = method.getDeclaringClass();
		Set<T> classAnnotations = findAllMergedAnnotations(declaringClass, annotationType);
		annotations.addAll(classAnnotations);
		Set<T> repeatableClassAnnotations = findMergedRepeatableAnnotations(declaringClass,
			annotationType, containerType);
		annotations.addAll(repeatableClassAnnotations);

		return annotations;
	}

	/**
	 * 基于find语义，查找方法上的重复注解
	 *
	 * @param method         注解所在的方法
	 * @param annotationType 重复注解类型
	 * @param containerType  重复注解容器
	 * @param <T>            重复注解的类型泛型
	 * @return 重复注解集合
	 */
	public static <T extends Annotation> Set<T> findMethodMergedRepeatableAnnotations(Method method,
		Class<T> annotationType, Class<? extends Annotation> containerType) {
		// 方法上查找
		Set<T> annotations = findAllMergedAnnotations(method, annotationType);
		Set<T> repeatableAnnotations = findMergedRepeatableAnnotations(method, annotationType, containerType);
		annotations.addAll(repeatableAnnotations);
		return annotations;
	}

	/**
	 * 基于find语义，查找类上的重复注解
	 *
	 * @param declaringClass 注解所在的方法
	 * @param annotationType 重复注解类型
	 * @param containerType  重复注解容器
	 * @param <T>            重复注解的类型泛型
	 * @return 重复注解集合
	 */
	public static <T extends Annotation> Set<T> findClassMergedRepeatableAnnotations(Class<?> declaringClass,
			Class<T> annotationType, Class<? extends Annotation> containerType) {
		// 判断注解的范围
		if (!isTypeScope(annotationType)) {
			return Set.of();
		}

		// 类上查找
		Set<T> annotations = findAllMergedAnnotations(declaringClass, annotationType);
		Set<T> repeatableClassAnnotations = findMergedRepeatableAnnotations(declaringClass,
			annotationType, containerType);
		annotations.addAll(repeatableClassAnnotations);

		return annotations;
	}

	/**
	 * 基于get语义，查找所有的重复注解
	 *
	 * @param method         注解所在的方法
	 * @param annotationType 重复注解类型
	 * @param containerType  重复注解容器
	 * @param <T>            重复注解的类型泛型
	 * @return 重复注解集合
	 */
	public static <T extends Annotation> Set<T> getAllMergedRepeatableAnnotations(Method method,
			Class<T> annotationType, Class<? extends Annotation> containerType) {
		// 方法上查找
		Set<T> annotations = getAllMergedAnnotations(method, annotationType);
		Set<T> repeatableAnnotations = getMergedRepeatableAnnotations(method, annotationType, containerType);
		annotations.addAll(repeatableAnnotations);

		// 判断注解的范围
		if (!isTypeScope(annotationType)) {
			return annotations;
		}

		// 类上查找
		Class<?> declaringClass = method.getDeclaringClass();
		Set<T> classAnnotations = getAllMergedAnnotations(declaringClass, annotationType);
		annotations.addAll(classAnnotations);
		Set<T> repeatableClassAnnotations = getMergedRepeatableAnnotations(declaringClass,
			annotationType, containerType);
		annotations.addAll(repeatableClassAnnotations);

		return annotations;
	}

	/**
	 * 基于find语义，融合方法 + 类上的注解信息
	 *
	 * @param method         方法对象
	 * @param annotationType 注解类型
	 * @param targetClass    当前方法所在的class对象
	 * @param <A>            注解类型
	 * @return 融合后的注解
	 * @see #synthesizeAnnotations(Collection, AnnotatedElement)
	 * @see org.springframework.core.annotation.AnnotatedElementUtils#findMergedAnnotation(AnnotatedElement, Class)}
	 */
	@Nullable
	public static <A extends Annotation> A findMergedMethodAnnotation(Method method,
			Class<A> annotationType, @Nullable Class<?> targetClass) {
		A annotation = findMergedAnnotation(method, annotationType);
		if (!isTypeScope(annotationType)) {
			return annotation;
		}

		List<A> mergedAnnotations = new ArrayList<>(2);
		if (annotation != null) {
			mergedAnnotations.add(annotation);
		}

		A classAnnotation = findMergedAnnotation(targetClass == null ?
			method.getDeclaringClass() : targetClass, annotationType);
		if (classAnnotation != null) {
			mergedAnnotations.add(classAnnotation);
		}

		if (mergedAnnotations.isEmpty()) {
			return null;
		}
		if (mergedAnnotations.size() == 1) {
			return mergedAnnotations.get(0);
		}
		return synthesizeAnnotations(mergedAnnotations, annotationType);
	}

	/**
	 * 基于find语义，融合方法 + 类上的注解信息
	 *
	 * @param method         方法对象
	 * @param annotationType 注解类型
	 * @param targetClass    当前方法所在的class对象
	 * @param <A>            注解类型
	 * @return 融合后的注解
	 * @see #synthesizeAnnotations(Collection, AnnotatedElement)
	 * @see org.springframework.core.annotation.AnnotatedElementUtils#getMergedAnnotation(AnnotatedElement, Class)}
	 */
	@Nullable
	public static <A extends Annotation> A getMergedMethodAnnotation(Method method,
			Class<A> annotationType, @Nullable Class<?> targetClass) {
		A annotation = getMergedAnnotation(method, annotationType);
		if (!isTypeScope(annotationType)) {
			return annotation;
		}

		List<A> mergedAnnotations = new ArrayList<>(2);
		if (annotation != null) {
			mergedAnnotations.add(annotation);
		}

		A classAnnotation = getMergedAnnotation(targetClass == null ?
			method.getDeclaringClass() : targetClass, annotationType);
		if (classAnnotation != null) {
			mergedAnnotations.add(classAnnotation);
		}

		return mergedAnnotations.isEmpty() ? null : synthesizeAnnotations(mergedAnnotations, annotationType);
	}

	/**
	 * 注解的对象转换为指定的注解类型
	 *
	 * @param annotation       注解对象
	 * @param annotationType   转换的注解类型
	 * @param annotatedElement {@link AnnotatedElement}
	 * @param <T>              注解类型
	 * @return 转换后的注解对象
	 */
	public static <T extends Annotation> T synthesizeAnnotation(Annotation annotation,
			Class<T> annotationType, @Nullable AnnotatedElement annotatedElement) {
		Map<String, Object> attributes = getAnnotationAttributes(annotatedElement, annotation);
		return org.springframework.core.annotation.AnnotationUtils.
			synthesizeAnnotation(attributes, annotationType, null);
	}

	/**
	 * 指定更新的注解字段名，合成新的注解，自动处理{@link AliasFor}语义
	 *
	 * @param attributes       更新注解字段
	 * @param annotation       原注解对象
	 * @param annotatedElement 注解所在的对象
	 * @param <T>              注解类型
	 * @return 合成后的注解
	 */
	@SuppressWarnings({ "unchecked", "ConstantConditions" })
	public static <T extends Annotation> T synthesizeAnnotation(Map<String, Object> attributes,
			T annotation, @Nullable AnnotatedElement annotatedElement) {
		if (CollectionUtils.isEmpty(attributes)) {
			return annotation;
		}

		attributes = new HashMap<>(attributes);
		Class<? extends Annotation> annotationType = annotation.annotationType();
		T synthesizeAnnotation = org.springframework.core.annotation.AnnotationUtils.
				synthesizeAnnotation(attributes, (Class<T>) annotationType, annotatedElement);
		return synthesizeAnnotations(List.of(synthesizeAnnotation, annotation), annotatedElement);
	}

	/**
	 * 多个注解的属性合成一个注解
	 * <p>
	 * 优先级从前往后越来越低，自动跳过默认值
	 *
	 * @param annotations      注解集合
	 * @param annotatedElement {@link AnnotatedElement}
	 * @param <T>              注解泛型
	 * @return 合成后的注解对象
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T synthesizeAnnotations(Collection<T> annotations,
			@Nullable AnnotatedElement annotatedElement) {
		if (CollectionUtils.isEmpty(annotations)) {
			return null;
		}

		// 获取优先级最低的注解信息
		List<T> candidateAnnotations = new ArrayList<>(annotations);
		T target = CollectionUtils.lastElement(candidateAnnotations);
		if (target == null || candidateAnnotations.size() == 1) {
			return target;
		}

		// 合并属性值
		Map<String, Object> attributes = getAnnotationAttributes(target, false);
		for (int i = candidateAnnotations.size() - 2; i >= 0; i--) {
			T annotation = candidateAnnotations.get(i);
			Map<String, Object> filterAttributes = filterDefaultValueAttributes(annotation, false);
			attributes.putAll(filterAttributes);
		}

		// 合成一个注解
		return org.springframework.core.annotation.AnnotationUtils.
			synthesizeAnnotation(attributes, (Class<T>) target.annotationType(), annotatedElement);
	}

	/**
	 * 查找注解的{@link Target}元注解确定注解作用范围
	 *
	 * @param annotationType 注解类型
	 * @return {@link ElementType}
	 */
	public static Set<ElementType> getAnnotationScope(Class<? extends Annotation> annotationType) {
		Target scope = annotationType.getAnnotation(Target.class);
		Set<ElementType> types = new HashSet<>();
		CollectionUtils.mergeArrayIntoCollection(scope.value(), types);
		return types;
	}

	/**
	 * 判断注解的作用范围是否{@link ElementType#TYPE}
	 *
	 * @param annotationType 注解类型
	 * @return yes or no
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean isTypeScope(Class<? extends Annotation> annotationType) {
		return getAnnotationScope(annotationType).contains(ElementType.TYPE);
	}

	/**
	 * 判断注解对象的某一个属性是否是默认值
	 * <p>
	 * 这里不能使用{@link org.springframework.core.annotation.AnnotationUtils#getDefaultValue(Class)}判断，
	 * 每次获取的值内存地址都不一样，因为JDK每次都有代理对象创建
	 *
	 * @param annotation    注解对象
	 * @param attributeName 属性名
	 * @return yes or no
	 */
	public static boolean isDefaultValue(Annotation annotation, String attributeName) {
		Map<String, Object> attributes = filterDefaultValueAttributes(annotation, false);
		return attributes.containsKey(attributeName);
	}

	/**
	 * 过滤注解中的默认值返回Map
	 *
	 * @param annotation 注解对象
	 * @param classValuesAsString 是否把{@link Class}写成字符串
	 * @return 注解属性Map
	 */
	public static Map<String, Object> filterDefaultValueAttributes(Annotation annotation, boolean classValuesAsString) {
		MergedAnnotation.Adapt[] adaptations = MergedAnnotation.Adapt.values(classValuesAsString, false);
		return MergedAnnotation.from(null, annotation)
				.withNonMergedAttributes()
				.filterDefaultValues()
				.asMap(mergedAnnotation ->
						new AnnotationAttributes(mergedAnnotation.getType()), adaptations);
	}

}

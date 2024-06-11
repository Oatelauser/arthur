package com.arthur.boot.process;

import org.springframework.core.type.filter.TypeFilter;

import java.util.Set;

/**
 * classpath类扫描器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-23
 * @see ClassPathScanningProcessor
 * @since 1.0
 */
public interface ClassPathScanner {

	/**
	 * 获取扫描描述信息
	 */
	Descriptor getClassPathDescriptor();

	/**
	 * 处理候选的类
	 *
	 * @param candidateClasses 候选的类
	 */
	void handleCandidateClass(Set<Class<?>> candidateClasses);

	class Descriptor {

		private String[] scanPackages;
		private TypeFilter[] includeFilters;
		private TypeFilter[] excludeFilters;

		public Descriptor(String[] scanPackages, TypeFilter[] includeFilters) {
			this(scanPackages, includeFilters, null);
		}

		public Descriptor(String[] scanPackages, TypeFilter[] includeFilters, TypeFilter[] excludeFilters) {
			this.scanPackages = scanPackages;
			this.includeFilters = includeFilters;
			this.excludeFilters = excludeFilters;
		}

		public String[] getScanPackages() {
			return scanPackages;
		}

		public void setScanPackages(String[] scanPackages) {
			this.scanPackages = scanPackages;
		}

		public TypeFilter[] getIncludeFilters() {
			return includeFilters;
		}

		public void setIncludeFilters(TypeFilter[] includeFilters) {
			this.includeFilters = includeFilters;
		}

		public TypeFilter[] getExcludeFilters() {
			return excludeFilters;
		}

		public void setExcludeFilters(TypeFilter[] excludeFilters) {
			this.excludeFilters = excludeFilters;
		}

	}

}

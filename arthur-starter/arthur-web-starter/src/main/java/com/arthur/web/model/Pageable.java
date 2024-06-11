package com.arthur.web.model;

import java.util.List;

/**
 * 分页结果对象
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-01
 * @since 1.0
 */
public class Pageable<T> {

	/**
	 * 总条数
	 */
	private int total;

	/**
	 * 每页显示数
	 */
	private int pageSize;

	/**
	 * 当前页
	 */
	private int currentPage;

	/**
	 * 当前页数据
	 */
	private List<T> records;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

}

package com.arthur.web.model;

import com.arthur.web.constant.SortType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

/**
 * 分页查询通用条件
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
@SuppressWarnings("unused")
@Schema(name = "分页查询请求")
public class PageSearchDto {

	/**
	 * 页数
	 */
	@Schema(description = "页数", defaultValue = "1", requiredMode = NOT_REQUIRED)
	private int pageNum = 1;

	/**
	 * 每页条数
	 */
	@Schema(description = "每页条数", defaultValue = "10", requiredMode = NOT_REQUIRED)
	private int pageSize = 10;

	/**
	 * 排序类型
	 */
	@Schema(description = "排序类型", defaultValue = "DESC", requiredMode = NOT_REQUIRED)
	private SortType sortType = SortType.DESC;

	/**
	 * 排序字段列表
	 */
	@Schema(description = "排序字段列表", requiredMode = NOT_REQUIRED)
	private List<String> sortColumns;

	/**
	 * 分组字段列表
	 */
	@Schema(description = "分组字段列表", requiredMode = NOT_REQUIRED)
	private List<String> groupColumns;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public List<String> getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(List<String> sortColumns) {
		this.sortColumns = sortColumns;
	}

	public List<String> getGroupColumns() {
		return groupColumns;
	}

	public void setGroupColumns(List<String> groupColumns) {
		this.groupColumns = groupColumns;
	}

}

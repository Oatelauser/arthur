package com.arthur.boot.mybatis.model;

import com.arthur.web.model.Pageable;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 分页对象
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public class PagedGridResult {

	public static <T>  Pageable<T> getPage(IPage<T> page) {
		Pageable<T> objectPage = new Pageable<>();
		objectPage.setTotal((int) page.getTotal());
		objectPage.setPageSize((int) page.getPages());
		objectPage.setCurrentPage((int) page.getCurrent());
		objectPage.setRecords(page.getRecords());
		return objectPage;
	}

}

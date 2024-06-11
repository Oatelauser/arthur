package com.arthur.boot.mybatis.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * 通用Mapper抽象接口
 * <p>
 * 1.新增Mysql批量插入方法实现
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @see com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn
 * @since 1.0
 */
public interface GenericMapper<T> extends BaseMapper<T> {

	/**
	 * 批量插入，仅适用于mysql
	 *
	 * @param entityList 实体列表
	 * @return 影响行数
	 */
	Integer insertBatchSomeColumn(Collection<T> entityList);

}

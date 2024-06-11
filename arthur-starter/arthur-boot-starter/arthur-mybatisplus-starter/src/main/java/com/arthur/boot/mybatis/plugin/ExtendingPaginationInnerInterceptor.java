package com.arthur.boot.mybatis.plugin;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 拓展分页拦截器
 * <p>
 * 防止因为size<0，而走全部扫描
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ExtendingPaginationInnerInterceptor extends PaginationInnerInterceptor {

    public ExtendingPaginationInnerInterceptor() {
    }

    public ExtendingPaginationInnerInterceptor(DbType dbType) {
        super(dbType);
    }

    public ExtendingPaginationInnerInterceptor(IDialect dialect) {
        super(dialect);
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        if (page != null && page.getSize() < 0) {
            page.setSize(0);
        }

        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

}

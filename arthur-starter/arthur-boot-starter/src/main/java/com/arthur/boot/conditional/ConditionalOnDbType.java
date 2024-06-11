package com.arthur.boot.conditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 条件判断数据库类型
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20 OnEnabledDatabase
 * @since 1.0
 */
@Documented
@SuppressWarnings("unused")
@Conditional(OnEnabledDbType.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ConditionalOnDbType {

    /**
     * 数据库类型
     */
    DbType value();

    @SuppressWarnings("SpellCheckingInspection")
    enum DbType {
        none,
        derby,
        mysql,
        mariadb,
        tidb,
        oracle,
        ali_oracle,
        oceanbase_oracle,
        oceanbase,
        sqlserver,
        sybase,
        jtds,
        mock,
        postgresql,
        edb,
        hsql,
        odps,
        db2,
        sqlite,
        ingres,
        h2,
        cloudscape,
        informix,
        timesten,
        as400,
        sapdb,
        JSQLConnect,
        JTurbo,
        firebirdsql,
        interbase,
        pointbase,
        edbc,
        mimer,
        dm,
        xugu,
        gbase,
        kingbase,
        log4jdbc,
        hive,
        phoenix,
        kylin,
        elastic_search,
        clickhouse,
        presto,
        trino,
        kdb,
        polardb,
        highgo,
        greenplum,
        gaussdb,
        ;

    }

}

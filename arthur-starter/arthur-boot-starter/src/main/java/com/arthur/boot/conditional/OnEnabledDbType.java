package com.arthur.boot.conditional;

import com.arthur.boot.conditional.ConditionalOnDbType.DbType;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 条件匹配数据库类型
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @since 1.0
 */
public class OnEnabledDbType extends SpringBootCondition implements ConfigurationCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotations annotations = metadata.getAnnotations();
        if (!annotations.isPresent(ConditionalOnDbType.class)) {
            return ConditionOutcome.match();
        }

        Environment environment = context.getEnvironment();
        String url = environment.getProperty("spring.datasource.url");
        if (!StringUtils.hasText(url)) {
            url = environment.getProperty("spring.data-source.url");
        }
        DbType dbType = resolveDbTypeIfPossible(url);
        if (dbType == null) {
            return ConditionOutcome.noMatch("无法解析数据库类型: " + url);
        }

        MergedAnnotation<ConditionalOnDbType> annotation = annotations.get(ConditionalOnDbType.class);
        DbType targetDbType = annotation.getEnum("value", DbType.class);
        return targetDbType.equals(dbType) ? ConditionOutcome.match() :
                ConditionOutcome.noMatch("数据库类型不匹配：[" + targetDbType + "] - [" + dbType + "]");
    }

    @NonNull
    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    /**
     * 根据JDBC Url判断JDBC的类型
     * copy by {@code com.alibaba.druid.util.JdbcUtils#getDbTypeRaw(String, String)}
     *
     * @param rawUrl JDBC连接地址
     * @return 数据库类型
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static DbType resolveDbTypeIfPossible(String rawUrl) {
        if (rawUrl == null) {
            return null;
        }

        if (rawUrl.startsWith("jdbc:derby:") || rawUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return DbType.derby;
        } else if (rawUrl.startsWith("jdbc:mysql:") || rawUrl.startsWith("jdbc:cobar:")
                || rawUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return DbType.mysql;
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return DbType.mariadb;
        } else if (rawUrl.startsWith("jdbc:tidb:")) {
            return DbType.tidb;
        } else if (rawUrl.startsWith("jdbc:oracle:") || rawUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return DbType.oracle;
        } else if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
            return DbType.ali_oracle;
        } else if (rawUrl.startsWith("jdbc:oceanbase:oracle:")) {
            return DbType.oceanbase_oracle;
        } else if (rawUrl.startsWith("jdbc:oceanbase:")) {
            return DbType.oceanbase;
        } else if (rawUrl.startsWith("jdbc:microsoft:") || rawUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return DbType.sqlserver;
        } else if (rawUrl.startsWith("jdbc:sqlserver:") || rawUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return DbType.sqlserver;
        } else if (rawUrl.startsWith("jdbc:sybase:Tds:") || rawUrl.startsWith("jdbc:log4jdbc:sybase:")) {
            return DbType.sybase;
        } else if (rawUrl.startsWith("jdbc:jtds:") || rawUrl.startsWith("jdbc:log4jdbc:jtds:")) {
            return DbType.jtds;
        } else if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
            return DbType.mock;
        } else if (rawUrl.startsWith("jdbc:postgresql:") || rawUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return DbType.postgresql;
        } else if (rawUrl.startsWith("jdbc:edb:")) {
            return DbType.edb;
        } else if (rawUrl.startsWith("jdbc:hsqldb:") || rawUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return DbType.hsql;
        } else if (rawUrl.startsWith("jdbc:odps:")) {
            return DbType.odps;
        } else if (rawUrl.startsWith("jdbc:db2:")) {
            return DbType.db2;
        } else if (rawUrl.startsWith("jdbc:sqlite:")) {
            return DbType.sqlite;
        } else if (rawUrl.startsWith("jdbc:ingres:")) {
            return DbType.ingres;
        } else if (rawUrl.startsWith("jdbc:h2:") || rawUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return DbType.h2;
        } else if (rawUrl.startsWith("jdbc:mckoi:")) {
            return DbType.mock;
        } else if (rawUrl.startsWith("jdbc:cloudscape:")) {
            return DbType.cloudscape;
        } else if (rawUrl.startsWith("jdbc:informix-sqli:") || rawUrl.startsWith("jdbc:log4jdbc:informix-sqli:")) {
            return DbType.informix;
        } else if (rawUrl.startsWith("jdbc:timesten:")) {
            return DbType.timesten;
        } else if (rawUrl.startsWith("jdbc:as400:")) {
            return DbType.as400;
        } else if (rawUrl.startsWith("jdbc:sapdb:")) {
            return DbType.sapdb;
        } else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
            return DbType.JSQLConnect;
        } else if (rawUrl.startsWith("jdbc:JTurbo:")) {
            return DbType.JTurbo;
        } else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
            return DbType.firebirdsql;
        } else if (rawUrl.startsWith("jdbc:interbase:")) {
            return DbType.interbase;
        } else if (rawUrl.startsWith("jdbc:pointbase:")) {
            return DbType.pointbase;
        } else if (rawUrl.startsWith("jdbc:edbc:")) {
            return DbType.edbc;
        } else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
            return DbType.mimer;
        } else if (rawUrl.startsWith("jdbc:dm:")) {
            return DbType.dm;
        } else if (rawUrl.startsWith("jdbc:kingbase:") || rawUrl.startsWith("jdbc:kingbase8:")) {
            return DbType.kingbase;
        } else if (rawUrl.startsWith("jdbc:gbase:")) {
            return DbType.gbase;
        } else if (rawUrl.startsWith("jdbc:xugu:")) {
            return DbType.xugu;
        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return DbType.log4jdbc;
        } else if (rawUrl.startsWith("jdbc:hive:")) {
            return DbType.hive;
        } else if (rawUrl.startsWith("jdbc:hive2:")) {
            return DbType.hive;
        } else if (rawUrl.startsWith("jdbc:phoenix:")) {
            return DbType.phoenix;
        } else if (rawUrl.startsWith("jdbc:kylin:")) {
            return DbType.kylin;
        } else if (rawUrl.startsWith("jdbc:elastic:")) {
            return DbType.elastic_search;
        } else if (rawUrl.startsWith("jdbc:clickhouse:")) {
            return DbType.clickhouse;
        } else if (rawUrl.startsWith("jdbc:presto:")) {
            return DbType.presto;
        } else if (rawUrl.startsWith("jdbc:trino:")) {
            return DbType.trino;
        } else if (rawUrl.startsWith("jdbc:inspur:")) {
            return DbType.kdb;
        } else if (rawUrl.startsWith("jdbc:polardb")) {
            return DbType.polardb;
        } else if (rawUrl.startsWith("jdbc:highgo:")) {
            return DbType.highgo;
        } else if (rawUrl.startsWith("jdbc:pivotal:greenplum:") || rawUrl.startsWith("jdbc:datadirect:greenplum:")) {
            return DbType.greenplum;
        } else if (rawUrl.startsWith("jdbc:opengauss:") || rawUrl.startsWith("jdbc:gaussdb:") || rawUrl.startsWith("jdbc:dws:iam:")) {
            return DbType.gaussdb;
        } else {
            return null;
        }
    }

}

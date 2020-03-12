package com.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
public class DynamicDataSourceInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement)args[0];
        String dbType = DynamicDataSourceHolder.DB_MASTER;
        if(!actualTransactionActive){
            if(mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
                //自增主键的情况使用master
                if(mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    dbType = DynamicDataSourceHolder.DB_MASTER;
                }else{
                    BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(args[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if (sql.matches(REGEX)) {
                        dbType = DynamicDataSourceHolder.DB_MASTER;
                    }else {
                        dbType = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        }else {
            dbType = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}] use [{}] Strategy, SqlCommanType [{}]..", mappedStatement.getId(), dbType,
                mappedStatement.getSqlCommandType().name());
        DynamicDataSourceHolder.setDbType(dbType);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        System.out.println(o.getClass().getName());
        if (o instanceof Executor){
            //如果线程是执行数据库操作的则把intercept的内容添加进线程中
            return Plugin.wrap(o,this);
        }
        else
            return o;

    }

    @Override
    public void setProperties(Properties properties) { }
}

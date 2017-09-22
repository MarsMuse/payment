package com.zhph.base.pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.zhph.base.pagination.dialect.Dialect;
import com.zhph.base.pagination.dialect.OracleDialect;
import com.zhph.base.utils.ReflectHandler;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName:  PaginationIntercepter   
 * Description:(获取到分页数据拦截器)   
 * author: FireMonkey
 * date:   2017年6月5日 下午4:54:31   
 *     
 * Copyright: 2017 
 *
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare",
args = {Connection.class,Integer.class})})
public class PaginationIntercepter implements Interceptor {

    private   Logger log  =  LoggerFactory.getLogger(PaginationIntercepter.class);
    private Dialect dialect;
    //需要分页的SqlId
    private String pageSqlId = ".*";
    
    
    /**
     * 
     * <p>Title: intercept</p>   
     * <p>Description: 拦截需要分页的方法</p>   
     * param invocation
     * return
     * throws Throwable   
     * see Interceptor#intercept(Invocation)
     */
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        
        StatementHandler  statementHandler = (StatementHandler) invocation.getTarget();
        BaseStatementHandler  delegate = (BaseStatementHandler) ReflectHandler.getValueByFieldName(statementHandler, "delegate");
        RowBounds  rowBounds = (RowBounds) ReflectHandler.getValueByFieldName(delegate, "rowBounds");
        MappedStatement  mappedStatement = (MappedStatement) ReflectHandler.getValueByFieldName(delegate, "mappedStatement");
        //判断方法是否符合要求
        if(!mappedStatement.getId().matches(this.pageSqlId)){
            return invocation.proceed();
        }
        log.debug("拦截到需要分页的Sql,ID为：{}" , mappedStatement.getId());
        //判断是否用rowBounds
        if(rowBounds == null || rowBounds  == RowBounds.DEFAULT){
            return invocation.proceed();
        }
        //判断是否使用自定义的插件
        if(!(rowBounds instanceof  BindedRowBounds)){
            return invocation.proceed();
        }
        BindedRowBounds  bindedrowBounds  =  (BindedRowBounds) rowBounds;
        if(this.dialect == null){
            dialect = new OracleDialect();
        }
        BoundSql  boundSql  =  statementHandler.getBoundSql();
        String  originalSql  = boundSql.getSql();
        PaginationIntercepter.dealSQL(originalSql);
        
        log.debug("分页Sql的原Sql为：{}", originalSql);
        Connection connection  =   (Connection) invocation.getArgs()[0];
        String countSql = new StringBuilder("select count(0) from (")
                .append(originalSql).append(") t").toString();
        log.debug("汇总Sql为：{}" , countSql);
        
        int count = 0;
        PreparedStatement  preparedStatement  = null;
        ResultSet rs = null;
        try{
            preparedStatement = (PreparedStatement) connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalParameters = (Map<String, Object>)ReflectHandler.getValueByFieldName(boundSql, "additionalParameters");
            ReflectHandler.setValueByFieldName(countBS, "additionalParameters", additionalParameters);
            MetaObject metaParameters = (MetaObject)ReflectHandler.getValueByFieldName(boundSql, "metaParameters");
            ReflectHandler.setValueByFieldName(countBS, "metaParameters", metaParameters);
            this.setParameters(preparedStatement, mappedStatement, countBS);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("总数据量：{}，SQL：{}", count, boundSql.getSql());
        }catch(RuntimeException e){
            throw e;
        } catch(Exception e){
            log.error("在获取到分页数据时出现异常");
            e.printStackTrace();
        } finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("在获取到分页数据时关闭结果集对象异常");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error("在获取到分页数据时关闭statement对象异常");
                }
            }
        }
        
        Page page = bindedrowBounds.getPage();
        page.setTotalRecords(count);
        page.refresh();
        ReflectHandler.setValueByFieldName(boundSql, "sql",
                this.dialect.getPaginationSQL(originalSql, page.getOffset(),
                        page.getLimit()));
        log.debug("分页SQL：{}", boundSql.getSql());
        ReflectHandler.setValueByFieldName(bindedrowBounds, "offset",
                RowBounds.NO_ROW_OFFSET);
        ReflectHandler.setValueByFieldName(bindedrowBounds, "limit",
                RowBounds.NO_ROW_LIMIT);
        return invocation.proceed();
    }
 /**
  * 
  * {参考DefaultParameterHandler的方法}
  * 
  * param
  * param
  * param
  * throws
  * author:{zou yao}
  */
    @SuppressWarnings("unchecked")
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
            BoundSql boundSql) throws SQLException
    {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (boundSql.getParameterObject() == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    typeHandler.setParameter(ps, i + 1, value, jdbcType);
                }
            }
        }
    }
    /**
     * 
     * Title: dealSQL   
     * Description: (对传入的Sql进行预处理)   
     * param: param originalSql      
     * return: void      
     * throws
     */
    public static final void dealSQL(String originalSql){
        originalSql = originalSql.trim();
        int  index  =  originalSql.lastIndexOf(";");
        if(index == (originalSql.length()-1)){
           originalSql = originalSql.substring(0 , originalSql.length()-1);
        }
    }
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties arg0) {}
    public Dialect getDialect() {
        return dialect;
    }
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
    public String getPageSqlId() {
        return pageSqlId;
    }
    public void setPageSqlId(String pageSqlId) {
        this.pageSqlId = pageSqlId;
    }
}

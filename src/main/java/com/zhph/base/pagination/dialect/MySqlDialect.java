package com.zhph.base.pagination.dialect;

public class MySqlDialect implements Dialect {

    @Override
    public String getPaginationSQL(String sql, int offset, int limit) {
        return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(limit).toString();
    }
}

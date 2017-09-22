package com.zhph.base.pagination.dialect;

public class OracleDialect implements Dialect{

    private  static final String LOCK_STATEMENT  =  "for update";
    private static final int LOCK_STATEMENT_LEN = LOCK_STATEMENT.length();
    private static final int PAGE_SQL_EXT_LEN = 100;
    @Override
    public String getPaginationSQL(String sql, int offset, int limit) {
        
        boolean isForUpdate = false;
        String pSql = sql.trim();
        if(pSql.toLowerCase().endsWith(LOCK_STATEMENT)){
            pSql = pSql.substring(0, pSql.length()-LOCK_STATEMENT_LEN);
            isForUpdate = true;
        }
        StringBuilder pageSql = new StringBuilder(pSql.length() + PAGE_SQL_EXT_LEN);
        pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ")
            .append(pSql)
            .append(" ) row_ ) where rownum_ > ")
            .append(offset)
            .append(" and rownum_ <=")
            .append(offset + limit);
        if(isForUpdate) {
            pageSql.append(LOCK_STATEMENT);
        }
        return pageSql.toString();
    }
}

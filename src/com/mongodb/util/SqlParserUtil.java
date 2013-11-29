package com.mongodb.util;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

/** */
/**
 * 单句Sql解析器制造工厂
 * 
 * @since 2013-6-10
 * @version 1.00
 */
public class SqlParserUtil {

	/**
	 * 方法的主要入口
	 * 
	 * @param sql
	 *            :要解析的sql语句
	 * @return 返回解析结果
	 */
	public static String getParsedSql(String sql) {
		sql = sql.trim();
		sql = sql.toLowerCase();
		sql = sql.replaceAll("\\s{1,}", " ");
		sql = "" + sql + " ENDOFSQL";
		// System.out.println(sql);
		return SingleSqlParserFactory.generateParser(sql).getParsedSql();
	}
	
	public static boolean isSelectQuery(String sql){
		SingleSqlParserFactory.
	}

	/**
	 * SQL语句解析的接口
	 * 
	 * @param sql
	 *            :要解析的sql语句
	 * @return 返回解析结果
	 */
	public static List<SqlSegment> getParsedSqlList(String sql) {
		sql = sql.trim();
		sql = sql.toLowerCase();
		sql = sql.replaceAll("\\s{1,}", " ");
		sql = "" + sql + " ENDOFSQL";
		// System.out.println(sql);
		return SingleSqlParserFactory.generateParser(sql).RetrunSqlSegments();
	}
}

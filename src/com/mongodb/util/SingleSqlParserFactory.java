package com.mongodb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleSqlParserFactory {
	public static final String INSERTREG = "(insert into)(.+)(select)(.+)(from)(.+)";
	public static final String SELECTREG = "(select)(.+)(from)(.+)";
	public static final String DELETEREG = "(delete from)(.+)";
	public static final String UPDATEREG = "(update)(.+)(set)(.+)";
	public static final String INSERTINTOREG = "(insert into)(.+)(values)(.+)";
	
	public static BaseSingleSqlParser generateParser(String sql) {
		if (contains(sql, INSERTREG)) {
			return new InsertSelectSqlParser(sql);
		} else if (contains(sql, SELECTREG)) {
			return new SelectSqlParser(sql);
		} else if (contains(sql, DELETEREG)) {
			return new DeleteSqlParser(sql);
		} else if (contains(sql, UPDATEREG)) {
			return new UpdateSqlParser(sql);
		} else if (contains(sql, INSERTINTOREG)) {
			return new InsertSqlParser(sql);
		}
		else
			return new InsertSqlParser(sql);
	}
	
//	public static 

	/**
	 *  看word是否在lineText中存在，支持正则表达式 　
	 * @param sql:要解析的sql语句 　
	 * @param regExp:正则表达式 　
	 * @return 　
	 */
	private static boolean contains(String sql, String regExp) {
		Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		return matcher.find();
	}

}

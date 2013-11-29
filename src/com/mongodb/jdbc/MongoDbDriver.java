package com.mongodb.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import com.mongodb.MongoClientURI;
import com.mongodb.util.StringUtils;


public class MongoDbDriver implements Driver {
	
	private DriverPropertyInfoHelper propertyInfoHelper = new DriverPropertyInfoHelper();
	
	static{
		try{
			DriverManager.registerDriver(new MongoDbDriver());
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	private static final String URL_SPEC_JDBC = "jdbc:";
	
	private static final String URL_SPEC = "mongodb://";

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		MongoClientURI mcu = null;
		if ((mcu = parseURL(url, info)) == null) {
			return null;
		}
		
		MongoDbConnection result = null;
		
		try{
			result = new MongoDbConnection(mcu, url);
		}catch (Exception e){
			throw new SQLException("Unexpected exception: " + e.getMessage(), e);
		}
		
		return result;
	}

	private MongoClientURI parseURL(String url, Properties defaults) {
		if (url == null) {
			return null;
		}
		
		if (!StringUtils.startsWithIgnoreCase(url, URL_SPEC_JDBC+URL_SPEC)) {
			return null;
		}
		
		//删掉开头的 jdbc:
		url = url.replace(URL_SPEC_JDBC, "");
		
		try {
			//FIXME 判断defaults中的参数,写入URL中?
			return new MongoClientURI(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		if (!StringUtils.startsWithIgnoreCase(url, URL_SPEC_JDBC+URL_SPEC)) {
			return true;
		}
		return false;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {

		return propertyInfoHelper.getPropertyInfo();
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

}

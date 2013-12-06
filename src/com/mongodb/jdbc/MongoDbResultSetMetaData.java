package com.mongodb.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

public class MongoDbResultSetMetaData implements ResultSetMetaData {
	
	private String[] keySet ;
	
	public MongoDbResultSetMetaData(Set<String> keySet) {
		super();
		this.keySet = new String[keySet.size()];
		this.keySet = keySet.toArray(this.keySet);
	}

	public MongoDbResultSetMetaData(String[] select) {
		super();
		this.keySet = select;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int getColumnCount() throws SQLException {
		return keySet.length;
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		return true;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		return true;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int isNullable(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		return keySet[column-1];
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		return keySet[column-1];
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public String getTableName(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

}

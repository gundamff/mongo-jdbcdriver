package com.mongodb.jdbc;

import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZDelete;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZFromItem;
import org.gibello.zql.ZInsert;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZSelectItem;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDbStatement implements Statement {

	private final DB db;

	private final MongoDbConnection con;

	private ResultSet lastResultSet ;
	
	private DBCollection dbCollection;

	protected String dbName = null;
	
	private static String R = "\\('([^']+)' todate '([^']+)'\\)";

	public MongoDbStatement(final MongoDbConnection connection)
			throws SQLException {
		if ((connection == null) || connection.isClosed()) {
			throw new SQLException("mongoDb_not_open");
		}

		this.con = connection;
		this.db = connection.getDb();
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
	public ResultSet executeQuery(final String sql) throws SQLException {
		if(execute(sql)){
			return this.lastResultSet;
		}
		return null;
	}

	private ResultSet handleQuery(ZQuery zql) throws Exception {
		
		System.out.println("handleQuery.zql="+zql);
		String collectionName = null;
		
		if(zql.getFrom().size()!=1){
			throw new SQLException("Only supports single-table check");
		}else{
			collectionName = zql.getFrom().get(0).toString();
		}
		
		System.out.println("handleQuery.collectionName="+collectionName);
		
		dbCollection = db.getCollectionFromString(collectionName);
		return new MongoDbResultSet(dbCollection.find( handleWhere(zql.getWhere()),handleSelectQuery(zql)));
	}

	private DBObject handleSelectQuery(ZQuery st) throws Exception {
		BasicDBObject query = new BasicDBObject();
		
		Iterator i = st.getSelect().iterator();
		while(i.hasNext()){
			query.append(i.next().toString(), "ture");
		}
		query.append("_id", "true");
		return query;
	}
	
	private static DBObject handleWhere(ZExp e)
			throws Exception {
		BasicDBObject queryCondition = new BasicDBObject();
		
		if (!(e instanceof ZExpression))
			return null;
		ZExpression w = (ZExpression) e;

		Vector operands = w.getOperands();
		if (operands == null)
			return null;

		if (w.getOperands().size() == 2) {
			if (w.getOperator().equals("=")) {
				queryCondition.put(w.getOperands().get(0).toString(), transform(w.getOperands()
						.get(1).toString()));
			} else if (w.getOperator().equals("<=")) {
				queryCondition.put(w.getOperands().get(0).toString(),
						new BasicDBObject("$lte", transform(w.getOperands()
								.get(1).toString())));
			} else if (w.getOperator().equals(">=")) {
				queryCondition.put(w.getOperands().get(0).toString(),
						new BasicDBObject("$gte", transform(w.getOperands()
								.get(1).toString())));
			}//TODO and or
		}
		return queryCondition;

	}
	
	private static Object transform(String source) throws ParseException, java.text.ParseException {
		//FIXME 解析SQL会有'
		if (source.endsWith("'") && source.startsWith("'")) {
			return source.replaceAll("^\\'(.*)\\'", "$1");
		}else if(Pattern.compile(R).matcher(source).matches()){
			Pattern compile = Pattern.compile(R);
			Matcher matcher = compile.matcher(source);
			if(matcher.find()){
					System.out.println(matcher.group(2));
					System.out.println(matcher.group(1));
					Date d = new SimpleDateFormat(matcher.group(2)).parse(matcher.group(1));
					System.out.println(DateFormat.getDateTimeInstance().format(d));
					return	d;
			}
			return Pattern.compile(R).matcher(source);
		}else {
			return new Double(source);
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void close() throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public void cancel() throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean execute(String sql) {
		ZqlParser p = new ZqlParser(new StringBufferInputStream(sql));
		ZStatement st = null;
		try {
			while((st = p.readStatement()) != null) {

			    System.out.println(st.toString()); // Display the statement

			    if(st instanceof ZQuery) { // An SQL query: query the DB
			    	this.lastResultSet = handleQuery((ZQuery)st);
			    } else if(st instanceof ZInsert) { // An SQL insert
//	          handleInsert((ZInsert)st);
			    } else if(st instanceof ZUpdate) {
//	          handleUpdate((ZUpdate)st);
			    } else if(st instanceof ZDelete) {
//	          handleDelete((ZDelete)st);
			    }
			  }
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.lastResultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO 自动生成的方法存根
		return false;
	}

}

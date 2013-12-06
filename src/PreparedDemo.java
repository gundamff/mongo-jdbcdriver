import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.gibello.zql.*;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.jdbc.MongoDbResultSet;

public class PreparedDemo {
	
//		http://blog.csdn.net/mydeman/article/details/6652387
//		http://www.cnblogs.com/hoojo/archive/2011/06/02/2068665.html
	public static void main(String args[]) {
		try {

			String sql = "select name from test where birthday >= todate('1987092308','yyyyMMddhh') ;EXIT;";
			DBAddress addr = new DBAddress("172.16.23.224", 30000, "test");
			DB mg = Mongo.connect(addr);
			DBCollection users = mg.getCollection("test");

			ZqlParser p = new ZqlParser(new StringBufferInputStream(sql));
			ZStatement st;
			while ((st = p.readStatement()) != null) {

				System.out.println(st.toString()); // Display the statement
				DBObject values = new BasicDBObject();
				
				
				DBCursor cur = users.find(handleWhere((ZQuery) st, values));
				
				 while (cur.hasNext()) { System.out.println(cur.next()); }
				 
//				System.out.println(cur.count());
				ResultSet rs = new MongoDbResultSet(cur);
				System.out.println(rs.getRow());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static DBObject handleQuery(ZQuery q, DBObject queryCondition)
			throws Exception {
		Vector sel = q.getSelect(); // SELECT part of the query

		ZExpression w = (ZExpression) q.getWhere();
		if (w != null) {
			queryCondition = handleWhere(w, queryCondition);
		}
		return queryCondition;
	}

	private static DBObject handleWhere(ZExp e, DBObject queryCondition)
			throws Exception {
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

	private static String R = "\\('([^']+)' todate '([^']+)'\\)";
	
	private static Object transform(String source) throws ParseException {
		if (source.endsWith("'") && source.startsWith("'")) {
			return source;
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

}

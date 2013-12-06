
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTest3 {
	
	public static void main(String[] args) throws Exception {
		Class.forName("com.mongodb.jdbc.MongoDbDriver");
		
		Connection c = DriverManager
				.getConnection("jdbc:mongodb://172.16.23.224:30000/test");
		Statement stmt = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		String query = "select Name,name,CtfId,Address from users where  CtfId = '370101190001011111';EXIT;";
		
//		ResultSet rs = statament.executeQuery("select '_id', name,idcode,address from test where birthday <= todate('1987092308','yyyyMMddhh') ;EXIT;");
		ResultSetMetaData rsmd;
		ResultSet resultSet ;
		List<String> colNames;

		/*rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			System.out.println(rsmd.getColumnName(i));
		}*/
		resultSet = stmt.executeQuery(query);
		while (resultSet.next()) {
			rsmd = resultSet.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				System.out.println(rsmd.getColumnName(i+1) + ":"
						+ resultSet.getObject(rsmd.getColumnName(i+1)));
			}
		}
		
		System.out.println(resultSet.getRow());
		
		
		
	}
	
	private static List<String> readFieldNames(ResultSetMetaData metaData)
	          throws SQLException {
	    List<String> colNames = new ArrayList<String>();
	    int count = metaData.getColumnCount();
	    for (int i = 0; i < count; i++) {
	      colNames.add(metaData.getColumnLabel(i + 1));
	      System.out.println(metaData.getColumnLabel(i + 1));
	    }
	    return colNames;
	  }

}

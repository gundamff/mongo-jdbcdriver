mongo-jdbcdriver
================
    Class.forName("com.mongodb.jdbc.MongoDbDriver");
		
		Connection c = DriverManager
				.getConnection("jdbc:mongodb://host:port/test");
		Statement stmt = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		String query = "select Name,name,CtfId,Address from users where  CtfId = '370101190001011111';EXIT;";
		
		ResultSetMetaData rsmd;
		ResultSet resultSet ;
		List<String> colNames;
		
		resultSet = stmt.executeQuery(query);
		while (resultSet.next()) {
			rsmd = resultSet.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				System.out.println(rsmd.getColumnName(i+1) + ":"
						+ resultSet.getObject(rsmd.getColumnName(i+1)));
			}
		}
		
		System.out.println(resultSet.getRow());

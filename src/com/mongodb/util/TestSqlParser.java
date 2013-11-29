package com.mongodb.util;

import java.util.Iterator;
import java.util.List;

public class TestSqlParser {
	public static void main(String[] args) {
        // TODO Auto-generated method stub
       //String test="select  a from  b " +
           //    "\n"+"where      a=b";
       //test=test.replaceAll("\\s{1,}", " ");
       //System.out.println(test);
       //程序的入口
        String testSql="select c1,c2,c3     from    t1,t2 where condi3=3 "+"\n"+"    or condi4=5 order by o1 desc,o2";
        SqlParserUtil test=new SqlParserUtil();
//        String result=test.getParsedSql(testSql);
//        System.out.println(result);
        
        System.out.println("##########################################");
        List<SqlSegment> l =test.getParsedSqlList(testSql);
        Iterator<SqlSegment> i = l.iterator();
        while(i.hasNext()){
        	System.out.println("body:"+i.next().getBody());
        }
        
       //List<SqlSegment> result=test.getParsedSqlList(testSql);//保存解析结果
    }
}

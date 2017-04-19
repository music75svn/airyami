<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*,java.util.*,javax.servlet.http.*"%>
<%
	/*++++++ DB 접속 환경 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url    = "jdbc:oracle:thin:@150.3.50.52:1521:orcl"; //서부개발
	//String url    = "jdbc:oracle:thin:@localhost:1521:ORCL";  
	String user   = "kgsbsc";
	String pass   = "kgsbsc";

	/*++++++ 로컬 파일생성경로 설정 (ex : D:/00.workspace/BSC_WP) ++++++++++++++++++++++*/
	String real_path = "d:/workSpace/KGSBSC";
	
%>

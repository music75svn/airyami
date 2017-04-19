<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*,java.util.*,javax.servlet.http.*"%>
<%
/*************************************************************************
* CLASS 명  	: db_connect.jsp
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 5월 29일 
* 기    능  	: db_connect
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 5월 29일  		  최 초 작 업 
**************************************************************************/
%>

<%!
	//문자열 공백채움
	public static String getStrFillBlank(String str) {
		return getStrFillBlank(str, 22);
	}

	//문자열 공백채움
	public static String getStrFillBlank(String str, int max) {
		String tmpStr = "";
		
		if(null == str || "".equals(str)) return str;
		int strLen = str.length();
		if(strLen >= max) return str;
		
		int fillLength = 0;
		fillLength = max - strLen;
		
		for(int i = 0; i < fillLength; i++) {
			str += " ";
		}
		return str;
	}

	public String nullChk(String str) {
		if(str == null) return "";
		else return str;
	} 

	public String nullChk(String str, String str2) {
		if(str == null) return str2;
		else return str;
	} 
	
	//calmel 
	public String replaceCamel(String str) {
		String val = "";
		String tmpStr = "";
		String result = "";
		boolean isUnderLine = false;
		
		val = str.toLowerCase();
		
		for(int i = 0; i < val.length(); i++) {
			tmpStr = val.substring(i, i + 1);
			if("_".equals(tmpStr)) {
				isUnderLine = true;
			} else {
				if(isUnderLine) {
					result += tmpStr.toUpperCase();
				} else {
					result += tmpStr;
				}
				isUnderLine = false;
			}
		}
		return result;
	}
	
	public String[] replaseCamelArr(String str[]) {
		String[] result = new String[0];
		
		if(str != null) {
			result = new String[str.length];
			
			for(int i = 0; i < str.length; i++) {
				result[i] = replaceCamel(str[i]);
			}
		}
		
		return result;
	} 
			
	//XSS 처리
	public String replaceStr ( String mainStr ) {
		String mainString = mainStr;
		String oldString1 = ">";
		String newString1 = "&gt;";
		String oldString2 = "<";
		String newString2 = "&lt;";
		StringBuffer mainSb = new StringBuffer(mainString);
		int i = mainString.lastIndexOf(oldString1);
		
		while (i >= 0) {
		    mainSb.replace(i, (i + oldString1.length()), newString1);
		    i = mainString.lastIndexOf(oldString1, i - 1);
		}
		
		mainString = mainSb.toString();
		mainSb = new StringBuffer(mainString);
		i = mainString.lastIndexOf(oldString2);
		
		while (i >= 0) {
		    mainSb.replace(i, (i + oldString2.length()), newString2);
		    i = mainString.lastIndexOf(oldString2, i - 1);
		}
		
		return mainSb.toString();
	}
	
	//SQL문을 실행한다.
	public ArrayList executeSql(String driver, String url, String user, String pass, String sqlType, String sql) {
		Connection conn = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		ResultSetMetaData rsmd = null;

		ArrayList list = new ArrayList();
		String[] values = null; 
		int colCnt = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,pass);
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			colCnt = rsmd.getColumnCount();
			
			while (rs.next()) {
				values = new String[colCnt];
				for(int i = 0; i < colCnt; i++) { 
					values[i] = replaceStr(nullChk(rs.getString(rsmd.getColumnName(i+1))));
				}
				list.add(values);
       		}
			 
		} catch(Exception e) {
			System.out.println("에러 발생 "+e);
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (stmt != null) try { stmt.close(); } catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) {}
		}
		
		return list;
	}
	
	//컬럼명을 가져온다.
	public String[] getColumnsNames(String driver, String url, String user, String pass, String sqlType, String sql) {
		Connection conn = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		ResultSetMetaData rsmd = null;
		String[] columnHeaders = null; 
		int colCnt = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,pass);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			colCnt = rsmd.getColumnCount();
			
			columnHeaders = new String[colCnt];
				
			for(int i = 0; i < colCnt; i++) { 
				columnHeaders[i] = nullChk(rsmd.getColumnName(i+1));
			}
			
		} catch(Exception e) {
			System.out.println("에러 발생 "+e);
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (stmt != null) try { stmt.close(); } catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) {}
		}
		
		return columnHeaders;
	}
	
	//컬럼별 헤더를 가져온다.
	public String[] getColumnComments(String driver, String url, String user, String pass, String sqlType, String sql) {
		Connection conn = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		ResultSetMetaData rsmd = null;
		String[] columnHeaders = null; 
		int colCnt = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,pass);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			colCnt = rsmd.getColumnCount();
			
			columnHeaders = new String[colCnt];
				
			for(int i = 0; i < colCnt; i++) { 
				columnHeaders[i] = getColumnCommentSingle(conn, nullChk(rsmd.getColumnName(i+1)));
			}
			
		} catch(Exception e) {
			System.out.println("에러 발생 "+e);
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (stmt != null) try { stmt.close(); } catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) {}
		}
		
		return columnHeaders;
	}
	
	//컬럼별 Comment를 가져온다.
	public String getColumnCommentSingle(Connection conn, String column) {
		PreparedStatement pstmt = null;
		ResultSet         rs    = null;
		
		String comment = "";
		
		String sql = "SELECT NVL(SUBSTR(MAX(COMMENTS), 1, 10), '"+ column +"') COL FROM USER_COL_COMMENTS WHERE COLUMN_NAME = '"+ column +"' AND COMMENTS IS NOT NULL AND ROWNUM = 1";
		
		try {
			pstmt = conn.prepareStatement(sql);
			System.out.println(sql);
			
			rs = pstmt.executeQuery(sql);
			
			while (rs.next()) {
				comment = rs.getString(1);
    		}
		} catch(Exception e) {
			System.out.println("에러 발생 "+e);
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
		}
		
		return comment;
	}
%>

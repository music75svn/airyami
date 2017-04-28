package egovframework.airyami.cmm.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL Object(Table, View, Procedure, Function, Trigger) DDL 생성.
 * 
 * @author
 */
public class MysqlDDLGenerator {

	// MYSQL Connection
	Connection conn = null;

	// DB 환경 변수 정의.
	String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306";
	String MYSQL_USER_ID = "air";
	String MYSQL_USER_PW = "dpdjdial12!@";
	String MYSQL_DB_NAME = "airyami";

	// 로그 파일 FileWriter 정의.
	FileWriter logWriter = null;

	// 루트 경로 정의.
	String ROOT_PATH = "D:\\SLP\\DataBaseSchema\\";
	// 로그 경로 정의.
	String LOG_PATH = ROOT_PATH + "Logs\\";
	String LOG_FILE_PATH = LOG_PATH + "create-ddl.log";
	// 폴더 경로 정의.
	String MYSQL_TABLE_PATH = ROOT_PATH + "TABLE\\";
	String MYSQL_FUNCTION_PATH = ROOT_PATH + "FUNCTION\\";
	String MYSQL_PROCEDURE_PATH = ROOT_PATH + "PROCEDURE\\";
	String MYSQL_TRIGGER_PATH = ROOT_PATH + "TRIGGER\\";
	// 확장자 정의.
	String MYSQL_DDL_EXT = "sql";

	// 새로 추가된 파일 목록
	List<String> newfileList = new ArrayList<String>();
	// 현재 파일 목록, 삭제 목록 생성용.
	Map<String, String> currentFileMap = new HashMap<String, String>();

	/**
	 * MySQL DDL 스크립트 생성 작업 수행.
	 */
	private void execute() {
		// 1. 작업 초기화 처리.
		init();
		// 2. Table, View DDL Script 생성.
		createTableAndViewDdl();
		// 3. Function DDL Script 생성.
		createFunctionDdl();
		// 4. Procedure DDL Script 생성.
		createProcedureDdl();
		// 5. Trigger DDL Script 생성.
		createTriggerDdl();
		// 6.작업 종료 처리.
		finalJob();
	}

	/**
	 * 1. 작업 초기화 처리.
	 */
	private void init() {

		File Dir = new File(LOG_PATH);
		if (!Dir.exists()) {
			Dir.mkdirs();
		}

		// 현재 파일 목록 생성.
		loadCurrentFileList(ROOT_PATH);

		try {
			logWriter = new FileWriter(LOG_FILE_PATH, false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER_ID,
					MYSQL_USER_PW);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 대상 DataBase 선택.
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("USE " + MYSQL_DB_NAME);
			pstmt.executeQuery();
		} catch (SQLException e) {
			trace(e.toString());
		} finally {
			closeStmt(pstmt, null);
		}
	}

	/**
	 * 2. Table, View DDL Script 생성.
	 */
	private void createTableAndViewDdl() {
		String TableNM = null;
		String TableDDL = null;
		// 테이블 목록 조회용.
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		// 테이블 스크립트 조회용.
		PreparedStatement pstmt2 = null;
		ResultSet resultSet2 = null;

		try {
			// 테이블 목록 조회.
			pstmt = conn.prepareStatement("SHOW TABLES");
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				// 테이블 & 뷰 명.
				TableNM = resultSet.getString(1);

				// 테이블 스크립트 조회.
				pstmt2 = conn.prepareStatement("SHOW CREATE TABLE " + TableNM);
				resultSet2 = pstmt2.executeQuery();

				if (resultSet2.next()) {
					TableDDL = resultSet2.getString(2);
					createFile(MYSQL_TABLE_PATH, TableNM, TableDDL);
				}
				resultSet2.close();
				pstmt2.close();
			}

		} catch (SQLException e) {
			trace(e.toString());
		} finally {
			closeStmt(pstmt2, resultSet2);
			closeStmt(pstmt, resultSet);
		}
	}

	/**
	 * 3. Function DDL Script 생성.
	 */
	private void createFunctionDdl() {
		String FunctionNM = null;
		String FunctionDDL = null;
		// 함수 목록 조회용.
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		// 함수 스크립트 조회용.
		PreparedStatement pstmt2 = null;
		ResultSet resultSet2 = null;

		try {
			// 함수 목록 조회.
			pstmt = conn.prepareStatement("SHOW FUNCTION STATUS");
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				// 함수 명
				FunctionNM = resultSet.getString(2);

				// 함수 스크립트 조회.
				pstmt2 = conn.prepareStatement("SHOW CREATE FUNCTION "
						+ FunctionNM);

				// schema가 달라서 펑션을 뽑아낼 수 없을때
				try {

					resultSet2 = pstmt2.executeQuery();

					if (resultSet2.next()) {
						FunctionDDL = resultSet2.getString(3);
						createFile(MYSQL_FUNCTION_PATH, FunctionNM, FunctionDDL);
					}
					resultSet2.close();
					pstmt2.close();

				} catch (Exception e) {
					trace("FUNCTION Fail : " + FunctionNM);
				}
			}

		} catch (SQLException e) {
			trace(e.toString());
		} finally {
			closeStmt(pstmt2, resultSet2);
			closeStmt(pstmt, resultSet);
		}
	}

	/**
	 * 4. Procedure DDL Script 생성.
	 */
	private void createProcedureDdl() {
		String ProcedureNM = null;
		String ProcedureDDL = null;
		// 프로시저 목록 조회용.
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		// 프로시저 스크립트 조회용.
		PreparedStatement pstmt2 = null;
		ResultSet resultSet2 = null;

		try {
			// 프로시저 목록 조회.
			pstmt = conn.prepareStatement("SHOW PROCEDURE STATUS");
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				// 프로시저 명
				ProcedureNM = resultSet.getString(2);

				// 프로시저 스크립트 조회.
				pstmt2 = conn.prepareStatement("SHOW CREATE PROCEDURE "
						+ ProcedureNM);

				// schema가 달라서 프로시저를 뽑아낼 수 없을때
				try {
					resultSet2 = pstmt2.executeQuery();

					if (resultSet2.next()) {
						ProcedureDDL = resultSet2.getString(3);
						createFile(MYSQL_PROCEDURE_PATH, ProcedureNM,
								ProcedureDDL);
					}
					resultSet2.close();
					pstmt2.close();

				} catch (Exception e) {
					trace("PROCEDURE Fail : " + ProcedureNM);
				}
			}

		} catch (SQLException e) {
			trace(e.toString());
		} finally {
			closeStmt(pstmt2, resultSet2);
			closeStmt(pstmt, resultSet);
		}
	}

	/**
	 * 5. Trigger DDL Script 생성.
	 */
	private void createTriggerDdl() {
		String TriggerNM = null;
		String TriggerDDL = null;
		// 트리거 목록 조회용.
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		// 트리거 스크립트 조회용.
		PreparedStatement pstmt2 = null;
		ResultSet resultSet2 = null;

		try {
			// 트리거 목록 조회.
			pstmt = conn.prepareStatement("SHOW TRIGGERS");
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				// 트리거 명
				TriggerNM = resultSet.getString(1);

				// 트리거 스크립트 조회.
				pstmt2 = conn.prepareStatement("SHOW CREATE TRIGGER "
						+ TriggerNM);
				resultSet2 = pstmt2.executeQuery();

				if (resultSet2.next()) {
					TriggerDDL = resultSet2.getString(3);
					createFile(MYSQL_TRIGGER_PATH, TriggerNM, TriggerDDL);
				}
				resultSet2.close();
				pstmt2.close();
			}

		} catch (SQLException e) {
			trace(e.toString());
		} finally {
			closeStmt(pstmt2, resultSet2);
			closeStmt(pstmt, resultSet);
		}
	}

	/**
	 * 6.작업 종료 처리.
	 */
	private void finalJob() {
		// trace("**** finalJob : " +
		// Calendar.getInstance().getTime().toString());

		// MYSQL Connection 종료.
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Log FileWriter 종료.
		if (logWriter != null) {
			try {
				logWriter.close();
			} catch (IOException e) {
				//
			}
		}
	}

	/**
	 * 현재 파일을 목록을 생성.
	 */
	private void loadCurrentFileList(String filePath) {
		File file = new File(filePath);
		File files[] = file.listFiles();

		if (files != null) {
			int cnt = files.length;
			for (int i = 0; i < cnt; i++) {

				if (files[i].isDirectory()) {
					loadCurrentFileList(files[i].getPath());
				} else if (files[i].isFile()
						&& files[i].getPath().length() > 3
						&& files[i].getPath()
								.substring(files[i].getPath().length() - 3)
								.equals(MYSQL_DDL_EXT)) {
					files[i].getPath();
					currentFileMap.put(files[i].getPath(), null);
				}

			}

		}
	}

	/**
	 * 대상 경로에 오브젝트 명으로 파일 생성.
	 */
	private void createFile(String fileBaseDir, String objName, String contents) {
		String fileFullPath = fileBaseDir + objName + "." + MYSQL_DDL_EXT;

		FileWriter fileWriter = null;
		try {
			// 폴더 경로 체크 및 생성.
			File Dir = new File(fileBaseDir);
			if (!Dir.exists()) {
				Dir.mkdirs();
			}

			File file = new File(fileFullPath);
			if (file.exists()) {
				currentFileMap.remove(fileFullPath);
			} else {

			}

			// DDL 파일 생성.
			fileWriter = new FileWriter(fileFullPath, false);
			fileWriter.write(contents);
		} catch (IOException e) {
			trace(e.toString());
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					trace(e.toString());
				}
			}
		}

		trace("create File : " + fileFullPath);
	}

	/**
	 * Statement, ResultSet 종료.
	 */
	private void closeStmt(Statement stmt, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				trace(e.toString());
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				trace(e.toString());
			}
		}
	}

	/**
	 * 작업 로그 처리 함수.
	 */
	private void trace(String str) {
		System.out.println(str);
		try {
			if (logWriter != null) {
				logWriter.write(str);
				logWriter.write("\n");
				logWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 메인 함수.
	 */
	public static void main(String[] args) {
		MysqlDDLGenerator ddlGenerator = new MysqlDDLGenerator();
		ddlGenerator.execute();
	}
}

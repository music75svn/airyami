/*************************************************************************
* CLASS 명  	: ScDeptUtil
* 작 업 자  	: 한봉준
* 작 업 일  	: 2012년 9월 3일 
* 기    능  	: 성과조직관련유틸리티
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    한봉준       2012년 09월 03일 		  최 초 작 업 
**************************************************************************/
package com.lexken.framework.scDeptUtil;

import java.sql.SQLException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.MyAppSqlConfig;
import com.lexken.framework.config.CommonConfig;

public class ScDeptUtilDAO {
	private static ScDeptUtilDAO instance = new ScDeptUtilDAO();
	private HashMap scDeptMapYear = new HashMap();

	public final String SC_DEPT_NM_SEPARATOR;
	public final int SC_DEPT_NM_DEPT_LEVEL;
	
	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	//생성자 private
	private ScDeptUtilDAO() {
		
		//initialize
		CommonConfig commonConfig = CommonConfig.getInstance();
		SC_DEPT_NM_SEPARATOR = commonConfig.getProperty("SC_DEPT_NM_SEPARATOR");
		SC_DEPT_NM_DEPT_LEVEL = Integer.parseInt(commonConfig.getProperty("SC_DEPT_NM_DEPT_LEVEL"));
		
		reSetCodeUtil();
	}

	//생성되어있는 객체 불러오기
	public static ScDeptUtilDAO getInstance() {
		return instance;
	}

	//객체 재생성 - 모든년도 데이터 *사용금지* 성과조직관리에서만 사용하세요!.
	public void reSetCodeUtil() {
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();
		ArrayList yearList = new ArrayList();
		try {
			yearList = (ArrayList)sqlMap.queryForList("com.lexken.framework.scDeptUtil.getYearList");
		} catch (SQLException e){
			logger.error("SQL 트랜잭션 오류 : " + e.toString());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
		}
		for (int i = 0; i < yearList.size(); i++) {
			HashMap map = (HashMap) yearList.get(i);
			String year = (String) map.get("YEAR");

			this.reSetCodeUtil(year);
		}
	}

	//객체 재생성 - 년도별 *사용금지* 성과조직관리에서만 사용하세요!
	public void reSetCodeUtil(String year) {
		synchronized(scDeptMapYear){
			SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();
			ArrayList yearList = new ArrayList();
			try {
				scDeptMapYear.put(year, (ArrayList)sqlMap.queryForList("com.lexken.framework.scDeptUtil.getList", year));
			} catch (SQLException e){
				logger.error("SQL 트랜잭션 오류 : " + e.toString());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.error(ex.toString());
				ex.printStackTrace();
			}
		}
	}

	//scDeptMapYear에서 해당년도 성과조직 목록 불러오기
	public ArrayList getScDeptMapYear(String year) {
		ArrayList list = (ArrayList) scDeptMapYear.get(year);

		return list;
	}


}
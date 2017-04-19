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

import java.util.ArrayList;
import java.util.HashMap;

public class ScDeptUtil {
	private ScDeptUtilDAO scDeptDAO = ScDeptUtilDAO.getInstance();
	
	public ScDeptUtilDAO getInstance() {
		return scDeptDAO;
	}
	
	public void reSetCodeUtil(String year) {
		scDeptDAO.reSetCodeUtil(year);
	}
	
	//기본 뎁스 정보로 조직명 가지고오기
	public String getName(String year, String scDeptId) {
		int depth = scDeptDAO.SC_DEPT_NM_DEPT_LEVEL;

		return getName(year, scDeptId, depth);
	}

	//사용자 뎁스 정보로 조직명 가지고오기
	public String getName(String year, String scDeptId, int depth) {
		ArrayList list = scDeptDAO.getScDeptMapYear(year);
		if (list == null) {
			return "";
		}

		StringBuffer returnNm = new StringBuffer();
		String separator = scDeptDAO.SC_DEPT_NM_SEPARATOR;
		String scDeptIdValue = scDeptId;
		for (int i = 0; i < depth; i++) {
			boolean checkScDeptIdCompare = false;
			for (int j = 0; j < list.size(); j++) {
				HashMap map = (HashMap) list.get(j);
				if (scDeptIdValue != null && scDeptIdValue.equals(map.get("SC_DEPT_ID"))) {
					if (returnNm.length() > 0) {
						returnNm.insert(0, separator);
					}
					returnNm.insert(0, map.get("SC_DEPT_NM"));
					scDeptIdValue = (String) map.get("UP_SC_DEPT_ID");
					checkScDeptIdCompare = true;
				}
			}
			if (!checkScDeptIdCompare) {
				return returnNm.toString();
			}
		}

		return returnNm.toString();
	}
}
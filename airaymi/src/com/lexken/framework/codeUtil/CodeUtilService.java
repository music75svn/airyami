/*************************************************************************
* CLASS 명  	: CodeUtilService
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: 코드유틸
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작   업   일         변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업
*   2    하윤식		 2012.06.25			  코드리스트반환수정
**************************************************************************/
package com.lexken.framework.codeUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.MyAppSqlConfig;

public class CodeUtilService {

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	// CommonModuleDao
	private CodeUtilDao codeUtilDao = CodeUtilDao.getInstance();

	// Instance
	private static CodeUtilService instance = null;
	public static CodeUtilService getInstance() {
		if (instance == null) {
			instance = new CodeUtilService();
		}
		return instance;
	}

	/**
	 * reload 공통코드
	 * @param keyword
	 * @return HashMap<String, ArrayList<CodeUtilVO>>  
	*/
	public HashMap<String, ArrayList<CodeUtilVO>> reLoadCodeHash() {
		/**********************************
		 * 정의
		 **********************************/
		HashMap<String, ArrayList<CodeUtilVO>> map = null;
		String codeGrpId = "";
		CodeUtilVO codeGrpVO = null;
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			ArrayList<CodeUtilVO> grpList = (ArrayList<CodeUtilVO>)codeUtilDao.getCodeGrpList(sqlMap);
			ArrayList<CodeUtilVO> codeList = (ArrayList<CodeUtilVO>)codeUtilDao.getCodeList(sqlMap);
			
			if(grpList != null && 0 < grpList.size()){
				map = new HashMap<String, ArrayList<CodeUtilVO>>();
				map.put("CODE_GRP_LIST", grpList);
				for(int i = 0; i < grpList.size(); i++){
					codeGrpVO = (CodeUtilVO)grpList.get(i);
					codeGrpId = codeGrpVO.getCode_grp_id();
					ArrayList<CodeUtilVO> list = getCodeList(codeGrpId, codeList);
					map.put(codeGrpId, list);
				}
			}
		} catch (SQLException e) {
			logger.error("SQL 트랜잭션 오류 : " + e.toString());
			e.printStackTrace();
			map = null;
		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
			map = null;	
		} 	
		
		return map;		
	}	
	
	/**
	 * 해당 코드그룹에 해당하는 코드리스트반환
	 * @param keyword
	 * @return ArrayList<CodeUtilVO>  
	*/
	public ArrayList<CodeUtilVO> getCodeList(String codeGrpId, ArrayList<CodeUtilVO> codeList) {
		ArrayList<CodeUtilVO> list = new ArrayList<CodeUtilVO>(0);
		
		if(codeList != null && 0 < codeList.size()){
			for(int i = 0; i < codeList.size(); i++) {
				CodeUtilVO codeUtilVO = (CodeUtilVO)codeList.get(i);
				if(codeGrpId.equals(codeUtilVO.getCode_grp_id())) {
					list.add(codeUtilVO);
				}
			}
		}
		
		return list;
	}
 

}

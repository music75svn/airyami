/*************************************************************************
 * CLASS 명      : MemEvalGunAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 03월 15일 
 * 기    능      : 평가군관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 03월 15일          최 초 작 업 
 **************************************************************************/
package com.lexken.mem.tot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class MemEvalGunAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 평가군관리_평가대상자관리화면 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalGunUserModify(SearchMap searchMap) {

        String stMode = searchMap.getString("mode");

     // 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConMember.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직


        searchMap.addList("evalGunUserList", getList("mem.tot.memEvalGun.getEvalGunUserList", searchMap));

		return searchMap;
	}
	
    /**
     * 직원개인평가(평가자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memEvalGunUserInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("mem.tot.memEvalGun.selectUserList", searchMap));
    	
    	return searchMap;
    }
	
	/**
	 * 평가군관리 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalGunList(SearchMap searchMap) {

		return searchMap;
	}
	
	/**
	 * 평가군관리 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalGunList_xml(SearchMap searchMap) {

		searchMap.addList("list", getList("mem.tot.memEvalGun.getList", searchMap));

		return searchMap;
	}

	/**
	 * 평가군관리 상세화면
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalGunModify(SearchMap searchMap) {
		String stMode = searchMap.getString("mode");
		
		if ("MOD".equals(stMode)) {
			Map detailMap = getDetail("mem.tot.memEvalGun.getDetail", searchMap);
			searchMap.addList("detail", detailMap);
			
			// 평가군 부서조회
			searchMap.addList("deptList", getList("mem.tot.memEvalGun.getEvalGunDeptList", searchMap));
		}
		
		// 배분표목록 조회
		searchMap.put("evalMethodGbnId", "01");
		searchMap.addList("evalGiveMethodList", getList("mem.base.memEvalUserRate.getEvalGiveMethodList", searchMap));
		
		return searchMap;
	}

	/**
	 * 평가군관리 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalGunProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");

		/**********************************
		 * 무결성 체크
		 **********************************/
		if (!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"SAVEUSER".equals(stMode) && !"GET".equals(stMode)) {
			returnMap = this.validChk(searchMap);

			if ((Integer) returnMap.get("ErrorNumber") < 0) {
				searchMap.addList("returnMap", returnMap);
				return searchMap;
			}
		}

		/**********************************
		 * 등록/수정/삭제
		 **********************************/
		if ("ADD".equals(stMode)) {
			searchMap = insertDB(searchMap);
		} else if ("MOD".equals(stMode)) {
			searchMap = updateDB(searchMap);
		} else if ("DEL".equals(stMode)) {
			searchMap = deleteDB(searchMap);
		} else if ("SAVE".equals(stMode)) {
			searchMap = allSaveDB(searchMap);
		} else if ("SAVEUSER".equals(stMode)) {
			searchMap = roopDB(searchMap);
		} else if ("GET".equals(stMode)) {
			searchMap = doBscUserInfo(searchMap);
		}

		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}
	
    /**
     * 평가대상자 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap doBscUserInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("mem.tot.memEvalGun.deleteEvalGunUserAll", searchMap);
	        
	        // 평가군 목록 조회
	        List evalGunList = getList("mem.tot.memEvalGun.getEvalGunTargetList", searchMap);
	        
	        SearchMap evalGunMap = new SearchMap();
	        Map evalGunMapTmp = null;
	        if(evalGunList != null){
		        for(int evalGrpIdx = 0; evalGrpIdx < evalGunList.size(); evalGrpIdx++){
		        	evalGunMapTmp = (Map)evalGunList.get(evalGrpIdx);
		        	evalGunMap.put("year", searchMap.getString("findYear"));
		        	evalGunMap.put("evalGunId", evalGunMapTmp.get("EVAL_GUN_ID"));
		        	evalGunMap.put("castTc", evalGunMapTmp.get("CAST_TC"));
		        	evalGunMap.put("posTc", evalGunMapTmp.get("POS_TC"));
		        	evalGunMap.put("applyDt", evalGunMapTmp.get("APPLY_DT"));
		        	String yearTc = (String)evalGunMapTmp.get("YEAR_TC");
		        	
		        	int yearTcMon = 0;
		        	evalGunMap.put("yearTc", yearTc);
		        	// 년차를 달로 변환
		        	if(yearTc != null && !"".equals(yearTc)){
		        		yearTcMon = Integer.parseInt(yearTc) * 12;
		        	}
		        	evalGunMap.put("yearTcMon", yearTcMon+"");
		        	evalGunMap.put("yearTcGubun", evalGunMapTmp.get("YEAR_TC_GUBUN"));

		        	returnMap = insertData("mem.tot.memEvalGun.insertEmpList", evalGunMap, true);
		        }
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	setRollBackTransaction();
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }

    /**
     * 평가대상자세팅
     * @param
     * @return String
     * @throws
     */
    public SearchMap roopDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	String[] empNos = searchMap.getString("empNos").split("\\|", 0);

        	setStartTransaction();

        	returnMap = updateData("mem.tot.memEvalGun.deleteEvalGunUserAll", searchMap, true); //기존제외대상자삭제


        	if(null != empNos) {
		        for (int i = 0; i < empNos.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empNos[i]))) {
			        	searchMap.put("empNo", empNos[i]);
			        	returnMap = insertData("mem.tot.memEvalGun.insertEvalGunUserData", searchMap);
		        	}
		        }
	        }

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }
	
	/**
	 * 평가군관리 등록
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insertDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			// 평가군관리ID 생성
			HashMap evalGrpIdMap = getDetail("mem.tot.memEvalGun.getEvalGunId", searchMap);
			searchMap.put("evalGunId", evalGrpIdMap.get("EVAL_GUN_ID"));

			// 평가군관리 등록
			returnMap = insertData("mem.tot.memEvalGun.insertData", searchMap);
			
			// 평가군부서 등록
			String[] deptCds = searchMap.getStringArray("deptCd");
		    for (int i = 0; i < deptCds.length; i++) {
		    	if(!deptCds[i].equals("")){
		        		searchMap.put("deptCd", deptCds[i]);
		        		returnMap = insertData("mem.tot.memEvalGun.insertEvalGunDeptData", searchMap);
		    	}
		    }		
			
		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 평가군관리 수정
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap updateDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			// 평가군관리 수정
			returnMap = updateData("mem.tot.memEvalGun.updateData", searchMap);

			// 평가군부서 삭제
			returnMap = deleteData("mem.tot.memEvalGun.deleteEvalGunDeptData", searchMap, true);
			
			// 평가군부서 등록
			String[] deptCds = searchMap.getStringArray("deptCd");
		    for (int i = 0; i < deptCds.length; i++) {
		    	if(!deptCds[i].equals("")){
		        		searchMap.put("deptCd", deptCds[i]);
		        		returnMap = insertData("mem.tot.memEvalGun.insertEvalGunDeptData", searchMap);
		    	}
		    }	
			
		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 평가군관리 정렬순서 일괄 저장
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap allSaveDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] evalGunIds = searchMap.getString("evalGunIds").split("\\|", -1);
			String[] sortOrders = searchMap.getString("sortOrders").split("\\|", -1);

			setStartTransaction();

			if (null != evalGunIds && 0 < evalGunIds.length) {
				for (int i = 0; i < evalGunIds.length - 1; i++) {
					searchMap.put("sortOrder", sortOrders[i]);
					logger.debug("sortOrders[i] : " + sortOrders[i]);
					searchMap.put("evalGunId", evalGunIds[i]);
					returnMap = updateData("mem.tot.memEvalGun.allSaveData", searchMap);
				}
			}

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 평가군관리 삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deleteDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] evalGunIds = searchMap.getString("evalGunIds").split("\\|", -1);

			setStartTransaction();

			if (null != evalGunIds && 0 < evalGunIds.length) {
				for (int i = 0; i < evalGunIds.length - 1; i++) {
					searchMap.put("evalGunId", evalGunIds[i]);
					returnMap = updateData("mem.tot.memEvalGun.deleteData",
							searchMap);
				}
			}

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * Validation 체크(무결성 체크)
	 * 
	 * @param SearchMap
	 * @return HashMap
	 */
	private HashMap validChk(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		int resultValue = 0;
		String stMode = searchMap.getString("mode");

		returnMap = ValidationChk.lengthCheck(searchMap.getString("evalGunNm"), "평가군명", 1, 200);
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.lengthCheck(searchMap.getString("evalGiveMethodId"), "배분표", 1, 7);
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.lengthCheck(searchMap.getString("sortOrder"), "순번", 0, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap.put("ErrorNumber", resultValue);
		return returnMap;
	}
}

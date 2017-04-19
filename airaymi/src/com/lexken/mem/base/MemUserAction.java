/*************************************************************************
 * CLASS 명      : MemUserAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 03월 15일 
 * 기    능      : 평가대상자관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 03월 15일          최 초 작 업 
 **************************************************************************/
package com.lexken.mem.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class MemUserAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 평가대상자관리 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memUserList(SearchMap searchMap) {
		
		// 평가대상여부 코드목록 조회
    	searchMap.put("findCodeGrpId", "241");
    	searchMap.addList("evalTgtYnList", getList("bsc.base.code.getList", searchMap));
    	
    	// 평가제외사유 코드목록 조회
    	searchMap.put("findCodeGrpId", "242");
    	searchMap.addList("evalExpRsIdList", getList("bsc.base.code.getList", searchMap));
		
		// 평가그룹목록조회
		searchMap.addList("evalGrpList", getList("mem.base.memUser.getEvalGrpList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 평가대상자관리 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memUserList_xml(SearchMap searchMap) {

		searchMap.addList("list", getList("mem.base.memUser.getList", searchMap));

		return searchMap;
	}

	/**
	 * 평가대상자관리 상세화면
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memUserModify(SearchMap searchMap) {
		String stMode = searchMap.getString("mode");
		if ("MOD".equals(stMode)) {
			searchMap.addList("detail", getDetail("mem.base.memUser.getDetail", searchMap));
		}

		// 평가그룹 조회
		searchMap.addList("evalGrpList", getList("mem.base.memUser.getEvalGrpList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 변경정보조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memModifyEmpList(SearchMap searchMap) {
		
		return searchMap;
	}
	
	/**
	 * 변경정보  데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memModifyEmpList_xml(SearchMap searchMap) {
		
		searchMap.addList("list", getList("mem.base.memUser.getModifyEmpList", searchMap));
		
		return searchMap;
	}

	/**
	 * 평가대상자관리 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memUserProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");

		/**********************************
		 * 무결성 체크
		 **********************************/
		if (!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"GET".equals(stMode) && !"REPEER".equals(stMode) && !"SAVEUSER".equals(stMode)) {
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
		} else if ("REPEER".equals(stMode)) {
			searchMap = doRePeer(searchMap);
		}

		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}
	
    /**
     * 동료평가재설정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap doRePeer(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("findEvalGrpId", "");
	        searchMap.put("findEmpNo", "");
	        returnMap = deleteData("mem.base.memUser.deletePeerAll", searchMap, true);
	        
	        String evalYnByGroup = "Y";
	        // 평가그룹별(동료평가) 평가여부 조회
	        searchMap.put("evalUserGubunId", "03");
	        evalYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
	        
	        logger.debug("evalYnByGroup : "+evalYnByGroup);
	        
	        if("Y".equals(evalYnByGroup)){
		        // 평가그룹 목록 조회
		        List evalGrpList = getList("mem.base.memUser.getEvalGrpTargetList", searchMap);
		        
		        SearchMap evalGrpMap = new SearchMap();
		        Map evalGrpMapTmp = null;
		        if(evalGrpList != null){
			        for(int evalGrpIdx = 0; evalGrpIdx < evalGrpList.size(); evalGrpIdx++){
			        	evalGrpMapTmp = (Map)evalGrpList.get(evalGrpIdx);
			        	evalGrpMap.put("year", searchMap.getString("findYear"));
			        	evalGrpMap.put("evalGrpId", evalGrpMapTmp.get("EVAL_GRP_ID"));
			        	evalGrpMap.put("castTc", evalGrpMapTmp.get("CAST_TC"));
			        	evalGrpMap.put("posTc", evalGrpMapTmp.get("POS_TC"));
			        	evalGrpMap.put("applyDt", evalGrpMapTmp.get("APPLY_DT"));
			        	String yearTc = (String)evalGrpMapTmp.get("YEAR_TC");
			        	int yearTcMon = 0;
			        	evalGrpMap.put("yearTc", yearTc);
			        	// 년차를 달로 변환
			        	if(yearTc != null && !"".equals(yearTc)){
			        		yearTcMon = Integer.parseInt(yearTc) * 12;
			        	}
			        	evalGrpMap.put("yearTcMon", yearTcMon+"");
			        	evalGrpMap.put("yearTcGubun", evalGrpMapTmp.get("YEAR_TC_GUBUN"));
			        	evalGrpMap.put("evalTgtYn", "Y");
			        	evalGrpMap.put("evalExpRsId", "99");
			        	
			        	logger.debug("evalGrpMap : "+evalGrpMap);

			        	returnMap = insertData("mem.base.memUser.insertRePeerYesGroup", evalGrpMap, true);
			        }
		        }
	        }else{
	        	returnMap = insertData("mem.base.memUser.insertRePeerNoGroup", searchMap, true);
	        }
	        
	        insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
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
     * 평가대상자 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap doBscUserInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("mem.base.memUser.deleteData", searchMap);
	        
	        returnMap = deleteData("mem.base.memUser.deletePeerAll", searchMap);
	        
	        // 평가그룹 목록 조회
	        List evalGrpList = getList("mem.base.memUser.getEvalGrpTargetList", searchMap);
	        
	        SearchMap evalGrpMap = new SearchMap();
	        Map evalGrpMapTmp = null;
	        if(evalGrpList != null){
		        for(int evalGrpIdx = 0; evalGrpIdx < evalGrpList.size(); evalGrpIdx++){
		        	evalGrpMapTmp = (Map)evalGrpList.get(evalGrpIdx);
		        	evalGrpMap.put("year", searchMap.getString("findYear"));
		        	evalGrpMap.put("evalGrpId", evalGrpMapTmp.get("EVAL_GRP_ID"));
		        	evalGrpMap.put("castTc", evalGrpMapTmp.get("CAST_TC"));
		        	evalGrpMap.put("posTc", evalGrpMapTmp.get("POS_TC"));
		        	evalGrpMap.put("applyDt", evalGrpMapTmp.get("APPLY_DT"));
		        	String yearTc = (String)evalGrpMapTmp.get("YEAR_TC");
		        	int yearTcMon = 0;
		        	evalGrpMap.put("yearTc", yearTc);
		        	// 년차를 달로 변환
		        	if(yearTc != null && !"".equals(yearTc)){
		        		yearTcMon = Integer.parseInt(yearTc) * 12;
		        	}
		        	evalGrpMap.put("yearTcMon", yearTcMon+"");
		        	evalGrpMap.put("yearTcGubun", evalGrpMapTmp.get("YEAR_TC_GUBUN"));
		        	evalGrpMap.put("evalTgtYn", "Y");
		        	evalGrpMap.put("evalExpRsId", "99");

		        	returnMap = insertData("mem.base.memUser.insertEmpList", evalGrpMap, true);
		        }
	        }
	        
	        insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
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
	 * 평가대상자관리 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memModifyEmpProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");

		/**********************************
		 * 등록/수정/삭제
		 **********************************/
		if ("APPLY".equals(stMode)) {
			searchMap = bscApplyDB(searchMap);
		}

		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}

	/**
	 * 평가대상자관리 변경정보 적용
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap bscApplyDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			// 평가대상자관리 변경정보  적용
			String[] empNos = searchMap.getString("empNos").split("\\|", -1);
			String[] bscDeptCds = searchMap.getString("bscDeptCds").split("\\|", -1);
			String[] bscCastTcs = searchMap.getString("bscCastTcs").split("\\|", -1);
			String[] bscPosTcs = searchMap.getString("bscPosTcs").split("\\|", -1);
			
        	if(null != empNos) {
		        for (int i = 0; i < empNos.length - 1; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empNos[i]))) {
			        	searchMap.put("empNo", empNos[i]);
			        	searchMap.put("bscDeptCd", bscDeptCds[i]);
			        	searchMap.put("bscCastTc", bscCastTcs[i]);
			        	searchMap.put("bscPosTc", bscPosTcs[i]);
			        	returnMap = updateData("mem.base.memUser.updateBscApplyData", searchMap);
		        	}
		        }
	        }
			
			//insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
			
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

        	returnMap = updateData("mem.base.memUser.deletePeerAll", searchMap, true); //기존제외대상자삭제


        	if(null != empNos) {
		        for (int i = 0; i < empNos.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empNos[i]))) {
			        	searchMap.put("empNo", empNos[i]);
			        	returnMap = insertData("mem.base.memUser.insertPeerData", searchMap);
		        	}
		        }
	        }

        	insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
        	
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
	 * 평가대상자관리 등록
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insertDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			// 평가대상자관리 등록
			returnMap = insertData("mem.base.memUser.insertData", searchMap);
			
			insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
			
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
     * 개인업적 평가자 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertEvalUserAdmin(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();        
        try {
        	// 평가자 권한부여
            returnMap = deleteData("mem.base.memUser.deleteAdminDatas", searchMap, true, true);//권한삭제
            returnMap = insertData("mem.base.memUser.insertAdminDatas", searchMap, true, true);//권한등록    
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
	/**
	 * 평가대상자관리 수정
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap updateDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			// 평가대상자관리 수정
			returnMap = updateData("mem.base.memUser.updateData", searchMap);

			insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
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
	 * 평가대상자관리 정렬순서 일괄 저장
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap allSaveDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] gEmpNos = searchMap.getStringArray("gEmpNos");
			String[] evalGrpIds = searchMap.getStringArray("gEvalGrpIds");
			String[] evalTgtYns = searchMap.getStringArray("gEvalTgtYns");
			String[] evalExpRsIds = searchMap.getStringArray("gEvalExpRsIds");
			String[] eval1EmpNos = searchMap.getStringArray("gEval1EmpNos");
			String[] eval2EmpNos = searchMap.getStringArray("gEval2EmpNos");
			String[] eval1EmpNms = searchMap.getStringArray("firstKornms");
			String[] eval2EmpNms = searchMap.getStringArray("secondKornms");

			setStartTransaction();

			if (null != gEmpNos && 0 < gEmpNos.length) {
				for (int i = 0; i < gEmpNos.length; i++) {
					searchMap.put("empNo", gEmpNos[i]);
					searchMap.put("evalGrpId", evalGrpIds[i]);
					searchMap.put("evalTgtYn", evalTgtYns[i]);
					searchMap.put("evalExpRsId", evalExpRsIds[i]);
					searchMap.put("eval1EmpNo", eval1EmpNos[i]);
					searchMap.put("eval2EmpNo", eval2EmpNos[i]);
					searchMap.put("eval1EmpNm", eval1EmpNms[i]);
					searchMap.put("eval2EmpNm", eval2EmpNms[i]);
					returnMap = updateData("mem.base.memUser.allSaveData", searchMap);
				}
				
				insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
			}

		} catch (Exception e) {
			e.printStackTrace();
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
	 * 평가대상자관리 삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deleteDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] empNos = searchMap.getString("empNos").split("\\|", -1);

			setStartTransaction();

			if (null != empNos && 0 < empNos.length) {
				for (int i = 0; i < empNos.length - 1; i++) {
					searchMap.put("empNo", empNos[i]);
					returnMap = updateData("mem.base.memUser.deleteData",
							searchMap);
				}
				
				insertEvalUserAdmin(searchMap); //평가자 /피평가자 권한 부여
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
	 * 평가대상자관리_평가대상자관리화면 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memPeerModify(SearchMap searchMap) {

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
    	String findDeptTmp = searchMap.getString("findDeptCd");
    	logger.debug("findDeptCdFind : "+findDeptTmp);
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직


        searchMap.addList("evalGunUserList", getList("mem.base.memUser.getPeerList", searchMap));

        searchMap.put("findDeptCd", findDeptTmp);
        
		return searchMap;
	}
	
    /**
     * 직원개인평가(평가자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memPeerInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("mem.base.memUser.selectUserList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memUserExcel(SearchMap searchMap) {
    	String excelFileName = "평가대상자관리";
    	String excelTitle = "평가대상자관리목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.DEPT_NM, (String)searchMap.get("findDeptNm")));
    	excelSearchInfoList.add(new ExcelVO("지급", (String)searchMap.get("castTcNm")));
    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("findEmpNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("대상자", "EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가그룹", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("1차평가자사번", "EVAL_1_EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("1차평가자성명", "EVAL_1_EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("2차평가자사번", "EVAL_2_EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("2차평가자성명", "EVAL_2_EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("동료평가자", "EVAL_PEER_COUNT", "center"));
    	excelInfoList.add(new ExcelVO("평가대상자여부", "EVAL_TGT_YN_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가제외사유", "EVAL_EXP_RS_ID_NM", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memUser.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 평가대상자관리 변경정보조회 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memModifyMemExcel(SearchMap searchMap) {
    	String excelFileName = "평가대상자관리";
    	String excelTitle = "평가대상자관리목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("findYear")+"년"));
    	excelSearchInfoList.add(new ExcelVO("직원명", (String)searchMap.get("findEmpNm2")));
    	excelSearchInfoList.add(new ExcelVO("변경구분", (String)searchMap.get("modifyGubunNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("대상자", "EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서코드(인사정보)", "BSC_DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("부서(인사정보)", "BSC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급(인사정보)", "BSC_CAST_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위(인사정보)", "BSC_POS_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memUser.getModifyEmpList", searchMap));
    	
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

		returnMap = ValidationChk.emptyCheck(searchMap.getString("empNo"), "평가대상자명");
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.emptyCheck(searchMap.getString("deptCd"), "조직");
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.emptyCheck(searchMap.getString("castTc"), "직급");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.emptyCheck(searchMap.getString("posTc"), "직위");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.emptyCheck(searchMap.getString("evalGrpId"), "평가그룹");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.emptyCheck(searchMap.getString("eval1EmpNo"), "1차평가자");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.emptyCheck(searchMap.getString("eval2EmpNo"), "2차평가자");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap.put("ErrorNumber", resultValue);
		return returnMap;
	}
}

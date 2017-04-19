/*************************************************************************
 * CLASS 명      : MemEvalGunAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 03월 15일 
 * 기    능      : 별도평가군관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 03월 15일          최 초 작 업 
 **************************************************************************/
package com.lexken.mem.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MemApartEvalGunAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 별도평가군관리 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memApartEvalGunList(SearchMap searchMap) {
    	List gradeList = getList("mem.base.memApartEvalGun.getEvalGrade", searchMap);
    	searchMap.addList("evalGrade", gradeList);
    	
		return searchMap;
	}
	
	/**
	 * 별도평가군관리 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memApartEvalGunList_xml(SearchMap searchMap) {

		searchMap.addList("list", getList("mem.base.memApartEvalGun.getList", searchMap));

		return searchMap;
	}
	
	/**
	 * 별도평가군관리 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memApartEvalGunProcess(SearchMap searchMap) {
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
		if ("SAVE".equals(stMode)) {
			searchMap = allSaveDB(searchMap);
		} else if ("SAVEUSER".equals(stMode)) {
			searchMap = roopDB(searchMap);
		}

		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}
	
    /**
     * 별도평가군관리 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memApartEvalGunExcel(SearchMap searchMap) {
    	String excelFileName = "별도평가군관리";
    	String excelTitle = "별도평가군목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("별도평가군", "EVAL_EXP_RS_NM", "left"));
    	excelInfoList.add(new ExcelVO("대상인원", "TARGET_COUNT", "center"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE_NM", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memApartEvalGun.getList", searchMap));
    	
    	return searchMap;
    	
    }
	
	/**
	 * 별도평가군관리 정렬순서 일괄 저장
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap allSaveDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] evalExpRsIds = searchMap.getString("evalExpRsIds").split("\\|", -1);
			String[] grades = searchMap.getString("grades").split("\\|", -1);

			setStartTransaction();

			if (null != evalExpRsIds && 0 < evalExpRsIds.length) {
				for (int i = 0; i < evalExpRsIds.length - 1; i++) {
					searchMap.put("evalExpRsId", evalExpRsIds[i]);
					searchMap.put("grade", grades[i]);
					returnMap = updateData("mem.base.memApartEvalGun.allSaveData", searchMap);
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
	 * 별도평가군관리_평가대상자관리화면 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memApartEvalGunUserModify(SearchMap searchMap) {

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


        searchMap.addList("evalGunUserList", getList("mem.base.memApartEvalGun.getApartEvalGunUserList", searchMap));

		return searchMap;
	}
	
    /**
     * 직원개인평가(평가자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memApartEvalGunUserInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("mem.base.memApartEvalGun.selectUserList", searchMap));
    	
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

        	if(null != empNos) {
		        for (int i = 0; i < empNos.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empNos[i]))) {
			        	searchMap.put("empNo", empNos[i]);
			        	returnMap = updateData("mem.base.memApartEvalGun.updateEvalExpRsId", searchMap);
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
	 * Validation 체크(무결성 체크)
	 * 
	 * @param SearchMap
	 * @return HashMap
	 */
	private HashMap validChk(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		int resultValue = 0;
		String stMode = searchMap.getString("mode");


		returnMap.put("ErrorNumber", resultValue);
		return returnMap;
	}
}

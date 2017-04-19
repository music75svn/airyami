/*************************************************************************
* CLASS 명      : PrsEvalExcAction
* 작 업 자      : 신인수
* 작 업 일      : 2013년 6월 22일
* 기    능      : 직원개인평가(평가대상제외자)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    신인수      2013년 6월 22일         최 초 작 업
**************************************************************************/
package com.lexken.prs.emp;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.codeUtil.CodeUtilReLoad;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class PrsEvalExcAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 직원개인평가(평가대상제외자) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcList(SearchMap searchMap) {

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findCodeGrpId")))) {
			searchMap.put("findCodeGrpId", 194);
		}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("codeGrpId")))) {
			searchMap.put("codeGrpId", 194);
		}

        searchMap.addList("codeGrpDatail", getDetail("prs.emp.prsEvalExc.getDetail", searchMap));

        return searchMap;
    }

    /**
     * 직원개인평가(평가대상제외자) 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.emp.prsEvalExc.getList", searchMap));

        return searchMap;
    }

    /**
     * 직원개인평가(평가대상제외자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcModify(SearchMap searchMap) {
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


        if("MOD".equals(stMode)) {
           searchMap.addList("prsExcList", getList("prs.emp.prsEvalExc.getPrsExcList", searchMap));
        }

        return searchMap;
    }

    /**
     * 직원개인평가(평가대상제외자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalUserInfo_ajax(SearchMap searchMap) {
    	 searchMap.addList("userList", getList("prs.emp.prsEvalExc.selectUserList", searchMap));
    	return searchMap;
    }


    /**
     * 직원개인평가(평가대상제외자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcNew(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	 searchMap.addList("codeGrpDatail", getDetail("prs.emp.prsEvalExc.getDetail1", searchMap));

    	if("MOD1".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.emp.prsEvalExc.getDetail", searchMap));
    	}

    	return searchMap;
    }


    /**
     * 직원개인평가(평가대상제외자) 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);

            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD1".equals(stMode)) {
        	searchMap = insertDB1(searchMap);
        } else if("MOD1".equals(stMode)) {
        	searchMap = updateDB1(searchMap);
        } else if("DEL".equals(stMode)){
        	searchMap = deleteDB(searchMap);
        }


        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("CALL".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("ADD".equals(stMode)){
        	searchMap = roopDB(searchMap);
        }

        /*****************************************
		 * 세션에 등록되어 있는 코드 정보 reflash
		 *****************************************/
		CodeUtilReLoad codeUtilReLoad = new CodeUtilReLoad();
		codeUtilReLoad.codeUtilReLoad();


        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 코드 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB1(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

	        /**********************************
	         * 코드ID 등록여부 확인
	         **********************************/
	        int cnt = getInt("prs.emp.prsEvalExc.getCodeCount", searchMap);

	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "코드ID"));
	        } else {

	        	String codeDefId = searchMap.getString("codeDefId");

	        	if("01".equals(codeDefId)) {
	        		/**********************************
	    	         * 코드ID 채번
	    	         **********************************/
			        String codeId = getStr("prs.emp.prsEvalExc.getCodeId", searchMap);
			        searchMap.put("codeId", codeId);
	        	}

	            returnMap = insertData("prs.emp.prsEvalExc.insertData1", searchMap);
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
     * 공통코드 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB1(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = updateData("prs.emp.prsEvalExc.updateData1", searchMap);

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
     * 평가제외대상자세팅
     * @param
     * @return String
     * @throws
     */
    public SearchMap roopDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	String[] empns = searchMap.getString("empns").split("\\|", 0);

        	setStartTransaction();

        	returnMap = updateData("prs.emp.prsEvalExc.deletePrsList", searchMap, true); //기존제외대상자삭제


        	if(null != empns) {
		        for (int i = 0; i < empns.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empns[i]))) {
			        	searchMap.put("empn", empns[i]);
			        	returnMap = insertData("prs.emp.prsEvalExc.insertRoopData", searchMap);
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
     * 직원개인평가 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
            setStartTransaction();

            returnMap = updateData("prs.emp.prsEvalExc.deletePrsList", searchMap, true); //기존제외대상자삭제

            returnMap = insertData("prs.emp.prsEvalExc.insertData", searchMap);

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
     * 직원개인평가 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
            setStartTransaction();

            returnMap = updateData("prs.emp.prsEvalExc.updateData", searchMap);

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
     * 직원개인평가 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

	        String[] codeIds = searchMap.getString("codeIds").split("\\|", 0);

	        for (int i=0; i < codeIds.length; i++) {
	            searchMap.put("codeId", codeIds[i]);
	            returnMap = updateData("prs.emp.prsEvalExc.deleteData", searchMap);
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 직원개인평가(평가대상제외자) 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEvalExcListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인평가(평가대상제외자)";
        String excelTitle = "직원개인평가(평가대상제외자) 리스트";

        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        //excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
        //excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));


        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("코드", "CODE_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드명", "CODE_NM", "left"));
    	excelInfoList.add(new ExcelVO("EXCEPT_COUNT", "EXCEPT_COUNT", "left"));


        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.emp.prsEvalExc.getList", searchMap));

        return searchMap;
    }

}

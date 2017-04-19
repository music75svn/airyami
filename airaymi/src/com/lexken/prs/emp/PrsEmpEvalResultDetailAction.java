/*************************************************************************
* CLASS 명      : PrsEmpEvalResultDetailAction
* 작 업 자      : 신인수
* 작 업 일      : 2013년 7월 6일
* 기    능      : 평가결과확인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    신인수      2013년 7월 6일         최 초 작 업
**************************************************************************/
package com.lexken.prs.emp;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class PrsEmpEvalResultDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 평가결과확인 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEmpEvalResultDetailList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {

	    	searchMap.addList("getEvalGrpList", getList("prs.emp.prsEmpEvalResultDetail.getEvalGrpList", searchMap));
	    	

	    	String findEvalGrpId = (String)searchMap.get("findEvalGrpId");

	    	if("".equals(StaticUtil.nullToDefault(findEvalGrpId,""))){
				searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpList", "EVAL_GRP_ID", 0));
			}

	    	//부서조회
	    	searchMap.addList("getDeptList", getList("prs.emp.prsEmpEvalResultDetail.getDeptList", searchMap));
    	}

        return searchMap;
    }

    /**
     * 평가결과확인 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEmpEvalResultDetailList_xml(SearchMap searchMap) {
    	/*LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {

    		searchMap.addList("getEvalGrpId", getList("prs.emp.prsEmpEvalResultDetail.getEvalGrpId", searchMap));

    		String findEvalGrpId = (String)searchMap.get("findEvalGrpId");

    		if("".equals(StaticUtil.nullToDefault(findEvalGrpId,""))){
				searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpId", "EVAL_GRP_ID", 0));
			}
    	}*/

    	//부서조회
    	//searchMap.addList("getDeptList", getList("prs.emp.prsEmpEvalResultDetail.getDeptList", searchMap));
    	
    	searchMap.addList("list", getList("prs.emp.prsEmpEvalResultDetail.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가군 조회 ajax
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("getEvalGrpList", getList("prs.emp.prsEmpEvalResultDetail.getEvalGrpList", searchMap));
    	
    	searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpList", "EVAL_GRP_ID", 0));
    	return searchMap;
    }

    /**
     * 평가군별 부서 조회 ajax
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("getDeptList", getList("prs.emp.prsEmpEvalResultDetail.getDeptList", searchMap));
    	
    	return searchMap;
    }

    /**
     * 평가결과확인 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEmpEvalResultDetailModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");

        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.emp.prsEmpEvalResultDetail.getDetail", searchMap));
        }

        return searchMap;
    }

    /**
     * 평가결과확인 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEmpEvalResultDetailProcess(SearchMap searchMap) {
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
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 평가결과확인 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
            setStartTransaction();

            returnMap = insertData("prs.emp.prsEmpEvalResultDetail.insertData", searchMap);

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
     * 평가결과확인 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
            setStartTransaction();

            returnMap = updateData("prs.emp.prsEmpEvalResultDetail.updateData", searchMap);

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
     * 평가결과확인 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

        try {


            setStartTransaction();
            /*
            if(null !=  && 0 < .length) {
                for (int i = 0; i < .length; i++) {

                    returnMap = updateData("prs.emp.prsEmpEvalResultDetail.deleteData", searchMap);
                }
            }
            */

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
     * 평가결과확인 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap prsEmpEvalResultDetailListExcel(SearchMap searchMap) {
        String excelFileName = "평가결과확인";
        String excelTitle = "평가결과확인 리스트";

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
        excelInfoList.add(new ExcelVO("평가군 코드", "EVAL_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("사원번호", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("조직코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("상위부서코드", "UP_DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("UP_DEPT_KOR_NM", "UP_DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "left"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("순위", "RANKING", "left"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "left"));


        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.emp.prsEmpEvalResultDetail.getList", searchMap));

        return searchMap;
    }

}

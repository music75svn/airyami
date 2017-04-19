/*************************************************************************
* CLASS 명      : ProEduAction
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 18일
* 기    능      : 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      시스템항목관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class GasFacInspBranchAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap gasFacInspBranchList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.gasFacInspBranch.getScDeptMapping", searchMap));
    	}
    	
    	searchMap.addList("deptList", getList("bsc.popup.gasFacInspBranch.getDeptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.gasFacInspBranch.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap gasFacInspBranchList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.gasFacInspBranch.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 부서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("deptList", getList("bsc.popup.gasFacInspBranch.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
        }

        return searchMap;
    }
    

    /**
     * 시스템항목관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemItemModify(SearchMap searchMap) {

    	String stMode = searchMap.getString("mode");

    	if(!"ADD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.popup.systemItem.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 시스템항목관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemItemProcess(SearchMap searchMap) {
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
     * 시스템항목관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = insertData("bsc.popup.systemItem.insertData", searchMap);

        	/**********************************
             * 권한설정
             **********************************/
	        returnMap = insertAdmin(searchMap);

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
     * 시스템항목관리 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("bsc.popup.systemItem.updateData", searchMap);

	        /**********************************
             * 권한설정
             **********************************/
	        returnMap = insertAdmin(searchMap);

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
     * 시스템항목관리 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

        try {
        	setStartTransaction();

        	String[] itemCds = searchMap.getString("itemCds").split("\\|", 0);

        	if(itemCds != null && itemCds.length > 0) {
		        for (int i = 0; i < itemCds.length; i++) {
		            searchMap.put("itemCd", itemCds[i]);
		            returnMap = updateData("bsc.popup.systemItem.deleteData", searchMap);
		        }
        	}

	        /**********************************
             * 권한설정
             **********************************/
	        returnMap = insertAdmin(searchMap);

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();

        	if(e.getMessage().equals(ErrorMessages.FAILURE_FOREIGN_CODE)) {
        		returnMap.put("ErrorNumber", ErrorMessages.FAILURE_FOREIGN_CODE);
    			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_FOREIGN_MESSAGE);
        	} else {
        		returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        	}
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
     * 지표권한등록
     * 04 : KPI담당자(실적입력자)
     * 05 : 실적승인자
     * @param
     * @return String
     * @throws
     */
    public HashMap insertAdmin(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
        	/**********************************
             * 기존 권한삭제
             **********************************/
	        returnMap = updateData("bsc.popup.systemItem.deleteAdmin", searchMap, true);

	        /**********************************
             * 엑셀입력자 입력
             **********************************/
	        returnMap = insertData("bsc.popup.systemItem.insertAdmin", searchMap);

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("year"), "평가년도");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("itemCd"), "항목코드", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("itemNm"), "항목명", 1, 140);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("itemActualDivi"), "연계구분");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("itemDivi"), "등록구분");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        } else {
        	if( "02".equals(searchMap.getString("itemDivi")) ){
        		returnMap = ValidationChk.emptyCheck(searchMap.getString("excelUserId"), "엑셀담당자");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }
        	}/* else if( "01".equals(searchMap.getString("itemDivi")) ){
        		returnMap = ValidationChk.selEmptyCheck(searchMap.getString("systemGbn"), "시스템구분");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }
        	}*/

        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "연계설명", 0, 1000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 연계개수 팝업 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popItemCntList(SearchMap searchMap) {
    	// 연계항목명 조회
        String itemNm = getStr("bsc.popup.systemItem.getItemNm", searchMap);
        searchMap.put("findItemNm", itemNm);

        return searchMap;
    }

    /**
     * 연계개수 팝업 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popItemCntList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.popup.systemItem.getPopItemCntList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "가스시설검사";
    	String excelTitle = "가스시설검사";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("검사구분", "INSP_KIND_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("가스법 구분", "LAW_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("중분류", "STIC_MID_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("소분류", "STIC_SML_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("신규 검사", "INSP_QTY", "center"));
    	excelInfoList.add(new ExcelVO("재검사", "RE_INSP_QTY", "center"));
    	excelInfoList.add(new ExcelVO("적용건수", "TOTAL", "center"));
    	excelInfoList.add(new ExcelVO("상대값", "RLTV_VAL", "center"));
    	excelInfoList.add(new ExcelVO("상대값의합", "TOTAL_RLTV_VAL", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	String deptId = searchMap.getString("deptId");
    	searchMap.put("findDeptId", deptId);
    	
    	ArrayList list = (ArrayList)getList("bsc.popup.gasFacInspBranch.getList", searchMap);
    	
    	searchMap.put("excelDataList", list);
    	
    	return searchMap;
    }
}

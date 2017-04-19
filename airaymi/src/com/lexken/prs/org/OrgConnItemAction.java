/*************************************************************************
* CLASS 명      : OrgConnItemAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 6월 10일
* 기    능      : 연계항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    지원길      2013년 6월 10일            최 초 작 업
**************************************************************************/
package com.lexken.prs.org;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.codeUtil.CodeUtilReLoad;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;

public class OrgConnItemAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 연계항목 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgConnItemList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 연계항목 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgConnItemList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.org.orgConnItem.getList", searchMap));

        return searchMap;
    }



    /**
     * 연계항목 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgConnItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	if("ADD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.org.orgConnItem.getOrgConnItemId", searchMap));
    	}
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.org.orgConnItem.getDetail", searchMap));
    	}    	

        return searchMap;
    }

    /**
     * 공통코드 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgConnItemProcess(SearchMap searchMap) {
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
     * 연계항목 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = insertData("prs.org.orgConnItem.insertData", searchMap);

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
     * 연계항목 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("prs.org.orgConnItem.updateData", searchMap);

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
     * 연계항목 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	String[] orgConnItemIds = searchMap.getString("orgConnItemId").split("\\|", 0);

        	if(null != orgConnItemIds && 0 < orgConnItemIds.length) {
		        for (int i = 0; i < orgConnItemIds.length; i++) {
		            searchMap.put("orgConnItemId", orgConnItemIds[i]);
		            returnMap = updateData("prs.org.orgConnItem.deleteData", searchMap);
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;

        returnMap = ValidationChk.emptyCheck(searchMap.getString("orgConnItemNm"), "연계항목명");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("orgConnItemNm"), "연계항목명", 1, 100);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "연계설명", 0, 1500);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 연계항목 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgConnItemListExcel(SearchMap searchMap) {
    	String excelFileName = "연계항목";
    	String excelTitle = "연계항목 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("연계항목명", StaticUtil.nullToDefault((String)searchMap.get("orgConnItemNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("연계항목코드", "ORG_CONN_ITEM_ID", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("연계항목명", "ORG_CONN_ITEM_NM", "left", "CNT1"));
    	excelInfoList.add(new ExcelVO("등록구분", "TYPE", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("연계개수", "CNT", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("연계설명", "CONTENT", "center", "CNT2"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.org.orgConnItem.getList", searchMap));

        return searchMap;
    }
}

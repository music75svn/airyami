/*************************************************************************
* CLASS 명      : GovMetricGrpAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 6월 20일
* 기    능      : 정평지표POOL
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방승현      2013년 6월 20일         최 초 작 업
**************************************************************************/
package com.lexken.gov.system;

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

public class GovMetricGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 정평지표POOL 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap govMetricGrpList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 정평지표POOL 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap govMetricGrpList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("gov.system.govMetricGrp.getList", searchMap));

        return searchMap;
    }

    /**
     * 정평지표POOL 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap govMetricGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.system.govMetricGrp.getDetail", searchMap));

    	}

    	/**********************************
         * 평가범주 조회
         **********************************/
    	searchMap.addList("evalCatGrpList", getList("gov.system.evalCatGrp.getevalCatModify", searchMap));

    	/**********************************
    	 * 평가부문 조회
    	 **********************************/
    	//searchMap.addList("evalCatList", getList("gov.system.evalCat.getevalCatModify", searchMap));

        return searchMap;
    }

    /**
     * 정평지표POOL 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap govMetricGrpProcess(SearchMap searchMap) {
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
     * 평가범주 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getCatList_ajax(SearchMap searchMap) {

    	searchMap.addList("govCatList", getList("gov.system.evalCat.getCatList", searchMap));

        return searchMap;
    }

    /**
     * 정평지표POOL 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = insertData("gov.system.govMetricGrp.insertData", searchMap);

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
     * 정평지표POOL 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("gov.system.govMetricGrp.updateData", searchMap);

	     // 정평지표에서도 사용하고 있는 지표POOL 개수
	        int cnt = getInt("gov.system.govMetricGrp.getMetricGrpCount", searchMap);

	        if(0 < cnt){
		        /********************************************
		         * 정평지표풀에서도 수정하면 정평지표에서도 수정되기
		         ********************************************/
		        returnMap = updateData("gov.system.govMetricGrp.updateMetricData", searchMap, true);

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
     * 정평지표POOL 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] govMetricGrpIds = searchMap.getString("govMetricGrpIds").split("\\|", 0);

	        setStartTransaction();	       
	        
	        if(null != govMetricGrpIds && 0 < govMetricGrpIds.length) {
		        for (int i = 0; i < govMetricGrpIds.length; i++) {
		            searchMap.put("govMetricGrpId", govMetricGrpIds[i]);
		            
		            int cnt = getInt("gov.system.govMetricGrp.getMetricGrpCount", searchMap);
		            
		            if(0 == cnt){
		            	
		            	returnMap = updateData("gov.system.govMetricGrp.deleteData", searchMap);
		            }else{
		        		returnMap.put("ErrorNumber", ErrorMessages.FAILURE_FOREIGN_CODE);
		    			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_FOREIGN_MESSAGE);
		    			break;
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 정평지표POOL 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap govMetricGrpListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표POOL";
    	String excelTitle = "정평지표POOL 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	/*
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문", "EVAL_CAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("정평지표POOL코드", "GOV_METRIC_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("정평지표POOL명", "GOV_METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.system.govMetricGrp.getList", searchMap));

        return searchMap;
    }

}

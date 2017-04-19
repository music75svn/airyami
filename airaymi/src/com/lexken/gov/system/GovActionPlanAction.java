/*************************************************************************
* CLASS 명      : GovActionPlanAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 06월 24일
* 기    능      : 정평실행계획
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박선혜      2013년 06월 24일         최 초 작 업
**************************************************************************/
package com.lexken.gov.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class GovActionPlanAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 정평실행계획관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActionPlanList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 정평실행계획관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActionPlanList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("gov.system.govActionPlan.getList", searchMap));

        return searchMap;
    }

    /**
     * 정평실행계획관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActionPlanModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();

    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", searchMap.get("year"));
		}

    	/**********************************
         * 정평지표 조회
         **********************************/
//    	searchMap.addList("metricList", getList("gov.system.govMetric.getNonMetricList", searchMap));

        /**********************************
         * 세부평가내용 조회
         **********************************/
//        searchMap.addList("calTypeDetailList", getList("gov.system.govMetric.calTypeDetailList", searchMap));

        /**********************************
         * 착안사항내용 조회
         **********************************/
//        searchMap.addList("detailConceptList", getList("gov.system.govMetric.detailConceptList", searchMap));


    	if ("MOD".equals(stMode)) {
    		if ("".equals(searchMap.get("planId"))) {
    			/**********************************
		         * 지표그룹 상세조회
		         **********************************/
		    	detail = getDetail("gov.system.govActionPlan.getConceptDetail", searchMap);
    		} else {
	    		/**********************************
		         * 지표그룹 상세조회
		         **********************************/
		    	detail = getDetail("gov.system.govActionPlan.getDetail", searchMap);
    		}
	        searchMap.addList("detail", detail);
    	}

        return searchMap;
    }


    /**
     * 세부평가내용 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getDetailList_ajax(SearchMap searchMap) {

    	searchMap.addList("calTypeDetailList", getList("gov.system.govMetric.calTypeDetailList", searchMap));

        return searchMap;
    }

    /**
     * 착안사항내용 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getConceptList_ajax(SearchMap searchMap) {

    	searchMap.addList("detailConceptList", getList("gov.system.govMetric.detailConceptList", searchMap));

    	return searchMap;
    }


    /**
     * 정평실행계획관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActionPlanProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
        	if ("".equals(searchMap.get("planId"))) {
        		searchMap = insertDB(searchMap);
        	} else {
        		searchMap = updateDB(searchMap);
        	}
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 정평실행계획관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {

        	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
    			searchMap.put("findYear", searchMap.get("year"));
    		}

        	setStartTransaction();

        	/**********************************
	         * 지표 코드 채번
	         **********************************/
	        String planId = getStr("gov.system.govActionPlan.getPlanId", searchMap);
	        searchMap.put("planId", planId);

        	returnMap = insertData("gov.system.govActionPlan.insertData", searchMap);


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
     * 정평실행계획관리 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();


	        returnMap = updateData("gov.system.govActionPlan.updateData", searchMap);

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
     * 정평실행계획관리 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] planIds = searchMap.getString("planIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != planIds && 0 < planIds.length) {
		        for (int i = 0; i < planIds.length; i++) {
		            searchMap.put("planId", planIds[i]);
		            returnMap = deleteData("gov.system.govActionPlan.deleteData", searchMap);
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
     * 정평실행계획관리 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActionPlanListExcel(SearchMap searchMap) {
    	String excelFileName = "정평실행계획관리";
    	String excelTitle = "정평실행계획관리 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("year")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가지표", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("세부항목", "DETAIL_EVAL_NM", "left"));
    	excelInfoList.add(new ExcelVO("착안사항", "CONCEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("실행계획", "PLAN_NM", "left"));
    	excelInfoList.add(new ExcelVO("주관부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자",   "INSERT_USER_NM", "left"));

    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", searchMap.get("useYn"));
		}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("gov.system.govActionPlan.getList", searchMap));

        return searchMap;
    }

}

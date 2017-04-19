/*************************************************************************
* CLASS 명      : BosActionResultAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 7월 16일 
* 기    능      : 기관장평가지표 실행결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김상용      2013년 7월 16일         최 초 작 업 
**************************************************************************/

package com.lexken.gov.bos;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class BosActionResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 기관장지표 실행계획 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap bosActionResultList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 기관장지표 실행계획 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap bosActionResultList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("gov.bos.bosActionResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 기관장지표 실행계획 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap bosActionResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();

    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", searchMap.get("year"));
		}

    	/**********************************
         * 경평지표 조회
         **********************************/
    	searchMap.addList("metricList", getList("gov.bos.bosMetric.getNonMetricList", searchMap));

        /**********************************
         * 세부평가내용 조회
         **********************************/
        searchMap.addList("calTypeDetailList", getList("gov.bos.bosMetric.calTypeDetailList", searchMap));

        /**********************************
         * 착안사항내용 조회
         **********************************/
        searchMap.addList("detailConceptList", getList("gov.bos.bosMetric.detailConceptList", searchMap));


    	if("MOD".equals(stMode)) {
    		/**********************************
	         * 지표그룹 상세조회
	         **********************************/
	    	detail = getDetail("gov.bos.bosActionResult.getDetail", searchMap);
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

    	searchMap.addList("calTypeDetailList", getList("gov.bos.bosMetric.calTypeDetailList", searchMap));

        return searchMap;
    }

    /**
     * 착안사항내용 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getConceptList_ajax(SearchMap searchMap) {

    	searchMap.addList("detailConceptList", getList("gov.bos.bosMetric.detailConceptList", searchMap));

    	return searchMap;
    }


    /**
     * 경평실행계획관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap bosActionResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

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
     * 경평실행계획관리 등록
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
	        String planId = getStr("gov.bos.bosActionResult.getPlanId", searchMap);
	        searchMap.put("planId", planId);

        	returnMap = insertData("gov.bos.bosActionResult.insertData", searchMap);


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
     * 경평실행계획관리 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();


	        returnMap = updateData("gov.bos.bosActionResult.updateData", searchMap);

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
     * 경평실행계획관리 삭제
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
		            returnMap = deleteData("gov.bos.bosActionResult.deleteData", searchMap);
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
     * 경평실행계획관리 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap bosActionResultListExcel(SearchMap searchMap) {
    	String excelFileName = "기관장평가지표실행계획";
    	String excelTitle = "기관장평가지표실행계획 리스트";

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
    	excelInfoList.add(new ExcelVO("평가지표", "BOSS_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("세부항목", "DETAIL_EVAL_NM", "left"));
    	excelInfoList.add(new ExcelVO("착안사항", "CONCEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("실행계획", "PLAN_NM", "left"));
    	excelInfoList.add(new ExcelVO("실행결과", "RESULT", "left"));
    	excelInfoList.add(new ExcelVO("주관부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자",   "INSERT_USER_NM", "left"));

    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", searchMap.get("useYn"));
		}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("gov.bos.bosActionResult.getList", searchMap));

        return searchMap;
    }

}

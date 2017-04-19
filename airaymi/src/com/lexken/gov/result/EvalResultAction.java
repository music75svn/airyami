/*************************************************************************
* CLASS 명      : EvalResultAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 6일
* 기    능      : 경영평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 6일         최 초 작 업
**************************************************************************/
package com.lexken.gov.result;

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

public class EvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 경영평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalResultList(SearchMap searchMap) {

    	searchMap.addList("fileList", getList("gov.result.evalResultMng.getAttachFileList", searchMap));

        return searchMap;
    }

    /**
     * 경영평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalResultList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("gov.result.evalResult.getList", searchMap));

        return searchMap;
    }

    /**
     * 경영평가결과 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.result.evalResult.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 경영평가결과 비교화면 조회
     * @param
     * @return String
     * @throws
     */
 public SearchMap popEvalResultList(SearchMap searchMap) {

        return searchMap;
    }
 	/**
 	 * 경영평가결과 비교화면
 	 * @param
 	 * @return String
 	 * @throws
 	 */
 public SearchMap popEvalResultList_xml(SearchMap searchMap) {

	 searchMap.addList("compare", getList("gov.result.evalResult.getCompare", searchMap));

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
     * 경영평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "경영평가결과";
    	String excelTitle = "경영평가결과 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주명", "EVAL_CAT_GRP_NM", "left", "ECG_CNT"));
    	excelInfoList.add(new ExcelVO("평가부문명", "EVAL_CAT_NM", "left", "EC_CNT"));
    	excelInfoList.add(new ExcelVO("정평지표명", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "center"));
    	excelInfoList.add(new ExcelVO("가중치", "GM_WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("비계량등급", "GM_GRADE", "center"));
    	excelInfoList.add(new ExcelVO("점수", "GM_SCORE", "right"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.result.evalResult.getList", searchMap));

        return searchMap;
    }

}

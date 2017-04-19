/*************************************************************************
* CLASS 명      : MetricMappingTabAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 1일
* 기    능      : 내부평가지표연계표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 1일         최 초 작 업
**************************************************************************/
package com.lexken.man.mon;

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

public class MetricMappingTabAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 내부평가지표연계표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricMappingTabList(SearchMap searchMap) {

    	searchMap.addList("stratSubjectList", getList("man.mon.metricMappingTab.getSubjectList", searchMap));

    	searchMap.addList("csfList", getList("man.mon.metricMappingTab.getCsfList", searchMap));

        return searchMap;
    }
    
    /**
     * 내부평가지표연계표 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricMappingTabList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("man.mon.metricMappingTab.getList", searchMap));
    	
    	return searchMap;
    }

    /**
     * 년도별 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getStratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("man.mon.metricMappingTab.getSubjectList", searchMap));

        return searchMap;
    }

    /**
     * 평가단 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getCsfList_ajax(SearchMap searchMap) {

    	searchMap.addList("csfList", getList("man.mon.metricMappingTab.getCsfList", searchMap));

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
     * 내부평가지표연계표 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricMappingTabListExcel(SearchMap searchMap) {
    	String excelFileName = "내부평가지표연계표";
    	String excelTitle = "내부평가지표연계표 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm") + (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("stratNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("CSF", StaticUtil.nullToDefault((String)searchMap.get("csfNm"), "전체")));


    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("경영목표", "MAN_NM", "center"));
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_NM", "center"));
    	excelInfoList.add(new ExcelVO("CSF", "CSF_NM", "left"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI", "MAN_KPI_NM", "left"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI 목표", "TGT_TARGET", "right"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI 실적", "VALUE", "right"));
    	excelInfoList.add(new ExcelVO("내부평가지표 ID", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표 목표", "METRIC_TARGET", "right"));
    	excelInfoList.add(new ExcelVO("내부평가지표 실적", "METRIC_ACTUAL", "right"));
    	excelInfoList.add(new ExcelVO("내부평가지표 점수", "METRIC_SCORE", "right"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.mon.metricMappingTab.getList", searchMap));

        return searchMap;
    }

}

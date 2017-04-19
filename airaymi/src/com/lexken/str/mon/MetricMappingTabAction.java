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
package com.lexken.str.mon;
    
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

    	searchMap.addList("stratSubjectList", getList("str.mon.metricMappingTab.getStratSubjectList", searchMap));
        
    	searchMap.addList("subjectList", getList("str.mon.metricMappingTab.getSubjectList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("subjectList", getList("str.mon.metricMappingTab.getSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * 내부평가지표연계표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMappingTabList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.mon.metricMappingTab.getList", searchMap));

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
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("stratSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("세부과제", StaticUtil.nullToDefault((String)searchMap.get("subjectNm"), "전체")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_SUBJECT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전략과제 달성률", "STRAT_SUBJECT_GOAL_RATE", "right"));
//    	excelInfoList.add(new ExcelVO("STRAT_SUBJECT_STATUS_ID", "STRAT_SUBJECT_STATUS_ID", "left"));
//    	excelInfoList.add(new ExcelVO("STRAT_SUBJECT_SORT_ORDER", "STRAT_SUBJECT_SORT_ORDER", "left"));
//    	excelInfoList.add(new ExcelVO("STRAT_SUBJECT_CNT", "STRAT_SUBJECT_CNT", "left"));
//    	excelInfoList.add(new ExcelVO("세부과제 코드", "SUBJECT_ID", "left"));
    	excelInfoList.add(new ExcelVO("세부과제", "SUBJECT_NM", "left"));
    	excelInfoList.add(new ExcelVO("세부과제 달성률", "SUBJECT_GOAL_RATE", "right"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_STATUS_ID", "SUBJECT_STATUS_ID", "left"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_SORT_ORDER", "SUBJECT_SORT_ORDER", "left"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_CNT", "SUBJECT_CNT", "left"));
//    	excelInfoList.add(new ExcelVO("이행성과지표 코드", "SUBJECT_METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("이행성과지표", "SUBJECT_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("이행성과지표 목표", "SUBJECT_METRIC_TARGET", "right"));
    	excelInfoList.add(new ExcelVO("이행성과지표 실적", "SUBJECT_METRIC_ACTUAL", "right"));
    	excelInfoList.add(new ExcelVO("이행성과지표 달성률", "SUBJECT_METRIC_GOAL_RATE", "right"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_METRIC_STATUS_ID", "SUBJECT_METRIC_STATUS_ID", "left"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_METRIC_SORT_ORDER", "SUBJECT_METRIC_SORT_ORDER", "left"));
//    	excelInfoList.add(new ExcelVO("SUBJECT_METRIC_CNT", "SUBJECT_METRIC_CNT", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표 목표", "METRIC_TARGET", "right"));
    	excelInfoList.add(new ExcelVO("내부평가지표 실적", "METRIC_ACTUAL", "right"));
    	excelInfoList.add(new ExcelVO("내부평가지표 점수", "METRIC_SCORE", "right"));
//    	excelInfoList.add(new ExcelVO("METRIC_STATUS_ID", "METRIC_STATUS_ID", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.mon.metricMappingTab.getList", searchMap));
    	
        return searchMap;
    }
    
}

/*************************************************************************
* CLASS 명      : MetricMappingAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 31일 
* 기    능      : 내평지표 연계 팝업
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 10월 31일         최 초 작 업 
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

public class MetricMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 내평지표 연계 팝업 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricMappingList(SearchMap searchMap) {
    	
    	/************************************************************************************
    	 * 지표입력 기한조회
    	 ************************************************************************************/
    	searchMap.addList("subjectMetricNm", getStr("str.mon.metricMapping.getSubjectMetricNm", searchMap));

        return searchMap;
    }
    
    /**
     * 내평지표 연계 팝업 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.mon.metricMapping.getList", searchMap));

        return searchMap;
    }
     
    /**
     * 내평지표 연계 팝업 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "내평지표 연계 팝업";
    	String excelTitle = "내평지표 연계 팝업 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("KPI ID", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI 명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직ID", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("성과조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적입력자", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("사용자명", "USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("KPI 연계 유형", "METRIC_PROPERTY_ID", "left"));
    	excelInfoList.add(new ExcelVO("METRIC_PROPERTY_NM", "METRIC_PROPERTY_NM", "left"));
    	excelInfoList.add(new ExcelVO("KPI 타입", "TYPE_ID", "left"));
    	excelInfoList.add(new ExcelVO("TYPE_NM", "TYPE_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET", "left"));
    	excelInfoList.add(new ExcelVO("실적", "ACTUAL", "left"));
    	excelInfoList.add(new ExcelVO("평가점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("평가상태", "STATUS", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.mon.metricMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}

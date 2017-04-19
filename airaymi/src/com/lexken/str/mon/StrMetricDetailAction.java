/*************************************************************************
* CLASS 명      : StrMetricDetailAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 30일 
* 기    능      : 이행성과지표 상세
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 10월 30일         최 초 작 업 
**************************************************************************/
package com.lexken.str.mon;
    
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
import com.lexken.framework.util.StaticUtil;

public class StrMetricDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이행성과지표 상세 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricDetailList(SearchMap searchMap) {

    	searchMap.addList("managementList", getList("str.mon.strMetricDetail.getManagement", searchMap));
    	
    	searchMap.addList("stratSubjectList", getList("str.mon.strMetricDetail.getStratSubjectList", searchMap));
    	
    	searchMap.addList("csfList", getList("str.mon.strMetricDetail.getCsfList", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("str.mon.strMetricDetail.getStratSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("str.mon.strMetricDetail.getCsfList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricDetailList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.mon.strMetricDetail.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표 상세 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap strMetricDetailListExcel(SearchMap searchMap) {
    	String excelFileName = "이행성과지표 상세";
    	String excelTitle = "이행성과지표 상세 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년월", (String)searchMap.get("yearNm") + " " + (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("stratSubjectNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_SUBJECT_NM", "left"));
    	excelInfoList.add(new ExcelVO("세부과제", "SUBJECT_NM", "left"));
    	excelInfoList.add(new ExcelVO("이행성과" + StringConstants.METRIC_NM, "SUBJECT_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "CHARGE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "CYCLE_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET", "left"));
    	excelInfoList.add(new ExcelVO("실적", "ACTUAL", "left"));
    	excelInfoList.add(new ExcelVO("달성률", "GOAL_RATE", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.mon.strMetricDetail.getList", searchMap));
    	
        return searchMap;
    }
    
}

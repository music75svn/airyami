/*************************************************************************
* CLASS 명      : MetricActivityActAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 9월 3일 
* 기    능      : KPI 실적상세화면
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 9월 3일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
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

public class MetricActivityActAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * KPI 실적상세화면 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActivityActList(SearchMap searchMap) {
    	
    	ArrayList tmpList = (ArrayList)getList("bsc.mon.metricActivityAct.getKPIInsertUserNm", searchMap);
    	
    	if(null != tmpList && 0 < tmpList.size() ){
    		HashMap tmpHash = (HashMap)tmpList.get(0);
    		
    		searchMap.put("KPINm", (String)tmpHash.get("METRIC_NM") );
    		searchMap.put("KPIStrategyNm", (String)tmpHash.get("STRATEGY_NM") );
    		searchMap.put("KPIInsertUserNm", (String)tmpHash.get("KPI_INSERT_USER_ID") );
    		
    	}
    	searchMap.addList("attachFileList", getList("bsc.mon.metricActivityAct.getInitiativeAttachFile", searchMap));
    	
    	searchMap.addList("list", getList("bsc.mon.metricActivityAct.getList", searchMap));
        return searchMap;
    }
    
    /**
     * KPI 실적상세화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActivityActList_xml(SearchMap searchMap) {
        
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * KPI 실적상세화면 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricActivityActListExcel(SearchMap searchMap) {
    	String excelFileName = "KPI 실적상세화면";
    	String excelTitle = "KPI 실적상세화면 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("기준월", "MON", "left"));
    	excelInfoList.add(new ExcelVO("지표ID", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("PLAN_NM_1MON", "PLAN_NM_1MON", "left"));
    	excelInfoList.add(new ExcelVO("PLAN_CONTENTS_1MON", "PLAN_CONTENTS_1MON", "left"));
    	excelInfoList.add(new ExcelVO("ACT_NM", "ACT_NM", "left"));
    	excelInfoList.add(new ExcelVO("ACT_CONTENTS", "ACT_CONTENTS", "left"));
    	excelInfoList.add(new ExcelVO("ACT_NM_Y", "ACT_NM_Y", "left"));
    	excelInfoList.add(new ExcelVO("NEXT_PLAN_NM", "NEXT_PLAN_NM", "left"));
    	excelInfoList.add(new ExcelVO("NEXT_PLAN_CONTENTS", "NEXT_PLAN_CONTENTS", "left"));
    	excelInfoList.add(new ExcelVO("PLAN_NM_Y", "PLAN_NM_Y", "left"));
    	excelInfoList.add(new ExcelVO("SELF_EVAL", "SELF_EVAL", "left"));
    	excelInfoList.add(new ExcelVO("FINAL_EVAL", "FINAL_EVAL", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_REASON", "EVAL_REASON", "left"));
    	excelInfoList.add(new ExcelVO("신호등상태", "STATUS_ID", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("CAUSE_DESC", "CAUSE_DESC", "left"));
    	excelInfoList.add(new ExcelVO("CATCH_UP", "CATCH_UP", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.metricActivityAct.getList", searchMap));
    	
        return searchMap;
    }
    
}

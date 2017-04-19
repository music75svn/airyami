/*************************************************************************
* CLASS 명      : MetricActivityPlanAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 9월 1일 
* 기    능      : 년간ACTIVITY계획
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 9월 1일             최 초 작 업 
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

public class MetricActivityPlanAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 년간ACTIVITY계획 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActivityPlanList(SearchMap searchMap) {

    	//지표상세정보
    	HashMap detail = new HashMap();
    	detail = getDetail("bsc.module.commModule.getMetricInfo", searchMap);
		searchMap.put("userId", detail.get("INSERT_USER_ID"));
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	
        //전략과제명
    	searchMap.addList("strategyNm", getStr("bsc.module.commModule.getStrategyNm", searchMap));
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));

        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));

    	//년간ACTIVITY계획목록
    	searchMap.addList("activityList", getList("bsc.base.activity.getDetailList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 년간ACTIVITY계획 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInitActPlanList(SearchMap searchMap) {

    	//지표상세정보
    	HashMap detail = new HashMap();
    	detail = getDetail("bsc.module.commModule.getMetricInfo", searchMap);
		searchMap.put("userId", detail.get("INSERT_USER_ID"));
		String scDeptId = (String)searchMap.getString("scDeptId");
		String metricId = (String)searchMap.getString("metricId");
		
		if(!"".equals(StaticUtil.nullToDefault(scDeptId, ""))){
			
			searchMap.put("findScDeptId", scDeptId);
			
		}
		
		if(!"".equals(StaticUtil.nullToDefault(metricId, ""))){
			
			searchMap.put("findMetricId", metricId);
			
		}
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	
        //전략과제명
    	searchMap.addList("strategyNm", getStr("bsc.module.commModule.getStrategyNm", searchMap));
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));

        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));

    	//년간ACTIVITY계획목록
    	searchMap.addList("activityList", getList("bsc.base.activity.getDetailList", searchMap));
    	
    	ArrayList tmpList = (ArrayList)getList("bsc.mon.metricActivityAct.getKPIInsertUserNm", searchMap);
    	
    	searchMap.addList("attachFileList", getList("bsc.mon.metricActivityAct.getInitiativeAttachFile", searchMap));
    	
    	searchMap.addList("list", getList("bsc.mon.metricActivityAct.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 년간ACTIVITY계획 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricActivityPlanListExcel(SearchMap searchMap) {
    	String excelFileName = "년간ACTIVITY계획";
    	String excelTitle = "년간ACTIVITY계획 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드그룹명", "CODE_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("코드부여구분", "CODE_DEF_ID", "left"));
    	excelInfoList.add(new ExcelVO("YEAR_YN", "YEAR_YN", "left"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("삭제일자", "DELETE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.metricActivityPlan.getList", searchMap));
    	
        return searchMap;
    }
    
}

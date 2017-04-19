/*************************************************************************
* CLASS 명      : MetricDefineAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 8월 31일 
* 기    능      : 지표정의서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 8월 31일             최 초 작 업 
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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricDefineAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 지표정의서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDefineList(SearchMap searchMap) {
    	
    	/**********************************
         * 조직별 가중치 합계 조회
         **********************************/
    	searchMap.addList("weightSum", getStr("bsc.base.metric.getDeptWeight", searchMap));
    	
    	/**********************************
         * 조직별 지표 승인상태 조회 
         **********************************/
    	searchMap.addList("status", getDetail("bsc.base.metric.getScDeptInfo", searchMap));
    	
    	
    	HashMap detail = new HashMap();
    	detail = getDetail("bsc.base.metric.getDetail", searchMap);
    	
    	searchMap.addList("detail", detail );
    	
    	
    	/**********************************
         * 전략과제 조회 
         **********************************/
        searchMap.addList("strategyList", getList("bsc.base.metric.getStrategyList", searchMap));
        
        /**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("bsc.base.metric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 득점산식조회 
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("bsc.base.metric.scoreCalTypeList", searchMap));
        
        /**********************************
         * 평가구간대 조회 
         **********************************/
        searchMap.addList("evalSectionList", getList("bsc.base.metric.evalSectionList", searchMap));
        
        /**********************************
         * 평가구간대 등급 조회 
         **********************************/
        searchMap.addList("gradeList", getList("bsc.base.metric.gradeList", searchMap));
        
        /**********************************
         * 목표 조회 
         **********************************/
        searchMap.addList("targetList", getList("bsc.base.metric.getTargetList", searchMap));
        
        /**********************************
         * 년목표 조회
         **********************************/
        searchMap.addList("targetY", getStr("bsc.base.metric.getTargetY", searchMap));
        
        /**********************************
         * KPI 산식명 조회 
         **********************************/
        String typeId = (String)detail.get("TYPE_ID");
        
        if("01".equals(typeId)) {
	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
	        
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
					calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
				}
	        }
	        
	        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
	        searchMap.addList("calTypeColDesc", calTypeColDesc);
        }
        
        
        return searchMap;
    }
    
    /**
     * 지표정의서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDefineList_xml(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 지표정의서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDefineModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.mon.metricDefine.getDetail", searchMap));
    	}
        
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
     * 지표정의서 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricDefineListExcel(SearchMap searchMap) {
    	
    	searchMap = metricDefineList(searchMap);
    	
        return searchMap;
    }
    
    /**
     * 지표정의서 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricDefineExcel(SearchMap searchMap) {
    	
    	/**********************************
         * 조직별 가중치 합계 조회
         **********************************/
    	searchMap.addList("weightSum", getStr("bsc.base.metric.getDeptWeight", searchMap));
    	
    	/**********************************
         * 조직별 지표 승인상태 조회 
         **********************************/
    	searchMap.addList("status", getDetail("bsc.base.metric.getScDeptInfo", searchMap));
    	
    	
    	HashMap detail = new HashMap();
    	detail = getDetail("bsc.base.metric.getDetail", searchMap);
    	
    	searchMap.addList("detail", detail );
    	
    	
    	/**********************************
         * 전략과제 조회 
         **********************************/
        searchMap.addList("strategyList", getList("bsc.base.metric.getStrategyList", searchMap));
        
        /**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("bsc.base.metric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 득점산식조회 
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("bsc.base.metric.scoreCalTypeList", searchMap));
        
        /**********************************
         * 평가구간대 조회 
         **********************************/
        searchMap.addList("evalSectionList", getList("bsc.base.metric.evalSectionList", searchMap));
        
        /**********************************
         * 평가구간대 등급 조회 
         **********************************/
        searchMap.addList("gradeList", getList("bsc.base.metric.gradeList", searchMap));
        
        /**********************************
         * 목표 조회 
         **********************************/
        searchMap.addList("targetList", getList("bsc.base.metric.getTargetList", searchMap));
        
        /**********************************
         * 년목표 조회
         **********************************/
        searchMap.addList("targetY", getStr("bsc.base.metric.getTargetY", searchMap));
        
        /**********************************
         * KPI 산식명 조회 
         **********************************/
        String typeId = (String) detail.get("TYPE_ID");
        
        if ("01".equals(typeId)) {
	        String actCalTypeNm = (String) detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTypeColValueMap = new HashMap<String, String>();
	        
	        if (null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>) calTypeColList.get(i);
		        	calTypeColValueMap.put((String) t.get("CAL_TYPE_COL"), (String) t.get("CAL_TYPE_COL_NM"));
				}
	        }
	        
	        String calTypeColDesc = (String) HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTypeColValueMap);
	        searchMap.addList("calTypeColDesc", calTypeColDesc);
        }

        return searchMap;
    }
}

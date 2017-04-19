/*************************************************************************
* CLASS 명      : CalMetricAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 7월 20일 
* 기    능      : 계산된 지표관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 7월 20일             최 초 작 업 
**************************************************************************/
package com.lexken.man.metric;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class CalMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 계산된 지표관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricList(SearchMap searchMap) {
    	    
        return searchMap;
    }
    
    /**
     * 계산된 지표관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.metric.calMetric.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 계산된 지표관리 상세 하위데이터 조회(달성율연계)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricScoreList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.metric.calMetric.getDetail", searchMap));

        return searchMap;
    }
    
    /**
     * 계산된 지표관리 상세 하위데이터 조회(달성율연계)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricActualList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.calMetric.getActualDetail", searchMap));

        return searchMap;
    }
    
    /**
     * 계산된 지표관리 하위 지표 조회 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricModify(SearchMap searchMap) {
        
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        
    	searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 계산된지표 상세 지표조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInfo_ajax(SearchMap searchMap) {
        
    	searchMap.addList("metricList", getList("bsc.base.calMetric.getMetricDetail", searchMap));

        return searchMap;
    }
    
    
    /**
     * 계산된 지표관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calMetricProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 계산된 지표관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String calGubun = searchMap.getString("calGubun");
        String[] subMetricIds = searchMap.getStringArray("subMetricIds");
        String[] weights = searchMap.getStringArray("weights");
        String[] calTypes = searchMap.getStringArray("calTypes");
        
        try {
        	setStartTransaction();
        
        	returnMap = updateData("man.metric.calMetric.updateData", searchMap);
        	
        	returnMap = updateData("man.metric.calMetric.deleteAllData", searchMap, true);
        	
        	if(subMetricIds != null) {
	        	for (int i = 0; i < subMetricIds.length; i++) {
		            searchMap.put("subMetricId", subMetricIds[i]);
		            
		            if("01".equals(calGubun)) { //실적
		            	searchMap.put("calPlace", calTypes[i]);
		            } else { //달성율
		            	searchMap.put("weight", weights[i]);
		            }
		            
		            returnMap = insertData("man.metric.calMetric.insertData", searchMap);
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
     * 계산된 지표관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        String[] delSubMetricIds = searchMap.getString("delSubMetricIds").split("\\|", 0);
	        
	        for (int i = 0; i < delSubMetricIds.length; i++) {
	            searchMap.put("subMetricId", delSubMetricIds[i]);
	            returnMap = updateData("man.metric.calMetric.deleteData", searchMap, true);
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
 
    
}

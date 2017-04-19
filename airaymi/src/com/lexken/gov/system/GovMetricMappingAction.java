/*************************************************************************
* CLASS 명      : GovMetricMappingAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 6일 
* 기    능      : 정평지표매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 6일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.system;
    
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

public class GovMetricMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평지표매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricMappingList(SearchMap searchMap) {
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("govMetricId")))) {
			searchMap.put("findGovMetric", (String)searchMap.get("govMetricId"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("pgmId")))) {
			searchMap.put("findPgmId", (String)searchMap.get("pgmId"));
		}
    	
    	searchMap.addList("govMetricList", getList("gov.system.govMetricMapping.getMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평지표매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.system.govMetricMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 정평지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getGovMetricList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govMetricList", getList("gov.system.govMetricMapping.getMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평지표매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricMappingModify(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        
    	searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
        
        return searchMap;
    }
    
    /**
     * 정평지표매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricMappingProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
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
     * 정평지표매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	 String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
 	        
 	         setStartTransaction();
 	        
 	         if(null != metricIds && 0 < metricIds.length) {
 		        for (int i = 0; i < metricIds.length; i++) {
 		            searchMap.put("metricId", metricIds[i]);
 		            returnMap = updateData("gov.system.govMetricMapping.deleteData", searchMap, true);
 		           
 		            returnMap = updateData("gov.system.govMetricMapping.insertData", searchMap);
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
     * 정평지표매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.system.govMetricMapping.updateData", searchMap);
	        
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
     * 정평지표매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
		            searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("gov.system.govMetricMapping.deleteData", searchMap);
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
     * 정평지표매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap govMetricMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표매핑";
    	String excelTitle = "정평지표매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("정평지표", (String)searchMap.get("govMetricNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가유형", "TYPE_NM", "left"));
    	excelInfoList.add(new ExcelVO("연계유형", "METRIC_PROPERTY_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("govMetricId")))) {
			searchMap.put("findGovMetric", (String)searchMap.get("govMetricId"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.system.govMetricMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}

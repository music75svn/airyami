package com.lexken.str.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.core.StaticFactory;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MetricMappingMngAction extends CommonService {

	private static final long serialVersionUID = 1L;
	    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이행성과지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMappingMngList(SearchMap searchMap) {
    	
    	String findStratSubjectId = searchMap.getString("findStratSubjectId");
    	String findCsfId = searchMap.getString("findCsfId");
    	String findSubjectMetricId = searchMap.getString("findSubjectMetricId");
    	
    	searchMap.addList("managementList", getList("str.system.metricMappingMng.getManagement", searchMap)); // 경영목표
    	
    	searchMap.addList("stratSubjectList", getList("str.system.metricMappingMng.getStrStratSubjectList", searchMap)); // 전략과제
    	
    	if("".equals(StaticUtil.nullToBlank(findStratSubjectId))){
    		searchMap.put("findStratSubjectId", (String)searchMap.getDefaultValue("stratSubjectList", "STRAT_SUBJECT_ID", 0));
    		//searchMap.addList("findStratSubjectId", (String)searchMap.getDefaultValue("stratSubjectList", "STRAT_SUBJECT_ID", 0));
    	}
    	
    	searchMap.addList("csfList", getList("str.system.metricMappingMng.getCsfList", searchMap)); // CSF
    	
    	if("".equals(StaticUtil.nullToBlank(findCsfId))){
    		searchMap.put("findCsfId", (String)searchMap.getDefaultValue("csfList", "CSF_ID", 0));
    		//searchMap.addList("findSubjectId", (String)searchMap.getDefaultValue("subjectList", "SUBJECT_ID", 0));
    	}
    	
    	searchMap.addList("subjectMetricList", getList("str.system.metricMappingMng.getStrMetricList", searchMap)); // 이행성과지표
    	
    	/*if("".equals(StaticUtil.nullToBlank(findSubjectMetricId))){
    		searchMap.put("findSubjectMetricId", (String)searchMap.getDefaultValue("subjectMetricList", "SUBJECT_METRIC_ID", 0));
    		//searchMap.addList("findSubjectMetricId", (String)searchMap.getDefaultValue("subjectMetricList", "SUBJECT_METRIC_ID", 0));
    	}*/
    	
        return searchMap;
    }
    
    /**
     * 이행성과지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMappingMngList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("str.system.metricMappingMng.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap managementList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("managementList", getList("str.system.metricMappingMng.getManagement", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("str.system.metricMappingMng.getStrStratSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("str.base.strMetric.getCsfList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subjectMetricList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("subjectMetricList", getList("str.system.metricMappingMng.getStrMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 연계지표 매핑화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricMappingModify(SearchMap searchMap) {
    	
    	searchMap.addList("managementList", getList("str.system.metricMappingMng.getManagement", searchMap)); // 경영목표
    	
    	searchMap.addList("stratSubjectList", getList("str.system.metricMappingMng.getStrStratSubjectList", searchMap)); // 전략과제
    	
    	searchMap.addList("csfList", getList("str.system.metricMappingMng.getCsfList", searchMap)); // CSF
    	
    	searchMap.addList("subjectMetricList", getList("str.system.metricMappingMng.getStrMetricList", searchMap)); // 이행성과지표
        
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        
    	searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 연계지표 매핑 지표조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInfo_ajax(SearchMap searchMap) {
        
    	searchMap.addList("metricList", getList("str.system.metricMappingMng.getMetricDetail", searchMap));

        return searchMap;
    }
    
    /**
     * 연계지표 매핑 지표조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap mappingMetricInfo_ajax(SearchMap searchMap) {
        
    	searchMap.addList("mappingMetricList", getList("str.system.metricMappingMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표관리 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMappingMngProcess(SearchMap searchMap) {
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
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteByOneData(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
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
     * 연계지표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        String subjectMetricId = searchMap.getString("subjectMetricId");
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        returnMap = updateData("str.system.metricMappingMng.deleteData", searchMap, true);
	        
	        if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(metricIds[i]))){
			        	searchMap.put("subjectMetricId", subjectMetricId);
			            searchMap.put("metricId", metricIds[i]);
			            returnMap = insertData("str.system.metricMappingMng.insertData", searchMap);
		        	}
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
     * 지표연계 관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteByOneData(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        String subjectMetricId = searchMap.getString("subjectMetricId");
	        
	        setStartTransaction();
	        
	        if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		        	searchMap.put("subjectMetricId", subjectMetricId);
		            searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("str.system.metricMappingMng.deleteByOneData", searchMap);
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
     * 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricMappingMngListExcel(SearchMap searchMap) {
    	String excelFileName = "이행성과지표 맵핑";
    	String excelTitle = "이행성과지표 맵핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("findStratSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("세부과제", StaticUtil.nullToDefault((String)searchMap.get("findSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("이행성과지표", StaticUtil.nullToDefault((String)searchMap.get("findSubjectMetricNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표구분", "TYPE_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표속성", "METRIC_PROPERTY_NM", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.system.metricMappingMng.getList", searchMap));
    	
        return searchMap;
    }
    
	
}


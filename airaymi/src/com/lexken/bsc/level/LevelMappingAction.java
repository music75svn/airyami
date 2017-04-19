/*************************************************************************
* CLASS 명      : LevelMappingAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 난이도평가 평가단/지표 매핑관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 10월 23일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;
    
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

public class LevelMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 난이도평가 평가단/지표 매핑관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelMappingList(SearchMap searchMap) {
    	
    	String findLinked = searchMap.getString("findLinked");
    	String findUserGrpId = searchMap.getString("findUserGrpId");
    	String checkUserGrpId = searchMap.getString("checkUserGrpId");
    	
    	if("".equals(StaticUtil.nullToBlank(findLinked))){
    		findLinked = searchMap.getString("linked");
    	}
    	searchMap.put("findLinked", findLinked);

    	searchMap.addList("evalUserGrpList", getList("bsc.level.levelGrp.getList", searchMap));
    	
    	if("Y".equals(StaticUtil.nullToBlank(findLinked))){
    		
			searchMap.put("findUserGrpId", checkUserGrpId);
    		
    	}
    	
    	/*
    	if("".equals(StaticUtil.nullToBlank(evalUserGrpId))){
    		if("".equals(StaticUtil.nullToBlank(checkUserGrpId))){
    			evalUserGrpId = searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0);
    		}else{
    			evalUserGrpId = checkUserGrpId;
    			searchMap.put("findUserGrpId", evalUserGrpId);
    		}
    	}
    	*/
    	
    	searchMap.put("evalUserGrpId", findUserGrpId);
    	
    	//난이도평가마감구분 마감'Y' 한개의 평가단이라도 마감이 되어있으면 임계치 수정 불가
		searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
		//난이도평가제출구분 제출'Y'
		searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * 난이도평가 평가단/지표 매핑관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.level.levelMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도평가 평가단/지표 매핑관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelMapping_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserGrpList", getList("bsc.level.levelGrp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도평가 평가단/지표 매핑관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
    	
        searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        searchMap.addList("metricList", getList("bsc.level.levelMapping.getMetricList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 난이도평가 평가단/지표 매핑관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelMappingProcess(SearchMap searchMap) {
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
     * 난이도평가 평가단/지표 매핑관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.level.levelMapping.insertData", searchMap);
        
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
     * 난이도평가 평가단/지표 매핑관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	/*
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.level.levelMapping.updateData", searchMap);
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
        */
    	
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        String evalUserGrpId = searchMap.getString("evalUserGrpId");
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        searchMap.put("evalUserGrpId", evalUserGrpId);
	        
	        returnMap = updateData("bsc.level.levelMapping.deleteData", searchMap, true);
	        
	        if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(metricIds[i]))){
			        	searchMap.put("evalUserGrpId", evalUserGrpId);
			            searchMap.put("metricId", metricIds[i]);
			            returnMap = insertData("bsc.level.levelMapping.insertData", searchMap);
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
     * 난이도평가 평가단/지표 매핑관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalUserGrpIds = searchMap.getString("evalUserGrpIds").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalUserGrpIds && 0 < evalUserGrpIds.length) {
		        for (int i = 0; i < evalUserGrpIds.length; i++) {
		            searchMap.put("evalUserGrpId", evalUserGrpIds[i]);
			searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("bsc.level.levelMapping.deleteMetricData", searchMap);
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    /**
     * 난이도평가 평가단 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap levelMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "난이도평가 평가단 관리";
    	String excelTitle = "난이도평가 평가단 관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("userGrpNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표", "METRIC_NM", "left"));
  
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.levelMapping.getList", searchMap));
    	
        return searchMap;
    }
}

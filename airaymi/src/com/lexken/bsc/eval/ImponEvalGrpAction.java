/*************************************************************************
* CLASS 명      : ImponEvalGrpAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 23일 
* 기    능      : 비계량평가단관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 23일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.eval;
    
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

public class ImponEvalGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량평가단관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalGrpList(SearchMap searchMap) {
    	
    	String findCycle = (String)searchMap.get("findCycle");
    	searchMap.addList("evalCycle", getList("bsc.eval.imponEvalGrp.getEvalCycle", searchMap));
    	
    	//디폴트 조회조건 설정(findCycle)
    	if("".equals(StaticUtil.nullToBlank(findCycle))) {
    		searchMap.put("findCycle", searchMap.getDefaultValue("evalCycle", "CODE_ID", 0));
    	}

    	searchMap.addList("metricGrpList", getList("bsc.eval.imponEvalGrp.getMetricGrpList", searchMap));
    	searchMap.addList("evalable", getDetail("bsc.eval.imponEvalGrp.getEvalable", searchMap));

    	return searchMap;
    }

    /**
     * 비계량평가단관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponEvalGrpExcel(SearchMap searchMap) {
    	String excelFileName = "비계량평가단관리";
    	String excelTitle = "비계량평가단관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("cycleNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("KPI POOL", "METRIC_GRP_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("성과조직", "DEPT_NM", "center", "CNT2"));
    	excelInfoList.add(new ExcelVO("성명", "EVAL_EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("직급명", "JIKGUB_NM", "center"));
    	excelInfoList.add(new ExcelVO("사번", "EVAL_EMP_ID", "center"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));

    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.eval.imponEvalGrp.getExcelList", searchMap));
    	
        return searchMap;
    }
        
    /**
     * 비계량평가단관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalGrpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.eval.imponEvalGrp.getList", searchMap));
        return searchMap;
    }
    
    /**
     * 비계량평가단관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalGrpModify(SearchMap searchMap) {
    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
		searchMap.addList("userList", getList("bsc.eval.imponEvalGrp.getUserList", searchMap));
        return searchMap;
    }
    
    /**
     * 비계량평가단관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalGrpProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode) || ("WEIGHT".equals(stMode))) {
        	searchMap = insertDB(searchMap);
        }
        
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 비계량평가단관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        if("ADD".equals(searchMap.get("mode"))){//평가단 등록/수정
        	returnMap = updateData("bsc.eval.imponEvalGrp.deleteData", searchMap, true);
        	String[] userIds = searchMap.getStringArray("userIds");
        	String[] weights = searchMap.getStringArray("weights");
        	for (int i = 0; i < userIds.length; i++) {
        		searchMap.put("userId", userIds[i]);
        		searchMap.put("weight", weights[i]);
        		returnMap = insertData("bsc.eval.imponEvalGrp.insertData", searchMap);
        		returnMap = updateData("bsc.eval.imponEvalGrp.deleteStatus", searchMap, true);
        		returnMap = insertData("bsc.eval.imponEvalGrp.insertStatus", searchMap);
        	}
        	
        } else if("WEIGHT".equals(searchMap.get("mode"))){//가중치 등록/수정
        	String[] userIds = searchMap.getStringArray("userIds");
        	String[] weights = searchMap.getStringArray("weights");
        	if(userIds!=null && userIds.length>0){
        		for (int i = 0; i < userIds.length; i++) {
        			searchMap.put("userId", userIds[i]);
        			searchMap.put("weight", weights[i]);
        			returnMap = updateData("bsc.eval.imponEvalGrp.updateWeights", searchMap);
        		}
        	}
        }
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }
    
    /**
     * 비계량평가단관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.eval.imponEvalGrp.updateData", searchMap);
	        
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
     * 비계량평가단관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] imponEvalGrpIds = searchMap.getString("imponEvalGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < imponEvalGrpIds.length; i++) {
	            searchMap.put("imponEvalGrpId", imponEvalGrpIds[i]);
	            returnMap = updateData("bsc.eval.imponEvalGrp.deleteData", searchMap);
	        }
	        
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    */   
        
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
    
    
}

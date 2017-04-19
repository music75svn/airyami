/*************************************************************************
* CLASS 명      : DeptStrategyAction
* 작 업 자      : 형광민
* 작 업 일      : 2012년 7월 23일 
* 기    능      : 전략과제관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    형광민      2012년 7월 23일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class DeptStrategyAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략과제관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyList(SearchMap searchMap) {
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        } else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }
        
        /************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String findScDeptId = searchMap.getString("findScDeptId");
    	searchMap.put("findSearchCodeId", findScDeptId);
    	if("".equals(findScDeptId)) {
    		searchMap.put("findScDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("findScDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}
    	
    	/***********************************************
         * 파라미터가 null로 들어올 때 디폴트 조회조건 셋팅.
         ***********************************************/
    	String findUseYn = (String)searchMap.getString("findUseYn", "Y");				// 사용여부가 없으면 사용을 셋팅.
    	searchMap.put("findUseYn", findUseYn);

    	searchMap.addList("perspectiveList", getList("bsc.base.perspective.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.base.deptStrategy.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
    	searchMap.addList("strategyList", getList("bsc.base.deptStrategy.getStrategyList", searchMap));
    	searchMap.addList("deptStrategyList", getList("bsc.base.deptStrategy.getDeptStrategyList", searchMap));
    	
//    	searchMap.addList("detail", getDetail("bsc.base.deptStrategy.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 전략과제관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyProcess(SearchMap searchMap) {
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
        if ("ADD".equals(stMode)) {
        //	String[] strategys = searchMap.getStringArray("strategyIds");
        //	String[] deptStrategies = searchMap.getStringArray("deptStrategies");
        	
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("BATCH".equals(stMode)) {
        	searchMap = batchDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 전략과제관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	String strategyId = searchMap.getString("strategyIds");
        	String [] strategys = searchMap.getString("strategyIds").split("\\|", 0);
        	
        	
        	
        	if (strategyId != null) {
	        	for (int i = 0; i < strategys.length; i++) {
	        		searchMap.put("strategyId", strategys[i]);
	        		deleteData("bsc.base.deptStrategy.deleteScDeptStrategy", searchMap, true);
	        	}
        	}
        	
        	String deptStrategiesId = searchMap.getString("deptStrategiesIds");
        	String [] deptStrategies = searchMap.getString("deptStrategiesIds").split("\\|", 0);
        	
        	if (deptStrategiesId != null && !"".equals(deptStrategiesId)) {
	        	for (int i = 0; i < deptStrategies.length; i++) {
	        		searchMap.put("strategyId", deptStrategies[i]);
	        		returnMap = insertData("bsc.base.deptStrategy.insertData", searchMap);
	        		
	        		if (returnMap.get("ErrorNumber") != null && (Integer)returnMap.get("ErrorNumber") < 0) {
	        			setRollBackTransaction();
	                	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
	        			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
	        		}
	        	}
        	}	
        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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
     * 전략과제관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.deptStrategy.updateData", searchMap);
	        
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
     * 전략과제관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    try {
	        String[] strategyIds = searchMap.getString("delStrategyIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < strategyIds.length; i++) {
	            searchMap.put("strategyId", strategyIds[i]);
	            returnMap = deleteData("bsc.base.deptStrategy.deleteData", searchMap);
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
     * 전략과제관리 일괄 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap batchDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        /**********************************
         * Parameter setting 
         **********************************/
        String[] strategyIds = searchMap.getString("strategyIds").split("\\|", 0);
        String[] perspectiveIds = searchMap.getStringArray("perspectiveIds");
        String[] sortOrders = searchMap.getStringArray("sortOrders");
        
        try {
	        setStartTransaction();
	        
        	for (int i = 0; i < strategyIds.length; i++) {
        		searchMap.put("strategyId", strategyIds[i]);
        		if ("-1".equals(perspectiveIds[i]))
        			searchMap.put("perspectiveId", null);
        		else
        			searchMap.put("perspectiveId", perspectiveIds[i]);
	            searchMap.put("sortOrder", sortOrders[i]);
	            
	            returnMap = updateData("bsc.base.deptStrategy.updateBatchData", searchMap);
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
     * 전략과제관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap deptStrategyExcel(SearchMap searchMap) {
    	String excelFileName = "전략과제";
    	String excelTitle = "전략과제 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("findScDeptNm"), "전체")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전략과제코드", "STRATEGY_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("전략과제명", "STRATEGY_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("BSC관점", "PERSPECTIVE_NM", "left", "CNT"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "center", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.deptStrategy.getExcelList", searchMap));
    	
        return searchMap;
    }
}

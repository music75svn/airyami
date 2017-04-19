/*************************************************************************
* CLASS 명      : DeptMappingAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 13일 
* 기    능      : 내평조직매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
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

public class DeptMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 내평조직매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 내평조직매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.measure.deptMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 내평조직매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.deptMapping.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 내평조직매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptMappingProcess(SearchMap searchMap) {
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
     * 내평조직매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
	         * 매핑 등록여부 확인
	         **********************************/
	        int cnt = getInt("gov.measure.deptMapping.getMappingCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "평가조직"));
	        } else {
	            returnMap = insertData("gov.measure.deptMapping.insertData", searchMap);
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
     * 내평조직매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.measure.deptMapping.updateData", searchMap);
	        
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
     * 내평조직매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	    	setStartTransaction();
	    	
	    	String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
	        
        	if(null != scDeptIds && 0 < scDeptIds.length) {
		        for (int i = 0; i < scDeptIds.length; i++) {
		            searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("gov.measure.deptMapping.deleteData", searchMap);
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
     * 내평조직매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap deptMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "내평조직매핑";
    	String excelTitle = "내평조직매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	 
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가조직ID", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("조직평가그룹", "SC_DEPT_GRP_NM", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.deptMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}

/*************************************************************************
* CLASS 명      : EvalCatGrpAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 1일 
* 기    능      : 평가범주관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 1일         최 초 작 업 
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

public class EvalCatGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가범주관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCatGrpList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가범주관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCatGrpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.system.evalCatGrp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가범주관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCatGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.system.evalCatGrp.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가범주관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCatGrpProcess(SearchMap searchMap) {
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
     * 평가범주관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.system.evalCatGrp.insertData", searchMap);
        
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
     * 평가범주관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.system.evalCatGrp.updateData", searchMap);
	        
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
     * 평가범주관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalCatGrpIds = searchMap.getString("evalCatGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalCatGrpIds && 0 < evalCatGrpIds.length) {
		        for (int i = 0; i < evalCatGrpIds.length; i++) {
		            searchMap.put("evalCatGrpId", evalCatGrpIds[i]);
		            returnMap = updateData("gov.system.evalCatGrp.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가범주관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalCatGrpListExcel(SearchMap searchMap) {
    	String excelFileName = "평가범주관리";
    	String excelTitle = "평가범주관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	/*
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주코드", "EVAL_CAT_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문개수", "EVAL_CAT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.system.evalCatGrp.getList", searchMap));
    	
        return searchMap;
    }
    
}

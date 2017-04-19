/*************************************************************************
* CLASS 명      : EvalGradeAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 22일 
* 기    능      : 평가등급및환산표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 22일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.common;
    
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
import com.lexken.framework.util.StaticUtil;

public class EvalGradeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가등급및환산표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가등급및환산표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.common.evalGrade.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가등급및환산표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.common.evalGrade.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가등급및환산표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeProcess(SearchMap searchMap) {
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
     * 평가등급및환산표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.common.evalGrade.insertData", searchMap);
        
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
     * 평가등급및환산표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.common.evalGrade.updateData", searchMap);
	        
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
     * 평가등급및환산표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalMethodIds = searchMap.getString("evalMethodIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalMethodIds && 0 < evalMethodIds.length) {
		        for (int i = 0; i < evalMethodIds.length; i++) {
		            searchMap.put("evalMethodId", evalMethodIds[i]);
		            returnMap = updateData("bsc.common.evalGrade.deleteData", searchMap);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("evalMethodNm"), "평가방법", 1, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.selEmptyCheck(searchMap.getString("evalMethodGbnId"), "평가방법 구분");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		  
		returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "설명", 0, 2000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가등급및환산표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalGradeListExcel(SearchMap searchMap) {
    	String excelFileName = "평가등급및환산표";
    	String excelTitle = "평가등급및환산표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법 구분", "EVAL_METHOD_GBN_NM", "left"));
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left","",15000));
    	excelInfoList.add(new ExcelVO("평가항목수", "GRADE_ITEM_CNT", "left"));
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findUseYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.common.evalGrade.getList", searchMap));
    	
        return searchMap;
    }
    
}

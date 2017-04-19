/*************************************************************************
* CLASS 명      : EvalGradeTabAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 23일 
* 기    능      : 등급표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 23일         최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;
    
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

public class MemEvalGradeTabAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 등급표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeTabList(SearchMap searchMap) {
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("year", (String)searchMap.get("findYear"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalMethodId")))) {
			searchMap.put("findEvalMethodId", (String)searchMap.get("evalMethodId"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("evalMethodId")))) {
			searchMap.put("evalMethodId", (String)searchMap.get("findEvalMethodId"));
		}
    	
    	searchMap.addList("evalMethodDetail", getDetail("mem.base.memEvalGrade.getDetail", searchMap));
	
        return searchMap;
    }
    
    /**
     * 등급표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeTabList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.base.memEvalGradeTab.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 등급표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeTabModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("evalMethodDetail", getDetail("mem.base.memEvalGrade.getDetail", searchMap));
    	
    	searchMap.addList("list", getList("mem.base.memEvalGradeScore.getList", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memEvalGradeTab.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 등급표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeTabProcess(SearchMap searchMap) {
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
     * 등급표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("mem.base.memEvalGradeTab.insertData", searchMap);
        
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
     * 등급표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("mem.base.memEvalGradeTab.updateData", searchMap);
	        
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
     * 등급표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalMethodIds = searchMap.getString("evalMethodIds").split("\\|", 0);
			String[] gradeItemIds = searchMap.getString("gradeItemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalMethodIds && 0 < evalMethodIds.length) {
		        for (int i = 0; i < evalMethodIds.length; i++) {
		            searchMap.put("evalMethodId", evalMethodIds[i]);
			searchMap.put("gradeItemId", gradeItemIds[i]);
		            returnMap = updateData("mem.base.memEvalGradeTab.deleteData", searchMap);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("gradeItemNm"), "등급항목 명", 1, 33);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 등급표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalGradeTabListExcel(SearchMap searchMap) {
    	String excelFileName = "등급표";
    	String excelTitle = "등급표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가방법 코드", "EVAL_METHOD_ID", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 코드", "GRADE_ITEM_ID", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 명", "GRADE_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memEvalGradeTab.getList", searchMap));
    	
        return searchMap;
    }
    
}

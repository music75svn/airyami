/*************************************************************************
* CLASS 명      : GradeConvertAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 난이도등급점수
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 10월 23일         최 초 작 업 
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

public class GradeConvertAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 난이도등급점수 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap gradeConvertList(SearchMap searchMap) {

    	/**********************************
         * 난이도평가마감구분 마감'Y' 
         * 한개의 평가단이라도 마감이 되어있으면 임계치 수정 불가
         **********************************/
	    searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
	    
	    /**********************************
         * 난이도평가제출구분 제출'Y'
         **********************************/
	    searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
	    
    	searchMap.addList("gradeList", getList("bsc.level.gradeConvert.getGradeList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 난이도등급점수 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap gradeConvertList_xml(SearchMap searchMap) {
        
    	ArrayList gradeList = (ArrayList)getList("bsc.level.gradeConvert.getGradeList", searchMap);
      	 
    	String[] gradeArr = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeArr = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeArr[i] = (String)map.get("GRADE"); 
    		}
    	}
    	
    	searchMap.put("gradeArr", gradeArr);
    	searchMap.addList("gradeList", gradeList);
    	
        searchMap.addList("list", getList("bsc.level.gradeConvert.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도등급점수 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap gradeConvertModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("gradeList", getList("bsc.level.gradeConvert.getGradeList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 난이도등급점수 상세화면(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap gradeConvertDetail_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.level.gradeConvert.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 난이도등급점수 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap gradeConvertProcess(SearchMap searchMap) {
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
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 난이도등급점수 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.level.gradeConvert.insertData", searchMap);
        
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
     * 난이도등급점수 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] grades = searchMap.getStringArray("grades");
	        String[] minValues = searchMap.getStringArray("minValues");
	        String[] maxValues = searchMap.getStringArray("maxValues");
	        
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.level.gradeConvert.deleteData", searchMap, true);
	        
	        if(null != grades && 0 < grades.length) {
		        for (int i = 0; i < grades.length; i++) {
		            searchMap.put("grade", grades[i]);
		            searchMap.put("minValue", minValues[i]);
		            searchMap.put("maxValue", maxValues[i]);
		            returnMap = updateData("bsc.level.gradeConvert.insertData", searchMap);
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
     * 난이도등급점수 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] grades = searchMap.getString("grades").split("\\|", 0);
			String[] itemGrpIds = searchMap.getString("itemGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != grades && 0 < grades.length) {
		        for (int i = 0; i < grades.length; i++) {
		            searchMap.put("grade", grades[i]);
		            searchMap.put("itemGrpId", itemGrpIds[i]);
		            returnMap = updateData("bsc.level.gradeConvert.deleteData", searchMap, true);
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
     * 난이도등급점수 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap gradeConvertListExcel(SearchMap searchMap) {
    	String excelFileName = "난이도등급점수";
    	String excelTitle = "난이도등급점수 리스트";
    	
    	HashMap gradeMap         = new HashMap();
    	HashMap mainMap         = new HashMap();
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	
    	ArrayList gradeList = (ArrayList)getList("bsc.level.gradeConvert.getGradeList", searchMap);
     	 
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가항목그룹명", "ITEM_GRP_NM", "left"));
    	
    	if(0<gradeList.size()){
    		for(int i=0 ; i<gradeList.size() ; i++){
    			
    			gradeMap = (HashMap)gradeList.get(i);
    			
    			excelInfoList.add(new ExcelVO((String)gradeMap.get("GRADE"), "GRADE_RANGE"+(String)gradeMap.get("GRADE"), "left"));
    		}
    	}
    	
		searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
						
						
		String[] gradeArr = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeArr = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeArr[i] = (String)map.get("GRADE"); 
    		}
    	}
    	
    	searchMap.put("gradeArr", gradeArr);
    	
    	ArrayList list = (ArrayList)getList("bsc.level.gradeConvert.getList", searchMap);
    	
    	if(0<list.size() && 0<gradeList.size()){
    		String max_val = "";
			String min_val = "";
			String grade_val = "";
    		for(int i=0 ; i<list.size() ; i++){
    			
    			mainMap = (HashMap)list.get(i);
    			
    			for(int j=0 ; j<gradeList.size() ; j++){
    				gradeMap = (HashMap)gradeList.get(j);
    				
    				grade_val = gradeMap.get("GRADE").toString();
    				
    				max_val = StaticUtil.nullToDefault(mainMap.get("MAX_"+grade_val), "");
    				min_val = StaticUtil.nullToDefault(mainMap.get("MIN_"+grade_val), "");
    				
	    			mainMap.put("GRADE_RANGE"+gradeMap.get("GRADE"), max_val+"~"+min_val);
    			}
    		}
    	}
    	
    	searchMap.put("excelDataList", list);
    	
    	
        return searchMap;
    }
    
}

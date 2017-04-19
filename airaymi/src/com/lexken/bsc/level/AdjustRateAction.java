/*************************************************************************
* CLASS 명      : AdjustRateAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 난이도 조정계수
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

public class AdjustRateAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 난이도 조정계수 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap adjustRateList(SearchMap searchMap) {
    	
    	/************************************************************************************
         * 난이도평가마감구분 마감'Y' 
         * 한개의 평가단이라도 마감이 되어있으면 임계치 수정 불가
         ************************************************************************************/
	    searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
	    
	    /**********************************
         * 난이도평가제출구분 제출'Y'
         **********************************/
	    searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
	    
	    /**********************************
    	 * 난이도등급명 조회
    	 **********************************/
    	searchMap.addList("secondGradeId", getStr("bsc.level.adjustRate.getSecondGradeId", searchMap));
    	searchMap.addList("secondGradeNm", getStr("bsc.level.adjustRate.getSecondGradeNm", searchMap));

    	searchMap.addList("gradeList", getList("bsc.level.adjustRate.getGradeList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 난이도 조정계수 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap adjustRateList_xml(SearchMap searchMap) {
    	ArrayList gradeList = (ArrayList)getList("bsc.level.adjustRate.getGradeList", searchMap);
     	
    	/************************************************************************************
    	 * 등급 가져오기
    	 ************************************************************************************/
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
    	
    	/************************************************************************************
    	 * 난이도등급 조회
    	 ************************************************************************************/
    	searchMap.put("firstGradeId", getStr("bsc.level.adjustRate.getFirstGradeId", searchMap));
    	searchMap.put("secondGradeId", getStr("bsc.level.adjustRate.getSecondGradeId", searchMap));
    	
    	
        searchMap.addList("list", getList("bsc.level.adjustRate.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도 조정계수 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap adjustRateModify(SearchMap searchMap) {
    	
    	ArrayList gradeList = (ArrayList)getList("bsc.level.adjustRate.getGradeList", searchMap);
     	
    	/************************************************************************************
    	 * 등급 가져오기
    	 ************************************************************************************/
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
    	
    	/************************************************************************************
    	 * 난이도등급 조회
    	 ************************************************************************************/
    	searchMap.put("firstGradeId", getStr("bsc.level.adjustRate.getFirstGradeId", searchMap));
    	searchMap.put("secondGradeId", getStr("bsc.level.adjustRate.getSecondGradeId", searchMap));
    	
    	/************************************************************************************
    	 * 난이도등급명 조회
    	 ************************************************************************************/
    	searchMap.addList("secondGradeNm", getStr("bsc.level.adjustRate.getSecondGradeNm", searchMap));
        
        return searchMap;
    }
    
    /**
     * 난이도 조정계수 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap adjustRateProcess(SearchMap searchMap) {
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
     * 난이도 조정계수 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        String[] adjustRates = searchMap.getStringArray("adjustRates");
	        String[] matchGrades = searchMap.getStringArray("matchGrades");
	        
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.level.adjustRate.deleteGradeData", searchMap, true);
	        
	        if(null != adjustRates && 0 < adjustRates.length) {
		        for (int i = 0; i < adjustRates.length; i++) {
		            searchMap.put("adjustRate", adjustRates[i]);
		            searchMap.put("matchGrade", matchGrades[i]);
		            returnMap = updateData("bsc.level.adjustRate.insertData", searchMap);
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
     * 난이도 조정계수 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] itemGrpIds = searchMap.getString("itemGrpIds").split("\\|", 0);
			String[] grades = searchMap.getString("grades").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != itemGrpIds && 0 < itemGrpIds.length) {
		        for (int i = 0; i < itemGrpIds.length; i++) {
		            searchMap.put("itemGrpId", itemGrpIds[i]);
		            searchMap.put("grade", grades[i]);
		            returnMap = updateData("bsc.level.adjustRate.deleteData", searchMap);
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
     * 난이도 조정계수 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap adjustRateListExcel(SearchMap searchMap) {
    	String excelFileName = "난이도 조정계수";
    	String excelTitle = "난이도 조정계수 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("평가항목그룹명", "ITEM_GRP_NM", "left", "GROUP_CNT"));
    	excelInfoList.add(new ExcelVO("평가등급", "GRADE", "left"));
    	
    	ArrayList gradeList = (ArrayList)getList("bsc.level.adjustRate.getGradeList", searchMap);
     	
    	/************************************************************************************
    	 * 등급 가져오기
    	 ************************************************************************************/
    	String[] gradeArr = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeArr = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeArr[i] = (String)map.get("GRADE"); 
    			
    			excelInfoList.add(new ExcelVO((String)map.get("GRADE"), (String)map.get("GRADE"), "left"));
    		}
    	}
    	
    	searchMap.put("gradeArr", gradeArr);
    	searchMap.addList("gradeList", gradeList);
    	
    	/************************************************************************************
    	 * 난이도등급 조회
    	 ************************************************************************************/
    	searchMap.put("firstGradeId", getStr("bsc.level.adjustRate.getFirstGradeId", searchMap));
    	searchMap.put("secondGradeId", getStr("bsc.level.adjustRate.getSecondGradeId", searchMap));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.adjustRate.getList", searchMap));
    	
        return searchMap;
    }
    
}

/*************************************************************************
* CLASS 명      : ImponAdjustAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 5일 
* 기    능      : 가감점반영
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 5일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.impon;
    
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

public class ImponAdjustAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 가감점반영 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponAdjustList(SearchMap searchMap) {

    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponItem.getDegreeList", searchMap));
    	
    	/**********************************
         * 평가대상지표 조회
         **********************************/
    	searchMap.addList("evalPoolList", getList("bsc.impon.imponItem.getPoolList", searchMap));	
    	
    	/**********************************
         * 대상코드 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.impon.imponAdjust.getItemList", searchMap));
    	
    	searchMap.addList("firstCodeId", (String)searchMap.getDefaultValue("itemList", "COL_NM", 0));
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}   
    	
    	searchMap.addList("isEvalCloseYn", getStr("bsc.impon.imponEvalStatus.isEvalCloseYn", searchMap));
    	searchMap.addList("isEvalConfirmYn", getStr("bsc.impon.imponEvalStatus.isEvalConfirmYn", searchMap));

        return searchMap;
    }
    
    /**
     * 가감점반영 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponAdjustList_xml(SearchMap searchMap) {
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.impon.imponAdjust.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("CODE_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 대상코드 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.impon.imponAdjust.getItemList", searchMap));
        
        searchMap.addList("list", getList("bsc.impon.imponAdjust.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 가감점반영 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponAdjustModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.imponAdjust.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 가감점반영 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponAdjustProcess(SearchMap searchMap) {
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
        } else if("EVALREFL".equals(stMode)) {
            searchMap = evalResultReflDB(searchMap);
        } else if("EVALCANCEL".equals(stMode)) {
            searchMap = evalConfirmCancelDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 가감점반영 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] evalDegreeIds 	= searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] metricGrpIds 	= searchMap.getString("metricGrpIds").split("\\|", 0);
			String[] metricIds 		= searchMap.getString("metricIds").split("\\|", 0);
			String[] adjustScores 	= searchMap.getString("adjustScores").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("metricGrpId", metricGrpIds[i]);
		            searchMap.put("metricId", metricIds[i]);
		            searchMap.put("adjustScore", adjustScores[i]);
		            searchMap.put("adjustScore1", adjustScores[i].equals("")?"0":adjustScores[i]);
		            returnMap = updateData("bsc.impon.imponAdjust.updateData", searchMap);
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
     * 가감점반영 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.impon.imponAdjust.updateData", searchMap);
	        
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
    
    
    public SearchMap evalResultReflDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("imponEvalConfirmYn", "Y");
	        returnMap = updateData("bsc.impon.imponAdjust.setEvalConfirmData", searchMap, true);
	        
        	returnMap = insertData("bsc.impon.imponAdjust.execEvalResultRefl", searchMap);
	        
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
    
    public SearchMap evalConfirmCancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("imponEvalConfirmYn", "N");
	        returnMap = updateData("bsc.impon.imponAdjust.setEvalConfirmData", searchMap);
	        
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
     * 가감점반영 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("metricGrpId", metricGrpIds[i]);
			searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("bsc.impon.imponAdjust.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalMetricScore"), "평가지표점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("adjustScore"), "가감점");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("finalMetricScore"), "최종지표점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 가감점반영 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponAdjustListExcel(SearchMap searchMap) {
    	String excelFileName = "가감점반영";
    	String excelTitle = "가감점반영 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가대상지표 POOL", "METRIC_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가대상지표", "METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("KPI ID", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI 명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직 코드", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("성과조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가단점수(사내)", "CODE_01", "left"));
    	excelInfoList.add(new ExcelVO("평가단점수(사외)", "CODE_02", "left"));
    	excelInfoList.add(new ExcelVO("평가단점수(사장)", "CODE_03", "left"));
    	excelInfoList.add(new ExcelVO("평가단점수(본부장)", "CODE_04", "left"));
    	excelInfoList.add(new ExcelVO("평가지표점수", "EVAL_METRIC_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("가감점", "ADJUST_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("최종지표점수", "FINAL_METRIC_SCORE", "left"));
    	
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.impon.imponAdjust.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("CODE_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 대상코드 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.impon.imponAdjust.getItemList", searchMap));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponAdjust.getList", searchMap));
    	
        return searchMap;
    }
    
}

/*************************************************************************
* CLASS 명      : ResultMonAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 8일 
* 기    능      : 기관장지표결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 8일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.bos;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ResultMonAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 기관장지표결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultMonList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 기관장지표결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultMonList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.bos.resultMon.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 기관장지표결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultMonModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.bos.resultMon.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 기관장지표결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultMonProcess(SearchMap searchMap) {
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
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 기관장지표결과 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.bos.resultMon.insertData", searchMap);
        
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
     * 기관장지표결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        String[] bossMetricIds = searchMap.getString("bossMetricIds").split("\\|", 0);
	        String[] bossEvalScores = searchMap.getString("bossEvalScores").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != bossEvalScores && 0 < bossEvalScores.length) {
		        for (int i = 0; i < bossMetricIds.length; i++) {
		            searchMap.put("bossMetricId", bossMetricIds[i]);
		            searchMap.put("bossEvalScore", bossEvalScores[i]);
		            returnMap = updateData("gov.bos.resultMon.updateData", searchMap);
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
     * 기관장지표결과 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] bossMetricIds = searchMap.getString("bossMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != bossMetricIds && 0 < bossMetricIds.length) {
		        for (int i = 0; i < bossMetricIds.length; i++) {
		            searchMap.put("bossMetricId", bossMetricIds[i]);
		            returnMap = updateData("gov.bos.resultMon.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("bossEvalScore"), "기관장평가점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 기관장지표결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap resultMonListExcel(SearchMap searchMap) {
    	String excelFileName = "기관장지표결과";
    	String excelTitle = "기관장지표결과 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가지표", "BOSS_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("기관장가중치", "BOSS_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("기관장득점", "BOSS_EVAL_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("기관가중치", "GOV_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("기관득점", "GOV_SCORE", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.bos.resultMon.getList", searchMap));
    	
        return searchMap;
    }
    
}

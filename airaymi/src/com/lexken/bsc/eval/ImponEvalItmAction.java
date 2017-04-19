/*************************************************************************
* CLASS 명      : ImponEvalItmAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 24일 
* 기    능      : 비계량평가항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 24일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.eval;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class ImponEvalItmAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량평가항목관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalItmList(SearchMap searchMap) {
    	
    	String findCycle = (String)searchMap.get("findCycle");
    	searchMap.addList("evalCycle", getList("bsc.eval.imponEvalGrp.getEvalCycle", searchMap));
    	
    	//디폴트 조회조건 설정(findCycle)
    	if("".equals(StaticUtil.nullToBlank(findCycle))) {
    		searchMap.put("findCycle", searchMap.getDefaultValue("evalCycle", "CODE_ID", 0));
    	}

    	searchMap.addList("itmList", getList("bsc.eval.imponEvalItm.getItmList", searchMap));
    	searchMap.addList("metricGrpList", getList("bsc.eval.imponEvalGrp.getMetricGrpList", searchMap));
    	searchMap.addList("evalable", getDetail("bsc.eval.imponEvalGrp.getEvalable", searchMap));
    	
        return searchMap;
    }

    /**
     * 비계량평가항목관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponEvalItmExcel(SearchMap searchMap) {
    	String excelFileName = "비계량평가항목관리";
    	String excelTitle = "비계량평가항목관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("cycleNm")));
    	excelSearchInfoList.add(new ExcelVO("사용유무", (String)searchMap.get("useYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가항목명", "ITEM_NM", "center"));
    	
    	excelInfoList.add(new ExcelVO("S", "S", "center"));
    	excelInfoList.add(new ExcelVO("B", "B", "center"));
    	excelInfoList.add(new ExcelVO("C", "C", "center"));
    	excelInfoList.add(new ExcelVO("D", "D", "center"));
    	excelInfoList.add(new ExcelVO("E", "E", "center"));
    	excelInfoList.add(new ExcelVO("F", "F", "center"));
    	excelInfoList.add(new ExcelVO("G", "G", "center"));
    	
    	excelInfoList.add(new ExcelVO("가중치", "ITEM_WEIGHT", "center"));
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.eval.imponEvalItm.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 비계량평가항목관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalItmList_xml(SearchMap searchMap) {
        
    	ArrayList codeList = (ArrayList)getList("bsc.eval.imponEvalItm.getItmList", searchMap);
    	 
    	String[] itmList = new String[0]; 
    	if(null != codeList && 0 < codeList.size()) {
    		itmList = new String[codeList.size()];
    		for(int i=0; i<codeList.size(); i++) {
    			HashMap map = (HashMap)codeList.get(i);
    			itmList[i] = (String)map.get("CODE_ID"); 
    		}
    	}
    	
    	searchMap.put("itmList", itmList);
    	searchMap.addList("itmList", codeList);
    	
        searchMap.addList("list", getList("bsc.eval.imponEvalItm.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량평가항목관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalItmModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("ADD".equals(stMode) || "MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.eval.imponEvalItm.getDetail", searchMap));
    		searchMap.addList("detailList", getList("bsc.eval.imponEvalItm.getDetailList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 비계량평가항목관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalItmProcess(SearchMap searchMap) {
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
        if("ADD".equals(stMode) || "WEIGHT".equals(stMode)) {
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
     * 비계량평가항목관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	if("ADD".equals(searchMap.get("mode"))){//평가항목 등록/수정
        		String[] grades = searchMap.getStringArray("grades");
        		String[] contents = searchMap.getStringArray("contents");
        		String itemCd = getStr("bsc.eval.imponEvalItm.getItemCd", searchMap);
        		
        		searchMap.put("itemCd", itemCd);
        		
        		returnMap = updateData("bsc.eval.imponEvalItm.insertData", searchMap);
        		if(grades!=null && grades.length>0){
        			for(int i=0; i<grades.length; i++){
        				searchMap.put("grade", grades[i]);
        				searchMap.put("content", contents[i]);
        				returnMap = insertData("bsc.eval.imponEvalItm.insertDetailData", searchMap);
        				
        			}
        		}
        		
        	} else if("WEIGHT".equals(searchMap.get("mode"))){//가중치 등록/수정
            	String[] itemCds = searchMap.getStringArray("itemCds");
            	String[] itemWeights = searchMap.getStringArray("itemWeights");
            	if(itemCds!=null && itemCds.length>0){
            		for (int i = 0; i < itemCds.length; i++) {
            			searchMap.put("itemCd", itemCds[i]);
            			searchMap.put("itemWeight", itemWeights[i]);
            			returnMap = insertData("bsc.eval.imponEvalItm.updateWeights", searchMap);
            		}
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
     * 비계량평가항목관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
        	String[] grades = searchMap.getStringArray("grades");
        	String[] contents = searchMap.getStringArray("contents");
        	
        	returnMap = updateData("bsc.eval.imponEvalItm.updateData", searchMap);
        	if(grades!=null && grades.length>0){
        		for(int i=0; i<grades.length; i++){
        			searchMap.put("grade", grades[i]);
        			searchMap.put("content", contents[i]);
        			returnMap = updateData("bsc.eval.imponEvalItm.updateDetailData", searchMap);
        			
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
     * 비계량평가항목관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    try {
	        String[] itemCds = searchMap.getString("itemCd").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < itemCds.length; i++) {
	        	searchMap.put("itemCd", itemCds[i]);
	            returnMap = updateData("bsc.eval.imponEvalItm.deleteData", searchMap);
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
    
    
}

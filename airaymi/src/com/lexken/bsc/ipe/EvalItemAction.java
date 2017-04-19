/*************************************************************************
* CLASS 명      : ImponItemAction
* 작 업 자      : 박경태
* 작 업 일      : 2013년 08월 08일
* 기    능      : 평가항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2013년 08월 08일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.ipe;
    
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

public class EvalItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalItemList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.ipe.evalItem.getDegreeList", searchMap));
    	
    	/**********************************
         * 평가대상지표 조회
         **********************************/
    	searchMap.addList("evalPoolList", getList("bsc.ipe.evalItem.getPoolList", searchMap));
    	
    	
    	if("".equals(StaticUtil.nullToDefault((String)searchMap.get("findEvalDegreeId"),""))){
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	if("".equals(StaticUtil.nullToDefault((String)searchMap.get("findMetricGrpId"),""))){
    		searchMap.put("findMetricGrpId", (String)searchMap.getDefaultValue("evalPoolList", "METRIC_GRP_ID", 0));
    	}
    	
    	/**********************************
         * 대상부서 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.ipe.evalItem.getItemList", searchMap));
    	
    	searchMap.addList("firstMetricId", (String)searchMap.getDefaultValue("itemList", "COL_NM", 0));
    	
    	/**********************************
         * 가중치 합계 가져오기 : 가중치 valueChk시에 활성화 필요
         **********************************/
    	//searchMap.put("weightSum", getDetail("bsc.ipe.evalItem.getWeightSum", searchMap));
        
    	return searchMap;
    }
    
    /**
     * 평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalItemList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.ipe.evalItem.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("METRIC_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 대상부서 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.ipe.evalItem.getItemList", searchMap));
    	
    	searchMap.addList("firstMetricId", (String)searchMap.getDefaultValue("itemList", "COL_NM", 0));

        searchMap.addList("list", getList("bsc.ipe.evalItem.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getDegreeList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.ipe.evalItem.getDegreeList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 비계량지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getPoolList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 비계량지표 조회
         **********************************/
    	searchMap.addList("evalPoolList", getList("bsc.ipe.evalItem.getPoolList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	/**********************************
         * 가중치 합계 가져오기
         **********************************/
    	if("MOD".equals(stMode)) {
    		searchMap.put("weightSum", getInt("bsc.ipe.evalItem.getWeightSum", searchMap));
    	}
    	/**********************************
         * 지표POOL명, 구분명 가져오기
         **********************************/
    	searchMap.addList("detailFindList", getDetail("bsc.ipe.evalItem.getFindList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.ipe.evalItem.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalItemProcess(SearchMap searchMap) {
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
        
        String deleteDt = searchMap.getString("deleteDt");
        
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
     * 평가항목 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] metricIds 	= searchMap.getStringArray("metricIds");
        	String[] weights 	= searchMap.getStringArray("weights");
        	
        	setStartTransaction();
        	
        	/**********************************
	         * 평가항목 코드 채번 
	         **********************************/
	        String itemId = getStr("bsc.ipe.evalItem.getItemId", searchMap);
	        searchMap.put("itemId", itemId);
        	
        	returnMap = insertData("bsc.ipe.evalItem.insertData", searchMap);
        	
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
     * 평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	
        	setStartTransaction();
        	
        	returnMap = insertData("bsc.ipe.evalItem.updateData", searchMap);
        	
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
     * 평가항목 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
			String[] itemIds = searchMap.getString("itemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("metricGrpId", metricGrpIds[i]);
		            searchMap.put("itemId", itemIds[i]);
		            returnMap = updateData("bsc.ipe.evalItem.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "SORT_ORDER");
		
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가항목 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalItemListExcel(SearchMap searchMap) {
    	String excelFileName = "평가항목";
    	String excelTitle = "평가항목 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("평가대상지표POOL", (String)searchMap.get("metricGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("비계량 항목", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "left"));
    	
    	
    	//searchMap.put("findEvalDegreeId", searchMap.get("evalDegreeIds"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ipe.evalItem.getList", searchMap));
    	
        return searchMap;
    }
    
}

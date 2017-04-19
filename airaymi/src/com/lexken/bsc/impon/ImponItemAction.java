/*************************************************************************
* CLASS 명      : ImponItemAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 30일 
* 기    능      : 평가항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 30일         최 초 작 업 
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

public class ImponItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponItemList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponItem.getDegreeList", searchMap));
    	
    	/**********************************
         * 평가대상지표 조회
         **********************************/
    	searchMap.addList("evalPoolList", getList("bsc.impon.imponItem.getPoolList", searchMap));
    	
    	
    	if("".equals(StaticUtil.nullToDefault((String)searchMap.get("findMetricGrpId"),""))){
    		searchMap.put("findMetricGrpId", (String)searchMap.getDefaultValue("evalPoolList", "METRIC_GRP_ID", 0));
    	}
    	/**********************************
         * 대상부서 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.impon.imponItem.getItemList", searchMap));
    	
    	searchMap.addList("firstMetricId", (String)searchMap.getDefaultValue("itemList", "COL_NM", 0));

        return searchMap;
    }
    
    /**
     * 평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponItemList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.impon.imponItem.getItemList", searchMap);
    	 
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
    	searchMap.addList("itemList", getList("bsc.impon.imponItem.getItemList", searchMap));
    	
    	searchMap.addList("firstMetricId", (String)searchMap.getDefaultValue("itemList", "COL_NM", 0));

        searchMap.addList("list", getList("bsc.impon.imponItem.getList", searchMap));

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
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponItem.getDegreeList", searchMap));

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
    	searchMap.addList("evalPoolList", getList("bsc.impon.imponItem.getPoolList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	/**********************************
         * 대상부서 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.impon.imponItem.getItemWeightList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.imponItem.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponItemProcess(SearchMap searchMap) {
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
	        String itemId = getStr("bsc.impon.imponItem.getItemId", searchMap);
	        searchMap.put("itemId", itemId);
        
        	returnMap = insertData("bsc.impon.imponItem.insertData", searchMap);
        	
        	/**********************************
	         * 평가항목 가중치 삭제 & 등록
	         **********************************/
	        returnMap = updateData("bsc.impon.imponItem.deleteWeightData", searchMap, true);
	        
        	if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            searchMap.put("weight", weights[i]);
		            returnMap = updateData("bsc.impon.imponItem.insertWeightData", searchMap);
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
     * 평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] metricIds 	= searchMap.getStringArray("metricIds");
        	String[] weights 	= searchMap.getStringArray("weights");
        	
        	setStartTransaction();
        	
        	returnMap = insertData("bsc.impon.imponItem.updateData", searchMap);
        	
        	/**********************************
	         * 평가항목 가중치 삭제 & 등록
	         **********************************/
	        returnMap = updateData("bsc.impon.imponItem.deleteWeightData", searchMap, true);
	        
        	if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            searchMap.put("weight", weights[i]);
		            returnMap = updateData("bsc.impon.imponItem.insertWeightData", searchMap);
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
		            returnMap = updateData("bsc.impon.imponItem.deleteData", searchMap);
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
    public SearchMap imponItemListExcel(SearchMap searchMap) {
    	String excelFileName = "평가항목";
    	String excelTitle = "평가항목 리스트";
    	
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
    	
    	
    	excelInfoList.add(new ExcelVO("평가항목구분", "ITEM_GBN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가항목", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적보고서담당자", "ACT_RPT_INSERT_USER_NM", "left"));
    	
    	//평가관리 - 비계량평가 - 평가항목의 컬럼은 평가대상자지표POOL에 따라서 가변되므로
    	//컬럼을 가변시켜주고 해당 값을 조회하는 getItemList에서 값과 컬럼이름을 얻어옵니다.
    	ArrayList itemList = (ArrayList)getList("bsc.impon.imponItem.getItemList", searchMap);
   	 	
    	String[] itemArray = new String[0]; 
    	String[] itemNm = new String[0];
    	
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		itemNm = new String[itemList.size()];
    		
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemNm[i] = (String)map.get("SC_DEPT_NM");
    			itemArray[i] = (String)map.get("METRIC_ID"); 
    			
    			excelInfoList.add(new ExcelVO(itemNm[i], "DEPT_" + itemArray[i], "left"));
    		}
    		
    	}
    	
    	excelInfoList.add(new ExcelVO("계", "SUM_WEIGHT", "left"));
    	
    	/*
    	//기존 코드
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_GBN_NM", "ITEM_GBN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI POOL I", "METRIC_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 코드", "ITEM_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 명", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("ACT_RPT_INSERT_USER_NM", "ACT_RPT_INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0001", "ITEM_DEP0001", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0002", "ITEM_DEP0002", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0003", "ITEM_DEP0003", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0004", "ITEM_DEP0004", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0005", "ITEM_DEP0005", "left"));
    	excelInfoList.add(new ExcelVO("ITEM_DEP0006", "ITEM_DEP0006", "left"));

    	ArrayList itemList = (ArrayList)getList("bsc.impon.imponItem.getItemList", searchMap);
    	
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("METRIC_ID"); 
    		}
    	}
    	
    	*/
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	
    	
    	searchMap.put("itemArray", itemArray);
    	searchMap.put("findEvalDegreeId", searchMap.get("evalDegreeId"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponItem.getList", searchMap));
    	
        return searchMap;
    }
    
}

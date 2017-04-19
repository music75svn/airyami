/*************************************************************************
* CLASS 명      : EvalUserGrpMappingEvalItemAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 30일 
* 기    능      : 평가단별 지표/항목 매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 30일         최 초 작 업 
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

public class EvalUserGrpMappingEvalItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가단별 지표/항목 매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemList(SearchMap searchMap) {
    	
    	/**********************************
         * 디폴트 조회조건 설정(year)
         **********************************/
    	
    	searchMap.addList("evalDegreeList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalDegreeList", searchMap));
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalUserGrpList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList_ajax(SearchMap searchMap) {
    	searchMap.addList("evalDegreeList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalDegreeList", searchMap));
        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpList_ajax(SearchMap searchMap) {
    	searchMap.addList("evalUserGrpList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalUserGrpList", searchMap));
        return searchMap;
    }
    
    
    /**
     * 평가단별 지표/항목 매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.impon.evalUserGrpMappingEvalItem.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단별 지표/항목 매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MODMAPPING".equals(stMode)) {
    		String evalTarget = searchMap.getString("evalTarget");
    		if( "".equals(evalTarget) ){
    			searchMap.put("evalTarget", "01");
    		}
    		searchMap.addList("existDifferentMappingYn", getStr("bsc.impon.evalUserGrpMappingEvalItem.existDifferentMappingList", searchMap));
    	}
        
        return searchMap;
    }
    
    
    /**
     * 평가단별 지표/항목 매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.impon.evalUserGrpMappingEvalItem.getMappingList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가단별 지표/항목 매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemMethodModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	// 이전차수반영
    	searchMap.addList("oldDegreelist", getList("bsc.impon.evalUserGrpMappingEvalItem.getOldDegreeReflList", searchMap));
    	// 평가방법
    	searchMap.addList("evalMethodlist", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalMethodList", searchMap));
        
    	if("MODMETHOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.evalUserGrpMappingEvalItem.getMethodDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    
    /**
     * 평가단별 지표/항목 매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrpMappingEvalItemProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MODMAPPING".equals(stMode)) {
            searchMap = updateMappingDB(searchMap);
        } else if("MODMETHOD".equals(stMode)) {
            searchMap = updateMethodDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    
    /**
     * 평가단별 지표/항목 매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateMappingDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.impon.evalUserGrpMappingEvalItem.deleteMappingData", searchMap, true);
	        
	        
	        String gbn = searchMap.getString("itemGbns");
	        int idx = gbn.indexOf("|", 0);
	        
	        String[] itemGbns = searchMap.getString("itemGbns").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
			String[] itemIds = searchMap.getString("itemIds").split("\\|", 0);
	        
	        if(null != itemGbns && 0 < itemGbns.length && -1 < idx) {
		        for (int i = 0; i < itemGbns.length; i++) {
		            searchMap.put("evalTarget", itemGbns[i]);
					searchMap.put("metricId", metricIds[i]);
					searchMap.put("itemId", itemIds[i]);
					returnMap = insertData("bsc.impon.evalUserGrpMappingEvalItem.insertMappingData", searchMap);
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
     * 평가단별 지표/항목 매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateMethodDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        returnMap = updateData("bsc.impon.evalUserGrpMappingEvalItem.deleteMethodData", searchMap, true);
	        
	        returnMap = insertData("bsc.impon.evalUserGrpMappingEvalItem.insertMethodData", searchMap);
	        
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가단별 지표/항목 매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalUserGrpMappingEvalItemListExcel(SearchMap searchMap) {
    	String excelFileName = "평가단별 지표_항목 매핑";
    	String excelTitle = "평가단별 지표_항목 매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", StaticUtil.nullToDefault((String)searchMap.get("evalUserGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("매핑여부", (String)searchMap.get("mappingYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "center", "EUG_ROWSPAN_CNT"));
    	excelInfoList.add(new ExcelVO("평가대상지표POOL", "METRIC_GRP_NM", "left", "MG_ROWSPAN_CNT"));
    	excelInfoList.add(new ExcelVO("평가항목", "EVAL_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "center", "MG_ROWSPAN_CNT"));
    	excelInfoList.add(new ExcelVO("이전차수반영", "OLD_DEGREE_REFL_NM", "center", "MG_ROWSPAN_CNT"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.evalUserGrpMappingEvalItem.getList", searchMap));
    	
        return searchMap;
    }
    
}

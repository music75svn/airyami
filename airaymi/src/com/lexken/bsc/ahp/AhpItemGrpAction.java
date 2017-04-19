/*************************************************************************
* CLASS 명      : AhpItemGrpAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 28일 
* 기    능      : AHP 평가대상그룹
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 28일         최 초 작 업 
*   2    김민주      2013년 06월 24일         추 가 작 업 
**************************************************************************/
package com.lexken.bsc.ahp;
    
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

public class AhpItemGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * AHP 평가대상그룹 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.ahp.ahpItemGrp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.ahp.ahpItemGrp.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"BATCH".equals(stMode)) {
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
        } else if("BATCH".equals(stMode)) {
            searchMap = batchDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.ahp.ahpItemGrp.insertData", searchMap);
        
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
     * AHP 평가대상그룹 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.ahp.ahpItemGrp.updateData", searchMap);
	        
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
     * AHP 평가대상그룹 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] itemGrpIds = searchMap.getString("itemGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != itemGrpIds && 0 < itemGrpIds.length) {
		        for (int i = 0; i < itemGrpIds.length; i++) {
		            searchMap.put("itemGrpId", itemGrpIds[i]);
		            returnMap = updateData("bsc.ahp.ahpItemGrp.deleteData", searchMap);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("itemGrpNm"), "평가대상 그룹 명", 1, 100);
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
     * AHP 평가대상그룹 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpItemGrpListExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가대상그룹";
    	String excelTitle = "AHP 평가대상그룹 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가대상 그룹 명", "ITEM_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("평가대상수", "ITEM_CNT", "center"));
    	excelInfoList.add(new ExcelVO("평가대상그룹매핑수", "MAPPING_CNT", "center"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "center"));
    
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpItemGrp.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpItemModify(SearchMap searchMap) {
        
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹 항목 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemGrpItemList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.ahp.ahpItemGrp.getItemList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 평가대상 일괄수정
     * @param      
     * @return String  
     * @throws
     */
    public SearchMap batchDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap(); 
        int cnt = 0;
        /**********************************
         * Parameter setting
         **********************************/
        String[] itemIds = searchMap.getStringArray("itemIds");
        String[] itemNms = searchMap.getStringArray("itemNms");
        String[] contents = searchMap.getStringArray("contents");
        String[] sortOrders = searchMap.getStringArray("sortOrders");
        String[] metricGrpIds = searchMap.getStringArray("metricGrpIds");
        String[] delItemIds = searchMap.getString("delItemIds").split("\\|", 0);
        
        try {
	        setStartTransaction();
	        if(null != itemIds && 0 < itemIds.length) {
	        	for (int i = 0; i < itemIds.length; i++) {
		        	//새로 등록할 평가대상 중복 체크
	        		if("".equals(StaticUtil.nullToBlank(itemIds[i]))) {
		        		searchMap.put("metricGrpId", metricGrpIds[i]);
		        		//등록된 평가대상 개수를 세어오기
		        		cnt = getInt("bsc.ahp.ahpItemGrp.getMetricGrpCount", searchMap);
		        	}
	        		
	        		if(cnt==0){
			        	if(!"".equals(StaticUtil.nullToBlank(itemIds[i]))) { //평가대상 수정
				            searchMap.put("itemId", itemIds[i]);
				            searchMap.put("itemNm", itemNms[i]);
				            searchMap.put("content", contents[i]);
				            searchMap.put("sortOrder", sortOrders[i]);
				            searchMap.put("metricGrpId", metricGrpIds[i]);
				            returnMap = updateData("bsc.ahp.ahpItemGrp.updateBatchItemData", searchMap);
			        	} else if("".equals(StaticUtil.nullToBlank(itemIds[i]))) { //평가대상 등록
		        			searchMap.put("itemNm", itemNms[i]);
		        			searchMap.put("content", contents[i]);
		        			searchMap.put("sortOrder", sortOrders[i]);
		        			searchMap.put("metricGrpId", metricGrpIds[i]);
		        			returnMap = insertData("bsc.ahp.ahpItemGrp.insertBatchItemData", searchMap);
			        	}
	        		}
		        }
	        }
        
	        /**********************************
	         * 평가대상 삭제
	         **********************************/
	        if(null != delItemIds && 0 < delItemIds.length) {
		        for (int i = 0; i < delItemIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(delItemIds[i]))) {
		        		searchMap.put("itemId", delItemIds[i]);
		            	returnMap = updateData("bsc.ahp.ahpItemGrp.deleteBatchItemData", searchMap, true);
		        	}
		        }
	        }
	        
	        /**********************************
	         * 평가표 생성
	         **********************************/
	        returnMap = updateData("bsc.ahp.ahpItemGrp.deleteItemTabData", searchMap, true);
	        returnMap = insertData("bsc.ahp.ahpItemGrp.execInsertItemTabData", searchMap);
	        
	        //신규로 등록할 평가대상이 이미 등록되어있을 경우 에러 메시지 
	        if(cnt > 0) {
	        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
    			returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "평가대상"));
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

}

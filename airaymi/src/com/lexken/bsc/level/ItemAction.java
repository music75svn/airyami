/*************************************************************************
* CLASS 명      : ItemAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 평가항목 관리
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

public class ItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가항목 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap itemList(SearchMap searchMap) {
    	/**********************************
         * 난이도평가마감구분 마감'Y' 
         * 한개의 평가단이라도 마감이 되어있으면 임계치 수정 불가
         **********************************/
	    searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
	    
	    /**********************************
         * 난이도평가제출구분 제출'Y'
         **********************************/
	    searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
	    
	    if("".equals(StaticUtil.nullToBlank((String)searchMap.get("itemGrpId")))) {
			searchMap.put("itemGrpId", (String)searchMap.get("findItemGrpId"));
		}
	    
	    if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findItemGrpId")))) {
			searchMap.put("findItemGrpId", (String)searchMap.get("itemGrpId"));
		}

	    searchMap.addList("itemGrpDetail", getDetail("bsc.level.itemGrp.getDetail", searchMap));
	    
        return searchMap;
    }
    
    /**
     * 평가항목 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap itemList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.level.item.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가항목 관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap itemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("itemGrpList", getList("bsc.level.item.getItemGrpList", searchMap));
    	
    	searchMap.addList("itemGrpDetail", getDetail("bsc.level.itemGrp.getDetail", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.level.item.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가항목 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap itemProcess(SearchMap searchMap) {
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
     * 평가항목 관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.level.item.insertData", searchMap);
        
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
     * 평가항목 관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.level.item.updateData", searchMap);
	        
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
     * 평가항목 관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] itemIds = searchMap.getString("itemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != itemIds && 0 < itemIds.length) {
		        for (int i = 0; i < itemIds.length; i++) {
		            searchMap.put("itemId", itemIds[i]);
		            returnMap = updateData("bsc.level.item.deleteData", searchMap);
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
     * 평가항목 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap itemListExcel(SearchMap searchMap) {
    	String excelFileName = "평가항목 관리";
    	String excelTitle = "평가항목 관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가항목 그룹명", (String)searchMap.get("itemGrpNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가항목명", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.item.getList", searchMap));
    	
        return searchMap;
    }
    
}

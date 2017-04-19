/*************************************************************************
* CLASS 명      : AhpItemMappingAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 28일 
* 기    능      : AHP 평가대상그룹매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 28일         최 초 작 업 
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

public class AhpItemMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * AHP 평가대상그룹매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemMappingList(SearchMap searchMap) {
    	String findEvalUserGrpId = searchMap.getString("findEvalUserGrpId");
    	String evalUserGrpId = searchMap.getString("evalUserGrpId");
    	
    	if("".equals(findEvalUserGrpId)) {
    		searchMap.put("findEvalUserGrpId", evalUserGrpId);
    	}
    	
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.ahp.ahpItemMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemMappingModify(SearchMap searchMap) {
    	
    	/************************************************************************************
    	 * 평가단명 조회
    	 ************************************************************************************/
    	searchMap.addList("evalUserGrpNm", getStr("bsc.ahp.ahpItemMapping.getEvalUserGrpNm", searchMap));

    	searchMap.addList("totList", getList("bsc.ahp.ahpItemMapping.getTotList", searchMap));
    	
    	searchMap.addList("selList", getList("bsc.ahp.ahpItemMapping.getSelList", searchMap));
        
        return searchMap;
    }
    
    /**
     * AHP 평가대상그룹매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpItemMappingProcess(SearchMap searchMap) {
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
     * AHP 평가대상그룹매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        String[] itemGrpIds = searchMap.getString("itemGrpIds").split("\\|", 0);
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.ahp.ahpItemMapping.deleteData", searchMap, true);
	        
	        if(null != itemGrpIds && 0 < itemGrpIds.length) {
		        for (int i = 0; i < itemGrpIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(itemGrpIds[i]))) {
			        	searchMap.put("itemGrpId", itemGrpIds[i]);
			        	returnMap = insertData("bsc.ahp.ahpItemMapping.insertData", searchMap);
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
     * AHP 평가대상그룹매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpItemMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가대상그룹매핑";
    	String excelTitle = "AHP 평가대상그룹매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가단 명", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 그룹 명", "ITEM_GRP_NM", "left"));
    	//excelInfoList.add(new ExcelVO("총가중치", "TOT_WEIGHT", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpItemMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}

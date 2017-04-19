/*************************************************************************
* CLASS 명      : OfficeAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 5일 
* 기    능      : 공공기관
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 5일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.office;
    
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

public class OfficeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공공기관 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeList(SearchMap searchMap) {
    	
    	searchMap.addList("officeGrplist", getList("gov.office.office.getOfficeGrpList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 공공기관 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.office.office.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 공공기관 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeGrpList_ajax(SearchMap searchMap) {

    	searchMap.addList("list", getList("gov.office.office.getOfficeGrpList", searchMap));

        return searchMap;
    }
    
    
    
    
    /**
     * 공공기관 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("officeGrplist", getList("gov.office.office.getOfficeGrpList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.office.office.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 공공기관 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeProcess(SearchMap searchMap) {
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
     * 공공기관 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.office.office.insertData", searchMap);
        
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
     * 공공기관 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.office.office.updateData", searchMap);
	        
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
     * 공공기관 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] officeIds = searchMap.getString("officeIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != officeIds && 0 < officeIds.length) {
		        for (int i = 0; i < officeIds.length; i++) {
		            searchMap.put("officeId", officeIds[i]);
		            returnMap = updateData("gov.office.office.deleteData", searchMap);
		            returnMap = updateData("gov.office.officeResult.deleteAllAttachFile", searchMap, true);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("officeNm"), "공공기관 기관명", 1, 50);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.selEmptyCheck(searchMap.getString("officeEvalGrpId"), "공공기관 평가군코드");
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
     * 공공기관 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap officeListExcel(SearchMap searchMap) {
    	String excelFileName = "공공기관";
    	String excelTitle = "공공기관 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공공기관 평가군", StaticUtil.nullToDefault((String)searchMap.get("officeEvalGrpNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("공기업평가군명", "OFFICE_EVAL_GRP_NM", "left", "OG_CNT"));
    	excelInfoList.add(new ExcelVO("기관코드", "OFFICE_ID", "center"));
    	excelInfoList.add(new ExcelVO("기관명", "OFFICE_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.office.office.getList", searchMap));
    	
        return searchMap;
    }
    
}

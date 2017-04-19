/*************************************************************************
* CLASS 명		: CsfAction
* 작 업 자		: 안요한
* 작 업 일		: 2013년 07월 18일 
* 기    능			: CSF관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안 요 한      2013년 07월 18일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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

public class CsfAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * CSF관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList(SearchMap searchMap) {
    	
    	//searchMap.addList("managementList", getList("bsc.base.csf.getManagement", searchMap));
    	
    	//searchMap.addList("strStratSubjectList", getList("bsc.base.csf.getStrStratSubject", searchMap));
    	
        return searchMap;
    }
    
    /**
     * CSF관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.csf.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("managementList", getList("bsc.base.csf.getManagement", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strStratSubjectList_ajax(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findManagementId")))) {
			searchMap.put("findManagementId", (String)searchMap.get("managementId"));
		}
    	
    	searchMap.addList("strStratSubjectList", getList("bsc.base.csf.getStrStratSubject", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * CSF관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	/**********************************
         * 경영목표, 전략과제 조회
         **********************************/
    	
    	searchMap.addList("managementList", getList("bsc.base.csf.getManagement", searchMap));
    	
    	searchMap.addList("strStratSubjectList1", getList("bsc.base.csf.getStrStratSubject1", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.csf.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * CSF관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfProcess(SearchMap searchMap) {
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
     * CSF관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.base.csf.insertData", searchMap);
        
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
     * CSF관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.csf.updateData", searchMap);
	        
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
     * CSF관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] csfIds = searchMap.getString("csfIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != csfIds && 0 < csfIds.length) {
		        for (int i = 0; i < csfIds.length; i++) {
		            searchMap.put("csfId", csfIds[i]);
		            returnMap = updateData("bsc.base.csf.deleteData", searchMap);
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
        
        /*returnMap = ValidationChk.selEmptyCheck(searchMap.getString("managementId"), "경영목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.selEmptyCheck(searchMap.getString("strStratSubjectId"), "전략과제");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}*/
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("csfNm"), "CSF 명", 1, 150);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.onlyNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * CSF관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap csfListExcel(SearchMap searchMap) {
    	String excelFileName = "CSF관리";
    	String excelTitle = "CSF관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("CSF코드", "CSF_ID", "left"));
    	excelInfoList.add(new ExcelVO("CSF", "CSF_NM", "left","",10000));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.csf.getList", searchMap));
    	
        return searchMap;
    }
    
}

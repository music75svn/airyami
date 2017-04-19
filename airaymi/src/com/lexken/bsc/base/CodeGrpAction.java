/*************************************************************************
* CLASS 명      : CodeGrpAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 5월 17일 
* 기    능      : 공통코드관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 5월 17일            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.codeUtil.CodeUtilReLoad;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;

public class CodeGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공통코드 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeGrpList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 공통코드 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeGrpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.codeGrp.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 공통코드 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap codeGrpListExcel(SearchMap searchMap) {
    	String excelFileName = "공통코드";
    	String excelTitle = "공통코드 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("코드그룹명", "CODE_GRP_NM", "left", "CNT1"));
    	excelInfoList.add(new ExcelVO("코드부여구분", "CODE_DEF_NM", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("연도별관리여부", "YEAR_YN", "center", "CNT1"));
    	excelInfoList.add(new ExcelVO("연도", "YEAR", "center", "CNT2"));
    	excelInfoList.add(new ExcelVO("코드", "CODE_ID", "center"));
    	excelInfoList.add(new ExcelVO("코드명", "CODE_NM", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.codeGrp.getExcelList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 공통코드 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.codeGrp.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 공통코드 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeGrpProcess(SearchMap searchMap) {
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
        } else if("MOD2".equals(stMode)) { //수정시 연도별 관리로 변경시 각 연도별로 코드가 자동생성
            searchMap = updateDB2(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /*****************************************
		 * 세션에 등록되어 있는 코드 정보 reflash
		 *****************************************/
		CodeUtilReLoad codeUtilReLoad = new CodeUtilReLoad();
		codeUtilReLoad.codeUtilReLoad();
		
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 공통코드 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("bsc.base.codeGrp.insertData", searchMap);
	        
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
     * 공통코드 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	
	        returnMap = updateData("bsc.base.codeGrp.updateData", searchMap);
	        
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
     * 공통코드 수정2 (연도별 관리로 변경시 각 연도별로 코드가 자동생성)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.codeGrp.updateData", searchMap); //코드그룹 수정
	        
	        returnMap = updateData("bsc.base.codeGrp.deletePreCode", searchMap, true); //기존 코드 삭제
	        
	        returnMap = updateData("bsc.base.codeGrp.insertYearCode", searchMap); //연도별로 코드 배포
	        
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
     * 공통코드 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    

        try {
        	setStartTransaction();
        	
        	String[] codeGrpIds = searchMap.getString("codeGrpIds").split("\\|", 0);
	        
        	if(null != codeGrpIds && 0 < codeGrpIds.length) {
		        for (int i = 0; i < codeGrpIds.length; i++) {
		            searchMap.put("codeGrpId", codeGrpIds[i]);
		            returnMap = updateData("bsc.base.codeGrp.deleteData", searchMap);
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
        
        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("codeDefId"), "코드부여구분");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("codeGrpNm"), "코드그룹", 1, 75);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("yearYn"), "연도별관리여부");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 1000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}

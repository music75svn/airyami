/*************************************************************************
* CLASS 명      : ExcepDeptAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 12일 
* 기    능      : 별도평가조직
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 12일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.excep;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ExcepDeptAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 별도평가조직 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepDeptList(SearchMap searchMap) {
    	
    	String findEvalDegreeId = StaticUtil.nullToDefault(((String)searchMap.get("findEvalDegreeId")), "");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	
    	searchMap.addList("evalDegreeList", getList("cbe.excep.excepDept.getDegreeList", searchMap));	
    	
    	if("".equals(findEvalDegreeId)){
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 마감조회
         **********************************/
    	searchMap.addList("closeYn", getStr("cbe.excep.excepStatus.getFindCloseYn", searchMap));
    	
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
    	searchMap.addList("evalDegreeList", getList("cbe.excep.excepDept.getDegreeList", searchMap));	

        return searchMap;
    }
    
    /**
     * 별도평가조직 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepDeptList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.excep.excepDept.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 별도평가조직 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepDeptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailGubun", getDetail("cbe.excep.excepDept.getDetailGubun", searchMap));
    	
    	// 평가방법
    	searchMap.addList("evalMethodlist", getList("cbe.excep.excepDept.getEvalMethodList", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.excep.excepDept.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 별도평가조직 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepDeptProcess(SearchMap searchMap) {
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
     * 별도평가조직 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.excep.excepDept.insertData", searchMap);
        	
        	returnMap = updateData("cbe.excep.excepDept.deleteAdminData", searchMap, true);//권한삭제
	        
	        returnMap = insertData("cbe.excep.excepDept.insertAdminData", searchMap, true);//권한등록
	        
	        returnMap = insertData("cbe.excep.excepDept.insertAdminBaseData", searchMap);//권한등록
        
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
     * 별도평가조직 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.excep.excepDept.updateData", searchMap);
	        
	        returnMap = updateData("cbe.excep.excepDept.deleteAdminData", searchMap, true);//권한삭제
	        
	        returnMap = insertData("cbe.excep.excepDept.insertAdminData", searchMap, true);//권한등록
	        
	        returnMap = insertData("cbe.excep.excepDept.insertAdminBaseData", searchMap);//권한등록
	        
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
     * 별도평가조직 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.excep.excepDept.deleteData", searchMap);
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
     * 별도평가조직 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap excepDeptListExcel(SearchMap searchMap) {
    	String excelFileName = "별도평가조직";
    	String excelTitle = "별도평가조직 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("별도평가대상조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가근거담당자", "EVAL_BASE_CHARGE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가자", "EVAL_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalDegreeId")))) {
			searchMap.put("findEvalDegreeId", (String)searchMap.get("evalDegreeId"));
		}
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.excep.excepDept.getList", searchMap));
    	
        return searchMap;
    }
    
}

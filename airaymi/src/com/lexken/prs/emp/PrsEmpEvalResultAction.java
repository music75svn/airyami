/*************************************************************************
* CLASS 명      : PrsEmpEvalResultAction
* 작 업 자      : 신인수
* 작 업 일      : 2013년 7월 5일 
* 기    능      : 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    신인수      2013년 7월 5일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.emp;
    
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

public class PrsEmpEvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalResultList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String findNameEmpn = (String)searchMap.get("findNameEmpn");
    	
    	if("findName".equals(findNameEmpn)){
    		searchMap.put("findName", findNameEmpn);
    	}else{
    		searchMap.put("findEmpn", findNameEmpn);
    	}
    	
		searchMap.put("nameEmpn", findNameEmpn);

		
		searchMap.addList("evalSubmitYn", getStr("prs.emp.prsEmpEvalResult.getEvalSubmitYn", searchMap));
		
		searchMap.addList("evalSubmitYnCount", getStr("prs.emp.prsEmpEvalResult.getEvalSubmitYnCount", searchMap)); //평가제출이 된 인원수
		
		searchMap.addList("evalSubmitYnCounts", getStr("prs.emp.prsEmpEvalResult.getEvalSubmitYnCounts", searchMap)); //년도 평가여부가 Y인 전체 인원수
		
		if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
		
			searchMap.addList("getEvalGrpList", getList("prs.emp.prsEmpEvalResult.getEvalGrpList", searchMap));

			//부서조회
			searchMap.addList("getDeptList", getList("prs.emp.prsEmpEvalResult.getDeptList", searchMap));
	 
			String findEvalGrpId = (String)searchMap.get("findEvalGrpId");
	 
			if("".equals(StaticUtil.nullToDefault(findEvalGrpId,""))){
				searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpList", "EVAL_GRP_ID", 0));
			}
		}
			
		return searchMap;
    }
    
    /**
     * 평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalResultList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
    		
    		searchMap.addList("getEvalGrpId", getList("prs.emp.prsEmpEvalResult.getEvalGrpId", searchMap));
    		 
			String findEvalGrpId = (String)searchMap.get("findEvalGrpId");
	 
			if("".equals(StaticUtil.nullToDefault(findEvalGrpId,""))){
				searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpId", "EVAL_GRP_ID", 0));
			}
    		
    	}
        
        searchMap.addList("list", getList("prs.emp.prsEmpEvalResult.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 평가군 조회 ajax
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("getEvalGrpList", getList("prs.emp.prsEmpEvalResult.getEvalGrpList", searchMap));
    	
    	searchMap.put("findEvalGrpId", (String)searchMap.getDefaultValue("getEvalGrpList", "EVAL_GRP_ID", 0));
    	return searchMap;
    }

    /**
     * 평가군별 부서 조회 ajax
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("getDeptList", getList("prs.emp.prsEmpEvalResult.getDeptList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalResultModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.emp.prsEmpEvalResult.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalResultProcess(SearchMap searchMap) {
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
        } else if("CANCEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("GET".equals(stMode)) {
            searchMap = updateDBS(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가결과 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.emp.prsEmpEvalResult.insertData", searchMap);
        
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
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDBS(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		if("Y".equals(searchMap.getString("evalCloseYn"))){
    			returnMap = deleteData("prs.emp.prsEmpEvalResult.deleteData", searchMap, true);
    			
    			String getMaxCount = getStr("prs.emp.prsEmpEvalResult.getMaxCount", searchMap);
    			
    			searchMap.put("getMaxCount", getMaxCount);
    			
    			returnMap = updateData("prs.emp.prsEmpEvalResult.insertData", searchMap);
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
     * 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.emp.prsEmpEvalResult.updateData", searchMap);
            
            if("Y".equals(searchMap.getString("evalCloseYn"))){
	            returnMap = deleteData("prs.emp.prsEmpEvalResult.deleteData", searchMap);

	            String getMaxCount = getStr("prs.emp.prsEmpEvalResult.getMaxCount", searchMap);
	        	
	        	searchMap.put("getMaxCount", getMaxCount);
	            
	            returnMap = updateData("prs.emp.prsEmpEvalResult.insertData", searchMap);
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
     * 평가결과 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            
            
            setStartTransaction();
            
            returnMap = updateData("prs.emp.prsEmpEvalResult.updateData", searchMap);
            
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
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap prsEmpEvalResultListExcel(SearchMap searchMap) {
        String excelFileName = "평가결과";
        String excelTitle = "평가결과 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        excelSearchInfoList.add(new ExcelVO("평가군", (String)searchMap.get("evalGrpNm")));
        //excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
        //excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("평가군", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("사원번호", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("상위부서코드", "UP_DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("상위부서", "UP_DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("조직코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "left"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("순위", "RANKING", "left"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.emp.prsEmpEvalResult.getList", searchMap));
        
        return searchMap;
    }
    
}

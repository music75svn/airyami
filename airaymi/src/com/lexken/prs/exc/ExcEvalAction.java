/*************************************************************************
* CLASS 명      : ExcEvalAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 07월 20일 
* 기    능      : 간부 별도평가군
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김상용      2013년 07월 20일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.exc;
    
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

public class ExcEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부 별도평가군 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalList(SearchMap searchMap) {
    	
    	searchMap.addList("yearCheck", getDetail("prs.eval.evalSchedule.getYearCheck", searchMap));		//해당년도 일정입력체크
    	searchMap.addList("rptSchedule", getDetail("prs.exc.excEval.getRptSchedule", searchMap)); //입력기간 조회
    	searchMap.addList("confirmYn", getDetail("prs.exc.excEval.getConfirmYn", searchMap)); //확정여부 조회
    	    	
    	return searchMap;
    }
    
    /**
     * 간부 별도평가군 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.exc.excEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부 별도평가군 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	HashMap detail = new HashMap();
    	
    	if("MOD".equals(stMode)) {
    		detail = getDetail("prs.exc.excEval.getDetail", searchMap);
    		searchMap.addList("detail", detail);
    		//관리조직 조회 
	        searchMap.addList("deptList", getList("prs.exc.excEval.getDeptList", searchMap));
	        searchMap.addList("posTcList", getList("prs.exc.excEval.getPosTcList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 간부 별도평가군 등록/대상자자동등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"AUTO".equals(stMode)) {
        	returnMap = this.validChk(searchMap);

        	if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
       
        /**********************************
         * 등록/대상자자동등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            //searchMap = insertDB(searchMap);
        } else if("AUTO".equals(stMode)) {
        	searchMap = insertAutoDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 간부 별도평가군 대상자 자동등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAutoDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
     try {
          setStartTransaction(); 
	        
	      /**********************************
	       * 대상자 자동등록
	       **********************************/
          
            returnMap = insertData("prs.exc.excEval.deleteUseDataAuto", searchMap);
		  	returnMap = insertData("prs.exc.excEval.insertUseDataAuto", searchMap);
		  	returnMap = insertData("prs.exc.excEval.insertLessThan1Months", searchMap);

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
     * 간부 별도평가군 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
			String[] excGrpIds = searchMap.getString("excGrpIds").split("\\|", 0);
			String[] excGrpGrades = searchMap.getString("excGrpGrades").split("\\|", 0);
	        setStartTransaction();
	        
	        if(null != excGrpIds && 0 < excGrpIds.length) {
		        for (int i = 0; i < excGrpIds.length; i++) {
		            searchMap.put("excGrpId", excGrpIds[i]);
		            
		            //vm 페이지에서 공백 체크를 위해 사용한 XX가 입력된 경우 DB에 공백값을 입력
		            if("XX".equals(excGrpGrades[i])){
		            	excGrpGrades[i] = "";
		            }
		            
		            searchMap.put("excGrpGrade", excGrpGrades[i]);
		            
		            returnMap = updateData("prs.exc.excEval.updateData", searchMap);
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
     *  확정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	String[] excGrpIds = searchMap.getString("excGrpIds").split("\\|", 0);
		String[] excGrpGrades = searchMap.getString("excGrpGrades").split("\\|", 0);
		String deptYn = searchMap.getString("deptYn");
    	try {
    		setStartTransaction();
    		
    		if("Y".equals(deptYn)){
	    		if(null != excGrpIds && 0 < excGrpIds.length) {
			        for (int i = 0; i < excGrpIds.length; i++) {
			            searchMap.put("excGrpId", excGrpIds[i]);
			            
			            //vm 페이지에서 공백 체크를 위해 사용한 XX가 입력된 경우 DB에 공백값을 입력
			            if("XX".equals(excGrpGrades[i])){
			            	excGrpGrades[i] = "";
			            }
			            searchMap.put("excGrpGrade", excGrpGrades[i]);
			            returnMap = updateData("prs.exc.excEval.updateData", searchMap);
			        }
			    }
    		}
    		
    		returnMap = updateData("prs.exc.excEval.insertDeptYnData", searchMap); //확정여부 수정
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
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
     * 간부 별도평가군 대상자관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalUseList(SearchMap searchMap) {
    	
        searchMap.addList("excGrpUseDatail", getDetail("prs.exc.excEval.getExcGrpUseDatail", searchMap));
    	    	
    	return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalUseList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.exc.excEval.getUseList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 상세보기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalUseModify(SearchMap searchMap) {
    	
    	searchMap.addList("excGrpUseDatail", getDetail("prs.exc.excEval.getExcGrpUseDatail", searchMap));
    	searchMap.addList("insaUseList", getList("prs.exc.excEval.getInsaUseList", searchMap));    	
        searchMap.addList("userList", getList("prs.exc.excEval.getUseList", searchMap));
        
        return searchMap;
    }    
    
    /**
     * 간부 별도평가군 대상자관리 등록/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalUseProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        if ("".equals(stMode)) stMode = "ADD"; 
        
        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertUseDB(searchMap);
        }else if("DEL".equals(stMode)) {
            searchMap = deleteUseDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertUseDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        String empn    = searchMap.getString("empns");
        String[] empns = searchMap.getString("empns").split("\\|", 0);
        
        returnMap = updateData("prs.exc.excEval.deleteUseData", searchMap, true); 
        
        if(!"".equals(empn)) {
	        for (int i = 0; i < empns.length; i++) {
	        	searchMap.put("empn", empns[i]);
	        	returnMap = insertData("prs.exc.excEval.insertUseData", searchMap);
	        }
        }
        
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 간부 별도평가군 대상자관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteUseDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] empns = searchMap.getString("empns").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != empns && 0 < empns.length) {
		        for (int i = 0; i < empns.length; i++) {
		        	
		            searchMap.put("empn", empns[i]);
		            returnMap = updateData("prs.exc.excEval.deleteUseData", searchMap);
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
     * 간부 별도평가군 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap excEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "간부 별도평가군 관리";
    	String excelTitle = "간부 별도평가군 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("findYear")));    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("별도평가군코드", 	"EXC_GRP_ID",	"right"));
    	excelInfoList.add(new ExcelVO("별도평가군", 	"EXC_GRP_NM",	"right"));
    	excelInfoList.add(new ExcelVO("대상인원", 		"EXC_COUNT",	"right"));
    	excelInfoList.add(new ExcelVO("평가방법", 		"EVAL_TYPE",	"right"));
    	excelInfoList.add(new ExcelVO("등급", 		"EVAL_GRADE",	"right"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.exc.excEval.getList", searchMap));
    	
        return searchMap;
    }
    
}

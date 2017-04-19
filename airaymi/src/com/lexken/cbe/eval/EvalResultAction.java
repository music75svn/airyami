/*************************************************************************
* CLASS 명      : EvalResultAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 10일 
* 기    능      : 종합평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 10일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.eval;
    
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

public class EvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 종합평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));	
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		searchMap.put("findEvalMembUserId", "");
    	} else {
    		searchMap.put("findEvalMembUserId", searchMap.get("loginUserId"));
    	}
    	
        return searchMap;
    }
    
    /**
     * 종합평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 종합평가결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalResult.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popResultDetail(SearchMap searchMap) {
    	
    	searchMap.addList("detailGubun", getDetail("cbe.eval.evalExceDept.getDetailGubun", searchMap));
    	
    	searchMap.addList("detailResult", getDetail("cbe.eval.evalResult.getResultList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 종합평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultProcess(SearchMap searchMap) {
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
     * 종합평가결과 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalResult.insertData", searchMap);
        
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
     * 종합평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalResult.updateData", searchMap);
	        
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
     * 종합평가결과 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalMembUserIds = searchMap.getString("evalMembUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("evalMembUserId", evalMembUserIds[i]);
		            returnMap = updateData("cbe.eval.evalResult.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("workTerm"), "근무기간");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 종합평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "종합평가결과";
    	String excelTitle = "종합평가결과 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("가중치그룹", "WEIGHT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("경쟁그룹", "RIVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("성명", "NAME", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("사업소", "FRS_DEPT_CONVERT_SCORE", "right","",3000));
    	excelInfoList.add(new ExcelVO("2차사업소", "SEC_DEPT_CONVERT_SCORE", "right","",3000));
    	excelInfoList.add(new ExcelVO("팀", "TEAM_CONVERT_SCORE", "right","",3000));
    	excelInfoList.add(new ExcelVO("개인", "PRS_CONVERT_SCORE", "right","",3000));
    	excelInfoList.add(new ExcelVO("종합점수", "CONBI_EVAL_SCORE", "right","",3000));
    	excelInfoList.add(new ExcelVO("종합등급", "CONBI_EVAL_FIX_GRADE", "right","",3000));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalDegreeId")))) {
			searchMap.put("findEvalDegreeId", (String)searchMap.get("evalDegreeId"));
		}
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalResult.getList", searchMap));
    	
        return searchMap;
    }
    
}

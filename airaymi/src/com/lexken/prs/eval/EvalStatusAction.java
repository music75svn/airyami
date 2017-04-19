/*************************************************************************
* CLASS 명      : EvalStatusAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 27일 
* 기    능      : 평가진행현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 27일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
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

public class EvalStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가진행현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("prs.eval.evalGroup.getDegreeList", searchMap));
    	
    	ArrayList evalDegreeList = new ArrayList();
    	evalDegreeList = (ArrayList)getList("prs.eval.evalGroup.getDegreeList", searchMap); 
        searchMap.addList("evalDegreeList", evalDegreeList);
        
        String evalDegreeId = "";
        if(null != evalDegreeList && 0 < evalDegreeList.size()) {
	        for (int i = 0; i < evalDegreeList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)evalDegreeList.get(i);
	        	if(i==0){
	        		evalDegreeId = (String)t.get("EVAL_DEGREE_ID");
	        	}
			}
        }
        
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalDegreeId")))) {
			searchMap.put("findEvalDegreeId", evalDegreeId);
		}
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("prs.eval.evalStatus.getUserList", searchMap));
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalStatus.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalStatus.getConfirmYn", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.eval.evalStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("prs.eval.evalStatus.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가진행현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusProcess(SearchMap searchMap) {
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
     * 평가진행현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("prs.eval.evalStatus.insertData", searchMap);
        
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
     * 평가진행현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	
        	String dataChk = getStr("prs.eval.evalStatus.getCloseYnDataChk", searchMap);
        	String closeYn = (String)searchMap.get("evalCloseYn");
        	
	        setStartTransaction();
	        
	        if("0".equals(dataChk)){
	        	returnMap = insertData("prs.eval.evalStatus.insertData", searchMap);
	        }else{
	        	returnMap = updateData("prs.eval.evalStatus.updateData", searchMap);
	        }
	        
	        if("Y".equals(closeYn)){
	        	/**********************************
	             * 실적집계
	             **********************************/
	        	returnMap = insertData("prs.eval.evalStatus.execDB", searchMap);
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
     * 평가진행현황 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", 0);
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("evalGrpId", evalGrpIds[i]);
			searchMap.put("evalUserId", evalUserIds[i]);
		            returnMap = updateData("prs.eval.evalStatus.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalSeq"), "평가 순번");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가진행현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "평가진행현황";
    	String excelTitle = "평가진행현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "EVAL_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("사용자명", "USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가군 코드", "EVAL_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가군 명", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가제출 여부", "EVAL_SUBMIT_YN", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_SUBMIT_YN_NM", "EVAL_SUBMIT_YN_NM", "left"));
    	excelInfoList.add(new ExcelVO("USER_CNT", "USER_CNT", "left"));
    	excelInfoList.add(new ExcelVO("USER_IDX", "USER_IDX", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalStatus.getList", searchMap));
    	
        return searchMap;
    }
    
}

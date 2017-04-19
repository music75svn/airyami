/*************************************************************************
* CLASS 명      : EvalWeightAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 7일 
* 기    능      : 가중치그룹
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 7일         최 초 작 업 
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
import com.lexken.framework.util.StaticUtil;

public class EvalWeightAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 가중치그룹 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));

        return searchMap;
    }
    
    /**
     * 가중치그룹 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalWeight.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 가중치그룹 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailGubun", getDetail("cbe.eval.evalExceDept.getDetailGubun", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalWeight.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 가중치그룹 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightProcess(SearchMap searchMap) {
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
     * 가중치그룹 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalWeight.insertData", searchMap);
        
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
     * 가중치그룹 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalWeight.updateData", searchMap);
	        
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
     * 가중치그룹 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] weightGrpIds = searchMap.getString("weightGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("weightGrpId", weightGrpIds[i]);
		            returnMap = updateData("cbe.eval.evalWeight.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("frsDeptWeight"), "사업소 가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("secDeptWeight"), "2차사업소 가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("teamWeight"), "팀 가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("prsWeight"), "개인평가 가중치");
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
     * 가중치그룹 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalWeightListExcel(SearchMap searchMap) {
    	String excelFileName = "가중치그룹";
    	String excelTitle = "가중치그룹 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("대상인원", "USER_CNT", "right"));
    	excelInfoList.add(new ExcelVO("처/실사업소", "FRS_DEPT_WEIGHT", "right","",3000));
    	excelInfoList.add(new ExcelVO("2차사업소", "SEC_DEPT_WEIGHT", "right","",3000));
    	excelInfoList.add(new ExcelVO("팀", "TEAM_WEIGHT", "right","",3000));
    	excelInfoList.add(new ExcelVO("개인", "PRS_WEIGHT", "right","",3000));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "right"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalDegreeId")))) {
			searchMap.put("findEvalDegreeId", (String)searchMap.get("evalDegreeId"));
		}
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalWeight.getList", searchMap));
    	
        return searchMap;
    }
    
}

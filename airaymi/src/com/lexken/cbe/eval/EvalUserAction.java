/*************************************************************************
* CLASS 명      : EvalUserAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 12월 11일 
* 기    능      : 평가대상자(평가종합)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 12월 11일         최 초 작 업 
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

public class EvalUserAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가대상자(평가종합) 조회
     * @param      
     * @return String  
     * @throws 
     */
    @SuppressWarnings("unchecked")
	public SearchMap evalUserList(SearchMap searchMap) {
    	
    	String findEvalDegreeId = StaticUtil.nullToDefault((String)searchMap.get("findEvalDegreeId"),"");
    	String findWeightGrpId = StaticUtil.nullToDefault((String)searchMap.get("findWeightGrpId"),"");
    	String findRivalGrpId = StaticUtil.nullToDefault((String)searchMap.get("findRivalGrpId"),"");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));	
    	
    	if("".equals(findEvalDegreeId)){
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 가중치그룹 조회
         **********************************/
    	searchMap.addList("weightGrpList", getList("cbe.eval.evalUser.getWeightGrpList", searchMap));
    	
    	if("".equals(searchMap.getString("findWeightGrpId"))) {
        	searchMap.put("findWeightGrpId", searchMap.getDefaultValue("weightGrpList", "WEIGHT_GRP_ID", 0));
        }
    	
    	/**********************************
         * 경쟁그룹 조회
         **********************************/
    	searchMap.addList("rivalGrpList", getList("cbe.eval.evalUser.getRivalGrpList", searchMap));	
    	
    	/**********************************
         * 종합평가 그룹별 조직 조회
         **********************************/
    	searchMap.addList("scDeptFirstList", getList("cbe.eval.evalUser.getScDeptFirstList", searchMap));	
    	searchMap.addList("scDeptSecondList", getList("cbe.eval.evalUser.getScDeptSecondList", searchMap));	
    	searchMap.addList("scDeptTeamList", getList("cbe.eval.evalUser.getScDeptTeamList", searchMap));	
    	searchMap.addList("jikgubList", getList("cbe.eval.evalUser.getJikgubList", searchMap));
    	searchMap.addList("evalGbnList", getList("cbe.eval.evalUser.getEvalGbnList", searchMap));
    	

        return searchMap;
    }
    
    /**
     * 평가대상자(평가종합) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserList_xml(SearchMap searchMap) {
    	
    	String searchFlag = "";
    	
    	String findWeightGrpId = StaticUtil.nullToDefault((String)searchMap.get("findWeightGrpId"),"");
    	String findRivalGrpId = StaticUtil.nullToDefault((String)searchMap.get("findRivalGrpId"),"");
    	String findEvalMethodGbn = StaticUtil.nullToDefault((String)searchMap.get("findEvalMethodGbn"),"");
    	String findName = StaticUtil.nullToDefault((String)searchMap.get("findName"),"");
    	
    	if("".equals(findWeightGrpId) && !"".equals(findRivalGrpId)){
    		searchFlag = "rival";
    	}else if(!"".equals(findWeightGrpId) && "".equals(findRivalGrpId)){
    		searchFlag = "weight";
    	}else if(!"".equals(findWeightGrpId) && !"".equals(findRivalGrpId)){
    		searchFlag = "both";
    	}else if("".equals(findWeightGrpId) && "".equals(findRivalGrpId)){
    		if("".equals(findEvalMethodGbn) && "".equals(findName)){
    			searchFlag = "none";
    		}else{
    			searchFlag = "all";
    		}
    	}
    	
    	searchMap.put("searchFlag", searchFlag);
        
        searchMap.addList("list", getList("cbe.eval.evalUser.getList", searchMap));

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
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalUser.getDegreeList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getWeightGrpList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("weightGrpList", getList("cbe.eval.evalUser.getWeightGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getRivalGrpList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("rivalGrpList", getList("cbe.eval.evalUser.getRivalGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자(평가종합) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalUser.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가대상자(평가종합) 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserProcess(SearchMap searchMap) {
        @SuppressWarnings("rawtypes")
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
     * 평가대상자(평가종합) 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalUser.insertData", searchMap);
        
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
     * 평가대상자(평가종합) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] evalMembUserIds = searchMap.getString("evalMembUserIds").split("\\|", 0);
			String[] weightGrpIds = searchMap.getString("chWeightGrpIds").split("\\|", 0);
			String[] rivalGrpIds = searchMap.getString("chRivalGrpIds").split("\\|", 0);
			String[] itemFrsDeptIds = searchMap.getString("itemFrsDeptIds").split("\\|", 0);
			String[] itemSecDeptIds = searchMap.getString("itemSecDeptIds").split("\\|", 0);
			String[] itemTeamIds = searchMap.getString("itemTeamIds").split("\\|", 0);
			String[] jikgubIds = searchMap.getString("jikgubIds").split("\\|", 0);
			String[] evalMethodGbns = searchMap.getString("evalMethodGbns").split("\\|", 0);
			
			String evalMembUserId = "";
			String weightGrpId = "";
			String rivalGrpId= "";
			String itemFrsDeptId= "";
			String itemSecDeptId= "";
			String itemTeamId= "";
			String jikgubId= "";
			String evalMethodGbn= "";
			
	        setStartTransaction();
	        
	        if(null != evalMembUserIds && 0 < evalMembUserIds.length) {
		        for (int i = 0; i < evalMembUserIds.length; i++) {
		        	
		        	evalMembUserId = evalMembUserIds[i];
		        	
		        	weightGrpId = weightGrpIds[i];
		        	if("none".equals(weightGrpId)){weightGrpId = "";}
		        	
		        	rivalGrpId = rivalGrpIds[i];
		        	if("none".equals(rivalGrpId)){rivalGrpId = "";}
		        	
		        	itemFrsDeptId = itemFrsDeptIds[i];
		        	if("none".equals(itemFrsDeptId)){itemFrsDeptId = "";}
		        	
		        	itemSecDeptId = itemSecDeptIds[i];
		        	if("none".equals(itemSecDeptId)){itemSecDeptId = "";}
		        	
		        	itemTeamId = itemTeamIds[i];
		        	if("none".equals(itemTeamId)){itemTeamId = "";}
		        	
		        	jikgubId = jikgubIds[i];
		        	if("none".equals(jikgubId)){jikgubId = "";}
		        	
		        	evalMethodGbn = evalMethodGbns[i];
		        	if("none".equals(evalMethodGbn)){evalMethodGbn = "";}
		        	
		            searchMap.put("evalMembUserId", evalMembUserId);
		            searchMap.put("weightGrpId", weightGrpId);
		            searchMap.put("rivalGrpId", rivalGrpId);
		            searchMap.put("itemFrsDeptId", itemFrsDeptId);
		            searchMap.put("itemSecDeptId", itemSecDeptId);
		            searchMap.put("itemTeamId", itemTeamId);
		            searchMap.put("jikgubId", jikgubId);
		            searchMap.put("evalMethodGbn", evalMethodGbn);
		            returnMap = updateData("cbe.eval.evalUser.updateData", searchMap);
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
     * 평가대상자(평가종합) 삭제 
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
		            returnMap = updateData("cbe.eval.evalUser.deleteData", searchMap);
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
     * 평가대상자(평가종합) 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalUserListExcel(SearchMap searchMap) {
    	String excelFileName = "평가대상자(평가종합)";
    	String excelTitle = "평가대상자(평가종합) 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("degreeNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가대상자 사번", "EVAL_MEMB_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("성명", "NAME", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치 그룹", "WEIGHT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("경쟁 그룹", "RIVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 사업소", "ITEM_FRS_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 2차사업소", "ITEM_SEC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 팀", "ITEM_TEAM_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법 구분", "EVAL_METHOD_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalUser.getExcelList", searchMap));
    	
        return searchMap;
    }
    
}

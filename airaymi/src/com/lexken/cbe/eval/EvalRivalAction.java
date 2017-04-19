/*************************************************************************
* CLASS 명      : EvalRivalAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 8일 
* 기    능      : 경쟁그룹
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 8일         최 초 작 업 
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

public class EvalRivalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 경쟁그룹 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalRivalList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));	

        return searchMap;
    }
    
    /**
     * 경쟁그룹 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalRivalList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalRival.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사용 조직 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popDeptCopy(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
    	searchMap.addList("useScDeptList", getList("cbe.eval.evalRival.getUseScDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 경쟁그룹 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalRivalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailGubun", getDetail("cbe.eval.evalExceDept.getDetailGubun", searchMap));
    	searchMap.addList("jikgubList", getList("cbe.eval.evalWeightMapping.getJikgubList", searchMap));
    	
    	/**********************************
         * 직급 조회 
         **********************************/
        ArrayList jikgubList = new ArrayList();
        jikgubList = (ArrayList)getList("cbe.eval.evalRival.jikgubList", searchMap); 
        searchMap.addList("checkJikgubList", jikgubList);
        String[] jikgubArray = new String[0]; 
        if(null != jikgubList && 0 < jikgubList.size()) {
        	jikgubArray = new String[jikgubList.size()];
        	for (int i = 0; i < jikgubList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)jikgubList.get(i);
	        	jikgubArray[i] = (String)t.get("JIKGUB_ID"); 
        	}
        }
        searchMap.addList("jikgubDesc", jikgubArray);
        
        searchMap.addList("deptDesc", getDetail("cbe.eval.evalRival.deptList", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalRival.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    
    /**
     * 년도별 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getEvalRival_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	
    	if( null == findEvalDegreeId || "".equals(findEvalDegreeId) ){
    		searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalRivalList", getList("cbe.eval.evalRival.getList", searchMap));

        return searchMap;
    }
    
    
    
    
    /**
     * 경쟁그룹 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalRivalProcess(SearchMap searchMap) {
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
     * 경쟁그룹 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
			String[] jikgubIds = searchMap.getString("jikgubIds").split("\\|", 0);
			
	        setStartTransaction();
	        
	        /**********************************
	         * 지표 코드 채번 
	         **********************************/
	        String rivalGrpId = getStr("cbe.eval.evalRival.getRivalGrpId", searchMap);
	        searchMap.put("rivalGrpId", rivalGrpId);
	        
	        returnMap = insertData("cbe.eval.evalRival.insertData", searchMap);
	        
	        if(null != scDeptIds && 0 < scDeptIds.length) {
	        	for (int i = 0; i < scDeptIds.length; i++) {
		            searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.eval.evalRival.deleteDeptData", searchMap, true);
		            returnMap = updateData("cbe.eval.evalRival.insertDeptData", searchMap);
		        }
		    }
	        
	        if(null != jikgubIds && 0 < jikgubIds.length) {
	        	returnMap = updateData("cbe.eval.evalRival.deleteJikgubData", searchMap, true);
	            
	        	for (int i = 0; i < jikgubIds.length; i++) {
		            searchMap.put("jikgubId", jikgubIds[i]);
		            returnMap = updateData("cbe.eval.evalRival.insertJikgubData", searchMap);
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
     * 경쟁그룹 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
			String[] jikgubIds = searchMap.getString("jikgubIds").split("\\|", 0);
			
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalRival.updateData", searchMap);
	        
	        returnMap = updateData("cbe.eval.evalRival.deleteDeptData", searchMap, true);
            
	        if(null != scDeptIds && 0 < scDeptIds.length) {
	        	for (int i = 0; i < scDeptIds.length; i++) {
		            searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.eval.evalRival.insertDeptData", searchMap);
		        }
		    }
	        
	    	returnMap = updateData("cbe.eval.evalRival.deleteJikgubData", searchMap, true);
	        
	        if(null != jikgubIds && 0 < jikgubIds.length) {
	        	for (int i = 0; i < jikgubIds.length; i++) {
		            searchMap.put("jikgubId", jikgubIds[i]);
		            returnMap = updateData("cbe.eval.evalRival.insertJikgubData", searchMap);
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
     * 경쟁그룹 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] rivalGrpIds = searchMap.getString("rivalGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("rivalGrpId", rivalGrpIds[i]);
		            
		            returnMap = updateData("cbe.eval.evalRival.deleteDeptData", searchMap, true);
		            returnMap = updateData("cbe.eval.evalRival.deleteJikgubData", searchMap, true);
		            returnMap = updateData("cbe.eval.evalRival.deleteData", searchMap);
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

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "SORT_ORDER");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 경쟁그룹 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalRivalListExcel(SearchMap searchMap) {
    	String excelFileName = "경쟁그룹";
    	String excelTitle = "경쟁그룹 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("가중치 그룹", "RIVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상조직", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상직급", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("근무기간", "WORK_TERM", "right"));
    	excelInfoList.add(new ExcelVO("평가대상인원수", "USER_CNT", "right"));
    	excelInfoList.add(new ExcelVO("반영일자", "REFL_DT", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalDegreeId")))) {
			searchMap.put("findEvalDegreeId", (String)searchMap.get("evalDegreeId"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalRival.getList", searchMap));
    	
        return searchMap;
    }
    
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popUserDetail(SearchMap searchMap) {
    	
    	searchMap.addList("detailGubun", getDetail("cbe.eval.evalExceDept.getDetailGubun", searchMap));
    	
    	searchMap.addList("detailGrp", getDetail("cbe.eval.evalRival.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popUserDetail_xml(SearchMap searchMap) {

    	searchMap.addList("userList", getList("cbe.eval.evalRival.getUserList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가대상자매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rivalGrpReflProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        if("ADD".equals(stMode)) {
            searchMap = updateRivalGrpDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 평가대상자매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateRivalGrpDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] reflUserIds = searchMap.getString("reflUserIds").split("\\|", 0);
			
	        setStartTransaction();
	        
	        if(null != reflUserIds && 0 < reflUserIds.length) {
	        	for (int i = 0; i < reflUserIds.length; i++) {
		            searchMap.put("reflUserId", reflUserIds[i]);
		            returnMap = updateData("cbe.eval.evalRival.updateRivalGrpData", searchMap);
		        }
	        	returnMap = updateData("cbe.eval.evalRival.updateRivalGrpReflDtData", searchMap);
	        	
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
    
}

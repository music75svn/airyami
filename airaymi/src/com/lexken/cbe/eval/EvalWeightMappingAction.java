/*************************************************************************
* CLASS 명      : EvalWeightMappingAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 7일 
* 기    능      : 평가대상자매핑
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

public class EvalWeightMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가대상자매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightMappingList(SearchMap searchMap) {
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));	

        return searchMap;
    }
    
    /**
     * 평가대상자매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalWeightMapping.getList", searchMap));

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
    	
    	searchMap.addList("useScDeptList", getList("cbe.eval.evalWeightMapping.getUseScDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailGubun", getDetail("cbe.eval.evalExceDept.getDetailGubun", searchMap));
    	searchMap.addList("jikgubList", getList("cbe.eval.evalWeightMapping.getJikgubList", searchMap));
    	
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalWeightMapping.getDetail", searchMap));
    		
    		/**********************************
             * 직급 조회 
             **********************************/
            ArrayList jikgubList = new ArrayList();
            jikgubList = (ArrayList)getList("cbe.eval.evalWeightMapping.jikgubList", searchMap); 
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
            
            searchMap.addList("deptDesc", getDetail("cbe.eval.evalWeightMapping.deptList", searchMap));
    	}
        
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
    	
    	searchMap.addList("detailGrp", getDetail("cbe.eval.evalWeight.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popUserDetail_xml(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("cbe.eval.evalWeightMapping.getUserList", searchMap));
        return searchMap;
    }
    
    /**
     * 평가대상자매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalWeightMappingProcess(SearchMap searchMap) {
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
     * 평가대상자매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalWeightMapping.insertData", searchMap);
        
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
     * 평가대상자매핑 수정
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
	        
	        returnMap = updateData("cbe.eval.evalWeightMapping.updateData", searchMap);
	        
	        /*if(null != scDeptIds && 0 < scDeptIds.length) {
	        	returnMap = updateData("cbe.eval.evalWeightMapping.deleteDeptData", searchMap, true);
	            
	        	for (int i = 0; i < scDeptIds.length; i++) {
		            searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.eval.evalWeightMapping.insertDeptData", searchMap);
		        }
		    }*/
	        
	        if(null != jikgubIds && 0 < jikgubIds.length) {
	        	returnMap = updateData("cbe.eval.evalWeightMapping.deleteJikgubData", searchMap, true);
	            
	        	for (int i = 0; i < jikgubIds.length; i++) {
		            searchMap.put("jikgubId", jikgubIds[i]);
		            returnMap = updateData("cbe.eval.evalWeightMapping.insertJikgubData", searchMap);
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
     * 평가대상자매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDept_ajax(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
			
	        setStartTransaction();
	        returnMap = updateData("cbe.eval.evalWeightMapping.deleteDeptPrsData", searchMap, true);
	        if(null != scDeptIds && 0 < scDeptIds.length) {
	        	for (int i = 0; i < scDeptIds.length; i++) {
	        		if( null != scDeptIds[i] && 0 < scDeptIds[i].length()   ){
			            searchMap.put("scDeptId", scDeptIds[i]);
			            //returnMap = updateData("cbe.eval.evalWeightMapping.deleteDeptPrsData", searchMap, true);
			            returnMap = updateData("cbe.eval.evalWeightMapping.insertDeptData", searchMap);
	        		}
		        }
		    }
	        
	        searchMap.addList("deptDesc", getList("cbe.eval.evalWeightMapping.deptList", searchMap));
	        
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
     * 평가대상자매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] weightGrpIds = searchMap.getString("weightGrpIds").split("\\|", 0);
			String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("weightGrpId", weightGrpIds[i]);
			searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.eval.evalWeightMapping.deleteData", searchMap);
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가대상자매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalWeightMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "평가대상자매핑";
    	String excelTitle = "평가대상자매핑 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("가중치 그룹", "WEIGHT_GRP_NM", "left"));
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
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalWeightMapping.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    /**
     * 평가대상자매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap weightGrpReflProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        if("ADD".equals(stMode)) {
            searchMap = updateWeightGrpDB(searchMap);
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
    public SearchMap updateWeightGrpDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] reflUserIds = searchMap.getString("reflUserIds").split("\\|", 0);
			
	        setStartTransaction();
	        
	        if(null != reflUserIds && 0 < reflUserIds.length) {
	        	for (int i = 0; i < reflUserIds.length; i++) {
		            searchMap.put("reflUserId", reflUserIds[i]);
		            returnMap = updateData("cbe.eval.evalWeightMapping.updateWeightGrpData", searchMap);
		        }
	        	returnMap = updateData("cbe.eval.evalWeightMapping.updateRivalGrpReflDtData", searchMap);
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

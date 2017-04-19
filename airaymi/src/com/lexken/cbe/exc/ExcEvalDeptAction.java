/*************************************************************************
* CLASS 명      : ExcEvalDeptAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 13일 
* 기    능      : 별도평가부서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2012년 07월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.exc;
    
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

public class ExcEvalDeptAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 별도평가조직 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalDeptList(SearchMap searchMap) {
    	
    	String findEvalDegreeId = StaticUtil.nullToDefault(((String)searchMap.get("findEvalDegreeId")), "");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	
    	searchMap.addList("evalDegreeList", getList("cbe.exc.excEvalDept.getDegreeList", searchMap));	
    	
    	if("".equals(findEvalDegreeId)){
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 확정 조회
         **********************************/
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));
    	
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
    	searchMap.addList("evalDegreeList", getList("cbe.exc.excEvalDept.getDegreeList", searchMap));	

        return searchMap;
    }
    
    /**
     * 별도평가부서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalDeptList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.exc.excEvalDept.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 별도평가부서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalDeptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	//확정여부 확인
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));
    	
    	searchMap.addList("detailGubun", getDetail("cbe.exc.excEvalDept.getDetailGubun", searchMap));
    	
    	// 평가방법
    	searchMap.addList("evalMethodlist", getList("cbe.exc.excEvalDept.getEvalMethodList", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.exc.excEvalDept.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 별도평가조직 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excEvalDeptProcess(SearchMap searchMap) {
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
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        } else if ("GET".equals(stMode)) {
        	searchMap = insertExcDept(searchMap);
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
        	
        	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("rptOldEmpn")))) {
    			searchMap.put("rptOldEmpn", (String)searchMap.get("rptEmpn"));
    		}
        
        	returnMap = insertData("cbe.exc.excEvalDept.insertData", searchMap);
        	
//        	returnMap = updateData("cbe.exc.excEvalDept.deleteAdminData", searchMap, true);//권한삭제
	        
//	        returnMap = insertData("cbe.exc.excEvalDept.insertAdminData", searchMap, true);//권한등록
	        
//	        returnMap = insertData("cbe.exc.excEvalDept.insertAdminBaseData", searchMap);//권한등록
        
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
	        
	        returnMap = updateData("cbe.exc.excEvalDept.updateData", searchMap);
	        
//	        returnMap = updateData("cbe.exc.excEvalDept.deleteAdminData", searchMap, true);//권한삭제
	        
//	        returnMap = insertData("cbe.exc.excEvalDept.insertAdminData", searchMap, true);//권한등록
	        
//	        returnMap = insertData("cbe.exc.excEvalDept.insertAdminBaseData", searchMap);//권한등록
	        
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
			String[] rptEmpns = searchMap.getString("rptEmpns").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("scDeptId", scDeptIds[i]);
		            
		            returnMap = updateData("cbe.exc.excEvalDept.deleteData", searchMap);
		            
		            if (rptEmpns.length > 0 && null == rptEmpns[i] || !"".equals(rptEmpns[i])) {
		            	searchMap.put("rptEmpn", rptEmpns[i]);
		            	searchMap.put("rptOldEmpn", rptEmpns[i]);
//		            	returnMap = updateData("cbe.exc.excEvalDept.deleteAdminData", searchMap, true);//권한삭제
		            }
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
     * 별도평가부서 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertExcDept(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("cbe.exc.excEvalDept.deleteExcDept", searchMap, true);
	        
	        returnMap = insertData("cbe.exc.excEvalDept.insertExcDept", searchMap);
	        
//	        returnMap = updateData("cbe.exc.excEvalDept.deleteAdminsData", searchMap, true);//권한삭제
	        
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
     * 별도평가부서 확정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = updateData("cbe.exc.excEvalDept.insertDeptYnData", searchMap); //확정여부 수정
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

}

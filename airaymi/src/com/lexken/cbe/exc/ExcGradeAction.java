/**************************************************************************
* CLASS 명      : ExcGradeAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 17일 
* 기    능      : 별도평가대상-평가등급실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안 요 한      2013년 07월 17일    최 초 작 업 
**************************************************************************/
package com.lexken.cbe.exc;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class ExcGradeAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 별도평가대상 등급조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGradeActionList(SearchMap searchMap) {
    	
    	searchMap.addList("Userlist", getList("cbe.exc.excGrade.getUserList", searchMap));
    	
    	searchMap.addList("Deptlist", getList("cbe.exc.excGrade.getDeptList", searchMap));
    	
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 별도평가대상 등급 저장/수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGradeProcess(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            //returnMap = this.validChk(searchMap);
            
//            if((Integer)returnMap.get("ErrorNumber") < 0 ){
//                searchMap.addList("returnMap", returnMap);
//                return searchMap;
//            }
        }
        
        /**********************************
         * 저장/제출
         **********************************/
        if("MOD".equals(stMode)) {
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
     * 별도평가대상 등급 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	String[] Empns = searchMap.getStringArray("empn");
        	String[] evalDegreeIds = searchMap.getStringArray("evalDegreeId");
        	String UserGrade = "";
        	String DeptGrade = "";
        	
        	String[] scDeptIds = searchMap.getStringArray("scDeptId");
        	String[] evalDegreeIdz = searchMap.getStringArray("evalDegreeIds");
        	
	        if(null != Empns && 0 < Empns.length) {
		        for (int i = 0; i < Empns.length; i++) {
		        	UserGrade = (String) searchMap.get("excUserGrade" + Empns[i]);
		        	searchMap.put("excUserGrade", UserGrade );
		            searchMap.put("empn", Empns[i]);
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            
		            returnMap = updateData("cbe.exc.excGrade.updateUserData", searchMap);
		        }
	        }
		            
	        if(null != scDeptIds && 0 < scDeptIds.length) {
		        for (int i = 0; i < scDeptIds.length; i++) {
		        	DeptGrade = (String) searchMap.get("excDeptGrade" + scDeptIds[i]);
		        	searchMap.put("excDeptGrade", DeptGrade );
		            searchMap.put("scDeptId", scDeptIds[i]);
		            searchMap.put("evalDegreeId", evalDegreeIdz[i]);
		            
		            returnMap = updateData("cbe.exc.excGrade.updateDeptData", searchMap);
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
     * 별도평가대상 확정 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		String[] Empns = searchMap.getStringArray("empn");
    		String[] evalDegreeIds = searchMap.getStringArray("evalDegreeId");
    		String UserGrade = "";
    		String DeptGrade = "";
    		
    		String[] scDeptIds = searchMap.getStringArray("scDeptId");
    		String[] evalDegreeIdz = searchMap.getStringArray("evalDegreeIds");
    		
    		if(null != Empns && 0 < Empns.length) {
    			for (int i = 0; i < Empns.length; i++) {
    				UserGrade = (String) searchMap.get("excUserGrade" + Empns[i]);
    				searchMap.put("excUserGrade", UserGrade );
    				searchMap.put("empn", Empns[i]);
    				searchMap.put("evalDegreeId", evalDegreeIds[i]);
    				
    				returnMap = updateData("cbe.exc.excGrade.updateUserData", searchMap); //등급 수정
    				
    			}
    		}
    		
    		if(null != scDeptIds && 0 < scDeptIds.length) {
    			for (int i = 0; i < scDeptIds.length; i++) {
    				DeptGrade = (String) searchMap.get("excDeptGrade" + scDeptIds[i]);
    				searchMap.put("excDeptGrade", DeptGrade );
    				searchMap.put("scDeptId", scDeptIds[i]);
    				searchMap.put("evalDegreeId", evalDegreeIdz[i]);
    				
    				returnMap = updateData("cbe.exc.excGrade.updateDeptData", searchMap); //등급수정
    			}
    		}
    		
    		returnMap = updateData("cbe.exc.excGrade.insertData", searchMap); //확정여부 수정
    		
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
     * 평가자가 평가대상자가 제출한 사업추진실적보고서를 조회
     * @param searchMap
     * @return
     */
    public SearchMap showRpt(SearchMap searchMap) {
    	String excEvalTargetGubun = searchMap.getString("excEvalTargetGubun");
    	HashMap excRpt = new HashMap();
    	
    	//사업추진보고서 정보를 가져온다
    	excRpt = getDetail("cbe.exc.excRpt.getMngRpt", searchMap);
    	
	    	if("D".equals(excEvalTargetGubun)) {
	    		
		    	if (excRpt == null || excRpt.size() == 0) {
		    		
		    		excRpt = getDetail("cbe.exc.excRpt.getExcRptInfo", searchMap);
		    		
		    	}
	    	} else {
	    		if (excRpt == null || excRpt.size() == 0) {
	    			
	    			excRpt = getDetail("cbe.exc.excRpt.getMngRptFromInsa", searchMap);
	    			
	    			List lastDept = getList("cbe.exc.excRpt.getLastYearDept", searchMap);
	    			
		    		excRpt.put("DUTY_START_DT", ((HashMap)lastDept.get(0)).get("START_PCMT_DATE"));
		    		excRpt.put("DUTY_END_DT", ((HashMap)lastDept.get(lastDept.size() - 1)).get("END_PCMT_DATE"));
	    			
	    		} else {
	    		
//	    		if (excRpt == null) excRpt = new HashMap();
	    		
		    		List lastDept = getList("cbe.exc.excRpt.getLastYearDept", searchMap);
		    		
		    		excRpt.put("DUTY_START_DT", ((HashMap)lastDept.get(0)).get("START_PCMT_DATE"));
		    		excRpt.put("DUTY_END_DT", ((HashMap)lastDept.get(lastDept.size() - 1)).get("END_PCMT_DATE"));
	    		}
	    		
	    	}
	    	
	    	oracle.sql.CLOB clob = ((oracle.sql.CLOB)excRpt.get("CONTENT"));
			String content = "";
			Reader reader = null;
			char[] cbuf;
			
			try {
				reader = clob.getCharacterStream();
				cbuf = new char[(int)(clob.length())];
				reader.read(cbuf);
				content = new String(cbuf);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				excRpt.put("CONTENT", content);
			}
	    	
	    	
	    	searchMap.addList("excRpt", excRpt);
        
    	/**********************************
         * 첨부파일
         **********************************/
	    searchMap.put("excRptId", getStr("cbe.exc.excRpt.getExcRptId", searchMap));
	    	
    	searchMap.addList("fileList", getList("cbe.exc.excRpt.getFileList", searchMap));
    	
        return searchMap;
        
    }
    
}

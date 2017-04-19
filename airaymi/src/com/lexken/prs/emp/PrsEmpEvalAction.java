/*************************************************************************
* CLASS 명      : PrsEmpEvalAction
* 작 업 자      : 신인수
* 작 업 일      : 2013년 7월 8일 
* 기    능      : 직원개인평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    신인수      2013년 7월 8일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.emp;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class PrsEmpEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인평가 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalList(SearchMap searchMap) {
    	
    	searchMap.addList("evalSchedule", getDetail("prs.emp.prsEmpEval.getEvalSchedule", searchMap)); //평가기간 조회
    	searchMap.addList("assessorList", getList("prs.emp.prsEmpEval.getEvalAssessor", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 직원개인평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalList_xml(SearchMap searchMap) {
    	String assessorEmpn = searchMap.getString("findAssessorEmpn");
    	
    	String empn = assessorEmpn.substring(0, 6);
    	String degree = assessorEmpn.substring(6);

    	if (!"".equals(empn) && !"".equals(degree)) {
    		searchMap.put("findAssessorEmpn", empn);
    		searchMap.put("degree", degree);
    		searchMap.addList("list", getList("prs.emp.prsEmpEval.getList", searchMap));
    	}
    	
    	searchMap.addList("evalSchedule", getDetail("prs.emp.prsEmpEval.getEvalSchedule", searchMap)); //평가기간 조회

        return searchMap;
    }
    
    /**
     * 평가자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpAssesorList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("assessorList", getList("prs.emp.prsEmpEval.getEvalAssessor", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원개인평가 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findEvalGrpId")))) {
        	
        	String evalGrpCd = getStr("prs.emp.prsEmpEval.getPrsEmpGroupCd", searchMap);
        	
    		searchMap.put("findEvalGrpId", evalGrpCd);
    	}
        
        HashMap prsEmpEvalGrp = getDetail("prs.emp.prsEmpEval.getPrsEmpEvalGrp", searchMap);
    	searchMap.addList("prsEmpEvalGrp", prsEmpEvalGrp);
    	
    	searchMap.addList("evalSchedule", getDetail("prs.emp.prsEmpEval.getEvalSchedule", searchMap));
    	
    	searchMap.put("evalMethodId", prsEmpEvalGrp.get("EVAL_METHOD_ID"));
    	
        searchMap.addList("evalItemList", getList("prs.emp.prsEmpEval.getPrsEmpEvalItemList", searchMap));
        
        List gradeList = getList("prs.emp.prsEmpEval.getPrsEmpEvalGrade", searchMap);
        
    	searchMap.addList("prsEmpEvalGrade", gradeList);
    	
//    	searchMap.addList("evalSubmitYn", getStr("prs.emp.prsEmpEval.getEvalSubmitYn", searchMap));
    	searchMap.addList("evalSubmitYn", getStr("prs.emp.prsEmpEval.getDeptEvalSubmitYn", searchMap)); //평가제출여부
    	
    	searchMap.addList("empSubmitYn", getStr("prs.emp.prsEmpEval.getEmpSubmitYn", searchMap)); //평가확정여부
        
        return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalModify_xml(SearchMap searchMap) {
    	
    	String[] empn = searchMap.getString("findAssessorEmpn").split("\\+", 0);

    	if(null != empn && 0 < empn.length) {
		        for (int i = 1; i < empn.length; i++) {
		            searchMap.put("findAssessorEmpn", empn[i-1]);
		            searchMap.put("degree", empn[i]);
		        }
    	}
    	
    	String assessorDeptId = getStr("prs.emp.prsEmpEval.getPrsEmpEvalAssesorDeptCd", searchMap);
    	
    	searchMap.put("assessorDeptId", assessorDeptId);
    	
    	List itemList = getList("prs.emp.prsEmpEval.getPrsEmpEvalItemList", searchMap);
    	searchMap.addList("evalItemList", itemList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for (int i = 0; i < itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("EVAL_ITEM_ID"); 
    		}
    	}
    	
    	String assessorDegree = searchMap.getString("assessorDegree");
    	
    	searchMap.put("itemArray", itemArray);
    	searchMap.put("assessorDegree", assessorDegree);
    	
		searchMap.addList("list", getList("prs.emp.prsEmpEval.getPrsEmpEvalMemberList", searchMap));

        return searchMap;
    }
    
    /**
     * 등급배분표
     * @param searchMap
     * @return
     */
    public SearchMap prsEmpEvalGradeItemList_xml(SearchMap searchMap) {
    	HashMap prsEmpEvalGrp = getDetail("prs.emp.prsEmpEval.getPrsEmpEvalGrp", searchMap);
    	searchMap.addList("prsEmpEvalGrp", prsEmpEvalGrp);
    	
    	searchMap.put("evalMethodId", prsEmpEvalGrp.get("EVAL_METHOD_ID"));
    	
    	List gradeList = getList("prs.emp.prsEmpEval.getPrsEmpEvalGrade", searchMap);
    	searchMap.addList("prsEmpEvalGrade", gradeList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != gradeList && 0 < gradeList.size()) {
    		itemArray = new String[gradeList.size()];
    		for (int i = 0; i < gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.addList("list", getList("prs.emp.prsEmpEval.getPrsEmpEvalItemGradeCount", searchMap));

        return searchMap;
    }
    
    /**
     * 등급배분표 현황조회 화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriTabMngList_xml(SearchMap searchMap) {
    	
    	
    	/**********************************
         * 등급배분표 등급 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 등급배분표 상세내용 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.common.evalGradeDistri.getList", searchMap));
        
        searchMap.addList("list", getList("bsc.common.evalGradeDistri.evalGradeDistriTabMngList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 직원개인평가 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap prsEmpEvalProcess(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("SUBMIT".equals(stMode)) { 
        	searchMap.put("evalSubmitYn", "Y");
        	searchMap = sumbitEvalDB(searchMap);
        } else if ("CANCEL".equals(stMode)) {
        	searchMap.put("evalSubmitYn", "N");
        	searchMap = sumbitEvalDB(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 평가자가 평가제출여부 설정
     * @param searchMap
     * @return
     */
    public SearchMap sumbitEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	
	        returnMap = updateData("prs.emp.prsEmpEval.updatePrsEmpEvalSubmitYn", searchMap);
	        
	        returnMap = updateData("prs.emp.prsEmpEval.updateEvalSubmitYn", searchMap);
	        
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
     * 평가내용 저장(저장, 평가완료, 취소)
     * 저장 	: mode == SAVE
     * 평가완료 : mode == SUBMIT
     * 취소 	: mode == CANCEL
     * @param searchMap
     * @return
     */
    public SearchMap saveEval_ajax(SearchMap searchMap) {
    	
    	HashMap returnMap    = new HashMap();
    	
    	String stMode = searchMap.getString("mode");
    	String assessorDegree = searchMap.getString("assessorDegree");
    	
    	String[] evalGradeIds = searchMap.getString("evalGradeIds").split(",");
    	String[] evalGradeValues = searchMap.getString("evalGradeValues").split(",");
    	
    	try {
        	setStartTransaction();
        	
        	if ("SUBMIT".equals(stMode))
        		searchMap.put("evalSubmitYn", "Y");
        	else 
        		searchMap.put("evalSubmitYn", "N");
        	
        	searchMap.put("score", searchMap.getString("score"));
        	searchMap.put("rank", searchMap.getString("rank"));
        	
        	if ("1ST".equals(searchMap.getString("assessorDegree"))) {
        		returnMap = updateData("prs.emp.prsEmpEval.saveEval1stScore", searchMap, true);
        	} else {
        		returnMap = updateData("prs.emp.prsEmpEval.saveEval2ndScore", searchMap, true);	
        	}
        	
        	if (!"CANCEL".equals(stMode)) {
        		
	        	for (int i = 0; i < evalGradeIds.length; i++) {
	        		searchMap.put("evalItemId", evalGradeIds[i]);
	        		searchMap.put("evalGrade", evalGradeValues[i]);
	        		
	        		if (!"-1".equals(evalGradeValues[i])) {
	        			if ("1ST".equals(searchMap.getString("assessorDegree"))) {
	                		returnMap = insertData("prs.emp.prsEmpEval.saveEval1stGrade", searchMap, true);
	                	} else {
	                		returnMap = insertData("prs.emp.prsEmpEval.saveEval2ndGrade", searchMap, true);
	                	}
	        		}
	        	}
        	}
        	
        	//searchMap.put("score", searchMap.getString("score"));
        	
        	//returnMap = insertData("prs.emp.prsEmpEval.saveEval", searchMap, true);
        	
        	searchMap.put("RESULT", "SUCCESS");
        	
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
    		searchMap.put("RESULT", "FAIL");
//        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
//			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
    	
    	return searchMap;
    }
    
    /**
     * 직원개인평가 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.emp.prsEmpEval.insertData", searchMap);
        
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
     * 직원개인평가 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.emp.prsEmpEval.updateData", searchMap);
            
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
     * 직원개인평가 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            
            
            setStartTransaction();
            
            /*
            if(null !=  && 0 < .length) {
                for (int i = 0; i < .length; i++) {
                    
                    returnMap = updateData("prs.emp.prsEmpEval.deleteData", searchMap);
                }
            }
            */
            
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
     * 직원개인평가 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap prsEmpEvalListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인평가";
        String excelTitle = "직원개인평가 리스트";
        
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
    	excelInfoList.add(new ExcelVO("조직코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_COUNT", "EVAL_COUNT", "left"));
    	excelInfoList.add(new ExcelVO("평가여부", "EVAL_YN", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_YN_NM", "EVAL_YN_NM", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.emp.prsEmpEval.getList", searchMap));
        
        return searchMap;
    }
    
}

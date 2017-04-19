/*************************************************************************
* CLASS 명      : EmpEvalConAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 19일 
* 기    능      : 직원개인기여도평가 평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 3월 19일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
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

public class EmpEvalConAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인기여도평가 평가 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConList(SearchMap searchMap) {
    	searchMap.addList("evalSchedule", getDetail("prs.evalCon.empEvalCon.getEvalSchedule", searchMap)); //평가기간 조회
    	searchMap.addList("assessorList", getList("prs.evalCon.empEvalCon.getEvalAssessor", searchMap));
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.evalCon.empEvalCon.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empAssesorList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("assessorList", getList("prs.evalCon.empEvalCon.getEvalAssessor", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 직원개인기여도평가 평가 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConModify(SearchMap searchMap) {
    	/*
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.evalCon.empEvalCon.getDetail", searchMap));
        }
        
        return searchMap;
        */
    	
    	String stMode = searchMap.getString("mode");
        
        if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findEvalGrpId")))) {

        	String evalGrpCd = getStr("prs.evalCon.empEvalCon.getEmpEvalConGroupCd", searchMap);

    		searchMap.put("findEvalGrpId", evalGrpCd);
    	}

        HashMap empEvalConGrp = getDetail("prs.evalCon.empEvalCon.getEmpEvalConGrp", searchMap);
    	searchMap.addList("empEvalConGrp", empEvalConGrp);

    	String deptNm = getStr("prs.evalCon.empEvalCon.getEmpEvalConDeptNm", searchMap);

		searchMap.put("empEvalConDeptNm", deptNm);

    	searchMap.addList("evalSchedule", getDetail("prs.evalCon.empEvalCon.getEvalSchedule", searchMap));
    	

        searchMap.addList("evalItemList", getList("prs.evalCon.empEvalCon.getEmpEvalConItemList", searchMap));

        List gradeList = getList("prs.evalCon.empEvalCon.getEmpEvalConGrade", searchMap);

    	searchMap.addList("empEvalConGrade", gradeList);

//    	searchMap.addList("evalSubmitYn", getStr("prs.evalCon.empEvalCon.getEvalSubmitYn", searchMap));
    	searchMap.addList("evalSubmitYn", getStr("prs.evalCon.empEvalCon.getDeptEmpEvalConSubmitYn", searchMap)); //평가제출여부

    	//String evalSubmitYn = getStr("prs.evalCon.empEvalCon.getDeptEmpEvalConSubmitYn", searchMap);
    	
		//searchMap.put("evalSubmitYn", evalSubmitYn);
    	
    	searchMap.addList("evalConSubmitYn", getStr("prs.evalCon.empEvalCon.getEvalConSubmitYn", searchMap)); //기여도평가 평가제출 여부
     	
    	searchMap.addList("empSubmitYn", getStr("prs.evalCon.empEvalCon.getEmpSubmitYn", searchMap)); //평가확정여부

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가  평가화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConModify_xml(SearchMap searchMap) {
    	
    	String assessorDeptId = getStr("prs.evalCon.empEvalCon.getEmpEvalConAssesorDeptCd", searchMap);
    	
    	searchMap.put("assessorDeptId", assessorDeptId);
    	
    	List itemList = getList("prs.evalCon.empEvalCon.getEmpEvalConItemList", searchMap);
    	searchMap.addList("evalItemList", itemList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for (int i = 0; i < itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("EVAL_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
		searchMap.addList("list", getList("prs.evalCon.empEvalCon.getEmpEvalConMemberList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConProcess(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("stMode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("SUBMIT".equals(stMode)) { 
        	searchMap.put("evalSubmitYn", "Y");
        	searchMap = updateEmpEvalConSubmitYn(searchMap);
        	searchMap = sumbitEvalDB(searchMap);
        } else if ("CANCEL".equals(stMode) || "SAVETEMP".equals(stMode)) {
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
    		
    		/*
    		String deptGrade = getStr("prs.evalCon.empEvalCon.getDeptGrade", searchMap);
    		
    		searchMap.put("deptGrade", deptGrade);
    		
    		
    		String finalGrade = getStr("prs.evalCon.empEvalCon.getFinalGrade", searchMap);
    		
    		searchMap.put("finalGrade", finalGrade);
    		
    		
    		returnMap = insertData("prs.evalCon.empEvalCon.updateEmpEvalConSubmitYn", searchMap);
    		*/
    		returnMap = updateData("prs.evalCon.empEvalCon.updateEvalSubmitYn", searchMap);
    		
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
     * 평가자가 평가제출여부 설정
     * @param searchMap
     * @return
     */
    public SearchMap updateEmpEvalConSubmitYn(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();

        	String[] empns = searchMap.getString("empns").split("\\|", 0);
        	String[] scores = searchMap.getString("scores").split("\\|", 0);
        	String[] ranks = searchMap.getString("ranks").split("\\|", 0);
        	
 	        setStartTransaction();
 	        
 	        for (int i = 0; i < empns.length; i++) {
 	            searchMap.put("empn", empns[i]);
 	            searchMap.put("score", scores[i]);
 	            searchMap.put("rank", ranks[i]);

 	    		String deptGrade = getStr("prs.evalCon.empEvalCon.getDeptGrade", searchMap);
 	    		
 	    		searchMap.put("deptGrade", deptGrade);
 	    		
 	    		
 	    		String finalGrade = getStr("prs.evalCon.empEvalCon.getFinalGrade", searchMap);
 	    		
 	    		searchMap.put("finalGrade", finalGrade);
 	    		
 	    		returnMap = insertData("prs.evalCon.empEvalCon.updateEmpEvalConSubmitYn", searchMap);
 	    		
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
     * 직원개인기여도평가 평가 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.evalCon.empEvalCon.insertData", searchMap);
        
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
     * 직원개인기여도평가 평가 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.evalCon.empEvalCon.updateData", searchMap);
            
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
     * 직원개인기여도평가 평가 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            
            
            setStartTransaction();
            
            /*if(null !=  && 0 < .length) {
                for (int i = 0; i < .length; i++) {
                    
                    returnMap = updateData("prs.evalCon.empEvalCon.deleteData", searchMap);
                }
            }*/
            
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
     * 평가자가 평가대상자가 제출한 자기성과기술서를 조회
     * @param searchMap
     * @return
     */
    public SearchMap empEvalConRpt(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	
    	// 자기성과기술서 성과목표리스트 가져온다.
    	List rptDetail = getList("prs.evalCon.empEvalCon.getRptDetail", searchMap); // 상세정보
    	
    	// 자기성과기술서 정보를 가져온다.
    	HashMap rpt = getDetail("prs.evalCon.empEvalCon.getRptMember", searchMap);
    	
//    	if(rpt == null){
//    		rpt = getDetail("prs.evalCon.empEvalCon.getRptReMember", searchMap);//대상자의 정보
//    	}else{
//			Clob clob = ((Clob)rpt.get("CONTENT"));
//			String content = "";
//			Reader reader = null;
//			char[] cbuf;
//			
//			try {
//				reader = clob.getCharacterStream();
//				cbuf = new char[(int)(clob.length())];
//				reader.read(cbuf);
//				content = new String(cbuf);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (IOException ioe) {
//				ioe.printStackTrace();
//			} finally {
//				rpt.put("CONTENT", content);
//			}
//    	}
    	
    	searchMap.addList("rpt", rpt);
    	searchMap.addList("rptDetail", rptDetail);
        
    	return searchMap;
    }

    /**
     * 직원개인기여도평가 평가 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap empEvalConListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인기여도평가 평가";
        String excelTitle = "직원개인기여도평가 평가 리스트";
        
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
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "ASSESSOR_EMPN", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_COUNT", "EVAL_COUNT", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_GRP_YN", "EVAL_GRP_YN", "left"));
    	excelInfoList.add(new ExcelVO("평가제출 여부", "EVAL_SUBMIT_YN", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "ASSESSOR_EMPN", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalCon.getList", searchMap));
        
        return searchMap;
    }
    

    /**
     * 등급배분표
     * @param searchMap
     * @return
     */
    public SearchMap empEvalConGradeItemList_xml(SearchMap searchMap) {
    	HashMap empEvalConGrp = getDetail("prs.evalCon.empEvalCon.getEmpEvalConGrp", searchMap);
    	searchMap.addList("empEvalConGrp", empEvalConGrp);
    	
    	List gradeList = getList("prs.evalCon.empEvalCon.getEmpEvalConGrade", searchMap);
    	searchMap.addList("empEvalConGrade", gradeList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != gradeList && 0 < gradeList.size()) {
    		itemArray = new String[gradeList.size()];
    		for (int i = 0; i < gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.addList("list", getList("prs.evalCon.empEvalCon.getEmpEvalConItemGradeCount", searchMap));

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

        		returnMap = updateData("prs.evalCon.empEvalCon.saveEvalScore", searchMap, true);

        	if (!"CANCEL".equals(stMode)) {
        		
	        	for (int i = 0; i < evalGradeIds.length; i++) {
	        		searchMap.put("evalItemId", evalGradeIds[i]);
	        		searchMap.put("evalGrade", evalGradeValues[i]);
            		returnMap = insertData("prs.evalCon.empEvalCon.saveEvalGrade", searchMap, true);
	        	}
        	}
        	
        	//searchMap.put("score", searchMap.getString("score"));
        	
        	//returnMap = insertData("prs.evalCon.empEvalCon.saveEval", searchMap, true);

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
}

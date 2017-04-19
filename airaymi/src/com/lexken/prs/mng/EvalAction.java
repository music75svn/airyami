/*************************************************************************
* CLASS 명      : EvalAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 06월 10일 
* 기    능      : 간부업적평가-간부개인업적평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 06월 10일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EvalAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부개인업적평가 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalList(SearchMap searchMap) {
    	
    	searchMap.addList("evalSchedule", getDetail("prs.mng.eval.getEvalSchedule", searchMap));
    	
    	searchMap.addList("evalDegreeList", getList("prs.mng.eval.getEvalDegreeList", searchMap));
    	
    	searchMap.addList("assessorList", getList("prs.mng.eval.getEvalAssessor", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 간부개인업적평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalList_xml(SearchMap searchMap) {
    	//searchMap.put("assessorDeptId", getStr("prs.mng.eval.getEvalAssesorDeptCd", searchMap));

    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(loginVO.chkAuthGrp("01")) {  // 62: 임원
    		searchMap.put("adminYN", "Y");
    	} else {
    		searchMap.put("adminYN", "N");
    	}
    	
		searchMap.addList("list", getList("prs.mng.eval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자 차수조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("evalDegreeList", getList("prs.mng.eval.getEvalDegreeList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap assesorList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("assessorList", getList("prs.mng.eval.getEvalAssessor", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findYear")))) {
    		searchMap.put("findYear", searchMap.getString("year"));
    	}
    	
    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findEvalGrpId")))) {
    		searchMap.put("findEvalGrpId", searchMap.getString("evalGrpId"));
    	}
    	
    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findAssessorEmpn")))) {
    		searchMap.put("findAssessorEmpn", searchMap.getString("assessorEmpn"));
    	}
    	
    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findEvalType")))) {
    		searchMap.put("findEvalType", searchMap.getString("evalType"));
    	}
    	
    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findAssessorGrpId")))) {
    		searchMap.put("findAssessorGrpId", searchMap.getString("assessorGrpId"));
    	}
    	
    	searchMap.put("assessorDeptId", getStr("prs.mng.eval.getEvalAssesorDeptCd", searchMap));
    	
    	searchMap.addList("evalSchedule", getDetail("prs.mng.eval.getEvalSchedule", searchMap));
    	
    	HashMap evalGrp = getDetail("prs.mng.eval.getEvalGrp", searchMap);
    	searchMap.addList("evalGrp", evalGrp);
    	
    	searchMap.put("evalMethodId", evalGrp.get("EVAL_METHOD_ID"));
    	
    	searchMap.addList("evalItemList", getList("prs.mng.eval.getEvalItemList", searchMap));
    	
    	searchMap.put("findEvalDegreeId", searchMap.getString("evalDegreeId"));
    	
    	List gradeList = getList("prs.mng.eval.getEvalGrade", searchMap);
    	searchMap.addList("evalGrade", gradeList);
    	
    	searchMap.addList("evalSubmitYn", getStr("prs.mng.eval.getEvalSubmitYn", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalModify_xml(SearchMap searchMap) {
    	String assessorDeptId = getStr("prs.mng.eval.getEvalAssesorDeptCd", searchMap);
    	
    	searchMap.put("assessorDeptId", assessorDeptId);
    	
    	List itemList = getList("prs.mng.eval.getEvalItemList", searchMap);
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
    	
		searchMap.addList("list", getList("prs.mng.eval.getEvalMemberList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자가 평가대상자가 제출한 자기성과기술서를 조회
     * @param searchMap
     * @return
     */
    public SearchMap showRpt(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	
    	// 전년도 소속부서 정보를 가져온다.
    	List lastDept = getList("prs.mng.rpt.getRptDept", searchMap);
    	if (lastDept == null || lastDept.size() == 0) {
    		lastDept = getList("prs.mng.rpt.getLastYearDeptFromPcmt", searchMap);
    	}
    	searchMap.addList("lastDept", lastDept);
    	
    	// 자기성과기술서 성과목표리스트 가져온다.
    	List rptDetail = getList("prs.mng.rpt.getRptDetail", searchMap); // 상세정보
    	
    	searchMap.addList("rptDetail", rptDetail);
    	searchMap.addList("talkList", getList("prs.mng.talkMng.getTalkList", searchMap));//중간면담
    	
    	// 자기성과기술서 정보를 가져온다.
    	HashMap rpt = getDetail("prs.mng.rpt.getRptMember", searchMap);
    	
    	if(rpt == null){
    		rpt = getDetail("prs.mng.rpt.getRptReMember", searchMap);//대상자의 정보
    	}else{
			Clob clob = ((Clob)rpt.get("CONTENT"));
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
				rpt.put("CONTENT", content);
			}
    	}
    	
    	searchMap.addList("rpt", rpt);
        
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.mng.rpt.getFileList", searchMap));
    	searchMap.addList("actualFileList", getList("prs.mng.actualMng.getFileList", searchMap));
    	
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
        		searchMap.put("evalEndYn", "Y");
        	else 
        		searchMap.put("evalEndYn", "N");
        	
        	searchMap.put("score", searchMap.getString("score"));
        	
        	returnMap = insertData("prs.mng.eval.saveEval", searchMap, true);
        	
        	if (!"CANCEL".equals(stMode)) {
	        	for (int i = 0; i < evalGradeIds.length; i++) {
	        		searchMap.put("evalItemId", evalGradeIds[i]);
	        		searchMap.put("evalGrade", evalGradeValues[i]);
	        		
	        		if ("-1".equals(evalGradeValues[i])) {
	        			returnMap = deleteData("prs.mng.eval.deleteEvalGrade", searchMap, true);
	        		} else {
	        			returnMap = insertData("prs.mng.eval.saveEvalGrade", searchMap, true);
	        		}
	        	}
        	}
        	
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
     * 등급배분표
     * @param searchMap
     * @return
     */
    public SearchMap gradeItemList_xml(SearchMap searchMap) {
    	
    	searchMap.getString("evalDegreeId");
    	searchMap.getString("findEvalDegreeId");
    	
    	List gradeList = getList("prs.mng.eval.getEvalGrade", searchMap);
    	searchMap.addList("evalGrade", gradeList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != gradeList && 0 < gradeList.size()) {
    		itemArray = new String[gradeList.size()];
    		for (int i = 0; i < gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.addList("list", getList("prs.mng.eval.getEvalItemGradeCount", searchMap));

        return searchMap;
    }
    
    /**
     * 평가제출 처리
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalProcess(SearchMap searchMap) {
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
        
        String[] evalMembEmpns = searchMap.getString("evalMembEmpns").split("\\|",0);
        String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeqs").split("\\|",0);
        
        try {
	        setStartTransaction();
	        
	        for (int i = 0; i < evalMembEmpns.length; i++) {
                
                searchMap.put("evalMembEmpn", evalMembEmpns[i]);
                searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
                
                returnMap = updateData("prs.mng.eval.updateEvalSubmitYnFor", searchMap);
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
     * 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalListExcel(SearchMap searchMap) {
    	String excelFileName = "개인업적평가";
    	String excelTitle = "개인업적평가 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가자","ASSESSOR_NM","center"));
    	excelInfoList.add(new ExcelVO("평가군","EVAL_GRP_NM","center"));
    	excelInfoList.add(new ExcelVO("평가차수","EVAL_DEGREE_NM","left"));
    	excelInfoList.add(new ExcelVO("평가자그룹","ASSESSOR_GRP_NM","center"));
    	excelInfoList.add(new ExcelVO("평가대상인원","MEMBER_CNT","left"));
    	excelInfoList.add(new ExcelVO("평가상태","EVAL_SUBMIT_YN","left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.mng.eval.getListExcel", searchMap));
    	
    	
        return searchMap;
    }
    
}

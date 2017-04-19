/*************************************************************************
* CLASS 명      : EmpEvalConResultAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 19일 
* 기    능      : 직원개인기여도평가 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 3월 19일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EmpEvalConResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
	    /**
	     * 직원개인기여도평가 평가결과 조회
	     * @param      
	     * @return String  
	     * @throws 
	     */
	    public SearchMap empEvalConEvalList(SearchMap searchMap) {
	    	searchMap.addList("evalSchedule", getDetail("prs.evalCon.empEvalCon.getEvalSchedule", searchMap)); //평가기간 조회
	    	searchMap.addList("assessorList", getList("prs.evalCon.empEvalConResult.getEvalAssessor", searchMap));
	    return searchMap;
	    }
	    
	    /**
	     * 직원개인기여도평가 평가결과 데이터 조회(xml)
	     * @param      
	     * @return String  
	     * @throws 
	     */
	    public SearchMap empEvalConEvalList_xml(SearchMap searchMap) {
	        
	        searchMap.addList("list", getList("prs.evalCon.empEvalConResult.getEvalList", searchMap));
	
	        return searchMap;
	    }
    
    	/**
    	 * 직원개인기여도평가 평가결과 조회
    	 * @param      
    	 * @return String  
    	 * @throws 
    	 */
    	public SearchMap empEvalConResultList(SearchMap searchMap) {
    		
			String findAssessorEmpn = searchMap.getString("assessorEmpn");
	    	String findDeptCd = searchMap.getString("deptCd");
	    	//String findSearchDeptId = searchMap.getString("findSearchDeptId");
	    	String findUpDeptCd = searchMap.getString("upDeptCd");
	    	String findChkAuth = "";  
	    	String findNameEmpn = (String)searchMap.get("findNameEmpn");
	    	
	    	if("findName".equals(findNameEmpn)){
	    		searchMap.put("findName", findNameEmpn);
	    	}else{
	    		searchMap.put("findEmpn", findNameEmpn);
	    	}

			searchMap.put("nameEmpn", findNameEmpn);
	   
	    	if("".equals(findAssessorEmpn)){
	    		findAssessorEmpn = searchMap.getString("findAssessorEmpn");
	    	}
	    	
	    	if ("".equals(StaticUtil.nullToBlank(searchMap.getString("findEvalGrpId")))) {
	         	
	         	String evalGrpCd = getStr("prs.evalCon.empEvalConResult.getEmpEvalConGroupCd", searchMap);
	         	
	     		searchMap.put("findEvalGrpId", evalGrpCd);
	     	}
	    	
			searchMap.addList("assessorList", getList("prs.evalCon.empEvalConResult.getEvalAssessor", searchMap));//평가자
	    	// 최상위 평가조직 조회
	    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConAssessor.getTopDeptInfo", searchMap);
	    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
	
	    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
	    	if (topScDept == null) {
	    		topScDept = new HashMap();
	    		topScDept.put("DEPT_CD", "");
	    		topScDept.put("DEPT_KOR_NM", "");
	    	}
	    	
	    	/**********************************
	         * Return
	         **********************************/
	    	if(loginVO.chkAuthGrp("01")) {
	    		//findDeptCd ="0000";
	    		findChkAuth ="01";
	    	}else{
	    		if("0000".equals(findDeptCd)){
	    			findDeptCd ="";
	    		}
	    		findChkAuth ="02";
	    	}
	    	
	    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
	    	if("".equals(findDeptCd)){
	    		findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
	    	}
	    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
	    	// 디폴트 조회조건 설정
	    	
	    	
	    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
	    	searchMap.put("findUpDeptCd", findUpDeptCd);
	    	searchMap.put("findUpDeptName", findUpDeptName);
	    	searchMap.put("findAssessorEmpn", findAssessorEmpn);
	    	searchMap.put("findChkAuth", findChkAuth);
	    	
	    	//searchMap.addList("deptTree", getList("prs.evalCon.empEvalConResult.getDeptList", searchMap)); //인사조직
	    	searchMap.addList("gradeList", getList("prs.evalCon.empEvalConResult.getGradeList", searchMap)); //코드리스트
	    	
	    	HashMap deptId = (HashMap)getDetail("prs.evalCon.empEvalConResult.getDeptId", searchMap);
	    	
	    	searchMap.put("alloDeptCd", findDeptCd);
	    	/*
	    	if(deptId == null){
	    		searchMap.put("alloDeptCd", findDeptCd);
	    	}else{
	    		searchMap.put("alloDeptCd", (String)deptId.get("DEPT_CD"));
		    	
	    	}
	    	*/
	    	
	    	List gradeList =  getList("prs.evalCon.empEvalConResult.getEvalConEvalItemList", searchMap);
	    	searchMap.addList("itemList", gradeList);
	    	
	    	List alloList = getList("prs.evalCon.empEvalConResult.getEmpEvalConAllo", searchMap);
	    	searchMap.addList("alloList", alloList);
	    	 
	    	searchMap.addList("alloCheList", getList("prs.evalCon.empEvalConResult.getEmpEvalConChe", searchMap));//BU / U만 체크
	    	
	    	searchMap.addList("evalSchedule", getDetail("prs.evalCon.empEvalCon.getEvalSchedule", searchMap)); //평가기간 조회
	    	searchMap.addList("evalSubmitYn", getStr("prs.evalCon.empEvalConResult.getDeptEmpEvalConSubmitYn", searchMap)); //기여도평가결과 제출여부
	     	
	    	searchMap.addList("evalConSubmitYn", getStr("prs.evalCon.empEvalConResult.getEvalConSubmitYn", searchMap)); //업무특성평가 평가완료 여부
	     	
	    	
	     	searchMap.addList("empSubmitYn", getStr("prs.evalCon.empEvalCon.getEmpSubmitYn", searchMap)); //평가확정여부
	         
     		return searchMap;
    	}
    
	    /**
	     * 직원개인기여도평가 평가결과 데이터 조회(xml)
	     * @param      
	     * @return String  
	     * @throws 
	     */
	    public SearchMap empEvalConResultList_xml(SearchMap searchMap) {
	    	
	    	List itemList =  getList("prs.evalCon.empEvalConResult.getEvalConEvalItemList", searchMap);
	    	searchMap.addList("itemList", itemList);
	    	
	    	String[] itemArray = new String[0]; 
	    	if (null != itemList && 0 < itemList.size()) {
	    		itemArray = new String[itemList.size()];
	    		for (int i = 0; i < itemList.size(); i++) {
	    			HashMap map = (HashMap)itemList.get(i);
	    			itemArray[i] = (String)map.get("EVAL_ITEM_ID"); 
	    		}
	    	}
	    	
	    	searchMap.put("itemArray", itemArray);
	        
	        searchMap.addList("list", getList("prs.evalCon.empEvalConResult.getList", searchMap));
	      
	
	        return searchMap;
	    }
   
    	/**
    	 * 등급배분표 평가결과
    	 * @param searchMap
    	 * @return
    	 */
    	public SearchMap empEvalConAlloItemList_xml(SearchMap searchMap) {
    	
	    	String findAssessorEmpn = searchMap.getString("findAssessorEmpn");
	    	String findDeptCd = searchMap.getString("findDeptCd");
	    	searchMap.put("findAssessorEmpn", findAssessorEmpn);
	    	
	    	if("".equals(findDeptCd) || "0000".equals(findDeptCd)){
	    		HashMap deptId = (HashMap)getDetail("prs.evalCon.empEvalConResult.getDeptId", searchMap);
	    		
	    		searchMap.put("alloDeptCd", (String)deptId.get("DEPT_CD"));
	    	}else{
	    		searchMap.put("alloDeptCd", findDeptCd);
	    	}
	    	
	    	List alloList = getList("prs.evalCon.empEvalConResult.getEmpEvalConAllo", searchMap);
	    	searchMap.addList("alloList", alloList);
	    	
	    	String[] itemArray = new String[0]; 
	    	if (null != alloList && 0 < alloList.size()) {
	    		itemArray = new String[alloList.size()];
	    		for (int i = 0; i < alloList.size(); i++) {
	    			HashMap map = (HashMap)alloList.get(i);
	    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
	    		}
	    	}
	    	searchMap.put("itemArray", itemArray);
	    	
	    	
	    	searchMap.addList("list", getList("prs.evalCon.empEvalConResult.getEmpEvalConItemAlloCount", searchMap));
	
	        return searchMap;
	    }
    
    /**
     * 직원개인기여도평가 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConResultProcess(SearchMap searchMap) {
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
         if("SUBMIT".equals(stMode)) {
        	searchMap.put("evalSubmitYn", "Y");
            searchMap = updateDB(searchMap);
        } else if("CANCEL".equals(stMode)) {
        	searchMap.put("evalSubmitYn", "N");
        	searchMap = updateDB(searchMap);
        } else if("TEMPSUBMIT".equals(stMode)) {
        	searchMap.put("evalSubmitYn", "N");
        	searchMap = updateDB2(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        	String[] evalConEmpnIds = searchMap.getStringArray("evalConEmpnIds");
    		String[] grades = searchMap.getStringArray("grades");
    		String evalGrpId = searchMap.getString("evalGrpId");
    		
    		try {
    			setStartTransaction();
    			
    			if(null != evalConEmpnIds && 0 < evalConEmpnIds.length) {
    				for (int i = 0; i < evalConEmpnIds.length; i++) {
    					
    					if("".equals(grades[i])){
    						searchMap.put("gradeMileage", "M");
    					}else{
    						searchMap.put("gradeMileage", grades[i]);
    					}
    					
    					searchMap.put("evalConEmpnId", evalConEmpnIds[i]);
    					searchMap.put("evalGrpId", evalGrpId);
    					
    					returnMap = updateData("prs.evalCon.empEvalConResult.insertData", searchMap);
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
     * 직원개인기여도평가 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        	String[] evalConEmpnIds = searchMap.getStringArray("evalConEmpnIds");
    		String[] grades = searchMap.getStringArray("grades");
    		String evalGrpId = searchMap.getString("evalGrpId");
    		
    		try {
    			setStartTransaction();
    			
    			if(null != evalConEmpnIds && 0 < evalConEmpnIds.length) {
    				for (int i = 0; i < evalConEmpnIds.length; i++) {
    					
    					searchMap.put("gradeMileage", grades[i]);
    					
    					searchMap.put("evalConEmpnId", evalConEmpnIds[i]);
    					searchMap.put("evalGrpId", evalGrpId);
    					
    					returnMap = updateData("prs.evalCon.empEvalConResult.insertData", searchMap);
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
     * 평가결과 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap empEvalConEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "평가결과";
    	String excelTitle = "평가결과목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("조직명", "DEPT_KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "ASSESSOR_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("평가자", "ASSESSOR_EMPN_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가대상자 사번", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("평가대상자명", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	if("2013".equals((String)searchMap.get("year"))){
	    	excelInfoList.add(new ExcelVO("직무가치향상도 점수", "SCORE_ETC", "left"));
	    	excelInfoList.add(new ExcelVO("업무수행 난이도", "EC00002", "left"));
	    	excelInfoList.add(new ExcelVO("협업수행도(부서간,부서내)", "EC00001", "left"));
    	} else{
	    	excelInfoList.add(new ExcelVO("직무가치향상도", "EC00003SCORE_ETC", "left"));
	    	excelInfoList.add(new ExcelVO("정보 보안활동 강화노력", "EC00004SCORE_ETC", "left"));
	    	excelInfoList.add(new ExcelVO("개인고유지표 달성노력", "EC00002", "left"));
	    	excelInfoList.add(new ExcelVO("부서 전략지표 달성노력", "EC00001", "left"));
	    	excelInfoList.add(new ExcelVO("부서 협업 수행노력", "EC00005", "left"));
    	}
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	List itemList =  getList("prs.evalCon.empEvalConResult.getEvalConEvalItemList", searchMap);
    	searchMap.addList("itemList", itemList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for (int i = 0; i < itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("EVAL_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
        
    	searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConResult.getExcelList", searchMap));
    	
    	return searchMap;
    	
    }
    
    
}

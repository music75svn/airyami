/*************************************************************************
* CLASS 명      : TalkMngAction
* 작 업 자      : 김효은
* 작 업 일      : 2013년 12월 10일 
* 기    능      : 중간면담
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2013년 12월 10일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
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

public class TalkMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 중간면담 리스트 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList(SearchMap searchMap) {

    	searchMap.put("findDeptCd", "0000");
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.mng.planApprove.getTopDeptInfo", searchMap);
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	searchMap.put("findDeptCd", findDeptCd);
    	
    	return searchMap;
    }
    
    
    /**
     * 중간면담 리스트 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(!loginVO.chkAuthGrp("01")){
	    	if(loginVO.chkAuthGrp("85")){
	    		searchMap.put("isManager", "Y");
	    	} 
	    	if(loginVO.chkAuthGrp("81")){
	    		searchMap.put("findEvalMembEmpn", loginVO.getUser_id());
	    	}
    	} else {
    		searchMap.put("isAdmin", "Y");
    	}
       searchMap.addList("list", getList("prs.mng.talkMng.getList", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 중간면담 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngList(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
    	searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
    	searchMap.addList("closingPlan", getDetail("prs.mng.planMng.getClosingPlan", searchMap));//일정마감
        return searchMap;
    }
    
    /**
     * 중간면담 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngList_xml(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
        searchMap.addList("talkList", getList("prs.mng.talkMng.getTalkList", searchMap));

        return searchMap;
    }
    
    /**
     * 중간면담 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.mng.talkMng.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 중간면담 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngProcess(SearchMap searchMap) {
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
     * 중간면담 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.mng.talkMng.insertData", searchMap);
        
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
     * 중간면담 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.mng.talkMng.updateData", searchMap);
            
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
     * 중간면담 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {

        	String[] talkSeqs = searchMap.getString("talkSeqs").split("\\|", 0);
        	String evalMembEmpn = searchMap.getString("evalMembEmpn");
        	String evalMembEmpnSeq = searchMap.getString("evalMembEmpnSeq");
            setStartTransaction();
            if(null != talkSeqs && 0 < talkSeqs.length) {
             for (int i = 0; i < talkSeqs.length; i++) {
 	            searchMap.put("talkSeq", talkSeqs[i]);
 	            searchMap.put("evalMembEmpn", evalMembEmpn);
 	            searchMap.put("evalMembEmpnSeq", evalMembEmpnSeq);
 	            returnMap = updateData("prs.mng.talkMng.deleteData", searchMap);
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
     * 성과계획서 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMngListExcel(SearchMap searchMap) {
        String excelFileName = "중간면담";
        String excelTitle = "중간면담 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("year")));
        //excelSearchInfoList.add(new ExcelVO("이름", StaticUtil.nullToDefault((String)searchMap.get("korNm"), "전체")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서 명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직무수행기간", "WORKDATE", "center"));
    	excelInfoList.add(new ExcelVO("계획서상태", "PLAN_STATUS_NM", "center"));
    	excelInfoList.add(new ExcelVO("면담자", "TALK_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("면담일자", "TALK_DT", "center"));
    	excelInfoList.add(new ExcelVO("면담내용", "TALK_CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("답변일자", "ANSWER_DT", "center"));
    	excelInfoList.add(new ExcelVO("답변내용", "ANSWER_CONTENT", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mng.talkMng.getExcelList", searchMap));
        return searchMap;
    }

}

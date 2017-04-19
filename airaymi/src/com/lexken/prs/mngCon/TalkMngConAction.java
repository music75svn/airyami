/*************************************************************************
* CLASS 명      : TalkMngConAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 4월 3일 
* 기    능      : 직원개인업무성과 중간면담
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 4월 3일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mngCon;
    
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

public class TalkMngConAction extends CommonService {

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
	    	if(loginVO.chkAuthGrp("31")){
	    		searchMap.put("isManager", "Y");
	    	} 
	    	if(loginVO.chkAuthGrp("30")){
	    		searchMap.put("findEvalMembEmpn", loginVO.getUser_id());
	    	}
    	} else {
    		searchMap.put("isAdmin", "Y");
    	}
       searchMap.addList("list", getList("prs.mngCon.talkMngCon.getTalkList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 중간면담 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngConList(SearchMap searchMap) {
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
    	searchMap.addList("membersTable", getDetail("prs.mngCon.planMngCon.getFindList", searchMap));//목록리스트
    	searchMap.addList("closingPlan", getDetail("prs.mngCon.planMngCon.getClosingPlan", searchMap));//일정마감

        return searchMap;
    }
    
    /**
     * 직원개인업무성과 중간면담 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngConList_xml(SearchMap searchMap) {
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
        searchMap.addList("list", getList("prs.mngCon.talkMngCon.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인업무성과 중간면담 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngConModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        searchMap.addList("membersTable", getDetail("prs.mngCon.planMngCon.getFindList", searchMap));//목록리스트
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.mngCon.talkMngCon.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 직원개인업무성과 중간면담 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkMngConProcess(SearchMap searchMap) {
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
     * 직원개인업무성과 중간면담 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
            
        	setStartTransaction();

            if(loginVO.chkAuthGrp("01")){
            	returnMap = insertData("prs.mngCon.talkMngCon.insertAdminData", searchMap);
            }else{
            	returnMap = insertData("prs.mngCon.talkMngCon.insertData", searchMap);
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
     * 직원개인업무성과 중간면담 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
            
        	setStartTransaction();

            if(loginVO.chkAuthGrp("01")){
            	returnMap = updateData("prs.mngCon.talkMngCon.updateAdminData", searchMap);
            }else{
            	returnMap = updateData("prs.mngCon.talkMngCon.updateData", searchMap);
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
     * 직원개인업무성과 중간면담 삭제 
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
 	            returnMap = updateData("prs.mngCon.talkMngCon.deleteData", searchMap);
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
     * 직원개인업무성과 중간면담 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap talkMngConListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인업무성과 중간면담";
        String excelTitle = "직원개인업무성과 중간면담 리스트";
        
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
        excelInfoList.add(new ExcelVO("면담 순번", "TALK_SEQ", "left"));
    	excelInfoList.add(new ExcelVO("면담자 사번", "TALK_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("면담자 성명", "TALK_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("면담일자", "TALK_DT", "left"));
    	excelInfoList.add(new ExcelVO("면담내용", "TALK_CONTENT", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mngCon.talkMngCon.getList", searchMap));
        
        return searchMap;
    }
    
}

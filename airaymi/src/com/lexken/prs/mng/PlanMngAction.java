/*************************************************************************
* CLASS 명      : PlanMngAction
* 작 업 자      : 김효은
* 작 업 일      : 2013년 12월 5일 
* 기    능      : 성과계획서 관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2013년 12월 5일         최 초 작 업 
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

public class PlanMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과계획서 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList(SearchMap searchMap) {
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("findEvalMembEmpn", loginVO.getUser_id());
    	}
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("prs.mng.planMng.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서  목표관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngList(SearchMap searchMap) {
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
     * 성과계획서 목표 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngList_xml(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
    	searchMap.addList("planList", getList("prs.mng.planMng.getPlanList", searchMap));

        return searchMap;
    }
    
   
    /**
     * 성과계획서 관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
    	searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
    	
        if("MOD".equals(stMode)) {
         searchMap.addList("detail", getDetail("prs.mng.planMng.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 성과계획서 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngProcess(SearchMap searchMap) {
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
        } else if("APC".equals(stMode)) {
        	searchMap = approveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과계획서 관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
			int PlanYnCnt = getInt("prs.mng.planMng.getPlanYn", searchMap);
        	
        	if(PlanYnCnt > 0){
        		searchMap.put("planStatusId","01");
        		returnMap = updateData("prs.mng.planMng.updatePlanData", searchMap);
        	}else{
        		returnMap = insertData("prs.mng.planMng.insertPlanData", searchMap);
        	}
            
            returnMap = insertData("prs.mng.planMng.insertData", searchMap);
        
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
     * 성과계획서 관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.mng.planMng.updateData", searchMap);
            
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
     * 성과계획서 관리 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap approveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		searchMap.put("planStatusId","02");
    		returnMap = updateData("prs.mng.planMng.updatePlanData", searchMap);
    		
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
     * 성과계획서 관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
        	
        	String[] targets = searchMap.getString("targets").split("\\|", 0);
        	String evalMembEmpn = searchMap.getString("evalMembEmpn");
        	String evalMembEmpnSeq = searchMap.getString("evalMembEmpnSeq");
        	
            setStartTransaction();
            if(null != targets && 0 < targets.length) {
             for (int i = 0; i < targets.length; i++) {
 	            searchMap.put("target", targets[i]);
 	            searchMap.put("evalMembEmpn", evalMembEmpn);
 	            searchMap.put("evalMembEmpnSeq", evalMembEmpnSeq);
 	            returnMap = updateData("prs.mng.planMng.deleteData", searchMap);
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
    public SearchMap planMngListExcel(SearchMap searchMap) {
        String excelFileName = "성과계획서";
        String excelTitle = "성과계획서 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        //excelSearchInfoList.add(new ExcelVO("이름", StaticUtil.nullToDefault((String)searchMap.get("korNm"), "전체")));
        excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("korNm")));
        excelSearchInfoList.add(new ExcelVO("부서명", (String)searchMap.get("deptKorNm")));
        excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
        excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("castTcNm")));
        excelSearchInfoList.add(new ExcelVO("근무기간", (String)searchMap.get("workDate")));
        excelSearchInfoList.add(new ExcelVO("확인자", StaticUtil.nullToDefault((String)searchMap.get("approveUserNm"), "없음")));
        excelSearchInfoList.add(new ExcelVO("계약서상태", (String)searchMap.get("planStatusNm")));
        excelSearchInfoList.add(new ExcelVO("최종확인일", (String)searchMap.get("finalApproveDt")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("설정방향", "DIRECTION_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과목표", "TARGET_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표량", "TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("1분기", "Q1_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("2분기", "Q2_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("3분기", "Q3_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("4분기", "Q4_TARGET_VALUE", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mng.planMng.getPlanList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 성과계획서 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap planMngListExcel2(SearchMap searchMap) {
        String excelFileName = "성과계획서";
        String excelTitle = "성과계획서 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        //excelSearchInfoList.add(new ExcelVO("이름", StaticUtil.nullToDefault((String)searchMap.get("korNm"), "전체")));
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "left"));
        excelInfoList.add(new ExcelVO("이름", "KOR_NM", "left"));
        excelInfoList.add(new ExcelVO("부서명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("설정방향", "DIRECTION_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과목표", "TARGET_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표량", "TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("1분기", "Q1_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("2분기", "Q2_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("3분기", "Q3_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("4분기", "Q4_TARGET_VALUE", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mng.planMng.getPlanFullList", searchMap));
        
        return searchMap;
    }
    

}

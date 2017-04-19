/*************************************************************************
* CLASS 명      : PlanMngConAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 4월 2일 
* 기    능      : 직원개인기여도평가 성과계획서관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 4월 2일         최 초 작 업 
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

public class PlanMngConAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인업무성과 성과계획서 관리 조회
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
     * 직원개인업무성과 성과계획서 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("evalConList", getList("prs.mngCon.planMngCon.getEvalConList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원개인기여도평가 성과계획서관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngConList(SearchMap searchMap) {
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
     * 직원개인기여도평가 성과계획서관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngConList_xml(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
        
        searchMap.addList("list", getList("prs.mngCon.planMngCon.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 성과계획서관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngConModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	searchMap.addList("membersTable", getDetail("prs.mngCon.planMngCon.getFindList", searchMap));//목록리스트
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.mngCon.planMngCon.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 성과계획서관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planMngConProcess(SearchMap searchMap) {
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
     * 직원개인기여도평가 성과계획서관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        	returnMap = insertData("prs.mngCon.planMngCon.insertPlanData", searchMap);
            returnMap = insertData("prs.mngCon.planMngCon.insertData", searchMap);
        
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
     * 직원개인기여도평가 성과계획서관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.mngCon.planMngCon.updateData", searchMap);
            
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
     * 직원개인기여도평가 성과계획서관리 삭제 
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
    	            returnMap = updateData("prs.mngCon.planMngCon.deleteData", searchMap);
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
    		returnMap = updateData("prs.mngCon.planMngCon.updatePlanData", searchMap);
    		
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
     * 직원개인기여도평가 성과계획서관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap planMngConListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인기여도평가 성과계획서관리";
        String excelTitle = "직원개인기여도평가 성과계획서관리 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("설정방향", "DIRECTION_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과목표", "TARGET_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표량", "TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("목료량선정근거", "TARGET_BASIS", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("1분기", "Q1_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("2분기", "Q2_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("3분기", "Q3_TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("4분기", "Q4_TARGET_VALUE", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mngCon.planMngCon.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 자기성과 기술서 양식 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap planMngConExampleListExcel(SearchMap searchMap) {
    	String excelFileName = "자기성과 기술서 엑셀양식";
    	//String excelTitle = StringConstants.METRIC_GRP_NM + "리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
//    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
//    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("metricGrpNm"), "전체")));
//    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "METRIC_GRP_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("성명", "METRIC_GRP_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("부서코드", "TYPE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("인사조직명", "METRIC_PROPERTY_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("지표구분", "UNIT_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("지표명", "EVAL_CYCLE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("지표특성", "TIME_ROLLUP_NM", "center", "CNT", 3500));
    	excelInfoList.add(new ExcelVO("단위", "ACT_CAL_TYPE", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("목표", "CAL_TYPE_COL", "center"));
    	excelInfoList.add(new ExcelVO("실적", "CAL_TYPE_COL_NM", "left"));
    	excelInfoList.add(new ExcelVO("달성도", "CAL_TYPE_COL_UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("정렬순서", "INSERT_GUBUN_NM", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	//searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.metricGrp.getExcelList", searchMap));

        return searchMap;
    }
    
}

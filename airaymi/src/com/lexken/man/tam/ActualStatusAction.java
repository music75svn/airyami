/*************************************************************************
* CLASS 명      : ActualStatusAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 10월 31일 
* 기    능      : 실적입력승인현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 10월 31일         최 초 작 업 
**************************************************************************/
package com.lexken.man.tam;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class ActualStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적입력승인현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusList(SearchMap searchMap) {
    	String gubun = (String)searchMap.get("gubun");
    	
    	if("".equals(StaticUtil.nullToBlank(gubun))) {
    		searchMap.put("gubun", "01"); //입력현황
    	}
    	
        return searchMap;
    }
    
    /**
     * 실적입력승인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusInsertList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.tam.actualStatus.getInsertList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적승인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusApproveList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.tam.actualStatus.getApproveList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 실적승인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusCheckList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("man.tam.actualStatus.getCheckList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 실적입력승인현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("man.tam.actualStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 실적입력승인현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualStatusProcess(SearchMap searchMap) {
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
        } else if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        }        
            
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 실적입력승인현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("man.tam.actualStatus.insertData", searchMap);
        
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
     * 실적입력승인현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("man.tam.actualStatus.updateData", searchMap);
	        
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
     * 실적입력승인현황 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] mons = searchMap.getString("mons").split("\\|", 0);
			String[] subjectMetricIds = searchMap.getString("subjectMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != mons && 0 < mons.length) {
		        for (int i = 0; i < mons.length; i++) {
		            searchMap.put("mon", mons[i]);
			searchMap.put("subjectMetricId", subjectMetricIds[i]);
		            returnMap = updateData("man.tam.actualStatus.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("actual"), "실적");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 실적입력현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap actualInsertStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "실적입력현황-입력현황";
    	String excelTitle = "실적입력현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("입력담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당부서", "INSERT_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "left"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "left"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "left"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "left"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "left"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.tam.actualStatus.getInsertList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적승인현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap actualCheckStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "실적승인현황-확인현황";
    	String excelTitle = "실적확인현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("승인담당자", "APPROVE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("승인부서", "APPROVE_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "left"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "left"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "left"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "left"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "left"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.tam.actualStatus.getCheckList", searchMap));
    	
        return searchMap;
    }
    
    public SearchMap actualApproveStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "실적승인현황-승인현황";
    	String excelTitle = "실적승인현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "left"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "left"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "left"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "left"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "left"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.tam.actualStatus.getApproveList", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	if("SP_MAN_SCORE_KGS".equals(searchMap.get("procId"))){
        		returnMap = insertData("man.tam.actualStatus.callSpManScoreProc", searchMap);
        	}else{
        		returnMap = insertData("man.tam.actualStatus.callSpManActualProc", searchMap);
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
    
}

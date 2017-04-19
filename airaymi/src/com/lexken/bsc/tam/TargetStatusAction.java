/*************************************************************************
* CLASS 명      : TargetStatusAction
* 작 업 자      : 정철수
* 작 업 일      : 2013년 07월 17일
* 기    능      : 목표입력/승인현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2013년 07월 17일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
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
import com.lexken.framework.util.StaticUtil;

public class TargetStatusAction extends CommonService {

private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적입력 및 승인현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusList(SearchMap searchMap) {
    	
    	String gubun = (String)searchMap.get("gubun");
    	
    	if("".equals(StaticUtil.nullToBlank(gubun))) {
    		searchMap.put("gubun", "01"); //입력현황
    	}

        return searchMap;
    }
    
    /**
     * 목표입력현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusInsertList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.targetStatus.getInsertList", searchMap));

        return searchMap;
    }
    
    /**
     * 목표확인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusCheckList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.targetStatus.getCheckList", searchMap));

        return searchMap;
    }
    
    /**
     * 목표승인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusApproveList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.tam.targetStatus.getApproveList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 실적입력현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetInsertStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "목표입력현황";
    	String excelTitle = "목표입력현황 리스트";
    	
		/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));

    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	excelSearchInfoList.add(new ExcelVO("구분", (String)searchMap.get("gubunNm")));
    	/************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀너비', '셀정렬' (left, right, center)
         * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID"));
         * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "center"));
         * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "center", 10000));
         **********************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("입력담당자", "INSERT_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("소속조직", "DEPT_NM", "left"));
    	//excelInfoList.add(new ExcelVO("이메일", "EMAIL", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "center"));
    	//excelInfoList.add(new ExcelVO("입력대상", "T_METRIC_CNT", "center"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "center"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "center"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "center"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "center"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "center"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetStatus.getInsertList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적승인현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetCheckStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "목표확인현황";
    	String excelTitle = "목표확인현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	excelSearchInfoList.add(new ExcelVO("구분", (String)searchMap.get("gubunNm")));

    	/************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀너비', '셀정렬' (left, right, center)
         * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "center"));
         * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", 10000, "center"));
         **********************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "APPROVE_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("승인담당자", "APPROVE_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("소속조직", "DEPT_NM", "left"));
    	//excelInfoList.add(new ExcelVO("이메일", "EMAIL", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "center"));
    //	excelInfoList.add(new ExcelVO("입력대상", "T_METRIC_CNT", "center"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "center"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "center"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "center"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "center"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "center"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetStatus.getCheckList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적승인현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetApproveStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "목표승인현황";
    	String excelTitle = "목표승인현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	excelSearchInfoList.add(new ExcelVO("구분", (String)searchMap.get("gubunNm")));
    	
    	/************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀너비', '셀정렬' (left, right, center)
    	 * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "center"));
    	 * excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", 10000, "center"));
    	 **********************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "APPROVE_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("승인담당자", "APPROVE_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("소속조직", "DEPT_NM", "left"));
    	//excelInfoList.add(new ExcelVO("이메일", "EMAIL", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "center"));
    	//	excelInfoList.add(new ExcelVO("입력대상", "T_METRIC_CNT", "center"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "center"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "center"));
    	excelInfoList.add(new ExcelVO("확인요청", "CNT06", "center"));
    	excelInfoList.add(new ExcelVO("확인", "CNT03", "center"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "center"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetStatus.getApproveList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 실적입력 및 승인현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.tam.targetStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 실적입력 및 승인현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusProcess(SearchMap searchMap) {
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
     * 실적입력 및 승인현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.tam.targetStatus.insertData", searchMap);
        
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
     * 실적입력 및 승인현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.tam.targetStatus.updateData", searchMap);
	        
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
     * 실적입력 및 승인현황 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] targetStatusIds = searchMap.getString("targetStatusIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < targetStatusIds.length; i++) {
	            searchMap.put("targetStatusId", targetStatusIds[i]);
	            returnMap = updateData("bsc.tam.targetStatus.deleteData", searchMap);
	        }
	        
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    */   
        
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}

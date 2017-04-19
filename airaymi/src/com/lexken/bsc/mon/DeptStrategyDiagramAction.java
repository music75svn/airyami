/*************************************************************************
* CLASS 명      : DeptStrategyDiagramAction
* 작 업 자      : 형광민
* 작 업 일      : 2012년 9월 7일 
* 기    능      : 전략체계도
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    형광민      2012년 9월 7일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class DeptStrategyDiagramAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략체계도 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagramList(SearchMap searchMap) {
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	HashMap topScDept = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topScDept)) {
    		topScDept = new HashMap();
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findScDeptId = (String)searchMap.getString("findScDeptId", (String)topScDept.get("SC_DEPT_ID"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findScDeptNm = (String)searchMap.getString("findScDeptNm", (String)topScDept.get("SC_DEPT_NM"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	
    	/**********************************
         * 디폴트 조회조건 설정
         **********************************/
    	searchMap.put("findScDeptId", findScDeptId);
    	searchMap.put("findScDeptNm", findScDeptNm);

        return searchMap;
    }
    
    /**
     * 전략체계도 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagramList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.mon.deptStrategyDiagram.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략체계도 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagramModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.mon.deptStrategyDiagram.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 전략체계도 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagramProcess(SearchMap searchMap) {
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
     * 전략체계도 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.mon.deptStrategyDiagram.insertData", searchMap);
        
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
     * 전략체계도 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.mon.deptStrategyDiagram.updateData", searchMap);
	        
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
     * 전략체계도 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] deptStrategyDiagramIds = searchMap.getString("deptStrategyDiagramIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < deptStrategyDiagramIds.length; i++) {
	            searchMap.put("deptStrategyDiagramId", deptStrategyDiagramIds[i]);
	            returnMap = updateData("bsc.mon.deptStrategyDiagram.deleteData", searchMap);
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

    /**
     * 전략체계도 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap deptStrategyDiagramListExcel(SearchMap searchMap) {
    	String excelFileName = "전략체계도";
    	String excelTitle = "전략체계도 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("관점코드", "PERSPECTIVE_ID", "left"));
    	excelInfoList.add(new ExcelVO("CSF 코드", "STRATEGY_ID", "left"));
    	excelInfoList.add(new ExcelVO("CSF명", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO("KIND", "KIND", "left"));
    	excelInfoList.add(new ExcelVO("X1", "X1", "left"));
    	excelInfoList.add(new ExcelVO("Y1", "Y1", "left"));
    	excelInfoList.add(new ExcelVO("X2", "X2", "left"));
    	excelInfoList.add(new ExcelVO("Y2", "Y2", "left"));
    	excelInfoList.add(new ExcelVO("X3", "X3", "left"));
    	excelInfoList.add(new ExcelVO("Y3", "Y3", "left"));
    	excelInfoList.add(new ExcelVO("X4", "X4", "left"));
    	excelInfoList.add(new ExcelVO("Y4", "Y4", "left"));
    	excelInfoList.add(new ExcelVO("평가진행상태", "STATUS", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.deptStrategyDiagram.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 전략과제 상세
     * @param SearchMap
     * @return HashMap
     */
    public SearchMap strategyDetailList(SearchMap searchMap) {
    	return searchMap;
    }
}

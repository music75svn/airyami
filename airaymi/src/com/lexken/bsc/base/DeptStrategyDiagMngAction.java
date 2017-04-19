/*************************************************************************
* CLASS 명      : DeptStrategyDiagMngAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 26일 
* 기    능      : 전략체계도관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 26일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.StaticUtil;

public class DeptStrategyDiagMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략체계도관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagMngList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topScDept)) {
    		topScDept = new HashMap();
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findScDeptId = (String)searchMap.getString("findScDeptId", (String)topScDept.get("SC_DEPT_ID"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findScDeptNm = (String)searchMap.getString("findScDeptNm", (String)topScDept.get("SC_DEPT_NM"));	// 조직명이  없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findScDeptId", findScDeptId);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findScDeptNm", findScDeptNm);
    	
        return searchMap;
    }
    
    /**
     * 전략체계도관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagMngList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.deptStrategyDiagMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략체계도관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.deptStrategyDiagMng.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 전략체계도관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        String stMode = searchMap.getString("mode");

		/**********************************
         * 수정/배경화면 파일삭제처리
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("FILEDEL".equals(stMode)) {
        	searchMap = deleteBackgroundFile(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 배경화면 파일삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteBackgroundFile(SearchMap searchMap) {
        HashMap returnMap       = new HashMap();
        FileHelper csFileHelper = new FileHelper();
        
        try {
        	
            String year = searchMap.getString("year");
            String scDeptId = searchMap.getString("scDeptId");
            
            /**********************************
             * 파일경로 설정
             **********************************/
            FileConfig fileConfig		= FileConfig.getInstance();
    		String stRealRootPath       = fileConfig.getProperty("FILE_WEB_ROOT_PATH");  // D:/00.workspace/00.bsc_workspace/BSC/WebContent
    		String stBackgroundPath 	= fileConfig.getProperty("FILE_BACKGROUND_ROOT_PATH"); // /images/flash/VBOXML
    		//String saveFile = stRealRootPath + stBackgroundPath;

    		String	saveFile = "/package/kgshome/kgsDomain7" + stBackgroundPath; //운영반영 때만
    		
    		saveFile = saveFile + "/" + "VBOX_STRATEGY_BG" + year + scDeptId + ".jpg";
    		
    		csFileHelper.deleteFile(saveFile);
        
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
     * 전략체계도관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.base.deptStrategyDiagMng.insertData", searchMap);
        
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
     * 전략체계도관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String strategy		= searchMap.getString("strategy");
        String line			= searchMap.getString("line");
        
        String arr_strategy[] 	= strategy.split("/");
        String arr_line[] 		= line.split("/");
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("bsc.base.deptStrategyDiagMng.deleteData", searchMap, true);
	        
	        if(arr_strategy.length > 0) {
		        for (int i = 0; i < arr_strategy.length; i++) {
		    		if (arr_strategy[i].trim().length() > 0 ) {
		    			String arr_values[] = arr_strategy[i].trim().split(",");
		    			String strategy_id	= arr_values[0].trim();
		    			String kind     	= arr_values[1].trim();
		    			String x_pos     	= arr_values[2].trim();
		    			String y_pos     	= arr_values[3].trim();
	
		    			strategy_id	= strategy_id.substring(strategy_id.indexOf(':')+1);
		    			kind 		= kind.substring(kind.indexOf(':')+1);
		    			x_pos 		= x_pos.substring(x_pos.indexOf(':')+1);
		    			y_pos 		= y_pos.substring(y_pos.indexOf(':')+1);
		    			
		    			searchMap.put("strategyId", strategy_id);
		    			searchMap.put("kind", kind);
		    			searchMap.put("x1Pos", x_pos);
		    			searchMap.put("y1Pos", y_pos);
		    			
		    			returnMap = insertData("bsc.base.deptStrategyDiagMng.insertData", searchMap);
		    		}
		    	}
	        }
	        
	        if(arr_line.length > 0) {
	        	for (int i = 0; i < arr_line.length; i++) {
	        		if (arr_line[i].trim().length() > 0 ) {
	        			String arr_values[] = arr_line[i].trim().split(",");
	        			String strategy_id	= arr_values[0].trim()+i;
	        			String x_pos     	= arr_values[1].trim();
	        			String y_pos     	= arr_values[2].trim();
	        			String x2_pos     	= arr_values[3].trim();
	        			String y2_pos     	= arr_values[4].trim();
	        			String x3_pos     	= arr_values[5].trim();
	        			String y3_pos     	= arr_values[6].trim();
	        			String x4_pos     	= arr_values[7].trim();
	        			String y4_pos     	= arr_values[8].trim();
	        			
	        			strategy_id	= strategy_id.substring(strategy_id.indexOf(':')+1);
	        			x_pos 		= x_pos.substring(x_pos.indexOf(':')+1);
	        			y_pos 		= y_pos.substring(y_pos.indexOf(':')+1);
	        			x2_pos 		= x2_pos.substring(x2_pos.indexOf(':')+1);
	        			y2_pos 		= y2_pos.substring(y2_pos.indexOf(':')+1);
	        			x3_pos 		= x3_pos.substring(x3_pos.indexOf(':')+1);
	        			y3_pos 		= y3_pos.substring(y3_pos.indexOf(':')+1);
	        			x4_pos 		= x4_pos.substring(x4_pos.indexOf(':')+1);
	        			y4_pos 		= y4_pos.substring(y4_pos.indexOf(':')+1);
	        			
	        			searchMap.put("strategyId", strategy_id);
	        			searchMap.put("x1Pos", x_pos);
	        			searchMap.put("y1Pos", y_pos);
	        			searchMap.put("x2Pos", x2_pos);
	        			searchMap.put("y2Pos", y2_pos);
	        			searchMap.put("x3Pos", x3_pos);
	        			searchMap.put("y3Pos", y3_pos);
	        			searchMap.put("x4Pos", x4_pos);
	        			searchMap.put("y4Pos", y4_pos);
	        			
	        			returnMap = insertData("bsc.base.deptStrategyDiagMng.insertData", searchMap);
	        		}
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
     * 전략체계도관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] deptStrategyDiagMngIds = searchMap.getString("deptStrategyDiagMngIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < deptStrategyDiagMngIds.length; i++) {
	            searchMap.put("deptStrategyDiagMngId", deptStrategyDiagMngIds[i]);
	            returnMap = updateData("bsc.base.deptStrategyDiagMng.deleteData", searchMap);
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
     * 전략체계도관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap deptStrategyDiagMngListExcel(SearchMap searchMap) {
    	String excelFileName = "전략체계도관리";
    	String excelTitle = "전략체계도관리 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("CSF코드", "STRATEGY_ID", "left"));
    	excelInfoList.add(new ExcelVO("CSF명", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO("KIND", "KIND", "left"));
    	excelInfoList.add(new ExcelVO("X1", "X1", "left"));
    	excelInfoList.add(new ExcelVO("Y1", "Y1", "left"));
    	excelInfoList.add(new ExcelVO("평가진행상태", "STATUS", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.deptStrategyDiagMng.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 전략체계도관리 전략체계도 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptStrategyDiagMng_xml(SearchMap searchMap) {
    	
    	searchMap.addList("strategyList", getList("bsc.base.deptStrategyDiagMng.getList", searchMap));
    	searchMap.addList("metricList", getList("bsc.base.deptStrategyDiagMng.getMetricList", searchMap));
    	searchMap.addList("arrowList", getList("bsc.base.deptStrategyDiagMng.getArrowList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 전략체계도 배경화면 업로드 팝업창 호출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachDeptStrategyDiagBackground(SearchMap searchMap) {
    	
    	return searchMap;
    	
    }
    
    /**
     * 전략체계도 배경화면 업로드
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachDeptStrategyDiagBackgroundProcess(SearchMap searchMap) {
    	String findYear = (String)searchMap.getString("findYear");
    	String findScDeptId = (String)searchMap.getString("findScDeptId");
		
    	/**********************************
		 * 파일복사
		 **********************************/
    	searchMap.fileCopyBackground("/temp", "VBOX_STRATEGY_BG", findYear, findScDeptId);
        return searchMap;
    }
    
    
}

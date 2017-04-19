package com.lexken.man.metric;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.core.StaticFactory;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricAction extends CommonService {

	private static final long serialVersionUID = 1L;
	    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * KGS2020지표 목록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricList(SearchMap searchMap) {
    	
    	String findUseYn = searchMap.getString("findUseYn");
    	if("".equals(StaticUtil.nullToBlank(findUseYn))){
    		searchMap.addList("findUseYn", findUseYn);
    	}
    	
    	searchMap.addList("managementList", getList("man.metric.metric.getManagement", searchMap));
    	
    	searchMap.addList("stratSubjectList", getList("man.metric.metric.getStrStratSubjectList", searchMap));
    	
    	searchMap.addList("csfList", getList("man.metric.metric.getCsfList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * KGS2020지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("man.metric.metric.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap managementList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("managementList", getList("man.metric.metric.getManagement", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("man.metric.metric.getStrStratSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("man.metric.metric.getCsfList", searchMap));

        return searchMap;
    }
    
    
    /**
     *  상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	
    	/**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("man.metric.metric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 득점산식조회 
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("man.metric.metricGrp.scoreCalTypeList", searchMap));
        
        /**********************************
         * 평가구간대 조회 
         **********************************/
        searchMap.addList("evalSectionList", getList("man.metric.metric.evalSectionList", searchMap));
        
        
        /**********************************
         * 평가구간대 등급 조회 
         **********************************/
        searchMap.addList("gradeList", getList("man.metric.metric.gradeList", searchMap));
  
    	/**********************************
         * 측정주기월 조회 
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("man.metric.metric.regMonList", searchMap); //팩기지수정
        String[] monArray = new String[0]; 
        if(null != regMonList && 0 < regMonList.size()) {
        	monArray = new String[regMonList.size()];
        	for (int i = 0; i < regMonList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)regMonList.get(i);
				monArray[i] = (String)t.get("MON"); 
        	}
        }
        
        searchMap.addList("regMonDesc", monArray);
        HashMap detail = new HashMap();
        
    	if("MOD".equals(stMode)) {
    		
    		searchMap.addList("csfList1", getList("man.metric.metric.getCsfList1", searchMap));
    	//	searchMap.addList("fileList", getList("man.metric.metric.getFileList", searchMap));
    		
    		detail = getDetail("man.metric.metric.getDetail", searchMap);    	
    		searchMap.addList("detail", detail);
    		
    		  /**********************************
	         * KPI 산식명 조회 
	         **********************************/
	        String typeId = (String)detail.get("TYPE_ID");
	        
	        if("01".equals(typeId)) {
		        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
		        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
		        
		        if(null != calTypeColList && 0 < calTypeColList.size()) {
			        for (int i = 0; i < calTypeColList.size(); i++) {
			        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
						calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
					}
		        }
		        
		        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
		        		       
	        }
	        
    	}else{
    		
    		searchMap.addList("csfList1", getList("man.metric.metric.getCsfList1", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 이행성과지표관리 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricProcess(SearchMap searchMap) {
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
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/strMetric");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }else if("ADDEXCEL".equals(stMode)){
        	searchMap = insertExcelDB(searchMap);    
        }
        
        /**********************************
         * Return
         **********************************/
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
     * 난이도평가 평가단 관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String typeId = searchMap.getString("typeId");
        
        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        
        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");
        
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             *  채번 
             **********************************/
	        String manKpiId = getStr("man.metric.metric.getManKpiId", searchMap);
	        searchMap.put("manKpiId", manKpiId);
        
	        /**********************************
	         *  등록 
	         **********************************/
        	returnMap = insertData("man.metric.metric.insertData", searchMap);
        
        	
        	/**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	
	       
	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표 
		        returnMap = updateData("man.metric.metric.deleteRegMon", searchMap, true);
		        
		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("man.metric.metric.insertRegMon", searchMap);
			        }
		        }
	        }
	        
        	/**********************************
	         * 실적산식 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metric.deleteCalTypeCol", searchMap, true);

	        /**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(typeId)) { //정량지표 
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		           returnMap = insertData("man.metric.metric.insertCalTypeCol", searchMap);
		        }
	        }
	        
	        /**********************************
	         * 구간대 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metric.deleteEvalSection", searchMap, true);
	        
	        /**********************************
	         * 구간대 등록 
	         **********************************/
	        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
	        
	        if("01".equals(typeId)) { //정량지표
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) { 
			        for (int i = 0; i < evalSectionIds.length; i++) {
			            searchMap.put("evalSectionId", evalSectionIds[i]);
			            searchMap.put("fromValue", fromValues[i]);
			            searchMap.put("toValue", toValues[i]);
			            searchMap.put("conversionScore", conversionScores[i]);
			            returnMap = insertData("man.metric.metric.insertEvalSection", searchMap);
			        }
	        	}
	        }
	        
	        /**********************************
             * 권한설정 
             **********************************/
	        returnMap = insertAdmin(searchMap);
	        
        	
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
     * 지표관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        
        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");
        
        try {
	        setStartTransaction();
	       
	        /**********************************
	         * KPI 수정 
	         **********************************/
	        returnMap = updateData("man.metric.metric.updateData", searchMap);
	        
	        
	    	/**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	        String typeId = searchMap.getString("typeId");
	        
	        
	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표 
		        returnMap = updateData("man.metric.metric.deleteRegMon", searchMap, true);
		        
		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("man.metric.metric.insertRegMon", searchMap);
			        }
		        }
	        }
	        
	        
	        /**********************************
	         * 실적산식 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metric.deleteCalTypeCol", searchMap, true);

	        /**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(typeId)) { //정량지표 
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		           returnMap = insertData("man.metric.metric.insertCalTypeCol", searchMap);
		        }
	        }
	        
	        /**********************************
	         * 구간대 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metric.deleteEvalSection", searchMap, true);
	        
	        /**********************************
	         * 구간대 등록 
	         **********************************/
	        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
	        
	        if("01".equals(typeId)) { //정량지표
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) { 
			        for (int i = 0; i < evalSectionIds.length; i++) {
			            searchMap.put("evalSectionId", evalSectionIds[i]);
			            searchMap.put("fromValue", fromValues[i]);
			            searchMap.put("toValue", toValues[i]);
			            searchMap.put("conversionScore", conversionScores[i]);
			            returnMap = insertData("man.metric.metric.insertEvalSection", searchMap);
			        }
	        	}
	        }
	        
	          
	        /**********************************
	         * 권한설정
	         **********************************/
			returnMap = insertAdmin(searchMap);
			
	        /**********************************
	         * 첨부파일 정보 등록
	         **********************************/
	     //   returnMap = insertFileInfo(searchMap);
	        
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
     * 지표권한등록
     * 08 : 담당자(목표/실적입력자)
     * 09 : 확인자(목표/실적확인자)
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertAdmin(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	
        	/**********************************
             * 기존 권한삭제 
             **********************************/
 			returnMap = updateData("man.metric.metric.deleteAdmin", searchMap, true);
	        
	        /**********************************
             * KPI담당자(실적입력자) 입력 
             **********************************/
 			returnMap = updateData("man.metric.metric.insertChargeUserAdmin", searchMap, true);
	        
	        /**********************************
             * 실적확인자 입력 
             **********************************/
 			returnMap = updateData("man.metric.metric.insertApproveUserAdmin", searchMap, true);
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }
    
    /**
     * 첨부파일 정보등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = deleteData("man.metric.metric.deleteFileInfo", searchMap);
	        	}
        	}
        	
        	/**********************************
             * 첨부파일 등록
             **********************************/
        	ArrayList fileInfoList = new ArrayList();
        	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();
	        
	        if(null != fileInfoList) {
				for(int i = 0; i < fileInfoList.size(); i++){
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					if(fileInfoVO != null){
						searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
						searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
						searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("man.metric.metric.insertFileInfo", searchMap);
					}
				}
	        }
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }
    
    /**
     * KGS2020지표 관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] manKpiIds = searchMap.getString("manKpiIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != manKpiIds && 0 < manKpiIds.length) {
		        for (int i = 0; i < manKpiIds.length; i++) {		       
		            searchMap.put("manKpiId", manKpiIds[i]);
		            returnMap = updateData("man.metric.metric.deleteData", searchMap);
		        }
		    }
	        
	        /******************************************
             * 권한설정
             ******************************************/
	        returnMap = insertAdmin(searchMap);
	        
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
     * 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricListExcel(SearchMap searchMap) {
    	String excelFileName = "KGS2020KPI";
    	String excelTitle = "KGS2020KPI 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("findStratSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("세부과제", StaticUtil.nullToDefault((String)searchMap.get("findSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", StaticUtil.nullToDefault((String)searchMap.get("findUseYnNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("경영목표", "MAN_NM", "left","MANAGEMENT_CNT"));
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_NM", "left","STRAT_CNT"));
    	excelInfoList.add(new ExcelVO("CSF", "CSF_NM", "left","SUB_CNT"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI", "MAN_KPI_NM", "left"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI", "MAN_KPI_ID", "left"));
    	excelInfoList.add(new ExcelVO("담당부서", "INSERT_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "CYCLE_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("중요도", "IMPORTANCE", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.metric.metric.getList", searchMap));
    	
        return searchMap;
    }
   
    /**
     * 엑셀업로드 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 엑셀로딩관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertExcelDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        ArrayList excelDataList = new ArrayList();

        try {
        	setStartTransaction();
        	String year=(String)searchMap.get("findYear");
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlManMetircImportanceUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strManKpiId = (String[]) excelDataList.get(0);
        		String[] strImportance = (String[]) excelDataList.get(1);
                

                 for(int i=0; i<strManKpiId.length; i++){
                	if(null != strManKpiId[i]){
                		searchMap.put("year", year);
                		searchMap.put("manKpiId", strManKpiId[i]);
                		searchMap.put("importance", strImportance[i]);
                	
                		returnMap = insertData("man.metric.metric.updateExcelData", searchMap);
                	}
                }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 지표풀 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricGrpSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표풀 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpList_xml(SearchMap searchMap) {

    	String findMetricGrpNm=(String)searchMap.get("findMetricGrpNm");
    	
    	searchMap.addList("list", getList("man.metric.metric.getManKpiGrpList", searchMap));

        return searchMap;
    }
    
    
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpData_ajax(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.metric.metric.getMetricGrpDataAjax", searchMap));
        
     //   searchMap.addList("listMon", getList("man.metric.metric.regGrpMonList", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 산식컬럼
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpCalTypeCol_ajax(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.metric.metric.getMetricGrpCalTypeColAjax", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpEvalSectionGrp_ajax(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.metric.metric.getMetricGrpEvalSectionGrpAjax", searchMap));

        return searchMap;
    }
    
 

}


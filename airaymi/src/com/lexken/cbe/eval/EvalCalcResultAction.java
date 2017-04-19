/*************************************************************************
* CLASS 명      : EvalCalcResultAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 12월 11일 
* 기    능      : 종합평가집계
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 12월 11일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.eval;
    
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class EvalCalcResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 종합평가집계 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCalcResultList(SearchMap searchMap) {
    	
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));
    	
    	String findEvalDegreeId = searchMap.getString("findEvalDegreeId");
    	if( null == findEvalDegreeId || "".equals(findEvalDegreeId) ){
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	searchMap.addList("rivalGrpList", getList("cbe.eval.evalRival.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 종합평가집계 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCalcResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalCalcResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 종합평가집계 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCalcResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalCalcResult.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 종합평가집계 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCalcResultProcess(SearchMap searchMap) {
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
        } else if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 종합평가집계 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalCalcResult.insertData", searchMap);
        
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
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	if("SP_CBE_CONBI_SCORE".equals(searchMap.get("procId"))){
        		returnMap = insertData("cbe.eval.evalCalcResult.callSpCBEConvertScore", searchMap);
        	}else if("SP_CBE_CONBI_GRADE".equals(searchMap.get("procId"))){
        		returnMap = insertData("cbe.eval.evalCalcResult.callSpCBEConvertGrade", searchMap);
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
     * 종합평가집계 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalCalcResult.updateData", searchMap);
	        
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
     * 종합평가집계 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalCalcResultListExcel(SearchMap searchMap) {
    	String excelFileName = "종합평가집계";
    	String excelTitle = "종합평가집계 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("경쟁그룹", (String)searchMap.get("rivalGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("성명", (String)searchMap.get("findUserNm")));
    	excelSearchInfoList.add(new ExcelVO("평가조직", (String)searchMap.get("findScDeptNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("가중치그룹", "WEIGHT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("경쟁그룹", "RIVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("성명", "NAME", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("사업소조직", "ITEM_FRS_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("2차사업소조직", "ITEM_SEC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("팀조직", "ITEM_TEAM_NM", "left"));
    	excelInfoList.add(new ExcelVO("사업소환산점수", "FRS_DEPT_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("2차사업소환산점수", "SEC_DEPT_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("팀환산점수", "TEAM_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("개인평가환산점수", "PRS_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("종합평가 점수", "CONBI_EVAL_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("종합평가 순위", "CONBI_EVAL_RANK", "center"));
    	excelInfoList.add(new ExcelVO("종합평가 등급", "CONBI_EVAL_GRADE", "center"));
    	excelInfoList.add(new ExcelVO("종합평가 확정 등급", "CONBI_EVAL_FIX_GRADE", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalCalcResult.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    /**
     * 엑셀업로딩용 다운로드 등록
     * @param      
     * @return String  
     * @throws 
     */
    
    public SearchMap evalCalcUploadFormExcel(SearchMap searchMap) {
    	String excelFileName = "종합평가집계";
    	String excelTitle = "종합평가집계 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm") + "[" + (String)searchMap.get("year") + "]" ));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm") + "[" + (String)searchMap.get("evalDegreeId") + "]" ));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("사번", "EVAL_MEMB_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("성명", "NAME", "left"));
    	
    	excelInfoList.add(new ExcelVO("평가구분", "EVAL_METHOD_GBN_NM", "left"));
    	
    	excelInfoList.add(new ExcelVO("가중치그룹", "WEIGHT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("경쟁그룹", "RIVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "JIKGUB_NM", "left"));
    	
    	excelInfoList.add(new ExcelVO("사업소조직", "ITEM_FRS_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("2차사업소조직", "ITEM_SEC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("팀조직", "ITEM_TEAM_NM", "left"));
    	
    	excelInfoList.add(new ExcelVO("사업소 조직점수", "FRS_DEPT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("2차사업소 조직점수", "SEC_DEPT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("팀 조직점수", "TEAM_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("개인평가 등급", "PRS_GRADE", "right"));
    	
    	excelInfoList.add(new ExcelVO("사업소 환산점수", "FRS_DEPT_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("2차사업소 환산점수", "SEC_DEPT_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("팀 환산점수", "TEAM_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("개인평가 환산점수", "PRS_CONVERT_SCORE", "right"));
    	
    	excelInfoList.add(new ExcelVO("사업소 가중치", "FRS_DEPT_WEIGHT", "right"));
    	excelInfoList.add(new ExcelVO("2차사업소 가중치", "SEC_DEPT_WEIGHT", "right"));
    	excelInfoList.add(new ExcelVO("팀 가중치", "TEAM_WEIGHT", "right"));
    	excelInfoList.add(new ExcelVO("개인평가 가중치", "PRS_WEIGHT", "right"));
    	
    	excelInfoList.add(new ExcelVO("사업소 가중치반영점수", "FRS_DEPT_WT_REFL_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("2차사업소 가중치반영점수", "SEC_DEPT_WT_REFL_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("팀 가중치반영점수", "TEAM_WT_REFL_CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("개인평가 가중치반영점수", "PRS_WT_REFL_CONVERT_SCORE", "right"));
    	
    	excelInfoList.add(new ExcelVO("종합평가 점수", "CONBI_EVAL_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("종합평가 순위", "CONBI_EVAL_RANK", "center"));
    	excelInfoList.add(new ExcelVO("종합평가 등급", "CONBI_EVAL_GRADE", "center"));
    	excelInfoList.add(new ExcelVO("종합평가 확정 등급", "CONBI_EVAL_FIX_GRADE", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalCalcResult.getCalcUploadFormExcel", searchMap));
    	
        return searchMap;
    }
    
    
    
    
    /**
     * 엑셀업로딩관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    
    public SearchMap excelUploadProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/excelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = cbeFixGradeUpExcel(searchMap);
        }  
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 등급배분표 엑셀업로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap cbeFixGradeUpExcel(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	int evalUserCol = 0;
        	int fixGradeCol = 16;
        	
        	searchMap.put("evalUserCol", evalUserCol);
        	searchMap.put("fixGradeCol", fixGradeCol);
        	
        	excelDataList = execlUpload(searchMap, returnMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";
            	
            	String upYear = "";
            	String upEvalDegreeId = "";
        		
        		String[] strUserList = (String[]) excelDataList.get(0);
                String[] strFixGradeList = (String[]) excelDataList.get(1);
                
                
                for(int i=0; i< strUserList.length ; i++) {
                	
                	if( 0 == i ){ // 평가년도  설정  부분
                		tmpVal = strUserList[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upYear = tmpVal.substring(startIdx + 1, endIdx);
                		
                		searchMap.put("year", upYear);
                    	
                	}else if( 1 == i ){ // 평가구분  설정  부분
                		tmpVal = strUserList[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upEvalDegreeId = tmpVal.substring(startIdx + 1, endIdx);
                		
                		searchMap.put("evalDegreeId", upEvalDegreeId);
                    	
                	}else if( 2 <= i ){ // 대상자들의 확정등급 등록 부분
                		
	                	if(!"".equals(StaticUtil.nullToBlank(strFixGradeList[i])) && !"".equals(StaticUtil.nullToBlank(strFixGradeList[i]))) {
		                	searchMap.put("evalMembUserId", strUserList[i]);
		                	searchMap.put("conbiEvalFixGrade", strFixGradeList[i]);
		                	
		                	returnMap = insertData("cbe.eval.evalCalcResult.updateFixGradeData", searchMap);
		                	inputValCnt++;
	                	}
                	}
                }
        	}
        	
        	/**********************************
             * 로그등록
             **********************************/
            searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);
        	
            /**********************************
             * 등록 실패시 작업
             **********************************/
            Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
    				setRollBackTransaction();
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
    
    
    
    /*************************************************
	* excel 실적 업로드 
	**************************************************/
	public ArrayList execlUpload(SearchMap searchMap, HashMap returnMap) throws Exception {
		ArrayList list = new ArrayList();
		
		FileConfig fileConfig = FileConfig.getInstance();
		
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자
		
		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlParsing2(searchMap, returnMap);
			} else { //엑셀2003
				list = execlParsing(searchMap, returnMap);
			}
		}
		
		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlParsing2(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();
			
		int fixGradeCol = searchMap.getInt("fixGradeCol");
		int evalUserCol = searchMap.getInt("evalUserCol");
		
		int rowCnt = 0;
	    
	    boolean isDataExist = false; //데이터 존재확인변수
	    
	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);
	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	String[] strUserList = new String[rowCnt-1];
	       	String[] strGradeList = new String[rowCnt-1];
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);
	       		
	       		XSSFCell currUserCol = currRow.getCell((short)evalUserCol);
       			if( null != currUserCol ){
	       			if( currUserCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strUserList[i-1] = String.valueOf(currUserCol.getNumericCellValue()); 
	       			}else if( currUserCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strUserList[i-1] = String.valueOf(currUserCol.getStringCellValue()); 
	       			} 
	       			
	       			XSSFCell currGradeCol = currRow.getCell((short)fixGradeCol);
	       			if( currGradeCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strGradeList[i-1] = String.valueOf(currGradeCol.getNumericCellValue()); 
	       			}else if( currGradeCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strGradeList[i-1] = String.valueOf(currGradeCol.getStringCellValue()); 
	       			} 
       			}
	       	}
	       	
	       	list.add(strUserList);
       		list.add(strGradeList);
		} catch(Exception e) {
			e.printStackTrace();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}
		
		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2003)
	**************************************************/
	public ArrayList execlParsing(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();
		
		int fixGradeCol = searchMap.getInt("fixGradeCol");
		int evalUserCol = searchMap.getInt("evalUserCol");
		
	    int rowCnt = 0;
	    
	    boolean isDataExist = false; //데이터 존재확인변수
	    
	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);
	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	       	
	       	String[] strUserList = new String[rowCnt-1];
	       	String[] strGradeList = new String[rowCnt-1];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		HSSFRow currRow = sheet.getRow(i);
	       			
       			HSSFCell currUserCol = currRow.getCell((short)evalUserCol);
       			if( null != currUserCol ){
	       			if( currUserCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strUserList[i-1] = String.valueOf(currUserCol.getNumericCellValue()); 
	       			}else if( currUserCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strUserList[i-1] = String.valueOf(currUserCol.getStringCellValue()); 
	       			} 
	       			
	       			HSSFCell currGradeCol = currRow.getCell((short)fixGradeCol);
	       			if( currGradeCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strGradeList[i-1] = String.valueOf(currGradeCol.getNumericCellValue()); 
	       			}else if( currGradeCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strGradeList[i-1] = String.valueOf(currGradeCol.getStringCellValue()); 
	       			} 
       			}
	       			
	       	}
	       	
	       	list.add(strUserList);
       		list.add(strGradeList);
		} catch(Exception e) {
			e.printStackTrace();
			searchMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			searchMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}
		
		return list;
	}    
    
    
}

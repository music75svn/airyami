/*************************************************************************
* CLASS 명      : EvalGradeDistriAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 22일 
* 기    능      : 등급배분표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 22일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.common;
    
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

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

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class EvalGradeDistriAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 등급배분표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriList(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("year", (String)searchMap.get("findYear"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalMethodId")))) {
			searchMap.put("findEvalMethodId", (String)searchMap.get("evalMethodId"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("evalMethodId")))) {
			searchMap.put("evalMethodId", (String)searchMap.get("findEvalMethodId"));
		}
    	
    	searchMap.addList("evalMethodDetail", getDetail("bsc.common.evalGrade.getDetail", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 등급배분표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.common.evalGradeDistri.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 등급배분표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("evalMethodDetail", getDetail("bsc.common.evalGrade.getDetail", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.common.evalGradeDistri.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    
    
    /**
     * 등급배분표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriTabMngList(SearchMap searchMap) {
    	
    	
    	String year = searchMap.getString("findYear");
    	String evalMethodId = searchMap.getString("findEvalMethodId");
    	
    	
    	searchMap.put("year", year);
    	searchMap.put("evalMethodId", evalMethodId);
    	searchMap.addList("evalMethodDetail", getDetail("bsc.common.evalGrade.getDetail", searchMap));
    	
    	searchMap.addList("itemList", getList("bsc.common.evalGradeDistri.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 등급배분표 현황조회 화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriTabMngList_xml(SearchMap searchMap) {
    	
    	
    	/**********************************
         * 등급배분표 등급 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 등급배분표 상세내용 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.common.evalGradeDistri.getList", searchMap));
        
        searchMap.addList("list", getList("bsc.common.evalGradeDistri.evalGradeDistriTabMngList", searchMap));
    	
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
     * 등급배분표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGradeDistriProcess(SearchMap searchMap) {
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
     * 등급배분표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.common.evalGradeDistri.insertData", searchMap);
        
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
     * 등급배분표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.common.evalGradeDistri.updateData", searchMap);
	        
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
     * 등급배분표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalMethodIds = searchMap.getString("evalMethodIds").split("\\|", 0);
			String[] gradeItemIds = searchMap.getString("gradeItemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalMethodIds && 0 < evalMethodIds.length) {
		        for (int i = 0; i < evalMethodIds.length; i++) {
		            searchMap.put("evalMethodId", evalMethodIds[i]);
			searchMap.put("gradeItemId", gradeItemIds[i]);
		            returnMap = updateData("bsc.common.evalGradeDistri.deleteData", searchMap);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("gradeItemNm"), "등급항목 명", 1, 33);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("gradeItemRate"), "등급항목 비율");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("gradeItemScore"), "등급항목 점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 등급배분표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalGradeDistriListExcel(SearchMap searchMap) {
    	String excelFileName = "등급배분표";
    	String excelTitle = "등급배분표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가방법 코드", "EVAL_METHOD_ID", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 코드", "GRADE_ITEM_ID", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 명", "GRADE_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 비율", "GRADE_ITEM_RATE", "left"));
    	excelInfoList.add(new ExcelVO("등급항목 점수", "GRADE_ITEM_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    /**
     * 등급배분표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalGradeDistriTabMngListExcel(SearchMap searchMap) {
    	String excelFileName = "등급배분표 상세";
    	String excelTitle = "등급배분표 상세";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm") + "[" + (String)searchMap.get("year") + "]" ));
    	excelSearchInfoList.add(new ExcelVO("평가방법", (String)searchMap.get("evalMethodNm") + "[" + (String)searchMap.get("evalMethodId") + "]" ));
    	
    	
    	ArrayList itemList = (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap);
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가대상수", "ITEM_CNT", "left"));
    	
    	if( null != itemList && 0 < itemList.size() ){
    		for(int j = 0;j < itemList.size(); j++) {
				HashMap imap = (HashMap)itemList.get(j);
				String colNm = (String)imap.get("GRADE_ITEM_NM") + "[" + (String)imap.get("GRADE_ITEM_ID") + "]";
				excelInfoList.add(new ExcelVO( colNm, (String)imap.get("GRADE_ITEM_ID"), "center"));
			}
    	}
    	
    	
    	ArrayList iqueryItemList = (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap);
    	String[] itemArray = new String[0]; 
    	if(null != iqueryItemList && 0 < iqueryItemList.size()) {
    		itemArray = new String[iqueryItemList.size()];
    		for(int i=0; i<iqueryItemList.size(); i++) {
    			HashMap map = (HashMap)iqueryItemList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.common.evalGradeDistri.evalGradeDistriTabMngList", searchMap));
    	
    	
        return searchMap;
    }
    
    
    
    /**
     * 엑셀로딩관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelUploadProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        /*
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        */
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/excelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = evalGradeDistriTabMngUpExcel(searchMap);
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
    public SearchMap evalGradeDistriTabMngUpExcel(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	/**********************************
             * 등급배분표 등급 가져오기
             **********************************/
        	String year = searchMap.getString("findYear");
        	String evalMethodId = searchMap.getString("findEvalMethodId");
        	
        	searchMap.put("year", year);
        	searchMap.put("evalMethodId", evalMethodId);
        	ArrayList itemList = (ArrayList)getList("bsc.common.evalGradeDistri.getList", searchMap);
        	int colCnt = itemList.size();
        	searchMap.put("colCnt", colCnt+"" );
        	
        	excelDataList = execlUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";
            	
            	String upYear = "";
            	String upEvalMethodId = "";
            	String[] upGradeItemId = new String[ colCnt ]; 
        		
        		String[] strCode = (String[]) excelDataList.get(0);
                String[][] strValue = (String[][]) excelDataList.get(1);
                
                
                for(int i=0; i< strCode.length ; i++) {
                	
                	if( 0 == i ){ // 평가년도  설정  부분
                		tmpVal = strCode[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upYear = tmpVal.substring(startIdx + 1, endIdx);
                		
                		searchMap.put("year", upYear);
                    	
                	}else if( 1 == i) { // 평가방법 코드  설정  부분
                		tmpVal = strCode[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upEvalMethodId = tmpVal.substring(startIdx + 1, endIdx);
                		
                		searchMap.put("evalMethodId", upEvalMethodId);
                		
                		/**********************************
                         * 기존 등록된 실적 삭제
                         **********************************/
                        returnMap = insertData("bsc.common.evalGradeDistri.deleteTabMngData", searchMap);
                	}else if( 2 == i) { // 등급항목 설정  부분
                		for(int j = 0 ; j < colCnt  ; j++ ){
                			tmpVal = strValue[i][j];
                    		int startIdx = tmpVal.lastIndexOf("[");
                    		int endIdx = tmpVal.lastIndexOf("]");
                    		upGradeItemId[j] = tmpVal.substring(startIdx + 1, endIdx);
                		}
                		
                	}else if( 3 <= i ){ // 등급표 부분
	                	for(int j = 0 ; j < colCnt ; j++ ){
		                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i][j]))) {
			                	searchMap.put("itemCnt", strCode[i]);
			                	searchMap.put("gradeItemId", upGradeItemId[j] );
			                	searchMap.put("itemDistriCnt", strValue[i][j]);
			                	
			                	returnMap = insertData("bsc.common.evalGradeDistri.insertTabMngData", searchMap);
			                	inputValCnt++;
		                	}
	                	}
                	}
                	
                }
        	}
        	
        	/**********************************
             * 로그등록
             **********************************/
            searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);
            //returnMap = deleteData("bsc.tam.newjcs.deleteLog", searchMap, true);
            //returnMap = insertData("bsc.tam.newjcs.insertLog", searchMap);
        	
            /**********************************
             * 등록 실패시 작업
             **********************************/
            Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            //returnMap = deleteData("bsc.tam.newjcs.deleteLog", searchMap, true, true);
    	            //returnMap = insertData("bsc.tam.newjcs.insertLog", searchMap, true, true);
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
	public ArrayList execlUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();
		
		FileConfig fileConfig = FileConfig.getInstance();
		
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자
		
		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = excelParsing2(searchMap);
			} else { //엑셀2003
				list = execlParsing(searchMap);
			}
		}
		
		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList excelParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();
			
	    int colCnt = Integer.parseInt((String)searchMap.get("colCnt"));
		
		int rowCnt = 0;
	    int columnCnt = 3;
	    
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
	       	
	       	String[] strCode = new String[rowCnt-4];
	       	String[][] strValue = new String[rowCnt-4][ colCnt ];
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);
	       		
	       		int col_cnt = columnCnt;
	       		
	       		if(columnCnt > currRow.getLastCellNum()) { 
	       			col_cnt = currRow.getLastCellNum();
	       		}
	       			
	       		for(int j = 0; j <= col_cnt; j++) { //열
	       			XSSFCell currCol = currRow.getCell((short)j);
	       			
	       			try {
		       			switch(currCol.getCellType()) {
		       			case XSSFCell.CELL_TYPE_NUMERIC:
		       				if(j == 0) {
		       					strCode[i-1] = String.valueOf(currCol.getNumericCellValue()); //code값
		       				} else {
		       					strValue[i-1][j-1] = String.valueOf(currCol.getNumericCellValue()); //실적값
		       				}
		       				break;
		       			case XSSFCell.CELL_TYPE_STRING:
		       				if(j == 0) {
		       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
		       				} else {
		       					strValue[i-1][j-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
		       				}
		       				break;
		       			default:
		   					break;
		       			}
		       		}catch (Exception e) {
		       				e.printStackTrace();
		       		}
	       		}
	       	}
	       	
	       	list.add(strCode);
       		list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2003)
	**************************************************/
	public ArrayList execlParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();
		
		int colCnt = Integer.parseInt((String)searchMap.get("colCnt"));
		
	    int rowCnt = 0;
	    int chkVal      = 1;
	    
	    boolean isDataExist = false; //데이터 존재확인변수
	    
	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);
	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	       	
	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt ];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		HSSFRow currRow = sheet.getRow(i);
	       			
	       		for(int j = 0; j <= colCnt; j++) { //열
	       			HSSFCell currCol = currRow.getCell((short)j);
	       		
	       			try {
		       			switch(currCol.getCellType()) {
		       			case HSSFCell.CELL_TYPE_NUMERIC:
		       				if(j == 0) {
		       					strCode[i-1] = String.valueOf(currCol.getNumericCellValue()); //code값
		       				} else {
		       					strValue[i-1][j-1] = String.valueOf(currCol.getNumericCellValue()); //실적값
		       				}
		       				break;
		       			case HSSFCell.CELL_TYPE_STRING:
		       				if(j == 0) {
		       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
		       				} else {
		       					strValue[i-1][j-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
		       				}
		       				break;
		       			default:
		       				break;
		       			}
	       			} catch (Exception e) {
	       				e.printStackTrace();
	       			}
	       		}
	       	}
	       	
	       	list.add(strCode);
       		list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
    
    
	/**
     * 등급배분표 팝업 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popDistriTabList(SearchMap searchMap) {
    	
    	searchMap.addList("evalMethodDetail", getDetail("bsc.common.evalGrade.getDetail", searchMap));

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalMethodId")))) {
			searchMap.put("findEvalMethodId", (String)searchMap.get("evalMethodId"));
		}
    	
    	searchMap.addList("itemList", getList("bsc.common.evalGradeDistri.getList", searchMap));
    	
    	return searchMap;
    }
	
	
    
}

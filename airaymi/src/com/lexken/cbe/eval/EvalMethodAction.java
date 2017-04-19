/*************************************************************************
* CLASS 명      : EvalMethodAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 7일 
* 기    능      : 평가환산방법
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 7일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.eval;
    
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
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

public class EvalMethodAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가환산방법 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMethodList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가환산방법 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMethodList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalMethod.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가환산방법 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMethodModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	// 평가방법
    	searchMap.addList("evalMethodlist", getList("cbe.eval.evalMethod.getEvalMethodList", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalMethod.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    
    
    /**
     * 조직평가환산방법 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptRankReflModify(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 조직평가환산방법 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptRankReflModify_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalMethod.evalDeptRankReflList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가환산방법 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMethodProcess(SearchMap searchMap) {
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
     * 평가환산방법 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalMethod.insertData", searchMap);
        
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
     * 평가환산방법 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalMethod.deleteData", searchMap, true);
	        
	        returnMap = insertData("cbe.eval.evalMethod.insertData", searchMap);
	        
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
     * 평가환산방법 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalGbnIds = searchMap.getString("evalGbnIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalGbnIds && 0 < evalGbnIds.length) {
		        for (int i = 0; i < evalGbnIds.length; i++) {
		            searchMap.put("evalGbnId", evalGbnIds[i]);
		            returnMap = updateData("cbe.eval.evalMethod.deleteData", searchMap);
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
     * 평가환산방법 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMethodListExcel(SearchMap searchMap) {
    	String excelFileName = "평가환산방법";
    	String excelTitle = "평가환산방법 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("코드", "CODE_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드명", "CODE_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법 명", "EVAL_METHOD_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalMethod.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    
    /**
     * 조직순위별 점수환산표  엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalDeptRankReflModifyExcel(SearchMap searchMap) {
    	String excelFileName = "조직순위별 점수환산표";
    	String excelTitle = "조직순위별 점수환산표";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm") + "[" + (String)searchMap.get("year") + "]" ));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("순위", "RANK_CNT", "left"));
    	excelInfoList.add(new ExcelVO("2", "CNT_02", "right"));
    	excelInfoList.add(new ExcelVO("3", "CNT_03", "right"));
    	excelInfoList.add(new ExcelVO("4", "CNT_04", "right"));
    	excelInfoList.add(new ExcelVO("5", "CNT_05", "right"));
    	excelInfoList.add(new ExcelVO("6", "CNT_06", "right"));
    	excelInfoList.add(new ExcelVO("7", "CNT_07", "right"));
    	excelInfoList.add(new ExcelVO("8", "CNT_08", "right"));
    	excelInfoList.add(new ExcelVO("9", "CNT_09", "right"));
    	excelInfoList.add(new ExcelVO("10", "CNT_10", "right"));
    	excelInfoList.add(new ExcelVO("11", "CNT_11", "right"));
    	excelInfoList.add(new ExcelVO("12", "CNT_12", "right"));
    	excelInfoList.add(new ExcelVO("13", "CNT_13", "right"));
    	excelInfoList.add(new ExcelVO("14", "CNT_14", "right"));
    	excelInfoList.add(new ExcelVO("15", "CNT_15", "right"));
    	excelInfoList.add(new ExcelVO("16", "CNT_16", "right"));
    	excelInfoList.add(new ExcelVO("17", "CNT_17", "right"));
    	excelInfoList.add(new ExcelVO("18", "CNT_18", "right"));
    	excelInfoList.add(new ExcelVO("19", "CNT_19", "right"));
    	excelInfoList.add(new ExcelVO("20", "CNT_20", "right"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalMethod.evalDeptRankReflList", searchMap));
    	
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
            searchMap = cbeDeptRankReflUpExcel(searchMap);
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
    public SearchMap cbeDeptRankReflUpExcel(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	/**********************************
             * 조직순위별 점수환산표 
             **********************************/
        	int deptCnt = 20;
        	searchMap.put("DEPT_CNT", deptCnt + "" );
        	
        	excelDataList = execlUpload(searchMap, returnMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";
            	
            	String upYear = "";
            	String upEvalMethodId = "";
            	String[] deptCntArr = new String[ deptCnt - 1 ]; 
        		
        		String[] strCode = (String[]) excelDataList.get(0);
                String[][] strValue = (String[][]) excelDataList.get(1);
                
                
                for(int i=0; i< strCode.length ; i++) {
                	
                	if( 0 == i ){ // 평가년도  설정  부분
                		tmpVal = strCode[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upYear = tmpVal.substring(startIdx + 1, endIdx);
                		
                		searchMap.put("year", upYear);
                    	
                	}else if( 1 == i) { // 조직수 설정  부분
                		for(int j = 0 ; j < deptCnt - 1 ; j++ ){
                			deptCntArr[j] = strValue[i][j];
                		}
                		returnMap = updateData("cbe.eval.evalMethod.deleteDetpRankMngData", searchMap, true);
                	}else if( 2 <= i ){ // 조직순위별 점수환산표  부분
                		
	                	for(int j = 0 ; j < deptCnt - 1; j++ ){
		                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i][j]))) {
			                	searchMap.put("rank", strCode[i]);
			                	searchMap.put("scDeptCnt", deptCntArr[j] );
			                	searchMap.put("convertScore", strValue[i][j]);
			                	
			                	returnMap = insertData("cbe.eval.evalMethod.insertDeptRankMngData", searchMap);
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
			
		int colCnt = Integer.parseInt((String)searchMap.get("DEPT_CNT"));
		
	    int rowCnt = 0;
	    
	    boolean isDataExist = false; //데이터 존재확인변수
	    
	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);
	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	       	
	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt - 1 ];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);
	       			
	       		for(int j = 0; j <= colCnt - 1; j++) { //열
	       			XSSFCell currCol = currRow.getCell((short)j);
	       		
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
	       		}
	       	}

	       	
	       	list.add(strCode);
       		list.add(strValue);
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
		
		int colCnt = Integer.parseInt((String)searchMap.get("DEPT_CNT"));
		
	    int rowCnt = 0;
	    
	    boolean isDataExist = false; //데이터 존재확인변수
	    
	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);
	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	       	
	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt - 1 ];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		HSSFRow currRow = sheet.getRow(i);
	       			
	       		for(int j = 0; j <= colCnt - 1; j++) { //열
	       			HSSFCell currCol = currRow.getCell((short)j);
	       		
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
	       		}
	       	}
	       	
	       	list.add(strCode);
       		list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
			searchMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			searchMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}
		
		return list;
	}    
    
    
    
}


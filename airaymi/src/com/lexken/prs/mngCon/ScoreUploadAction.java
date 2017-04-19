/*************************************************************************
* CLASS 명      : ScoreUploadAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 4월 8일 
* 기    능      : 업무성과점수 업로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 4월 8일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mngCon;
    
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

public class ScoreUploadAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무성과점수 업로드 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scoreUploadList(SearchMap searchMap) {
    	searchMap.addList("evalGrpList", getList("prs.mngCon.scoreUpload.getEvalGrpId", searchMap));
    	searchMap.addList("evalItemList", getList("prs.mngCon.scoreUpload.getEvalItemId", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 업무성과점수 업로드 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scoreUploadList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.mngCon.scoreUpload.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무성과점수 업로드 평가군
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scoreUploadEvalGrp_ajax(SearchMap searchMap) {
    	searchMap.addList("evalGrpList", getList("prs.mngCon.scoreUpload.getEvalGrpId", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 업무성과점수 업로드 평가항목
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scoreUploadEvalItem_ajax(SearchMap searchMap) {
    	searchMap.addList("evalItemList", getList("prs.mngCon.scoreUpload.getEvalItemId", searchMap));
    	
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
     * 업무성과점수 업로드 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scoreUploadProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/scoreExcelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = mngConScoreUploadExcel(searchMap);
        }  
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     * 업무성과점수 업로드 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap scoreUploadListExcel(SearchMap searchMap) {
        String excelFileName = "업무성과점수 업로드";
        String excelTitle = "업무성과점수 업로드";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm") + "[" + (String)searchMap.get("year") + "]" )); 
        excelSearchInfoList.add(new ExcelVO("평가군", (String)searchMap.get("evalGrpNm") + "[" + (String)searchMap.get("evalGrpId") + "]" )); 
        excelSearchInfoList.add(new ExcelVO("평가항목", (String)searchMap.get("evalItemNm") + "[" + (String)searchMap.get("evalItemId") + "]" ));
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("부서명", "DEPT_FULL_NM", "left"));
        excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가항목코드", "EVAL_ITEM_ID", "center"));
    	excelInfoList.add(new ExcelVO("평가항목명", "EVAL_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        
        searchMap.put("findEvalGrpId", (String)searchMap.get("evalGrpId"));
        searchMap.put("findEvalItemId", (String)searchMap.get("evalItemId"));
        searchMap.put("excelDataList", (ArrayList)getList("prs.mngCon.scoreUpload.getList", searchMap));
        
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
		searchMap.fileCopy("/temp", "/scoreExcelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = mngConScoreUploadExcel(searchMap);
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
    public SearchMap mngConScoreUploadExcel(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        //int inputValCnt = 0;
        try {
        	setStartTransaction();
        	
        	excelDataList = execlUpload(searchMap);
        	
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strDeptCd 	= (String[]) excelDataList.get(0);
        		String[] strEmpn 	= (String[]) excelDataList.get(1);
        		String[] strItemId 	= (String[]) excelDataList.get(2);
        		String[] strScore 	= (String[]) excelDataList.get(3);
                
                
                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
        		
        		for(int i=0; i<strItemId.length; i++) {
        			 
                 	if(!"".equals(StaticUtil.nullToBlank(strItemId[i])) && !"".equals(StaticUtil.nullToBlank(strScore[i]))) {
                 		searchMap.put("deptCd", strDeptCd[i]);
                 		searchMap.put("empn", strEmpn[i]);
 	                	searchMap.put("evalItemId", strItemId[i]);
 	                	searchMap.put("score", strScore[i]);
 	                	
 	                	if(searchMap.getString("empn").length() == 6 && searchMap.getString("evalItemId").length() == 7){
 	                		returnMap = insertData("prs.mngCon.scoreUpload.updateData", searchMap);
 	                	}
                 	}
                }
        	}
        	
        	/**********************************
             * 로그등록
             **********************************/
            /*searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);
            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true);
            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap);*/
        	
            /**********************************
             * 등록 실패시 작업
             **********************************/
           /* Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true, true);
    	            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap, true, true);
    	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
    			} 
    		}*/
        	
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
   				System.out.println("엑셀2007");
   				list = excelParsing2(searchMap);
   			} else { //엑셀2003
   				System.out.println("엑셀2003");
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

	    int rowCnt = 0;
	    int columnCnt = 9;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strDeptCd 	= new String[rowCnt-1];
	     	String[] strEmpn 	= new String[rowCnt-1];
	     	String[] strItemId 	= new String[rowCnt-1];
	       	String[] strScore 	= new String[rowCnt-1];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = 1; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);
	       		
	       		int col_cnt = columnCnt;

	       		if(columnCnt > currRow.getLastCellNum()) {
	       			col_cnt = currRow.getLastCellNum();
	       		}

	       		for(int j = 0; j < col_cnt; j++) { //열
	       			XSSFCell currCol = currRow.getCell((short)j);
	       			switch(currCol.getCellType()) {
	       			case XSSFCell.CELL_TYPE_NUMERIC:
	       				if(j == 1){
	       					strDeptCd[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 2) {
	       					strEmpn[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 6) {
	       					strItemId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); 
	       				}else if(j == 8) {
	       					strScore[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue());
		       			}
	       				break;
	       			case XSSFCell.CELL_TYPE_STRING:
	       				if(j == 1){
	       					strDeptCd[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 2) {
	       					strEmpn[i-1] =  StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 6) {
	       					strItemId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 8) {
	       					strScore[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strDeptCd);
	       	list.add(strEmpn);
	       	list.add(strItemId);
	       	list.add(strScore);
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

	    int rowCnt = 0;
	    int columnCnt = 9;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);
	       	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	    	
	       	String[] strDeptCd 	= new String[rowCnt-1];
	    	String[] strEmpn 	= new String[rowCnt-1];
	    	String[] strItemId 	= new String[rowCnt-1];
	       	String[] strScore 	= new String[rowCnt-1];

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = 1; i < rowCnt; i++) { //행
	       		HSSFRow currRow = sheet.getRow(i);
	       		
	       		int col_cnt = columnCnt;

	       		if(columnCnt > currRow.getLastCellNum()) {
	       			col_cnt = currRow.getLastCellNum();
	       		}

	       		for(int j = 0; j < col_cnt; j++) { //열
	       			HSSFCell currCol = currRow.getCell((short)j);
	       			switch(currCol.getCellType()) {
	       			case HSSFCell.CELL_TYPE_NUMERIC:
	       				if(j == 1){
	       					strDeptCd[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 2) {
	       					strEmpn[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); 
	       				}else if(j == 6) {
	       					strItemId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				} else if(j == 8) {
	       					strScore[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); 
	       				}
	       				break;
	       			case HSSFCell.CELL_TYPE_STRING:
	       				if(j == 1){
	       					strDeptCd[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 2) {
	       					strEmpn[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				}else if(j == 6) {
	       					strItemId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				} else if(j == 8) {
	       					strScore[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strDeptCd);
	       	list.add(strEmpn);
	       	list.add(strItemId);
	       	list.add(strScore);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
   	}
   	
}

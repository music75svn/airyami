/*************************************************************************
* CLASS 명      : intEvalResult
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 20일
* 기    능      : 내부평가결과
* -------------------------------- 변 경 이 력 --------------------------------
* 번 호		작 업 자      	 작   업   일        변 경 내 용              비고
* -----		---------  		---------------  -------------------------	-------
*   1   	안 요 한		2013년 08월 20일      최 초 작 업
**************************************************************************/
package com.lexken.bsc.dept;

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

public class IntEvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 내부평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap intEvalResultList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 내부평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap intEvalResultList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.dept.intEvalResult.getList", searchMap));

        return searchMap;
    }


    /**
     * 내부평가결과 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap intEvalResultProcess(SearchMap searchMap) {
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
        if ("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if ("GET_BSC_SCORE".equals(stMode)) {
        	searchMap = getBscScore(searchMap);
        } else if ("GET_GOV_SCORE".equals(stMode)) {
        	searchMap = getGovScore(searchMap);
        } else if("EXCEL".equals(stMode)) {
            searchMap = mngConScoreUploadExcel(searchMap);
        }  

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 내부평가결과 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();
        	String[] scDeptIds = searchMap.getStringArray("scDeptIds");
        	String[] scDeptNms = searchMap.getStringArray("scDeptNms");
        	String[] scores    = searchMap.getStringArray("scores");
        	String[] weights    = searchMap.getStringArray("weights");
            String[] adjusts   = searchMap.getStringArray("adjusts");
            String[] govScores = searchMap.getStringArray("govScores");
            String[] govWeights = searchMap.getStringArray("govWeights");
            String[] grades    = searchMap.getStringArray("grades");

            for(int i=0; i<scDeptIds.length; i++){
            	if(null != scDeptIds[i]){
            		searchMap.put("scDeptId", scDeptIds[i]);
            		searchMap.put("scDeptNm", scDeptNms[i]);
            		searchMap.put("score", scores[i]);
            		searchMap.put("weight", weights[i]);
            		searchMap.put("adjust", adjusts[i]);
            		searchMap.put("govScore", govScores[i]);
            		searchMap.put("govWeight", govWeights[i]);
            		searchMap.put("grade", grades[i]);

            		returnMap = updateData("bsc.dept.intEvalResult.updateData", searchMap);
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
     * 내평점수 가져오기
     * @param
     * @return String
     * @throws
     */
    public SearchMap getBscScore(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	//수정중
        	returnMap = deleteData("bsc.dept.intEvalResult.deleteData", searchMap, true);

        	returnMap = insertData("bsc.dept.intEvalResult.getBscScore", searchMap);

        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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
     * 정평점수 가져오기
     * @param
     * @return String
     * @throws
     */
    public SearchMap getGovScore(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();
        	//수정중

        	returnMap = insertData("bsc.dept.intEvalResult.getGovScore", searchMap);

        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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
     * 정부평가 지표별 책임가중치 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap intEvalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "내부평가점수";
    	String excelTitle = "내부평가점수LIST";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("year")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가군","SC_DEPT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서코드","SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("부서","SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("연말평가반영비율","WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("정부평가점수","GOV_SCORE", "center"));
    	excelInfoList.add(new ExcelVO("정부평가반영비율","GOV_WEIGHT", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.dept.intEvalResult.getListExcel", searchMap));
    	
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
     * 내부평가결과 업로드 등록/수정/삭제
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
    
    /*************************************************
   	* Excel Parsing (Excel ver 2007)
   	**************************************************/
   	public ArrayList excelParsing2(SearchMap searchMap) {
   		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 6;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strScDeptId 	= new String[rowCnt-1];
	    	String[] strWeight 		= new String[rowCnt-1];
	    	String[] strGovScore 	= new String[rowCnt-1];
	       	String[] strGovWeight 	= new String[rowCnt-1];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = 3; i < rowCnt; i++) { //행
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
	       					strScDeptId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 3) {
	       					strWeight[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 4) {
	       					strGovScore[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); 
	       				}else if(j == 5) {
	       					strGovWeight[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
		       			}
	       				break;
	       			case XSSFCell.CELL_TYPE_STRING:
	       				if(j == 1){
	       					strScDeptId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 3) {
	       					strWeight[i-1] =  StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 4) {
	       					strGovScore[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 5) {
	       					strGovWeight[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strScDeptId);
	       	list.add(strWeight);
	       	list.add(strGovScore);
	       	list.add(strGovWeight);
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
	    int columnCnt = 6;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);
	       	
	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수
	    	
	       	String[] strScDeptId 	= new String[rowCnt-1];
	    	String[] strWeight 		= new String[rowCnt-1];
	    	String[] strGovScore 	= new String[rowCnt-1];
	       	String[] strGovWeight 	= new String[rowCnt-1];
	       	
	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = 3; i < rowCnt; i++) { //행
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
	       					strScDeptId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue());
	       				}else if(j == 3) {
	       					strWeight[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); 
	       				}else if(j == 4) {
	       					strGovScore[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue());
	       				} else if(j == 5) {
	       					strGovWeight[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); 
	       				}
	       				break;
	       			case HSSFCell.CELL_TYPE_STRING:
	       				if(j == 1){
	       					strScDeptId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue());
	       				}else if(j == 3) {
	       					strWeight[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				}else if(j == 4) {
	       					strGovScore[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				} else if(j == 5) {
	       					strGovWeight[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); 
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strScDeptId);
	       	list.add(strWeight);
	       	list.add(strGovScore);
	       	list.add(strGovWeight);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
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
        		String[] strScDeptId 	= (String[]) excelDataList.get(0);
        		String[] strWeight 	= (String[]) excelDataList.get(1);
        		String[] strGovScore 	= (String[]) excelDataList.get(2);
        		String[] strGovWeight 	= (String[]) excelDataList.get(3);
                
                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
        		
        		for(int i=0; i<strScDeptId.length; i++) {
        			 
                 	if(!"".equals(StaticUtil.nullToBlank(strScDeptId[i]))) {
                 		searchMap.put("scDeptId", strScDeptId[i]);
                 		searchMap.put("weight", strWeight[i]);
 	                	searchMap.put("govScore", strGovScore[i]);
 	                	searchMap.put("govWeight", strGovWeight[i]);
 	                	
                		returnMap = updateData("bsc.dept.intEvalResult.updateData2", searchMap);
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
}

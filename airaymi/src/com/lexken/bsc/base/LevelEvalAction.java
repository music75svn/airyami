/*************************************************************************
* CLASS 명      : LevelEvalAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 9월 3일
* 기    능      : 난이도평가 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 9월 9일             최 초 작 업
**************************************************************************/
package com.lexken.bsc.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class LevelEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 난이도평가 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap levelEvalList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        //if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        //}
        
        /*else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }
        */

    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);
    	if("".equals(scDeptId)) {
    		searchMap.put("scDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("findScDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("scDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}
    	
    	searchMap.addList("codelist", getList("bsc.base.levelEval.getCodeList", searchMap));
    	
        return searchMap;
    }

    /**
     * 난이도평가 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap levelEvalList_xml(SearchMap searchMap) {
    	
    	List evalKpilist = getList("bsc.base.levelEval.getCodeList", searchMap);
    	searchMap.addList("evalKpilist", evalKpilist);
    	
    	String [] codeIds = new String[0];
		
    	if (evalKpilist != null) {
    		codeIds = new String[evalKpilist.size()];
	    	
	    	for (int i = 0; i < evalKpilist.size(); i++) {
	    		codeIds[i] = (String)((HashMap)evalKpilist.get(i)).get("CODE_ID");
	    	}
    	}
    	
    	searchMap.put("codeIds", codeIds);

        searchMap.addList("list", getList("bsc.base.levelEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도평가 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap levelEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if ("ADDEXCEL".equals(stMode)) {
        	searchMap = insertExcelDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 난이도평가 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	searchMap.put("findYear", searchMap.getString("year"));
    	
    	int colCnt = searchMap.getInt("colCnt");
    	String [] metricIds = searchMap.getString("metricIds").split("\\|", 0);

        try {
        	setStartTransaction();
            
            for (int i = 0; i < metricIds.length; i++) {
            	if (null != metricIds[i] && !"".equals(metricIds[i])) {
            		searchMap.put("metricId", metricIds[i]);
            		returnMap = updateData("bsc.base.levelEval.deleteLevelData", searchMap, true);
            		returnMap = updateData("bsc.base.levelEval.deleteLevelTotalData", searchMap, true);
            		
            		
            		String [] items = searchMap.getString("item"+ Integer.toString(i)).split("\\|", 0);
        	    	String [] scores = searchMap.getString("score"+ Integer.toString(i)).split("\\|", 0);
            		for (int j = 0; j < colCnt; j++) {
            	    	searchMap.put("item", items[j]);
            	    	searchMap.put("score", scores[j]);
            	    	
            	    	returnMap = insertData("bsc.base.levelEval.insertLevelData", searchMap);
            		}
            		
            		returnMap = insertData("bsc.base.levelEval.insertLevelTotalData", searchMap);
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
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap levelEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "LevelEval";

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("평가부서코드", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가부서명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표코드", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가항목코드", "EVAL_ITEM_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가항목명", "EVAL_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.levelEval.getExcelList", searchMap));

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

        	excelDataList = execlLevelUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strYear		= (String[]) excelDataList.get(0);
        		String[] strScdeptId	= (String[]) excelDataList.get(1);
        		String[] strMetricId	= (String[]) excelDataList.get(2);
        		String[] strItemId		= (String[]) excelDataList.get(3);
        		String[] strScore		= (String[]) excelDataList.get(4);
        		
        		/**********************************
                 * 평가점수 삭제
                 **********************************/
        		searchMap.put("strYear", strYear[0]);
        		
                returnMap = updateData("bsc.base.levelEval.deleteLevelDataExcel", searchMap, true);
                returnMap = updateData("bsc.base.levelEval.deleteLevelTotalDataExcel", searchMap, true);
        		
        		for(int i=0; i<strScdeptId.length; i++){
                	if(null != strScdeptId[i]){
                		searchMap.put("strYear", strYear[i]);
                		searchMap.put("strMetricId", strMetricId[i]);
                		searchMap.put("strScdeptId", strScdeptId[i]);
                		searchMap.put("strItemId", strItemId[i]);
                		searchMap.put("strScore", strScore[i]);

                        /**********************************
                         * 평가점수 입력
                         **********************************/
                        returnMap = insertData("bsc.base.levelEval.insertLevelDataExcel", searchMap);
                	}
                }
        		
        		returnMap = insertData("bsc.base.levelEval.insertLevelTotalDataExcel", searchMap);
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
	 * excel 난이도평가 점수 업로드
	 **************************************************/
	public ArrayList execlLevelUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlLevelParsing2(searchMap);
			} else { //엑셀2003
				list = execlLevelParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlLevelParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 8;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strYear = new String[rowCnt-1];
			String[] strMetricId = new String[rowCnt-1];
			String[] strScdeptId = new String[rowCnt-1];
			String[] strItemId = new String[rowCnt-1];
			String[] strScore = new String[rowCnt-1];

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
	       				if(j == 0) {
	       					strYear[i-1] = String.valueOf(currCol.getNumericCellValue()); //년도
						} else if(j == 1) {
							strScdeptId[i-1] = String.valueOf(currCol.getNumericCellValue()); //평가부서
						} else if(j == 3) {
							strMetricId[i-1] = String.valueOf(currCol.getNumericCellValue()); //지표
						} else if(j == 5) {
							strItemId[i-1] = String.valueOf(currCol.getNumericCellValue()); //평가항목
						} else if(j == 7) {
							strScore[i-1] = String.valueOf(currCol.getNumericCellValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = String.valueOf(currCol.getStringCellValue()); //년도
						} else if(j == 1) {
							strScdeptId[i-1] = String.valueOf(currCol.getStringCellValue()); //평가부서
						} else if(j == 3) {
							strMetricId[i-1] = String.valueOf(currCol.getStringCellValue()); //지표
						} else if(j == 5) {
							strItemId[i-1] = String.valueOf(currCol.getStringCellValue()); //평가항목
						} else if(j == 7) {
							strScore[i-1] = String.valueOf(currCol.getStringCellValue()); //점수
						}
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strYear);
			list.add(strScdeptId);
			list.add(strMetricId);
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
	public ArrayList execlLevelParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 8;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strYear = new String[rowCnt-1];
			String[] strMetricId = new String[rowCnt-1];
			String[] strScdeptId = new String[rowCnt-1];
			String[] strItemId = new String[rowCnt-1];
			String[] strScore = new String[rowCnt-1];
			

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
	       			case XSSFCell.CELL_TYPE_NUMERIC:
	       				if(j == 0) {
	       					strYear[i-1] = String.valueOf(currCol.getNumericCellValue()); //년도
						} else if(j == 1) {
							strScdeptId[i-1] = String.valueOf(currCol.getNumericCellValue()); //평가부서
						} else if(j == 3) {
							strMetricId[i-1] = String.valueOf(currCol.getNumericCellValue()); //지표
						} else if(j == 5) {
							strItemId[i-1] = String.valueOf(currCol.getNumericCellValue()); //평가항목
						} else if(j == 7) {
							strScore[i-1] = String.valueOf(currCol.getNumericCellValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = String.valueOf(currCol.getStringCellValue()); //년도
						} else if(j == 1) {
							strScdeptId[i-1] = String.valueOf(currCol.getStringCellValue()); //평가부서
						} else if(j == 3) {
							strMetricId[i-1] = String.valueOf(currCol.getStringCellValue()); //지표
						} else if(j == 5) {
							strItemId[i-1] = String.valueOf(currCol.getStringCellValue()); //평가항목
						} else if(j == 7) {
							strScore[i-1] = String.valueOf(currCol.getStringCellValue()); //점수
						}
	       			default:
	   					break;
	       			}
				}
			}

			list.add(strYear);
			list.add(strScdeptId);
			list.add(strMetricId);
			list.add(strItemId);
			list.add(strScore);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}

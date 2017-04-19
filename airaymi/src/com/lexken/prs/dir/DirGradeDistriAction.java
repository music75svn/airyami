/*************************************************************************
* CLASS 명      : DirGradeDistriAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 26일
* 기    능      : 등급배분표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 06월 26일         최 초 작 업
**************************************************************************/
package com.lexken.prs.dir;

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
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class DirGradeDistriAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 등급배분표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirGradeDistriList(SearchMap searchMap) {

    	searchMap.addList("gradeList",  getList("prs.dir.dirGradeDistri.getGradeList", searchMap));

    	return searchMap;
    }

    /**
     * 등급배분표 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirGradeDistriList_xml(SearchMap searchMap) {

    	/**********************************
         * 등급배분표 등급 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("prs.dir.dirGradeDistri.getGradeList", searchMap);

    	String[] itemArray = new String[0];
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("GRADE");
    		}
    	}

    	searchMap.put("gradeArray", itemArray);

    	/**********************************
         * 등급배분표 상세내용 가져오기
         **********************************/
    	searchMap.addList("gradeList", getList("prs.dir.dirGradeDistri.getGradeList", searchMap));

        searchMap.addList("list", getList("prs.dir.dirGradeDistri.dirGradeDistriTabList", searchMap));


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
     * 등급배분표 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirGradeDistriListExcel(SearchMap searchMap) {
    	String excelFileName = "등급배분표상세";
    	String excelTitle = "등급배분표상세";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm") + "[" + (String)searchMap.get("year") + "]" ));

    	ArrayList gradeList = (ArrayList)getList("prs.dir.dirGradeDistri.getGradeList", searchMap);

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("대상수", "ITEM_CNT", "left"));

    	if( null != gradeList && 0 < gradeList.size() ){
    		for(int j = 0;j < gradeList.size(); j++) {
				HashMap imap = (HashMap)gradeList.get(j);
				String colNm = (String)imap.get("GRADE") + "[" + (String)imap.get("GRADE") + "]";
				excelInfoList.add(new ExcelVO( colNm, (String)imap.get("GRADE"), "center"));
			}
    	}


    	ArrayList iqueryGradeList = (ArrayList)getList("prs.dir.dirGradeDistri.getGradeList", searchMap);
    	String[] gradeArray = new String[0];
    	if(null != iqueryGradeList && 0 < iqueryGradeList.size()) {
    		gradeArray = new String[iqueryGradeList.size()];
    		for(int i=0; i<iqueryGradeList.size(); i++) {
    			HashMap map = (HashMap)iqueryGradeList.get(i);
    			gradeArray[i] = (String)map.get("GRADE");
    		}
    	}

    	searchMap.put("gradeArray", gradeArray);

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("prs.dir.dirGradeDistri.dirGradeDistriTabList", searchMap));


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

        searchMap = dirGradeDistriTabUpExcel(searchMap);

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
    public SearchMap dirGradeDistriTabUpExcel(SearchMap searchMap) {

    	HashMap returnMap = new HashMap();
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	/**********************************
             * 등급배분표 등급 가져오기
             **********************************/
        	String year = searchMap.getString("findYear");

        	searchMap.put("year", year);
        	ArrayList gradeList = (ArrayList)getList("prs.dir.dirGradeDistri.getGradeList", searchMap);
        	int colCnt = gradeList.size();
        	searchMap.put("colCnt", colCnt+"" );

        	excelDataList = execlUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";

            	String upYear = "";
            	String[] upGrade = new String[ colCnt ];
        		String[] strCode = (String[]) excelDataList.get(0);
                String[][] strValue = (String[][]) excelDataList.get(1);

        		/**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
                returnMap = deleteData("prs.dir.dirGradeDistri.deleteTabData", searchMap, true);


                for(int i=0; i< strCode.length ; i++) {

                	if( 0 == i ){ // 평가년도  설정  부분
                		tmpVal = strCode[i];
                		int startIdx = tmpVal.lastIndexOf("[");
                		int endIdx = tmpVal.lastIndexOf("]");
                		upYear = tmpVal.substring(startIdx + 1, endIdx);

                		searchMap.put("year", upYear);

                	}else if( 1 == i) { // 등급항목 설정  부분
                		for(int j = 0 ; j < colCnt  ; j++ ){
                			tmpVal = strValue[i][j];
                    		int startIdx = tmpVal.lastIndexOf("[");
                    		int endIdx = tmpVal.lastIndexOf("]");
                    		upGrade[j] = tmpVal.substring(startIdx + 1, endIdx);
                		}
                	}else if( 2 <= i ){ // 등급표 부분
	                	for(int j = 0 ; j < colCnt ; j++ ){
		                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i][j]))) {
			                	searchMap.put("itemCnt", strCode[i]);
			                	searchMap.put("grade", upGrade[j] );
			                	searchMap.put("itemDistriCnt", strValue[i][j]);

			                	returnMap = insertData("prs.dir.dirGradeDistri.insertTabData", searchMap);
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
	public ArrayList execlUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execelParsing2(searchMap);
			} else { //엑셀2003
				list = execlParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execelParsing2(SearchMap searchMap) {
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

	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt ];

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
	    int columnCnt = 3;

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
	       			} catch(Exception e) {
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

}

/*************************************************************************
* CLASS 명      : DeptGovWeightAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 05일 
* 기    능      : 부서별 정평지표 점수반영 목록
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 07월 05일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.dept;
    
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class DeptGovWeightAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 부서별 정평지표 책임가중치 목록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptGovWeightList(SearchMap searchMap) {
    	searchMap.addList("govMetricList", getList("bsc.dept.deptGovWeight.getGovMetricList", searchMap));
    	
    	searchMap.addList("cnt", getDetail("bsc.dept.deptGovWeight.getMeticCount", searchMap)); 
    	
        return searchMap;
    }
    
    /**
     * 부서별 정평지표 책임가중치 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptGovWeightList_xml(SearchMap searchMap) {
    	List govMetricList = getList("bsc.dept.deptGovWeight.getGovMetricList", searchMap);
    	searchMap.addList("govMetricList", govMetricList);
    	
    	String [] govMetricIds = new String[0];
    			
    	if (govMetricList != null) {
	    	govMetricIds = new String[govMetricList.size()];
	    	
	    	for (int i = 0; i < govMetricList.size(); i++) {
	    		govMetricIds[i] = (String)((HashMap)govMetricList.get(i)).get("GOV_METRIC_ID");
	    	}
    	}
    	
    	searchMap.put("govMetricIds", govMetricIds);
        searchMap.addList("list", getList("bsc.dept.deptGovWeight.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 정부평가 지표별 책임가중치 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptGovWeigthListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표 책임가중치 및 반영비율";
    	String excelTitle = "정평지표 책임가충치 및 반영비율 LIST";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서코드","SC_DEPT_ID", "center"));
    	List govMetricList = getList("bsc.dept.deptGovWeight.getGovMetricList", searchMap);
    	searchMap.addList("govMetricList", govMetricList);
    	
    	String [] govMetricIds = new String[0];
    	String [] govMetricNms = new String[0];
    			
    	if (govMetricList != null) {
	    	govMetricIds = new String[govMetricList.size()];
	    	govMetricNms = new String[govMetricList.size()];
	    	
	    	for (int i = 0; i < govMetricList.size(); i++) {
	    		HashMap imap = (HashMap)govMetricList.get(i);
	    		String colNm = (String)imap.get("GOV_METRIC_NM") + "[" + (String)imap.get("GOV_METRIC_ID") + "]";
	    		govMetricIds[i] = (String)((HashMap)govMetricList.get(i)).get("GOV_METRIC_ID");
	    		govMetricNms[i] = (String)((HashMap)govMetricList.get(i)).get("GOV_METRIC_NM");
	    		excelInfoList.add(new ExcelVO(colNm, govMetricIds[i], "center"));
	    	}
    	}
    	
    	searchMap.put("govMetricIds", govMetricIds);
        searchMap.addList("list", getList("bsc.dept.deptGovWeight.getList", searchMap));
		
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.dept.deptGovWeight.getList", searchMap));
    	
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

        searchMap = popExcelUpload(searchMap);

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 정평지표 점수 엑셀업로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {

    	HashMap returnMap = new HashMap();
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	/**********************************
             * 정평지표 개수 가져오기
             **********************************/
        	String year = searchMap.getString("findYear");

        	searchMap.put("year", year);
        	ArrayList gradeList = (ArrayList)getList("bsc.dept.deptGovWeight.getGovMetricList", searchMap);
        	int colCnt = gradeList.size();
        	searchMap.put("colCnt", colCnt+"");

        	excelDataList = execlUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";

            	String[] upGrade = new String[ colCnt ];
        		String[] strCode = (String[]) excelDataList.get(0);
                String[][] strValue = (String[][]) excelDataList.get(1);

        		/**********************************
                 * 기존 등록된 가중치 삭제
                 **********************************/
                returnMap = deleteData("bsc.dept.deptGovWeight.deleteData", searchMap, true);
                
                for(int i=0; i< strCode.length ; i++) {

                	if( 0 == i) { // 지표항목 부분
                		for(int j = 0 ; j < colCnt  ; j++ ){
                			tmpVal = strValue[i][j];
                			
                			if (tmpVal == null) tmpVal = "";
                			
                    		int startIdx = tmpVal.lastIndexOf("[");
                    		int endIdx = tmpVal.lastIndexOf("]");
                    		
                    		if (startIdx >= 0 && endIdx >= 0)
                    			upGrade[j] = tmpVal.substring(startIdx + 1, endIdx);
                    		else
                    			upGrade[j] = "";
                		}
                	}else if( 1 <= i ){ // 가중치 부분
	                	for(int j = 0 ; j < colCnt ; j++ ){
		                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i][j]))) {
			                	searchMap.put("scDeptId", strCode[i]);
			                	searchMap.put("govMetricId", upGrade[j] );
			                	searchMap.put("weight", strValue[i][j]);

			                	returnMap = insertData("bsc.dept.deptGovWeight.insertTabData", searchMap);
			                	inputValCnt++;
		                	}
	                	}
                	}

                }
                returnMap = insertData("bsc.dept.deptGovWeight.insertTotaldata", searchMap); //부서별 가중치 총 합계
                
        	}

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

	       	rowCnt = sheet.getLastRowNum(); //실제 row수

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt ];

	       	for(int i = 2; i < rowCnt+1; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);

	       		int col_cnt = columnCnt;

	       		if(columnCnt > currRow.getLastCellNum()) {
	       			col_cnt = currRow.getLastCellNum();
	       		}

	       		for(int j = 1; j <= colCnt+1; j++) { //열
	       			XSSFCell currCol = currRow.getCell((short)j);
	       			try {
		       			switch(currCol.getCellType()) {
		       			case XSSFCell.CELL_TYPE_NUMERIC:
		       				if(j == 1) {
		       					strCode[i-2] = String.valueOf(currCol.getNumericCellValue()); //code값
		       				} else {
		       					strValue[i-2][j-2] = String.valueOf(currCol.getNumericCellValue()); //실적값
		       				}
		       				break;
		       			case HSSFCell.CELL_TYPE_STRING:
		       				if(j == 1) {
		       					strCode[i-2] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
		       				} else {
		       					strValue[i-2][j-2] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
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

	       	rowCnt = sheet.getLastRowNum(); //실제 row수

	       	String[] strCode = new String[rowCnt-1];
	       	String[][] strValue = new String[rowCnt-1][ colCnt ];

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = 2; i < rowCnt+1; i++) { //행
	       		HSSFRow currRow = sheet.getRow(i);

	       		for(int j = 1; j <= colCnt+1; j++) { //열
	       			HSSFCell currCol = currRow.getCell((short)j);
	       			
	       			try {
		       			switch(currCol.getCellType()) {
		       			case HSSFCell.CELL_TYPE_NUMERIC:
		       				if(j == 1) {
		       					strCode[i-2] = String.valueOf(currCol.getNumericCellValue()); //code값
		       				} else {
		       					strValue[i-2][j-2] = String.valueOf(currCol.getNumericCellValue()); //실적값
		       				}
		       				break;
		       			case HSSFCell.CELL_TYPE_STRING:
		       				if(j == 1) {
		       					strCode[i-2] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
		       				} else {
		       					strValue[i-2][j-2] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
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

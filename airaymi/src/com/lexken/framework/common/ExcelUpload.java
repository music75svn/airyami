/*************************************************************************
 * CLASS 명  	: ExcelUpload
 * 작 업 자  	: 하윤식
 * 작 업 일  	: 2012.07.26
 * 기    능  	: 엑셀업로드 관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작    업   일         변 경 내 용             비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    하윤식	  2012.07.26          최 초 작 업
 **************************************************************************/
package com.lexken.framework.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

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

import com.lexken.framework.config.FileConfig;
import com.lexken.framework.util.StaticUtil;

public class ExcelUpload {

	private static ExcelUpload instance = null;
	private final Log logger = LogFactory.getLog(getClass());


	public static ExcelUpload getInstance() {
		if (instance == null) {
			instance = new ExcelUpload();
		}
		return instance;
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
				list = execlParsing2(searchMap);
			} else { //엑셀2003
				list = execlParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* excel 목표 업로드
	**************************************************/
	public ArrayList execlTgtUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlTgtParsing2(searchMap);
			} else { //엑셀2003
				list = execlTgtParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 3;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strCode = new String[rowCnt-1];
	       	String[] strValue = new String[rowCnt-1];

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
	       					strCode[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //code값
	       				} else if(j == 2) {
	       					//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //실적값
	       					strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
	       				}
	       				break;
	       			case XSSFCell.CELL_TYPE_STRING:
	       				if(j == 0) {

	       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
	       				} else if(j == 2) {
	       					strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
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
	    int columnCnt = 3;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strCode = new String[rowCnt-1];
	       	String[] strValue = new String[rowCnt-1];

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
	       				if(j == 0) {
	       					strCode[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //code값
	       				} else if(j == 2) {
	       					//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //실적값
	       					strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
	       				}
	       				break;
	       			case HSSFCell.CELL_TYPE_STRING:
	       				if(j == 0) {

	       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
	       				} else if(j == 2) {
	       					strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
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
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlTgtParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 5;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strCode = new String[rowCnt-1];
	       	String[] strMon = new String[rowCnt-1];
	       	String[] strValue = new String[rowCnt-1];

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
	       					strCode[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //code값
	       				} else if(j == 3) {
	       					strMon[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //목표월
	       				} else if(j == 4) {
		       				//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //목표값
		       				strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //목표값
		       			}
	       				break;
	       			case XSSFCell.CELL_TYPE_STRING:
	       				if(j == 0) {
	       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
	       				} else if(j == 3) {
	       					strMon[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //목표월
	       				} else if(j == 4) {
	       					strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //목표값
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strCode);
	       	list.add(strMon);
       		list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2003)
	**************************************************/
	public ArrayList execlTgtParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 5;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strCode = new String[rowCnt-1];
	       	String[] strMon = new String[rowCnt-1];
	       	String[] strValue = new String[rowCnt-1];

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
	       				if(j == 0) {
	       					strCode[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //code값
	       				} else if(j == 3) {
	       					strMon[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //목표월
	       				} else if(j == 4) {
	       					//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //목표값
	       					strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //목표값
	       				}
	       				break;
	       			case HSSFCell.CELL_TYPE_STRING:
	       				if(j == 0) {
	       					strCode[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //code값
	       				} else if(j == 3) {
	       					strMon[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //목표월
	       				} else if(j == 4) {
	       					strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //목표값
	       				}
	       				break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strCode);
	       	list.add(strMon);
       		list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 파견직설정 업로드
	 **************************************************/
	public ArrayList execlBonDisSetUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");

		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlBonDisSetParsing2(searchMap);
			} else { //엑셀2003
				list = execlBonDisSetParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlBonDisSetParsing2(SearchMap searchMap) {
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

	       	String[] strEmpn 				= new String[rowCnt-3];
			String[] strKorNm 				= new String[rowCnt-3];
			String[] strDeptNm 				= new String[rowCnt-3];
			String[] strDeptDetailNm		= new String[rowCnt-3];
			String[] strCastTcNm 			= new String[rowCnt-3];
			String[] strPosTcNm 			= new String[rowCnt-3];
			String[] strStartPcmtDate 		= new String[rowCnt-3];
			String[] strWorkMon 			= new String[rowCnt-3];

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
	       				if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 1) {
							strKorNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //이름
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서
						} else if(j == 3) {
							strDeptDetailNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부(팀)명
						} else if(j == 4) {
							strCastTcNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급
						} else if(j == 5) {
							strPosTcNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위
						} else if(j == 6) {
							strStartPcmtDate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //입사일
						} else if(j == 7) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무개월
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 1) {
							strKorNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //이름
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strDeptDetailNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부(팀)명
						} else if(j == 4) {
							strCastTcNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급
						} else if(j == 5) {
							strPosTcNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위
						} else if(j == 6) {
							strStartPcmtDate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //입사일
						} else if(j == 7) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무개월
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strEmpn);
			list.add(strKorNm);
			list.add(strDeptNm);
			list.add(strDeptDetailNm);
			list.add(strCastTcNm);
			list.add(strPosTcNm);
			list.add(strStartPcmtDate);
			list.add(strWorkMon);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlBonDisSetParsing(SearchMap searchMap) {
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

			String[] strEmpn 			= new String[rowCnt-3];
			String[] strKorNm 			= new String[rowCnt-3];
			String[] strDeptNm 			= new String[rowCnt-3];
			String[] strDeptDetailNm	= new String[rowCnt-3];
			String[] strCastTcNm 		= new String[rowCnt-3];
			String[] strPosTcNm 		= new String[rowCnt-3];
			String[] strStartPcmtDate 	= new String[rowCnt-3];
			String[] strWorkMon 		= new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 1) {
							strKorNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //이름
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서
						} else if(j == 3) {
							strDeptDetailNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부(팀)명
						} else if(j == 4) {
							strCastTcNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급
						} else if(j == 5) {
							strPosTcNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위
						} else if(j == 6) {
							strStartPcmtDate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //입사일
						} else if(j == 7) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무개월
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 1) {
							strKorNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //이름
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strDeptDetailNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부(팀)명
						} else if(j == 4) {
							strCastTcNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급
						} else if(j == 5) {
							strPosTcNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위
						} else if(j == 6) {
							strStartPcmtDate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //입사일
						} else if(j == 7) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무개월
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strEmpn);
			list.add(strKorNm);
			list.add(strDeptNm);
			list.add(strDeptDetailNm);
			list.add(strCastTcNm);
			list.add(strPosTcNm);
			list.add(strStartPcmtDate);
			list.add(strWorkMon);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 임원연봉설정 업로드
	 **************************************************/
	public ArrayList execlDirSalUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlDirSalParsing2(searchMap);
			} else { //엑셀2003
				list = execlDirSalParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlDirSalParsing2(SearchMap searchMap) {
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

	       	String[] strEmpn = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strCastTc = new String[rowCnt-3];
			String[] strPosTc = new String[rowCnt-3];
			String[] strWorkMon = new String[rowCnt-3];
			String[] strRate = new String[rowCnt-3];
			String[] strSalary = new String[rowCnt-3];
			String[] strContent = new String[rowCnt-3];

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
	       				if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 2) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 3) {
							strCastTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급코드
						} else if(j == 5) {
							strPosTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위코드
						} else if(j == 12) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무기간
						} else if(j == 13) {
							strRate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지급률
						} else if(j == 14) {
							strSalary[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본연봉
						} else if(j == 15) {
							strContent[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //비고
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 2) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 3) {
							strCastTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급코드
						} else if(j == 5) {
							strPosTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위코드
						} else if(j == 12) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무기간
						} else if(j == 13) {
							strRate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지급률
						} else if(j == 14) {
							strSalary[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본연봉
						} else if(j == 15) {
							strContent[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //비고
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strEmpn);
			list.add(strDeptCd);
			list.add(strCastTc);
			list.add(strPosTc);
			list.add(strWorkMon);
			list.add(strRate);
			list.add(strSalary);
			list.add(strContent);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlDirSalParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 16;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strEmpn = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strCastTc = new String[rowCnt-3];
			String[] strPosTc = new String[rowCnt-3];
			String[] strWorkMon = new String[rowCnt-3];
			String[] strRate = new String[rowCnt-3];
			String[] strSalary = new String[rowCnt-3];
			String[] strContent = new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 2) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 3) {
							strCastTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급코드
						} else if(j == 5) {
							strPosTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위코드
						} else if(j == 12) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무기간
						} else if(j == 13) {
							strRate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지급률
						} else if(j == 14) {
							strSalary[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본연봉
						} else if(j == 15) {
							strContent[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //비고
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 2) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 3) {
							strCastTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급코드
						} else if(j == 5) {
							strPosTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위코드
						} else if(j == 12) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무기간
						} else if(j == 13) {
							strRate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지급률
						} else if(j == 14) {
							strSalary[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본연봉
						} else if(j == 15) {
							strContent[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //비고
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strEmpn);
			list.add(strDeptCd);
			list.add(strCastTc);
			list.add(strPosTc);
			list.add(strWorkMon);
			list.add(strRate);
			list.add(strSalary);
			list.add(strContent);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 조직관리역량평가 업로드
	 **************************************************/
	public ArrayList execlOrgDeptScoreUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlOrgDeptScoreParsing2(searchMap);
			} else { //엑셀2003
				list = execlOrgDeptScoreParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlOrgDeptScoreParsing2(SearchMap searchMap) {
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

	       	String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

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
	       			try {
		       			switch(currCol.getCellType()) {
		       			case XSSFCell.CELL_TYPE_NUMERIC:
		       				if(j == 0) {
								strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
							} else if(j == 1) {
								strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
							} else if(j == 3) {
								strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
							} else if(j == 4) {
								strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							if(j == 0) {
								strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
							} else if(j == 1) {
								strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
							} else if(j == 3) {
								strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
							} else if(j == 4) {
								strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
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

	       	list.add(strYear);
			list.add(strDeptCd);
			list.add(strGrade);
			list.add(strScore);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlOrgDeptScoreParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 16;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

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
					
					try {
						switch(currCol.getCellType()) {
						case HSSFCell.CELL_TYPE_NUMERIC:
							if(j == 0) {
								strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
							} else if(j == 1) {
								strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
							} else if(j == 3) {
								strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
							} else if(j == 4) {
								strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							if(j == 0) {
								strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
							} else if(j == 1) {
								strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
							} else if(j == 3) {
								strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
							} else if(j == 4) {
								strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
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

			list.add(strYear);
			list.add(strDeptCd);
			list.add(strGrade);
			list.add(strScore);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	/*************************************************
	 * excel 조직업무평가결과 업로드
	 **************************************************/
	public ArrayList execlInsaDeptScoreUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlInsaDeptScoreParsing2(searchMap);
			} else { //엑셀2003
				list = execlInsaDeptScoreParsing(searchMap);
			}
		}

		return list;
	}
	
	/*************************************************
	 * excel 조직업무평가결과 업로드
	 **************************************************/
	public ArrayList execlMemDeptScoreUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlInsaDeptScoreParsing2(searchMap);
			} else { //엑셀2003
				list = execlInsaDeptScoreParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlInsaDeptScoreParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 5;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strDeptNm = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

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
	       				if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서명
						} else if(j == 2) {
							strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strYear);
			list.add(strDeptCd);
			list.add(strDeptNm);
			list.add(strGrade);
			list.add(strScore);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlMemDeptScoreParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 5;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strDeptNm = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

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
	       				if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서명
						} else if(j == 2) {
							strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strYear);
			list.add(strDeptCd);
			list.add(strDeptNm);
			list.add(strGrade);
			list.add(strScore);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlInsaDeptScoreParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 5;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strDeptNm = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서명
						} else if(j == 2) {
							strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strYear);
			list.add(strDeptCd);
			list.add(strDeptNm);
			list.add(strGrade);
			list.add(strScore);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execMemDeptScoreParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 5;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strYear = new String[rowCnt-3];
			String[] strDeptCd = new String[rowCnt-3];
			String[] strDeptNm = new String[rowCnt-3];
			String[] strGrade = new String[rowCnt-3];
			String[] strScore = new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서명
						} else if(j == 2) {
							strGrade[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //점수
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 2) {
							strDeptNm[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서명
						} else if(j == 3) {
							strGrade[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //등급
						} else if(j == 4) {
							strScore[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //점수
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strYear);
			list.add(strDeptCd);
			list.add(strDeptNm);
			list.add(strGrade);
			list.add(strScore);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}


	/*************************************************
	 * excel 직원급여설정 업로드
	 **************************************************/
	public ArrayList execlEmpPayUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlEmpPayParsing2(searchMap);
			} else { //엑셀2003
				list = execlEmpPayParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlEmpPayParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 7;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strEmpn = new String[rowCnt-3];
			String[] strPay = new String[rowCnt-3];

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
	       			case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 5) {
							strPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본급
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 5) {
							strPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본급
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strEmpn);
			list.add(strPay);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlEmpPayParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 7;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strEmpn = new String[rowCnt-3];
			String[] strPay = new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 5) {
							strPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본급
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 5) {
							strPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본급
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strEmpn);
			list.add(strPay);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 기초연봉 순위 업로드
	 **************************************************/
	public ArrayList execlBasicSalUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlBasicPayParsing2(searchMap);
			} else { //엑셀2003
				list = execlBasicPayParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlBasicPayParsing2(SearchMap searchMap) {
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

	       	String[] strYear = new String[rowCnt-3];
			String[] strEmpn = new String[rowCnt-3];
			String[] strRank = new String[rowCnt-3];

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
	       			case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 7) {
							strRank[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //순위
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 7) {
							strRank[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //순위
						}
						break;
					default:
						break;
					}
				}
			}

	       	list.add(strYear);
			list.add(strEmpn);
			list.add(strRank);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlBasicPayParsing(SearchMap searchMap) {
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

			String[] strYear = new String[rowCnt-3];
			String[] strEmpn = new String[rowCnt-3];
			String[] strRank = new String[rowCnt-3];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 7) {
							strRank[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //순위
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 7) {
							strRank[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //순위
						}
						break;
					default:
						break;
					}
				}
			}

	       	list.add(strYear);
			list.add(strEmpn);
			list.add(strRank);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 임원연봉설정 업로드
	 **************************************************/
	public ArrayList execlBonDirUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");

		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlBonDirParsing2(searchMap);
			} else { //엑셀2003
				list = execlBonDirParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlBonDirParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 16;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strEmpn 				= new String[rowCnt-3];
	       	String[] strDeptCd 				= new String[rowCnt-3];
			String[] strCastTc     			= new String[rowCnt-3];
			String[] strPosTc     			= new String[rowCnt-3];
			String[] strSalary		 		= new String[rowCnt-3];
			String[] strRate		 		= new String[rowCnt-3];
			String[] strWorkMon   	 		= new String[rowCnt-3];
			String[] strTotBonSal  	 		= new String[rowCnt-3];

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
	       				if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						}else if(j == 4) {
							strCastTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급코드
						} else if(j == 6) {
							strPosTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위코드
						} else if(j == 11) {
							strSalary[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본연봉
						}else if(j == 12) {
							strRate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지급률
						}else if(j == 13) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무개월
						}else if(j == 14) {
							strTotBonSal[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //총지급액
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						}else if(j == 4) {
							strCastTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급코드
						} else if(j == 6) {
							strPosTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위코드
						} else if(j == 11) {
							strSalary[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본연봉
						}else if(j == 12) {
							strRate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지급률
						}else if(j == 13) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무개월
						}else if(j == 14) {
							strTotBonSal[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //총지급액
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strEmpn);
	       	list.add(strDeptCd);
			list.add(strCastTc);
			list.add(strPosTc);
			list.add(strSalary);
			list.add(strRate);
			list.add(strWorkMon);
			list.add(strTotBonSal);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlBonDirParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 16;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strEmpn 				= new String[rowCnt-3];
			String[] strDeptCd 				= new String[rowCnt-3];
			String[] strCastTc     			= new String[rowCnt-3];
			String[] strPosTc     			= new String[rowCnt-3];
			String[] strSalary		 		= new String[rowCnt-3];
			String[] strRate		 		= new String[rowCnt-3];
			String[] strWorkMon   	 		= new String[rowCnt-3];
			String[] strTotBonSal  	 		= new String[rowCnt-3];


			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						} else if(j == 1) {
							strDeptCd[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						}else if(j == 4) {
							strCastTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직급코드
						} else if(j == 6) {
							strPosTc[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직위코드
						} else if(j == 11) {
							strSalary[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //기본연봉
						}else if(j == 12) {
							strRate[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지급률
						}else if(j == 13) {
							strWorkMon[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //근무개월
						}else if(j == 14) {
							strTotBonSal[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //총지급액
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						} else if(j == 1) {
							strDeptCd[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						}else if(j == 4) {
							strCastTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직급코드
						} else if(j == 6) {
							strPosTc[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직위코드
						} else if(j == 11) {
							strSalary[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //기본연봉
						}else if(j == 12) {
							strRate[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지급률
						}else if(j == 13) {
							strWorkMon[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //근무개월
						}else if(j == 14) {
							strTotBonSal[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //총지급액
						}
						break;
					default:
						break;
					}
				}
			}

		 	list.add(strEmpn);
		 	list.add(strDeptCd);
			list.add(strCastTc);
			list.add(strPosTc);
			list.add(strSalary);
			list.add(strRate);
			list.add(strWorkMon);
			list.add(strTotBonSal);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * excel 직원개인성과 업로드
	 **************************************************/
	public ArrayList execlBonEmpUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");

		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlBonEmpParsing2(searchMap);
			} else { //엑셀2003
				list = execlBonEmpParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlBonEmpParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 15;

	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();

		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strSeq 				= new String[rowCnt-3];
	       	String[] strEmpn 				= new String[rowCnt-3];
			String[] strInsidePay     		= new String[rowCnt-3];
			String[] strGovPay     			= new String[rowCnt-3];
			String[] strTotPay		 		= new String[rowCnt-3];

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
	       				if(j == 0) {
	       					strSeq[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //일련번호
						} else if(j == 1) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						}else if(j == 12) {
							strInsidePay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //자체성과급
						} else if(j == 13) {
							strGovPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //정평성과급
						} else if(j == 14) {
							strTotPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //총지급액
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strSeq[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //일련번호
						} else if(j == 1) {
						strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						}else if(j == 12) {
							strInsidePay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //자체성과급
						} else if(j == 13) {
							strGovPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //정평성과급
						} else if(j == 14) {
							strTotPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //총지급액
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strSeq);
	       	list.add(strEmpn);
			list.add(strInsidePay);
			list.add(strGovPay);
			list.add(strTotPay);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlBonEmpParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 15;

		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

			String[] strSeq 				= new String[rowCnt-3];
	       	String[] strEmpn 				= new String[rowCnt-3];
			String[] strInsidePay     		= new String[rowCnt-3];
			String[] strGovPay     			= new String[rowCnt-3];
			String[] strTotPay		 		= new String[rowCnt-3];


			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 3; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
	       					strSeq[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //일련번호
						} else if(j == 1) {
							strEmpn[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //사원번호
						}else if(j == 12) {
							strInsidePay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //자체성과급
						} else if(j == 13) {
								strGovPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //정평성과급
						} else if(j == 14) {
								strTotPay[i-3] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //총지급액
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strSeq[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //일련번호
						} else if(j == 1) {
							strEmpn[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //사원번호
						}else if(j == 12) {
							strInsidePay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //자체성과급
						} else if(j == 13) {
							strGovPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //정평성과급
						} else if(j == 14) {
							strTotPay[i-3] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //총지급액
						}
						break;
					default:
						break;
					}
				}
			}

		 	list.add(strSeq);
	       	list.add(strEmpn);
			list.add(strInsidePay);
			list.add(strGovPay);
			list.add(strTotPay);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	
	/*************************************************
	 * excel 임원연봉설정 업로드
	 **************************************************/
	public ArrayList execlManMetircImportanceUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlManMetircImportancelParsing2(searchMap);
			} else { //엑셀2003
				list = execlManMetircImportancelParsing(searchMap);
			}
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlManMetircImportancelParsing2(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

	    int rowCnt = 0;
	    int columnCnt = 9;
	    int iStIndex = 6;
	    
	    boolean isDataExist = false; //데이터 존재확인변수

	    ArrayList list = new ArrayList();
	    
		try {
	       	//업로드된 엑셀파일 읽는다.
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제 row수

	       	String[] strManKpiId = new String[rowCnt-iStIndex];
			String[] strImportance = new String[rowCnt-iStIndex];
			

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	for(int i = iStIndex; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);

	       		int col_cnt = columnCnt;

	       		if(columnCnt > currRow.getLastCellNum()) {
	       			col_cnt = currRow.getLastCellNum();
	       		}

	       		for(int j = 0; j < col_cnt; j++) { //열
	       			XSSFCell currCol = currRow.getCell((short)j);

	       			switch(currCol.getCellType()) {
	       			case XSSFCell.CELL_TYPE_NUMERIC:
	       				if(j == 4) {
	       					strManKpiId[i-iStIndex] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //man_kpi_id
						} else if(j == 9) {
							strImportance[i-iStIndex] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //중요도						
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						
						if(j == 4) {
	       					strManKpiId[i-iStIndex] = String.valueOf(currCol.getStringCellValue()); //man_kpi_id
						} else if(j == 9) {
							strImportance[i-iStIndex] = String.valueOf(currCol.getStringCellValue()); //중요도						
						}
					
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strManKpiId);
			list.add(strImportance);
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlManMetircImportancelParsing(SearchMap searchMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int rowCnt = 0;
		int columnCnt = 16;
	    int iStIndex = 6;
		boolean isDataExist = false; //데이터 존재확인변수

		ArrayList list = new ArrayList();

		try {
			//업로드된 엑셀파일 읽는다.
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet = wb.getSheetAt(0);

			rowCnt = sheet.getLastRowNum() + 1; //실제 row수

		  	String[] strManKpiId = new String[rowCnt-iStIndex];
			String[] strImportance = new String[rowCnt-iStIndex];

			if(rowCnt > 1) {
				isDataExist = true;
			}

		   	for(int i = iStIndex; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 4) {
	       					strManKpiId[i-iStIndex] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //man_kpi_id
						} else if(j == 9) {
							strImportance[i-iStIndex] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //중요도						
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 4) {
	       					strManKpiId[i-iStIndex] = String.valueOf(currCol.getStringCellValue()); //man_kpi_id
						} else if(j == 9) {
							strImportance[i-iStIndex] = String.valueOf(currCol.getStringCellValue()); //중요도						
						}
						break;
					default:
						break;
					}
				}
			}

		 	list.add(strManKpiId);
			list.add(strImportance);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/*************************************************
	 * excel 조직관리역량평가 세부항목 업로드
	 **************************************************/
	public ArrayList execlOrgUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlOrgItemParsing2(searchMap);
			} else { //엑셀2003
				list = execlOrgItemParsing(searchMap);
			}
		}

		return list;
	}
	
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlOrgItemParsing2(SearchMap searchMap) {
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
	       	
	       	String[] strYear = new String[rowCnt-1];
	       	String[] strMon = new String[rowCnt-1];
			String[] strMetricId = new String[rowCnt-1];
			String[] strCalTypeCol = new String[rowCnt-1];
			String[] strEmpNo = new String[rowCnt-1];
			String[] strValue = new String[rowCnt-1];

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
							strYear[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strMon[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가월
						} else if(j == 2) {
							strMetricId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지표코드
						} else if(j == 4) {
							strCalTypeCol[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //산식항목
						} else if(j == 6) {
							strEmpNo[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직원번호
						} else if(j == 8) {
							strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strMon[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가월
						} else if(j == 2) {
							strMetricId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지표코드
						} else if(j == 4) {
							strCalTypeCol[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //산식항목
						} else if(j == 6) {
							strEmpNo[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직원번호
						} else if(j == 8) {
							strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strYear);
	       	list.add(strMon);
			list.add(strMetricId);
			list.add(strCalTypeCol);
			list.add(strEmpNo);
			list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	
	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlOrgItemParsing(SearchMap searchMap) {
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

	       	String[] strYear = new String[rowCnt-1];
	       	String[] strMon = new String[rowCnt-1];
			String[] strMetricId = new String[rowCnt-1];
			String[] strMetricNm = new String[rowCnt-1];
			String[] strCalTypeCol = new String[rowCnt-1];
			String[] strCalTypeColNm = new String[rowCnt-1];
			String[] strEmpNo = new String[rowCnt-1];
			String[] strValue = new String[rowCnt-1];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 1; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strMon[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가월
						} else if(j == 2) {
							strMetricId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //지표코드
						} else if(j == 4) {
							strCalTypeCol[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //산식항목
						} else if(j == 6) {
							strEmpNo[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //직원번호
						} else if(j == 8) {
							strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strMon[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가월
						} else if(j == 2) {
							strMetricId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //지표코드
						} else if(j == 4) {
							strCalTypeCol[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //산식항목
						} else if(j == 6) {
							strEmpNo[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //직원번호
						} else if(j == 8) {
							strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strYear);
			list.add(strMon);
			list.add(strMetricId);
			list.add(strCalTypeCol);
			list.add(strEmpNo);
			list.add(strValue);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/*************************************************
	 * excel 공통지표실적 업로드
	 **************************************************/
	public ArrayList execlActUpload(SearchMap searchMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlOrgItemParsing2(searchMap);
			} else { //엑셀2003
				list = execlOrgItemParsing(searchMap);
			}
		}

		return list;
	}
	
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlActItemParsing2(SearchMap searchMap) {
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
			String[] strDeptCd = new String[rowCnt-1];
			String[] strOrgEvalItemId = new String[rowCnt-1];
			String[] strCalTypeCol = new String[rowCnt-1];
			String[] strValue = new String[rowCnt-1];

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
							strYear[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 3) {
							strOrgEvalItemId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //세부평가항목코드
						} else if(j == 5) {
							strCalTypeCol[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //산식항목
						} else if(j == 7) {
							//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //실적값
							strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 3) {
							strOrgEvalItemId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //세부평가항목코드
						} else if(j == 5) {
							strCalTypeCol[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //산식항목
						} else if(j == 7) {
							strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
						}
						break;
	       			default:
	   					break;
	       			}
	       		}
	       	}

	       	list.add(strYear);
			list.add(strDeptCd);
			list.add(strOrgEvalItemId);
			list.add(strCalTypeCol);
			list.add(strValue);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	
	/*************************************************
	 * Excel Parsing (Excel ver 2003)
	 **************************************************/
	public ArrayList execlActItemParsing(SearchMap searchMap) {
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
			String[] strDeptCd = new String[rowCnt-1];
			String[] strOrgEvalItemId = new String[rowCnt-1];
			String[] strCalTypeCol = new String[rowCnt-1];
			String[] strValue = new String[rowCnt-1];

			if(rowCnt > 1) {
				isDataExist = true;
			}

			for(int i = 1; i < rowCnt; i++) { //행
				HSSFRow currRow = sheet.getRow(i);

				int col_cnt = columnCnt;
				int col_cnt1 = columnCnt;


				if(columnCnt > currRow.getLastCellNum()) {
					col_cnt = currRow.getLastCellNum();
					col_cnt1 = currRow.getLastCellNum();
				}

				for(int j = 0; j < col_cnt; j++) { //열
					HSSFCell currCol = currRow.getCell((short)j);

					switch(currCol.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(j == 0) {
							strYear[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //부서코드
						} else if(j == 3) {
							strOrgEvalItemId[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //세부평가항목코드
						} else if(j == 5) {
							strCalTypeCol[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //산식항목
						} else if(j == 7) {
							//strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).intValue()); //실적값
							strValue[i-1] = String.valueOf(new Double(currCol.getNumericCellValue()).doubleValue()); //실적값
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						if(j == 0) {
							strYear[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //평가년도
						} else if(j == 1) {
							strDeptCd[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //부서코드
						} else if(j == 3) {
							strOrgEvalItemId[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //세부평가항목코드
						} else if(j == 5) {
							strCalTypeCol[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //산식항목
						} else if(j == 7) {
							strValue[i-1] = StaticUtil.nullToBlank(currCol.getStringCellValue()); //실적값
						}
						break;
					default:
						break;
					}
				}
			}

			list.add(strYear);
			list.add(strDeptCd);
			list.add(strOrgEvalItemId);
			list.add(strCalTypeCol);
			list.add(strValue);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}

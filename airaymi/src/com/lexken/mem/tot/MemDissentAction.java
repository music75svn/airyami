/*************************************************************************
 * CLASS 명      : MemDissentAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 03월 15일 
 * 기    능      : 이의신청
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 03월 15일          최 초 작 업 
 **************************************************************************/
package com.lexken.mem.tot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MemDissentAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 이의신청 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memDissentList(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
	   	if(loginVO.chkAuthGrp("01")) {
    		searchMap.addList("authGrp", "ADMIN");
    	}else{
    		searchMap.addList("authGrp", "TARGET");
    	}
		return searchMap;
	}
	
	/**
	 * 이의신청 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memDissentList_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
		logger.debug("chkAuthGrp : "+loginVO.chkAuthGrp("01"));
		if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
		
		searchMap.addList("list", getList("mem.tot.memDissent.getList", searchMap));

		return searchMap;
	}

	/**
	 * 이의신청 상세화면
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memDissentModify(SearchMap searchMap) {
		
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
		
	   	if(loginVO.chkAuthGrp("01")) {
    		searchMap.addList("authGrp", "ADMIN");
    	}else{
    		searchMap.addList("authGrp", "TARGET");
    		searchMap.put("empNo", loginVO.getUser_id());
    		searchMap.addList("empInfo", getDetail("mem.eval.memEval.getEmpNm", searchMap));
    	}
		
	   	// 양식조회
	   	searchMap.addList("formFileList", getList("mem.tot.memDissent.getFormAttachFileList", searchMap));
	   	
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
            /**********************************
             * 이의신청 상세조회
             **********************************/
    		searchMap.addList("detail", getDetail("mem.tot.memDissent.getDetail", searchMap));
    		
            /**********************************
             * 이의신청 파일조회
             **********************************/
            searchMap.addList("fileList", getList("mem.tot.memDissent.getAttachFileList", searchMap));
    	}
		
		return searchMap;
	}

	/**
	 * 이의신청 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memDissentProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");

		/**********************************
		 * 무결성 체크
		 **********************************/
		if (!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"SAVEUSER".equals(stMode) && !"GET".equals(stMode)) {
			returnMap = this.validChk(searchMap);

			if ((Integer) returnMap.get("ErrorNumber") < 0) {
				searchMap.addList("returnMap", returnMap);
				return searchMap;
			}
		}

		/**********************************
		 * 등록/수정/삭제
		 **********************************/
		if ("ADD".equals(stMode)) {
			searchMap = insertDB(searchMap);
		} else if ("MOD".equals(stMode)) {
			searchMap = updateDB(searchMap);
		} else if ("DEL".equals(stMode)) {
			searchMap = deleteDB(searchMap);
		}

		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}

	/**
	 * 이의신청 등록
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insertDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/dissent");
		
		try {
			setStartTransaction();

			// 이의신청시컨스 생성
			HashMap dissentMap = getDetail("mem.tot.memDissent.getDissentSeq", searchMap);
			searchMap.put("dissentSeq", dissentMap.get("DISSENT_SEQ"));

			LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
			searchMap.put("sEmpNo", loginVO.getUser_id());
			// 이의신청 등록
			returnMap = insertData("mem.tot.memDissent.insertData", searchMap);
			
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
						
						returnMap = insertData("mem.tot.memDissent.insertFileInfo", searchMap);
					}
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
	 * 이의신청 수정
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap updateDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/dissent");
		
		try {
			setStartTransaction();

			// 이의신청 수정
			returnMap = updateData("mem.tot.memDissent.updateData", searchMap);
			
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("mem.tot.memDissent.deleteFileInfo", searchMap);
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
						
						returnMap = insertData("mem.tot.memDissent.insertFileInfo", searchMap);
					}
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
	 * 이의신청 삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deleteDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] dissentSeqs = searchMap.getString("dissentSeqs").split("\\|", -1);

			setStartTransaction();

			if (null != dissentSeqs && 0 < dissentSeqs.length) {
				for (int i = 0; i < dissentSeqs.length - 1; i++) {
					searchMap.put("dissentSeq", dissentSeqs[i]);
					returnMap = updateData("mem.tot.memDissent.deleteData",
							searchMap);
				}
			}

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
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
     * 이의신청 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDissentExcel(SearchMap searchMap) {
    	String excelFileName = "이의신청";
    	String excelTitle = "이의신청목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("findProcStatusNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("이의제목", "DISSENT_TITLE", "left"));
    	excelInfoList.add(new ExcelVO("성과조직", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("이의신청자", "EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("신청일", "CREATE_DT", "center"));
    	excelInfoList.add(new ExcelVO("처리상태", "PROC_STATUS_NM", "center"));
    	excelInfoList.add(new ExcelVO("처리완료일", "PROC_DT", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
		
		if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.tot.memDissent.getList", searchMap));
    	
    	return searchMap;
    	
    }
	
	/**
	 * Validation 체크(무결성 체크)
	 * 
	 * @param SearchMap
	 * @return HashMap
	 */
	private HashMap validChk(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		int resultValue = 0;
		String stMode = searchMap.getString("mode");

		returnMap = ValidationChk.lengthCheck(searchMap.getString("dissentTitle"), "이의제목", 1, 200);
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.lengthCheck(searchMap.getString("dissentContent"), "이의내용", 1, 4000);
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap.put("ErrorNumber", resultValue);
		return returnMap;
	}
}

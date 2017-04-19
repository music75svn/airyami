/*************************************************************************
* CLASS 명      : TargetMngAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 16일
* 기    능      : 지표목표입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 16일         최 초 작 업
**************************************************************************/
package com.lexken.bsc.tam;

import java.util.ArrayList;
import java.util.HashMap;

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

public class TargetMngAction extends CommonService {

private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 목표입력 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	String actualType = searchMap.getString("findActualType");

    	String get_flag	 = searchMap.getString("flag");
    	String get_tgt_sId	 = searchMap.getString("findTargetStatusId");

    	if( get_flag == "" && get_tgt_sId == "" ) {
    		searchMap.put("flag","1");
    		searchMap.put("findTargetStatusId","01");
    	}else if( "1".equals(get_flag) && "ALLSTATUS".equals(get_tgt_sId) ) {
    		searchMap.put("findTargetStatusId","01");
    	}

    	if( get_flag == "" ) {
    		searchMap.put("flag","1");
    	}

    	/**********************************
         * 목표 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("bsc.tam.targetMng.getInsertUserList", searchMap));

    	/**********************************
         * 목표 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("bsc.tam.targetMng.getApproveUserList", searchMap));

    	/**********************************
    	 * 목표 확인자 조회
    	 **********************************/
    	searchMap.addList("checkUserList", getList("bsc.tam.targetMng.getCheckUserList", searchMap));

    	/**********************************
         * 목표입력기한 여부 가져오기
         **********************************/
        searchMap.addList("targetInputTermYn", getStr("bsc.tam.targetMng.getTargetInputTermYn", searchMap));

        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearClosingYn", getStr("bsc.system.closingYear.getYearClosingYn", searchMap));

        /**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findInsertUserId")))) {
    			if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
    				//	searchMap.put("findInsertUserId", searchMap.getDefaultValue("insertUserList", "INSERT_USER_ID", 0));
    				searchMap.put("findInsertUserId", "");
    			}
    		}

    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findApproveUserId")))) {
    			if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {

    				if("check".equals(actualType) ){
    					//searchMap.put("findApproveUserId", searchMap.getDefaultValue("approveUserList", "APPROVE_USER_ID", 0));
    					searchMap.put("findApproveUserId","");
					}else{
						searchMap.put("findApproveUserId","");
					}

    			}
    		}
    	} else {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    		searchMap.put("findApproveUserId", searchMap.get("loginUserId"));
    	}

        /************************************
         * 입력대상 세부과제 지표조회
         ************************************/
    	if("input".equals(actualType)) {
	        searchMap.addList("scDeptTreeList", getList("bsc.tam.targetMng.getInsertScDeptTree", searchMap));
	        searchMap.addList("metricTreeList", getList("bsc.tam.targetMng.getInsertMetricTree", searchMap));
    	}else{
    		searchMap.addList("scDeptTreeList", getList("bsc.tam.targetMng.getApproveScDeptTree", searchMap));
	        searchMap.addList("metricTreeList", getList("bsc.tam.targetMng.getApproveMetricTree", searchMap));
    	}

        /**********************************
         * 디폴트 입력 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("metricId")))) {
    		//searchMap.put("metricId", (String)searchMap.getString("findMetricId"));
    	}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("metricId")))) {
    		searchMap.put("metricId", searchMap.getDefaultValue("metricTreeList", "METRIC_ID", 0));
    	}

    	/**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearCloseYn", getStr("bsc.tam.actualMng.getYearCloseYn", searchMap));

        return searchMap;
    }


    public SearchMap insertUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("bsc.tam.targetMng.getInsertUserList", searchMap));

        return searchMap;
    }

    public SearchMap approveUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("bsc.tam.targetMng.getApproveUserList", searchMap));

        return searchMap;
    }

    public SearchMap checkUserList_ajax(SearchMap searchMap) {

    	/**********************************
    	 * 목표 확인자 조회
    	 **********************************/
    	searchMap.addList("checkUserList", getList("bsc.tam.targetMng.getCheckUserList", searchMap));

    	return searchMap;
    }


    /**
     * 목표관리 상세데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngDetailList(SearchMap searchMap) {

    	String metricId = searchMap.getString("metricId");

    	if(!"".equals(metricId)) {

    		String findTargetStatusId = searchMap.getString("findTargetStatusId");
    		searchMap.put("findTargetStatusId", findTargetStatusId);
	    	/**********************************
	         * 월별목표조회
	         **********************************/
	        searchMap.addList("list", getList("bsc.tam.targetMng.getList", searchMap));

	        /**********************************
	         * 목표입력상태 가져오기
	         **********************************/
	        searchMap.addList("detail", getDetail("bsc.tam.targetMng.getTargetStatusId", searchMap));


	        /**********************************
	         *  목표입력월 목록 보기
	         **********************************/
	        searchMap.addList("inputYnList", getList("bsc.tam.targetMng.getInputYnList", searchMap));

	        /**********************************
	         * 첨부파일 조회
	         **********************************/
	        searchMap.addList("fileList", getList("bsc.tam.targetMng.getFileList", searchMap));

    	}

        /**********************************
         * 목표입력기한 여부 가져오기
         **********************************/
    	searchMap.addList("targetInputTermYn", getStr("bsc.tam.targetMng.getTargetInputTermYn", searchMap));

    	/**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearCloseYn", getStr("bsc.tam.actualMng.getYearCloseYn", searchMap));

        return searchMap;
    }


    /**
     * 목표값 계산기 저장내역 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetCalculator(SearchMap searchMap) {

    	/**********************************
         * 목표값 계산기 저장내역 조회
         **********************************/
    	String tmpYear = searchMap.getString("year");

    	String year_1 = ( Integer.parseInt(tmpYear) - 1 ) + "";
    	String year_2 = ( Integer.parseInt(tmpYear) - 2 ) + "";
    	String year_3 = ( Integer.parseInt(tmpYear) - 3 ) + "";
    	String year_4 = ( Integer.parseInt(tmpYear) - 4 ) + "";
    	String year_5 = ( Integer.parseInt(tmpYear) - 5 ) + "";


    	searchMap.addList("year01", year_1);
    	searchMap.addList("year02", year_2);
    	searchMap.addList("year03", year_3);
    	searchMap.addList("year04", year_4);
    	searchMap.addList("year05", year_5);

        searchMap.addList("calculDetail", getDetail("bsc.tam.targetMng.getTargetCalculSetting", searchMap));

    	return searchMap;

    }


    /**
     * 목표입력 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.tam.targetMng.getList", searchMap));

        return searchMap;
    }

    /**
     * 목표입력 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.tam.targetMng.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 목표입력 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngProcess(SearchMap searchMap) {
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
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/bscTarget");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("STATUS".equals(stMode)) {
            searchMap = statusDB(searchMap);
            
            String targetStatusId = searchMap.getString("targetStatusId");
            if(targetStatusId.equals("06")||targetStatusId.equals("05")||targetStatusId.equals("03")) {
            	statusProcDB(searchMap);
            }
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     * 목표입력 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetCalculMngProcess(SearchMap searchMap) {
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
        if("CALCUL_ADD".equals(stMode)) {
            searchMap = insertTargetCalCulDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 목표입력 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        String[] mons	 	= searchMap.getStringArray("mons");
        String[] colValues 	= searchMap.getStringArray("colValues");

        try {
        	setStartTransaction();

        	/**********************************
             * 월별 목표입력
             **********************************/

        	if(colValues != null && 0 < colValues.length) {
	        	for (int i = 0; i < colValues.length; i++) {
		            searchMap.put("mon", mons[i]);
		            searchMap.put("target", colValues[i]);
		            returnMap = insertData("bsc.tam.targetMng.insertDataProc", searchMap);
		        }

	        	returnMap = insertData("bsc.tam.targetMng.targetTimeRollup", searchMap);

    		}

        	/**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);

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
     * 첨부파일 정보등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");

        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles && 0 < delAttachFiles.length) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("bsc.tam.targetMng.deleteFileInfo", searchMap);
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
						searchMap.put("attachFileSuffix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("bsc.tam.targetMng.insertFileInfo", searchMap);
					}
				}
	        }

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 목표입력 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("bsc.tam.targetMng.updateData", searchMap);

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
     * 목표상태 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
        String   returnReason = searchMap.getString("returnReason");

        try {
        	setStartTransaction();

        	if(null != metricIds && 0 < metricIds.length) {
		        for (int i=0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            if(returnReason.equals("")){
		            	returnMap = updateData("bsc.tam.targetMng.updateStatusData", searchMap, true);
		            }else{
		            	returnMap = updateData("bsc.tam.targetMng.updateReturnStatusData", searchMap, true);
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
     * 쪽지 보내기
     * @param
     * @return String
     * @throws
     */
    public void statusProcDB(SearchMap searchMap) {
        /**********************************
         * Parameter setting
         **********************************/
    	String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);

        try {
        	setStartTransaction();

        	searchMap.put("procParam1", searchMap.getString("year"));
        	searchMap.put("procParam2", "BSC_TARGET");
        	searchMap.put("procParam3", searchMap.getString("targetStatusId"));
        	
        	if(null != metricIds && 0 < metricIds.length && !"".equals(searchMap.getString("year")) && !"".equals(searchMap.getString("targetStatusId"))) {
		        for (int i=0; i < metricIds.length; i++) {
		            searchMap.put("procParam4", metricIds[i]);
		            insertData("bsc.message.message.insertNoteProc", searchMap, true);
		        }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        } finally {
        	setEndTransaction();
        }
    }
    

    /**
     * 목표입력 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] mons = searchMap.getString("mons").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != mons && 0 < mons.length) {
		        for (int i = 0; i < mons.length; i++) {
		            searchMap.put("mon", mons[i]);
			searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("bsc.tam.targetMng.deleteData", searchMap);
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
     * 목표계산기 저장값 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertTargetCalCulDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	/**********************************
             * 목표계산기 저장값 등록
             **********************************/
        	returnMap = updateData("bsc.tam.targetMng.deleteTargetCalcul", searchMap, true);
        	returnMap = insertData("bsc.tam.targetMng.insertTargetCalcul", searchMap);

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
     * 반려입력 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnTarget(SearchMap searchMap) {
    	return searchMap;
    }

    /**
     * 반려조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnView(SearchMap searchMap) {

    	/**********************************
         * 반려 내용 가져오기
         **********************************/
        searchMap.addList("returnReason", getStr("bsc.tam.targetMng.getReturnReason", searchMap));

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

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("target"), "목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 목표입력 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngListExcel(SearchMap searchMap) {
    	String excelFileName = "목표입력";
    	String excelTitle = "목표입력 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	/*
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	 */

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	//excelInfoList.add(new ExcelVO("평가년도", "YEAR", "center"));
    	excelInfoList.add(new ExcelVO("입력담당자", "USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("처리상태", "TARGET_STATUS_NM", "left"));



    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetMng.getExcelList", searchMap));

        return searchMap;
    }

    /**
     * 목표입력 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngListExcel1(SearchMap searchMap) {
    	String excelFileName = "목표확인승인";
    	String excelTitle = "목표확인승인 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	/*
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	 */

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	//excelInfoList.add(new ExcelVO("평가년도", "YEAR", "center"));
    	excelInfoList.add(new ExcelVO("확인담당자", "USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("처리상태", "TARGET_STATUS_NM", "left"));



    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetMng.getExcelList1", searchMap));

        return searchMap;
    }

}

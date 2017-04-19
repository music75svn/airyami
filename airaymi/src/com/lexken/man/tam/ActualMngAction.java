/*************************************************************************
* CLASS 명      : ActualAction
* 작 업 자      : 정용훈
* 작 업 일      : 2013년 06월 24일
* 기    능      : 실적입력/승인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정용훈      2013년 06월 24일         최 초 작 업
**************************************************************************/
package com.lexken.man.tam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ActualMngAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 실적입력/승인 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	String actualType = searchMap.getString("findActualType");

    	String findMon = searchMap.getString("findMon");

    	//findMon이 공백인 경우 01을 디폴트로 입력합니다. 실적등록(입력)기간에서 평가년도 변경시 findMon이 초기화되는 예외처리
    	if("".equals(findMon)){
    		searchMap.put("findMon", "01");
    	}

    	String get_flag	 = searchMap.getString("flag");
    	String findActualStatusId = searchMap.getString("findActualStatusId");

    	if( get_flag == "" && findActualStatusId == "" ) {
    		searchMap.put("flag","1");
    	}else if( "1".equals(get_flag) && "ALLSTATUS".equals(findActualStatusId) ) {
    		searchMap.put("findActualStatusId","01");
    	}

    	if( get_flag == "" ) {
    		searchMap.put("flag","1");
    	}


    	String findUserId = searchMap.getString("findUserId");

    	 if(!loginVO.chkAuthGrp("01") || !loginVO.chkAuthGrp("60")) {

 	    	if(actualType.equals("input")) {

 	    		if("".equals(findUserId)){

 	    			if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findActualStatusId")))) {
 	    				searchMap.put("findActualStatusId", "01");
 	    			}

 	    		}

 			} else if (actualType.equals("check")) {

 				if("".equals(findUserId)) {

 					if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findActualStatusId")))) {
 						searchMap.put("findActualStatusId", "06");
 					}

 				}

 			} else if (actualType.equals("approve")) {

				if("".equals(findUserId)) {

					if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findActualStatusId")))) {
						searchMap.put("findActualStatusId", "03");
						searchMap.put("findUserId","ALL");
					}

				}
			}

 	    }

    	/**********************************
    	 * 처리상태 조회
    	 **********************************/
    	searchMap.addList("getStatusIdList", getList("man.tam.actualMng.getStatusId", searchMap));

    	/**********************************
         * 목표 입력, 승인자 조회
         **********************************/
    	searchMap.addList("userList", getList("man.tam.targetMng.getUserList", searchMap));

    	/**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
        searchMap.addList("actualInputTermYn", getStr("man.tam.actualMng.getActualInputTermYn", searchMap));

        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearClosingYn", getStr("man.system.closingYear.getYearClosingYn", searchMap));

        /**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		/*if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findUserId")))) {
    			searchMap.put("findUserId", searchMap.getDefaultValue("userList", "USER_ID", 0));
    		}*/
    	} else {
    		searchMap.put("findUserId", searchMap.get("loginUserId"));
    	}

    	/************************************
         * 입력대상 세부과제 지표조회
         ************************************/
    	searchMap.put("paramGubun", "1");
		searchMap.addList("mngTreeList1", getList("man.tam.actualMng.getMngActualTree", searchMap));
		searchMap.put("paramGubun", "2");
		searchMap.addList("mngTreeList2", getList("man.tam.actualMng.getMngActualTree", searchMap));
		searchMap.put("paramGubun", "3");
        searchMap.addList("mngTreeList3", getList("man.tam.actualMng.getMngActualTree", searchMap));
        searchMap.put("paramGubun", "4");
        searchMap.addList("mngTreeList4", getList("man.tam.actualMng.getMngActualTree", searchMap));

    	/**********************************
         * 디폴트 입력 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("manKpiId")))) {
    		searchMap.put("manKpiId", (String)searchMap.getString("findManKpiId"));
    	}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("manKpiId")))) {
    		searchMap.put("manKpiId", searchMap.getDefaultValue("mngTreeList4", "MAN_KPI_ID", 0));
    	}

        return searchMap;
    }

    /**
     * 실적 입력, 승인자 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap userList_ajax(SearchMap searchMap) {

    	/**********************************
         * 실적입력자 조회
         **********************************/

    	searchMap.addList("userList", getList("man.tam.actualMng.getUserList", searchMap));

        return searchMap;
    }

    /**
     * 실적관리 상세데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngDetailList(SearchMap searchMap) {

    	searchMap.addList("actualList", getList("man.tam.actualMng.getList", searchMap));
    	searchMap.addList("detailList", getDetail("man.tam.actualMng.getDetail", searchMap));

    	/**********************************
         * 첨부파일 조회
         **********************************/
        searchMap.addList("fileList", getList("man.tam.actualMng.getFileList", searchMap));

        return searchMap;
    }


    /**
     * 실적입력/승인 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if("ADD".equals(stMode)) {
            returnMap = this.validChk(searchMap);

            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }

        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/man/actual/"+searchMap.getString("findYear")+"/"+searchMap.getString("findMon"));

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
            searchMap = valueDB(searchMap);
        } else if("MOD".equals(stMode)) {
            //searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            //searchMap = deleteDB(searchMap);
        } else if("STATUS".equals(stMode)) {
            searchMap = statusDB(searchMap);

            String actualStatusId = searchMap.getString("actualStatusId");
            if(actualStatusId.equals("06")||actualStatusId.equals("05")||actualStatusId.equals("03")) {
            	statusProcDB(searchMap);
            }
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap	= new HashMap();
        int     resultValue	= 0;
        String[] colValues	= searchMap.getStringArray("colValues");

        for(int i=0; i < colValues.length; i++) {
        	returnMap = ValidationChk.checkCommaPointNumber(colValues[i], "실적값");

        	if((Integer)returnMap.get("ErrorNumber") < 0) {
        		return returnMap;
        	}
		}

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }


    /**
     * 실적입력/승인 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap valueDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        String[] manKpiIds	= searchMap.getStringArray("manKpiIds");
        String[] mons	 	= searchMap.getStringArray("mons");

        try {
        	setStartTransaction();

        	/**********************************
             * 실적계산
             **********************************/
        	if(manKpiIds != null && 0 < manKpiIds.length) {
        		searchMap.put("manKpiId", manKpiIds[0]);
        		searchMap.put("mon", mons[0]);

        		returnMap = insertData("man.tam.actualMng.callSpManActualProc", searchMap);
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
     * 실적입력/승인 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        String[] manKpiIds	= searchMap.getStringArray("manKpiIds");
        String[] mons	 	= searchMap.getStringArray("mons");
        String[] colValues 	= searchMap.getStringArray("colValues");
        String[] calTypeCols= searchMap.getStringArray("calTypeCols");
        String[] actStatusId 	= searchMap.getStringArray("actStatusId");

        String colId = "";

        try {
        	setStartTransaction();
        	if(manKpiIds != null && 0 < manKpiIds.length) {
        		searchMap.put("manKpiId", manKpiIds[0]);
        		searchMap.put("mon", mons[0]);
        		searchMap.put("actStatusId", actStatusId[0]);
        	}

        	/**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);

        	/**********************************
             * 월별 실적입력
             **********************************/
        	if(manKpiIds != null && 0 < manKpiIds.length) {

        		returnMap = updateData("man.tam.actualMng.deleteData", searchMap, true);

        		/**********************************
		         * 수식 알파벳 put
		         **********************************/
        		String[] calTypeColArray = new String[manKpiIds.length];
        		String[] calTypeColValArray = new String[manKpiIds.length];
        		for (int i = 0; i < manKpiIds.length; i++) {
        			calTypeColArray[i]		= calTypeCols[i];
        			calTypeColValArray[i]	= colValues[i];

        		}

        		searchMap.put("calTypeColArray", calTypeColArray);
        		searchMap.put("calTypeColValArray", calTypeColValArray);

	        	returnMap = insertData("man.tam.actualMng.insertData", searchMap);
    		}


        	/**********************************
             * 월별 점수입력
             **********************************/
        	//returnMap = updateData("man.tam.actualMng.deleteScoreData", searchMap, true);

        	//returnMap = insertData("man.tam.actualMng.insertScoreData", searchMap);


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
					returnMap = insertData("man.tam.actualMng.deleteFileInfo", searchMap);
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
						returnMap = insertData("man.tam.actualMng.insertFileInfo", searchMap);
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
     * 반려조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnView(SearchMap searchMap) {

    	/**********************************
         * 반려 내용 가져오기
         **********************************/
        searchMap.addList("returnReason", getStr("man.tam.actualMng.getReturnReason", searchMap));

    	return searchMap;
    }






























    public SearchMap insertUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("man.tam.actualMng.getInsertUserList", searchMap));

        return searchMap;
    }

    public SearchMap approveUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("man.tam.actualMng.getApproveUserList", searchMap));

        return searchMap;
    }


    /**
     * 실적관리 상세데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngDetailList2(SearchMap searchMap) {


    	String manKpiId = searchMap.getString("manKpiId");
    	String gubun = "";

    	if(!"".equals(manKpiId)) {

    		//현재 지표를 선택했는지 CSF를 선택했는지 mnaKpiId의 첫 글자를 잘라서 구분합니다.
    		gubun = manKpiId.substring(0, 1);

    		/**********************************
	         * 수식 리스트 조회 쿼리를 사용하기 위한 알파벳 put
	         **********************************/
    		char[] calTypeColArray = new char[26];

    		for (int i = 0; i < 26; i++) {
    		//65~90
    			calTypeColArray[i] = (char)((char)65+i);
    		}
    		searchMap.put("calTypeColArray", calTypeColArray);
    		searchMap.put("gubun", gubun);

    		/**********************************
             * 실적입력 월에 따른 가능 여부 가져오기
             **********************************/
            searchMap.addList("actualInputMonYn", getStr("man.tam.actualMng.getActualInputMonYn", searchMap));

    		/**********************************
	         * 월별 목표,실적 조회 , 목표에 따른 수식 리스트 조회
	         **********************************/

    		/*if("C".equals(gubun)) {
    			//CSF를 선택한 경우 DetailList.vm에 리스트들을 복수로 출력하게 됩니다. 이 부분은 팀장님의 지적으로 주석처리 되었습니다.
    			searchMap.put("csfId", manKpiId);
    			List manKpiIdList = getList("man.tam.actualMng.getManKpiIdInCsf", searchMap);

    			List list = new ArrayList();
    			HashMap calList = new HashMap();

    			for (int i = 0; i < manKpiIdList.size(); i++) {
    				searchMap.put("manKpiId", ((HashMap)manKpiIdList.get(i)).get("MAN_KPI_ID"));
    				list.add(getList("man.tam.actualMng.getList", searchMap));
    				calList.put(searchMap.get("manKpiId"), getList("man.tam.actualMng.getCalList", searchMap));
    			}

    			searchMap.addList("list", list);
    			searchMap.addList("calList", calList);
    		} else {} // 이 부분은 하단의 if와 붙어 else if 가 되는 부분이기에 사용을 하시려면 수정을 요구합니다.*/

    			if ("K".equals(gubun) ){
    			//지표(KPI)를 선택한 경우 DetailList.vm에 단수로 리스트를 출력합니다.
    			List list = new ArrayList();
    			HashMap calList = new HashMap();

				searchMap.put("manKpiId", manKpiId);
				list.add(getList("man.tam.actualMng.getList", searchMap));
				calList.put(searchMap.get("manKpiId"), getList("man.tam.actualMng.getCalList", searchMap));

    			searchMap.addList("list", list);
    			searchMap.addList("calList", calList);


		        /**********************************
		         * 실적입력상태 가져오기
		         **********************************/
    			searchMap.put("paramAction", "detail");
		        searchMap.addList("detail", getDetail("man.tam.actualMng.getActualStatusId", searchMap));

		        /**********************************
		         * 첨부파일 조회
		         **********************************/
		        searchMap.addList("fileList", getList("man.tam.actualMng.getFileList", searchMap));
    		}
    	}

        /**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
    	searchMap.addList("actualInputTermYn", getStr("man.tam.actualMng.getActualInputTermYn", searchMap));

    	 /**********************************
         * 실적입력 달 가져오기
         **********************************/
        searchMap.addList("actualInputMonYn", getStr("man.tam.actualMng.getActualInputMonYn", searchMap));

        return searchMap;
    }

    /**
     * 실적입력/승인 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngList_xml(SearchMap searchMap) {

    	 /**********************************
         * 리스트 조회
         **********************************/
        searchMap.addList("list", getList("man.tam.actualMng.getList", searchMap));

        /**********************************
         * 수식리스트 조회
         **********************************/
        searchMap.addList("callist", getList("man.tam.actualMng.getCalList", searchMap));
        return searchMap;
    }

    /**
     * 실적입력/승인 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("man.tam.actualMng.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 실적입력/승인 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngProcess2(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/str/target");

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
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 실적입력/승인 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();


        	/**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);

        	/**********************************
             * 월별 실적입력
             **********************************/

        	searchMap.put("insertUserId", searchMap.get("loginUserId"));
        	returnMap = insertData("man.tam.actualMng.insertData", searchMap);


        	/**********************************
             * 월별 점수입력
             **********************************/
        	returnMap = updateData("man.tam.actualMng.deleteScoreData", searchMap, true);

        	returnMap = insertData("man.tam.actualMng.insertScoreData", searchMap);


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
     * 실적상태 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        searchMap.put("orgManKpiId", searchMap.getString("manKpiId"));
        String[] manKpiIds = searchMap.getString("manKpiId").split("\\|", 0);
        String   returnReason = searchMap.getString("returnReason");

        try {
        	setStartTransaction();

        	if(null != manKpiIds && 0 < manKpiIds.length) {
		        for (int i=0; i < manKpiIds.length; i++) {
		            searchMap.put("manKpiId", manKpiIds[i]);
		            if(returnReason.equals("")){
		            	returnMap = updateData("man.tam.actualMng.updateStatusData", searchMap, true);
		            }else{
		            	returnMap = updateData("man.tam.actualMng.updateReturnStatusData", searchMap, true);
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
        String[] manKpiIds = searchMap.getString("orgManKpiId").split("\\|", 0);

        try {
        	setStartTransaction();

        	searchMap.put("procParam1", searchMap.getString("year"));
        	searchMap.put("procParam2", "MAN_ACTUAL");
        	searchMap.put("procParam3", searchMap.getString("actualStatusId"));

        	if(null != manKpiIds && 0 < manKpiIds.length && !"".equals(searchMap.getString("year")) && !"".equals(searchMap.getString("actualStatusId"))) {
		        for (int i=0; i < manKpiIds.length; i++) {
		            searchMap.put("procParam4", manKpiIds[i]);
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
     * 실적입력/승인 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("man.tam.actualMng.updateData", searchMap);

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
     * 실적입력/승인 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] mons = searchMap.getString("mons").split("\\|", 0);
			String[] manKpiIds = searchMap.getString("manKpiIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != mons && 0 < mons.length) {
		        for (int i = 0; i < mons.length; i++) {
		            searchMap.put("mon", mons[i]);
			searchMap.put("manKpiId", manKpiIds[i]);
		            returnMap = updateData("man.tam.actualMng.deleteData", searchMap);
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
     * 반려입력 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnActual(SearchMap searchMap) {
    	return searchMap;
    }

    /**
     * 실적입력/승인 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap actualMngListExcel(SearchMap searchMap) {
    	String excelFileName = "실적입력/승인";
    	String excelTitle = "실적입력/승인 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("LV", "LV", "left"));
    	excelInfoList.add(new ExcelVO("MON01", "MON01", "left"));
    	excelInfoList.add(new ExcelVO("MON02", "MON02", "left"));
    	excelInfoList.add(new ExcelVO("MON03", "MON03", "left"));
    	excelInfoList.add(new ExcelVO("MON04", "MON04", "left"));
    	excelInfoList.add(new ExcelVO("MON05", "MON05", "left"));
    	excelInfoList.add(new ExcelVO("MON06", "MON06", "left"));
    	excelInfoList.add(new ExcelVO("MON07", "MON07", "left"));
    	excelInfoList.add(new ExcelVO("MON08", "MON08", "left"));
    	excelInfoList.add(new ExcelVO("MON09", "MON09", "left"));
    	excelInfoList.add(new ExcelVO("MON10", "MON10", "left"));
    	excelInfoList.add(new ExcelVO("MON11", "MON11", "left"));
    	excelInfoList.add(new ExcelVO("MON12", "MON12", "left"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.tam.actualMng.getList", searchMap));

        return searchMap;
    }

}

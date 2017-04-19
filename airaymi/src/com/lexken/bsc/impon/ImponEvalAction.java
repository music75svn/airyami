/*************************************************************************
* CLASS 명      : ImponEvalAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 30일 
* 기    능      : 비계량평가실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 30일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.impon;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ImponEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량평가실시 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalList(SearchMap searchMap) {

    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponEval.getEvalDegreeList", searchMap));
    	
		//searchMap.addList("detail", getDetail("bsc.impon.imponComment.getDetail", searchMap));
		
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	
    	/**********************************
         * 평가기간 조회
         **********************************/
    	searchMap.addList("evalTermDetail", getDetail("bsc.impon.imponEval.getEvalDegreeTermDetail", searchMap));
    	
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if( loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60") ) {
    		searchMap.put("evalUserAuthId", "01");
    	} else {
    		searchMap.put("evalUserAuthId", "00");
    		if(searchMap.get("findEvalUserId") == null || "".equals(searchMap.get("findEvalUserId"))){
    			searchMap.put("findEvalUserId",loginVO.getUser_id());
    		}
    	}
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.impon.imponEval.getEvalUserGrpList", searchMap));
    	
    	String findEvalUserGrpId = (String)searchMap.get("findEvalUserGrpId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserGrpId))) {
    		searchMap.put("findEvalUserGrpId", (String)searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0));
    	}
    	
    	/**********************************
         * 평가자 조회
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")){
    		searchMap.addList("evalUserList", getList("bsc.impon.imponEval.getEvalUserList", searchMap));
    	}
    	
    	/**********************************
         * 첨부파일 조회
         **********************************/
    	searchMap.addList("fileList", getList("bsc.impon.imponEval.getFileList", searchMap));
    	
    	String findEvalUserId = (String)searchMap.get("findEvalUserId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserGrpId))) {
    		searchMap.put("findEvalUserId", (String)searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
    	}
    	
        return searchMap;
    }
    
    /**
     * 비계량평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.impon.imponEval.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가구분조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegree_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponEval.getEvalDegreeList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrp_ajax(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if( loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60") ) {
    		searchMap.put("evalUserAuthId", "01");
    	} else {
    		searchMap.put("evalUserAuthId", "00");
    	}
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.impon.imponEval.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가자조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUser_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("evalUserList", getList("bsc.impon.imponEval.getEvalUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    @SuppressWarnings("unchecked")
	public SearchMap imponEvalModify(SearchMap searchMap) {
        
    	searchMap.put("evalDegreeNm", getStr("bsc.impon.imponEval.getEvalDegreeNm", searchMap));
    	searchMap.put("evalUserNm", getStr("bsc.impon.imponEval.getEvalUserNm", searchMap));
    	searchMap.put("evalGrpNm", getStr("bsc.impon.imponEval.getEvalGrpNm", searchMap));
    	searchMap.put("evalPoolNm", getStr("bsc.impon.imponEval.getEvalPoolNm", searchMap));
    	
    	searchMap.put("totCnt", getStr("bsc.impon.imponEval.getTotCnt", searchMap));
    	searchMap.put("evalCnt", getStr("bsc.impon.imponEval.getEvalCnt", searchMap));
    	searchMap.put("evalItemCnts", getStr("bsc.impon.imponEval.getEvalItemCnts", searchMap));
		
		/**********************************
         * 평가기간 조회
         **********************************/
    	searchMap.addList("evalTermDetail", getDetail("bsc.impon.imponEval.getEvalDegreeTermDetail", searchMap));
    	
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("bsc.impon.imponEval.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("bsc.impon.imponEval.getConfirmYn", searchMap));
    	
    	ArrayList gradeList = (ArrayList)getList("bsc.impon.imponEval.getEvalGradeList", searchMap);
    	
    	String[] gradeIdArray = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeIdArray = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeIdArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("gradeIdArray", gradeIdArray);
    	searchMap.addList("gradeList", gradeList);
    	
    	searchMap.addList("evalItemCnt", getDetail("bsc.impon.imponEval.getEvalItemCnt", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalModify_xml(SearchMap searchMap) {
    	
    	ArrayList gradeList = (ArrayList)getList("bsc.impon.imponEval.getEvalGradeList", searchMap);
    	
    	String[] gradeIdArray = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeIdArray = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeIdArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("gradeIdArray", gradeIdArray);
    	searchMap.addList("gradeList", gradeList);
    	
    	searchMap.addList("evalList", getList("bsc.impon.imponEval.getEvalList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalProcess(SearchMap searchMap) {
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
        if("SUB".equals(stMode)) {
            searchMap = submitUpdateDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.impon.imponEval.insertData", searchMap);
        
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
     * 개인평가 평가실시 (현황) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap submitUpdateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.impon.imponEval.deleteSubmitData", searchMap, true);
	        
	        returnMap = insertData("bsc.impon.imponEval.insertSubmitData", searchMap);
	        
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
     * 개인평가 평가실시 (현황) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	    	
	        String[] itemGbns 	= searchMap.getString("itemGbns").split("\\|", 0);
			String[] itemIds 	= searchMap.getString("itemIds").split("\\|", 0);
			String[] seqs 		= searchMap.getString("seqs").split("\\|", 0);
			String[] gradeIds 	= searchMap.getString("gradeIds").split("\\|", 0);
			String[] ipeItemIds = searchMap.getString("ipeItemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.impon.imponEval.deleteData", searchMap, true);
	        
	        if(null != itemIds && 0 < itemIds.length) {
		        for (int i = 0; i < itemIds.length; i++) {
		            searchMap.put("itemGbn", itemGbns[i]);
					searchMap.put("seq", seqs[i]);
					searchMap.put("itemId", itemIds[i]);
					searchMap.put("gradeId", gradeIds[i]);
					searchMap.put("ipeItemId", ipeItemIds[i]);
					if(!"".equals(StaticUtil.nullToDefault(gradeIds[i], ""))){
						
						if("01".equals(itemGbns[i])){
							returnMap = insertData("bsc.impon.imponEval.insert01Data", searchMap);
						}else if("02".equals(itemGbns[i])){
							returnMap = insertData("bsc.impon.imponEval.insert02Data", searchMap);
						}
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
     * 비계량평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "비계량평가실시";
    	String excelTitle = "비계량평가실시 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "center"));
    	excelInfoList.add(new ExcelVO("평가자사번", "EVAL_USER_ID", "center"));
    	excelInfoList.add(new ExcelVO("평가자명", "USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가항목명", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가등급", "GRADE_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가등급점수", "GRADE_ITEM_SCORE", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponEval.getEvalAllList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가의견 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalFileProcess(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
        try {
        	setStartTransaction();
	        
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
	        		returnMap = insertData("bsc.impon.imponEval.deleteFileInfo", searchMap, true);
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
						returnMap = insertData("bsc.impon.imponEval.insertFileInfo", searchMap);
					}
				}
	        }
	        
	        searchMap.put("findEvalDegreeId", (String)searchMap.get("evalDegreeId"));
	        searchMap.put("findEvalUserGrpId", (String)searchMap.get("evalUserGrpId"));
	        searchMap.put("findEvalUserId", (String)searchMap.get("evalUserId"));
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }
    
}

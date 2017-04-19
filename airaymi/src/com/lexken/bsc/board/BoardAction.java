/*************************************************************************
* CLASS 명      : BoardAction
* 작 업 자      : 한봉준
* 작 업 일      : 2012년 9월 6일 
* 기    능      : 게시판관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    한봉준      2012년 9월 6일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.board;
    
import java.util.ArrayList;
import java.util.HashMap;

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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class BoardAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 게시판관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardList(SearchMap searchMap) {
    	
    	if("Y".equals(searchMap.getString("findStagnationYn"))){
	    	String[] bbsIds = searchMap.getString("findBbsIds").split("\\|", 0);
	    	  		
	    	if(null != bbsIds && 0 < bbsIds.length) {
	    		for (int i = 0; i < bbsIds.length; i++) {
	    			String paramYear = bbsIds[i].substring(0,4);
	    			if(searchMap.getString("findYear").equals(paramYear)){
	    					String paramBscId = bbsIds[i].substring(4);
	    					searchMap.put("findBbsId",paramBscId);
	    				break;
	    			}else{
	    				searchMap.put("findBbsId","");
	    			}
	    		}
	    	}	    	
    	}
    	
    	if ("findBbsTitle".equals(searchMap.get("findColName"))) {
    		searchMap.put("findBbsTitle", searchMap.get("findKeyword"));
    	} else if ("findBbsRegisterName".equals(searchMap.get("findColName"))) {
    		searchMap.put("findBbsRegisterName", searchMap.get("findKeyword"));
    	}

        return searchMap;
    }
    
    /**
     * 게시판관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardList_xml(SearchMap searchMap) {
    	
    	if ("findBbsTitle".equals(searchMap.get("findColName"))) {
    		searchMap.put("findBbsTitle", searchMap.get("findKeyword"));
    	} else if ("findBbsRegisterName".equals(searchMap.get("findColName"))) {
    		searchMap.put("findBbsRegisterName", searchMap.get("findKeyword"));
    	}
        
        searchMap.addList("list", getList("bsc.board.board.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 게시판관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardView(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.board.board.getDetail", searchMap));
    	}
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("bsc.board.board.getFileList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 게시판관리 상세화면(등록/수정)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.board.board.getDetail", searchMap));
    	} else if ("REP".equals(stMode)) {
    		searchMap.addList("replyDetail", getDetail("bsc.board.board.getDetail", searchMap));
    	}
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("bsc.board.board.getFileList", searchMap));
    	
        
        return searchMap;
    }
    
    /**
     * 게시판관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/board");
		
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("REP".equals(stMode)) {
            searchMap = insertReplyDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 게시판관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();

        	String bbsSeq = getStr("bsc.board.board.selCommonBoardMaxSeq", searchMap);
        	searchMap.put("bbsSeq", bbsSeq);

            String bbsPseq = String.valueOf(bbsSeq);
            int size = bbsPseq.length();
            size = 9 - size;
            for (int i = 0; i < size; i++) {
            	bbsPseq = "0" + bbsPseq;
            }
        	searchMap.put("bbsPseq", bbsPseq);
        	
        	returnMap = insertData("bsc.board.board.insertData", searchMap);
        	
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
     * 게시판관리 답글 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertReplyDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();

        	String bbsSeq = getStr("bsc.board.board.selCommonBoardMaxSeq", searchMap);
        	searchMap.put("bbsSeq", bbsSeq);
        	
        	returnMap = insertData("bsc.board.board.updateSeqData", searchMap);
        	returnMap = insertData("bsc.board.board.insertReplyData", searchMap);
        	
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
     * 게시판관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.board.board.updateData", searchMap);
	        
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
     * 게시판관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        String replyCount = getStr("bsc.board.board.selectReplyCount", searchMap);
	        if ("0".equals(replyCount)) {
		        returnMap = updateData("bsc.board.board.deleteData", searchMap);
	        }else{	        	
	        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_FOREIGN_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_FOREIGN_MESSAGE);
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
        
        //Validation 체크 추가
        returnMap = ValidationChk.lengthCheck(searchMap.getString("bbsTitle"), "제목", 1, 120);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("bbsContents"), "내용", 1, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
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
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("bsc.board.board.deleteFileInfo", searchMap);
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
						returnMap = insertData("bsc.board.board.insertFileInfo", searchMap);
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
     * 부진부서 KPI 상세보기
     * @param      
     * @return String  
     * @throws 
     */    
    public SearchMap stagnationDeptMetricList(SearchMap searchMap) {

    	String metricId		= searchMap.getString("metricId");
    	String analCycle 	= searchMap.getString("analCycle");
    	
    	if("".equals(metricId)){
        	metricId = getStr("bsc.board.board.getMetricId", searchMap);
        	searchMap.put("metricId", metricId);
    	}
    	
    	if("".equals(analCycle)){
    		searchMap.put("analCycle", "Y");
    	}
    	
    	//URL 주소 가져오기
    	searchMap.addList("url", getDetail("bsc.mon.metricActualDetail.getUrl", searchMap));
    	
    	//지표상세정보
    	HashMap detail = new HashMap();
    	searchMap.addList("metricInfo", detail=(HashMap)getDetail("bsc.module.commModule.getMetricInfo", searchMap));

    	detail = getDetail("bsc.module.commModule.getMetricInfo", searchMap);
    	searchMap.put("scDeptId", detail.get("SC_DEPT_ID"));
		searchMap.put("strategyId", detail.get("STRATEGY_ID"));
		searchMap.put("metricId", detail.get("METRIC_ID"));
		searchMap.put("userId", detail.get("INSERT_USER_ID"));
		searchMap.put("approveUserId", detail.get("APPROVE_USER_ID"));
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	
        //전략과제명
    	searchMap.addList("strategyNm", getStr("bsc.module.commModule.getStrategyNm", searchMap));
    	
        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));
    	
    	//지표실적확인자명
    	searchMap.addList("approveUserNm", getStr("bsc.board.board.getApproveUserNm", searchMap));
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));
    	
    	//지표검색
    	searchMap.addList("metricList", getList("bsc.board.board.getMetricList", searchMap));
    	
    	//목표 실적 점수
    	searchMap.addList("scoreList", getList("bsc.mon.metricActualDetail.getScoreList", searchMap));
    	
    	//실적산식 조회
    	String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
    	String calTypeColDesc = "";
        if(actCalTypeNm!=null && !"".equals(actCalTypeNm)){
        	HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
        	ArrayList calTypeColList = (ArrayList)getList("bsc.tam.actualMng.calTypeColList", searchMap);
        	
        	if(null != calTypeColList && 0 < calTypeColList.size()) {
        		for (int i = 0; i < calTypeColList.size(); i++) {
        			HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
        			calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
        		}
        	}
        	
        	//실적산식명 가져오기
        	calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
        }
        
        searchMap.addList("calTypeColDesc", calTypeColDesc);        
    	
        //산식항복별 실적 가져오기
        String[] months = {"A", "B", "C", "D", "E", "F", "G", "H",
        			       "I", "J", "K", "L", "M", "N", "O", "P",
        			       "Q", "R","S", "T", "U", "V", "W", "X", "Y", "Z",
        				   "AA", "BB", "CC", "DD", "EE", "FF", "GG","HH",
        				   "II", "JJ", "KK", "LL", "MM", "NN","OO", "PP",
        				   "QQ", "RR", "SS", "TT", "UU","VV", "WW", "XX", "YY", "ZZ"};
        
        searchMap.put("months", months);
        searchMap.addList("colValueList", getList("bsc.mon.metricActualDetail.getActualColValue", searchMap));        
        
      //목표 실적 점수
    	searchMap.addList("detail", getDetail("bsc.board.board.stagnationDetailData", searchMap));
        
    	return searchMap;
    }  
    
    /**
     * 부진부서 KPI 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stagnationDeptMetricProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validStagnationChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }		
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertStagnationDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 부진부서 KPI 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertStagnationDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = insertData("bsc.board.board.insertStagnationData", searchMap);
        
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
     *  부진부서 KPI Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validStagnationChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;        
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("resultContents"), "실적", 0, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("resultCoachContents"), "문제점 및 성과코칭", 0, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("insertUserContents"), "지표확인자입력", 0, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }    
        
    
    
    /**
     * 게시판관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap boardListExcel(SearchMap searchMap) {
    	String excelFileName = "게시판관리";
    	String excelTitle = "게시판관리 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("BBS_ID", "BBS_ID", "left"));
    	excelInfoList.add(new ExcelVO("BBS_SEQ", "BBS_SEQ", "left"));
    	excelInfoList.add(new ExcelVO("BBS_PSEQ", "BBS_PSEQ", "left"));
    	excelInfoList.add(new ExcelVO("BBS_SORT1", "BBS_SORT1", "left"));
    	excelInfoList.add(new ExcelVO("BBS_SORT2", "BBS_SORT2", "left"));
    	excelInfoList.add(new ExcelVO("BBS_DEPT", "BBS_DEPT", "left"));
    	excelInfoList.add(new ExcelVO("BBS_CATEGORY", "BBS_CATEGORY", "left"));
    	excelInfoList.add(new ExcelVO("BBS_TITLE", "BBS_TITLE", "left"));
    	excelInfoList.add(new ExcelVO("BBS_CONTENTS", "BBS_CONTENTS", "left"));
    	excelInfoList.add(new ExcelVO("BBS_REGISTER", "BBS_REGISTER", "left"));
    	excelInfoList.add(new ExcelVO("BBS_REGISTER_NAME", "BBS_REGISTER_NAME", "left"));
    	excelInfoList.add(new ExcelVO("BBS_PASSWD", "BBS_PASSWD", "left"));
    	excelInfoList.add(new ExcelVO("BBS_READ", "BBS_READ", "left"));
    	excelInfoList.add(new ExcelVO("BBS_RECOMMAND", "BBS_RECOMMAND", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("수정일자", "UPDATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("삭제일자", "DELETE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.board.board.getList", searchMap));
    	
        return searchMap;
    }
    
}

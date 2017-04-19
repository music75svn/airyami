/*************************************************************************
* CLASS 명      : RptAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 05월 31일 
* 기    능      : 간부업적평가-자기성과기술서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 05월 31일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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

public class RptAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 자기성과기술서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rptList(SearchMap searchMap) {
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
		if(loginVO.chkAuthGrp("81") && !loginVO.chkAuthGrp("01")) {
			searchMap.put("findEvalMembEmpn", loginVO.getUser_id());
			searchMap.put("findDeptCd", loginVO.getDept_cd());
    	}else{
    		searchMap.put("findEvalMembEmpn", "");
    		searchMap.put("findDeptCd", "");
    	}
    	
    	searchMap.addList("rptSchedule", getDetail("prs.mng.rpt.getRptSchedule", searchMap));//자기평가기술서 제출기간조회
    	searchMap.addList("evalClosing", getDetail("prs.mng.rpt.getClosing", searchMap)); //평가마감여부 조회
    	return searchMap;
    }
    
    /**
     * 자기성과기술서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rptList_xml(SearchMap searchMap) {
    	
		searchMap.addList("list", getList("prs.mng.rpt.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 자기성과기술서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	//searchMap.addList("detail", getList("prs.mng.rpt.getRptDetail", searchMap));
    	searchMap.addList("rptSchedule", getDetail("prs.mng.rpt.getRptSchedule", searchMap));
    	
    	// 전년도 소속부서 정보를 가져온다.
    	List lastDept = getList("prs.mng.rpt.getRptDept", searchMap);
    	if (lastDept == null || lastDept.size() == 0) {
    		lastDept = getList("prs.mng.rpt.getLastYearDeptFromPcmt", searchMap);
    	}
    	searchMap.addList("lastDept", lastDept);
    	
    	// 자기성과기술서 성과목표리스트 가져온다.
    	List rptDetail = getList("prs.mng.rpt.getRptDetail", searchMap); // 상세정보
    	/*if (rptMember == null || rptMember.size() == 0) {
    		rptMember = getList("prs.mng.rpt.getRptReMember", searchMap);
    	}*/
    	
    	searchMap.addList("rptDetail", rptDetail);
    	
    	HashMap rpt = getDetail("prs.mng.rpt.getRptMember", searchMap);//content를 포함한 대상자의 정보
    	
    	if(rpt == null){
    		rpt = getDetail("prs.mng.rpt.getRptReMember", searchMap);//대상자의 정보
    		//rpt.put("CONTENT", getRptString(searchMap));
    		rpt.put("CONTENT", getRptString2(searchMap));
    	}else{
	    	oracle.sql.CLOB clob = ((oracle.sql.CLOB)rpt.get("CONTENT"));
			String content = "";
			Reader reader = null;
			char[] cbuf;
			
			try {
				if(null != clob){
					reader = clob.getCharacterStream();
					cbuf = new char[(int)(clob.length())];
					reader.read(cbuf);
					content = new String(cbuf);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				rpt.put("CONTENT", content);
			}
    	}
    	
    	searchMap.addList("rpt", rpt);
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.mng.rpt.getFileList", searchMap));
    	
        return searchMap;
    }
    
    private String getRptString(SearchMap searchMap) {
    	StringBuffer sb = new StringBuffer();
    	
    	List evalItems = getList("prs.mng.rpt.getEvalItem", searchMap);
    	
    	sb.append("<p>&nbsp;</p>");
    	sb.append("<div style=\"margin: 5px; clear: both;\">");
    	sb.append("   <div style=\"margin: 5px; clear: both;\">");
    	sb.append("      <table border=\"0\" cellSpacing=\"1\" cellPadding=\"2\" width=\"100%\" bgColor=\"gray\" align=\"center\">");
    	sb.append("         <tbody>");
    	sb.append("	    <tr>");
    	sb.append("	       <td style=\"text-align: center;\">");
    	sb.append("	          <font style=\"font-family: 굴림체; font-size: 18pt;\"><strong>요약</strong></font>");
    	sb.append("	       </td>");
    	sb.append("	    </tr>");
    	sb.append("	    <tr>");
    	sb.append("	       <td style=\"color: black; background-color: white;\">");
    	sb.append("	          <p><font style=\"font-size: 10pt;\">&#9671</font></p>");
    	sb.append("		  <p><font style=\"font-size: 9pt;\">&#9671</font></p>");
    	sb.append("		  <p><font style=\"font-size: 9pt;\">&#9671</font></p>");
    	sb.append("	       </td>");
    	sb.append("	    </tr>");
    	sb.append("	 </tbody>");
    	sb.append("      </table>");
    	sb.append("   </div>");
    	sb.append("   <p>&nbsp;</p>");
    	sb.append("   <p>&nbsp;</p>");
    	sb.append("   <p><font style=\"font-family: 굴림체; font-size: 14pt;\"><strong>&#91주요내용&#93</strong></font></p>");
    	sb.append("   <p><strong><font style=\"font-family: 굴림체; font-size: 10pt;\"></font></strong>&nbsp;</p>");
    	
    	for (int i = 0; i < evalItems.size(); i++) {
    		String item = (i + 1) + ". " + ((HashMap)evalItems.get(i)).get("EVAL_ITEM_NM");
    		sb.append("   <p><strong><font style=\"font-family: 굴림체; font-size: 12pt;\">" + item + "</font></strong></p>");
        	sb.append("   <p><font style=\"font-family: 굴림체; font-size: 10pt;\">&nbsp;&nbsp;&nbsp; &#9675 </font></p>");
        	sb.append("   <p><font style=\"font-family: 굴림체; font-size: 10pt;\">&nbsp;</font></p>");
    	}
    	
    	sb.append("</div>");
    	   
    	return sb.toString();
    }
    
    private String getRptString2(SearchMap searchMap) {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append("<p>&nbsp;</p>");
    	sb.append("<div style=\"margin: 5px; clear: both;\">");
    	sb.append("   <p>&nbsp;</p>");
		sb.append("   <p><strong><font style=\"font-family: 굴림체; font-size: 24pt;\">" + "&#91보고서 아래 첨부파일 참조&#93" + "</font></strong></p>");
    	sb.append("</div>");
    	   
    	return sb.toString();
    }
    
    /**
     * 자기성과기술서 저장/제출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rptProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/mng_rpt");
		
        /**********************************
         * 저장/제출
         **********************************/
        if("SAVE".equals(stMode)) {
        	searchMap.put("submitYn", "N");
            searchMap = saveDB(searchMap);
        } else if("SUBMIT".equals(stMode)) {
        	searchMap.put("submitYn", "Y");
            searchMap = saveDB(searchMap);
        } else if("CANCEL".equals(stMode)) {
        	searchMap = cancelDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 자기성과기술서 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
//        	returnMap = insertData("prs.mng.rpt.saveData", searchMap);
        	returnMap = deleteData("prs.mng.rpt.deleteData", searchMap, true);
        	returnMap = insertData("prs.mng.rpt.insertData", searchMap);

        	String[] lastDeptCd 	= searchMap.getStringArray("lastDeptCd");
        	String[] startPcmtDate 	= searchMap.getStringArray("startPcmtDate");
        	String[] endPcmtDate 	= searchMap.getStringArray("endPcmtDate");
        	
        	returnMap = deleteData("prs.mng.rpt.deleteLastYearDept", searchMap, true);
        	
        	if(null != lastDeptCd ){
	        	for (int i = 0; i < lastDeptCd.length; i++) {
	        		searchMap.put("seq", i);
	        		searchMap.put("lastDeptCd", lastDeptCd[i]);
	        		searchMap.put("fromDt", startPcmtDate[i]);
	        		searchMap.put("toDt", endPcmtDate[i]);
	        		
	        		returnMap = insertData("prs.mng.rpt.insertLastYearDept", searchMap);
	        	}
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
     * 자기성과기술서 제출 취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.mng.rpt.cancelData", searchMap);
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
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("prs.mng.rpt.deleteFileInfo", searchMap);
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
						returnMap = insertData("prs.mng.rpt.insertFileInfo", searchMap);
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
}

/*************************************************************************
* CLASS 명      : ExcRptAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 15일 
* 기    능      : 별도평가대상-사업추진보고서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안 요 한      2013년 07월 15일    최 초 작 업 
**************************************************************************/
package com.lexken.cbe.exc;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class ExcRptAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 별도평가대상 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excRptList(SearchMap searchMap) {
    	
    	searchMap.addList("rptSchedule", getDetail("cbe.exc.excRpt.getRptSchedule", searchMap));
    	
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 별도평가대상 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excRptList_xml(SearchMap searchMap) {
        
		searchMap.addList("list", getList("cbe.exc.excRpt.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사업추진보고서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excRptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	String excEvalTargetGubun = searchMap.getString("excEvalTargetGubun");
    	HashMap excRpt = new HashMap();
    	
    	//확정여부 확인
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));
    	
		//제출기한 확인
    	searchMap.addList("rptSchedule", getDetail("cbe.exc.excRpt.getRptSchedule", searchMap));
	    
    	//사업추진보고서 정보를 가져온다
    	excRpt = getDetail("cbe.exc.excRpt.getMngRpt", searchMap);
    	
	    	if("D".equals(excEvalTargetGubun)) {
	    		
		    	if (excRpt == null || excRpt.size() == 0) {
		    		
		    		excRpt = getDetail("cbe.exc.excRpt.getExcRptInfo", searchMap);
		    		
		    	}
	    	} else {
	    		if (excRpt == null || excRpt.size() == 0) {
	    			
	    			excRpt = getDetail("cbe.exc.excRpt.getMngRptFromInsa", searchMap);
	    			
	    			List lastDept = getList("cbe.exc.excRpt.getLastYearDept", searchMap);
	    			
		    		excRpt.put("DUTY_START_DT", ((HashMap)lastDept.get(0)).get("START_PCMT_DATE"));
		    		excRpt.put("DUTY_END_DT", ((HashMap)lastDept.get(lastDept.size() - 1)).get("END_PCMT_DATE"));
	    			
	    		} else {
	    		
//	    		if (excRpt == null) excRpt = new HashMap();
	    		
		    		List lastDept = getList("cbe.exc.excRpt.getLastYearDept", searchMap);
		    		
		    		excRpt.put("DUTY_START_DT", ((HashMap)lastDept.get(0)).get("START_PCMT_DATE"));
		    		excRpt.put("DUTY_END_DT", ((HashMap)lastDept.get(lastDept.size() - 1)).get("END_PCMT_DATE"));
	    		}
	    		
	    	}
	    	
	    	oracle.sql.CLOB clob = ((oracle.sql.CLOB)excRpt.get("CONTENT"));
			String content = "";
			Reader reader = null;
			char[] cbuf;
			
			try {
				if (clob != null) {
	    			reader = clob.getCharacterStream();
	    			cbuf = new char[(int)(clob.length())];
	    			reader.read(cbuf);
	    			content = new String(cbuf);
				} else {
					content = getRptString(searchMap);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}finally {
				excRpt.put("CONTENT", content);
			}
	    	
	    	
	    	searchMap.addList("excRpt", excRpt);
        
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("cbe.exc.excRpt.getFileList", searchMap));
    	
        return searchMap;
        
    }
    
    	private String getRptString(SearchMap searchMap) {
        	StringBuffer sb = new StringBuffer();
        	
        	sb.append("<p>&nbsp;</p>");
        	sb.append("<div style=\"margin: 5px; clear: both;\">");
        	sb.append("   <div style=\"margin: 5px; clear: both;\">");
        	sb.append("      <table border=\"0\" cellSpacing=\"1\" cellPadding=\"2\" width=\"100%\" bgColor=\"gray\" align=\"center\">");
        	sb.append("         <tbody>");
        	sb.append("	    <tr>");
        	sb.append("	       <td style=\"text-align: center;\">");
        	sb.append("	          <font style=\"font-family: 바탕체; font-size: 16pt;\"><strong><주요내용 요약></strong></font>");
        	sb.append("	       </td>");
        	sb.append("	    </tr>");
        	sb.append("	    <tr>");
        	sb.append("	       <td style=\"color: black; background-color: white;\">");
        	sb.append("		  <p><font style=\"font-family: 휴먼명조; font-size: 16pt;\">&#9671</font></p>");
        	sb.append("	          <p><br></p>");
        	sb.append("		  <p><font style=\"font-family: 휴먼명조; font-size: 16pt;\">&#9671</font></p>");
        	sb.append("	       </td>");
        	sb.append("	    </tr>");
        	sb.append("	 </tbody>");
        	sb.append("      </table>");
        	sb.append("   </div>");
        	sb.append("   <p>&nbsp;</p>");
        	sb.append("   <p>&nbsp;</p>");
        	sb.append("   <p><font style=\"font-family: HY헤드라인M; font-size: 16pt;\"><strong>&#91내용&#93</strong></font></p>");
        	sb.append("   <p><strong><font style=\"font-family: 휴먼명조; font-size: 15pt;\"></font></strong>&nbsp;</p>");
        	
        	sb.append("</div>");
        	   
        	return sb.toString();
        }
    
    /**
     * 사업추진보고서 저장/제출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excRptProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            //returnMap = this.validChk(searchMap);
            
//            if((Integer)returnMap.get("ErrorNumber") < 0 ){
//                searchMap.addList("returnMap", returnMap);
//                return searchMap;
//            }
        }
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/exc_rpt");
		
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
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 사업추진실적 보고서 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String excEvalTargetGubun = searchMap.getString("excEvalTargetGubun");
        
        try {
        	setStartTransaction();
        	
//        	returnMap = insertData("prs.mng.rpt.saveData", searchMap);
        	returnMap = deleteData("cbe.exc.excRpt.deleteData", searchMap, true);
        	
        	searchMap.put("rptId", getStr("cbe.exc.excRpt.getRptId", searchMap));
        	
        	 if("D".equals(excEvalTargetGubun)) {

        		 returnMap = insertData("cbe.exc.excRpt.insertDeptData", searchMap);
        		 
        	 }else {
        		 
        		 returnMap = insertData("cbe.exc.excRpt.insertUserData", searchMap);
        		 
        	 }
        	
//        	String[] lastDeptCd 	= searchMap.getStringArray("lastDeptCd");
//        	String[] startPcmtDate 	= searchMap.getStringArray("startPcmtDate");
//        	String[] endPcmtDate 	= searchMap.getStringArray("endPcmtDate");
//        	
//        	returnMap = deleteData("prs.mng.rpt.deleteLastYearDept", searchMap, true);
//        	
//        	for (int i = 0; i < lastDeptCd.length; i++) {
//        		searchMap.put("seq", i);
//        		searchMap.put("lastDeptCd", lastDeptCd[i]);
//        		searchMap.put("fromDt", startPcmtDate[i]);
//        		searchMap.put("toDt", endPcmtDate[i]);
//        		
//        		returnMap = insertData("cbe.excRpt.excRpt.insertLastYearDept", searchMap);
//        	}
        	
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
     * 사업추진실적 보고서 제출 취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("cbe.exc.excRpt.cancelData", searchMap);
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
     * 사업추진실적 보고서 확정 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = updateData("cbe.exc.excRpt.insertData", searchMap); //확정여부 수정
    		
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
					returnMap = insertData("cbe.exc.excRpt.deleteFileInfo", searchMap);
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
						returnMap = insertData("cbe.exc.excRpt.insertFileInfo", searchMap);
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

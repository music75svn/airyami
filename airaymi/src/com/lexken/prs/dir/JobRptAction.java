/*************************************************************************
* CLASS 명      : JobRptAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 임원평가-직무성과기술서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 06월 25일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;

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

public class JobRptAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직무성과기술서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap jobRptList(SearchMap searchMap) {
    	
    	searchMap.addList("jobRptSchedule", getDetail("prs.dir.jobRpt.getRptSchedule", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직무성과기술서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap jobRptList_xml(SearchMap searchMap) {
        
		searchMap.addList("list", getList("prs.dir.jobRpt.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직무성과기술서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap jobRptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("jobRptSchedule", getDetail("prs.dir.jobRpt.getRptSchedule", searchMap)); //직무성과제출기간 조회
    	
    	// 직무성과기술서 정보를 가져온다.
    	HashMap rpt = getDetail("prs.dir.jobRpt.getDirRpt", searchMap);
    	
		oracle.sql.CLOB clob = ((oracle.sql.CLOB)rpt.get("RPT_CONTENT"));
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
			rpt.put("RPT_CONTENT", content);
		}
		
    	searchMap.addList("rpt", rpt);
        
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.dir.jobRpt.getFileList", searchMap));
    	
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
    	
    	sb.append("</div>");
    	   
    	return sb.toString();
    }
    
    /**
     * 직무성과기술서 저장/제출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap jobRptProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/dir_rpt");
		
        /**********************************
         * 저장/제출
         **********************************/
        if("SAVE".equals(stMode)) {
        	searchMap.put("rptSubmitYn", "N");
            searchMap = saveDB(searchMap);
        } else if("SUBMIT".equals(stMode)) {
        	searchMap.put("rptSubmitYn", "Y");
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
     * 직무성과기술서 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.dir.jobRpt.updateData", searchMap);

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
     * 직무성과기술서 제출 취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.dir.jobRpt.cancelData", searchMap);
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
					returnMap = insertData("prs.dir.jobRpt.deleteFileInfo", searchMap);
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
						returnMap = insertData("prs.dir.jobRpt.insertFileInfo", searchMap);
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

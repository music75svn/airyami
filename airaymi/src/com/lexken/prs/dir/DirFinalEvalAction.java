/*************************************************************************
* CLASS 명      : DirEvalAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 임원평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방 승 현      2013년 06월 25일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class DirFinalEvalAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
	private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
	public SearchMap dirEvalList(SearchMap searchMap) {
    	searchMap.addList("evalYn", getDetail("prs.dir.dirFinalEval.getList", searchMap));// 마감여부
    	HashMap rpt = getDetail("prs.dir.dirFinalEval.getGradeCntList", searchMap);

    	searchMap.addList("gradeCntlist", rpt);
    	
    	searchMap.addList("evalSchedule", getDetail("prs.dir.dirFinalEval.getEvalSchedule", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.dir.dirFinalEval.getList_xml", searchMap));
        
    
      // searchMap.addList("gradeCntlist", getList("prs.dir.dirFinalEval.getGradeCntList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if("CON".equals(stMode)) {
        	searchMap = updateDB2(searchMap);
       }
        
         return searchMap;
    }
    
    /**
     * 평가결과 입력
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] empns = searchMap.getStringArray("empn");
            String[] effScore = searchMap.getStringArray("effScore");
            
            String[] finalScore = searchMap.getStringArray("finalScore"); 
            String[] finalGrade = searchMap.getStringArray("finalGrade"); 
            String[] finalRanking = searchMap.getStringArray("finalRanking");
            
            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);            		
            		searchMap.put("effScore", effScore[i]);
            		searchMap.put("finalScore", finalScore[i]);
            		searchMap.put("finalGrade", finalGrade[i]);
            		searchMap.put("finalRanking", finalRanking[i]);
 
        			if(!"".equals(empns[i]) ){
        				returnMap = updateData("prs.dir.dirFinalEval.updateEvalData", searchMap);
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
     * 평가결과 입력 및 확정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	  String[] empns = searchMap.getStringArray("empn");
              String[] effScore = searchMap.getStringArray("effScore");
              
              String[] finalScore = searchMap.getStringArray("finalScore"); 
              String[] finalGrade = searchMap.getStringArray("finalGrade"); 
              String[] finalRanking = searchMap.getStringArray("finalRanking");
              
              for(int i=0; i<empns.length; i++){
              	if(null != empns[i]){
              		searchMap.put("empn", empns[i]);            		
              		searchMap.put("effScore", effScore[i]);
              		searchMap.put("finalScore", finalScore[i]);
              		searchMap.put("finalGrade", finalGrade[i]);
              		searchMap.put("finalRanking", finalRanking[i]);
   
          			if(!"".equals(empns[i]) ){
          				returnMap = updateData("prs.dir.dirFinalEval.updateEvalData", searchMap);
          			}
              	}
              }
            
            returnMap = updateData("prs.dir.dirFinalEval.updateEvalConfirm", searchMap);
            returnMap = insertData("prs.dir.dirFinalEval.updateExceptGrade", searchMap);
            
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
     * 평가결과 입력 및 확정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB3(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();        	
            
            returnMap = updateData("prs.dir.dirFinalEval.updateEvalFinalConfirm", searchMap);
            
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
     * 평가자가 평가대상자가 제출한 자기성과기술서를 조회
     * @param searchMap
     * @return
     */
    public SearchMap showRpt(SearchMap searchMap) {
    	
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
     * 평가결과 대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalResultList(SearchMap searchMap) {
    	// 마감여부
    	searchMap.addList("evalYn", getDetail("prs.dir.dirFinalEval.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가결과 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalResultList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("prs.dir.dirFinalEval.getEvalResultList_xml", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
         if("RST".equals(stMode)) { //평가결과 확정
        	searchMap = updateDB3(searchMap);
        }
        
         return searchMap;
    }
    
}

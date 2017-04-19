/*************************************************************************
* CLASS 명      : ScDeptDiagramAction
* 작 업 자      : 형광민
* 작 업 일      : 2012년 9월 6일 
* 기    능      : 조직성과도
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    형광민      2012년 9월 6일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.NoteSendManager;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.OpnionSendManager;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class ScDeptDiagramAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직성과도 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagramList(SearchMap searchMap) {
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	//searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
    	HashMap topScDeptMap = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topScDeptMap)) {
    		topScDeptMap = new HashMap();
    	}
    	
    	/**********************************
         * 파라미터가 null로 들어올 때 디폴트값 셋팅
         **********************************/
    	String findScDeptId = (String)searchMap.getString("findScDeptId", (String)topScDeptMap.get("SC_DEPT_ID"));	// 조직코드가 없으면 본인조직코드를 셋팅.
    	String findScDeptNm = (String)searchMap.getString("findScDeptNm", (String)topScDeptMap.get("SC_DEPT_NM"));	// 조직명이 없으면 본인조직명을 셋팅.
    	
    	
    	String gubun = (String)searchMap.getString("findDiagramGubun", "");	// 지역본부 지사
    	if(gubun.equals("02")) {
    		HashMap scDeptMap = (HashMap)getDetail("bsc.mon.scDeptDiagram.getDiagramGubunDept", searchMap);
    		if(StaticUtil.isEmpty(scDeptMap)) {
    			scDeptMap = new HashMap();
        	}
    		
    		findScDeptId = (String)scDeptMap.get("SC_DEPT_ID");
    		findScDeptNm = (String)scDeptMap.get("SC_DEPT_NM");
    	}
    	
    	/**********************************
         * 디폴트 조회조건 설정
         **********************************/
    	searchMap.put("findScDeptId", findScDeptId);
    	searchMap.put("findScDeptNm", findScDeptNm);

        return searchMap;
    }
    
    /**
     * 조직성과도 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagramList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.mon.scDeptDiagram.getList", searchMap));

        return searchMap;
    }

    /**
     * 조직성과도 조직순위 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgRankList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.mon.scDeptDiagram.getOrgRankList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagramMetricChart_xml(SearchMap searchMap) {
        
    	//차트 목표
    	searchMap.addList("target", getDetail("bsc.mon.scDeptDiagram.getChartTarget", searchMap));
        
    	//차트 실적
    	searchMap.addList("actual", getDetail("bsc.mon.scDeptDiagram.getChartActual", searchMap));
    	
    	//차트 점수
    	searchMap.addList("score", getDetail("bsc.mon.scDeptDiagram.getChartScore", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적관리 상세데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap causeMetricView(SearchMap searchMap) {
	        
    	String metricId = searchMap.getString("metricId");
    	
    	if(!"".equals(metricId)) {
	        
    		/**********************************
	         * 실적입력자 조회
	         **********************************/
    		String userId = getStr("bsc.mon.scDeptDiagram.getUserId", searchMap);
        	
    		searchMap.put("userId", userId);
    		
	        /**********************************
	         * 미진사유 조회
	         **********************************/
	        searchMap.addList("causeDetail", getDetail("bsc.mon.scDeptDiagram.getCause", searchMap));
	        
	        /**********************************
	         * 첨부파일 조회
	         **********************************/
	        searchMap.addList("fileList", getList("bsc.mon.scDeptDiagram.getFileList", searchMap));
	        
    	}
    	
        return searchMap;
    }
    
    /**
     * 조직성과도 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagramModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.mon.scDeptDiagram.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 조직성과도 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagramProcess(SearchMap searchMap) {
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
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("OPNION".equals(stMode)) {
        	searchMap = updateOpnionDB(searchMap);
        	searchMap = sendOpnionDB(searchMap);
        }else if("DESC".equals(stMode)) {
        	searchMap = updateDescDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 조직성과도 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.mon.scDeptDiagram.insertData", searchMap);
        
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
     * 의견 쪽지보내기 처리 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap sendOpnionDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	
    	String content = searchMap.getString("opnion");

    	try {
    		
	    	ArrayList UserList = new ArrayList();
	    	UserList = (ArrayList)getList("bsc.mon.scDeptDiagram.getUserList", searchMap);
	        String[] targetId = new String[0];
	        if(null != UserList && 0 < UserList.size()) {
	        	targetId = new String[UserList.size()];
	        	for (int i = 0; i < UserList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)UserList.get(i);
		        	
		        	targetId[i] = (String)t.get("SEND_USER");
		        	searchMap.put("targetId", targetId[i]);
		        	searchMap.put("message", content);
		        	
		        	send.OpnionSend(searchMap);
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
     * 조직성과도 의견 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateOpnionDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	
    	String content = searchMap.getString("opnion");
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("bsc.mon.scDeptDiagram.updateOpnionDB", searchMap);
    		
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
     * 조직성과도 미진사유 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDescDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	
    	String content = searchMap.getString("desc");
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("bsc.mon.scDeptDiagram.updateDescDB", searchMap);
    		
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
     * 조직성과도 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.mon.scDeptDiagram.updateData", searchMap);
	        
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
     * 조직성과도 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] scDeptDiagramIds = searchMap.getString("scDeptDiagramIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < scDeptDiagramIds.length; i++) {
	            searchMap.put("scDeptDiagramId", scDeptDiagramIds[i]);
	            returnMap = updateData("bsc.mon.scDeptDiagram.deleteData", searchMap);
	        }
	        
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    */   
        
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 조직성과도 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap scDeptDiagramListExcel(SearchMap searchMap) {
    	String excelFileName = "조직성과도";
    	String excelTitle = "조직성과도 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("성과조직코드", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("성과조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("상위성과조직코드", "UP_SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("프로그램레벨(메뉴레", "LEVEL_ID", "left"));
    	excelInfoList.add(new ExcelVO("LEVEL1", "LEVEL1", "left"));
    	excelInfoList.add(new ExcelVO("조직평가그룹", "SC_DEPT_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("조직도형", "DEPT_KIND", "left"));
    	excelInfoList.add(new ExcelVO("SUB_DEPT_X_POS", "SUB_DEPT_X_POS", "left"));
    	excelInfoList.add(new ExcelVO("SUB_DEPT_Y_POS", "SUB_DEPT_Y_POS", "left"));
    	excelInfoList.add(new ExcelVO("평가진행상태", "STATUS", "left"));
    	excelInfoList.add(new ExcelVO("STATUS_NM", "STATUS_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("MAP_CNT", "MAP_CNT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.scDeptDiagram.getList", searchMap));
    	
        return searchMap;
    }
    
}

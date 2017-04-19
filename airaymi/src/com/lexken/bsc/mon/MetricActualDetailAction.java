/*************************************************************************
* CLASS 명      : MetricActualDetailAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 29일 
* 기    능      : 월별지표실적상세
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 29일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.OpnionSendManager;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricActualDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 월별지표실적상세 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActualDetailList(SearchMap searchMap) {

    	if("".equals(searchMap.get("analCycle"))){
    		searchMap.put("analCycle", "Y");
    	}
    	
    	
    	//지표상세정보
    	HashMap detail = new HashMap();
    	searchMap.addList("metricInfo", detail=(HashMap)getDetail("bsc.module.commModule.getMetricInfo", searchMap));

    	detail = getDetail("bsc.module.commModule.getMetricInfo", searchMap);
		searchMap.put("userId", detail.get("INSERT_USER_ID"));
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	
        //전략과제명
    	searchMap.addList("strategyNm", getStr("bsc.module.commModule.getStrategyNm", searchMap));
    	
        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));

        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));
    	
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
        
    	return searchMap;
    }

    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActualDetailChart_xml(SearchMap searchMap) {
        
    	//차트 목표
    	searchMap.addList("target", getDetail("bsc.mon.metricActualDetail.getChartTarget", searchMap));
        
    	//차트 실적
    	searchMap.addList("actual", getDetail("bsc.mon.metricActualDetail.getChartActual", searchMap));
    	
    	//차트 점수
    	searchMap.addList("score", getDetail("bsc.mon.metricActualDetail.getChartScore", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricActualDetailProcess(SearchMap searchMap) {
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
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.mon.metricActualDetail.insertData", searchMap);
        
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
     * 월별지표실적상세 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.mon.metricActualDetail.updateData", searchMap);
	        
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
     * 월별지표실적상세 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] metricActualDetailIds = searchMap.getString("metricActualDetailIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < metricActualDetailIds.length; i++) {
	            searchMap.put("metricActualDetailId", metricActualDetailIds[i]);
	            returnMap = updateData("bsc.mon.metricActualDetail.deleteData", searchMap);
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
    
    public SearchMap popMetricActualDetailList(SearchMap searchMap) {

    	if("".equals(searchMap.get("analCycle"))){
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
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));

    	
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
        
        if(!"".equals(searchMap.getString("metricId"))) {
        	if("".equals(searchMap.getString("mon"))){
        		searchMap.put("mon", searchMap.getString("findMon"));
        	}
        	if("".equals(searchMap.getString("year"))){
        		searchMap.put("year", searchMap.getString("findYear"));
        	}
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
     * 월별지표실적상세 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricActualDetailListExcel(SearchMap searchMap) {
    	String excelFileName = "월별지표실적상세";
    	String excelTitle = "월별지표실적상세 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("KPI코드", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI 타입", "TYPE_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI 명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("기준월", "MON", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT", "left"));
    	excelInfoList.add(new ExcelVO("기간실적 누적방법", "TIME_ROLLUP", "left"));
    	excelInfoList.add(new ExcelVO("산식패턴", "CAL_TYPE_NM", "left"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("TARGETY", "TARGETY", "left"));
    	excelInfoList.add(new ExcelVO("사용자명", "USER_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.metricActualDetail.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricActualDetailProcess(SearchMap searchMap) {
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
        if("OPNION".equals(stMode)) {
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
     * 조직성과도 의견 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDescDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	
    	String content = searchMap.getString("causeDesc");
    	
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
     * 의견 쪽지보내기 처리 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap sendOpnionDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	
    	String content = searchMap.getString("opnion");
    	String messageTitle = "지표관련의견-성과모니터링/실적상세/미진사유 확인"+"["+searchMap.getString("year")+searchMap.getString("mon") + "] " + searchMap.getString("metricNm");
    	
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
		        	searchMap.put("messageTitle", messageTitle);
		        	
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
    
}

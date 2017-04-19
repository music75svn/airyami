/*************************************************************************
* CLASS 명      : GovActualMngAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 8일
* 기    능      : 계량실적입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 8일         최 초 작 업
**************************************************************************/
package com.lexken.gov.measure;

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
import com.lexken.framework.util.StaticUtil;

public class GovActualMngAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 계량실적입력 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngList(SearchMap searchMap) {

    	searchMap.addList("insertUserList", getList("gov.measure.govActualMng.getInsertUserList", searchMap));
    	searchMap.addList("evalCatGrpList", getList("gov.measure.govActualMng.getEvalCatGrpList", searchMap));
    	
        return searchMap;
    }

    /**
     * 계량실적입력 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngList_xml(SearchMap searchMap) {

    	String[] monArray = {"01", "02", "03", "04"
    						,"05", "06", "07", "08"
    						,"09", "10", "11", "12"
    						};

    	searchMap.put("monArray", monArray);

        searchMap.addList("list", getList("gov.measure.govActualMng.getList", searchMap));


        return searchMap;
    }

    /**
     * 계량실적입력 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popGovActualMngList(SearchMap searchMap) {
    	
    	searchMap.addList("metricInfo", getDetail("gov.measure.govActualMng.getDetail", searchMap));
    	searchMap.addList("scoreList", getList("gov.measure.govActualMng.getScore", searchMap));
    	
    	/**********************************
         * 의견 조회
         **********************************/
        searchMap.addList("causeDetail", getDetail("gov.measure.govActualMng.getCause", searchMap));
        
        /**********************************
         * 첨부파일 조회
         **********************************/
        //searchMap.addList("fileList", getList("bsc.mon.scDeptDiagram.getFileList", searchMap));

        return searchMap;
    }

    /**
     * 계량실적입력 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap popGovActualMngList_xml(SearchMap searchMap) {
        searchMap.addList("list", getList("gov.measure.govActualMng.getPopListPop", searchMap));
        return searchMap;
    }


    /**
     * 계량실적입력차트 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngChart_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("gov.measure.govActualMng.getScore", searchMap));
        return searchMap;
    }
    
    
    /**
     * 계량실적입력 담당자(조회조건) 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertUserList_ajax(SearchMap searchMap) {

        searchMap.addList("insertUserList", getList("gov.measure.govActualMng.getInsertUserList", searchMap));

        return searchMap;
    }


    /**
     * 계량실적입력 평가범주(조회조건) 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalCatGrpList_ajax(SearchMap searchMap) {

    	searchMap.addList("evalCatGrpList", getList("gov.measure.govActualMng.getEvalCatGrpList", searchMap));

        return searchMap;
    }


    /**
     * 계량실적입력 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.govActualMng.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 계량실적입력 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngProcess(SearchMap searchMap) {
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
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 계량실적입력 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {

        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	String[] months = searchMap.getString("months").split("\\|", 0);
        	String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
        	String[] govMetricCalTypeColIds = searchMap.getString("govMetricCalTypeColIds").split("\\|", 0);
        	String[] govMetricCalTypeColActuals = searchMap.getString("govMetricCalTypeColActuals").split("\\|", 0);

        	if(null != govMetricIds && 0 < govMetricIds.length) {
        		for (int i = 0; i < govMetricIds.length; i++) {
					searchMap.put("mon", months[i].trim() );
					searchMap.put("govMetricId", govMetricIds[i].trim() );
					searchMap.put("govCalTypeColId", govMetricCalTypeColIds[i].trim() );
					searchMap.put("govCalTypeColActual", govMetricCalTypeColActuals[i].trim().replace("none", "") );

					returnMap = updateData("gov.measure.govActualMng.deleteData", searchMap, true);
					returnMap = insertData("gov.measure.govActualMng.insertData", searchMap);
        		}
  		    }

        	searchMap.put("procId", "SP_GOV_ACTUAL");

        	if("SP_GOV_ACTUAL".equals(searchMap.get("procId"))){
        		returnMap = insertData("gov.measure.govActualMng.callSpGovActualProc", searchMap);
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
     * 데이터집계관리 프로시저 실행
     * @param
     * @return String
     * @throws
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	if("SP_GOV_ACTUAL".equals(searchMap.get("procId"))){
        		returnMap = insertData("gov.measure.govActualMng.callSpGovActualProc", searchMap);
        	}else{
        		//..
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
     * 계량실적입력 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap govActualMngListExcel(SearchMap searchMap) {
    	String excelFileName = "계량실적입력";
    	String excelTitle = "계량실적입력 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("담당자", (String)searchMap.get("insertUserNm")));
    	excelSearchInfoList.add(new ExcelVO("평가범주", (String)searchMap.get("evalCatGrpNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("정평지표명", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("산식항목명", "ACT_CAL_TYPE_COL_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "CAL_TYPE_UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("1월", "MON01", "right"));
    	excelInfoList.add(new ExcelVO("2월", "MON02", "right"));
    	excelInfoList.add(new ExcelVO("3월", "MON03", "right"));
    	excelInfoList.add(new ExcelVO("4월", "MON04", "right"));
    	excelInfoList.add(new ExcelVO("5월", "MON05", "right"));
    	excelInfoList.add(new ExcelVO("6월", "MON06", "right"));
    	excelInfoList.add(new ExcelVO("7월", "MON07", "right"));
    	excelInfoList.add(new ExcelVO("8월", "MON08", "right"));
    	excelInfoList.add(new ExcelVO("9월", "MON09", "right"));
    	excelInfoList.add(new ExcelVO("10월", "MON10", "right"));
    	excelInfoList.add(new ExcelVO("11월", "MON11", "right"));
    	excelInfoList.add(new ExcelVO("12월", "MON12", "right"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "center"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);


    	ArrayList list = (ArrayList)getList("gov.measure.govActualMng.getPopListPop", searchMap);
    	HashMap tmpMap = null;

    	if( 0 < list.size() ){
    		for(int i=0 ; i<list.size() ; i++){

    			tmpMap = (HashMap)list.get(i);
    			String tmpStr = ( (String)tmpMap.get("ACT_CAL_TYPE_COL_ID") ).replace("ZZZ", "실적");
    			tmpMap.put("ACT_CAL_TYPE_COL_NM", tmpStr );
    		}
    	}

    	searchMap.put("excelDataList", list);


    	//searchMap.put("excelDataList", (ArrayList)getList("gov.measure.govActualMng.getList", searchMap));

        return searchMap;
    }

    /**
     * 지표실적상세 미진사유/의견등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popGovActualMngProcess(SearchMap searchMap) {
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
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("gov.measure.govActualMng.updateOpnionDB", searchMap);
    		
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
    	String messageTitle = "지표관련의견-성과모니터링/정부경영평가현황/의견 확인"+"["+searchMap.getString("findYear")+searchMap.getString("findMon") + "] " + searchMap.getString("govMetricNm");
    	
    	try {
    		
	    	ArrayList UserList = new ArrayList();
	    	UserList = (ArrayList)getList("gov.measure.govActualMng.getUserList", searchMap);
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

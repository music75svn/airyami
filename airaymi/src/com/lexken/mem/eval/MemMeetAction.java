/*************************************************************************
* CLASS 명      : MemMeetAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월24일 
* 기    능      : 중간면담
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 24일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemMeetAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 중간면담
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 중간면담 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
        
        searchMap.addList("list", getList("mem.eval.memMeet.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 중간면담 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetSearchList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 중간면담 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetSearchList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.put("mode", "SEARCH");
    	searchMap.put("sEmpNo", loginVO.getUser_id());
        
        searchMap.addList("list", getList("mem.eval.memMeet.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 중간면담 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMeetExcel(SearchMap searchMap) {
    	String excelFileName = "중간면담";
    	String excelTitle = "중간면담목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	if(!"SEARCH".equals((String)searchMap.get("mode"))){
	    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("findDeptNm")));
	    	excelSearchInfoList.add(new ExcelVO("지급", (String)searchMap.get("castTcNm")));
	    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
	    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("findEmpNm")));
    	}
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("이름", "EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_NM", "center"));
    	excelInfoList.add(new ExcelVO("계획서상태", "METRIC_STATUS_NM", "center"));
    	excelInfoList.add(new ExcelVO("면담요청수/면담총수", "MEET_COUNT", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.eval.memMeet.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 중간면담 상세 목록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetDetailList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 중간면담 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetDetailList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("mem.eval.memMeet.getDetailList", searchMap));

        return searchMap;
    }
    
    /**
     * 중간면담상세 삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetDetailProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("DEL".equals(stMode)) {
            searchMap = deleteDetailDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 중간면담 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDetailDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] seqs = searchMap.getString("seqs").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != seqs && 0 < seqs.length) {
		        for (int i = 0; i < seqs.length - 1; i++) {
		        	searchMap.put("seq", seqs[i]);
		            returnMap = updateData("mem.eval.memMeet.deleteDetailData", searchMap);
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
     * 중감면담 상세 수정화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetDetailModify(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap(); 
    	
    	try{
    		setStartTransaction();
    		
        	// 지표별 면담 저장 여부 조회
        	String meetByMetricYn = (String)getDetail("mem.eval.memMeet.getMeetByMetricYn", searchMap).get("MEET_BY_METRIC_YN");
        	
        	if("N".equals(meetByMetricYn)){
        		// 지표별 면담 월 저장
        		returnMap = insertData("mem.eval.memMeet.insertMeetByMetricInitData", searchMap);
        	}
        	

        	searchMap.put("findYear", searchMap.getString("year"));
        	searchMap.put("findCodeGrpId", "248");
        	searchMap.addList("meetTypeList", getList("bsc.base.code.getList", searchMap));
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
    	
    	searchMap.addList("detail", getDetail("mem.eval.memMeet.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적입력 평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetEvalItemList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
        
        searchMap.addList("list", getList("mem.eval.memMeet.getEvalItemList", searchMap));

        return searchMap;
    }
    
    /**
     * 중간면담상세 삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetDetailModifyProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"COMPLATE".equals(stMode) && !"CANCEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("SAVE".equals(stMode)) {
            searchMap = saveMeetDB(searchMap);
        } else if("COMPLATE".equals(stMode)) {
        	searchMap = complateMeetDB(searchMap);
        } else if("CANCEL".equals(stMode)) {
        	searchMap = complateCancelMeetDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 중간면담 저장 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveMeetDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
	    	searchMap.put("sEmpNo", loginVO.getUser_id());
	        
	    	// 지표별 면담 삭제
	    	returnMap = insertData("mem.eval.memMeet.deleteMeetByMetricInitData", searchMap);
	    	
	    	// 지표별 면담 초기 등록	    	
	    	returnMap = insertData("mem.eval.memMeet.insertMeetByMetricInitData", searchMap);
	    	
	    	String[] metricIds = searchMap.getStringArray("metricIds");
	    	
        	/**********************************
             * 지표별 중간면담내용 입력
             **********************************/
        	if(null != metricIds && 0 < metricIds.length) {
 		        for (int i = 0; i < metricIds.length; i++) {
 					searchMap.put("metricId", metricIds[i]);
 					searchMap.put("meetTypeCd", searchMap.get("meetTypeCd"+metricIds[i]));
 					searchMap.put("actCheck", searchMap.get("actCheck"+metricIds[i]));
 					searchMap.put("coaching", searchMap.get("coaching"+metricIds[i]));
 					
 					returnMap = updateData("mem.eval.memMeet.updateMeetByMetricInitData", searchMap);
 		        }
 		    }
	    	
	        returnMap = updateData("mem.eval.memMeet.saveMeetData", searchMap);
	        
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
     * 중간면담  면담완료 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap complateCancelMeetDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        // 면담완료취소
	    	searchMap.put("meetState", "01");
	        returnMap = updateData("mem.eval.memMeet.complateMeetData", searchMap);
	        
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
     * 중간면담  면담완료 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap complateMeetDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        // 면담저장
	        saveMeetDB(searchMap);
	        
	    	searchMap.put("meetState", "02");
	        returnMap = updateData("mem.eval.memMeet.complateMeetData", searchMap);
	        
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
     * 지표별 면담내용
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popEvalOpinion(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 실적조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalActSearchList(SearchMap searchMap) {
    	// 지표명 조회
    	searchMap.addList("metricInfo", getDetail("mem.eval.memMeet.getMetricNm", searchMap));
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
        String stMode = searchMap.getString("mode");
        
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("meetDt"), "면담일자", 1, 10);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("excellentOpinion"), "우수한점", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("unsufficientOpinion"), "미흡한점", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("etcOpinion"), "기타의견", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}

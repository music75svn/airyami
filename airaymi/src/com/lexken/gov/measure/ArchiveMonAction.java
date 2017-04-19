/*************************************************************************
* CLASS 명      : ArchiveMonAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 5일 
* 기    능      : 계량지표총괄표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 5일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class ArchiveMonAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 계량지표총괄표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonList(SearchMap searchMap) {

    	String findEvalCatGrpId = (String)searchMap.get("findEvalCatGrpId");		
    			
    	searchMap.addList("list", getList("gov.measure.archiveMon.getList", searchMap));
    	
		if("".equals(StaticUtil.nullToBlank(findEvalCatGrpId))) {
    		searchMap.put("findEvalCatGrpId", searchMap.getDefaultValue("list", "EVAL_CAT_GRP_ID", 0));
    	}
    	
        return searchMap;
    }
    
    /**
     * 계량지표총괄표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonList_xml(SearchMap searchMap) {
        
    	String[][] convertArray =  {{"YYY", ""} , {"NNN","┗"}};
    	
        searchMap.addList("list", getList("gov.measure.archiveMon.getList", searchMap));
        searchMap.addList("CONVERT_ARRAY", convertArray);

        return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonCatGrpChart_ajax(SearchMap searchMap) {
        
    	//차트 범주
    	//searchMap.addList("catGrpList", getDetail("gov.measure.archiveMon.getCatGrpList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonCatChart_ajax(SearchMap searchMap) {
        
    	//차트 부문
    	//searchMap.addList("catList", getDetail("gov.measure.archiveMon.getCatList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonCatGrpChart_xml(SearchMap searchMap) {
        
    	//차트 범주
    	searchMap.addList("catGrpList", getList("gov.measure.archiveMon.getCatGrpList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonCatGrpByOneChart_xml(SearchMap searchMap) {
        
    	//차트 범주
    	searchMap.addList("catGrpByOneList", getList("gov.measure.archiveMon.getCatGrpByOneList", searchMap));
        
        return searchMap;
    }
    
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml) - 평가부문 별
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonCatChart_xml(SearchMap searchMap) {
        
    	//차트 부문
    	searchMap.addList("catList", getList("gov.measure.archiveMon.getCatList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 월별지표실적상세 차트 데이터 조회(xml)- 정평지표 별
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonGovChart_xml(SearchMap searchMap) {
    	
    	//차트 부문
    	searchMap.addList("govList", getList("gov.measure.archiveMon.getGovList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 계량지표총괄표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.archiveMon.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 계량지표총괄표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveMonProcess(SearchMap searchMap) {
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
     * 계량지표총괄표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.measure.archiveMon.insertData", searchMap);
        
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
     * 계량지표총괄표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.measure.archiveMon.updateData", searchMap);
	        
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
     * 계량지표총괄표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
		            returnMap = updateData("gov.measure.archiveMon.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("baseValue"), "기준치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("bestTarget"), "최고목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("lowestTarget"), "최저목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("stadConst"), "표준편차");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 계량지표총괄표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMappingMetricList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 계량지표총괄표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMappingMetricList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("gov.measure.archiveMon.getMappingMetricList", searchMap));
    	
        return searchMap;
    }    
    
    /**
     * 계량지표총괄표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap archiveMonListExcel(SearchMap searchMap) {
    	String excelFileName = "계량지표총괄표";
    	String excelTitle = "계량지표총괄표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문", "EVAL_CAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("정평지표", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표방향성", "DIREC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET", "right"));
    	excelInfoList.add(new ExcelVO("실적", "ACTUAL", "right"));
    	excelInfoList.add(new ExcelVO("달성율", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.archiveMon.getList", searchMap));
    	
        return searchMap;
    }
    
}

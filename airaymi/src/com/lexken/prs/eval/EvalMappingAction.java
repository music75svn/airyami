/*************************************************************************
* CLASS 명      : EvalMappingAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 26일 
* 기    능      : 평가자매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class EvalMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가자매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMappingList(SearchMap searchMap) {
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("prs.eval.evalGroup.getDegreeList", searchMap));
    	
    	if("".equals(StaticUtil.nullToDefault(findEvalDegreeId,""))){
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalStatus.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalStatus.getConfirmYn", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.eval.evalMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalMapping.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가자매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMappingProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         ********************************
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }**/
        
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
     * 평가자매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	//setStartTransaction();
        
        	//returnMap = insertData("prs.eval.evalMapping.insertData", searchMap);
        	
        	String[] evalGrpIds 	= searchMap.getString("evalGrpIds").split("\\|", 0);
			String[] evalUser1Ids 	= searchMap.getString("evalUser1Ids").split("\\|", 0);
			String[] evalUser2Ids 	= searchMap.getString("evalUser2Ids").split("\\|", 0);
			String[] evalUser3Ids 	= searchMap.getString("evalUser3Ids").split("\\|", 0);
			String[] weights1 		= searchMap.getString("weights1").split("\\|", 0);
			String[] weights2 		= searchMap.getString("weights2").split("\\|", 0);
			String[] weights3 		= searchMap.getString("weights3").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalMapping.deleteData", searchMap, true);
	        
	        if(null != evalGrpIds && 0 < evalGrpIds.length) {
		        for (int i = 0; i < evalGrpIds.length; i++) {
		            if(!evalUser1Ids[i].equals("N")){
			        	searchMap.put("evalGrpId", 	evalGrpIds[i]);
			            searchMap.put("evalUserId", evalUser1Ids[i]);
			            searchMap.put("evalSeq", 	"1");
			            searchMap.put("weight", 	weights1[i]);
			            returnMap = updateData("prs.eval.evalMapping.insertData", searchMap);
		            }
		            if(!evalUser2Ids[i].equals("N")){
			        	searchMap.put("evalGrpId", 	evalGrpIds[i]);
			            searchMap.put("evalUserId", evalUser2Ids[i]);
			            searchMap.put("evalSeq", 	"2");
			            searchMap.put("weight", 	weights2[i]);
			            returnMap = updateData("prs.eval.evalMapping.insertData", searchMap);
		            }
		            if(!evalUser3Ids[i].equals("N")){
			        	searchMap.put("evalGrpId", 	evalGrpIds[i]);
			            searchMap.put("evalUserId", evalUser3Ids[i]);
			            searchMap.put("evalSeq", 	"3");
			            searchMap.put("weight", 	weights3[i]);
			            returnMap = updateData("prs.eval.evalMapping.insertData", searchMap);
		            }
		        }
		    }
	        
	        returnMap = updateData("prs.eval.evalMapping.deleteAdminData", searchMap, true);//권한삭제
	        
	        returnMap = insertData("prs.eval.evalMapping.insertAdminData", searchMap);//권한등록
        
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
     * 평가자매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalMapping.updateData", searchMap);
	        
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
     * 평가자매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", 0);
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("evalGrpId", evalGrpIds[i]);
		            searchMap.put("evalUserId", evalUserIds[i]);
		            returnMap = updateData("prs.eval.evalMapping.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalSeq"), "평가 순번");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가자매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "평가자매핑";
    	String excelTitle = "평가자매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가군 코드", "EVAL_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가군 명", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_ID1", "EVAL_USER_ID1", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_NM1", "EVAL_USER_NM1", "left"));
    	excelInfoList.add(new ExcelVO("WEIGHT1", "WEIGHT1", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_ID2", "EVAL_USER_ID2", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_NM2", "EVAL_USER_NM2", "left"));
    	excelInfoList.add(new ExcelVO("WEHIGHT2", "WEHIGHT2", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_ID3", "EVAL_USER_ID3", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_USER_NM3", "EVAL_USER_NM3", "left"));
    	excelInfoList.add(new ExcelVO("WEIGHT3", "WEIGHT3", "left"));
    	excelInfoList.add(new ExcelVO("총가중치", "TOT_WEIGHT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}

/*************************************************************************
* CLASS 명      : ExcGroupAction
* 작 업 자      : 박종호
* 작 업 일      : 2013년 05월 31일 
* 기    능      : 간부 별도평가군
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박종호      2013년 05월 31일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.exc;
    
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

public class ExcGroupAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부 별도평가군 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupList(SearchMap searchMap) {
    	
    	searchMap.addList("yearCheck", getDetail("prs.eval.evalSchedule.getYearCheck", searchMap));		//해당년도 일정입력체크
    	    	
    	return searchMap;
    }
    
    /**
     * 간부 별도평가군 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.exc.excGroup.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부 별도평가군 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	HashMap detail = new HashMap();
    	
    	if("MOD".equals(stMode)) {
    		detail = getDetail("prs.exc.excGroup.getDetail", searchMap);
    		searchMap.addList("detail", detail);
    		//관리조직 조회 
	        searchMap.addList("deptList", getList("prs.exc.excGroup.getDeptList", searchMap));
	        searchMap.addList("posTcList", getList("prs.exc.excGroup.getPosTcList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 간부 별도평가군 등록/대상자자동등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"AUTO".equals(stMode)) {
        	returnMap = this.validChk(searchMap);

        	if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
       
        /**********************************
         * 등록/대상자자동등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("AUTO".equals(stMode)) {
        	searchMap = insertAutoDB(searchMap);
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
     * 간부 별도평가군 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
	         * 간부 별도평가군 코드 채번 
	         **********************************/
	        String excGrpId = getStr("prs.exc.excGroup.getExcGrpId", searchMap);
	        searchMap.put("excGrpId", excGrpId);
	        
	        /**********************************
	         * 간부 별도평가군 / 부서 / 직위 입력
	         **********************************/
        	returnMap = insertData("prs.exc.excGroup.insertData", searchMap);
        	
	        String[] deptCds = searchMap.getStringArray("deptId");
	        String[] posTcs = searchMap.getStringArray("posTc");
        
        	returnMap = updateData("prs.exc.excGroup.deleteDeptData", searchMap, true);
        	returnMap = updateData("prs.exc.excGroup.deletePosData", searchMap, true);

		    for (int i = 0; i < deptCds.length; i++) {
		    	if(!deptCds[i].equals("")){
		        		searchMap.put("deptCd", deptCds[i]);
		        		returnMap = insertData("prs.exc.excGroup.insertDeptData", searchMap);
		    	}
		    }		
		    for (int i = 0; i < posTcs.length; i++) {
		    	if(!posTcs[i].equals("")){
		        		searchMap.put("posTc", posTcs[i]);
		        		returnMap = insertData("prs.exc.excGroup.insertPosData", searchMap);
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
     * 간부 별도평가군 대상자 자동등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAutoDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
     try {
          setStartTransaction(); 
	        
	      /**********************************
	       * 대상자 자동등록
	       **********************************/
          
            returnMap = insertData("prs.exc.excGroup.deleteUseDataAuto", searchMap);
		  	returnMap = insertData("prs.exc.excGroup.insertUseDataAuto", searchMap);
		  	returnMap = insertData("prs.exc.excGroup.insertLessThan1Months", searchMap);
		  	
		  	insertadminExcEvalInfo(searchMap); //권한 부여 작업

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
     * 별도 평가대상자 권한 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertadminExcEvalInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        
        	returnMap = deleteData("prs.exc.excGroup.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.exc.excGroup.insertAdminData", searchMap);
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
     * 간부 별도평가군 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        	setStartTransaction();
	        
	        	returnMap = updateData("prs.exc.excGroup.updateData", searchMap);
	        	        
		        String[] deptCds = searchMap.getStringArray("deptId");
		        String[] posTcs = searchMap.getStringArray("posTc");
	        
	        	returnMap = updateData("prs.exc.excGroup.deleteDeptData", searchMap, true);
	        	returnMap = updateData("prs.exc.excGroup.deletePosData", searchMap, true);

			    for (int i = 0; i < deptCds.length; i++) {
			    	if(!deptCds[i].equals("")){
			        		searchMap.put("deptCd", deptCds[i]);
			        		returnMap = insertData("prs.exc.excGroup.insertDeptData", searchMap);
			    	}
			    }		
			    for (int i = 0; i < posTcs.length; i++) {
			    	if(!posTcs[i].equals("")){
			        		searchMap.put("posTc", posTcs[i]);
			        		returnMap = insertData("prs.exc.excGroup.insertPosData", searchMap);
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
     * 간부 별도평가군 삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] excGrpIds = searchMap.getString("excGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != excGrpIds && 0 < excGrpIds.length) {
		        for (int i = 0; i < excGrpIds.length; i++) {
		            searchMap.put("excGrpId", excGrpIds[i]);
		            returnMap = updateData("prs.exc.excGroup.deleteData", searchMap);
		            returnMap = updateData("prs.exc.excGroup.deleteDeptData", searchMap, true);
		        	returnMap = updateData("prs.exc.excGroup.deletePosData", searchMap, true);
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("excGrpNm"), "별도평가군명", 1, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		if("".equals(searchMap.getString("deptId")) && "".equals(searchMap.getString("posTc"))){
			returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "부서와 직위 중 한가지는 필수 입력입니다.");
    		return returnMap;
		}
		
		if("02".equals(searchMap.getString("evalType"))){
			int bscRate = 0;
			int prsRate = 0;
			int orgRate = 0;
			int effRate = 0;
			
			if("0".equals(searchMap.get("bscRate"))){
				searchMap.put("bscRate", "");	
			}else if(!"".equals(searchMap.get("bscRate"))){
				bscRate = searchMap.getInt("bscRate");
			}
			
			if("0".equals(searchMap.get("prsRate"))){
				searchMap.put("prsRate", "");	
			}else if(!"".equals(searchMap.get("prsRate"))){
				prsRate = searchMap.getInt("prsRate");
			}
			
			if("0".equals(searchMap.get("orgRate"))){
				searchMap.put("orgRate", "");	
			}else if(!"".equals(searchMap.get("orgRate"))){
				orgRate = searchMap.getInt("orgRate");
			}
			
			if("0".equals(searchMap.get("effRate"))){
				searchMap.put("effRate", "");	
			}else if(!"".equals(searchMap.get("effRate"))){
				effRate = searchMap.getInt("effRate");
			}
			
			int sumRate = bscRate + prsRate + orgRate + effRate;
			
			if(100 != sumRate){
				returnMap.put("ErrorNumber",  -1);
        		returnMap.put("ErrorMessage", "비율의 합은 100이여야 합니다.");
            	
            	return returnMap;
			}			
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
    
    /**
     * 간부 별도평가군 대상자관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupUseList(SearchMap searchMap) {
    	
        searchMap.addList("excGrpUseDatail", getDetail("prs.exc.excGroup.getExcGrpUseDatail", searchMap));
    	    	
    	return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupUseList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.exc.excGroup.getUseList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 상세보기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupUseModify(SearchMap searchMap) {
    	
    	searchMap.addList("excGrpUseDatail", getDetail("prs.exc.excGroup.getExcGrpUseDatail", searchMap));
    	searchMap.addList("insaUseList", getList("prs.exc.excGroup.getInsaUseList", searchMap));    	
        searchMap.addList("userList", getList("prs.exc.excGroup.getUseList", searchMap));
        
        return searchMap;
    }    
    
    /**
     * 간부 별도평가군 대상자관리 등록/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excGroupUseProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        if ("".equals(stMode)) stMode = "ADD"; 
        
        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertUseDB(searchMap);
        }else if("DEL".equals(stMode)) {
            searchMap = deleteUseDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertUseDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        String empn    = searchMap.getString("empns");
        String[] empns = searchMap.getString("empns").split("\\|", 0);
        
        returnMap = updateData("prs.exc.excGroup.deleteUseData", searchMap, true); 
        
        if(!"".equals(empn)) {
	        for (int i = 0; i < empns.length; i++) {
	        	searchMap.put("empn", empns[i]);
	        	returnMap = insertData("prs.exc.excGroup.insertUseData", searchMap);
	        }
        }
        
        insertadminExcEvalInfo(searchMap); //권한 부여 작업
        
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 간부 별도평가군 대상자관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteUseDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] empns = searchMap.getString("empns").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != empns && 0 < empns.length) {
		        for (int i = 0; i < empns.length; i++) {
		        	logger.debug("empn :"+empns[i]);
		            searchMap.put("empn", empns[i]);
		            returnMap = updateData("prs.exc.excGroup.deleteUseData", searchMap);
		        }
		    }
	        
	        insertadminExcEvalInfo(searchMap); //권한 부여 작업
	        
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
     * 간부 별도평가군 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap excGroupListExcel(SearchMap searchMap) {
    	String excelFileName = "간부 별도평가군 관리";
    	String excelTitle = "간부 별도평가군 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("findYear")));    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("별도평가군코드", 	"EXC_GRP_ID",	"right"));
    	excelInfoList.add(new ExcelVO("별도평가군", 		"EXC_GRP_NM",	"right"));
    	excelInfoList.add(new ExcelVO("대상인원", 			"EXC_COUNT",	"right"));
    	excelInfoList.add(new ExcelVO("평가방법", 			"EVAL_TYPE",	"right"));
    	excelInfoList.add(new ExcelVO("등급", 				"EVAL_GRADE",	"right"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.exc.excGroup.getList", searchMap));
    	
        return searchMap;
    }
    
}

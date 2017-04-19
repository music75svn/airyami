/*************************************************************************
* CLASS 명      : EvalPersonageAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 28일 
* 기    능      : 사외인사
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 28일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.common;
    
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

public class EvalPersonageAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 사외인사 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalPersonageList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 사외인사 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalPersonageList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.common.evalPersonage.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사외인사 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalPersonageModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.common.evalPersonage.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 사외인사 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalPersonageProcess(SearchMap searchMap) {
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
     * 사외인사 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	 int isExistUserCnt = Integer.parseInt( getStr("bsc.common.evalPersonage.getExistUserYn", searchMap) );
        	
        	if( 0 == isExistUserCnt ){
        		returnMap = insertData("bsc.common.evalPersonage.insertData", searchMap);
        	}
        	
        	returnMap.put("isExistUserCnt",isExistUserCnt);
        
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
     * 사외인사 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.common.evalPersonage.updateData", searchMap);
	        
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
     * 사외인사 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] personageIds = searchMap.getString("personageIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != personageIds && 0 < personageIds.length) {
		        for (int i = 0; i < personageIds.length; i++) {
		            searchMap.put("personageId", personageIds[i]);
		            returnMap = updateData("bsc.common.evalPersonage.deleteData", searchMap);
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("personageId"), "사외인사 ID", 1, 16);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("name"), "성명", 1, 16);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("passwd"), "비밀번호", 1, 16);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("passwdConfirm"), "비밀번호 확인", 1, 16);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		if(!searchMap.getString("passwdConfirm").equals(searchMap.getString("passwd"))) {
			returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "입력하신 비밀번호가 다릅니다.");
	    	
	    	return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("organ"), "소속", 0, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("pos"), "직위", 0, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("mobileNo"), "휴대폰 연락처", 1, 20);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.checkPhoneNumber(searchMap.getString("mobileNo"), 13, "휴대폰 연락처");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.checkPhoneNumber(searchMap.getString("officeTelNo"), 13, "사무실 연락처");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.checkPhoneNumber(searchMap.getString("homeTelNo"), 13, "자택 연락처");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.lengthCheck(searchMap.getString("eMail"), "E_MAIL", 1, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.emailCheck(searchMap.getString("eMail"));
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.lengthCheck(searchMap.getString("address"), "주소", 0, 500);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("birthYmd"), "출생일", 0, 10);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("school"), "학력", 0, 500);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("userHistory"), "경력", 0, 2000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 2000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 사외인사 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalPersonageListExcel(SearchMap searchMap) {
    	String excelFileName = "사외인사";
    	String excelTitle = "사외인사 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("성명", (String)searchMap.get("findName")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사외인사 ID", "PERSONAGE_ID", "left"));
    	excelInfoList.add(new ExcelVO("비밀번호", "PASSWD", "left"));
    	excelInfoList.add(new ExcelVO("성명", "NAME", "left"));
    	excelInfoList.add(new ExcelVO("소속", "ORGAN", "left"));
    	excelInfoList.add(new ExcelVO("직위", "POS", "left"));
    	excelInfoList.add(new ExcelVO("휴대폰 연락처", "MOBILE_NO", "left"));
    	excelInfoList.add(new ExcelVO("사무실 연락처", "OFFICE_TEL_NO", "left"));
    	excelInfoList.add(new ExcelVO("자택 연락처", "HOME_TEL_NO", "left"));
    	excelInfoList.add(new ExcelVO("E_MAIL", "E_MAIL", "left"));
    	excelInfoList.add(new ExcelVO("주소", "ADDRESS", "left"));
    	excelInfoList.add(new ExcelVO("출생일", "BIRTH_YMD", "left"));
    	excelInfoList.add(new ExcelVO("학력", "SCHOOL", "left"));
    	excelInfoList.add(new ExcelVO("경력", "USER_HISTORY", "left"));
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.common.evalPersonage.getList", searchMap));
    	
        return searchMap;
    }
    
}

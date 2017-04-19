/*************************************************************************
* CLASS 명      : ScDeptMngAction
* 작 업 자      : 형광민
* 작 업 일      : 2012년 6월 20일 
* 기    능      : 성과조직관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    형광민      2012년 6월 20일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.scDeptUtil.ScDeptUtilReLoad;
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

public class ScDeptMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과조직관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMngList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if(topScDept==null){
    		topScDept =new HashMap();
    		topScDept.put("SC_DEPT_ID", "");
    		topScDept.put("SC_DEPT_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findScDeptId =  StaticUtil.nullToDefault((String)searchMap.getString("findScDeptId"), (String)topScDept.get("SC_DEPT_ID"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findScDeptNm =  StaticUtil.nullToDefault((String)searchMap.getString("findScDeptNm"), (String)topScDept.get("SC_DEPT_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	
    	String findLevelId = (String)searchMap.getString("findLevelId", "0");			// 조직레벨이 없으면 전사조직레벨을 셋팅.(전사로 조회하는 경우 전사조직도 리스트에 포함시키기 위함.)
    	String findUseYn = (String)searchMap.getString("findUseYn", "Y");				// 사용여부가 없으면 사용을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findScDeptId", findScDeptId);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findScDeptNm", findScDeptNm);
    	searchMap.put("findLevelId", findLevelId);
    	searchMap.put("findUseYn", findUseYn);
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 성과조직관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMngList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.base.scDeptMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 성과조직관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMngModify(SearchMap searchMap) {
    	String mode = (String)searchMap.get("mode");
    	String scDeptId = (String)searchMap.get("scDeptId");
    	String upScDeptId = (String)searchMap.get("upScDeptId");
    	String findUseYn = (String)searchMap.get("findUseYn");
    	
    	// 신규 등록 및 미사용조직 수정시 조직트리에 상위조직 설정.
		searchMap.put("findSearchCodeId", upScDeptId);    		
    	
    	if("ADD".equals(mode)) {
    		searchMap.put("scDeptId", "");					// 신규 등록시 조직코드 초기화.
    	} else {
    		if("Y".equals(findUseYn)) {
    			searchMap.put("findSearchCodeId", scDeptId);	// 사용조직 수정시 조직트리에 선택조직 설정.
    		}
    	}
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        searchMap.addList("detail", getDetail("bsc.base.scDeptMng.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 성과조직관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        String upScDeptId = (String)searchMap.get("upScDeptId");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"GET".equals(stMode)) {
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
        } else if("GET".equals(stMode)) {
        	searchMap = getInserDB(searchMap);
        }
        
        /**********************************
         * 작업 후 검색조건 셋팅
         **********************************/
        if("ADD".equals(stMode) || "MOD".equals(stMode)) {
        	// 신규 등록 및 수정 후  상위조직으로 조회.
        	searchMap.put("findSearchCodeId", upScDeptId);
     		searchMap.put("findScDeptId", upScDeptId);
        }
        
        /*****************************************
		 * 세션에 등록되어 있는 부서 정보 reflash
		 *****************************************/
        ScDeptUtilReLoad scDeptUtilReLoad = new ScDeptUtilReLoad();
        scDeptUtilReLoad.scDeptUtilReLoad();
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과조직관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	//성과조직 코드 채번
	        String scDeptId = getStr("bsc.base.scDeptMng.getScDeptId", searchMap);
	        searchMap.put("scDeptId", scDeptId);
        
        	returnMap = insertData("bsc.base.scDeptMng.insertData", searchMap);
        	
        	//권한설정
	        returnMap = insertAdmin(searchMap);
        
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
     * 성과조직관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.scDeptMng.updateData", searchMap);
	        
	        //권한설정
	        returnMap = insertAdmin(searchMap);
	        
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
     * 성과조직관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 

        try {
	        String scDeptIds = searchMap.getString("scDeptIds");
	        String[] keyArray = scDeptIds.split("\\|", 0);
	 
	        setStartTransaction();
	        
	        for (int i=0; i<keyArray.length; i++) {
	            searchMap.put("scDeptId", keyArray[i]);
	            returnMap = updateData("bsc.base.scDeptMng.deleteData", searchMap, true);
	            //returnMap = deleteData("bsc.base.scDeptMng.deleteMappingData", searchMap);
	        }
	        
	        //권한설정
	        returnMap = insertAdmin(searchMap);
	        
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
     * 정평지표점수반영 조직 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getInserDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.scDeptMng.UpDateGovRefData", searchMap);
	        
        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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
        
        //Validation 체크 추가
        returnMap = ValidationChk.lengthCheck(searchMap.getString("scDeptNm"), "평가조직명", 1, 75);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        if( !"0".equals(searchMap.getString("levelId")) ) {
        	returnMap = ValidationChk.emptyCheck(searchMap.getString("upScDeptId"), "상위조직");
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                return returnMap;
            }
        }
        
        returnMap = ValidationChk.onlyNumber(searchMap.getString("sortOrder"), "정렬순서");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    /**
     * 지표권한등록
     * 02 : 부서장(KPI승인자)
     * 03 : 부서담당자(KPI입력자)
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertAdmin(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	//기존 권한삭제
	        returnMap = updateData("bsc.base.scDeptMng.deleteAdmin", searchMap, true);
	        
	        //부서장(KPI승인자) 입력
	        returnMap = insertData("bsc.base.scDeptMng.insertManagerUserAdmin", searchMap);
	        
	        //부서담당자(KPI입력자) 입력
	        returnMap = insertData("bsc.base.scDeptMng.insertBscUserAdmin", searchMap);
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }
    
    /**
     * 성과조직관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap scDeptMngExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.SC_DEPT_NM;
    	String excelTitle = StringConstants.SC_DEPT_NM + "리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.UP_SC_DEPT_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("upScDeptNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM + "코드", "SC_DEPT_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM + "명", "SC_DEPT_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("조직레벨", "LEVEL_ID", "left", "CNT"));
    	excelInfoList.add(new ExcelVO("조직평가그룹", "SC_DEPT_GRP_NM", "left", "CNT"));
    	excelInfoList.add(new ExcelVO("담당자", "BSC_USER_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("부서장", "MANAGER_USER_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "center", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.scDeptMng.getExcelList", searchMap));
    	
        return searchMap;
    }
    
}

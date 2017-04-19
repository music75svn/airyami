/*************************************************************************
* CLASS 명      : ScDeptMappingAction
* 작 업 자      : 형광민
* 작 업 일      : 2012년 7월 16일 
* 기    능      : 성과조직매핑관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    형광민      2012년 7월 16일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.awt.List;
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

public class ScDeptMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과조직매핑관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.scDeptMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);
    	HashMap topDept = (HashMap)getDetail("bsc.module.commModule.getTopDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topDept)) {
    		topDept = new HashMap();
    	}
    	
    	if(StaticUtil.isEmpty(topScDept)) {
    		topScDept = new HashMap();
    	}
    	
    	String topDeptId = (String)topDept.get("DEPT_ID");	// 조직트리구성할 때 li태그에 id를 조직코드로 만드는데 id는  처음에 숫자가 나오면 안되므로 문자를 붙여줌.(실조직인 경우 숫자가 많음.) 
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findScDeptId = (String)searchMap.getString("findScDeptId", (String)topScDept.get("SC_DEPT_ID"));	// 성과조직코드가 없으면 전사성과조직코드를 셋팅.
    	String findDeptId = (String)searchMap.getString("findDeptId", topDeptId);								// 실조직코드가 없으면 전사실조직코드를 셋팅.
    	String findMappingCriterion = (String)searchMap.getString("findMappingCriterion", "01");				// 매핑기준이 없으면 01(성과조직)을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findScDeptId", findScDeptId);					// 검색버튼 조회시 성과조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findDeptId", findDeptId);						// 검색버튼 조회시 실조직트리에 기존 선택한 조직이 표시되도록 설정.
    	searchMap.put("findMappingCriterion", findMappingCriterion);
    	
    	if("01".equals(findMappingCriterion)) {
    		searchMap.put("treeCheckboxYn", "false");
    		searchMap.put("tree2CheckboxYn", "true");
    	} else {
    		searchMap.put("treeCheckboxYn", "true");
    		searchMap.put("tree2CheckboxYn", "false");
    	}
        
    	searchMap.addList("treeList", getList("bsc.base.scDeptMapping.getScDeptMappingTreeList", searchMap));	// 성과조직 트리
    	searchMap.addList("treeList2", getList("bsc.base.scDeptMapping.getDeptMappingTreeList", searchMap));	// 실조직 트리
    	if("01".equals(findMappingCriterion)) {
    		// 매핑기준이 성과조직일 때
    		ArrayList list = new ArrayList();
    		HashMap dept = new HashMap();
    		dept.put("SC_DEPT_ID", findScDeptId);
    		list.add(dept);
    		searchMap.addList("mappingScDeptList", list);		// 성과조직트리에서 선택한 조직
    		searchMap.addList("mappingDeptList", getList("bsc.base.scDeptMapping.getMappingDeptList", searchMap));		// 성과조직에 매핑된 실조직 리스트 조회
    	} else {
    		// 매핑기준이 실조직일때
    		ArrayList list = new ArrayList();
    		HashMap dept = new HashMap();
    		dept.put("DEPT_ID", findDeptId);
    		list.add(dept);
    		searchMap.addList("mappingScDeptList", getList("bsc.base.scDeptMapping.getMappingScDeptList", searchMap));	// 실조직에 매핑된 성과조직 리스트 조회
    		searchMap.addList("mappingDeptList", list);	// 실조직트리에서 선택한 조직
    	}
        
        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptMappingProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        String findMappingCriterion = (String)searchMap.getString("findMappingCriterion");
        
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
        	if("01".equals(findMappingCriterion)) {
	    		// 매핑기준이 성과조직일 때
        		searchMap = updateDBScDeptMapping(searchMap);
	        } else if("02".equals(findMappingCriterion)) {
	        	// 매핑기준이 실조직일때
	        	searchMap = updateDBDeptMapping(searchMap);
	        }
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.base.scDeptMapping.insertData", searchMap);
        
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
     * 성과조직매핑관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDBScDeptMapping(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        int totCnt = 0;
        try {
        	
        	String scDeptMappingId = searchMap.getString("scDeptMappingIds");
        	String[] scDeptMappingIds = null;
        	if(!"".equals(scDeptMappingId)){
        		scDeptMappingIds = searchMap.getString("scDeptMappingIds").split("\\|", 0);
        	}
        	
	        setStartTransaction();
	        
	        returnMap = deleteData("bsc.base.scDeptMapping.deleteScDeptMappingData", searchMap, true);
	        if( scDeptMappingIds != null) {
		        for (int i = 0; i < scDeptMappingIds.length; i++) {
	        		searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
	        		int cnt = getInt("bsc.base.scDeptMapping.selectDeptMappingData", searchMap);
	        		
	        		totCnt += cnt;
	        	}
		        
		        if(0 < totCnt) {
		        	setRollBackTransaction();
		        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
		            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "실조직"));
		        }else{
		        	for (int i = 0; i < scDeptMappingIds.length; i++) {
		        		searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
			            returnMap = insertData("bsc.base.scDeptMapping.insertScDeptMappingData", searchMap);
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
     * 실조직매핑관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDBDeptMapping(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        try {
        	
        	String deptMappingId    = searchMap.getString("deptMappingIds");
        	String[] deptMappingIds = null;
        	
        	if(!"".equals(deptMappingId)){
        		deptMappingIds = searchMap.getString("deptMappingIds").split("\\|", 0);
        	}
        	
	        setStartTransaction();
	        
        	returnMap = deleteData("bsc.base.scDeptMapping.deleteDeptMappingData", searchMap, true);
	        
        	if(deptMappingIds != null) {
        		for (int i = 0; i < deptMappingIds.length; i++) {
    	            searchMap.put("deptMappingId", deptMappingIds[i]);
    	            returnMap = insertData("bsc.base.scDeptMapping.insertDeptMappingData", searchMap);
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
     * 성과조직매핑관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] scDeptMappingIds = searchMap.getString("scDeptMappingIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < scDeptMappingIds.length; i++) {
	            searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
	            returnMap = updateData("bsc.base.scDeptMapping.deleteData", searchMap);
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
     * 성과조직관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap scDeptMappingExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.SC_DEPT_NM + "매핑";
    	String excelTitle = StringConstants.SC_DEPT_NM + "매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM + "코드", "SC_DEPT_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM + "명", "SC_DEPT_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("실조직코드", "DEPT_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("실조직명", "DEPT_NM", "left", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.scDeptMapping.getExcelList", searchMap));
    	
        return searchMap;
    }
}

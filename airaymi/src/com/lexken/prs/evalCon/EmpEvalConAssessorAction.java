/*************************************************************************
* CLASS 명      : EmpEvalConAssessorAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 18일 
* 기    능      : 직원기여도평가 평가자관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 3월 18일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
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

public class EmpEvalConAssessorAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원기여도평가 평가자관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConAssessorList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConAssessor.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);
    	
    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConAssessor.getDeptList", searchMap)); //인사조직
    	

        return searchMap;
    }
    
    /**
     * 직원기여도평가 평가자관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConAssessorList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.evalCon.empEvalConAssessor.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 사람찾기 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchUser(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("prs.evalCon.empEvalConAssessor.getRealDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptUserInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("userList", getList("prs.evalCon.empEvalConAssessor.getUserList", searchMap));

        return searchMap;
    }
    
    
    
    /**
     * 직원기여도평가 평가자관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConAssessorProcess(SearchMap searchMap) {
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
        } else if("GET".equals(stMode)) {
            searchMap = insertAssessorInfo(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원기여도평가 평가자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            
            String[] deptCds = searchMap.getStringArray("deptCds");
            String[] empns = searchMap.getStringArray("empns");
            
            for(int i=0; i<empns.length; i++){
            	
            	searchMap.put("deptCd", deptCds[i]);
            	returnMap = deleteData("prs.evalCon.empEvalConAssessor.deleteData", searchMap, true);
            	
            	if(null != empns[i] && !"".equals(empns[i])) {
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("empn", empns[i]);
            		returnMap = insertData("prs.evalCon.empEvalConAssessor.insertData", searchMap);
            		
            	}
            }
            
           insertAssessorAdmin(searchMap); //평가가 권한 부여
        
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
     * 평가자 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAssessorInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("prs.evalCon.empEvalConAssessor.deleteEvalGrpInfo", searchMap, true);//평가군매핑삭제
	        returnMap = deleteData("prs.evalCon.empEvalConAssessor.deleteAssessorInfo", searchMap, true);
	        
	        returnMap = insertData("prs.evalCon.empEvalConAssessor.insertEvalGrpInfo", searchMap);//평가군 추가
	        returnMap = insertData("prs.evalCon.empEvalConAssessor.insertAssessorInfo", searchMap);
	        
	       insertAssessorAdmin(searchMap); //평가가 권한 부여
	        
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
     * 평가자 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAssessorAdmin(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();        
        try {
            returnMap = deleteData("prs.evalCon.empEvalConAssessor.deleteAdminDatas", searchMap, true);//권한삭제
            returnMap = insertData("prs.evalCon.empEvalConAssessor.insertAdminDatas", searchMap);//권한등록    
            
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
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
     * 직원기여도평가 평가자관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap empEvalConAssessorListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인기여도평가 평가자관리";
        String excelTitle = "직원개인기여도평가 평가자관리 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "ASSESSOR_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConAssessor.getList", searchMap));
        
        return searchMap;
    }
    
}

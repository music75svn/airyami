/*************************************************************************
* CLASS 명      : EvalMngAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 05월 21일 
* 기    능      : 간부업적평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 05월 21일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class EvalMngAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
//    private final EvalGrpAssessorAction action = new EvalGrpAssessorAction();
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String
     * @throws 
     */
    public SearchMap evalMngList(SearchMap searchMap) {
    	
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.mng.evalMng.getTopDeptInfo", searchMap);

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
    	
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	searchMap.put("findEvalGrpType", "02");
    	searchMap.addList("getEvalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
        //System.out.println(searchMap.get("findKorNm"));
        searchMap.addList("list", getList("prs.mng.evalMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngModify(SearchMap searchMap) {
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));
    	
    	String stMode = searchMap.getString("mode");
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.mng.evalMng.getDetail", searchMap));
    	}
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
        searchMap.addList("getEvalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {        	
        	searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {        	
        	searchMap = deleteDB(searchMap);
        } else if("SAVE".equals(stMode)) {        	
        	searchMap = saveDB(searchMap);	
        } else if("GET".equals(stMode)) {
            searchMap = insertInsaInfo(searchMap);
        } else if("MANAGER".equals(stMode)) {
            searchMap = updateManagerInfo(searchMap);
        }
        return searchMap;
    }
    
    /**
     * 평가대상자  입력
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            
        	returnMap = insertData("prs.mng.evalMng.insertData", searchMap);
	        
	        searchMap.put("adminGubun", "81");
	        returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
        	
        	searchMap.put("adminGubun", "85");
        	returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
            
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
     * 간부평가대상자 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.mng.evalMng.updateData", searchMap);
            
            searchMap.put("adminGubun", "85");
        	returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
            
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
     * 간부평가대상자 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        String[] evalMembEmpns = searchMap.getString("evalMembEmpn").split("\\|",0);
        String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeq").split("\\|",0);
        try {
            
            setStartTransaction();
            
                for (int i = 0; i < evalMembEmpns.length; i++) {
                    
                    searchMap.put("evalMembEmpn", evalMembEmpns[i]);
                    searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
                    
                    returnMap = insertData("prs.mng.evalMng.deleteData", searchMap);
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
     * 평가대상자 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] evalMembEmpns = searchMap.getStringArray("evalMembEmpns");
            String[] evalMembEmpnSeqs = searchMap.getStringArray("evalMembEmpnSeqs");
            
            for(int i=0; i<evalMembEmpns.length; i++){
            	if(null != evalMembEmpns[i]){
            		searchMap.put("evalMembEmpn", evalMembEmpns[i]);
            		searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
            		
            		String evalYn = searchMap.getString("evalYn" + evalMembEmpns[i] + evalMembEmpnSeqs[i]);
            		String prsYn = searchMap.getString("prsYn" + evalMembEmpns[i] + evalMembEmpnSeqs[i]);
            		String orgYn = searchMap.getString("orgYn" + evalMembEmpns[i] + evalMembEmpnSeqs[i]);
            		
            		searchMap.put("evalYn", evalYn);
            		searchMap.put("prsYn", prsYn);
            		searchMap.put("orgYn", orgYn);
            		
            		String empn = evalMembEmpns[i];
            		
        			if(!"".equals(empn) ){	
        			
        				returnMap = updateData("prs.mng.evalMng.updateEvalMngData", searchMap);
        				
        			}
        			
//        			if("N".equals(evalYn) || ("N".equals(prsYn))) {
//        				
//        				returnMap = deleteData("prs.mng.evalMng.updateEvalMngGrpData", searchMap, true);
//        			}
            	}
            }
            insertEvalMngInfo(searchMap); //권한 부여 작업
            
            
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
     * 간부평가 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertInsaInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.mng.evalMng.insertPrsMemberData", searchMap);
	        
	        searchMap.put("adminGubun", "81");
	        returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
        	
        	searchMap.put("adminGubun", "85");
        	returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
	        
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
     * 간부개인 평가대상자 권한 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertEvalMngInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	searchMap.put("adminGubun", "81");
        	returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
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
     * 부서장정보 업데이트
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateManagerInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.mng.evalMng.updateManagerData", searchMap);
	        
	        searchMap.put("adminGubun", "85");
        	returnMap = deleteData("prs.mng.evalMng.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.mng.evalMng.insertAdminData", searchMap);
	        
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
     * 평가군(간부) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalGrpList_ajax(SearchMap searchMap) {
    	searchMap.addList("list", getList("prs.mng.evalMng.getEvalGrpList", searchMap));
        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMngListExcel(SearchMap searchMap) {
    	String excelFileName = "간부업적평가 평가대상자";
    	String excelTitle = "간부업적평가 평가대상자 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("castTcNm")));
    	excelSearchInfoList.add(new ExcelVO("평가군", (String)searchMap.get("evalGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("userNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번","EVAL_MEMB_EMPN","center"));
    	excelInfoList.add(new ExcelVO("이름","KOR_NM","center"));
    	excelInfoList.add(new ExcelVO("부서","DEPT_FULL_NM","left"));
    	excelInfoList.add(new ExcelVO("직급","CAST_TC_NM","center"));
    	excelInfoList.add(new ExcelVO("직위","POS_TC_NM","left"));
    	excelInfoList.add(new ExcelVO("평가군","EVAL_GRP_NM","left"));
    	excelInfoList.add(new ExcelVO("평가자그룹","ASSESSOR_GRP_NM","left"));
    	excelInfoList.add(new ExcelVO("부서장","MANAGER_USER_NM","center"));
    	excelInfoList.add(new ExcelVO("직무수행시작일","FROM_DT","center"));
    	excelInfoList.add(new ExcelVO("직무수행종료일","TO_DT","center"));
    	excelInfoList.add(new ExcelVO("간부평가대상여부","EVAL_YN","center"));
    	excelInfoList.add(new ExcelVO("개인업적평가여부","PRS_YN","center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.mng.evalMng.getList", searchMap));
    	
    	
        return searchMap;
    }
}

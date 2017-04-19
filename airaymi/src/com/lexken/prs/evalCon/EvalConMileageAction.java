/*************************************************************************
* CLASS 명      : EvalConIMileageAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 19일 
* 기    능      : 직원개인기여도평가 마일리지관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 3월 19일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EvalConMileageAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인기여도평가 마일리지관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	
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
    	
    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직
    	
    	String findNameEmpn = (String)searchMap.get("findNameEmpn");
    	
    	if("findName".equals(findNameEmpn)){
    		searchMap.put("findName", findNameEmpn);
    	}else{
    		searchMap.put("findEmpn", findNameEmpn);
    	}

		searchMap.put("nameEmpn", findNameEmpn);

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 마일리지관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageList_xml(SearchMap searchMap) {

    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		searchMap.put("adminYn", "Y");
    	} else{
    		searchMap.put("adminYn", "N");
    	}
    	
    	searchMap.put("sabun",loginVO.getSabun());
    	
        searchMap.addList("list", getList("prs.evalCon.evalConMileage.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 직원개인기여도평가 마일리지관리 상세조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageDetailList(SearchMap searchMap) {
    	
    	String finalMileage = getStr("prs.evalCon.evalConMileage.getFinalMileage", searchMap);
 	   
 	    searchMap.put("finalMileage", finalMileage);
    	
    	return searchMap;
    }
    
    
    /**
    * 직원개인기여도평가 마일리지관리 데이터 조회(xml)
    * @param      
    * @return String  
    * @throws 
    */
   public SearchMap evalConMileageDetailList_xml(SearchMap searchMap) {
	   
	   searchMap.put("empn", searchMap.get("findEmpn"));
	   
	   //searchMap.addList("finalMileage", getStr("prs.evalCon.evalConMileage.getFinalMileage", searchMap));
	   
       searchMap.addList("list", getList("prs.evalCon.evalConMileage.getDetailList", searchMap));

       return searchMap;
   }
   
   /**
    * 직원개인기여도평가 마일리지관리 데이터 조회(xml)
    * @param      
    * @return String  
    * @throws 
    */
   public SearchMap evalConGubun_ajax(SearchMap searchMap) {
	   
	   searchMap.addList("gubun", getList("prs.evalCon.evalConMileage.getGubunId", searchMap));
	   return searchMap;
   }
   
    
    /**
     * 직원개인기여도평가 마일리지관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        /*if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.evalCon.evalConMileage.getDetail", searchMap));
        }*/
        searchMap.addList("finalMileage", getStr("prs.evalCon.evalConMileage.getFinalMileage", searchMap));
        
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 마일리지관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
       if("GET".equals(stMode)) {
            searchMap = insertMileageDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 마일리지관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageDetailProcess(SearchMap searchMap) {
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
    	} else if("DEL".equals(stMode)) {
    		searchMap = deleteDB(searchMap);
    	}
    	
    	/**********************************
    	 * Return
    	 **********************************/
    	return searchMap;
    }
    
    /**
     * 직원개인기여도평가 마일리지관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertMileageDB(SearchMap searchMap) {
    	 HashMap returnMap    = new HashMap();    
         
         try {
             setStartTransaction();
             
            returnMap = insertData("prs.evalCon.evalConMileage.insertMileage", searchMap); // 평가대상자(제외자 포함) 마일리지 추가
             
             List empnList = getList("prs.evalCon.evalConMileage.getEmpn", searchMap);
             String[] empn = new String[0]; 
             ArrayList list = new ArrayList();
             System.out.println("================= empnList.size()======="+ empnList.size());
             if(null != empnList && 0 < empnList.size()) {
            	 System.out.println("================1===================");
            	 empn = new String[empnList.size()];
            	 for (int i = 0; i < empnList.size(); i++) {
                	 System.out.println("================i==================="+i);
            		 HashMap map = (HashMap)empnList.get(i);
 					 searchMap.put("empn", (String)map.get("EMPN"));
        		     returnMap = insertData("prs.evalCon.evalConMileage.insertMileageAddData", searchMap); // 평가대상자 마일리지 이력 추가
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
    	 * 직원개인기여도평가 마일리지관리 등록
    	 * @param      
    	 * @return String  
    	 * @throws 
    	 */
    	public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.evalCon.evalConMileage.insertData", searchMap);
        
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
     * 직원개인기여도평가 마일리지관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        try {
        	String[] mileageNos = searchMap.getString("mileageNos").split("\\|", 0);
        	String empn = searchMap.getString("empn");
        	
            setStartTransaction();
            
            if(null != mileageNos && 0 < mileageNos.length) {
		        for (int i = 0; i < mileageNos.length; i++) {
		            searchMap.put("mileageNo", mileageNos[i]);
		            searchMap.put("empn", empn);
		            returnMap = updateData("prs.evalCon.evalConMileage.updateMileageUse", searchMap);
		            returnMap = updateData("prs.evalCon.evalConMileage.deleteMileageUse", searchMap);
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    /**
     * 직원개인기여도평가 일괄 마일리지 부여
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageBatchDetail(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConMember.getTopDeptInfo", searchMap);

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

    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 일괄 마일리지 부여 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConMileageBatchProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
        	searchMap = roopDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 일괄 마일리지 부여
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB1(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	//returnMap = updateData("prs.evalCon.evalConMileage.insertDataBatch", searchMap);

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
     * 평가제외대상자세팅
     * @param
     * @return String
     * @throws
     */
    public SearchMap roopDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	String[] empns = searchMap.getString("empns").split("\\|", 0);

        	setStartTransaction();

        	if(null != empns) {
		        for (int i = 0; i < empns.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(empns[i]))) {
			        	searchMap.put("empn", empns[i]);
			        	searchMap.put("total", getInt("prs.evalCon.evalConMileage.getFinalMileage2", searchMap));
			        	
			        	int total = searchMap.getInt("total");
			        	
			        	if("U".equals(searchMap.get("gubunId"))){
		    				total  = total - (searchMap.getInt("mileage"));
		    			}else{
		    				total  = total + (searchMap.getInt("mileage"));
		    			}
			        	
			        	/*
			        	if(total < 0){
			            	setRollBackTransaction();
			            	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_CHK_MILEAGE_OVER_CODE);
			    			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_CHK_MILEAGE_OVER_MESSAGE);
			    		}
			        	*/
			        	
			        	searchMap.put("total", total);
			        	
			        	returnMap = insertData("prs.evalCon.evalConMileage.insertData", searchMap);
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
     * 직원개인평가(일괄 마일리지 부여) 사용자 목록
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalConMileageUserInfo_ajax(SearchMap searchMap) {
    	 searchMap.addList("userList", getList("prs.evalCon.evalConMileage.selectUserList", searchMap));
    	return searchMap;
    }
    
    /**
     * 마일리지관리 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalConMileageListExcel(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String excelFileName = "마일리지관리";
    	String excelTitle = "마일리지관리목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서", "DEPT_KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("당해년도 평가등급", "GRADE", "center"));
    	excelInfoList.add(new ExcelVO("당해년도 마일리지", "MILEAGE", "center"));
    	excelInfoList.add(new ExcelVO("전년도 마일리지", "MILEAGE2", "center"));
    	/*
    	excelInfoList.add(new ExcelVO("총마일리지", "TOTAL_MILEAGE", "center"));
    	excelInfoList.add(new ExcelVO("사용마일리지", "USE_MILEAGE", "center"));
    	excelInfoList.add(new ExcelVO("잔여마일리지", "FINAL_MILEAGE", "center"));
    	*/
    	excelInfoList.add(new ExcelVO("전년도+당해년도 마일리지", "SUM_MILEAGE", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	

    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		searchMap.put("adminYn", "Y");
    	} else{
    		searchMap.put("adminYn", "N");
    	}
    	
    	searchMap.put("sabun",loginVO.getSabun());
    	
    	searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.evalConMileage.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
}

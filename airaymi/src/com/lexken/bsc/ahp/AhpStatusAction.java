/*************************************************************************
* CLASS 명      : AhpStatusAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 29일 
* 기    능      : AHP 평가현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 29일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.ahp;
    
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

public class AhpStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * AHP 평가현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpStatusList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalUserGrpList", getList("bsc.ahp.ahpStatus.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.ahp.ahpStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.ahp.ahpStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * AHP 평가현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpStatusProcess(SearchMap searchMap) {
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
         * Return
         **********************************/
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
     * AHP 평가현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가현황";
    	String excelTitle = "AHP 평가현황 리스트";
    	
    	String evalUserGrpId = searchMap.getString("evalUserGrpId");
    	searchMap.put("findEvalUserGrpId", evalUserGrpId);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EVAL_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("성명", "USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상그룹", "ITEM_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가문항", "EVAL_CNT", "center"));
    	excelInfoList.add(new ExcelVO("전체문항", "TOT_CNT", "center"));
    	excelInfoList.add(new ExcelVO("평가제출 여부", "EVAL_SUBMIT_YN_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpStatus.getList", searchMap));
    	
        return searchMap;
    }
    
}

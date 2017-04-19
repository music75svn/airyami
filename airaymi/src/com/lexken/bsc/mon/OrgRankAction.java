/*************************************************************************
* CLASS 명      : OrgRankAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 8월 13일 
* 기    능      : 조직순위
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 8월 13일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
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

public class OrgRankAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직순위 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgRankList(SearchMap searchMap) {
    	
    	searchMap.addList("perspectiveList", getList("bsc.mon.orgRank.getPerspectiveList", searchMap));
    	
    	/************************************************************************************
    	 * 지표입력 기한조회
    	 ************************************************************************************/
    	searchMap.addList("perspectiveFirstId", getStr("bsc.mon.orgRank.getPerspectiveFirstId", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 조직순위 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgRankList_xml(SearchMap searchMap) {
    	
    	ArrayList codeList = (ArrayList)getList("bsc.mon.orgRank.getPerspectiveList", searchMap);
   	 
    	String[] perspectiveList = new String[0]; 
    	if(null != codeList && 0 < codeList.size()) {
    		perspectiveList = new String[codeList.size()];
    		for(int i=0; i<codeList.size(); i++) {
    			HashMap map = (HashMap)codeList.get(i);
    			perspectiveList[i] = (String)map.get("PERSPECTIVE_ID"); 
    		}
    	}
    	
    	searchMap.put("perspectiveList", perspectiveList);
    	searchMap.addList("perspectiveList", codeList);
        
        searchMap.addList("list", getList("bsc.mon.orgRank.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직순위 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap orgRankListExcel(SearchMap searchMap) {
    	String excelFileName = "조직순위";
    	String excelTitle = "조직순위 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEARMON, (String)searchMap.get("yearNm")+(String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("평가그룹", (String)searchMap.get("scDeptGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("조직레벨", StaticUtil.nullToDefault((String)searchMap.get("levelNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("분석주기", StaticUtil.nullToDefault((String)searchMap.get("analCycleNm"), "당월")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("순위", "RANK", "center"));
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM, "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("총점", "CONVERSION_SCORE", "right"));

    	
    	
    	
    	
    	ArrayList codeList = (ArrayList)getList("bsc.mon.orgRank.getPerspectiveList", searchMap);
      	 
    	String[] perspectiveList = new String[0]; 
    	if(null != codeList && 0 < codeList.size()) {
    		perspectiveList = new String[codeList.size()];
    		for(int i=0; i<codeList.size(); i++) {
    			HashMap map = (HashMap)codeList.get(i);
    			perspectiveList[i] = (String)map.get("PERSPECTIVE_ID"); 
    			excelInfoList.add(new ExcelVO((String)map.get("PERSPECTIVE_NM"), (String)map.get("COL_NM"), "right"));
    		}
    	}
    	
    	searchMap.put("perspectiveList", perspectiveList);
    	searchMap.addList("perspectiveList", codeList);
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
        
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.orgRank.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * Top 하위레벨 메뉴조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptGrp_ajax(SearchMap searchMap) {
    	searchMap.addList("scDeptGrpList", getList("bsc.mon.orgRank.getScDeptGrp", searchMap));

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
    
    
}

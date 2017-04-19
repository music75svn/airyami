/*************************************************************************
* CLASS 명      : EvalResultAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 25일 
* 기    능      : 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 10월 25일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;
    
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

public class EvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList(SearchMap searchMap) {
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	searchMap.addList("itemGrpList", getList("bsc.level.evalResult.getItemGrpList", searchMap));
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.level.evalResult.getItemList", searchMap));
    	
    	/**********************************
         * 항목그룹 정보 가져오기
         **********************************/
    	searchMap.addList("itemGrpInfoList", getList("bsc.level.evalResult.getItemGrpInfoList", searchMap));
    	
    	/************************************************************************************
    	 * 첫번째 평가항목그룹 조회
    	 ************************************************************************************/
    	searchMap.addList("firstItemGrpId", getStr("bsc.level.evalResult.getFirstItemGrpId", searchMap));
    	
    	/**********************************
         * 평가단 가져오기
         **********************************/
    	searchMap.addList("evalUserGrpList", getList("bsc.level.evalStatus.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	ArrayList itemGrpList = (ArrayList)getList("bsc.level.evalResult.getItemGrpList", searchMap);
     	 
    	String[] itemGrpArray = new String[0]; 
    	if(null != itemGrpList && 0 < itemGrpList.size()) {
    		itemGrpArray = new String[itemGrpList.size()];
    		for(int i=0; i<itemGrpList.size(); i++) {
    			HashMap map = (HashMap)itemGrpList.get(i);
    			itemGrpArray[i] = (String)map.get("ITEM_GRP_ID"); 
    		}
    	}
    	
    	searchMap.put("itemGrpArray", itemGrpArray);
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.level.evalResult.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.addList("itemGrpList", itemGrpList);
    	searchMap.addList("itemList", itemList);
        searchMap.addList("list", getList("bsc.level.evalResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.level.evalResult.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultProcess(SearchMap searchMap) {
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
        if("APPLY".equals(stMode)) {
            searchMap = applyDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 조정계수 반영
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap applyDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.level.evalResult.applyData", searchMap);
        
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "난이도평가결과";
    	String excelTitle = "난이도 평가결과 리스트";
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalUserGrpId")))) {
			searchMap.put("findEvalUserGrpId", (String)searchMap.get("evalUserGrpId"));
		}
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	ArrayList itemGrpList = (ArrayList)getList("bsc.level.evalResult.getItemGrpList", searchMap);
     	 
    	String[] itemGrpArray = new String[0]; 
    	if(null != itemGrpList && 0 < itemGrpList.size()) {
    		itemGrpArray = new String[itemGrpList.size()];
    		for(int i=0; i<itemGrpList.size(); i++) {
    			HashMap map = (HashMap)itemGrpList.get(i);
    			itemGrpArray[i] = (String)map.get("ITEM_GRP_ID"); 
    		}
    	}
    	
    	searchMap.put("itemGrpArray", itemGrpArray);
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	ArrayList itemList = (ArrayList)getList("bsc.level.evalResult.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	searchMap.addList("itemGrpList", itemGrpList);
    	searchMap.addList("itemList", itemList);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", StaticUtil.nullToDefault((String)searchMap.get("evalUserGrpNm"), "")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM, "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO(StringConstants.WEIGHT, "WEIGHT", "center"));
    	
    	if(null != itemList && 0 < itemList.size()) {
    		for(int i = 0;i < itemList.size(); i++) {
    			HashMap tmap = (HashMap)itemList.get(i);
    			excelInfoList.add(new ExcelVO((String)tmap.get("ITEM_NM"), (String)tmap.get("ITEM_COL_NM"), "center"));
    		}
    	}
    	
    	if(null != itemGrpList && 0 < itemGrpList.size()) {
    		for(int i = 0;i < itemGrpList.size(); i++) {
    			HashMap tmap = (HashMap)itemGrpList.get(i);
    			excelInfoList.add(new ExcelVO((String)tmap.get("ITEM_GRP_NM") + " 점수", (String)tmap.get("ITEM_GRP_SCORE_COL_NM"), "center"));
    			excelInfoList.add(new ExcelVO((String)tmap.get("ITEM_GRP_NM") + " 등급", (String)tmap.get("ITEM_GRP_GRADE_COL_NM"), "center"));
    		}
    	}
    	
    	excelInfoList.add(new ExcelVO("조정계수", "ADJUST_RATE", "center"));
    	excelInfoList.add(new ExcelVO("담당자", "USER_NM", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.evalResult.getList", searchMap));
    	
        return searchMap;
    }
    
}

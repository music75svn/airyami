/*************************************************************************
* CLASS 명      : MatrixResultAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 10월 26일 
* 기    능      : 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 10월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.matrix;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class MatrixResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixResultList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalGrpList", getList("bsc.matrix.matrixKpi.getMatEvalList", searchMap));
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	searchMap.addList("itemGrpList", getList("bsc.matrix.matrixResult.getItemGrpList", searchMap));
    	
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.matrix.matrixResult.getItemList", searchMap));
    	
    	/**********************************
         * 항목그룹 정보 가져오기
         **********************************/
    	searchMap.addList("itemGrpInfoList", getList("bsc.matrix.matrixResult.getItemGrpInfoList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixResultList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	ArrayList itemGrpList = (ArrayList)getList("bsc.matrix.matrixResult.getItemGrpList", searchMap);
     	 
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
    	ArrayList itemList = (ArrayList)getList("bsc.matrix.matrixResult.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	searchMap.addList("itemGrpList", getList("bsc.matrix.matrixResult.getItemGrpList", searchMap));
    	
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.matrix.matrixResult.getItemList", searchMap));
        
        searchMap.addList("list", getList("bsc.matrix.matrixResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("CHOICE".equals(stMode)) {
            searchMap = choiceDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 지표선정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap choiceDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	       String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != metricGrpIds && 0 < metricGrpIds.length) {
		        for (int i = 0; i < metricGrpIds.length; i++) {
		            searchMap.put("metricGrpId", metricGrpIds[i]);
		            returnMap = updateData("bsc.matrix.matrixResult.choiceData", searchMap);
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
    public SearchMap matrixResultListExcel(SearchMap searchMap) {
    	String excelFileName = "평가결과";
    	String excelTitle = "평가결과 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("evalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("POOL구분", (String)searchMap.get("poolGubunNm")));
    	excelSearchInfoList.add(new ExcelVO("선정여부", (String)searchMap.get("choiceNm")));
    	excelSearchInfoList.add(new ExcelVO("평점", (String)searchMap.get("scoreNm")));
    	
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	ArrayList itemGrpList = (ArrayList)getList("bsc.matrix.matrixResult.getItemGrpList", searchMap);
     	 
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
    	ArrayList itemList = (ArrayList)getList("bsc.matrix.matrixResult.getItemList", searchMap);
    	 
    	String[] itemArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	/**********************************
         * 항목그룹 가져오기
         **********************************/
    	searchMap.addList("itemGrpList", getList("bsc.matrix.matrixResult.getItemGrpList", searchMap));
    	
    	
    	/**********************************
         * 항목 가져오기
         **********************************/
    	searchMap.addList("itemList", getList("bsc.matrix.matrixResult.getItemList", searchMap));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("POOL 구분", "POOL_GUBUN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상지표 POOL 명", "METRIC_GRP_NM", "left"));
    	
    	if(null != itemGrpList && 0 < itemGrpList.size()) {
    		for(int i = 0;i < itemGrpList.size(); i++) {
    			HashMap tmap = (HashMap)itemGrpList.get(i);
    			for(int j = 0;j < itemList.size(); j++) {
    				HashMap imap = (HashMap)itemList.get(j);
    				if(((String)imap.get("ITEM_GRP_ID")).equals((String)tmap.get("ITEM_GRP_ID"))){
    					excelInfoList.add(new ExcelVO((String)imap.get("ITEM_NM"), (String)imap.get("COL_NM"), "center"));
    				}
    			}
    			excelInfoList.add(new ExcelVO((String)tmap.get("ITEM_GRP_NM") + " 평점", (String)tmap.get("COL_NM"), "center"));
    		}
    	}
    	
    	excelInfoList.add(new ExcelVO("평정", "METRIC_GRP_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("순위", "KPI_RANK", "left"));
    	excelInfoList.add(new ExcelVO("선정 여부", "CHOICE_YN", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.matrix.matrixResult.getList", searchMap));
    	
        return searchMap;
    }
    
}

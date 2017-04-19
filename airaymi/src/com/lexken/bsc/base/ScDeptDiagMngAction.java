/*************************************************************************
* CLASS 명      : ScDeptDiagMngAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 16일 
* 기    능      : 조직성과도관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 16일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.StaticUtil;

public class ScDeptDiagMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직성과도관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMngList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topScDept)) {
    		topScDept = new HashMap();
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findScDeptId = (String)searchMap.getString("findScDeptId", (String)topScDept.get("SC_DEPT_ID"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findScDeptNm = (String)searchMap.getString("findScDeptNm", (String)topScDept.get("SC_DEPT_NM"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findScDeptId", findScDeptId);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findScDeptNm", findScDeptNm);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	
    	if("".equals((String)searchMap.get("findDiagramGubun"))) {
    		searchMap.put("findDiagramGubun","01");
    	}
    	
    	return searchMap;
    }
    
    /**
     * 조직성과도관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMngList_xml(SearchMap searchMap) {
        
//        searchMap.addList("list", getList("bsc.base.scDeptDiagMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직성과도관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.scDeptDiagMng.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 조직성과도관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        String stMode = searchMap.getString("mode");

		/**********************************
         * 수정/배경화면 파일삭제처리
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("FILEDEL".equals(stMode)) {
        	searchMap = deleteBackgroundFile(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 배경화면 파일삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteBackgroundFile(SearchMap searchMap) {
        HashMap returnMap       = new HashMap();
        FileHelper csFileHelper = new FileHelper();
        
        try {
        	
            String year = searchMap.getString("year");
            String scDeptId = searchMap.getString("scDeptId");
            
            /**********************************
             * 파일경로 설정
             **********************************/
            FileConfig fileConfig		= FileConfig.getInstance();
    		String stRealRootPath       = fileConfig.getProperty("FILE_WEB_ROOT_PATH");  // D:/00.workspace/00.bsc_workspace/BSC/WebContent
    		String stBackgroundPath 	= fileConfig.getProperty("FILE_BACKGROUND_ROOT_PATH"); // /images/flash/VBOXML
    		//String saveFile = stRealRootPath + stBackgroundPath;

    		String	saveFile = "/package/kgshome/kgsDomain7" + stBackgroundPath; //운영반영 때만
    		
    		scDeptId = searchMap.getString("findDiagramGubun");
    		if(scDeptId.equals("01")) {
    			scDeptId = "D000001";
    		} else if(scDeptId.equals("02")) {
    			scDeptId = "D000002";
    		} else if(scDeptId.equals("03")) {
    			scDeptId = "D000003";
    		}
    		
    		saveFile = saveFile + "/" + "VBOX_ORG_BG_" + year + scDeptId + ".jpg";
    		
    		csFileHelper.deleteFile(saveFile);
        
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
     * 조직성과도관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.base.scDeptDiagMng.insertData", searchMap);
        
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
     * 조직성과도관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        String codes		= searchMap.getString("code");
        //String findYear		= searchMap.getString("findYear");
        //String findScDeptId	= searchMap.getString("findScDeptId");
    	String arr_code[] 	= codes.split(";");

        try {
	        setStartTransaction();
	        
	    	if("".equals((String)searchMap.get("findDiagramGubun"))) {
	    		searchMap.put("findDiagramGubun","01");
	    	}
	        
	        /**********************************
             * 조직성과도 삭제
             **********************************/
	        returnMap = updateData("bsc.base.scDeptDiagMng.deleteData", searchMap, true);
	        
	        /**********************************
             * 조직성과도 등록
             **********************************/
	        if(null != arr_code) {
		        for (int i = 0; i < arr_code.length; i++) {
		    		if (arr_code[i].trim().length() > 0 ) {
		    			String arr_values[]		= arr_code[i].trim().split(",");
		    			String dwDeptId       	= arr_values[0].trim();
		    			String subDeptXPos   	= arr_values[1].trim();
		    			String subDeptYPos   	= arr_values[2].trim();
	
		    			dwDeptId     	= dwDeptId.substring(dwDeptId.indexOf('=') + 1);
		    			subDeptXPos 	= subDeptXPos.substring(subDeptXPos.indexOf('=') + 1);
		    			subDeptYPos 	= subDeptYPos.substring(subDeptYPos.indexOf('=') + 1);
	
		    			//searchMap.put("findYear", findYear);
		    			searchMap.put("dwDeptId", dwDeptId);
		    			searchMap.put("subDeptXPos", subDeptXPos);
		    			searchMap.put("subDeptYPos", subDeptYPos);
		    			//searchMap.put("findScDeptId", findScDeptId);
		    			
		    			returnMap = updateData("bsc.base.scDeptDiagMng.insertData", searchMap);
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
     * 조직성과도관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 조직성과도관리 신호등 데이터(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap trafficSignal_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.base.scDeptDiagMng.getSignal", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 조직성과도관리 조직도 데이터(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMng_xml(SearchMap searchMap) {

    	if("".equals((String)searchMap.get("findDiagramGubun"))) {
    		searchMap.put("findDiagramGubun","01");
    	}
    	
    	searchMap.addList("list", getList("bsc.base.scDeptDiagMng.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 조직성과도 배경화면 업로드 팝업창 호출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachScDeptDiagBackground(SearchMap searchMap) {
    	
    	return searchMap;
    	
    }
    
    /**
     * 조직성과도 배경화면 업로드
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachScDeptDiagBackgroundProcess(SearchMap searchMap) {
    	String findYear = (String)searchMap.getString("findYear");
    	String findScDeptId = (String)searchMap.getString("findScDeptId");
    	
    	findScDeptId = (String)searchMap.getString("findDiagramGubun");
		if(findScDeptId.equals("01")) {
			findScDeptId = "D000001";
		} else if(findScDeptId.equals("02")) {
			findScDeptId = "D000002";
		} else if(findScDeptId.equals("03")) {
			findScDeptId = "D000003";
		}
		
    	/**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopyBackground("/temp", "VBOX_ORG_BG", findYear, findScDeptId);
		
        return searchMap;
    }
    
    /**
     * 조직성과도관리 조직도 데이터(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap scDeptDiagMng2_xml(SearchMap searchMap) {

    	if("".equals((String)searchMap.get("findDiagramGubun"))) {
    		searchMap.put("findDiagramGubun","01");
    	}
    	
    	searchMap.addList("list", getList("bsc.base.scDeptDiagMng.getList2", searchMap));
    	
    	return searchMap;
    	
    }
    
}

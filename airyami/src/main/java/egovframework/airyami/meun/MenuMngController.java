package egovframework.airyami.meun;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;

/**
 * 
 * 메뉴를 관리한다. 목록조회/상세조회/등록/수정
 * 
 * @author 배수한
 * @since 2017.05.11 
 * @version 1.0
 * @see
 *
 * <pre>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.05.11      배수한             최초 생성
 *
 * </pre>
 */

@Controller
public class MenuMngController {
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());

	/** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
    
	/**
	 *  공통 Service 
	 */ 	
	@Resource(name="egovMessageSource")
	protected EgovMessageSource egovMessageSource;
	
	/** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService; 
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
    /**
     * 메뉴 목록조회 페이지 이동 
     */
    @RequestMapping(value="/menu/menuView.do")
    public String goMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	//사이트 목록을 가져와서 모델에 넣는다.
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>>> :: " + params);
    	CommonUtils.setModelByParams(model, params, request);

    	params.put( "CODE_GROUP_ID", "MENU_TYPE" ); //
		List<ValueMap> code_MENU_TYPE = commCodeService.selectCommCode(params);
		model.put("ds_cd_MENU_TYPE", code_MENU_TYPE);
		
		params.put( "CODE_GROUP_ID", "USER_TYPE" ); //
		List<ValueMap> code_USER_TYPE = commCodeService.selectCommCode(params);
		model.put("ds_cd_USER_TYPE", code_USER_TYPE);
        
    	return "/menu/menuView";
    }
    
    /**
     * 메뉴 목록조회 
     */
    @RequestMapping(value="/menu/menuList.do")
    public String getMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.getMenuList() ================== start]");
		
    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************		
    	
    	try{  	        
    		List<ValueMap> ds_list = cmmService.getCommDbList(params, "menu.getMenuList");
    		result.put("ds_menulist", ds_list);
    	} catch(Exception e){
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }  
    
    /**
     * 메뉴 상세조회 
     */
    @RequestMapping(value="/menu/selectMenu.do")
    public String selectMenuView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.selectMenu() ================== start]");

    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************
		
    	try{
    		ValueMap ds_detail =  cmmService.getCommDbMap(params, "menu.selectMenu");	    		
    		result.put("ds_detail", ds_detail);

    		List<ValueMap> ds_list = cmmService.getCommDbList(params, "menu.selectMenuUserGroup");
    		result.put("ds_menuUsergrplist", ds_list);
    	}
    	catch(Exception e){
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }
    

    /**
     * 메뉴 수정 
     */
    @RequestMapping(value="/menu/updateMenu.do")
    public String updateMenuMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	boolean success = true;
    	ValueMap result = new ValueMap();
    	String msg = egovMessageSource.getMessage("success.common.update");
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.updateMenuMng() ================== start]");

		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************
    	try{
    		int maxOrder = cmmService.getCommDbInt(params, "menu.selectMenuOrderMax");
    		
//    		ValueMap ds_detail =  menuMngDAO.selectMenu ( params );
//    		int oldMenuOrder = ds_detail.getInteger("MENU_ORDER");
//    		int menuOder = Integer.parseInt((String)params.get("MENU_ORDER"));
//    		params.put("OLD_MENU_ORDER", oldMenuOrder);
//    		
//    		if(oldMenuOrder > menuOder) {
//    			menuMngDAO.updateMenuOrderUp( params );
//    		} else if(oldMenuOrder < menuOder) {
//    			menuMngDAO.updateMenuOrderDown( params );			
//    		}
    		
    		if(Integer.parseInt((String)params.get("MENU_ORDER")) < 1) {
    			success = false;
    			msg = egovMessageSource.getMessage("fail.menu.minorder");    			
    		} else if(Integer.parseInt((String)params.get("MENU_ORDER")) > maxOrder) {
    			success = false;
    			msg = egovMessageSource.getMessage("fail.menu.maxorder");
    		} else {
        		int updateCnt =  cmmService.updateCommDb(params, "menu.updateMenu");
        		
        		// 메뉴내용 변경
        		if ( updateCnt == 0 ) {
        			success = false;
        			msg = egovMessageSource.getMessage("fail.common.update");
        		}
        		
        		// 메뉴명 변경
        		if(success)
        		{
        			updateCnt =  cmmService.updateCommDb(params, "menu.updateMenuNm");
        			if ( updateCnt == 0 ) {
        				success = false;
        				msg = egovMessageSource.getMessage("fail.common.update");
        			}
        		}

    		}    		
    	}
    	catch(Exception e){
			msg = egovMessageSource.getMessage("fail.common.update");
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}

		//*****************************************************************************
		// 화면 출력 데이터 셋팅
		//*****************************************************************************
		result.put("msg", msg);    		
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));

		log.info("result result :: " + result);
    	return null;
    }

    /**
     * 메뉴 등록 페이지 이동 
     */
    @RequestMapping(value="/menu/goInsertMenu.do")
    public String goInsertMenuMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>> :: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	params.put( "CODE_GROUP_ID", "MENU_TYPE" ); //
		List<ValueMap> code_MENU_TYPE = commCodeService.selectCommCode(params);
		model.put("ds_cd_MENU_TYPE", code_MENU_TYPE);
		
    	//ValueMap siteDetail = siteMngService.selectSiteView(params);

    	//model.put("SITE_NAME", siteDetail.getString("SITE_NAME"));
		return "/menu/menuForm";
    }
    
    /**
     * 메뉴 등록 
     */
    @RequestMapping(value="/menu/insertMenu.do")
    public String insertMenuMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    			
		boolean success = true;
    	ValueMap result = new ValueMap();
    	String msg = egovMessageSource.getMessage("success.common.insert");
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.insertMenuMng() ================== start]");

		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************
		// Transaction Setting(1) 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
    	try{
    		// 신규등록을 위한 기본정보 셋팅
    		ValueMap ds_detail = cmmService.getCommDbMap(params, "menu.selectInsertedMenu");
    		params.put("MENU_CODE", ds_detail.get("MENU_CODE"));
    		params.put("MENU_LEVEL", ds_detail.get("MENU_LEVEL"));
    		params.put("MENU_ORDER", ds_detail.get("MENU_ORDER"));
    		
    		// 테이블 입력
    		cmmService.insertCommDb(params, "menu.insertMenu");
    		cmmService.insertCommDb(params, "menu.insertMenuNm");
    		
    		// TB_USER_GROUP 자동 등록 필요
    		cmmService.insertCommDb(params, "menu.insertMenuUserGroup");
    		
    		transactionManager.commit(status);
    	}
    	catch(Exception e){
			msg = egovMessageSource.getMessage("fail.common.insert");
			transactionManager.rollback(status);
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
		//*****************************************************************************
		// 화면 출력 데이터 셋팅
		//*****************************************************************************
		result.put("msg", msg);
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));

		log.info("result result :: " + result);
    	return null;
		
    }
}
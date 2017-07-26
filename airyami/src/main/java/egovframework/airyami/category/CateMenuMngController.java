package egovframework.airyami.category;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
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
 * 상품 카테고리 메뉴를 관리한다. 목록조회/상세조회/등록/수정
 * 
 * @author 배수한
 * @since 2017.06.01 
 * @version 1.0
 * @see
 *
 * <pre>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.06.01      배수한             최초 생성
 *
 * </pre>
 */

@Controller
public class CateMenuMngController {
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
//    @Resource(name="txManager")
//    PlatformTransactionManager transactionManager;
    
    @Autowired
    private DataSourceTransactionManager transactionManager;
    
    /**
     * 카테고리 목록조회 페이지 이동 
     */
    @RequestMapping(value="/category/cateTreeView.do")
    public String goCateView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	//사이트 목록을 가져와서 모델에 넣는다.
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>>> :: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	return "category/cateTreeView";
    }    
    
    /**
     * 카테고리 목록조회 
     */
    @RequestMapping(value="/category/cateList.do")
    public String getCateList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[CateMenuMngController.getCateList() ================== start]");
		
    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************		
    	
    	try{  	        
    		List<ValueMap> ds_list = cmmService.getCommDbList(params, "cateTree.getCateList");
    		result.put("ds_catelist", ds_list);
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
     * 카테고리 상세조회 
     */
    @RequestMapping(value="/category/selectCate.do")
    public String selectCate(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.selectCate() ================== start]");

    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************
		
    	try{
    		ValueMap ds_detail =  cmmService.getCommDbMap(params, "cateTree.selectCate");	    		
    		result.put("ds_detail", ds_detail);
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
     * 카테고리 수정 
     */
    @RequestMapping(value="/category/updateCate.do")
    public String updateCateMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	boolean success = true;
    	ValueMap result = new ValueMap();
    	String msg = egovMessageSource.getMessage("success.common.update");
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[CateMngController.updateCateMng() ================== start]");

		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************
    	try{
    		int maxOrder = cmmService.getCommDbInt(params, "cateTree.selectCateOrderMax");
    		
    		ValueMap ds_detail =  cmmService.getCommDbMap(params, "cateTree.selectCate");
    		int oldCateOrder = ds_detail.getInteger("CATE_ORDER");
    		int cateOder = Integer.parseInt((String)params.get("CATE_ORDER"));
    		params.put("OLD_CATE_ORDER", oldCateOrder);
    		
    		if(oldCateOrder > cateOder) {
    			cmmService.updateCommDb(params, "cateTree.updateCateOrderUp");
    		} else if(oldCateOrder < cateOder) {
    			cmmService.updateCommDb(params, "cateTree.updateCateOrderDown");			
    		}
    		
    		if(Integer.parseInt((String)params.get("CATE_ORDER")) < 1) {
    			success = false;
    			msg = egovMessageSource.getMessage("fail.cate.minorder");    			
    		} else if(Integer.parseInt((String)params.get("CATE_ORDER")) > maxOrder) {
    			success = false;
    			msg = egovMessageSource.getMessage("fail.cate.maxorder");
    		} else {
        		int updateCnt =  cmmService.updateCommDb(params, "cateTree.updateCate");
        		
        		// 카테고리내용 변경
        		if ( updateCnt == 0 ) {
        			success = false;
        			msg = egovMessageSource.getMessage("fail.common.update");
        		}
        		
        		// 카테고리명 변경
        		if(success)
        		{
        			updateCnt =  cmmService.updateCommDb(params, "cateTree.updateCateNm");
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
     * 카테고리 삭제 
     */
    @RequestMapping(value="/category/deleteCate.do")
    public String deleteCateMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	String msg = egovMessageSource.getMessage("success.common.update");
    	//*****************************************************************************
    	// 입력부 로그 출력 
    	//*****************************************************************************
    	logger.debug("===================================================================");
    	logger.debug("[CateMngController.deleteCateMng() ================== start]");
    	
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
    		// tb_category_nm 삭제
    		int delCnt = cmmService.deleteCommDb(params, "cateTree.deleteCateNm");
    		
    		// tb_category 삭제
    		delCnt = cmmService.deleteCommDb(params, "cateTree.deleteCate");
    		
    		transactionManager.commit(status);
    	}
    	catch(Exception e){
    		msg = egovMessageSource.getMessage("fail.common.delete");
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

    /**
     * 카테고리 등록 페이지 이동 
     */
    @RequestMapping(value="/category/goInsertCate.do")
    public String goInsertCateMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>> :: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	//ValueMap siteDetail = siteMngService.selectSiteView(params);

    	//model.put("SITE_NAME", siteDetail.getString("SITE_NAME"));
		return "/category/cateTreeForm";
    }
    
    /**
     * 카테고리 등록 
     */
    @RequestMapping(value="/category/insertCate.do")
    public String insertCateMng(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    			
		boolean success = true;
    	ValueMap result = new ValueMap();
    	String msg = egovMessageSource.getMessage("success.common.insert");
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[CateMenuMngController.insertCateMng() ================== start]");

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
    		ValueMap ds_detail = cmmService.getCommDbMap(params, "cateTree.selectInsertedCate");
    		params.put("CATE_CODE", ds_detail.get("CATE_CODE"));
    		params.put("CATE_LEVEL", ds_detail.get("CATE_LEVEL"));
    		params.put("CATE_ORDER", ds_detail.get("CATE_ORDER"));
    		
    		// 테이블 입력
    		cmmService.insertCommDb(params, "cateTree.insertCate");
    		
    		if(1==1)
    			throw new Exception();
    		
    		cmmService.insertCommDb(params, "cateTree.insertCateNm");
    		
    		transactionManager.commit(status);
    	}
    	catch(Exception e){
			msg = egovMessageSource.getMessage("fail.common.insert");
			transactionManager.rollback(status);
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    		
    		throw e;
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
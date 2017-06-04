/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.airyami.cmm.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.service.FileService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.PageInfo;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;



/**
 * 
 * 예제용 Controller를 정의한다.
 * @author 배수한
 * @since 2017.05.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.05.01      배수한             최초 생성
 *
 * </pre>
 */

@Controller
public class TemplateController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	/** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
   
    /** FileService */
    @Resource(name = "fileService")
    private FileService fileService;
    
    
    /** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService;
    

    /**
     * 테스트페이지 이동 
     */
    @RequestMapping(value="/template/template.do")
    public String goTestTemplete(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("/template/template param 1111::1111 " + params);
    	CommonUtils.setModelByParams(model, params);	// 전달받은 내용 다른 페이지에 전달할때 사용
    	log.info("params.LANG_CD :: " + params.get("LANG_CD"));
    	
    	//params.clear();
		params.put( "CODE_GROUP_ID", "LANG" ); //언어코드 대분류
//		params.put( "LANG_CD", "ko" ); //언어
		List<ValueMap> code_LANG = commCodeService.selectCommCode(params);
		model.put("ds_cd_LANG", code_LANG);
		
		params.put( "CODE_GROUP_ID", "GRP_CD" ); //대분류 테스트
		List<ValueMap> code_GRP_CD = commCodeService.selectCommCode(params);
		model.put("ds_cd_GRP_CD", code_GRP_CD);
    	
    	return "/template/template";
    }
    
    /**
     * 테스트페이지 이동 
     */
    @RequestMapping(value="/template/template_list.do")
    public String goTemplateList(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("/template/template param 1111::1111 " + params);
    	CommonUtils.setModelByParams(model, params);	// 전달받은 내용 다른 페이지에 전달할때 사용
    	
    	//params.clear();
    	params.put( "CODE_GROUP_ID", "LANG" ); //언어코드 대분류
    	List<ValueMap> code_LANG = commCodeService.selectCommCode(params);
    	model.put("ds_cd_LANG", code_LANG);
    	
    	return "/template/template_list";
    }
    
    
    /**
	 * templateList 로 화면이동
	 * @param 
	 * @param model
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/template/selectList.do")
    public String selectList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	
    	// 이건 체크박스로 리스트에서 값 가지고 오는거..
    	// 일단 리스트에선 패스..
    	/*
    	if(!CommonUtils.isNull((String)params.get("rowDatas"))){
    		List<Object> rParams = JsonUtil.parseToList( (String)params.get("rowDatas") );
    		
    		for(int i = 0 ; i < rParams.size() ; i++){
    			Map<String,Object> rowData = (Map<String, Object>) rParams.get(i);
    			log.debug("rowData.CD :: " + rowData.get("CD"));
    		}
    	}
    	*/
    	
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	
    	PageInfo pageInfo = null;
    	
    	// 엑셀 여부..( 전체조회인지.. 아닌지)
    	if(!params.containsKey("EXCEL_YN"))
    		params.put("EXCEL_YN", "N");
    	 
    	String excelYn = (String)params.get("EXCEL_YN");
    	
    	try{
    		if("N".equals(excelYn)){
    			int pageNo = Integer.parseInt( CommonUtils.NVL((String)params.get("pageNo"), "0")  );
    			int totCnt = commCodeService.listCommCodeCnt(params);
    			
    			if("".equals( pageNo ) || pageNo < 1 ) {
    				pageInfo = new PageInfo(1, totCnt, EgovProperties.getProperty("Globals.DbType"));
    			} else {
    				pageInfo = new PageInfo(pageNo, totCnt, EgovProperties.getProperty("Globals.DbType"));
    			}
    			
    			if( !CommonUtils.isNull( (String)params.get("pageRowCnt") ) ){
    				pageInfo.setPerUnit( Integer.valueOf( (String)params.get("pageRowCnt") ));
    				pageInfo.calculate();
    			}
    			
    			params.put( "STARTUNIT", pageInfo.getStartUnit() );
    			params.put( "ENDUNIT", pageInfo.getEndUnit() );
    			params.put("TOTCNT", totCnt);
    			result.put("totCnt", totCnt);
    		}
    	
    		List<ValueMap> list = commCodeService.listCommCode(params);
    		
    		if("Y".equals(excelYn)){
    			model.put( "excel_name", "GROUPCODE" );
    			model.put( "data", list );
    			if(params.containsKey("SEARCH_CONDITION"))
    				model.put( "excel_condition", params.get("SEARCH_CONDITION") );
    	        
    	        log.debug("excelView Call!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	        
    	        return "excelView";
    		}
    		
    		result.put("ds_list", list);
    		result.put("pageInfo", pageInfo);
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
     * detail 호출 
     */
    @RequestMapping(value="/template/templateView.do")
    public String goTemplateView(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param 1111:: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	
    	ValueMap ds_right = new ValueMap();
    	ds_right.put("RIGHT", "C,W,R");
    	model.put("PAGERIGHT", ds_right.get("RIGHT"));
    	
    	
    	
    	return "/template/templateView";
    }
    
    
    /**
     * detail 상세조회 
     */
    @RequestMapping(value="/template/getCodeView.do")
    public String getCodeView(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		ValueMap ds_detail = commCodeService.getCommCode(params);
    		
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
     * detail 상세조회 
     */
    @RequestMapping(value="/template/getCodeGrpView.do")
    public String getCodeGrpView(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		ValueMap ds_detail = commCodeService.getCodeGrpDetail(params);
    		
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
     * update form 호출 
     */
    @RequestMapping(value="/template/templateForm.do")
    public String goTemplateFrom(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param :: goTemplateFrom " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	return "/template/templateForm";
    }
    
    
    /**
     * popup 호출 
     */
    @RequestMapping(value="/template/template_popup.do")
    public String goTemplatePopup(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param 1111:: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	return "/template/template_popup";
    }
    
    /**
     * popup 호출 
     */
    @RequestMapping(value="/template/templateForm_popup.do")
    public String goTemplateFormPopup(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param 1111:: " + params);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	params.put( "CODE_GROUP_ID", "LANG" ); //코드 대분류
    	List<ValueMap> code_LANG = commCodeService.selectCommCode(params);
    	model.put("ds_cd_LANG", code_LANG);
    	
    	return "/template/templateForm_popup";
    }
    
    
    /**
     * 저장 예제.. merge 
     */
    @RequestMapping(value="/template/saveCodeGrp.do")
    public String saveCodeGrp(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		commCodeService.saveCodeGrp(params);
    	}
    	catch(Exception e){
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    		result.put("msg", egovMessageSource.getMessage("fail.common.msg", CommonUtils.getLocale(request)) );
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	
    	return null;
    }
    
    
    /**
     * 상품정보 상세 조회시  
     */
    @RequestMapping(value="/template/selectProdDetail.do")
    public String selectProdDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	ValueMap ds_detail = cmmService.getCommDbMap(params, "product.getProductDetail");
    	result.put("ds_detail", ds_detail);    
    	
		
		// 첨부파일조회
		if(ds_detail != null)  {// 
			params.put("PROD_NO", ds_detail.getString("PROD_NO"));
			//List<ValueMap> fileList = fileService.selectFileList(ds_detail); // ds_detail에 FILE_MST_SEQ 존재
			List<ValueMap> fileList = cmmService.getCommDbList(params, "prodImg.selectFileList"); // ds_detail에 PROD_NO 존재
			log.debug("fileList = " + fileList);
			ds_detail.put("fileList", fileList);			
	    }
    	
		result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }
    
    /**
     * 상품이미지 저장
     */
    @RequestMapping(value="/template/saveImg.do")
    public String saveCommunityBoardArticle(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param 1111:: " + params);
    	
    	boolean success = true;
//    	String msg = egovMessageSource.getMessage("prodimg.save.success.msg");	// 상품이미지가 저장되없습니다.
    	String msg = "상품이미지가 저장되없습니다.";
    	ValueMap result = new ValueMap();    	

    	try
    	{
    		/*
    		 * 상품이미지 저장
    		 */
    		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	    	final Map<String, MultipartFile> files = multiRequest.getFileMap();
	    	log.info("files ::  " + files);
	    	
	    	if(!files.isEmpty()){
				ValueMap parseResult = null;
				params.put("FOLDER_NM", (String)params.get("PROD_NO"));
				parseResult = fileService.attachImgFiles(files, (String)params.get("PROD_NO"), params);
				success = parseResult.getBoolean("success");
				
				//List<ValueMap> f_list = fileService.selectFileList(params);
				//log.debug("f_list :: " + f_list);
			}
    		
    		
    	} catch(Exception e){
    		success = false;    		
    		//msg = egovMessageSource.getMessage("prodimg.save.fail.msg");	// 상품이미지 등록에 실패하였습니다.
    		msg = "상품이미지 등록에 실패하였습니다.";
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	result.put("msg", msg);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));

    	return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * update form 호출 
     */
    @RequestMapping(value="/template/insertFile.do")
    public String insertFile(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param :: goTemplateFrom " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	final Map<String, MultipartFile> files = multiRequest.getFileMap();
    	log.info("files ::  " + files);
    	
    	
    	
    	if(!files.isEmpty()){
    		ValueMap ds_boardInfo = null;
    		params.put("BOARD_ID", "5555");
    		// FILE MST  정보 또는 커뮤니티 정보
    		if(!CommonUtils.isNull((String)params.get("BOARD_ID"))){
    			//ds_boardInfo = boardMasterService.getBoardMaster(params);
    		}
			ValueMap parseResult = null;
			
			
			// FILE_MST_SEQ 는 처음 인서트일경우에는 null을 넣으면 된다.
			parseResult = fileService.attachFiles(files, (String)params.get("FILE_MST_SEQ"), params, ds_boardInfo);
			params.put("FILE_MST_SEQ", (BigDecimal)parseResult.get("FILE_MST_SEQ"));
			log.debug("result :: " + result);
//			success = parseResult.getBoolean("success");
			
			List<ValueMap> f_list = fileService.selectFileList(params);
			log.debug("f_list :: " + f_list);
		}
    	//boardService.insertBoardFile(params);
    	
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }

}

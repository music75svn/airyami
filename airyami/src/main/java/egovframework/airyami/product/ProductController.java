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
package egovframework.airyami.product;

import java.util.ArrayList;
import java.util.HashMap;
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
 * 상품 Controller를 정의한다.
 * @author 유연주
 * @since 2017.05.09
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.05.17      유연주             최초 생성
 *
 * </pre>
 */

@Controller
public class ProductController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	/** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
	
	/** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService;
    
    /** FileService */
    @Resource(name = "fileService")
    private FileService fileService;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
    /**
     * 상품 리스트 이동 
     */
    @RequestMapping(value="/product/productList.do")
    public String productList(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params);	// 전달받은 내용 다른 페이지에 전달할때 사용
 
    	// 브랜드
    	params.put( "CODE_GROUP_ID", "BRAND" ); //브랜드
    	List<ValueMap> brandList = commCodeService.selectCommCode(params);
    	model.put("ds_brandList", brandList);

    	// 카테고리 대분류 조회
    	params.put( "CATE_LEVEL", "1" ); //카테고리 대분류
    	params.put( "UPPER_CATE_CODE", "1" ); //최상위카테고리
    	List<ValueMap> lCateList = cmmService.getCommDbList(params, "product.getCategoryList");
    	model.put("ds_lCateList", lCateList);    	
    	return "/product/productList";
    }
    
    /**
	 * 상품리스트 조회
	 * @param 
	 * @param model
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/product/selectProductList.do")
    public String selectProductList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
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
    			int totCnt = cmmService.getCommDbInt(params, "product.getProductCount");
    			
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
    			params.put( "TOTCNT", totCnt);
    			result.put( "totCnt", totCnt);
    		}
    	
    		List<ValueMap> list = cmmService.getCommDbList(params, "product.getProductList");
    		
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
     * 상품 detail 호출 
     */
    @RequestMapping(value="/product/productDetail.do")
    public String productDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	// 브랜드
    	params.put( "CODE_GROUP_ID", "BRAND" ); //브랜드 코드
    	List<ValueMap> brandList = commCodeService.selectCommCode(params);
    	model.put("ds_brandList", brandList);
    	
    	// 언어코드
    	params.put( "CODE_GROUP_ID", "LANG" ); //언어코드 코드
    	List<ValueMap> code_LANG = commCodeService.selectCommCode(params);
    	model.put("ds_cd_LANG", code_LANG);
    	
    	// 국가코드 조회
    	params.put( "CODE_GROUP_ID", "COUNTRY_CODE" ); //국가코드
    	List<ValueMap> addrCountryList = commCodeService.selectCommCode(params);
    	model.put("ds_addrCountryList", addrCountryList);
    	
    	// 공급화폐코드 조회
    	params.put( "CODE_GROUP_ID", "CURRENCY" ); //공급화폐코드
    	List<ValueMap> supplyCurrencyList = commCodeService.selectCommCode(params);
    	model.put("ds_supplyCurrencyList", supplyCurrencyList);
    	
    	// 용량단위코드 조회	
    	params.put( "CODE_GROUP_ID", "VOLUME_UNIT" ); //용량단위코드
    	List<ValueMap> volumeUnitList = commCodeService.selectCommCode(params);
    	model.put("ds_volumeUnitList", volumeUnitList);
    	
    	// 중량단위코드 조회	
    	params.put( "CODE_GROUP_ID", "WEIGHT_UNIT" ); //중량단위코드
    	List<ValueMap> weightUnitList = commCodeService.selectCommCode(params);
    	model.put("ds_weightUnitList", weightUnitList);
    	
    	// 상품판매단위코드 조회	
    	params.put( "CODE_GROUP_ID", "SALES_SKU" ); //상품판매단위코드
    	List<ValueMap> salesSkuList = commCodeService.selectCommCode(params);
    	model.put("ds_salesSkuList", salesSkuList);
    	
    	// 카테고리 대분류 조회
    	params.put( "CATE_LEVEL", "1" ); //카테고리 대분류
    	params.put( "UPPER_CATE_CODE", "1" ); //최상위카테고리
    	List<ValueMap> lCateList = cmmService.getCommDbList(params, "product.getCategoryList");
    	model.put("ds_lCateList", lCateList);
    	
    	return "/product/productDetail";
    }
    
    /**
     * 상품 detail 상세조회 
     */
    @RequestMapping(value="/product/getProductDetail.do")
    public String getProductDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		ValueMap ds_detail = cmmService.getCommDbMap(params, "product.getProductDetail");
    		
    		// 상품명 목록 조회
    		List<ValueMap> ds_langNameList = cmmService.getCommDbList(params, "product.getProductNameList");
    		
    		result.put("ds_langNameList", ds_langNameList);
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
     * 상품 detail 저장 
     */
    @RequestMapping(value="/product/saveProduct.do")
    public String saveProduct(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		if("CREATE".equals(params.get("PROC_MODE"))){
    			// 상품코드 발번
    			String prodNo = cmmService.getCommDbString(params, "product.getProdNo");
    			params.put("PROD_NO", prodNo);
    			
    			// 상품 등록
    			List queryList = new ArrayList();
    			Map<String,Object> queryMap = new HashMap();
    			queryMap.putAll(params);
    			queryMap.put("queryGubun", "INSERT");
    			queryMap.put("query", "product.insertProduct");
    			queryList.add(queryMap);
    			
    			// 상품명 등록
    			Map<String,Object> langMap = new HashMap();
    			langMap.putAll(params);
    			langMap.put( "CODE_GROUP_ID", "LANG" ); //언어코드 대분류
    			List<ValueMap> code_LANG = commCodeService.selectCommCode(langMap);
    			for(int i = 0; i < code_LANG.size(); i++){
    				queryMap = new HashMap();
    				queryMap.putAll(params);
    				Map code_LANGMap = (ValueMap)code_LANG.get(i);
    				queryMap.put("PROD_NM", params.get("PROD_NM_"+code_LANGMap.get("CD")));
    				queryMap.put("PROD_SHORT_NM", params.get("PROD_SHORT_NM_"+code_LANGMap.get("CD")));
    				queryMap.put("PRODUCT_EXPL_TEXT", params.get("PRODUCT_EXPL_TEXT_"+code_LANGMap.get("CD")));
    				queryMap.put("LANG_CD", code_LANGMap.get("CD"));
    				queryMap.put("queryGubun", "UPDATE");
    				queryMap.put("query", "product.saveProdNm");
        			
        			log.debug("params : "+queryMap);
        			
        			queryList.add(queryMap);
    			}

    			cmmService.saveCommDbList(queryList);
    		}else if("UPDATE".equals(params.get("PROC_MODE"))){
    			// 상품 수정
    			List queryList = new ArrayList();
    			Map<String,Object> queryMap = new HashMap();
    			queryMap.putAll(params);
    			queryMap.put("queryGubun", "UPDATE");
    			queryMap.put("query", "product.updateProduct");
    			queryList.add(queryMap);
    			
    			// 상품명 등록
    			Map<String,Object> langMap = new HashMap();
    			langMap.putAll(params);
    			langMap.put( "CODE_GROUP_ID", "LANG" ); //언어코드 대분류
    			List<ValueMap> code_LANG = commCodeService.selectCommCode(langMap);
    			for(int i = 0; i < code_LANG.size(); i++){
    				queryMap = new HashMap();
    				queryMap.putAll(params);
    				Map code_LANGMap = (ValueMap)code_LANG.get(i);
    				queryMap.put("PROD_NM", params.get("PROD_NM_"+code_LANGMap.get("CD")));
    				queryMap.put("PROD_SHORT_NM", params.get("PROD_SHORT_NM_"+code_LANGMap.get("CD")));
    				queryMap.put("PRODUCT_EXPL_TEXT", params.get("PRODUCT_EXPL_TEXT_"+code_LANGMap.get("CD")));
    				queryMap.put("LANG_CD", code_LANGMap.get("CD"));
    				queryMap.put("queryGubun", "UPDATE");
    				queryMap.put("query", "product.saveProdNm");
        			
        			log.debug("params : "+queryMap);
        			
        			queryList.add(queryMap);
    			}

    			cmmService.saveCommDbList(queryList);
    		}else if("DELETE".equals(params.get("PROC_MODE"))){
    			// 상품 삭제
    			List queryList = new ArrayList();
    			Map<String,Object> queryMap = new HashMap();
    			queryMap.putAll(params);
    			queryMap.put("queryGubun", "DELETE");
    			queryMap.put("query", "product.deleteProduct");
    			queryList.add(queryMap);
    			
    			// 메시지 삭제
    			queryMap = new HashMap();
    			queryMap.putAll(params);
    			queryMap.put("queryGubun", "DELETE");
    			queryMap.put("query", "product.deleteProductNm");
    			queryList.add(queryMap);
    			cmmService.saveCommDbList(queryList);
    		}
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
     * 하위카테고리 목록을 조회한다. 
     * @param UPPER_CATE_CODE 상위카테고리
     * @param 
     * @return 카테고리 List
     * @exception Exception
     */
    @RequestMapping(value="/category/selectLowerCateList.do")
    public String selectCommCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		List<ValueMap> list = cmmService.getCommDbList(params, "product.getCategoryList");
    		
    		result.put("ds_list", list);
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
     * 상품 popup 호출 
     */
    @RequestMapping(value="/product/productImgPop.do")
    public String goTemplateFormPopup(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	params.put( "CODE_GROUP_ID", "LANG" ); //코드 대분류
    	List<ValueMap> code_LANG = commCodeService.selectCommCode(params);
    	model.put("ds_cd_LANG", code_LANG);
    	
    	params.put( "CODE_GROUP_ID", "IMG_TYPE" ); //상품이미지 종류
    	List<ValueMap> imgType = commCodeService.selectCommCode(params);
    	model.put("ds_imgType", imgType);
    	
    	return "/product/productImgPop";
    }
    
    /**
     * 상품정보 상세 조회시  
     */
    @RequestMapping(value="/product/selectProdDetail.do")
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
    @RequestMapping(value="/product/saveImg.do")
    public String saveCommunityBoardArticle(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param 1111:: " + params);
    	
    	boolean success = true;
    	String msg = egovMessageSource.getMessage("prodimg.save.success.msg");	// 상품이미지가 저장되없습니다.
    	ValueMap result = new ValueMap();    	

    	try
    	{
    		/*
    		 * 사전에 삭제 요청한 것이 있으면 지워준다.
    		 */
	    	if( !CommonUtils.isNull((String)params.get("PROD_NO")) & !CommonUtils.isNull((String)params.get("FILE_DEL_DTL_SEQ")) ){
	    		String sDtlIds = (String)params.get("FILE_DEL_DTL_SEQ");
	    		String[] sDtlArr = sDtlIds.split("\\|");
	    		Map<String,Object> delParams = new HashMap();
	    		
	    		for( int i = 0; i < sDtlArr.length; i++) {
	    			delParams.clear();
	    			delParams.put( "PROD_NO", (String)params.get("PROD_NO"));
	    			delParams.put( "FILE_DTL_SEQ", sDtlArr[i] );
	    			cmmService.deleteCommDb(delParams, "prodImg.deleteFile");
	    		}
	    	}
	    	
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
    		msg = egovMessageSource.getMessage("prodimg.save.fail.msg");	// 상품이미지 등록에 실패하였습니다.
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	result.put("msg", msg);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));

    	return null;
    }
}

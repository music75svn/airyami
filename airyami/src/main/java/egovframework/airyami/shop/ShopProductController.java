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
package egovframework.airyami.shop;

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
public class ShopProductController {
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
     * 상품 detail 호출 
     */
    @RequestMapping(value="/shop/shopProductDetail.do")
    public String shopProductDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	return "/shop/shopProductDetail";
    }
    
    /**
     * 상품 detail 상세조회 
     */
    @RequestMapping(value="/shop/getShopProductDetail.do")
    public String getShopProductDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		ValueMap ds_detail = cmmService.getCommDbMap(params, "product.getProductDetail");
    		
    		// 첨부파일조회
    		if(ds_detail != null)  {// 
    			params.put("PROD_NO", ds_detail.getString("PROD_NO"));
    			//List<ValueMap> fileList = fileService.selectFileList(ds_detail); // ds_detail에 FILE_MST_SEQ 존재
    			List<ValueMap> fileList = cmmService.getCommDbList(params, "prodImg.selectFileList"); // ds_detail에 PROD_NO 존재
    			log.debug("fileList = " + fileList);
    			ds_detail.put("fileList", fileList);			
    	    }
    		
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
     * 상품 장바구니 저장 
     */
    @RequestMapping(value="/shop/saveCart.do")
    public String saveCart(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{

			// 고객  P/R No 발번
			String prNo = cmmService.getCommDbString(params, "shopProduct.getPrNo");
			params.put("PR_NO", prNo);
    		
			// 상품상세 조회
			ValueMap ds_product_detail = cmmService.getCommDbMap(params, "product.getProductDetail");
			
			params.put("PROD_NO", ds_product_detail.get("PROD_NO"));
			params.put("SUPPLY_COUNTRY", ds_product_detail.get("SUPPLY_COUNTRY"));
			params.put("SUPPLY_CURRENCY", ds_product_detail.get("SUPPLY_CURRENCY"));
			params.put("ORG_PRICE", ds_product_detail.get("ORG_PRICE"));
			
			cmmService.insertCommDb(params, "shopProduct.insertCart");
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
     * 장바구니 리스트 이동 
     */
    @RequestMapping(value="/shop/shopCartList.do")
    public String shopCartList(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params);	// 전달받은 내용 다른 페이지에 전달할때 사용
 
    	// 국가코드 조회
    	params.put( "CODE_GROUP_ID", "COUNTRY_CODE" ); //국가 대분류
    	List<ValueMap> addrCountryList = commCodeService.selectCommCode(params);
    	model.put("ds_addrCountryList", addrCountryList);
    	
    	// 국가번호 조회
    	params.put( "CODE_GROUP_ID", "COUNTRY_NUMBER" ); //국가번호 대분류
    	List<ValueMap> addrCountryNumberList = commCodeService.selectCommCode(params);
    	model.put("ds_addrCountryNumberList", addrCountryNumberList);
    	
    	// 지불수단조회
    	params.put( "CODE_GROUP_ID", "PAYMENT_METHOD" ); //지불수단
    	List<ValueMap> paymentMethodList = commCodeService.selectCommCode(params);
    	model.put("ds_paymentMethodList", paymentMethodList);
    	
    	// 이전배송지 조회
    	ValueMap preShipAddrInfo = cmmService.getCommDbMap(params, "shopProduct.getPreShipAddrInfo");
    	model.put("ds_preShipAddrInfo", preShipAddrInfo);
    	
    	params.put("SEARCH_USER_ID", params.get("LOGIN_ID"));
		ValueMap userInfo = cmmService.getCommDbMap(params, "user.getUserDetail");
		model.put("ds_userInfo", userInfo);
    	
    	return "/shop/shopCartList";
    }
    
    /**
	 * 장바구니 조회
	 * @param 
	 * @param model
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/shop/selectShopCartList.do")
    public String selectShopCartList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
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
    			int totCnt = cmmService.getCommDbInt(params, "shop.getShopCartCount");
    			
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
    	
    		List<ValueMap> list = cmmService.getCommDbList(params, "shop.getShopCartList");
    		
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
     * 상품 장바구니 삭제 
     */
    @RequestMapping(value="/shop/deleteCart.do")
    public String deleteCart(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		String[] prNoList = ((String) params.get("PR_NO")).split("@@");
    		
    		for(int i = 0; i < prNoList.length; i++){
    			if(prNoList[i] != null && !"".equals(prNoList[i])){
	    			String prNo = prNoList[i].split("=")[1];
	    			log.debug("prNO2 : "+prNo);
	    			params.put("PR_NO", prNo);
	    			cmmService.deleteCommDb(params, "shop.deleteCart");
    			}
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
     * 상품 구매 저장 
     */
    @RequestMapping(value="/shop/savePurchase.do")
    public String savePurchase(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		String[] prNoList = ((String) params.get("prods[PR_NO]")).split("@@");

    		// PO_NO 발번
			String poNo = cmmService.getCommDbString(params, "shop.getPoNo");
			params.put("PO_NO", poNo);
			
    		// TB_CUSTOMER_PO_HEADER 등록
			cmmService.insertCommDb(params, "shop.insertCustomerPoHeader");
			
    		for(int i = 0; i < prNoList.length; i++){
    			if(prNoList[i] != null && !"".equals(prNoList[i])){
	    			String prNo = prNoList[i].split("=")[1];
	    			log.debug("prNO2 : "+prNo);
	    			params.put("PR_NO", prNo);
    			}
    		}
    		
    		// TB_CUSTOMER_PO_DETAIL 등록
    		cmmService.insertCommDb(params, "shop.insertCustomerPoDetail");
    		
			// 배송지 등록
    		cmmService.insertCommDb(params, "shop.insertCustomerPoShipToAddress");
    		
    		// 배송관리에 PREV_SHIP_TO_ADDR_YN 플레그 Y로 변경
    		if(!"".equals(params.get("SHIP_TO_SEQ"))){
    			// 모두 이전 배송지 'N'로 변경
    			cmmService.updateCommDb(params, "shop.updatePrevAll_N");
    			
    			// 선택된 배송지 'Y'로 변경
    			cmmService.updateCommDb(params, "shop.updatePrev_Y");
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
}

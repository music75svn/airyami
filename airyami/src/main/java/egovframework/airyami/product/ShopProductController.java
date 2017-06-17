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
    @RequestMapping(value="/product/shopProductDetail.do")
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
    	
    	return "/shop/shopProductDetail";
    }
}

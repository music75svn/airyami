package egovframework.airyami.cmm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import egovframework.airyami.cmm.service.ShopService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("shopService")
public class ShopServiceImpl extends AbstractServiceImpl implements ShopService
{
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());

	/** DAO */
    @Resource(name="cmmDAO")
    private CmmDAO cmmDAO;

    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
    /*
     * 구매
     */
	public String savePurchase(Map<String, Object> params) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		TransactionStatus status = transactionManager.getTransaction(def);
		
		String returnVal = "";
		
		try{
		
			String[] prNoList = ((String) params.get("prods[PR_NO]")).split("@@");
	
			List<String> prNoArr = new ArrayList();
			for(int i = 0; i < prNoList.length; i++){
				if(prNoList[i] != null && !"".equals(prNoList[i])){
	    			String prNo = prNoList[i].split("=")[1];
	    			prNoArr.add(prNo);
				}
			}
			params.put("prNoArr", prNoArr);
			List<ValueMap> poHeaderList = cmmDAO.getCommDbList(params, "shop.getPoHeaderList");
			
			log.debug("poHeaderList : "+poHeaderList);
			
			ValueMap poHeaderMap = null;
			for(int i = 0; i < poHeaderList.size(); i++){
				poHeaderMap = poHeaderList.get(i);
				String poNo = cmmDAO.getCommDbString(params, "shop.getPoNo");
				poHeaderMap.put("PO_NO", poNo);
				
	    		// 추천인 회사 아이디 조회
	    		String partnerBizEntityId = cmmDAO.getCommDbString(params, "shop.getPartnerBizEntityId");
	    		poHeaderMap.put("PARTNER_BIZ_ENTITY_ID", partnerBizEntityId);
	    		poHeaderMap.put("PHONE_COUNTRY_NO", params.get("PHONE_COUNTRY_NO"));
	    		poHeaderMap.put("PHONE_NO", params.get("PHONE_NO"));
	    		poHeaderMap.put("PAYMENT_TERMS", params.get("PAYMENT_TERMS"));
	    		poHeaderMap.put("CURRENCY", params.get("LOCAL_CURRENCY"));
	    		
	    		// 환율조회
	    		ValueMap exchangeRateInfo = cmmDAO.getCommDbMap(poHeaderMap, "shop.getExchangeRateInfo");
	    		poHeaderMap.put("BASIC_EXT_RATE", exchangeRateInfo.get("BASIC_EXT_RATE"));
	    		poHeaderMap.put("BIZ_EXT_RATE", exchangeRateInfo.get("BIZ_EXT_RATE"));
	    		poHeaderMap.put("LOGIN_ID", params.get("LOGIN_ID"));
	    		
				long totPoAMt = 0;
				int seq = 0;
				ValueMap poDetailMap = new ValueMap();
	    		for(int j = 0; j < prNoArr.size(); j++){
	    			String prNo = prNoArr.get(j);
	    			poDetailMap.put("PR_NO", prNo);
	    			
	    			// PR_NO로 장바구니 정보 조회
	    			ValueMap shopCartInfo = cmmDAO.getCommDbMap(poDetailMap, "shop.getShopCartInfo");
	    			
	    			if(shopCartInfo.get("SUPPLY_COUNTRY").equals(poHeaderMap.get("SUPPLY_COUNTRY")) &&
	    				shopCartInfo.get("SUPPLY_CURRENCY").equals(poHeaderMap.get("SUPPLY_CURRENCY"))){
	    				
		    			// 한정판매 validation(Y:판매가능, N:판매불가능, F:판매가능하나 마지막임)
		    			String limitFlag = cmmDAO.getCommDbString(shopCartInfo, "shop.getLimitProdValid");
		    			
		    			log.debug("limitFlag : "+limitFlag);
		    			
		    			if("N".equals(limitFlag)){
		    				// 한정판매에 적용
		    				returnVal = "SOLD_OUT";
		    				
		    				return returnVal;
		    			}
	    				
	    				poDetailMap.put("PO_NO", poHeaderMap.get("PO_NO"));
	    				poDetailMap.put("SEQ", ++seq);
	    				poDetailMap.put("PROD_NO", shopCartInfo.get("PROD_NO"));
	    				poDetailMap.put("SELLER_BIZ_ENTITY_ID", shopCartInfo.get("SELLER_BIZ_ENTITY_ID"));
	    				poDetailMap.put("ORG_PRICE", shopCartInfo.get("ORG_PRICE"));
	    				poDetailMap.put("LOGIN_ID", params.get("LOGIN_ID"));
	    				
	    				// UNIT PRICE 함수 조회
	    				ValueMap unitParam = new ValueMap();
	    				unitParam.put("PROD_NO", shopCartInfo.get("PROD_NO"));
	    				unitParam.put("CUSTOMER_ID", params.get("LOGIN_ID"));
	    				unitParam.put("CURRENCY", params.get("LOCAL_CURRENCY"));
	    				unitParam.put("QTY", shopCartInfo.get("PR_QTY"));
	    				log.debug("unitParam : "+unitParam);
	    				String unitPrice = cmmDAO.getCommDbString(unitParam, "shop.getUnitPrice");
	    				
	    				poDetailMap.put("UNIT_PRICE", unitPrice);
	    				poDetailMap.put("PO_QTY", shopCartInfo.get("PR_QTY"));
	    				
	    				log.debug("poDetailMap : "+poDetailMap);
	    				
		    			float poAmt = (Integer)poDetailMap.get("PO_QTY") * Float.parseFloat(unitPrice);
		    			totPoAMt += poAmt;
		    			log.debug("music75 totPoAMt1 : "+totPoAMt);
		    			poDetailMap.put("PO_AMT", poAmt);
		    			
		        		// TB_CUSTOMER_PO_DETAIL 등록
		        		cmmDAO.insertCommDb(poDetailMap, "shop.insertCustomerPoDetail");
		        		
		        		if("F".equals(limitFlag)){
		    				// 한정판매에 판매완료로 세팅
		        			cmmDAO.updateCommDb(poDetailMap, "product.updateSalesLimitProdSoldOut");
		    			}
	    			}
	    		}
	
	    		log.debug("music75 totPoAMt2 : "+totPoAMt);
	    		poHeaderMap.put("TOT_PO_AMT", totPoAMt);
	
	    		log.debug("poHeaderMap : "+poHeaderMap);
				// TB_CUSTOMER_PO_HEADER 등록
				cmmDAO.insertCommDb(poHeaderMap, "shop.insertCustomerPoHeader");
				
				// 배송지 등록
				params.put("PO_NO", poHeaderMap.get("PO_NO"));
				cmmDAO.insertCommDb(params, "shop.insertCustomerPoShipToAddress");
			}
			
			// 배송관리에 PREV_SHIP_TO_ADDR_YN 플레그 Y로 변경
			if(!"".equals(params.get("SHIP_TO_SEQ"))){
				// 모두 이전 배송지 'N'로 변경
				cmmDAO.updateCommDb(params, "shop.updatePrevAll_N");
				
				if("D".equals(params.get("SHIP_TO_SEQ"))){
					// 직접입력한 배송지 등록
					String seq = cmmDAO.getCommDbString(params, "shop.getCustShipToAddrSeq");
					params.put("SEQ", seq);
					cmmDAO.insertCommDb(params, "shop.insertCustShipToAddr");
				}else{
					// 선택된 배송지 'Y'로 변경
					cmmDAO.updateCommDb(params, "shop.updatePrev_Y");
				}
			}
			
			// 사용자 정보에 마지막 주문일 세팅
			params.put("USER_ID", params.get("LOGIN_ID"));
			cmmDAO.updateCommDb(params, "user.updateLastOrderDate");
			
			ValueMap poDetailMap = new ValueMap();
    		for(int j = 0; j < prNoArr.size(); j++){
    			String prNo = prNoArr.get(j);
    			poDetailMap.put("PR_NO", prNo);
    			// 장바구니 삭제
        		cmmDAO.deleteCommDb(poDetailMap, "shop.deleteCart");
    		}
    		
		
		}
    	catch(Exception e){
    		transactionManager.rollback(status);
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
		
		return returnVal;
	}
}

package egovframework.airyami.cmm.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.airyami.cmm.service.KeyService;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("keyService")
public class KeyServiceImpl extends AbstractServiceImpl implements KeyService
{
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());

	/** DAO */
    @Resource(name="keyDAO")
    private KeyDAO keyDAO;

    /*
     * 채번한 신규 번호를 입력한다. 
     * 없으면 1번부터 채번하고 신규로 입력한다.
     * @see kr.co.dreamwise.cmm.service.KeyService#selectForKey(java.util.Map)
     */
    public BigDecimal getKey(Map<String, Object> paramMap) throws Exception {
    	// TODO Auto-generated method stub
    	BigDecimal keyNum = new BigDecimal(1);
    	
    	String sKeyKind = (String)paramMap.get("KEY_KIND");
    	logger.debug("sKeyKind :: " + sKeyKind);
    	
    	// KEY_KIND 가 존재하는지 체크
    	if(checkKey(paramMap)){
    		// 존재하면 select for update 진행
    		keyNum = selectForKey(paramMap);
    		
    		// 신규값으로 update
    		paramMap.put("KEY_NUM", keyNum);
    		updateKey(paramMap);
    	}
    	else{
    		// 미존재시 KEY_KIND 로 새로 생성하고 1을 리턴한다.
    		paramMap.put("KEY_NUM", keyNum);
    		insertKey(paramMap);
    	}
    	
    	return keyNum;
    }
    
    /*
     * 채번테이블에서 해당하는 키값을 읽어온다.  select for update 
     * @see kr.co.dreamwise.cmm.service.KeyService#updateKey(java.util.Map)
     */
    public BigDecimal selectForKey(Map<String, Object> paramMap)
    		throws Exception {
    	// TODO Auto-generated method stub
    	return keyDAO.selectForKey(paramMap);
    }
    
    /*
     * 채번테이블에 사용된 순번을 업데이트 한다.
     * @see kr.co.dreamwise.cmm.service.KeyService#updateKey(java.util.Map)
     */
	public int updateKey(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return keyDAO.updateKey(paramMap);
	}


	/*
	 * 신규 키종류일경우 신규로 입력한다.
	 * @see kr.co.dreamwise.cmm.service.KeyService#insertKey(java.util.Map)
	 */
	public int insertKey(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return keyDAO.insertKey(paramMap);
	}

	/*
	 * 키종류가 존재하는 체크한다.
	 * @see kr.co.dreamwise.cmm.service.KeyService#checkKey(java.util.Map)
	 */
	public boolean checkKey(Map<String, Object> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
		if( keyDAO.checkKey(paramMap) > 0 )
			return true;
		
		return false;
	}
	

   
}

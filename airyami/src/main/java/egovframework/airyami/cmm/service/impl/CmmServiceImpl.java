package egovframework.airyami.cmm.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("cmmService")
public class CmmServiceImpl extends AbstractServiceImpl implements CmmService
{
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());

	/** DAO */
    @Resource(name="cmmDAO")
    private CmmDAO cmmDAO;

    
    /*
     * 메뉴 클릭시 접속 로그 남기기
     */
	public int insertMenuLog(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return cmmDAO.insertMenuLog(paramMap);
	}
	
	/*
	 * 컨텐츠 로그 남기기
	 */
	public int insertContentLog(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return cmmDAO.insertContentLog(paramMap);
	}
	
		
	/***
	 * 관리자 > 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getHeadMenuList ( Map<String,Object> params  ) throws Exception {
		return cmmDAO.getHeadMenuList ( params );

	};
	
	/***
	 * 관리자 > 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getLeftMenuList ( Map<String,Object> params  ) throws Exception {
		return cmmDAO.getLeftMenuList ( params );

	};
	
	/***
	 * 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getMenuList ( Map<String,Object> params  ) throws Exception {
		return cmmDAO.getMenuList ( params );
		
	};
	
	///////////////////////////////////////////////////////////////////////////////
	//공통 서비스 by 유연주
	///////////////////////////////////////////////////////////////////////////////
	
	/***
	 * 공통 서비스 리스트 반환
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getCommDbList ( Map<String,Object> params, String sql) throws Exception{
		return cmmDAO.getCommDbList ( params, sql );
		
	};
	/***
	 * 공통 서비스 Map 반환
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ValueMap getCommDbMap ( Map<String,Object> params, String sql) throws Exception{
		return cmmDAO.getCommDbMap ( params, sql );
		
	};
	/**
	 * 공통 서비스 Integer 반환
	 */
	public int getCommDbInt(Map<String,Object> params, String sql) throws Exception{
		return cmmDAO.getCommDbInt ( params, sql );
		
	};
	/**
	 * 공통 서비스 String 반환
	 */
	public String getCommDbString(Map<String,Object> params, String sql) throws Exception{
		return cmmDAO.getCommDbString ( params, sql );
		
	};
	
	/**
	 * 공통 서비스 등록
	 */
	public void insertCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		cmmDAO.insertCommDb(paramMap, sql);
	}
	
	/**
	 * 공통 서비스 수정
	 */
	public int updateCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		return cmmDAO.updateCommDb(paramMap, sql);
	}
	
	/**
	 * 공통 서비스 삭제
	 */
	public int deleteCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		return cmmDAO.deleteCommDb(paramMap, sql);
	}
}

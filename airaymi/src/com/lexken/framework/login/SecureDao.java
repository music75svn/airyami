/*************************************************************************
* CLASS 명  	: SecureDao
* 작 업 자  	: 박소라
* 작 업 일  	: 2009.06.01
* 기    능  	: 인증관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박소라		 2009.06.01			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.login;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lexken.framework.common.SearchMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SecureDao {
	// log4j 설정
	private final Log logger = LogFactory.getLog(getClass());

	// Instance
	private static SecureDao instance = null;
	public static SecureDao getInstance() {
		if (instance == null) {
			instance = new SecureDao();
		}
		return instance;
	}
	
	/**
	 * 메뉴권한 여부
	 * @param menuId, sqlMap
	 * @return boolean
	 * @throws SQLException  
	 */
	public boolean getMenuGradeFlag(HashMap hashMap, SqlMapClient sqlMap)  throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		List chkList = null;
		boolean	gradeChk = false;				// 메뉴 접근 권한		
		
		/**********************************
		 * 메뉴권한 여부
		 **********************************/
		chkList = (List)sqlMap.queryForList("Secure.menuGradeFlag", hashMap);
		
		// 권한이 있으면 true 를 리턴한다.
		if(chkList != null && chkList.size() > 0)  gradeChk = true;
		
		return gradeChk;		
	}
	
	/**
	 * 페이지 네비게이션 가져오기
	 * @param menuId, sqlMap
	 * @return List
	 * @throws SQLException  
	 */
	public List getPageNavigator(String menuId, SqlMapClient sqlMap)  throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		List navi_list = null;
		
		/**********************************
		 * 페이지 네비게이션 가져오기
		 **********************************/
		navi_list  = (List)sqlMap.queryForList("Secure.getPageNavigator", menuId);

		return navi_list;		
	}

	/**
	 * 로그인 패스워드 확인 정보
	 * @param SearchMap, SqlMapClient
	 * @return HashMap  
	 * @throws SQLException  
	 */
	public HashMap getTryLoginInfo(SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		HashMap loginInfoMap = new HashMap();
		
		/**********************************
		 * 로그인 패스워드 확인 정보
		 **********************************/
		loginInfoMap  = (HashMap)sqlMap.queryForObject("Secure.getTryLoginInfo", searchMap);
		
		return loginInfoMap;
	}
	
	/**
	 * 로그인 정보 조회
	 * @param searchMap
	 * @return LoginVO    
	*/
	public LoginVO getUserInfo(SearchMap searchMap, SqlMapClient sqlMap)  throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		LoginVO loginVO = null;				// 로그인 정보
		
		/**********************************
		 * 페이지 네비게이션 가져오기
		 **********************************/
		loginVO  = (LoginVO)sqlMap.queryForObject("Secure.getUserInfo", searchMap);

		return loginVO;	
	}

	/**
	 * 로그인 상이 입력 정보 셋팅
	 * @param SearchMap, SqlMapClient
	 * @return int  
	 * @throws SQLException  
	 */
	public int updateLoginInfo(SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		Object key = sqlMap.update("Secure.updateLoginInfo", searchMap);

		return 0;
	}
	
	
	/**
	 * 로그인 정보 조회
	 * @param searchMap
	 * @return LoginVO    
	*/
	public int getUserInfoCount(SearchMap searchMap, SqlMapClient sqlMap)  throws SQLException {
		int     intDataCnt = 0;
        intDataCnt = ((Integer)sqlMap.queryForObject("Secure.getUserInfoCount", searchMap)).intValue();
        
        return intDataCnt;
	}
	
	/**
	 * 페이지 네비게이션 가져오기
	 * @param menuId, sqlMap
	 * @return List
	 * @throws SQLException  
	 */
	public ArrayList<MenuVO> getMenuList(String userId, SqlMapClient sqlMap)  throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		ArrayList<MenuVO> menuList = new ArrayList();
		
		ArrayList<MenuVO> menu1List = null;
		ArrayList<MenuVO> menu2List = null;
		ArrayList<MenuVO> menu3List = null;
		
		MenuVO mvo = new MenuVO();
		
		/**********************************
		 * 페이지 네비게이션 가져오기
		 **********************************/
		menu1List  = (ArrayList<MenuVO>)sqlMap.queryForList("Secure.getHighMenuList", userId);
		menu2List  = (ArrayList<MenuVO>)sqlMap.queryForList("Secure.getSubMenuList", userId);
		menu3List  = (ArrayList<MenuVO>)sqlMap.queryForList("Secure.getMenuList", userId);
		
		if(null != menu1List && 0 < menu1List.size()) {
			for(int i = 0; i < menu1List.size(); i++) {
				mvo = menu1List.get(i);
				menuList.add(mvo);
			}
		}
		
		if(null != menu2List && 0 < menu2List.size()) {
			for(int i = 0; i < menu2List.size(); i++) {
				mvo = menu2List.get(i);
				menuList.add(mvo);
			}
		}
		
		if(null != menu3List && 0 < menu3List.size()) {
			for(int i = 0; i < menu3List.size(); i++) {
				mvo = menu3List.get(i);
				menuList.add(mvo);
			}
		}
		 
		return menuList;		
	}
	
	/**
	 * 권한정보 조회
	 * @param String
	 * @return ArrayList    
	*/
	public ArrayList<String> getAuthGrpList(SearchMap searchMap, SqlMapClient sqlMap)  throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		ArrayList<String> authGrpList = null;
		
		/**********************************
		 * 페이지 네비게이션 가져오기
		 **********************************/
		authGrpList  = (ArrayList<String>)sqlMap.queryForList("Secure.authGrpList", searchMap);

		return authGrpList;		
	}
	
	/**
	 * 로그인 로그 등록
	 * @param hashMap, sqlMap
	 * @return int  
	 * @throws SQLException    
	 */
	public int insertLoginLog(HashMap hashMap, SqlMapClient sqlMap) throws SQLException {
		Object key = sqlMap.insert("Secure.insertLoginLog", hashMap);
		return 0;
	}	
	
	/**
	 * 사용자 로그 등록
	 * @param hashMap, sqlMap
	 * @return int  
	 * @throws SQLException    
	 */
	public int insertUserLog(HashMap hashMap, SqlMapClient sqlMap) throws SQLException {
		Object key = sqlMap.insert("Secure.insertUserLog", hashMap);
		return 0;
	}
}

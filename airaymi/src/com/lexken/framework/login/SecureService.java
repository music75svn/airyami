/*************************************************************************
* CLASS 명  	: SecureService
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

import javax.servlet.http.HttpSession;

import com.lexken.framework.common.MyAppSqlConfig;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.util.CalendarHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SecureService {
	// log4j 설정
	private final Log logger = LogFactory.getLog(getClass());
	
	// PersonDao
	//private SecureDao secureDao = SecureDao.getInstance(); //Singleton Pattern 주석처리
	private SecureDao secureDao = new SecureDao();
	
	// Instance
	private static SecureService instance = null;
	public static SecureService getInstance() {
		if (instance == null) {
			instance = new SecureService();
		}
		return instance;
	}
	
	/**
	 * 메뉴권한 여부
	 * @param hashMap
	 * @return boolean    
	*/
	public boolean getMenuGradeFlag(HashMap hashMap) {
		/**********************************
		 * 정의
		 **********************************/
		boolean	gradeChk = false;				// 메뉴 접근 권한
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			gradeChk = secureDao.getMenuGradeFlag(hashMap, sqlMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return gradeChk;		
	}
	
	/**
	 * 페이지 네비게이션 가져오기
	 * @param menuId
	 * @return List  
	*/
	public List getPageNavigator(String menuId) {
		/**********************************
		 * 정의
		 **********************************/
		List navi_list = null;
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();
		
		try {
			
			navi_list = secureDao.getPageNavigator(menuId, sqlMap);
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} 	
		
		return navi_list;		
	}

	/**
	 * 로그인 시도 횟수
	 * @param searchMap
	 * @return LoginVO    
	*/
	public HashMap getTryLoginInfo(SearchMap searchMap) {
		/**********************************
		 * 정의
		 **********************************/
		HashMap tryLoginInfoMap = new HashMap();
		HashMap returnMap 		= new HashMap();
		
		int 	resultValue		= 0;
		String loginState = "reset";
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			
			tryLoginInfoMap = secureDao.getTryLoginInfo(searchMap, sqlMap);
    		if(null != tryLoginInfoMap.get("PW_YN")){
    			if("N".equals(tryLoginInfoMap.get("PW_YN"))){ //입력한 패스워드가 틀렸을경우
	    			// 접속 시도 횟수가 5번 이상일때
					if(5 <= Integer.parseInt(tryLoginInfoMap.get("FINAL_USER_PW_DFFRNC_CNT").toString())){
						loginState	= "over5";
						returnMap.put("ErrorNumber",  ErrorMessages.LOGIN_EXCEED_CODE);
						returnMap.put("ErrorMessage", ErrorMessages.LOGIN_EXCEED_MESSAGE);
					}else{
						loginState	= "under5";
						returnMap.put("ErrorNumber",  ErrorMessages.USER_UNDEFINED_CODE);
						returnMap.put("ErrorMessage", ErrorMessages.USER_UNDEFINED_MESSAGE);
					}
	        	}
    		}else{ //존재하지 않는 아이디
    			return returnMap;
    		}

    		// 트랜잭션 시작
			sqlMap.startTransaction();

			// 처리
			searchMap.put("loginState", loginState);
			//resultValue  = secureDao.updateLoginInfo(searchMap, sqlMap);
			
			// 트랜잭션 완료
			sqlMap.commitTransaction();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            // 트랜잭션 종료
            try {
                sqlMap.endTransaction();
            } catch (SQLException e) {
                logger.error("SQL 트랜잭션 오류 : " + e.toString());
            }
        }	
		
		return returnMap;
	}
    
	/**
	 * 로그인 정보 조회
	 * @param searchMap
	 * @return LoginVO    
	*/
	public LoginVO getUserInfo(SearchMap searchMap) {
		/**********************************
		 * 정의
		 **********************************/
		LoginVO loginVO = null;				// 메뉴 접근 권한
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			loginVO = secureDao.getUserInfo(searchMap, sqlMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return loginVO;		
	}

	
	/**
	 * 로그인 정보 조회
	 * @param searchMap
	 * @return LoginVO    
	*/
	public int getUserInfoCount(SearchMap searchMap) {
		/**********************************
		 * 정의
		 **********************************/
		int 	intDataCnt		= 0;
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			intDataCnt = secureDao.getUserInfoCount(searchMap, sqlMap);
			logger.debug("intDataCnt : " + intDataCnt); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return intDataCnt;		
	}
	
	/**
	 * 권한별 메뉴리스트
	 * @param String
	 * @return ArrayList<MenuVO>    
	*/
	public ArrayList<MenuVO> getMenuList(String userId) {
		/**********************************
		 * 정의
		 **********************************/
		ArrayList<MenuVO> menuList = null;
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			menuList = secureDao.getMenuList(userId, sqlMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return menuList;		
	}
	
	/**
	 * 권한정보 조회
	 * @param String
	 * @return ArrayList    
	*/
	public ArrayList getAuthGrpList(SearchMap searchMap) {
		/**********************************
		 * 정의
		 **********************************/
		ArrayList authGrpList = null;
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			authGrpList = secureDao.getAuthGrpList(searchMap, sqlMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return authGrpList;		
	}
	
	/**
     * 로그 등록 
     * @param HashMap
     * @return HashMap  
     */
    public int insertLoginLog(HashMap hashMap) {
        int     resultValue     = 0;
        
        /**********************************
         * sqlMap생성
         **********************************/
        SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

        /**********************************
         * 등록
         **********************************/
        try {
            
        	CalendarHelper ch = new CalendarHelper();
        	
            // 트랜잭션 시작                  
            sqlMap.startTransaction();
            LoginVO loginVO = (LoginVO)hashMap.get("loginVO");
    		hashMap.put("user_id"	, 		loginVO.getUser_id() 	);
    		hashMap.put("user_nmm"	, 		loginVO.getUser_nm()  	);
    		hashMap.put("client_ip"	,		loginVO.getLogin_ip()	);
    		hashMap.put("jikgub_cd"	, 		loginVO.getJikgub_cd() 	);
    		hashMap.put("jikgub_nm"	, 		loginVO.getJikgub_nm() 	);
    		hashMap.put("pgm_id"	,  		"");
    		hashMap.put("pgm_nm"	,  		"");
    		hashMap.put("url"		,  		"");
            resultValue = secureDao.insertLoginLog(hashMap, sqlMap); 
            // 트랜잭션 완료                  
            sqlMap.commitTransaction();
            
        } catch (SQLException e) {
            logger.error("SQL 트랜잭션 오류 : " + e.toString());
            
            
        } finally {         
            // 트랜잭션 종료              
            try {
                sqlMap.endTransaction();
            } catch (SQLException e) {
                logger.error("SQL 트랜잭션 오류 : " + e.toString());
                
                
            }
        }
        
        return resultValue;       
    }
    
    /**
     * 사용자 로그 등록
     * @param HashMap
     * @return HashMap  
     */
    public int insertUserLog(SearchMap searchMap) {
        int     resultValue     = 0;
        String methodName = (String)searchMap.get("methodName");
        String isPageFirstLoadingYN = (String)searchMap.get("isPageFirstLoadingYN");
        String pgmId	  = (String)searchMap.getString("findSubPgmId");
        HttpSession session =  (HttpSession) searchMap.get("session");
        LoginVO loginVO =  (LoginVO)session.getAttribute("loginVO");
        
        /**********************************
         * sqlMap생성
         **********************************/
        SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

        /**********************************
         * 등록
         **********************************/
        try {
            
        	CalendarHelper ch = new CalendarHelper();
        	
            // 트랜잭션 시작                  
            sqlMap.startTransaction();
            
            //사용자 조회화면 로그
    		if(methodName.endsWith("List") && "Y".equals(isPageFirstLoadingYN)){
    			searchMap.put("user_id"	, 		loginVO.getUser_id() 	);
    			searchMap.put("user_nmm"	, 	loginVO.getUser_nm()  	);
    			searchMap.put("client_ip"	,	loginVO.getLogin_ip()	);
    			searchMap.put("jikgub_cd"	, 	loginVO.getJikgub_cd() 	);
    			searchMap.put("jikgub_nm"	, 	loginVO.getJikgub_nm() 	);
    			searchMap.put("pgm_id"	,  		pgmId);
    			searchMap.put("pgm_nm"	,  		"");
    			resultValue = secureDao.insertUserLog(searchMap, sqlMap); 
    		}
                            
            // 트랜잭션 완료                  
            sqlMap.commitTransaction();
            
        } catch (SQLException e) {
            logger.error("SQL 트랜잭션 오류 : " + e.toString());
            
            
        } finally {         
            // 트랜잭션 종료              
            try {
                sqlMap.endTransaction();
            } catch (SQLException e) {
                logger.error("SQL 트랜잭션 오류 : " + e.toString());
                
                
            }
        }
        
        
        
        
        
        
        return resultValue;       
    }
}

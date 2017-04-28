package egovframework.airyami.cmm.util;


import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthCheck {
	HttpServletRequest m_req = null;
    HttpServletResponse m_res= null;
    
    public static final String MEMBERID = "memberID" ;
    public static final String SEQ 		= "seq" ;
    
    // 수출역량강화사업 추가 세션
    public static final String USER_ID = "USER_ID";
    public static final String USER_NM = "USER_NM";
    public static final String DEPT_ID = "DEPT_ID";
    public static final String DEPT_NM = "DEPT_NM";
    public static final String LOGIN_EMAIL = "USER_EMAIL";
    public static final String LOGIN_TEL_NO = "USER_TEL";
    public static final String USER_GROUP = "USER_GROUP";
    public static final String SITE_ID = "SITE_ID";
    public static final String USER_TYPE = "USER_TYPE";
    
    CommonUtils util = new CommonUtils();  
      
    public AuthCheck() {} 
    
    public AuthCheck (HttpServletRequest req, HttpServletResponse res ) { // for servlet
    	m_req = req;
    	m_res = res;
    }
    
    public void init (HttpServletRequest req, HttpServletResponse res ) { // for jsp
    	m_req = req;
    	m_res = res;
    }
    
    public void putSession ( Properties p ) {
    	HttpSession sess = m_req.getSession( true );
    	String key = null;
    	for (Enumeration e = p.keys() ; e.hasMoreElements() ;) {
    		key = (String)e.nextElement();
    		sess.setAttribute( key  , p.getProperty( key  )) ;
        }
    }
    
    
    public boolean isLogin() {
    	if ( util.isNull ( getMemInfo( USER_ID ) ) ) return false; else return true;
    }
    
    
	public boolean isLogin(HttpServletRequest req , HttpServletResponse res )
		throws Exception {
		String goUrl =  req.getRequestURI() + ( req.getQueryString() != null ? "?"+req.getQueryString() : "");
		String isPop = req.getParameter("pop");
		if( !isLogin() ) {
			if(isPop != null && isPop.equalsIgnoreCase("y")){
				res.getWriter().println("<script>alert('�α����� ����Ͻñ� �ٶ�ϴ�.')</script>");
				return false;
			}
			res.sendRedirect("/member/login.jsp?goUrl=" + goUrl);
			return false;
		}
		return true;
	}

    
	public boolean isLogin(HttpServletRequest req , HttpServletResponse res, String returnUrl, boolean isPop)
		throws Exception {

		String goUrl =  returnUrl;

		if (!isLogin()) {
			if(isPop){
				res.getWriter().println("<script language='javascript'>");
				res.getWriter().println("	window.opener.location='/member/login.jsp?goUrl=" + returnUrl + "';");
				res.getWriter().println("	window.close();");
				res.getWriter().println("</script>");

				return false;
			} else {
				res.sendRedirect("/member/login.jsp?goUrl=" + goUrl);
			}

			return false;
		}

		return true;
	}

    public boolean logOut() {
    	HttpSession session = m_req.getSession( false );
    	session.invalidate();
    	return true;
    }
    
    public String getMemInfo( String s ) {
        HttpSession session = m_req.getSession( false );
        if( session == null || session.getAttribute( s ) == null ){
                return null;
        }
        return (String)session.getAttribute( s );    
    }
    
    public String getSeq() throws Exception {
		String s = util.NVL( getMemInfo( SEQ ) );
		return s;
    }
    
    public String getMemberID() throws Exception {
    	String s = util.NVL( getMemInfo( MEMBERID ) );
    	return s;
    }
    
    public String getUser_id() throws Exception {
    	String s = util.NVL( getMemInfo( USER_ID ) );
    	return s;
    } // end getUser_id
    
    public String getUser_nm() throws Exception {
    	String s = util.NVL( getMemInfo( USER_NM ) );
    	return s;
    } // end getUser_nm
    
    public String getDept_id() throws Exception {
    	String s = util.NVL( getMemInfo( DEPT_ID ) );
    	return s;
    } // end getDept_id
    
    public String getDept_nm() throws Exception {
    	String s = util.NVL( getMemInfo( DEPT_NM ) );
    	return s;
   } // end getDept_nm
    
    
    public String getLogin_email() throws Exception {
    	String s = util.NVL( getMemInfo( LOGIN_EMAIL ) );
    	return s;
	} // end getLogin_email
    
    public String getLoing_tel_no() throws Exception {
    	String s = util.NVL( getMemInfo( LOGIN_TEL_NO ) );
    	return s;
    } // end getLoing_tel_no
    
	public String getLoing_user_group() throws Exception {
    	String s = util.NVL( getMemInfo( USER_GROUP ) );
    	return s;
    } // end
	
	public String getLoing_site_id() throws Exception {
		String s = util.NVL( getMemInfo( SITE_ID ) );
		return s;
	} // end 
	
	public String getUser_type() throws Exception {
		String s = util.NVL( getMemInfo( USER_TYPE ));
		return s;
	} // end getUser_type
	
	public String getUser_Nm() throws Exception {
		String s = util.NVL( getMemInfo( USER_NM ));
		return s;
	} // end getUser_nm

}

package egovframework.airyami.cmm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


public class CommonUtils {
    /**
     * 맵의 각각의 키/값을 JSON 객체로 만들어 출력으로 설정한다.
     * @param map  맵
     */
    public static String setJsonResult(ValueMap map) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("{");
    	if (map != null) {
    		int i = 0;
    		for (Map.Entry<String,Object> entry : map.entrySet()) {
    			String key = entry.getKey();
    			Object value = entry.getValue();
    			if (i > 0) {
    				builder.append("\n,");
    			}
    			builder.append("\"");
    			builder.append(key);
    			builder.append("\":");
    			builder.append(JsonUtil.toString(value));
    			i++;
    		}
    	}
    	builder.append("}");
    	
    	String jsonResult = builder.toString();
    	return jsonResult;
    	
//        InputStream inputStream;
//        
//        try {
//            return inputStream = new ByteArrayInputStream(jsonResult.getBytes("utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }
    
    public static Map<String,Object> getRequestMap(HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }

        @SuppressWarnings("unchecked")
        Enumeration<String> parameter = request.getParameterNames();
        Map<String,Object> result = new HashMap<String,Object>();

        while (parameter.hasMoreElements())
        {
            String name = parameter.nextElement();
//            String value = request.getParameter(name);
            String value = "";
            String[] valueArr = request.getParameterValues(name);
//            System.out.println(" name ::: " + name + " = " + value);
//            if (value != null && !"".equals(value))
            if (valueArr!=null && valueArr.length>0)
            {
            	
            	value = valueArr[0];
            	if("".equals(value))
            		continue;
            	
            	for(int i = 1 ;  i < valueArr.length ; i++){
//	                	System.out.println(" name ::: " + name + " = " + valueArr[i]);
                	value += "," + valueArr[i];
                }
            	
            	if( name.indexOf("_DT") > -1 ){
            		value = value.replaceAll("-", "");
            	}
            	
//            	System.out.println(" name ::: " + name + " = " + value);
            	
               	result.put(name, value);
            }
        }

        
        result.put("LANG_CD", getLocale(request));
        
        
        AuthCheck idCk = new AuthCheck (request, null);
        if (idCk.isLogin()){
        	try {
				result.put("LOGIN_ID", idCk.getUser_id());
				result.put("LOGIN_NM", idCk.getUser_nm());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return result;
    }
    
    public static Map<String,Object> getParamsBydelimiter(String sParams, String sDelimiter)
    {
    	Map<String,Object> result = new HashMap<String,Object>();
    	String[] arrParams = sParams.split(sDelimiter);
    	
    	for(int i = 0 ; i < arrParams.length; i++)
    	{
    		String[] arrParam = arrParams[i].split("=");
    		String name = arrParam[0];
    		String value = arrParam[1];
    		
    		if (value != null && !"".equals(value))
    		{
    			if( name.indexOf("_DT") > -1 ){
    				value = value.replaceAll("-", "");
    			}
    			result.put(name, value);
    		}
    	}
    	
    	return result;
    }
    
    /**
     * 맵의 각각의 키/값을 ModelMap 에 담는다. 
     * @param map  맵
     */
    public static void setModelByParams(ModelMap model, Map<String,Object> params)
    {
    	if ( params.isEmpty() )
    	{
    		return;
    	}
    	
    	// 특수사항..  목록 유알엘을 목원시키기 위한 조치.
    	if( params.containsKey("LISTURL") ){
    		params.put("LISTURL", ((String)params.get("LISTURL")).replaceAll("\\|", "\\&") );
    	}
    	
    	for (String key : params.keySet()) {
            model.put(key, params.get(key));
        }
    }
    
    /**
     * 맵의 각각의 키/값을 ModelMap 에 담는다. 
     * @param map  맵
     */
    public static void setModelByParams(ModelMap model, Map<String,Object> params, HttpServletRequest request)
    {	
    	setModelByParams(model, params);
    	
    	model.put("MYPATH", UrlUtil.getMenuID(request));
    	model.put("REFPATH", UrlUtil.getRefPath(request));
    }
    
    /**
     * 체크박스에 선택된 코드를 맵에 넣어준다. 
     * @param map  맵
     */
    public static void setCheckboxParams(String checkCD, String paramNM, Map<String,Object> params)
    {
    	if ( params.isEmpty() )
    	{
    		return;
    	}
    	
    	List<String> strORGANS = new ArrayList<String>();
    	for (String key : params.keySet()) {
    		if(key.indexOf(checkCD) > -1){
    			//예외상황.. ㅠㅠ
    			if("CKPM".equals(checkCD)){
    				strORGANS.add( ((String)params.get(key)).substring(1));
    				continue;
    			}
    			
    			strORGANS.add((String)params.get(key));
    			
    		}
        }
    	
    	Collections.sort(strORGANS);
    	
    	params.put(paramNM, strORGANS);
    }
    
    /**
     * Json 배열문자를 map에 담아준다. 
     * @param map  맵
     */
    public static void setArrayParams(String paramNM, Map<String,Object> params, String psExcept)
    {
    	if ( params.isEmpty() )
    	{
    		return;
    	}
    	
    	List<String> strParams = new ArrayList<String>();
    	String strTemp = (String)params.get(paramNM);
    	
    	String[] arrTmp = strTemp.split( "," );
    	
    	for( int i = 0; i < arrTmp.length; i++) {
    		if(psExcept.indexOf(arrTmp[i]) > -1)
    			continue;
    		strParams.add(arrTmp[i]);
    	}
    	
    	params.put(paramNM, strParams);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// null 관련 함수 ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
    * 문자열을 받아서 null이면 false로 리턴
    *
    * @param str
    * @return boolean
    */
    public static final boolean isNull(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals("null"))
        return true;
    else
        return false;
    }

    /**
     * 문자열이 널이면 공백 문자열로 리턴
     *
     * @param str
     * @return String
     */
    public static final String NVL(String str) {
        if (isNull(str))
            return "";
        else
            return str;
    }

    /**
     * 문자열이 널이면 대체할 문자열을 리턴
     *
     * @param str
     * @param NVLString
     * @return String
     */
    public static final String NVL(String str, String NVLString) {
        if (isNull(str))
            return NVLString;
        else
            return str;
    }

    /**
     * 입력 char가 숫자형인지 아닌지를 리턴. "0"~"9"이면 true, 아니면 false.
     *
     * @param c 문자(Char)
     * @return "0"~"9"이면 true, 아니면 false
     */
    public static final boolean isDigitChar(char c)
    {
        char x = '0';
        char y = '9';
        return c >= x && c <= y;
    }

    /**
     * 숫자인지 체크
     * 2006/01/12 최범석
     * @return boolean
     */
    public static final boolean isNumeric(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!isDigitChar(str.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * 날짜에 하이픈등 기호 넣기
     * 2006/04/18 하윤철
     * @param inStr 날짜 문자열
     * @param delim 날짜 사이에 들어갈 문자
     * @return String
     */
    public static final String dateFormat(String inStr, char delim) {
        StringBuffer sb = new StringBuffer("");
        String str   = null;

        if (inStr == null) return "";
        str   = inStr.trim();

        if (str.length() != 8) return str;
        if (!isNumeric(str)) return str;

        if (str.length() == 8) {        //날짜
            sb.append(str.substring(0, 4));
            sb.append(delim);
            sb.append(str.substring(4, 6));
            sb.append(delim);
            sb.append(str.substring(6, 8));
        }

        return sb.toString();
    }
    
    /**
     * 사업자번호에 하이픈 넣기
     * @param inStr 사업자번호
     * @param delim 사이에 들어갈 문자
     * @return String
     */
    public static final String bizNoFormat(String inStr, char delim) {
        StringBuffer sb = new StringBuffer("");
        String str   = null;
        
        if (inStr == null) return "";
        str   = inStr.trim();
        
        if (str.length() != 10) return str;
        if (!isNumeric(str)) return str;
        
        if (str.length() == 10) {        //사업자번호
            sb.append(str.substring(0, 3));
            sb.append(delim);
            sb.append(str.substring(3, 5));
            sb.append(delim);
            sb.append(str.substring(5, 10));
        }
        
        return sb.toString();
    }

    /**
     * 법인번호에 하이픈 넣기
     * @param inStr 사업자번호
     * @param delim 사이에 들어갈 문자
     * @return String
     */
    public static final String corpNoFormat(String inStr, char delim) {
        StringBuffer sb = new StringBuffer("");
        String str   = null;
        
        if (inStr == null) return "";
        str   = inStr.trim();
        
        if (str.length() <= 6) return str;
        if (!isNumeric(str)) return str;
        
        if (str.length() > 6) {        //사업자번호
            sb.append(str.substring(0, 6));
            sb.append(delim);
            sb.append(str.substring( 6 ));
        }
        
        return sb.toString();
    }
    
    /**
     * 전화번호에 하이픈등 기호 넣기
     * 2006/01/12 최범석
     * @param inStr 전화번호 문자열
     * @param delim 전화번호 사이에 들어갈 문자
     * @return String
     */
    public static final String telFormat(String inStr, char delim) {
        StringBuffer sb = new StringBuffer("");
        String str   = null;

        if (inStr == null) return "";
        str   = inStr.trim();

        if (str.length() < 9) return str;
        if (!isNumeric(str)) return str;

        if (str.substring(0,2).equals("02")) {    //지역번호  2자리
            if (str.length() == 9) {
                sb.append(str.substring(0, 2));
                sb.append(delim);
                sb.append(str.substring(2, 5));
                sb.append(delim);
                sb.append(str.substring(5, 9));
            } else if (str.length() == 10) {
                sb.append(str.substring(0, 2));
                sb.append(delim);
                sb.append(str.substring(2, 6));
                sb.append(delim);
                sb.append(str.substring(6, 10));
            } else {
                sb.append(str);
            }
        }
        else // 이통사 및 지역번호 3자리
        {
            if (str.length() == 10) {
                sb.append(str.substring(0, 3));
                sb.append(delim);
                sb.append(str.substring(3, 6));
                sb.append(delim);
                sb.append(str.substring(6, 10));
            } else if (str.length() == 11) {
                sb.append(str.substring(0, 3));
                sb.append(delim);
                sb.append(str.substring(3, 7));
                sb.append(delim);
                sb.append(str.substring(7, 11));
            } else { // 식별번호가 없는것
                sb.append(str);
            }
        }
        return sb.toString();
    }
    
    public static final String amtFormat( String strValue ) {
        try
        {
            String format = "#,##0.##";
            DecimalFormat df = new DecimalFormat(format);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            
            dfs.setGroupingSeparator( ',' );
            df.setGroupingSize( 3 );
            df.setDecimalFormatSymbols( dfs );
            
            return (df.format( Double.parseDouble( strValue ) )).toString();            
        }
        catch ( Exception e )
        {
            return "";
        }

    }
    
    
    public static String getTimeStamp() {
        String rtnStr = null;

        String pattern = "yyyyMMddhhmmssSSS";

        try {
            SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
            Timestamp ts = new Timestamp(System.currentTimeMillis());

            rtnStr = sdfCurrent.format(ts.getTime());
        } catch (Exception e) {
            //e.printStackTrace();
            
            //throw new RuntimeException(e);    // 蹂댁븞�먭� �꾩냽議곗튂
        }

        return rtnStr;
    }
    
    public static String getToday(String pattern) {
    	String rtnStr = null;
    	
    	//String pattern = "yyyyMMddhhmmssSSS";
    	
    	try {
    		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
    		Timestamp ts = new Timestamp(System.currentTimeMillis());
    		
    		rtnStr = sdfCurrent.format(ts.getTime());
    	} catch (Exception e) {
    		//e.printStackTrace();
    		
    		//throw new RuntimeException(e);    // 蹂댁븞�먭� �꾩냽議곗튂
    	}
    	
    	return rtnStr;
    }

    public static Date stringToDate(String dateString, String format)
	{
		SimpleDateFormat transFormat = new SimpleDateFormat(format);
		
		Date toDate;
		try {
			toDate = transFormat.parse(dateString);
		} catch (ParseException e) {
			toDate = null;
//			e.printStackTrace();
		}
		
		return toDate;
	} 
    
	public static String dateToString(Date date, String format)
	{
		SimpleDateFormat transFormat = new SimpleDateFormat(format);
		
		String toDate = transFormat.format(date);
		
		return toDate;
	}
	
	
	/**
	  * 랜덤문자 생성 
	  */
	 public static String randomChar(int len) {
		 char[] charSet = new char[]{'0','1','2','3','4','5','6','7','8','9'};
	    
		 int idx = 0;
		 StringBuffer sb = new StringBuffer();
		 for(int i=0; i<len; i++){
			 idx = (int)(charSet.length*Math.random());
			 sb.append(charSet[idx]);
		 }
	    
		 return sb.toString();
	 }
	 
	 /**
	  * 숫자+대문자 랜덤 비밀번호 생성
	  * 2015/09/18 이동훈
	  * @param 비밀번호 길이
	  * @return 숫자+대문자 랜덤스트링
	  */
	 public static String randPass(int len){
		 int i  = 0;
		 StringBuffer str = new StringBuffer();
		 int bool = 0;
		 for(i = 0 ; i < len ; i++) {
			 bool = (int) (Math.random() * 2);
			 if(bool == 0){
				 str.append( (char) ((Math.random() * 10) + 48));
			 }
			 else{
				 str.append( (char) ((Math.random() * 26) + 65));
			 }
		 }
		 return str.toString();
	 }
	 
	 /**
	  * 클라이언트 ip 주소 읽어오기 
	  */
	 public static String getClientIP(HttpServletRequest request) {
		 

	     String ip = request.getHeader("X-FORWARDED-FOR"); 
	     
	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("Proxy-Client-IP");
	     }

	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
	     }

	     if (ip == null || ip.length() == 0) {
	         ip = request.getRemoteAddr() ;
	     }
	     
	     return ip;

	 }
	 
	 /**
	  * 디데이 계산하기
	  * */
	 public static int getDday(int dDayYear, int dDayMonth, int dDayDate) {
	 	Calendar today = Calendar.getInstance();
		Calendar dday = Calendar.getInstance();
		
		dday.set(dDayYear, dDayMonth-1, dDayDate);
		
		long day = dday.getTimeInMillis()/86400000; //86400000: 1일*24시간*60분*60초*1000
		
		long tday = today.getTimeInMillis()/86400000;
		long count = day - tday;
		
		int dDayCount = (int) count;
		
		
		return dDayCount;
	 }
	 
	 /**
	  * 디데이 계산하기
	  * */
	 public static Locale getLocale(HttpServletRequest request) {
		 
//		 return Locale.getDefault()+"";
		 HttpSession session = request.getSession();
		 if( isNull(session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME)+"") )
			 return Locale.getDefault();
					 
		 return (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
	 }
	 
	 /**
	  * Locale Null여부 확인
	  * */
	 public static boolean getLocaleNull(HttpServletRequest request) {		 
		 HttpSession session = request.getSession();
		 boolean reValue =false;
		 if( isNull(session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME)+"") )
			 reValue = true;
		 return reValue;					 
	 }	 
	 
	 /**
	  * SHA256 처리(단방향 암호화, 비밀번호에 사용)
	  * */
	 public static String getSHA256(final String input){

			if(input.equals("") || input == null) return "";

			String SHA = "";
			try{
				MessageDigest sh = MessageDigest.getInstance("SHA-256");
				sh.update(input.getBytes());
				byte byteData[] = sh.digest();
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < byteData.length ; i++){
					sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
				}
				SHA = sb.toString();

			}catch(NoSuchAlgorithmException e){
				e.printStackTrace();
				SHA = null;
			}
			return SHA;
		}
	 
	 
	 /**
	  * 정규식 validation 체크
	  * */
	 public static ValueMap checkRegular(String sData, String sRegular) {
		ValueMap result = new ValueMap();
		boolean success = true;
		result.put("PASS_YN", success);
		 
		Pattern p = null;
		Matcher m = null;
		String exp = "";
		String msg = "";
		 
		// 숫자만
		if("NUM".equals(sRegular)){
			exp = "^[0-9]+$";
			msg = " 숫자만 입력하세요";
		}
		 
		// 숫자+영문
		if("NUMENG".equals(sRegular)){
			exp = "^[a-zA-Z0-9]+$";
			msg = " 숫자+영문만 입력하세요";
		}
		 
		// 한글,띄어쓰기만 가능
		if("KOR".equals(sRegular)){
			exp = "^[가-힣\\s]+$";
			msg = " 한글만 입력하세요";
		}
			
		// 영문,띄어쓰기만 가능
		if("ENG".equals(sRegular)){
			exp = "^[a-zA-Z\\s]+$";
			msg = " 영문만 입력하세요";
		}
		// 전화번호
		if("TEL".equals(sRegular)){
			exp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$";
			msg = " 전화번호형식이 아닙니다.";
		}
		// 이메일
		if("EMAIL".equals(sRegular)){
			exp = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";
			msg = " 유효한 이메일 주소가 아닙니다.";
		}
		// 도메인
		if("DOMAIN".equals(sRegular)){
			exp = "^(((http(s?))\\:\\/\\/)?)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$";
			msg = " 유효한 도메인 주소가 아닙니다.";
		}
		 
		 
		 if(isNull(exp) || isNull(sData)){
			 return result;
		 }
		 
		 p = Pattern.compile(exp);
		 m = p.matcher(sData);
		 
		 success = m.matches();
		 
		 if(!success){
			 result.put("ERRMSG", msg);
		 }
		 
		 result.put("PASS_YN", m.matches());
		 return result;
	 }
	 
	/**
	 * 로그인 여부 체크 및 user_group 허용이 있을경우 방아서 처리
	 * 
	 */
	public static boolean checkAuth(HttpServletRequest request) {		 
		HttpSession session = request.getSession();
		boolean reValue =false;
		
		AuthCheck idCk = new AuthCheck (request, null);
		// 로그인 상태만 체크한다.
		if (idCk.isLogin()){
			reValue = true;
		}
		
		return reValue;
	        
	}
	
	/**
	 * 로그인 여부 체크 및 user_group 허용이 있을경우 방아서 처리
	 * 
	 */
	public static boolean checkAuth(HttpServletRequest request, Map<String,Object> params) {		 
		HttpSession session = request.getSession();
		boolean reValue =false;
		
		AuthCheck idCk = new AuthCheck (request, null);
		// 로그인 상태만 체크한다.
		if (idCk.isLogin()){
			// USER_GROUP 체크가 필요한경우 체크한다.
			if(params.containsKey("USER_GROUP")){
				try {
					String myUserGroup = idCk.getLoing_user_group();
					String allowUserGroup = (String)params.get("USER_GROUP");
					
					if( !allowUserGroup.contains(myUserGroup) ){
						return reValue;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
					
			reValue = true;
		}
		
		return reValue;
		
	}
	
	
}


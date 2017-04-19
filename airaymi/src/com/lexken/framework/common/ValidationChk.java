/**
 * *************************************************************************
 *  CLASS 명  : ValidationChk
 *  작 업 자  : 박소라
 *  작 업 일  : 2009.04.21
 *  기    능  : Validation 체크(데이터 무결성 체크)
 *  ---------------------------- 변 경 이 력 --------------------------------
 *  번호  작 업 자   작     업     일        변 경 내 용              비고
 *  ----  --------  -----------------  -------------------------    --------
 *    1    박소라		 2009.04.21		   최 초 작 업 
 *    2    최은정		 2010.08.03		   사업자 번호 유효성검사 추가
 * ************************************************************************* 
 */
package com.lexken.framework.common;

import gnu.regexp.RE;

import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ValidationChk {
    
	private static RE mailWithName 	= null;
	private static RE mailOnly 		= null;
	private static RE regtNoChk1 	= null;
	private static RE regtNoChk2 	= null;
	private static RE regtNoChk3 	= null;
	private static RE regtNoChk4 	= null;
	private static RE regtNoChk5 	= null;
	private static RE regtNoChk6 	= null;
	private static RE regtNoChk7 	= null;
	private static RE regtNoChk8 	= null;
	private static RE regtNoChk9 	= null;
	private static RE regtNoChk10 	= null;
	private static RE regtNoChk11 	= null;
	private static RE regtNoChk12 	= null;
	private static RE regtNoChk13 	= null;
    
	// Logger
	private static final Log logger = LogFactory.getLog(ValidationChk.class);
	

	/**	 
	 * 문자열의 최저, 최대 길이를 체크하여 결과를 리턴
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @param lowLength: 최소입력자리수
	 * @param highLength: 최대입력자리수
	 * @return HashMap  
	*/
    public static HashMap lengthCheck(String objValue, String strName, int lowLength, int highLength) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments1      = {strName, lowLength};
    	Object[] arguments2      = {strName, lowLength, highLength};
    	
    	int nsize = 0;
		nsize = getLength(objValue);
    	
		if ( lowLength > 0 && nsize == 0 ) {
			resultValue = ErrorMessages.FAILURE_CHK_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_MESSAGE, strName);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}

		if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH_MESSAGE, arguments1);
			} else {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH2_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH2_MESSAGE, arguments2);
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
	
	/**	 
	 * 숫자만 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap onlyNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
		String  chars           = "0123456789"; 

		for (int inx = 0; inx < tmp.length(); inx++) {
			if (chars.indexOf(tmp.charAt(inx)) == -1) {
			    resultValue = ErrorMessages.FAILURE_ONLY_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_NUM_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
		    	return returnMap;
			}	   
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
	/**	 
	 * 숫자/, 허용
	 * @param objValue : 개체 값
	 * @param num      : 자리수
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkCommaNumber(String objValue, int num, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments      = {strName, num};
    	
    	String  chars = "0123456789,";

		for (int inx = 0; inx < objValue.length(); inx++) {
			if (chars.indexOf(objValue.charAt(inx)) == -1) {
			    resultValue = ErrorMessages.FAILURE_CHK_COMMA_NUMBER_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_COMMA_NUMBER_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
		    	return returnMap;
			}	   
		}

		if ( num > 0 ) {
			if ( objValue.length() != 0 && objValue.length() > num ) {
				resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE, arguments);
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
    /**	 
	 * 숫자/. 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap onlyNumberPoint(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
		String  chars           = ".0123456789"; 

		for (int inx = 0; inx < tmp.length(); inx++) {
			if (chars.indexOf(tmp.charAt(inx)) == -1) {
			    resultValue = ErrorMessages.FAILURE_ONLY_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_NUM_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
		    	return returnMap;
			}	   
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
    /**	 
	 * 숫자/,/. 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkCommaPointNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
		String  chars           = ".,0123456789"; 

		for (int inx = 0; inx < tmp.length(); inx++) {
			if (chars.indexOf(tmp.charAt(inx)) == -1) {
			    resultValue = ErrorMessages.FAILURE_ONLY_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_NUM_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
		    	return returnMap;
			}	   
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
	/**	 
	 * 영문/숫자 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap onlyEng1(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	int nsize = 0;
		nsize = getLength(objValue);    	

    	for (int i = 0; i < nsize; i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z')
				continue;
			else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z')
				continue;
			else if (tmp.charAt(i) == '_' || tmp.charAt(i) == '-')
				continue;
			else if (tmp.charAt(i) == ' ' || tmp.charAt(i) == '.')
				continue;
			else {
				resultValue = ErrorMessages.FAILURE_ONLY_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_ENG_MESSAGE, strName);
				break;
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 공백 존재여부 확인
	 * @param strName  : 경고개체명
	 * @param objValue : 개체 값
	 * @param strContent: 콘텐츠
	 * @return HashMap  
	*/
    public static HashMap check_TextBlank(String strName, String objValue, String strContent) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments      = {strName, strContent};
    	
    	
		if ("".equals(objValue.trim())) {
			resultValue = ErrorMessages.FAILURE_CHK_EMPTY_BLANK_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_BLANK_MESSAGE, arguments);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 전화번호 숫자/- 허용
	 * @param objValue : 개체 값
	 * @param num      : 자리수
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkPhoneNumber(String objValue, int num, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments      = {strName, num};
    	
    	String  chars = "0123456789-";

		for (int inx = 0; inx < objValue.length(); inx++) {
			if (chars.indexOf(objValue.charAt(inx)) == -1) {
			    resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
		    	return returnMap;
			}	   
		}

		if ( objValue.length() != 0 && objValue.length() > num ) {
			resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE, arguments);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * Email Check
	 * @param objValue : 개체 값
	 * @return HashMap  
	*/
    public static HashMap emailCheck(String objValue) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	
    	if(objValue == null || "".equals(objValue)){
    		returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
			return returnMap;
    	}
    	
    	try{
			mailWithName = new RE(
			"([^<^>^@]*)<([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)>");
			mailOnly = new RE(
			"([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)");
			
			if (!(mailWithName.isMatch(objValue) || mailOnly
					.isMatch(objValue))) {
				resultValue = ErrorMessages.FAILURE_CHK_EMAIL_CODE;
				resultMsg   = ErrorMessages.FAILURE_CHK_EMAIL_MESSAGE;
			}
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
    	}catch(Exception e){
    		e.printStackTrace();
    		returnMap.put("ErrorNumber",  ErrorMessages.FAILURE_CHK_EMAIL_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_CHK_EMAIL_MESSAGE);
    	}
    	
    	return returnMap;	        
    }
    
	/**	 
	 * 숫자로만 된 스트링 허용 안함
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap notOnlyNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
		String  chars           = "0123456789";
    	int     icount          = 0;

		for (int inx = 0; inx < tmp.length(); inx++) {
			if (chars.indexOf(tmp.charAt(inx)) < 0) {
				icount++;
			}	   
		}

		if (icount < 1){
			resultValue = ErrorMessages.FAILURE_NOT_ONLY_NUM_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_NOT_ONLY_NUM_MESSAGE, strName);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 첫글자를 숫자로만 된 스트링 허용 안함
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap firstnotOnlyNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue.substring(0, 1);
    	String  chars           = "0123456789";
    	
		if (chars.indexOf(tmp.charAt(0)) != -1) {
			resultValue = ErrorMessages.FAILURE_FIRST_NOT_ONLY_NUM_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_FIRST_NOT_ONLY_NUM_MESSAGE, strName);
		}		
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }    

	/**	 
	 * 첫글자를 영문으로만 된 스트링 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap firstOnlyEng(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	boolean returnBool      = false;
    	
		if (tmp.charAt(0) >= 'a' && tmp.charAt(0) <= 'z')
			returnBool      = true;
		else if (tmp.charAt(0) >= 'A' && tmp.charAt(0) <= 'Z')
			returnBool      = true;
		else {
			resultValue = ErrorMessages.FAILURE_CHK_FIRST_ENG_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_FIRST_ENG_MESSAGE, strName);
			returnBool      = false;
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 입력여부 체크하기
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap emptyCheck(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	
    	
		if ("".equals(objValue.trim())) {
			resultValue = ErrorMessages.FAILURE_CHK_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_MESSAGE, strName);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    /**	 
     * 징수금액 체크
     * @param objValue : 개체 값
     * @param strName  : 경고개체명
     * @return HashMap  
     */
    public static HashMap collectAmtEmptyCheck(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	
    	
    	if ("".equals(objValue.trim())) {
    		resultValue = ErrorMessages.FAILURE_CHK_COLLECT_OVER_CODE;
    		resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_COLLECT_OVER_MESSAGE, strName);
    	}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
    	returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    /**	 
     * 두값의 크기 비교(징수누계액 > 사용 누계)
     * @param objValue : 개체 값
     * @param strName  : 경고개체명
     * @return HashMap  
     */
    public static HashMap amtCheck(String amtA, String amtB,String amtBName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	int amtANum = 0;
    	int amtBANum = 0;
    	
    	amtANum = Integer.parseInt(amtA);
    	amtBANum = Integer.parseInt(amtB);
    	
    	if (amtANum > amtBANum) {
    		resultValue = ErrorMessages.FAILURE_CHK_OVER_CODE;
    		resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_OVER_MESSAGE, amtBName);
    	}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
    	returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 선택여부 체크하기
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap selEmptyCheck(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	
		if ("".equals(objValue.trim())) {
			resultValue = ErrorMessages.FAILURE_CHK_SEL_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_SEL_EMPTY_MESSAGE, strName);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
	/**	 
	 * 영문 대문자, '_'(언더바)만 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
    	*/
    public static HashMap onlyUpperEngUnderbar(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z')
				continue;
			else if (tmp.charAt(i) == '_')
				continue;
			else {
				resultValue = ErrorMessages.FAILURE_ONLY_UPP_ENG_UNDERBAR_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_UPP_ENG_UNDERBAR_MESSAGE, strName);
				break;
			}
		}		
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
	/**	 
	 * 영문/숫자만 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap onlyEngNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z')
				continue;
			else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z')
				continue;
			else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9')
				continue;
			else {
				resultValue = ErrorMessages.FAILURE_ONLY_ENG_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_ENG_NUM_MESSAGE, strName);
				break;
			}
		}		
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	/**	 
	 * 영문소문자/숫자만 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap onlyLowEngNumber(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
		
		for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z')
				continue;
			else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9')
				continue;
			else {
				resultValue = ErrorMessages.FAILURE_ONLY_LOW_ENG_NUM_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_ONLY_LOW_ENG_NUM_MESSAGE, strName);
				break;
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 영문 반드시 포함하기 
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap ifChrChk(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	int     icount          = 0;
    	
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				icount++;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				icount++;
			}
		}
    	
    	if (icount < 1) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_CHR_MESSAGE, strName);
    	}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 한글, 특수문자 제한하기 
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap ifKoChrChk(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	int     icount          = 0;
    	
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				continue;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				continue;
			} else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9'){
				continue;
			} else if (tmp.charAt(i) == '_' || tmp.charAt(i) == '-' || tmp.charAt(i) == '.'){
				continue;
			} else {
				icount++;
			}
		}
    	
    	if (icount > 0) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_KO_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_KO_CHR_MESSAGE, strName);
    	}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 회원가입시 아이디, 비밀번호 입력 제한 
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap userJoinInputChk(String objValue, String strName, int lowLength, int highLength) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	int     icount          = 0;
    	int     nsize           = tmp.length();
    	Object[] arguments1     = {strName, lowLength};
    	Object[] arguments2     = {strName, lowLength, highLength};
    	
    	if ( lowLength > 0 && nsize == 0 ) {
    		resultValue = ErrorMessages.FAILURE_CHK_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_MESSAGE, strName);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
    	}   	
    	
    	for (int i = 0; i < nsize; i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				continue;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				continue;
			} else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9'){
				continue;
			} else if (tmp.charAt(i) == '_'){
				continue;
			} else {
				icount++;
			}
		}
    	
    	if (icount > 0) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_KO_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_KO_CHR_MESSAGE, strName);
    	}
    	
    	if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH_MESSAGE, arguments1);
			} else {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH2_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH2_MESSAGE, arguments2);
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 회원가입시 아이디, 비밀번호 입력 제한 
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap userPwChk(String objValue, String strName, int lowLength, int highLength) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	int     icount          = 0;
    	int     jcount          = 0;
    	int     kcount          = 0;
    	int     lcount          = 0;
    	int     nsize           = tmp.length();
    	Object[] arguments1     = {strName, lowLength};
    	Object[] arguments2     = {strName, lowLength, highLength};
    	
    	if ( lowLength > 0 && nsize == 0 ) {
    		resultValue = ErrorMessages.FAILURE_CHK_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_MESSAGE, strName);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
    	}   	
    	
    	for (int i = 0; i < nsize; i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				jcount = 1;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				jcount = 1;
			} else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9'){
				kcount = 1;
			} else if ((tmp.charAt(i) == '~') || (tmp.charAt(i) == '!') || (tmp.charAt(i) == '@') || (tmp.charAt(i) == '#') || (tmp.charAt(i) == '$') || (tmp.charAt(i) == '%') || (tmp.charAt(i) == '^') || (tmp.charAt(i) == '&') || (tmp.charAt(i) == '*') || (tmp.charAt(i) == '(') || (tmp.charAt(i) == ')')){
				lcount = 1;
			} else if ((tmp.charAt(i) == '_') || (tmp.charAt(i) == '{') || (tmp.charAt(i) == '}') || (tmp.charAt(i) == '[') || (tmp.charAt(i) == ']')){
				lcount = 1;
			} else {
				icount++;
			}
		}
    	
    	if (icount > 0) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_KO_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_KO_CHR_MESSAGE, strName);
    	}
    	
    	if (jcount < 1) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_CHR_MESSAGE, strName);
    	}
    	
    	if (kcount < 1) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_NUM_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_NUM_MESSAGE, strName);
    	}
    	
    	if (lcount < 1) {
    		resultValue = ErrorMessages.FAILURE_CHK_IF_SPC_CHR_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_IF_SPC_CHR_MESSAGE, strName);
    	}
    	
    	if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH_MESSAGE, arguments1);
			} else {
				resultValue = ErrorMessages.FAILURE_CHK_LENGTH2_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH2_MESSAGE, arguments2);
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 전화번호 숫자/- 허용 
	 * @param objValue : 개체 값
	 * @param num      : 자리수
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkMenuId(String objValue, int num, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	Object[] arguments      = {strName, num};
    	
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				continue;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				continue;
			} else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9'){
				continue;
			} else if (tmp.charAt(i) == '-'){
				continue;
			} else {
				resultValue = ErrorMessages.FAILURE_CHK_MENU_ID_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_MENU_ID_MESSAGE, strName);
				
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);
				
				return returnMap;	 
			}
		}
    	
    	if ( objValue.length() != 0 && objValue.length() > num ) {
			resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE, arguments);
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }    

    
	/**	 
	 * 한글, 영문, 숫자 허용
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap 
	*/
    public static HashMap checkMenuName(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  tmp             = objValue;
    	
    	for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z'){
				continue;
			} else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z'){
				continue;
			} else if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9'){
				continue;
			} else if (tmp.charAt(i) >= '가' && tmp.charAt(i) <= '힝'){
				continue;
			} else if (tmp.charAt(i) == '&' || tmp.charAt(i) == '/' || tmp.charAt(i) == '\'' || tmp.charAt(i) == '-' || tmp.charAt(i) == ' '){
				continue;
			} else if (tmp.charAt(i) == '(' || tmp.charAt(i) == ')'){
				continue;
			} else {
				resultValue = ErrorMessages.FAILURE_CHK_MENU_NAME_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_MENU_NAME_MESSAGE, strName);
				
				break;	 
			}
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 연속된 문자/숫자 체크
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap continutyCheck(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	String  str             = objValue;
    	String  temp 	        = "";
    	String  temp2 	        = objValue.substring(0,1);	//문자열 첫째 자리 값을 할당합니다.
    	int     con 	        = 1; 					//연속3번이 되어야만 con값이 3이 될 수 있습니다.
		boolean chk		        = true;
		
		for(int i = 1; i < str.length(); i++){
			temp = str.substring(i,i+1);//입력된 값을 하나하나 새로담는다
			
			if(temp.equals(temp2)){
				con++; //temp2은 그대로고...con값만 증가
			}else{
				con = 1; //연속된 값이 아니었으므로 다시 첨부터 1
				temp2 = temp;  //temp2 또한 연속된 값이 아니었으므로 다시 temp의 값을 하나 받아서 시작
			}
			
			if(con == 3){ //연속 3번
				resultValue = ErrorMessages.FAILURE_CHK_CONTINUTY_CODE;
				resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_CONTINUTY_MESSAGE, strName);
				
				break; 
			}
		}
    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 유효한 날짜인지 체크
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap 
	*/
    public static int checkDateValidation(String objValue) {
    	int     err 	        = 0;
		
		if(objValue.length() != 8 ){	   
			err = 1;
	    	return err;
		}		

		int     r_year 	        = Integer.parseInt(objValue.substring(0,4));
		int     r_month         = Integer.parseInt(objValue.substring(4,6));
		int     r_day 	        = Integer.parseInt(objValue.substring(6,8));
		
		if (r_month<1 || r_month>12) err = 1;
		if (r_day<1 || r_day>31) err = 1;
		if (r_year<0 ) err = 1;
		
		if (r_month==4 || r_month==6 || r_month==9 || r_month==11){
			if (r_day==31) err=1;
		}

		// 2,윤년체크
		if (r_month==2){
			if (r_day>29) err=1;
			if (r_day==29 && !StaticUtil.checkYunYear(r_year)) err=1;			
		}
    	
    	return err;	        
    }

    
	/**	 
	 * 유효한 날짜인지 체크
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap 
	*/
    public static HashMap checkDate(String objValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
		
    	//숫자 여부 판별
    	returnMap = onlyNumber(objValue, strName);
    	if( (Integer)returnMap.get("ErrorNumber") < 0 ){
			return returnMap;
		}
    	
    	//날짜 체크
		if (checkDateValidation(objValue) > 0){
			resultValue = ErrorMessages.FAILURE_CHK_DATE_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_DATE_MESSAGE, strName);
		}
    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 날짜 기간 비교
	 * @param strValue : 시작일
	 * @param endValue : 종료일
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkPeriodDate(String strValue, String endValue, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	CalendarHelper ch = new CalendarHelper();
		
		if (ch.compareDateFull(strValue, endValue) < 0){
			resultValue = ErrorMessages.FAILURE_CHK_PERIOD_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PERIOD_MESSAGE, strName);
		}
    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 필수입력 자리수 체크
	 * @param objValue : 개체 값
	 * @param num      : 필수입력 자리수
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap essentialityLength(String objValue, int num, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments      = {strName, num};
		
    	int nsize = 0;
		nsize = getLength(objValue);
		
		if (nsize > num){
			resultValue = ErrorMessages.FAILURE_CHK_LENGTH3_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH3_MESSAGE, arguments);
		}
		    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 필수입력 자리수 체크(선택)
	 * @param objValue : 개체 값
	 * @param num      : 필수입력 자리수
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap selEssentialityLength(String objValue, int num, String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	Object[] arguments      = {strName, num};
		
		if (objValue.length() < num){
			resultValue = ErrorMessages.FAILURE_CHK_SEL_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_SEL_EMPTY_MESSAGE, arguments);
		}
		    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 첨부가능한 파일 형식 체크(선택)
	 * @param objValue : 개체 값
	 * @param fileType : 첨부가능한 파일 형식
	 * @return HashMap  
	*/
    public static HashMap chkAttachFileType(String objValue, String fileType) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	int     icount          = 0;
    	String[] fileTypes  = fileType.split(",");
    	String[] attachFile = objValue.split("\\.");
		String attachExte   = attachFile[attachFile.length-1].toLowerCase();
		
		for( int i=0; i<fileTypes.length; i++){
			if( attachExte.equalsIgnoreCase(fileTypes[i]) ){
				icount++;
			}
		}
    	
		if( icount > 1 ){
			resultValue = ErrorMessages.FAILURE_CHK_ATTACH_FILE_TYPE_CODE;
			resultMsg   = ErrorMessages.FAILURE_CHK_ATTACH_FILE_TYPE_MESSAGE;			
		}
		    	    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

    
	/**	 
	 * 첨부가능한 파일 크기 체크(선택)
	 * @param objValue : 개체 값
	 * @param maxSize  : 첨부가능한 파일 크기
	 * @return HashMap  
	*/
    public static HashMap chkAttachFileSize(int objValue, String maxSize) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
		int     maxSizeTmp      = 0;      		

		String unit = maxSize.substring(maxSize.length()-2);  	
		int size = Integer.parseInt(maxSize.substring(0, maxSize.length()-2)); 
		
		if( "KB".equalsIgnoreCase(unit) ){
			maxSizeTmp = size * 1024;
		}else if( "MB".equalsIgnoreCase(unit) ){
			maxSizeTmp = size * 1024 * 1024;
		}else if( "GB".equalsIgnoreCase(unit) ){
			maxSizeTmp = size * 1024 * 1024 * 1024;
		}		
	 	
		if( objValue > maxSizeTmp ){
			resultValue = ErrorMessages.FAILURE_CHK_ATTACH_FILE_SIZE_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_ATTACH_FILE_SIZE_MESSAGE, maxSize);
		}		
		
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    

	
	/**
	 * 년월 날짜 체크
	 * @param SearchMap
	 * @return HashMap   
	 */
    public static HashMap yymmCheck(String val, int len, String strName) {
		HashMap returnMap 		= new HashMap();
		int 	resultValue		= 0;

		if ("".equals(val)) { 
			returnMap.put("ErrorNumber",  resultValue);
			return returnMap;	
		}
		
		//String tmpDate = val.replaceAll("-", "");
		String tmpDate = val;
		String tmpMM = tmpDate.substring(tmpDate.length()-2);
		String msg = null;
		
		returnMap = ValidationChk.onlyNumber(val, strName);
		if ((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		if (tmpDate.length() != len || Integer.parseInt(tmpMM) < 1 || Integer.parseInt(tmpMM) > 12) {
			msg = "[" + strName + "] : 유효하지 않은 날짜 형식입니다.";
			returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", msg);
			
	    	return returnMap;
		}
		
		returnMap.put("ErrorNumber",  resultValue);
		return returnMap;		
	}
    

	/**	 
	 * 두 날짜 비교하여 결과를 리턴
	 * @param fromValue: 시작일
	 * @param toValue  : 종료일
	 * @param strName  : 경고개체명
	 * @param lowLength: 최소입력자리수
	 * @param highLength: 최대입력자리수
	 * @return HashMap  
	*/
    public static HashMap comparePeriod(String fromValue, String toValue, String fromName, String toName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";

    	if ( "".equals(fromValue) || "".equals(toValue) ) {
    		returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
    		returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
    	}
    	
    	int arguments1      = Integer.parseInt(fromValue);
    	int arguments2      = Integer.parseInt(toValue);
    	Object[] arguments3 = {fromName, toName};
    	
		if ( arguments1 > arguments2) {
			resultValue = ErrorMessages.FAILURE_COMPARE_PERIOD_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_COMPARE_PERIOD_MESSAGE, arguments3);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }    
    

	/**	 
	 * 주민번호 유효성 체크
	 * @param jumin1 : 주민번호 앞자리
	 * @param jumin2 : 주민번호 뒷자리
	 * @return HashMap  
	*/
    public static HashMap chkJumin(String jumin1, String jumin2) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	int	i     = 0;
    	int IDtot = 0;
    	
    	for (i=0 ; i<=5 ; i++) {
			IDtot = IDtot + ((i%8+2) * Integer.parseInt(jumin1.substring(i,i+1))) ;
    	}
    	for (i=6 ; i<=11 ; i++) {
			IDtot = IDtot + ((i%8+2) * Integer.parseInt(jumin2.substring(i-6,i-5))) ;
    	}
    	
    	IDtot = 11 - (IDtot % 11);
		IDtot = IDtot % 10;

		if (IDtot != Integer.parseInt(jumin2.substring(6,7))) {
			returnMap = isRegNo_fgnno(jumin1 + "" + jumin2);
			if( (Integer)returnMap.get("ErrorNumber") >= 0 ){
				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
				returnMap.put("ErrorMessage", resultMsg);		    	
			}
		} else {
			returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
		}
    	
    	return returnMap;	        
    }     
    

	/**	 
	 * 외국인 주민등록번호 체크
	 * @param jmno : 주민번호
	 * @return HashMap  
	*/
    public static HashMap isRegNo_fgnno(String jmno) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= ErrorMessages.FAILURE_CHK_JUMIN_CODE;
    	String  resultMsg       = ErrorMessages.FAILURE_CHK_JUMIN_MESSAGE;
    	int sum		 = 0;
    	int odd		 = 0;
    	String[] buf = new String[13];
    	
    	for(int i=0; i<13; i++) { 
            buf[i]=String.valueOf(jmno.charAt(i)); 
        }

    	odd = Integer.parseInt(buf[7])*10 + Integer.parseInt(buf[8]);

    	if(odd%2 != 0) { 
			returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}
    	
    	if( (Integer.parseInt(buf[11])!=6) && (Integer.parseInt(buf[11])!=7) && (Integer.parseInt(buf[11])!=8) && (Integer.parseInt(buf[11])!=9) ) {
			returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}
    	
    	int[] multipliers = {2,3,4,5,6,7,8,9,2,3,4,5};
    	
    	for(int i=0; i<12; i++) { 
    		sum += (Integer.parseInt(buf[i]) *multipliers[i]); 
    	}
    	
    	sum = 11 - (sum%11);
    	
    	if(sum >= 10) { sum   -= 10; }
    	
    	sum += 2;
    	
        if(sum >= 10) { sum   -= 10; }
        
        if(sum != Integer.parseInt(buf[12])) {
        	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
        }
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(ErrorMessages.SUCCESS_CODE));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

	/**	 
	 * 사업자 번호 유효성
	 * @param objValue : 사업자번호
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap chkCompanynum(String objValue , String strName) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  tmpValue		= "0";
    	String  resultMsg       = "";
    	String  chars           = "0123456789"; 
    	int nsize				= 0;

    	nsize = getLength(objValue.replaceAll("-", ""));
    	if (nsize == 10){
    		
    		tmpValue = objValue.replaceAll("-", "");
    		
    		for (int inx = 1; inx < tmpValue.length(); inx++) {
    			
    			if (chars.indexOf(tmpValue.charAt(inx)) == -1) {
        			resultValue = ErrorMessages.FAILURE_CHK_PERMIT_CODE;
        			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PERMIT_MESSAGE, "사업자번호");
    				
    				returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
    				returnMap.put("ErrorMessage", resultMsg);
    				
    		    	return returnMap;
    			}
    		}

    	    int w_c, w_e, w_f, w_tot;
    	    w_c = tmpValue.charAt(8) * 5  ; 	// 9번째자리의 숫자에 5를 곱한다.
    	    w_e = w_c/10;				// 10으로 나누고 10진수 형태의 숫자형으로 만든당..나눈몫
    	    w_f = w_c % 10           ;   	// 10으로 나눈 나머지....
    	    
    	    w_tot = tmpValue.charAt(0) * 1  ;
    	    w_tot += tmpValue.charAt(1) * 3 ;
    	    w_tot += tmpValue.charAt(2) * 7 ;
    	    w_tot += tmpValue.charAt(3) * 1 ;
    	    w_tot += tmpValue.charAt(4) * 3 ;
    	    w_tot += tmpValue.charAt(5) * 7 ;
    	    w_tot += tmpValue.charAt(6) * 1 ;
    	    w_tot += tmpValue.charAt(7) * 3 ;
    	    w_tot += tmpValue.charAt(9) * 1 ;
    	    w_tot += (w_e + w_f)       ;

    		if (w_tot % 10 != 0 ){ // 10으로 나누어 지면 false를 그렇지 않으면 true를 반환합니당..
    			resultValue = ErrorMessages.FAILURE_CHK_PERMIT_CODE;
    			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PERMIT_MESSAGE, "사업자번호");
    	    }

    	}else{
    		resultValue = ErrorMessages.FAILURE_CHK_PERMIT_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PERMIT_MESSAGE, "사업자번호");
    	}
        returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
        returnMap.put("ErrorMessage", resultMsg);

    	return returnMap;
    }    

    /**	 
	 * 사업자 등록번호 길이 체크
	 * @param objValue : 사업자등록번호
	 * @param strName  : 경고개체명
	 * @param companyLength: 입력자리수
	 * @return HashMap  
	*/
    public static HashMap LengthCheck_companynum(String objValue, String strName, int companyLength) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	int nsize = 0;
		Object[] arguments = {strName, companyLength};
		
    	nsize = getLength(objValue);
		
		if ( companyLength > 0 && nsize == 0 ) {
			resultValue = ErrorMessages.FAILURE_CHK_EMPTY_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_EMPTY_MESSAGE, strName);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}
		if ( nsize < companyLength || nsize > companyLength ) {
			resultValue = ErrorMessages.FAILURE_CHK_LENGTH3_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_LENGTH3_MESSAGE, arguments);
	    	
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
	    	
	    	return returnMap;
		}
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	
	}
    
    public  static boolean checkAddresses(String addresses) throws Exception {
		mailWithName = new RE(
		"([^<^>^@]*)<([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)>");
		mailOnly = new RE(
		"([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)");
		
		if (!(mailWithName.isMatch(addresses) || mailOnly
				.isMatch(addresses))) {
			return false;
		}
		
		return true;
	}

    
	/**
	 * 문자열의 길이를 리턴
	 * @param str : 개체 값
	 * @return int 
	*/
    public static int getLength(String str) {
		int len = str.length();
		int cnt=0, index=0;

		while (index < len)
		{
			if (str.charAt(index++) < 256) // 1바이트 문자라면...
				cnt++;     // 길이 1 증가
			else // 2바이트 문자라면...
				cnt += 3;  // 길이 3 증가
		}

		return cnt;
    }
    
    /**	 
	 * 숫자범위 검사
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkNumberRange(String objValue, String strName,String strRangeValue) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	long    iValue          = Long.parseLong(objValue);    	
    	long    iRange          = Long.parseLong(strRangeValue); 
		
		Object[] arguments3 = {strName, strRangeValue};
		
		if(iValue>iRange){
			resultValue = ErrorMessages.FAILURE_CHK_NUMBER_RANGE_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_NUMBER_RANGE_MESSAGE, arguments3);
		}//if
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }
    
    /**	 
	 * 숫자범위 검사 (double type)
	 * @param objValue : 개체 값
	 * @param strName  : 경고개체명
	 * @return HashMap  
	*/
    public static HashMap checkNumberDoubleRange(String objValue, String strName,String strRangeValue) {
    	HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
    	String  resultMsg       = "";
    	double    iValue          = Double.parseDouble(objValue);    	
    	double    iRange          = Double.parseDouble(strRangeValue); 
		
		Object[] arguments3 = {strName, strRangeValue};
		
		if(iValue>iRange){
			resultValue = ErrorMessages.FAILURE_CHK_NUMBER_RANGE_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_NUMBER_RANGE_MESSAGE, arguments3);
		}//if
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
		returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;	        
    }

      /**
	 * 소수점이 있는 컬럼 체크 들어올경우 확인
	 * @param objValue : 개체 값
     * @param totLen : 총 자리수
     * @param commaLen : 소수점 자리수  
	 * @param strRangeValue  : 경고개체명
	 * @return HashMap
     * SAMPLE  OB ( 15 , 3 )    --> totLen = 15  commaLen = 3  
	*/
    public static HashMap checkNumberComma(String objValue, int totLen , int commaLen , String strRangeValue) {
       HashMap returnMap 		= new HashMap();
       String  resultValue		= "0";
       String  resultMsg        = "";
       int     intLen           = 0;
       
       intLen = totLen - commaLen;

        Object[] arguments2 = {strRangeValue, intLen };
        Object[] arguments3 = {strRangeValue, intLen ,commaLen };

        int comma =0 ;
        int commaNum =0;   // 컴마 위치
        String ch;
        int  i=1;     // 컴마 갯수

        objValue =  objValue.trim();


        for(int x = 0; x < objValue.length() ; x++)
        {

               ch= objValue.substring(x,x+1);
               if( ".".equals(ch) )
               {
                     commaNum = x+1;
                     comma=i;
                    i++;
               }
         }
       
        if(comma < 1)	/*"."가 없을 경우*/
        {
           if(objValue.length() > intLen)
            {
                resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_CODE;
			    resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE, arguments2);
            }
        }else if(comma==1)
        {
            int commaMaxLength = objValue.length();
            String commaStirng =  objValue.substring(commaNum,commaMaxLength);
            int  commalength = commaStirng.length() ;
            String intStirng =  objValue.substring(0, commaNum-1);
            int  intlength = intStirng.length() ;


            // 소수점만 입력한경우
            if( commalength == 0 ){
               resultValue = ErrorMessages.FAILURE_CHK_NUMBER_COMMA_CODE;
			   resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_NUMBER_COMMA_MESSAGE, arguments3);
            }

            if( commalength > commaLen )
            {
               resultValue = ErrorMessages.FAILURE_CHK_NUMBER_COMMA_CODE;
               resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_NUMBER_COMMA_MESSAGE, arguments3);
            }
            
            if(intlength > intLen)
            {
                resultValue = ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_CODE;
			    resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_PHONE_NUMBER_LEN_MESSAGE, arguments2);
            }
        }else if(comma > 1)  {     /*"." 2개 이상일 경우*/

            resultValue = ErrorMessages.FAILURE_CHK_NUMBER_COMMA_CODE;
			resultMsg   = ErrorMessages.format(ErrorMessages.FAILURE_CHK_NUMBER_COMMA_MESSAGE, arguments3);
        }
        
        returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
        returnMap.put("ErrorMessage", resultMsg);

    	return returnMap;
    }
    
    /**
     * 배열내에 중복되는 값 존재여부 확인
     * @param arrStr
     * @return
     */
    public static HashMap checkDuplDataInArray(String[] arrStr, String strName){
    	HashMap returnMap = new HashMap();
    	HashMap tempMap = new HashMap();
    	String  resultValue		= "0";
        String  resultMsg        = "";
        
    	for(int index = 0;index < arrStr.length;index++){
    		if(tempMap.containsKey(arrStr[index])){
    			resultValue = ErrorMessages.FAILURE_CHK_DUPLICATED;
    			resultMsg = ErrorMessages.format(ErrorMessages.FAILURE_CHK_DUPLICATED_MESSAGE, strName);
    			
    			break;
    		}else{
    			tempMap.put(arrStr[index], "");
    		}
    	}
    	
    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
        returnMap.put("ErrorMessage", resultMsg);
    	
    	return returnMap;
    }
    
    /**
     * 출원 번호 형식 체크
     * @param arrStr
     * @return
     */
 	public static HashMap checkRegtNo(String regtNo, String frgnYn, String ptntPctPtappYn, String ptappRegistNatCd, String mparmUrl){
 		HashMap returnMap 		= new HashMap();
    	String  resultValue		= "0";
        String  resultMsg       = "";
        
        if(regtNo == null || "".equals(regtNo)){
    		returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
			return returnMap;
    	}
        
        try{
        	regtNoChk1 	= new RE("(10)-([0-9]{4})-([0-9]{7})");
        	regtNoChk2 	= new RE("(10)-([0-9]{7})-(0000)");
        	regtNoChk3 	= new RE("(PCT)/([A-Z]{2})([0-9]{4})/([0-9]{6})");
        	regtNoChk4 	= new RE("([0-9]{2})/([0-9]{6})");
        	regtNoChk5 	= new RE("([0-9]{7})");
        	regtNoChk6 	= new RE("(20)([0-9]{2})-([0-9]{6})");
        	regtNoChk7 	= new RE("([0-9]{8}).([0-9]{1})");
        	regtNoChk8 	= new RE("(20)([0-9]{10}).([0-9]{1})");
        	regtNoChk9 	= new RE("(ZL20)([0-9]{10}).([0-9]{1})");
        	regtNoChk10 = new RE("([0-9]{8})");
        	regtNoChk11 = new RE("([0-9]{7}).([0-9]{1})");
        	regtNoChk12 = new RE("([0-9]{9})");
        	regtNoChk13 = new RE("(I)([0-9]{6})");
        	
        	if("N".equals(frgnYn)){			//국내
    			if("N".equals(ptntPctPtappYn) && "KR".equals(ptappRegistNatCd) && "01".equals(mparmUrl) ){
    				if (!regtNoChk1.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "KR".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk2.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}
    		}else if("Y".equals(frgnYn)) {	//국제
    			if("Y".equals(ptntPctPtappYn) && "XI".equals(ptappRegistNatCd) && "01".equals(mparmUrl) ){
    				if (!regtNoChk3.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "US".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk4.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "US".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk5.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "JP".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk6.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "JP".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk5.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "XU".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk7.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "XU".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk5.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "CN".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk8.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "CN".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk9.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "DE".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk10.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "DE".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk7.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "GB".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk11.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "GB".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk5.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "CA".equals(ptappRegistNatCd)){
    				if (!regtNoChk5.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "TW".equals(ptappRegistNatCd) && "01".equals(mparmUrl)){
    				if (!regtNoChk12.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}else if("N".equals(ptntPctPtappYn) && "TW".equals(ptappRegistNatCd) && "02".equals(mparmUrl)){
    				if (!regtNoChk13.isMatch(regtNo)) {
    					resultValue = ErrorMessages.FAILURE_CHK_REGTNO_CODE;
    					resultMsg   = ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE;
    				}
    			}
    		}
			
	    	returnMap.put("ErrorNumber",  Integer.parseInt(resultValue));
			returnMap.put("ErrorMessage", resultMsg);
    	}catch(Exception e){
    		e.printStackTrace();
    		returnMap.put("ErrorNumber",  ErrorMessages.FAILURE_CHK_REGTNO_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_CHK_REGTNO_MESSAGE);
    	}
    	return returnMap;	       
    }    
    
}

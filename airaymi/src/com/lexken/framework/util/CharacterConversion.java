package com.lexken.framework.util;

public final class CharacterConversion {
	/**
	 * 문자열을 인코딩한다
	 * @param str 변환할 문자열
	 * @param oCharsetName 원래 문자셋
	 * @param tCharsetName 변환될 문자셋
	 * @return 변환된 문자열
	 */
	public static synchronized String convert( String str, String oCharsetName, String tCharsetName )
	{
		String retStr = null;

		if (str == null ) return null;

		try {
			retStr = new String(new String(str.getBytes(oCharsetName), tCharsetName));
		}
		catch( java.io.UnsupportedEncodingException e ){
			retStr = new String(str);
		}

        return retStr;
	}
	
	/**
	 * 8859_1 --> KSC5601
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */
	public static synchronized String E2K( String str )
	{
        return convert(str,"8859_1", "KSC5601");
	}

	/**
	 * KSC5601 --> 8859_1
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */	
	public static synchronized String K2E( String str )
	{

		return convert(str,"KSC5601", "8859_1");
	}

	/**
	 * 8859_1 --> UTF-8
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */	
	public static synchronized String E2U( String str )
	{
        return convert(str,"8859_1", "utf-8");
	}

	/**
	 * UTF-8 --> 8859_1
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */	
	public static synchronized String U2E( String str )
	{
        return convert(str,"utf-8", "8859_1");
	}

}

package com.lexken.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.lang.ClassLoader;


/** <pre>
 * 미리 지정된 코드관련 데이터 파일에서 코드관련 정보를 읽어들이고 특정 코드에 해당하는 코드명을 찾는 클래스.
 *
 *사용방법.
 * CodeDic.get("ERRORCODE","110") -> "데이타가 없습니다."
 *
 */

public class Broking
{

	/**
	 * code와 message를 hashtable에 관리한다.
	 */
	static Hashtable data = new Hashtable();

	/**
	 * 최종 code file load 시간
	 */
	static long ld_time = 0;

	/**
	 * code file이 최종 modify된 시간
	 */
	static long cur_time;

	/**
	 * code file
	 */
	static File f = null;
	private static Object lock = new Object();

//	private final Log logging = null;
	/**
	 * code file이 수정된 경우 file을 다시 읽어 hashtable에 저장
	 * @return void
	 */
	public static void load()
	{
		String temp;
		StringTokenizer st;
		String token1, token2;

        String path ;
		try
		{
            // TEST      WEB-INF\classes
//             path = "/rdais/BROKING.DAT";
//            f = new File(path);

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//              f = new File(loader.getResource("").getFile());
//             f = new File(f.getParent() + File.separator + "BROKING.DAT" );
            f = new File(loader.getResource("BROKING.properties").getFile());

            ld_time 			= f.lastModified();
			InputStreamReader fr = new InputStreamReader(new FileInputStream(f), "KSC5601");
			BufferedReader br 	= new BufferedReader(fr);
			String stName 		= "";
			Hashtable hsTmp 	= null;
            
            int i =0;
            while((temp = br.readLine()) != null)
			{
				try{
					String stTmp 		= null;
					String stCode 		= null;
					String stCodeName 	= null;

					if ( temp.trim().length() == 0 ) continue;
					if ( temp.charAt(0) == '#' ) continue;


					st = new StringTokenizer(temp, "|");
					stTmp = st.nextToken().trim();

					if ( !stName.equals(stTmp) )
					{
						if ( !stName.equals("") )
						{
							data.put(stName, hsTmp );
//							logging.error("common", "[CodeDic]"+stName+", "+hsTmp);
						}

						stName = stTmp;
						hsTmp 		= new Hashtable();
					}

					stCode 		= st.nextToken().trim();
					stCodeName 	= st.nextToken().trim();

					hsTmp.put(stCode, stCodeName);
//					logging.error("common", "[CodeDic]"+stCode+", "+stCodeName);

				}
				catch(Exception e){
				}
			}

			data.put(stName, hsTmp );

//			logging.info("[CodeDic] CodeDic finished END");
		}
		catch (Exception e)  {}
	}

	/**
	 * code에 해당하는 message를 return한다. 해당 message가 없거나 인수가 null인 경우에는 ""를 return한다.
	 *
	 * @return String code에 해당하는 코드명..
     * @param gubun java.lang.String 구분코드
     * @param code java.lang.String code
	 */
	public static String get(String gubun, String code) throws Exception
	{

        if(gubun == null || code == null ) return "";

		String msg	 = null;
		Hashtable hs = null;

		
		if ( f == null )
		{
			cur_time = 1;
		}
		else
		{
			// code file의 최종 수정 시간을 check한다.
			cur_time = f.lastModified();
		}


		synchronized ( lock )
		{
			if (ld_time < cur_time) load();
		}

		hs = (Hashtable)data.get(gubun);

		if ( hs == null ){
			msg = "";
		}
		else {
			msg = (String)hs.get(code);
			if (msg == null){
				msg = "";
			}
		}

		return msg;
	}

	/**
	 * Group code에 해당하는 Hashtable을 return한다.
	 * @return String gubun 해당하는 코드/코드명 hashtable정보
     * @param gubun java.lang.String 구분코드
	 */
	public static Hashtable getAll(String gubun)
	{
		Hashtable hs = null;

		// code file의 최종 수정 시간을 check한다.
		if ( f == null )
		{
			cur_time = 1;
		}
		else
		{
			// code file의 최종 수정 시간을 check한다.
			cur_time = f.lastModified();
		}

		synchronized ( lock )
		{
			if (ld_time < cur_time) load();
		}

		hs = (Hashtable)data.get(gubun);

		return hs;
	}


}

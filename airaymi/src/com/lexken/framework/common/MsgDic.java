package com.lexken.framework.common;

import java.io.*;
import java.util.*;


/** <pre>
 * 미리 지정된 코드관련 데이터 파일에서 코드관련 정보를 읽어들이고 특정 코드에 해당하는 코드명을 찾는 클래스.
 *
 * 사용방법.
 * MsgDic.get("ERRORCODE","110") -> "데이타가 없습니다."
 *
 */


public class MsgDic
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

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            f = new File(loader.getResource("MSG.properties").getFile());

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
//							logging.error("common", "[MsgDic]"+stName+", "+hsTmp);
						}

						stName = stTmp;
						hsTmp 		= new Hashtable();
					}

					stCode 		= st.nextToken().trim();
					stCodeName 	= st.nextToken().trim();

					hsTmp.put(stCode, stCodeName);
//					logging.error("common", "[MsgDic]"+stCode+", "+stCodeName);

				}
				catch(Exception e){
				}
			}

			data.put(stName, hsTmp );

//			logging.info("[MsgDic] MsgDic finished END");
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
            load();
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
			load();
		}
	
		hs = (Hashtable)data.get(gubun);

		return hs;
	}

    // 사용자 정의 에러 코드
    public static HashMap  getErrorMsg( String errorCode   )
    {
        HashMap returnMap  = new HashMap();

        return  getErrorMsg( returnMap ,  0 , "" , errorCode  );
    }

    // 사용자 정의 에러 코드
    public static HashMap  getErrorMsg(  HashMap returnMap  , String errorCode   )
    {
        return  getErrorMsg( returnMap ,  0 , "" , errorCode  );
    }

    public static HashMap  getErrorMsg(int oraErrCode,  String msg   )
    {
        HashMap returnMap  = new HashMap();

        return  getErrorMsg( returnMap ,  oraErrCode, msg ,  ""  );
    }
    
    public static HashMap  getErrorMsg( HashMap returnMap  , int oraErrCode,  String msg   )
    {
        return  getErrorMsg( returnMap ,  oraErrCode, msg ,  ""  );
    }

    public static HashMap  getErrorMsg(int oraErrCode,  String msg , String errorCode   )
    {
        HashMap returnMap  = new HashMap();

        return  getErrorMsg( returnMap ,  oraErrCode, msg ,  errorCode  );
    }

    public static HashMap  getErrorMsg( HashMap returnMap , int oraErrCode,  String msg , String errorCode  )
    {

       try {

           // 오라클 오류
           if ( oraErrCode > 0 ) {
                if ( !"".equals( get("ERRORMSG" , ""+oraErrCode))) {
                    returnMap.put("ErrorNumber", -1);
                    returnMap.put("ErrorMessage", get("ERRORMSG" ,  ""+oraErrCode) );
                }


           }else  if ( !"".equals(errorCode) ) {
               // 사용자 정의 에러 코드
                returnMap.put("ErrorNumber", -1);
                returnMap.put("ErrorMessage", get("ERRORMSG" , errorCode) );
           }else {
                //  오라클 오류 아닐경우
                if ( msg.indexOf("errno:238") >0 )
                {
                    returnMap.put("ErrorNumber", -1);
                    returnMap.put("ErrorMessage", get("ERRORMSG" , "UE001") );
                }else  if ( msg.indexOf("java.lang.IllegalArgumentException") >0 )
                {
                    returnMap.put("ErrorNumber", -1);
                    returnMap.put("ErrorMessage", get("ERRORMSG" , "UE002") );
                }
           }

           }catch (Exception e) {}
            finally
           {
               return returnMap;
           }

/*      오라클 오류유형별 건수 
        ORA-03113	1290    // DBlINK
        ORA-00001	1141    // 무결성 오류
        ORA-01089	1020    // SHUTDOWN
        ORA-06512	440     //  SELECT 중복
        ORA-01438	220     // 지정한 정도를 초과한 값이 열에 지정되었습니다
        ORA-00904	199     //열명이 부적합합니다
        ORA-02068	190     // DB liNK 이후
        ORA-01722	145      // 수치기 부족합니다.
        ORA-01401	126      // 열에대한 입력값이 크다는
        ORA-02063	113     // "%s%s가 선행됨 (%s%s로 부터)"
        ORA-06550	62      // "줄 %s, 열%s:%s"
        ORA-01115	41      // 화일 '%s'의 블록(블록 번호 %s) 읽기 IO 오류입니다
        ORA-01110	41      // 데이타 화일 %s: '%s'"
        ORA-27091	41
        ORA-27072	41
        ORA-01400	37      // 행의 입력으로 필수 열(NOT NULL)에 값이 지정되지 않았습니다
        ORA-01403	27      // 데이타가 없습니다"
        ORA-20001	12
        ORA-04068	8       // 패키지%s%s%s의 기존 상태가 버려졌습니다
        ORA-04061	8       // "%s의 기존상태가 무효화되었습니다
        ORA-04065	8       // 실행불가, %s가 변경 혹은 삭제되었습니다
        ORA-01427	5      // 단일 행 부속 질의에 의해 2개 이상의 행이 리턴되었습니다
        ORA-00923	2      // FROM 키워드가 있어야할 곳에 없습니다"
        ORA-00933	1      // SQL 명령어가 올바르게 종료되지 않았습니다"


        errno:238   378
        java.net.SocketException: Socket closed 921    // 파일 업로드 관련
        Cause: java.lang.IllegalArgumentException   83
*/


    }

     public static HashMap  getMsg(String CodeGbn , String Errcode )
     {
          HashMap returnMap  = new HashMap();

         return getMsg(  returnMap ,  CodeGbn ,  Errcode );
     }

     public static HashMap  getMsg( HashMap returnMap , String CodeGbn , String Errcode )
     {

         try {

                returnMap.put("ErrorNumber", -1);
                returnMap.put("ErrorMessage", get( CodeGbn , Errcode) );


           }catch (Exception e) {}
            finally
           {
               return returnMap;
           }

     }
}

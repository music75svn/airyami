package com.lexken.framework.common;

import sutil.Encrypt;


public class SsoEncrypt {
	
	 public String[] EncodeByFileInType(String strType, String strFlePath, String strData){

		    String[] arrRet = new String[4];
		    String strD0 = null;  
		    String strD1 = null;  
		    String strD2 = null;  
		    String strD3 = null;  
		    
		    strD0 = strType;

		    strD1 = EncodeBySType(strData);
		    
		    arrRet[0] = strD1;
		    arrRet[1] = strD2;
		    arrRet[2] = strD3;
		    arrRet[3] = strD0;

		    return arrRet;
		  }

		  public String[] EncodeByFile(String strFlePath, String strData){

		    String strType = null;
		    
		    strType     = "SDS";
		    
		    return EncodeByFileInType(strType, strFlePath, strData);
		  }

		  public String[] EncodeByCertIDInType(String strType, String strCertID, String strData){

		    String[] arrRet = new String[4];
		    String strD0 = null;  
		    String strD1 = null;  
		    String strD2 = null;  
		    String strD3 = null;  
		    
		    strD0 = strType;
		    
		    strD1 = EncodeBySType(strData);
		    
		    arrRet[0] = strD1;
		    arrRet[1] = strD2;
		    arrRet[2] = strD3;
		    arrRet[3] = strD0;

		    return arrRet;
		  }

		  public String[] EncodeByCertID(String strCertID, String strData){

		    String strType = null;  
		    
		    strType   = "SDS";
		    
		    return EncodeByCertIDInType(strType, strCertID, strData);
		  }

		  public String DecodeByFileInType(String strType, String strFlePath, String strKey, String strHash, String strData){
		    
		    String strRet = null;
		    
		    strRet = DecodeBySType(strData);
		    
		    return strRet;
		  }

		  public String DecodeByFile(String strFlePath, String strKey, String strHash, String strData){

		    String strMode = "SDS";
		    
		    return DecodeByFileInType(strMode, strFlePath, strKey, strHash, strData);
		  }


		  public String EncodeBySType(String strData){

		    String strRet = null;

		    strRet = Encrypt.com_Encode(":" + strData + ":sisenc");

		    return strRet;  
		  }


		  public static String DecodeBySType(String strData){

		    String strRet = null;
		    int e=0, d=0, s=0, i=0;

		    strRet = Encrypt.com_Decode(strData);

		    e = strRet.indexOf(":");
		    d = strRet.indexOf(":sisenc");
		    if(e > -1 && d > -1)
		      strRet = strRet.substring(e+1, d);
		    return strRet;
		  }


}

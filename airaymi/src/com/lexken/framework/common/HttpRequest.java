package com.lexken.framework.common;

public class HttpRequest
{
	protected javax.servlet.http.HttpServletRequest m_req;
	protected String strNullParam ;
	protected String m_method ;

	/**
	 * HttpRequest
	 * @param request  
	 * @return   
	 * @throws   
	*/
	public HttpRequest(javax.servlet.http.HttpServletRequest request)
	{
		m_req = request;
		strNullParam = null;
		m_method = request.getMethod();
	}

	
	/**
	 * _get_parameter
	 * @param varName  
	 * @return String  
	 * @throws   
	*/
	protected String _get_parameter(String varName)
	{
		/*
		if("GET".equals(m_method)){
			return toKorean(m_req.getParameter(varName.trim()));
		}else{
			return toKorean(m_req.getParameter(varName.trim()));
		}
		*/
		if("GET".equals(m_method)){
			if(m_req.getRequestURL().indexOf("localhost") > 0)   
				//return toKorean(m_req.getParameter(varName.trim()));  // 로컬환경(톰캣)
				return m_req.getParameter(varName.trim());  // 로컬환경(제우스)
			else											// 서버(제우스)
				return m_req.getParameter(varName.trim());    
		}else{
			if(m_req.getRequestURL().indexOf("localhost") > 0)   
				return toKorean(m_req.getParameter(varName.trim()));  // 로컬환경(톰캣)
				//return m_req.getParameter(varName.trim());  // 로컬환경(제우스)
			else											// 서버(제우스)
				return m_req.getParameter(varName.trim());    
		}

	}


	/**
	 * getFormatNumber
	 * @param n, str  
	 * @return String  
	 * @throws   
	*/
	public String getFormatNumber(int n, String str)
	{
		StringBuffer sb = new StringBuffer();
		String nStr = n + "";
		try
		{
			int nLength = nStr.length();
			int nComma = 3-nLength%3;
			if (nComma == 3) nComma = 0;
			for(int i=0; i<nLength; i++)
			{
				if (nComma==3)
				{
					sb.append(str);
					nComma = 0;
				}
				sb.append( nStr.charAt(i));
				nComma++;
			}
			
		}
		catch(Exception e)
		{
			//throw e;
		}
		return sb.toString();
	}

	
	/**
	 * getFormatNumber
	 * @param d, str  
	 * @return String  
	 * @throws   
	*/
	public String getFormatNumber(double d, String str)
	{
		StringBuffer sb = new StringBuffer();
		String dStr = d + "";
		String nStr = dStr.substring(0, dStr.indexOf("."));
		String eStr = dStr.substring(dStr.indexOf("."), dStr.length());
		try
		{
			int nLength = nStr.length();
			int nComma = 3-nLength%3;
			if (nComma == 3) nComma = 0;
			for(int i=0; i<nLength; i++)
			{
				if (nComma==3)
				{
					sb.append(str);
					nComma = 0;
				}
				sb.append( nStr.charAt(i));
				nComma++;
			}
			sb.append(eStr);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return sb.toString();
	}

	
	/**
	 * NVL
	 * @param str, bDefault  
	 * @return boolean  
	 * @throws   
	*/
	public boolean NVL(String str, boolean bDefault)
	{
		boolean r = bDefault;
		try
		{
			if (str != null)
				r = Boolean.getBoolean(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return r;
	}

	
	/**
	 * NVL
	 * @param str, nDefault  
	 * @return int  
	 * @throws   
	*/
	public int NVL(String str, int nDefault) 
	{
		int r = nDefault;
		try
		{
			if (str != null)
				r = Integer.parseInt(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return r;
	}


	/**
	 * NVL
	 * @param str, dDefault  
	 * @return double  
	 * @throws   
	*/
	public double NVL(String str, double dDefault)
	{
		double r = dDefault;
		try
		{
			if (str != null)
				r = Double.parseDouble(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return r;
	}

	
	/**
	 * NVL
	 * @param str, strDefault  
	 * @return String  
	 * @throws   
	*/
	public String NVL(String str, String strDefault)
	{
		String r = strDefault;
		try
		{
			if (str != null)
				r = str;
		}
		catch(Exception e)
		{
			//throw e;
		}
		return r;
	}
	
	
	/**
	 * getNullParamName
	 * @param   
	 * @return String  
	 * @throws   
	*/
	public String getNullParamName()
	{
		return strNullParam;
	}

	
	/**
	 * resetNullParamName
	 * @param   
	 * @return 
	 * @throws   
	*/
	public void resetNullParamName()
	{
		strNullParam = null;
	}

	
	/**
	 * getBoolean
	 * @param varName  
	 * @return boolean
	 * @throws   
	*/
	public boolean getBoolean(String varName)
	{
		String str = null;
		boolean x = false;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName.trim();
			}
			else
				x = Boolean.getBoolean(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return x;
	}

	
	/**
	 * getInt
	 * @param varName  
	 * @return int
	 * @throws   
	*/
	public int getInt(String varName) 
	{
		String str = null;
		int x = 0;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName.trim();
			}
			else
				x = Integer.parseInt(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return x;
	}

	
	/**
	 * getString
	 * @param varName  
	 * @return String
	 * @throws   
	*/
	public String getString(String varName)
	{
		String str = null;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName;
			}
		}
		catch(Exception e)
		{
			//throw e;
		}

		if(str==null)
			str = "";

		return str;
	}

	/**
	 * getStringUTF
	 * @param varName  
	 * @return String
	 * @throws   
	*/
	public String getStringUTF(String varName) throws Exception
	{
		m_req.setCharacterEncoding("UTF-8");

		String str = null;
		try
		{
			str = m_req.getParameter(varName.trim());
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName;
			}
		}
		catch(Exception e)
		{
			//throw e;
		}

		if(str==null)
			str = "";

		return str;
	}
	
	/**
	 * getDouble
	 * @param varName  
	 * @return double
	 * @throws   
	*/
	public double getDouble(String varName) 
	{
		String str = null;
		double x = 0.0;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName.trim();
			}
			else
				x = Double.parseDouble(str);
		}
		catch(Exception e)
		{
			//throw e;
		}
		return x;
	}
	

	/**
	 * getInt :  m_req.getString, getInt 2개 파라미터 <- default value
	 * @param varName, nDefault
	 * @return int
	 * @throws   
	*/
	public int getInt(String varName, int nDefault)
	{
		String str = null;
		int x = nDefault;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName.trim();
			}
			else
			{
				if (str.equals(""))
				{
					if (strNullParam==null)
						strNullParam = varName.trim();
				}
				else
					x = Integer.parseInt(str);
			}
		}
		catch(Exception e)
		{
			//throw e;
		}
		return x;
	}

	
	/**
	 * getString
	 * @param varName, strDefault
	 * @return String
	 * @throws   
	*/
	public String getString(String varName, String strDefault)
	{
		String str = null;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName;
			}
			else
			{
				if (str.equals(""))
				{
					if (strNullParam==null)
						strNullParam = varName.trim();
					str = strDefault;
				}
			}

		}
		catch(Exception e)
		{
			//throw e;
		}

		if(str==null)
			str = strDefault;

		return str;
	}
	

	/**
	 * getDouble
	 * @param varName, nDefault
	 * @return double
	 * @throws   
	*/
	public double getDouble(String varName, double nDefault)
	{
		String str = null;
		double x = nDefault;
		try
		{
			str = _get_parameter(varName);
			if (str == null)
			{
				if (strNullParam==null)
					strNullParam = varName.trim();
			}
			else
			{
				if (str.equals(""))
				{
					if (strNullParam==null)
						strNullParam = varName.trim();
				}
				else
					x = Double.parseDouble(str);
			}
		}
		catch(Exception e)
		{
			//throw e;
		}
		return x;
	}
	

	/**
	 * getDouble
	 * @param varName, nDefault
	 * @return String
	 * @throws   
	*/
	static public String statLPAD(String strOrg, int length, String fillString)
	{
		int nLength = strOrg.length();
		
		if (fillString.length() != 1) return strOrg;
		if (length <= nLength) return strOrg;

		String strRepeat = "";
		for(int i=0; i< length-nLength; i++)
		{
			strRepeat += fillString;
		}
		return (strRepeat + strOrg);
	}

	
	/**
	 * LPAD
	 * @param strOrg, length, fillString
	 * @return String
	 * @throws   
	*/
	public String LPAD(String strOrg, int length, String fillString)
	{
		return HttpRequest.statLPAD(strOrg, length, fillString);
	}
	

	/**
	 * toKorean
	 * @param str
	 * @return String
	 * @throws   
	*/
	public String toKorean(String str)
	{		
		if (str == null) return str;
		String result = null;
		try
		{
			result = new String(str.getBytes("8859_1"), "EUC-KR");
		}
		catch (Exception e)
		{
			//
		}
		return result;
	}

	
	/**
	 * toEnglish
	 * @param str
	 * @return String
	 * @throws   
	*/
	public String toEnglish(String str)
	{
		if (str == null) return str;
		String result = null;
		try
		{
			result = new String(str.getBytes("EUC-KR"), "8859_1");
		}
		catch (Exception e)
		{
			//
		}
		return result;
	}
	
	/**
	 * getParameterValuesToKorean
	 * @param str
	 * @return String[]
	 * @throws   
	*/
	public String[] getParameterValuesToKorean(String varName)
	{	
		String strTemp[] = null;
		
		strTemp = m_req.getParameterValues(varName.trim());
		
		if(strTemp != null){
			if("GET".equals(m_method)){
				for(int i=0; i<strTemp.length; i++){
					if(m_req.getRequestURL().indexOf("localhost") > 0)   //로컬환경(톰캣)일때만 
						strTemp[i] = toKorean(strTemp[i]);
				}
			} else {
				for(int i=0; i<strTemp.length; i++){
					if(m_req.getRequestURL().indexOf("localhost") > 0)   //로컬환경(톰캣)일때만 
						strTemp[i] = toKorean(strTemp[i]);
				}				
			}
		}

		return strTemp;
	}
	
}

package com.lexken.framework.common;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TempletManager
{
	protected java.util.Hashtable args = new java.util.Hashtable();
	private final Log logger = LogFactory.getLog(getClass());
	
	public TempletManager() throws Exception
	{
		super();
	}

	public void setArg(String name, String value) 
	{
		args.put(name,value);
	}

	public String getTemplet(String FileName) throws Exception
	{
		//java.io.BufferedReader in = null;
		StringBuffer buf = new StringBuffer();
		
		FileInputStream fin=new FileInputStream(FileName);
		//BufferedReader br=new BufferedReader(new InputStreamReader(fin,"utf-8"));
		BufferedReader br=new BufferedReader(new InputStreamReader(fin,"euc-kr"));

		try 
		{
			//java.io.File file = new java.io.File(FileName);
			//in  = new java.io.BufferedReader(new java.io.FileReader(file));
			String line;
			String tmp;
			double cnt = 0;
			
			while( (line = br.readLine()) != null )	{
		    	tmp = line.replace("\"", "'");		    	
		    	
		    	if (cnt == 0) {		    		
		    		tmp = "<html>";		    		
		    	}		    	
				buf.append(tmp + "\n");
				cnt++;
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
		finally 
		{
			try { if ( br != null ) br.close(); } catch(Exception e){}
		}
		return buf.toString();
	}

	public String parseTemplet(String Templet) throws Exception
	{		
		StringBuffer content = new StringBuffer();

		while( Templet.length() > 0 ) 
		{
			int position = Templet.indexOf("<@");
			if ( position == -1 ) 
			{
				content.append(Templet);
				break;
			}
			if ( position != 0 ) {
				content.append(Templet.substring(0,position));
			}

			if ( Templet.length() == position + 2 ) break;
			String remainder = Templet.substring(position+2);
			
			int markEndPos = remainder.indexOf(">");
			if ( markEndPos == -1 ) break;
			
			String argname = remainder.substring(0, markEndPos).trim();
			String value = (String)args.get(argname);
			if ( value != null ) {
				content.append(value);
			}
			
			if ( remainder.length() == markEndPos + 1 ) break;
			Templet = remainder.substring(markEndPos + 1);
		}
		
		return content.toString();
	}

	public String[] parseTemplet(String Templet, String Parse1, String Parse2, String ChangeStr) throws Exception
	{
		String[]     ReturnValue = new String[2];
		StringBuffer content     = new StringBuffer();

		ReturnValue[0] = Templet;
		ReturnValue[1] = "";

		int position = Templet.indexOf(Parse1);

		if ( position == -1 ) return ReturnValue;
		
		if ( position != 0 ) content.append(Templet.substring(0,position));

		if ( Templet.length() == position + Parse1.length() ) return ReturnValue;
		
		String remainder = Templet.substring(position + Parse1.length());

		int markEndPos = remainder.indexOf(Parse2);
		if ( markEndPos == -1 ) return ReturnValue; 
			
		String argname = remainder.substring(0, markEndPos);
			
		if ( remainder.length() == markEndPos + 1 )  return ReturnValue;
		Templet = remainder.substring(markEndPos);

		content.append(ChangeStr);

		Templet = remainder.substring(markEndPos + Parse2.length());
		content.append(Templet);

		ReturnValue[0] = content.toString();
		ReturnValue[1] = argname;

		return ReturnValue;
	}
}

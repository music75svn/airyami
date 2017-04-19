package com.lexken.framework.util;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.sql.Timestamp;

public class ByTimestampFileRenamePolicy implements FileRenamePolicy {
  
  private String strCode = "";		
//  Logger logger	= Logger.getLogger(ByTimestampFileRenamePolicy.getClass());

  public ByTimestampFileRenamePolicy()
  {
  	this( new String(""));
  }
  

  public ByTimestampFileRenamePolicy(String v)
  {
  	if ( v.length() < 1)
  	{
  	  	this.strCode = v;
  	}else{
  	  	this.strCode = v + "";
  	}
  	
  }
    
  public File rename(File f) {
        
    if (!f.exists()) {
      //return f;
    }
    
    String fileSystemName = "";			
    String name 	= null;				
    String ext 		= null;				
    String body 	= null;				
    String tmpStr 	= "";
    Timestamp ts 	= null;				
    
    name = f.getName();
    
    int dot = name.lastIndexOf(".");
    if (dot != -1) 
    {
      ext = name.substring(dot);  // includes "."
      body = name.substring(0, dot);
    }
    else 
    {
      body = name;
      ext = "";
    }

    ts =  new java.sql.Timestamp(System.currentTimeMillis()) ;
    tmpStr = ts.toString();
    tmpStr =  tmpStr.substring( 0, tmpStr.indexOf(" ") ).substring(0,4) +tmpStr.substring(0, tmpStr.indexOf(" ") ).substring(5,7)
              +tmpStr.substring( 0, tmpStr.indexOf(" ") ).substring(8)
              + tmpStr.substring( (tmpStr.indexOf(" ")+1), 19 ).substring(0, 2)+ tmpStr.substring( (tmpStr.indexOf(" ")+1), 19 ).substring(3, 5)
              + tmpStr.substring( (tmpStr.indexOf(" ")+1), 19 ).substring(6);

    fileSystemName = this.strCode + tmpStr + ext;
    f = new File(f.getParent(), fileSystemName);
    
    return f;
  }
}

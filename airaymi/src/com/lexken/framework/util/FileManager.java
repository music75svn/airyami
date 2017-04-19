package com.lexken.framework.util;

import java.io.*;
import java.io.File;

import java.net.*;


public final class FileManager {

	private FileManager() {}

	public static boolean copy(String src, String dest) { 
		try{ 
			FileInputStream  in  = new FileInputStream(src); 
			FileOutputStream out = new FileOutputStream(dest); 
			byte buffer[] = new byte[1024]; 

			while(true){ 
				int bytes_read = in.read(buffer); 
				if(bytes_read == -1) 
				break; 
				out.write(buffer, 0, bytes_read); 
			} 

			in.close(); 
			out.close(); 
		} catch ( FileNotFoundException e ) {
			return false;
		} catch ( SecurityException e ) {
			return false;
		} catch ( IOException e ) { 
			return false;
		} 

		return true;
	}
	
	public static boolean copy(String src_dir, String src, String dest_dir, String dest) { 
		try{ 
			FileInputStream  in  = new FileInputStream(src_dir + "/" + src); 
			FileOutputStream out = new FileOutputStream(dest_dir +"/" + dest); 
			byte buffer[] = new byte[1024]; 

			while(true){ 
				int bytes_read = in.read(buffer); 
				if(bytes_read == -1) 
				break; 
				out.write(buffer, 0, bytes_read); 
			} 

			in.close(); 
			out.close(); 
		} catch ( FileNotFoundException e ) {
			return false;
		} catch ( SecurityException e ) {
			return false;
		} catch ( IOException e ) { 
			return false;
		} 

		return true;
	}
	
	public static boolean delete(String file) {
		boolean rtn;

		try {
			File f = new File(file);
			rtn =  f.delete();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean delete(String dir, String file) {
		boolean rtn;

		try {
			File f = new File(dir, file);
			rtn =  f.delete();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean rmdir(String dir){
		boolean rtn = false;

		try {
			File f = new File(dir);
			String[] dirlist = f.list();
			for ( int i = 0; i < dirlist.length ; i++ ){
				File ff = new File(dir + "/" + dirlist[i]);
				rtn =  ff.delete();
			}
			rtn =  f.delete();
		} catch ( SecurityException e ) {
			return false;
		}
		return rtn;
	}

	public static boolean isDirectory(String dir) {
		boolean rtn;

		try {
			File f = new File(dir);
			rtn  = f.isDirectory();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean isDirectory(String path, String dir) {
		boolean rtn;

		try {
			File f = new File(path, dir);
			rtn  = f.isDirectory();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean isFile(String file) {
		boolean rtn;

		try {
			File f = new File(file);
			rtn  = f.isFile();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean isFile(String dir, String file) {
		boolean rtn;

		try {
			File f = new File(dir, file);
			rtn  = f.isFile();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean exists(String file) {
		boolean rtn;

		try {
			File f = new File(file);
			rtn  = f.exists();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean exists(String dir, String file) {
		boolean rtn;

		try {
			File f = new File(dir, file);
			rtn  = f.exists();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static long length(String file) {
		long rtn;

		try {
			File f = new File(file);
			rtn  = f.length();
		} catch ( SecurityException e ) {
			return -1;
		}

		return rtn;
	}

	public static long length(String dir, String file) {
		long rtn;

		try {
			File f = new File(dir, file);
			rtn  = f.length();
		} catch ( SecurityException e ) {
			return -1;
		}

		return rtn;
	}

	public static boolean mkdir(String dir) {
		boolean rtn;

		try {
			File f = new File(dir);
			rtn = f.mkdir();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean mkdir(String path, String dir) {
		boolean rtn;

		try {
			File f = new File(path, dir);
			rtn = f.mkdir();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean mkdirs(String dir) {
		boolean rtn;
		try {
			File f = new File(dir);
			rtn = f.mkdirs();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean mkdirs(String path, String dir) {
		boolean rtn;

		try {
			File f = new File(path, dir);
			rtn = f.mkdirs();
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean renameTo(String src, String dest) {
		boolean rtn;

		try {
			File f1 = new File(src);
			File f2 = new File(dest);
			rtn = f1.renameTo(f2);
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static boolean renameTo(String src_dir, String src, String dest_dir, String dest) {
		boolean rtn;

		try {
			File f1 = new File(src_dir, src);
			File f2 = new File(dest_dir, dest);
			rtn = f1.renameTo(f2);
		} catch ( SecurityException e ) {
			return false;
		}

		return rtn;
	}

	public static String renameToPolicy(String src, String dest) {
		boolean rtn;

		try {
			File f1 = new File(src);
			File f2 = new File(dest);

			File f3 = DefaultFileRenamePolicy(f2);
			rtn = f1.renameTo(f3);

			if ( rtn == true ) return f3.getName();
			else return "";

		} catch ( SecurityException e ) {
			return "";
		}
	}


	public static String renameToPolicy(String src_dir, String src, String dest_dir, String dest) {
		boolean rtn;

		try {
			File f1 = new File(src_dir, src);
			File f2 = new File(dest_dir, dest);

			File f3 = DefaultFileRenamePolicy(f2);
			rtn = f1.renameTo(f3);

			if ( rtn == true ) return f3.getName();
			else return "";

		} catch ( SecurityException e ) {
			return "";
		}
	}

	public static File DefaultFileRenamePolicy(File f){
		if ( !f.exists()) {
			return f;
		}
   		String name = f.getName();
    	String body = null;
    	String ext = null;

    	int dot = name.lastIndexOf(".");
    	if (dot != -1) {
      		body = name.substring(0, dot);
      		ext = name.substring(dot);  // includes "."
    	}
    	else {
      		body = name;
      		ext = "";
    	}

    	int count = 0;
    	while (f.exists()) {
      		count++;
      		String newName = body + "[" + count + "]" + ext;
      		f = new File(f.getParent(), newName);
    	}
    	return f;
  	}

	/*
	2006-10-27 Kimsd 
	url 경로로 html 소스 가져오기
	*/
	public static String getResultFromUrl(String to_domain, String from_domain, String from_url) {
		URL url = null;
		//URL base = null;
		BufferedReader in = null;
		//String str = "";
		StringBuffer sb = new StringBuffer();

		try {
			url = new URL(from_domain + from_url);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			//str = "<base href='" + from_domain + "'/>";
			sb.append ("<base href='");
			sb.append (from_domain);
			sb.append ("'/>");

			while ((line = in.readLine()) != null) {
				//str = str + line;
				sb.append (line);
			}
			sb.append ("<base href='");
			sb.append (to_domain);
			sb.append ("'/>");
			//str = str + "<base href='" + to_domain + "'/>";
		} catch (Exception ex) {
			return "exp:" + ex.getMessage();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex2) {
			
			}
		}

		return sb.toString();
	}

	/*
	2006-11-16 Kimsd 
	url 경로로 jsp처리 결과 조회
	*/	
	public static String getResultFromUrlParam(String tgt_url, String query_str, String method) 
	{
		//#3 음원파일 생성(http://tts.hanaro.com연동)
		URL url = null ;
		HttpURLConnection urlConn = null;
		BufferedReader bf=null;
		DataOutputStream out1 = null;

		String ret_str = "";
		String ret_tmp = null;

		try {
			url = new URL(tgt_url);
			urlConn = (HttpURLConnection)url.openConnection();
			byte[] sendByte = new String(query_str).getBytes();
			urlConn.setDefaultUseCaches(false);
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setRequestMethod(method);
			
			urlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			//urlConn.setRequestProperty("content-type", "text/html");
			
			out1 = new DataOutputStream(urlConn.getOutputStream());
			out1.write(sendByte);
			out1.flush();
			
			//recieve
			bf = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while((ret_tmp = bf.readLine()) != null) {
				ret_str = ret_str + ret_tmp;
			}
			return ret_str;
			   
		} catch (MalformedURLException malformedurlexception) {
			ret_str = "-91:[MALFORMEDURLEXCEPTION]오류가 발생하였습니다.";
			return ret_str;
			//malformedurlexception.printStackTrace(); 
		} catch (IOException ioexception) {
			try {
				url = new URL(tgt_url);
				urlConn = (HttpURLConnection)url.openConnection();
				byte[] sendByte = new String(query_str).getBytes();
				urlConn.setDefaultUseCaches(false);
				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.setRequestMethod(method);
				
				urlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				//urlConn.setRequestProperty("content-type", "text/html");
				
				out1 = new DataOutputStream(urlConn.getOutputStream());
				out1.write(sendByte);
				out1.flush();
				
				//recieve
				bf = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				while((ret_tmp = bf.readLine()) != null) {
					ret_str = ret_str + ret_tmp;
				}
				return ret_str;
				   
			} catch (MalformedURLException malformedurlexception2) {
				ret_str = "-91:[MALFORMEDURLEXCEPTION]오류가 발생하였습니다.";
				return ret_str;
				//malformedurlexception.printStackTrace(); 
			} catch (IOException ioexception2) {
				ret_str = "-92:[IOEXCEPTION]오류가 발생하였습니다. ";
				return ret_str;
				//ioexception.printStackTrace(); 
			}
			/*
			ret_str = "-92:[IOEXCEPTION]오류가 발생하였습니다. ";
			return ret_str;
			*/ 
		} finally {
			try {
				if( bf != null && !bf.ready()) bf.close();
			} catch(Exception ex3) {
			}
		}
	}

	/*
	2006-10-27 Kimsd 
	파일 읽기
	*/

	static public void writeFile(String srFileName , String srContent)throws FileNotFoundException, IOException{
  		writeFile( srFileName , srContent , false);
	}

	static public void writeFile(String srFileName , String srContent , boolean append)throws FileNotFoundException, IOException{
	  	FileOutputStream 	fosOut	= null;  	
	  	PrintStream 		psOut 	= null;
		  	
	  	try{	
	    	fosOut = new FileOutputStream(srFileName,append);	  	
	    	psOut  = new PrintStream(fosOut);	  	
		  	psOut.println(srContent);
		  	psOut.close();
		  	psOut = null;					
		} catch(java.io.FileNotFoundException fnfe){			
		}	
	}
	
/**
 * get string the file contents
 */
	static public String readFile(String srFileName)throws FileNotFoundException, IOException{		
		String 			content = null;
		FileReader 		fr 		= null;
		BufferedReader 	br 		= null;
		
		try{
			content = "";		
			fr = new FileReader(srFileName);
			br = new BufferedReader(fr);	
			
			String 			temp = "";
			
			while ((temp=br.readLine()) != null ){
				content = content + temp + "\r\n";
			}	
						
			fr.close();
			fr = null;	
			br.close();
			br =null;							
		}catch(java.io.FileNotFoundException fnfe){			
		}catch(java.io.IOException ioe){			
		}
		
		return content;
	}
	
	/**
	 * after crate file(filename) and write a content(content)
	 */
	public static int createFileAndWrite(String filename, String content)
    {
        try
        {
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(filename));
            bufferedwriter.write(content);
            bufferedwriter.close();
			return 0;
        }
        catch(IOException ioexception)
        {
			return 1;
        }

    }

	public static String getFileName(String fileFullPath) {
		return getFileName(fileFullPath, "");
	}
	
	public static String getFileName(String fileFullPath, String type) {
		String[] 	arrFilePath		= null;
		int			iLastIndex 		= 0;
		int			iCount			= 0;
		String		fileName		= "";
		
		try {
			fileFullPath 	= fileFullPath.replace("\\", "/");
			arrFilePath		= fileFullPath.split("/");
			iLastIndex		= arrFilePath.length;
			
			fileName		= arrFilePath[iLastIndex-1];
			
			if("PATH".equals(type)) {
				fileName = "";
				for(iCount=0; iCount<iLastIndex-1; iCount++) {
					if(iCount != 0 ) { 
						fileName += "/";
					}
					fileName += arrFilePath[iCount];
				}
			} else {
				if(!"".equals(type)) {
					fileName = getFileExtent(fileName, type);
				}
			}

		} catch(Exception e) {
			fileName = "";
		}
		
		return fileName;
	}
	
	public static String getFileExtent(String fileFullPath) {
		return getFileExtent(fileFullPath, "");
	}
	
	public static String getFileExtent(String fileFullPath, String type) {
		String[] 	arrFilePath		= null;
		int			iLastIndex 		= 0;
		int			iCount			= 0;
		String		fileName		= "";
		
		try {
			
			arrFilePath		= fileFullPath.split("[.]");
			iLastIndex		= arrFilePath.length;
			
			fileName		= arrFilePath[iLastIndex-1];
			
			if("NAME".equals(type)) {
				fileName = "";
				for(iCount=0; iCount<iLastIndex-1; iCount++) {
					if(iCount != 0) { fileName += "."; }
					fileName += arrFilePath[iCount];
				}
			}
			
		} catch(Exception e) {
			fileName = "";
		}
		
		return fileName;

	}
}


package com.lexken.framework.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
public class FileHelper extends File {

	private String pathName  = null;  //파일의 경로(예 : "/weblogic")
	private String fileName  = null;  //파일의 이름(예 : "data.txt")
	private String fullName  = null;  //경로 + 이름(예 : "/weblogic/data.txt")

	private int READ_MAX_LEN = 1024 * 1 * 1;  //파일을 한번에 읽어들일 버퍼의 크기

	private UtilityHelper util = new UtilityHelper();


	/**
	* 기능 : Default 생성자
    * @param   name   객체를 생성시킬 파일명
	*/
    public FileHelper(){
        super("/");
    }


	/**
	* 기능 : FileHelper 클래스에 입력된 파일 또는 디렉토리에 대한 파일 객체를 생성시킨다.
    * @param   name   객체를 생성시킬 파일명
	*/
    public FileHelper(String fullName){
        super(fullName);
        this.fullName = fullName;
    }


	/**
	* 기능 : FileHelper 클래스에 입력된 경로에 있는 파일에 대한 파일 객체를 생성시킨다.
    * @param  parent   파일이 있는 경로명
	* @param  child    객체를 생성시킬 파일명
	*/
    public FileHelper(String parent, String child) {
        super(parent, child);
        this.pathName = parent;
        this.fileName = child;
    }


	/**
	* 기능 : FileHelper 클래스에 입력된 경로에 있는 파일에 대한 파일 객체를 생성시킨다.
    * @param  parent   파일이 있는 경로명에 대한 파일 객체
	* @param  child    객체를 생성시킬 파일명
	*/
    public FileHelper(File parent, String child) {
        super(parent, child);
        this.fileName = child;
    }


	/**
	* 기능 : 생성자에서 생성한 파일 객체로부터 텍스트 파일을 읽어서 문자열로 반환한다.
    * @param   name      텍스트 파일명
	* @return  String    텍스트 파일의 내용을 읽어들인 문자열
	*/
    public String readTextFile() throws IOException {
		FileInputStream reader = null;
		StringBuffer strText   = null;

		int readedLen    = 0;
		int fromOffset   = 0;
		int toOffset     = READ_MAX_LEN;
		byte[] tmp = new byte[READ_MAX_LEN];

		try {
		    reader  = new FileInputStream(this);
		    strText = new StringBuffer();

			while ((readedLen = reader.read(tmp, fromOffset, toOffset)) > 0) {
                strText.append(new String(tmp));
                
                tmp = new byte[READ_MAX_LEN];
			}
        }
		catch(IOException e) {
			e.printStackTrace();
			try {
				if (reader != null) reader.close();
			}
			catch(Exception ee) {
			}
			throw e;
		}
		finally {
			try {
				if (reader != null) reader.close();
			}
			catch(Exception e) {
			}
		}			

        return strText.toString().trim();
    }


	/**
	* 기능 : 주어진 텍스트 파일을 읽어서 문자열로 반환한다.
    * @param   name      텍스트 파일명
	* @return  String    텍스트 파일의 내용을 읽어들인 문자열
	*/
    public String readTextFile(String name)	throws FileNotFoundException, IOException {
		if ((name!=null) && (name.length()>0)) {
    		FileInputStream reader = null;
    		StringBuffer strText   = null;
    
    		int readedLen    = 0;
    		int fromOffset   = 0;
    		int toOffset     = READ_MAX_LEN;
    		byte[] tmp = new byte[READ_MAX_LEN];
    
    		try {
    		    reader  = new FileInputStream(new File(name));
    		    strText = new StringBuffer();
    
    			while ((readedLen = reader.read(tmp, fromOffset, toOffset)) > 0) {
                    strText.append(new String(tmp));
                    
                    tmp = new byte[READ_MAX_LEN];
    			}
            }
            catch(FileNotFoundException e) {
                throw e;
    		}
    		catch(IOException e) {
    			e.printStackTrace();
    			try {
    				if (reader != null) reader.close();
    			}
			    catch(Exception ee) {
			    }
    			throw e;
    		}
    		finally {
    			try {
    				if (reader != null) reader.close();
    			}
    			catch(Exception e) {
    			}
    		}			
            return strText.toString().trim();
		    
		}
		else {
		    return "";
		}
    }


	/**
	* 기능 : 입력받은 텍스트 문자열을 생성자에서 생성한 객체에 기록한다.(기존에 파일이 내용을 모두 지우고 새로 기록한다.)
    * @param   fileContent   파일에 기록할 텍스트 문자열
    * @param   filePath      기록할 파일명(경로 + 파일명)
	*/
	public void writeFile(String fileContent) throws IOException {
		BufferedWriter buf_writer = null;

		try {
			buf_writer = new BufferedWriter(new FileWriter(this.fullName));
			buf_writer.write(fileContent);
		}
		catch (IOException e) {
			e.printStackTrace();
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception ee) {
			}
			throw e;
		}
		finally {
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception e) {
			}
		}
	}


	/**
	* 기능 : 입력받은 텍스트 문자열을 입력된 파일에 기록한다.(기존에 파일이 내용을 모두 지우고 새로 기록한다.)
    * @param   fileContent   파일에 기록할 텍스트 문자열
    * @param   filePath      기록할 파일명(경로 + 파일명)
	*/
	public void writeFile(String fileContent, String filePath) throws IOException {
		BufferedWriter buf_writer = null;

		try {
			buf_writer = new BufferedWriter(new FileWriter(filePath));
			buf_writer.write(fileContent);
		}
		catch (IOException e) {
			e.printStackTrace();
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception ee) {
			}
			throw e;
		}
		finally {
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception e) {
			}
		}
	}


	/**
	* 기능 : 입력받은 텍스트 문자열을 생성자에서 생성한 객체에 기록한다.(기존에 파일의 제일 뒤에 추가하여 기록한다.)
    * @param   fileContent   파일에 기록할 텍스트 문자열
	*/
	public void appendTextFile(String fileContent) throws IOException {
		BufferedWriter buf_writer = null;

		try {
			buf_writer = new BufferedWriter(new FileWriter(this.fullName, true));
			buf_writer.write(fileContent);
		}
		catch (IOException e) {
			e.printStackTrace();
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception ee) {
			}
			throw e;
		}
		finally {
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception e) {
			}
		}
	}


	/**
	* 기능 : 입력받은 텍스트 문자열을 입력된 파일에 기록한다.(기존에 파일의 제일 뒤에 추가하여 기록한다.)
    * @param   fileContent   파일에 기록할 텍스트 문자열
    * @param   filePath      기록할 파일명(경로 + 파일명)
	*/
	public void appendTextFile(String fileContent, String filePath) throws IOException {
		BufferedWriter buf_writer = null;

		try {
			buf_writer = new BufferedWriter(new FileWriter(filePath, true));
			buf_writer.write(fileContent);
		}
		catch (IOException e) {
			e.printStackTrace();
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception ee) {
			}
			throw e;
		}
		finally {
			try {
				if(buf_writer != null) buf_writer.close();
			}
			catch(Exception e) {
			}
		}
	}

	/**
    * temp 디렉토리에 있는 파일을 원래의 디렉토리로 이동한다.
    * @param  source          복사할 파일의 위치와 이름
    * @param  target          복사되어질 파일의 위치와 이름(한글이름일 경우 내부적으로 Unicode로 인코딩한다.
    * @param  overwriteFlag   동일 파일이 존재할 경우 Overwrite 여부 체크
    * @param  delFlag         원본 파일의 삭제 여부
    * @param  String          복사된 파일의 이름
    */
    public String copyFile(String source, String target, boolean overwriteFlag, boolean delFlag) {
		File sourceFile    = null;
		File targetFile    = null;
		String newFileName = null;

		try {
		    sourceFile  = new File(source);
		    /* temp 폴더로 복사시에는 한글이 깨져서 보이나 원폴더로 복사 될때는 정상적으로 보이도록 하기 위해 */
		    targetFile  = new File(target);
		    newFileName = targetFile.getName();

		    /* 파일복사 프로세스  */
            /* 동일이름이 존재하면 새로운 파일로 Overwrite 한다. */
            if(overwriteFlag == true) {
    		    copyPhysicalFile(sourceFile, targetFile);
    		    //실제 파일 이동
                //reNamePhysicalFile(sourceFile, targetFile);
            }
            //동일 이름의 파일이 존재하면 이름을 변경하여 복사한다.
            else {
                if(targetFile.exists()) {
                    newFileName = createNewFileName(targetFile);
                    targetFile = new File(targetFile.getParent(), newFileName);
                }

                //실제 파일 복사
                //copyPhysicalFile(sourceFile, targetFile);
                //실제 파일 이동
                reNamePhysicalFile(sourceFile, targetFile);
            }

		    //원본파일 삭제
            /*
		    if(delFlag == true) {
		        if(sourceFile.exists()) {
		            sourceFile.delete();
		        }
		    }
		    */
		} catch(Exception e) {
		    e.printStackTrace();
		}
		
		return newFileName;
    }
    
    
    /**
     * 소스 디렉토리에 있는 파일을 원래의 디렉토리로 이동한다.
     * @param  source    복사할 원본 파일
     * @param  target    복사되어질 파일
     * @return boolean   복사 성공 여부
     */
     public boolean reNamePhysicalFile(File sourceFile, File targetFile) {
 		
 		boolean copyStatus = false;

 		if(sourceFile!=null && targetFile!=null) {
     		try {
     			sourceFile.renameTo(targetFile);
     		    copyStatus = true;
     		    
     		} catch(Exception e) {
     		    e.printStackTrace();
     		}
             finally {
     		    
             }
         }
         else {
         }

         return copyStatus;
     }

	/**
    * 소스 디렉토리에 있는 파일을 원래의 디렉토리로 이동한다.
    * @param  source    복사할 원본 파일
    * @param  target    복사되어질 파일
    * @return boolean   복사 성공 여부
    */
    public boolean copyPhysicalFile(File sourceFile, File targetFile) {
		InputStream inputStream   = null;
		OutputStream outputStream = null;
		int i, len=0;
		boolean copyStatus = false;

		if(sourceFile!=null && targetFile!=null) {
    		try {
    		    inputStream  = new FileInputStream(sourceFile);
    		    outputStream = new FileOutputStream(targetFile);

      		    while((i=inputStream.read()) != -1) {
    		       outputStream.write(i);
    		       len++;
    		    }
    		    copyStatus = true;

    		} catch(IOException e) {
    		    try {
        		    if(inputStream != null)  inputStream.close();
            	    if(outputStream != null) outputStream.close();
        		}
        		catch(Exception ee) {
        		}
    		    e.printStackTrace();
    		}
            finally {
    		    try {
        		    if(inputStream != null)  inputStream.close();
            	    if(outputStream != null) outputStream.close();
        		}
        		catch(Exception e) {
        		}
            }
        }
        else {
        }

        return copyStatus;
    }


	/**
    * 소스 디렉토리에 있는 파일을 원래의 디렉토리로 이동한다.
    * @param  orgFile    원래의 파일
    * @return String     새로 생성된 파일명
    */
    public String createNewFileName(File orgFile) {
		File   newFile     = null;
		String newFileName = null;
		String orgFileName = null;
        String namePart    = null;
        String exePart     = null;
        String pathPart    = null;
        String DELIM       = "_";
        String newIndex    = "";
        int nameLength     = 0;
        
		if(orgFile!=null) {
    		try {
                //확장자와 이름을 분리한다.
                orgFileName = orgFile.getName();
                pathPart    = orgFile.getParent();
                nameLength  = orgFileName.lastIndexOf(".");

                if(nameLength > 0) {
                    namePart = orgFileName.substring(0, nameLength);
                    exePart  = orgFileName.substring(nameLength, orgFileName.length());
                }
                else {
                    namePart = orgFileName;
                    exePart  = "";
                }

                /* 파일명이 기존의 이름이 변경된 상태에서 만들어진 것인지 체크하는 부분
                   뒤에서 3번째 자리가 "_"가 아닐 경우 번호 붙이는 로직에 의해서 만들어진 경우가 아니라
                   사용자에 의해 만들어진 이름이므로 01번부터 이름을 부여한다.
                */
                if(namePart.length() > 2) {
                    if(DELIM.equals(namePart.substring(namePart.length()-3, namePart.length()-2))) {
                        /* 세번째 자리가 "_"이더라도 뒤에가 숫자가 아닐 경우는 역시 사용자에 의해 만들어지 이름이다. */
                        try {
                            /* 뒤의 2자리가 숫자가 아니라면 NumberFormatException을 발생할 것이다. */
                            int currIndex = Integer.parseInt(namePart.substring(namePart.length()-2, namePart.length()));
                            /* 새로운 파일의 인덱스를 찾아온다. */
                            newIndex = getNewFileNameIndex(pathPart, namePart, DELIM, currIndex, exePart);

                            //기존에 붙였던 인덱스 번호를 삭제한다.
                            namePart = namePart.substring(0, namePart.length()-3);
                        }
                        catch(NumberFormatException e) {
                            newIndex = "01";
                        }
                    }
                    else {
                        newIndex = getNewFileNameIndex(pathPart, namePart, DELIM, 0, exePart);
                    }
                }
                else {
                    newIndex = getNewFileNameIndex(pathPart, namePart, DELIM, 0, exePart);
                }

                //새로운 이름을 부여한다.
                newFileName = namePart + DELIM + newIndex + exePart;

    		} catch(Exception e) {
    		    e.printStackTrace();
    		}
        }
        else {
        }

        return newFileName;
    }


    private String getNewFileNameIndex(String pathPart, String namePart, String DELIM, int index, String exePart) {
        File newFile    = null;
        String newIndex = null;
        /* 파일명이 존재하지 않을때까지 계속 인덱스를 증가시킨다. */
        do {
            newIndex = String.valueOf(++index);
            //무조건 자리수를 2자리로 만든다.
            if(newIndex.length() < 2) {
                newIndex = "0" + newIndex;
            }

            //새로운 파일 객체를 생성한다.
            newFile = new File(pathPart, namePart + DELIM + newIndex + exePart);

            //동일 파일의 사이즈를 99까지 제한한다.
            if(index == 100) {
                newIndex = "99";
                break;
            }
        } while(newFile.exists());

        return newIndex;
    }


	/**
	* 기능 : 생성자에서 지정한 디렉토리에 있는 파일을 전체 삭제한다.
	* 해당 폴더에 디렉토리가 없을 경우 사용한다.
	*/
	public void deleteAllFile() throws SecurityException {
		try {
			File[] delFileList = this.listFiles();
			for(int i=0; i<delFileList.length; i++) {
			    (delFileList[i]).delete();
			}
		}
		catch (SecurityException  e) {
			e.printStackTrace();
			throw e;
		}
	}


	 /**
	* 기능 : 생성자에서 디렉토리명을 포함한 파일명을 지정하고 해당 파일을 삭제한다.
	*/
	public void deleteFile() throws SecurityException {
		File deleteFile = null;
		try {
		    deleteFile = new File(fullName);
		    if(deleteFile.exists()) {
		        deleteFile.delete();
		    }
		    else {
		    }
		}
		catch (SecurityException  e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	/**
	* 기능 : 디렉토리명을 포함한 파일명을 파라메터로 받아 해당 파일을 삭제한다.
    * @param  	fileName   삭제할 파일(디렉토리명 포함)
	*/
	public void deleteFile(String fullName) throws SecurityException {
		File deleteFile = null;
		this.fullName = fullName;
		try {
		    deleteFile = new File(fullName);
		    if(deleteFile.exists()) {
		        deleteFile.delete();
		    }
		    else {
		    }
		}
		catch (SecurityException  e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	/**
	* 기능 : 생성자에서 지정한 디렉토리에 있는 파일중 배열에 있는 파일만 삭제한다.
    * @param  	fileList   삭제할 파일의 목록(디렉토리명 포함)
	*/
	public void deleteFile(String[] fileList) throws SecurityException {
		File deleteFile = null;
		try {
			for(int i=0; i<fileList.length; i++) {
			    deleteFile = new File(fileList[i]);
			    if(deleteFile.exists()) {
			        deleteFile.delete();
			    }
			    else {
			    }
			}
		}
		catch (SecurityException  e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	
	/**
	 * 기능 : 파일명에서 파일확장자만 리턴한다.
	 * @param      
	 * @return     String      파일 확장자명
	 */
	public String getFileExtension() {
	    String rtnExtension = "";
	    int iExtIndex = this.fileName.lastIndexOf(".");
	    int iLen      = this.fileName.length();
	    
	    if (this.fileName!=null && this.fileName.length()!=0){
            if ( iExtIndex != -1 ) {
                rtnExtension = this.fileName.substring(iExtIndex , iLen);
            } else {
                /* 확장자가 없는 경우 "" 를 리턴한다. */
                rtnExtension = "";
            }
        }
        return rtnExtension;
	}
	
	
	/**
	 * 기능 : 입력받은 파일명에서 파일확장자만 리턴한다.
	 * @param      String      파일명
	 * @return     String      파일 확장자명
	 */
	public String getFileTypeExtension(String fileName) {
	    String rtnExtension = "";
	    int iExtIndex = fileName.lastIndexOf(".");
	    int iLen      = fileName.length();
	    
	    if (fileName!=null && fileName.length()!=0){
            if ( iExtIndex != -1 ) {
                rtnExtension = fileName.substring(iExtIndex , iLen);
            } else {
                /* 확장자가 없는 경우 "" 를 리턴한다. */
                rtnExtension = "";
            }
        }
        return rtnExtension;
	}
	
	/**
	 * 
	 * @param fileName - 파일 확장자를 포함한 파일명
	 * @return
	 */
	public String getFileNameExtension(String fileName) {
	    String rtnExtension = "";
	    int iExtIndex = fileName.lastIndexOf(".");
	    
	    if (fileName!=null && fileName.length()!=0){
            if ( iExtIndex != -1 ) {
                rtnExtension = fileName.substring(0 , iExtIndex);
            } else {
                /* 확장자가 없는 경우 "" 를 리턴한다. */
                rtnExtension = "";
            }
        }
        return rtnExtension;
	}
	
	
	
	
}

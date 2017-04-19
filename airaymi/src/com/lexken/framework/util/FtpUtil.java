/**
 * @(#)file    FtpUtil.java
 * @(#)author  JaeHyun Park
 * @(#)version 1.0
 * @(#)date    2009. 04. 10
 */
package com.lexken.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FtpUtil extends FTPClient {

    private String server = null;
    private String user = null;
    private String password = null;
    private String sourcePath = null;
    private String targetPath = null;
    private String sourceFileName = null;
    private String targetFileName = null;
    
    protected Logger logger = Logger.getLogger(this.getClass());

    private static String fileSeperator = null;
    {
        fileSeperator = System.getProperty("file.separator");
    }

    /**
     * Default 생성자 - 아무런 작업을 하지 않는다.
     */
    public FtpUtil() {
    }

    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.sourceFileName = filename;
        this.targetFileName = filename;
    }

    /**
     * @param sourceFileName The sourceFileName to set.
     */
    public void setSourceFilename(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    /**
     * @param targetFileName The targetFileName to set.
     */
    public void setTargetFilename(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param sourcePath The sourcePath to set.
     */
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * @param targetPath The targetPath to set.
     */
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    /**
     * @param server The server to set.
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @param user The user to set.
     */
    public void setUser(String user) {
        this.user = user;
    }
    
   
    /**
     * 파일을 FTP 서버로 전송한다.
     * 
     * @throws Exception
     */
    public void upFile() throws Exception {
    	FileInputStream fis = null;
    	
        if( !isValidate() ) {
            logger.error("[Exception] FtpUtil.upFile() : Parameter has null or wrong value(s)...");
        }
         
        try {
            this.setControlEncoding("euc-kr");
            this.connect(server);
            
            int reply = this.getReplyCode();
            
            if(!FTPReply.isPositiveCompletion(reply)) {
                this.disconnect();
                logger.error("FTP 접속실패 IP : " + server);
            } else {
            	logger.debug("user =====> " + user);
            	logger.debug("pswd =====> " + password);
            	logger.debug("source =====> " + sourcePath);
            	logger.debug("target =====> " + targetPath);
            	logger.debug("filenm =====> " + sourceFileName);
            	
                this.login(user, password);
                
                logger.debug("login =====> OK");
                
                makeDirectoryFullPath(targetPath);
                
                logger.debug("Path =====> OK");
                
                this.setFileType(FTP.BINARY_FILE_TYPE);
                
                this.enterLocalActiveMode();		//FTP를 Active Mode 로 설정한다.
                
                this.setSoTimeout(10000);			//현재 커넥션 timeout을 10000밀리세컨드 설정.
                
                File source_file = new File(sourcePath + "/" + sourceFileName);
                fis = new FileInputStream(source_file);
                this.storeFile(targetPath + "/" + targetFileName, fis);
                fis.close();
                logger.debug("File Save =====> OK");
                
                this.logout();
                logger.debug("Logout =====> OK");
                
                //InputStream in = new FileInputStream(source_file);
                //this.storeFile(targetPath + "/" + targetFileName, in);
                //in.close();
                
                //this.logout();
            }
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            //throw e;
        } finally {
        	if(fis != null) {
                try {
                    fis.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        	if(this != null || this.isConnected()) {
                try {
                    this.disconnect();
                    logger.info("FTP Disconnected......... ");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 폴더가 없을 경우 폴더를 생성한다.
     * @param path
     * @return
     */
    private boolean makeDirectoryFullPath(String path) {
        boolean result = true;
        
        try {
            String[] arrPath = path.split("\\/");
            String fullPath = "";
            
            if(arrPath != null) {
                for(int i=0; i<arrPath.length; i++) {
                    if(arrPath[i] != null && !"".equals(arrPath[i])) {
                        fullPath += fileSeperator + arrPath[i];
                        if(!this.changeWorkingDirectory(fullPath)) {
                            result = this.makeDirectory(fullPath);
                            if(!result) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch(IOException ioe) {
            result = false;
        }

        return result;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    private boolean isValidate() throws Exception {
        if( server == null || "".equals(server) || user == null || "".equals(user)
                || password == null || "".equals(password) || sourceFileName == null || "".equals(sourceFileName) || targetFileName == null || "".equals(targetFileName)
                || sourcePath == null || "".equals(sourcePath) || targetPath == null || "".equals(targetPath) ) {
            return false;
        }
        
        return true;
    }
}
package egovframework.airyami.cmm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView
{

    public void download() {
        setContentType("application/download; utf-8;");
    }
    
    @Override
    protected void renderMergedOutputModel( Map<String,Object> model, HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        File file = (File)model.get("downloadFile");
        
        response.setContentType(getContentType());
        response.setContentLength((int)file.length());
        
        String browser = getBrowser(request);
        
        String fileName = (String)model.get("realFileName");
        String dispositionPrefix = "attachment;filename=";
        String encodedFileName = null;
        
        System.out.println("browser :: " + browser);
        System.out.println("fileName :: " + fileName);
        
        if(browser.equals("MSIE")){
            encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if(browser.equals("Firefox")) {
            //encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        	StringBuffer sb = new StringBuffer();
        	for(int i = 0; i < fileName.length(); i++){
        		char c = fileName.charAt(i);
        		if(c > '~'){
        			sb.append(URLEncoder.encode("" + c, "UTF-8"));
        		} else {
        			sb.append(c);
        		}
        	}
        	encodedFileName = sb.toString();
        } else if(browser.equals("Opera")) {
            encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if(browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < fileName.length(); i++){
                char c = fileName.charAt(i);
                if(c > '~'){
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFileName = sb.toString();
        } else {
            throw new RuntimeException("Not supported browser.");
        }
        
        System.out.println("encodedFileName :: " + encodedFileName);
        response.setHeader("Content-Disposition", dispositionPrefix + encodedFileName);
        response.setHeader("Content-Transfer-Encoding", "binary");
        
        if(browser.equals("Opera")) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
        
        
        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            System.out.println("e :: " + e.getStackTrace());
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (Exception e2) {}
            }
        }//try end

        out.flush();
        out.close();
    }
    
    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if(header.indexOf("MSIE") > -1){
            return "MSIE";
        } else if(header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if(header.indexOf("Opera") > -1) {
            return "Opera";
        } else {
            return "Firefox";
        }
    }
}

package com.lexken.framework.scDeptUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.lexken.framework.config.CommonConfig;

public class ScDeptUtilReLoad {

	private String[] scDeptUtilReLoadUrls;
	
	public void setScDeptUtilReLoadUrl(){
		CommonConfig commonConfig = CommonConfig.getInstance();
		String tempUrls = commonConfig.getProperty("SC_DEPT_UTIL_RELOAD_URL");
		if(tempUrls != null && !"".equals(tempUrls)){
			scDeptUtilReLoadUrls = tempUrls.split("\\|");
		}else{
			scDeptUtilReLoadUrls = null;
		}
	}
	
	public void scDeptUtilReLoad(){
		String tempResult = "";
		if(scDeptUtilReLoadUrls == null){
			setScDeptUtilReLoadUrl();
		}
		
		if(scDeptUtilReLoadUrls != null && scDeptUtilReLoadUrls.length > 0){
			for(int i=0; i<scDeptUtilReLoadUrls.length; i++){
				tempResult = this.getHttpPage(scDeptUtilReLoadUrls[i]);
			}
		}
		
	}
	
	/**
     * targetUrl에 해당하는 화면을 조회
     * @param targetUrl 조회할 화면의 URL(Http://localhost:80/test.html)
     * @return
     */
    public String getHttpPage(String targetUrl) {
        URL url = null;
        URLConnection con = null;
        DataOutputStream out = null;
        InputStream in = null;
        InputStreamReader insr = null;

        BufferedReader br = null;
        StringBuffer buf = new StringBuffer();
        String str = "";
        try {
            url = new URL(targetUrl);
            con = url.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);

            con.setUseCaches(false);
            con.setRequestProperty("content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            in = con.getInputStream();
            
            insr = new InputStreamReader(in, "utf-8");
            br = new BufferedReader(insr);
            
            String temp = null;
            
            while((temp = br.readLine())!=null) {             
                buf.append(temp);
                buf.append("\n");
            }       
            
            return buf.toString();
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        finally {
            try {
                if( out != null ) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch(Exception e) {
            }
        }
    }

}

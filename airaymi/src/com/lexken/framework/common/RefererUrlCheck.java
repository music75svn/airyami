package com.lexken.framework.common;

import com.lexken.framework.config.CommonConfig;

public class RefererUrlCheck {

	private static String[] refererUrls;
	
	public static void setRefererUrl(){
		CommonConfig commonConfig = CommonConfig.getInstance();
		String tempUrls = commonConfig.REFERER_URL;
		if(tempUrls != null && !"".equals(tempUrls)){
			refererUrls = tempUrls.split("\\|");
		}else{
			refererUrls = null;
		}
	}
	
	public static boolean chkRefererUrl(String referer){
		boolean result = false;
		if(referer != null){
			if("ALL".equals(referer)){
				result = true;
			}else{
				if(refererUrls == null){
					setRefererUrl();
				}
				for(int i=0; i<refererUrls.length; i++){
					if(referer.startsWith(refererUrls[i])){
						result = true;
						break;
					}
				}
			}
		}
		return result;
		
	}
	
	
}

package egovframework.airyami.cmm.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.multipart.MultipartFile;


public class FileUtil {
	
	public static final int BUFF_SIZE = 2048;
    
	/**
	* 전체 첨부파일 사이즈.
	*
	* @param files
	* @return files_size
	* @throws Exception
	*/
	public static boolean checkFileSize(Map<String, MultipartFile> files , long fileLimitSize) throws Exception {
	
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		fileLimitSize = fileLimitSize * 1048576;
		long files_size = 0;
		
		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			file = entry.getValue();
			files_size += file.getSize();
		}
		if(fileLimitSize >= files_size){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	/**
	* 전체 첨부파일 확장자 체크.
	*
	* @param files
	* @param adminBoard
	* @return files_ext
	* @throws Exception
	*/
	public static boolean checkFileExtension(Map<String, MultipartFile> files, String Upload_ext ) throws Exception {
	
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		String files_ext = "";
		String [] Upload_Extension = Upload_ext.split(",");
		int lastIndex = 0;
		int trueCheck = 0;
		int loopCheck = 0;
		
		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			file = entry.getValue();
			lastIndex = file.getOriginalFilename().lastIndexOf('.');
			files_ext = file.getOriginalFilename().substring( lastIndex+1, file.getOriginalFilename().length() );
			if(files_ext != null && files_ext != ""){
				loopCheck ++;
			}
			for(int i = 0; i < Upload_Extension.length; i++){
				if(Upload_Extension[i].toLowerCase().equals(files_ext.toLowerCase())){
					trueCheck++;
				}
			}
		}
		
		System.out.println("loopCheck :: " + loopCheck);
		System.out.println("trueCheck :: " + trueCheck);
		
		if(trueCheck == loopCheck){
			return true;
		}else{
			return false;
		}
	}
}

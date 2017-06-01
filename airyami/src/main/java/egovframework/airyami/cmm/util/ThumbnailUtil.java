package egovframework.airyami.cmm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

//import net.coobird.thumbnailator.Thumbnails;

public class ThumbnailUtil {

	/**
	 * 이미지를 입력한 width와 height로 변환한다
	 * @param request
	 * @return
	 */
	public static void imageScale(String srcFile, String dstFile, int width, int height) throws IOException
	{
		imageScale(srcFile, dstFile, width, height, "jpg");
	}
	
	public static void imageScale(String srcFile, String dstFile, int width, int height, String imgType) throws IOException
	{
		ResampleOp resampleOp = new ResampleOp(width, height);
		
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
		
		BufferedImage srcImg = ImageIO.read(new File(srcFile));

		BufferedImage rescaledImage = resampleOp.filter(srcImg, null);
			
		ImageIO.write(rescaledImage, imgType, new File(dstFile)); // png, jpg, gif, etc
			
	}

	// thumbnailator  사용코드  : 사용이 간단하고, scale, watermark 합성등 추가 기능이 추가 제공되지만 with,height가 원본 이미지 비율대로 변환됨(입력값으로 정확하게 반영안됨)
	// 차후 필요시 사용을 위해 code남겨둠
//	public static void generateThumbnail(String srcFilePath, String dstFilePath, int width, int height) throws IOException
//	{
//		Thumbnails.of(srcFilePath).size(width,  height).toFile(dstFilePath);
//	}
//
//	public static void generateThumbnail(String srcFilePath, String dstFilePath, float scale) throws IOException
//	{
//		Thumbnails.of(srcFilePath).scale(scale/100).toFile(dstFilePath);
//	}
}

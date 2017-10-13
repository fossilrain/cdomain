package cn.com.barcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

import sun.misc.BASE64Encoder; 

public class BarcodeUtil {
	/**
     * 128条形码
     *
     * @param strBarCode
     *            条形码：0-100位
     * @param dimension
     *            商品条形码：尺寸
     * @param barheight
     *            商品条形码：高度
     * @return 图片(Base64编码)
     */
	  public static String generateBarCode128(String strBarCode,String dimension,String barheight) {
	        
	 
	        try {
	        	ByteArrayOutputStream outputStream = null;
	            BufferedImage bi = null;
	            int len = strBarCode.length();
	            JBarcode productBarcode = new JBarcode(Code128Encoder.getInstance(),
	                    WidthCodedPainter.getInstance(),
	                    EAN13TextPainter.getInstance());
	 
	            // 尺寸，面积，大小 密集程度
	            productBarcode.setXDimension(Double.valueOf(dimension).doubleValue());
	            // 高度 10.0 = 1cm 默认1.5cm06928772300378
	            
	            productBarcode.setBarHeight(Double.valueOf(barheight).doubleValue());
	            // 宽度
	            productBarcode.setWideRatio(Double.valueOf(30).doubleValue());
//	      		    是否显示字体
	            productBarcode.setShowText(true);
//	         	   显示字体样式
	            productBarcode.setTextPainter(BaseLineTextPainter.getInstance()); 
	 
	            // 生成二维码
	            
	            bi = productBarcode.createBarcode(strBarCode);
	            
	            outputStream = new ByteArrayOutputStream();
	            ImageIO.write(bi, "jpg", outputStream);
	            BASE64Encoder encoder = new BASE64Encoder();
//	          System.err.println(encoder.encode(outputStream.toByteArray()));

	            return encoder.encode(outputStream.toByteArray());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "encodeError";
	        }
	    }
	
}

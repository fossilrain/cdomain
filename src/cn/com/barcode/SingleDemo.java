package cn.com.barcode;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

public class SingleDemo {

	public static void main(String[] args) throws InvalidAtributeException {
		JBarcode jbcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
		//Code128 Code Example
        jbcode.setEncoder(Code128Encoder.getInstance());
        jbcode.setPainter(WidthCodedPainter.getInstance());
        jbcode.setTextPainter(BaseLineTextPainter.getInstance());
        jbcode.setShowCheckDigit(false);
        String code = "201709212200438426";
        BufferedImage img = jbcode.createBarcode(code);
        saveToPNG(img, "Code128.png");
	}
	static void saveToPNG(BufferedImage img, String fileName){
	       saveToFile(img, fileName, ImageUtil.PNG);
	}
	static void saveToFile(BufferedImage img, String fileName, String format){
    	try{
            FileOutputStream fos = new FileOutputStream("d:/images/"+fileName);
            ImageUtil.encodeAndWrite(img, format, fos, 96, 96);
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

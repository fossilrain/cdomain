package cn.com.barcode;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

/**
 * Servlet implementation class BarcodeServlet
 */
@WebServlet("/barcode")
public class BarcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BarcodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		JBarcode jbcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
		//Code128 Code Example
        jbcode.setEncoder(Code128Encoder.getInstance());
        jbcode.setPainter(WidthCodedPainter.getInstance());
        jbcode.setTextPainter(BaseLineTextPainter.getInstance());
        jbcode.setShowCheckDigit(false);
//        String code = "201709212200438426";
        String code = request.getParameter("id");
        if(StringUtils.isBlank(code)){
        	code = "Empty Code";
        }
        try{
        	BufferedImage img = jbcode.createBarcode(code);
            ImageUtil.encodeAndWrite(img, ImageUtil.PNG, response.getOutputStream(), 96, 96);
            response.getOutputStream().close();
        }catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

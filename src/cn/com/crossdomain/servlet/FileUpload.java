package cn.com.crossdomain.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet(description = "文件上传（从表单）", urlPatterns = { "/FileUpload" })
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");//客户端网页我们控制为UTF-8
		response.setContentType("text/html;charset=utf-8");
		String path=request.getSession().getServletContext().getRealPath(File.separator) ;
		List<String> piclist=new ArrayList<String>();  //放上传的图片名
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload sfu=new ServletFileUpload(factory);
        sfu.setHeaderEncoding("UTF-8");  //处理中文问题
        sfu.setSizeMax(-1);   //限制文件大小
        try {
            List<FileItem> fileItems= sfu.parseRequest(request);  //解码请求 得到所有表单元素
            for (FileItem fi : fileItems) {
                //有可能是 文件，也可能是普通文字 
                if (fi.isFormField()) { //这个选项是 文字 
                    System.out.println("表单值为："+new String(fi.getString().getBytes("iso-8859-1"),"utf-8")+"\n表单名称为："+fi.getFieldName());
                }else{
                	if(fi.getSize() > 0){//有文件
                		 String fn=fi.getName();
                         System.out.println("文件名是："+fn);  //文件名 
                         // fn 是可能是这样的 c:\abc\de\tt\fish.jpg
                         path = path+"datedir";
                         File dirx = new File(path);
                         if(!dirx.exists()){
                         	dirx.mkdirs();
                         }
                         fi.write(new File(path,fn));
                         if (fn.endsWith(".jpg")) {
                             piclist.add(fn);  //把图片放入集合
                         }
                	}
                   
                }                
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }
		response.getWriter().append("Served at: ").append(request.getContextPath()+"\n"+piclist);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		
	}

}

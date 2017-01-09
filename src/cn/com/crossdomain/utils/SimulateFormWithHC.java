package cn.com.crossdomain.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SimulateFormWithHC {
	/**
	 * 
	 * @param request
	 * @param url	转发至的url
	 * @param isDel 转发请求后是否删除本地文件
	 * @return
	 */
	public static String execute(HttpServletRequest request,String url,boolean isDel){
		String resMsg = "";
		String path=request.getSession().getServletContext().getRealPath(File.separator)+"uuid_file"+File.separator;
		String curDateDir = (new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date()).replace("-", File.separator);
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload sfu=new ServletFileUpload(factory);
        sfu.setHeaderEncoding("UTF-8");  //处理中文问题
        sfu.setSizeMax(1024*1024);   //限制文件大小
        List<String> fileDirLi=new ArrayList<String>();
        try {
            List<FileItem> fileItems= sfu.parseRequest(request);  //解码请求 得到所有表单元素
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Consts.UTF_8);
            FileBody bin = null;
            StringBody content = null;
            String fn=null;
            for (FileItem fi : fileItems) {//有可能是 文件，也可能是普通文字 
                if (fi.isFormField()) { //这个选项是 文字 
                	content = new StringBody(fi.getString(), ContentType.create("text/plain", Consts.ISO_8859_1));
                	builder.addPart(fi.getFieldName(), content);
                }else{// 是文件
                	if(fi.getSize() > 0){//有文件
                		fn=fi.getName();
                        path = path+curDateDir+File.separator+UUID.randomUUID().toString();
                        File dirx = new File(path);
                        if(!dirx.exists()){
                        	dirx.mkdirs();
                        }
                        fi.write(new File(path,fn));
                        fileDirLi.add(path);
                        bin = new FileBody(new File(path,fn));
                        builder.addPart(fi.getFieldName(), bin);
                    }
                }  
            }    
            HttpEntity reqEntity = builder.build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(reqEntity);
            CloseableHttpClient httpClient = HttpClients.createDefault();
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				System.out.println("----------------------------------------");
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 取响应内容
					resMsg = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
				}
				// 销毁
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isDel){//删除文件
        	if(fileDirLi.size() > 0){
        		for(int i=0,n=fileDirLi.size();i<n;i++){
        			deleteAllFilesOfDir(new File(fileDirLi.get(i)));
        		}
        	}
        }
        return resMsg;
	}
	/**
	 * 删除文件夹及文件
	 * @param path
	 */
	private static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	} 
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 要上传的文件的路径
			String filePath = "D:\\my_shutdown.log";
			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			HttpPost httpPost = new HttpPost("http://localhost:80/testjs/servlet/UploadServlet");
			// 把文件转换成流对象FileBody
			FileBody bin = new FileBody(new File(filePath));
			// 普通字段 重新设置了编码方式
			StringBody comment = new StringBody("这里是一个评论", ContentType.create("text/plain", Consts.UTF_8));
			// StringBody comment = new StringBody("这里是一个评论",
			// ContentType.TEXT_PLAIN);

			StringBody name = new StringBody("王五", ContentType.create("text/plain", Consts.UTF_8));
			StringBody password = new StringBody("123456", ContentType.create("text/plain", Consts.UTF_8));

			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("media", bin)// 相当于<input type="file" name="media"/>
					.addPart("comment", comment).addPart("name", name)// 相当于<input type="text" name="name" value=name>
					.addPart("password", password).build();

			httpPost.setEntity(reqEntity);

			System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				System.out.println("----------------------------------------");
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 打印响应内容
					System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
				}
				// 销毁
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

}

package cn.com.crossdomain.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.crossdomain.utils.CommonHttpRequest;

/**
 * Servlet implementation class CrossDomain
 */
@WebServlet("/CrossDomain")
public class CrossDomain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrossDomain() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		Map<String,String[]> parameter = request.getParameterMap();
		String commonURL = "";
		String charset = "";
		StringBuffer msg = new StringBuffer();
		if(parameter != null && parameter.size() > 0){
			for(String keyx:parameter.keySet()){
				if("commonURL".equals(keyx)){
					commonURL = parameter.get("commonURL")[0];
				}else if("charset".equals(keyx)){
					charset = parameter.get("charset")[0];
				}else{
					if(parameter.get(keyx) != null && parameter.get(keyx).length > 0){
						for(int i=0,n=parameter.get(keyx).length;i<n;i++){
							msg.append("&"+keyx+"="+parameter.get(keyx)[i]);
						}
					}
				}
			}
		}
		if("".equals(commonURL)){
			response.getWriter().append("commonURL缺失");
		}else{
			if("".equals(charset)){
				charset = "UTF-8";
			}
			String resg = CommonHttpRequest.doHttpRequest(commonURL, "POST", 300000, msg.toString(), charset);
			response.getWriter().append(resg);
		}
	}

}

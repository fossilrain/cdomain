/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 Ricardo Mariaca
 * http://dynamicreports.sourceforge.net
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package cn.com.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
/**
 * @author Ricardo Mariaca (dynamicreports@gmail.com)
 */
@WebServlet("/pdfServlet")
public class HtmlServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		InputStream is = HtmlServlet2.class.getResourceAsStream("Blank_A4.jrxml");
		resp.setContentType("application/pdf");
		
		OutputStream out = resp.getOutputStream();
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(out);
		try {
			JasperReportBuilder report = report();
			
			report
			  .setTemplate(Templates.reportTemplate)
			  .setTemplateDesign(is)
			  .setParameters(createParameters())
			  /*.columns(
			  	col.column("Item",       "item",      type.stringType()),
			  	col.column("数量",   "quantity",  type.integerType()),
			  	col.column("Unit price", "unitprice", type.integerType()))*/
//			  .title(Templates.createTitleComponent("TestDRByDiyDesigner"))
			  .setDataSource(createDataSource())
			  .show(false)
			  ;
			File tempFile = File.createTempFile("xjjjxxxxxxxxx", ".pdf");
			report.toPdf(new FileOutputStream(tempFile));
			out.write(FileUtils.readFileToByteArray(tempFile));
			System.out.println("%%%%%%%%%%"+tempFile.getCanonicalPath());
//			report.toPdf(pdfExporter);//打印的pdf地址  
		} catch (DRException e) {
			e.printStackTrace();
		}
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	private class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}
	
	private Map<String, Object> createParameters(){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("yuwen", "98");
		parameters.put("shuxue", "100");
		parameters.put("yingyu", "90");
		parameters.put("shengwu", "99");
		parameters.put("wuli", "99");
		parameters.put("huaxue", "88");
		parameters.put("name", "张三");
		parameters.put("id", "201710122015");
		parameters.put("rank", "1");
		parameters.put("maxsubject", "数学");
		return parameters;
	}
	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("yuwen","shuxue","yingyu","shengwu","wuli","huaxue","name","id","rank","maxsubject");
		/*for (int i = 0; i < 10; i++) {
			dataSource.add("Book", (int) (Math.random() * 10) + 1, (int) (Math.random() * 100) + 1);
		}*/
		dataSource.add("98","100","90","99","99","88","张三","201710122015","1","数学");
		return dataSource;
	}
}
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

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

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
@WebServlet("/zitiServlet")
public class HtmlServlet3 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		InputStream is = HtmlServlet3.class.getResourceAsStream("templatedesign1.jrxml");
		resp.setContentType("application/pdf");
		
		OutputStream out = resp.getOutputStream();
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(out);
		try {
			JasperReportBuilder report = report();
			report.setTemplate(Templates.reportTemplate)
			.setParameters(createParameters())
			  .setTemplateDesign(is)
			  .columns(
			  	col.column("Item",       "item",      type.stringType()),
			  	col.column("数量",   "quantity",  type.integerType()),
			  	col.column("Unit price", "unitprice", type.integerType()))
			  //.title(Templates.createTitleComponent("JasperTemplateDesign1"))
			  .setDataSource(createDataSource())
//			  .show(false)
			  ;
			  ;
			File tempFile = File.createTempFile("最终测试web", ".pdf");
			report.toPdf(new FileOutputStream(tempFile));
			out.write(FileUtils.readFileToByteArray(tempFile));
			System.out.println("%%%%%%%%%%"+tempFile.getCanonicalPath());
//			report.toPdf(pdfExporter);//打印的pdf地址  
		} catch (DRException e) {
			e.printStackTrace();
		}
		out.close();
	}
	private Map<String, Object> createParameters(){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("zhongwen", "中文門");
		parameters.put("amhl", "አማርኛ中文abc");
		return parameters;
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
		for (int i = 0; i < 10; i++) {
			dataSource.add("አማርኛBook中文", (int) (Math.random() * 10) + 1, (int) (Math.random() * 100) + 1);
		}
		return dataSource;
	}
}
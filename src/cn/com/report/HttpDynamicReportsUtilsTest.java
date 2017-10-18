package cn.com.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jbarcode.encode.InvalidAtributeException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;

public class HttpDynamicReportsUtilsTest {
	public static void main(String[]args) throws IOException, InvalidAtributeException{
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("id", "201710122126003");
		parameters.put("name", "张三");
		parameters.put("barcode", "201710122126");
		
		DRDataSource dataSource = new DRDataSource("sort","feeItem","feeVal");
		for (int i = 0; i < 10; i++) {
			dataSource.add(String.valueOf(i+1), "xxx费用xxx", String.valueOf((Math.random() * 100) + 1));
		}
		File tempFile = File.createTempFile("cjg_tmp_pdf_test", ".pdf");
		try {
			InputStream is = HttpDynamicReportsUtilsTest.class.getResourceAsStream("Invoice2.jrxml");
			JasperReportBuilder report = report();
			report
			.setTemplate(Templates.reportTemplate)
			.setTemplateDesign(is)
			.setParameters(parameters)
			.setDataSource(dataSource)
			;
			report.toPdf(new FileOutputStream(tempFile));
		} catch (DRException e) {
			e.printStackTrace();
		} finally{
		}
	}
}

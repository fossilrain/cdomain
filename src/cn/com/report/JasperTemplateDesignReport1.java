/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2016 Ricardo Mariaca
 * http://www.dynamicreports.org
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
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class JasperTemplateDesignReport1 {
	
	public JasperTemplateDesignReport1() throws IOException {
		build();
	}
	
	private void build() throws IOException {
		InputStream is = JasperTemplateDesignReport1.class.getResourceAsStream("templatedesign1.jrxml");
		
		try {
			JasperReportBuilder report = report();
			report.setTemplate(Templates.reportTemplate)
			  .setTemplateDesign(is)
			  .setParameters(createParameters())
			  .columns(
			  	col.column("Item",       "item",      type.stringType()),
			  	col.column("数量",   "quantity",  type.integerType()),
			  	col.column("Unit price", "unitprice", type.integerType()))
//			  .title(Templates.createTitleComponent("JasperTemplateDesign1"))
			  .setDataSource(createDataSource())
			  .show(false);
			try {  
              /* FileOutputStream fileOutputStream = new FileOutputStream("D:/images/语言web.pdf");//构建一个pdf存放的输出位置  
                report.toPdf(fileOutputStream);//打印的pdf地址  
*/				File tempFile = File.createTempFile("最终测试1", ".pdf");
				report.toPdf(new FileOutputStream(tempFile));
				System.out.println("%%%%%%%%%%"+tempFile.getCanonicalPath());
				/*try {  
                    fileOutputStream.flush();  //保证pdf输出完毕  
                    fileOutputStream.close();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  */
            } catch (FileNotFoundException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }
		} catch (DRException e) {
			e.printStackTrace();
		}
	}
	private Map<String, Object> createParameters(){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("zhongwen", "中文");
		return parameters;
	}
	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
		for (int i = 0; i < 10; i++) {
			dataSource.add("Book中文", (int) (Math.random() * 10) + 1, (int) (Math.random() * 100) + 1);
		}
		return dataSource;
	}
	
	public static void main(String[] args) throws IOException {
		new JasperTemplateDesignReport1();
	}
}
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

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.cnd;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperHtmlExporterBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.PercentageColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

/**
 * @author Ricardo Mariaca (dynamicreports@gmail.com)
 */
@WebServlet("/htmlServlet")
public class HtmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {

	resp.setContentType("text/html");
	OutputStream out = resp.getOutputStream();

	JasperHtmlExporterBuilder htmlExporter = export.htmlExporter(out)
                                                 .setImagesURI("servlets/image?image=");
	/*JasperHtmlExporterBuilder htmlExporter = export.htmlExporter(out)
            .setImagesURI("http://127.0.0.1:8280/cdomain/barcode?id=201709212200438001&id=");*/
	CurrencyType currencyType = new CurrencyType();
	
	StyleBuilder boldStyle         = stl.style().bold();
	StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
	StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
	                                    .setBorder(stl.pen1Point())
	                                    .setBackgroundColor(Color.LIGHT_GRAY);
	StyleBuilder titleStyle        = stl.style(boldCenteredStyle)
	                                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
	                                    .setFontSize(15);
	
	//                                                           title,     field name     data type
	TextColumnBuilder<String>     itemColumn      = col.column("Item",       "item",      type.stringType()).setStyle(boldStyle);
	TextColumnBuilder<Integer>    quantityColumn  = col.column("Quantity",   "quantity",  type.integerType());
	TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", currencyType);
	//price = unitPrice * quantity
	TextColumnBuilder<BigDecimal> priceColumn     = unitPriceColumn.multiply(quantityColumn).setTitle("Price")
	                                                               .setDataType(currencyType);
	PercentageColumnBuilder       pricePercColumn = col.percentageColumn("Price %", priceColumn);
	TextColumnBuilder<Integer>    rowNumberColumn = col.reportRowNumberColumn("No.")
	                                                    //sets the fixed width of a column, width = 2 * character width
	                                                   .setFixedColumns(2)
	                                                   .setHorizontalAlignment(HorizontalAlignment.CENTER);
	Bar3DChartBuilder itemChart = cht.bar3DChart()
	                                 .setTitle("Sales by item")
	                                 .setCategory(itemColumn)
	                                 .addSerie(
	                                	 cht.serie(unitPriceColumn), cht.serie(priceColumn));
	Bar3DChartBuilder itemChart2 = cht.bar3DChart()
	                                 .setTitle("Sales by item")
	                                 .setCategory(itemColumn)
	                                 .setUseSeriesAsCategory(true)
	                                 .addSerie(
	                                	 cht.serie(unitPriceColumn), cht.serie(priceColumn));
	ColumnGroupBuilder itemGroup = grp.group(itemColumn);
	itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));
	
	ConditionalStyleBuilder condition1 = stl.conditionalStyle(cnd.greater(priceColumn, 150))
	                                        .setBackgroundColor(new Color(210, 255, 210));
	ConditionalStyleBuilder condition2 = stl.conditionalStyle(cnd.smaller(priceColumn, 30))
	                                        .setBackgroundColor(new Color(255, 210, 210));
	ConditionalStyleBuilder condition3 = stl.conditionalStyle(cnd.greater(priceColumn, 200))
	                                        .setBackgroundColor(new Color(0, 190, 0))
	                                        .bold();
	ConditionalStyleBuilder condition4 = stl.conditionalStyle(cnd.smaller(priceColumn, 20))
	                                        .setBackgroundColor(new Color(190, 0, 0))
	                                        .bold();
	StyleBuilder priceStyle = stl.style()
	                             .conditionalStyles(
	                              	condition3, condition4);
	priceColumn.setStyle(priceStyle);
	try {
		JasperReportBuilder report = report();
		report
		  .setColumnTitleStyle(columnTitleStyle)
		  .setSubtotalStyle(boldStyle)
		  .highlightDetailEvenRows()
		  .columns(//add columns
		  	rowNumberColumn, itemColumn, quantityColumn, unitPriceColumn, priceColumn, pricePercColumn)
		  .columnGrid(
		  	rowNumberColumn, quantityColumn, unitPriceColumn, grid.verticalColumnGridList(priceColumn, pricePercColumn))
		  .groupBy(itemGroup)
		  .subtotalsAtSummary(
		  	sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
		  .subtotalsAtFirstGroupFooter(
		  	sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
		  .detailRowHighlighters(
		  	condition1, condition2)
		  .title(//shows report title
		  	cmp.horizontalList()
		  		.add(
//		  			cmp.image(Templates.class.getResource("images/dynamicreports.png")).setFixedDimension(80, 80),
		  			cmp.image(new URL("http://127.0.0.1:8280/cdomain/barcode?id=201709212200438001")).setFixedDimension(155, 60),
		  			cmp.text("DynamicReports").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
		  			cmp.text("Getting started").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
		  		.newRow()
		  		.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
		  .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))//shows number of page at page footer
		  .summary(
		  	cmp.horizontalList(itemChart, itemChart2))
		  .setDataSource(createDataSource());//set datasource

		req.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, report.toJasperPrint());
		report.toHtml(htmlExporter);//create and show report
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
	
	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
		dataSource.add("Notebook", 1, new BigDecimal(500));
		dataSource.add("DVD", 5, new BigDecimal(30));
		dataSource.add("DVD", 1, new BigDecimal(28));
		dataSource.add("DVD", 5, new BigDecimal(32));
		dataSource.add("Book", 3, new BigDecimal(11));
		dataSource.add("Book", 1, new BigDecimal(15));
		dataSource.add("Book", 5, new BigDecimal(10));
		dataSource.add("Book", 8, new BigDecimal(9));
		return dataSource;
	}
}
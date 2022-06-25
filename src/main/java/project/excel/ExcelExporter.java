package project.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import project.entities.PosaoENTITY;

public class ExcelExporter {
    public void export(List<PosaoENTITY> list, HttpServletResponse response) throws IOException {
    	HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("sheet");
		HSSFRow head = sheet.createRow(0);
		
		CellStyle style = hwb.createCellStyle();
        HSSFFont font = hwb.createFont();
        font.setBold(true);
        style.setFont(font);
		
		HSSFCell h1 = head.createCell(0);
		HSSFCell h2 = head.createCell(1);
		HSSFCell h3 = head.createCell(2);
		HSSFCell h4 = head.createCell(3);
		HSSFCell h5 = head.createCell(4);
		HSSFCell h6 = head.createCell(5);
		HSSFCell h7 = head.createCell(6);
		HSSFCell h8 = head.createCell(7);
		
		h1.setCellStyle(style);
		h2.setCellStyle(style);
		h3.setCellStyle(style);
		h4.setCellStyle(style);
		h5.setCellStyle(style);
		h6.setCellStyle(style);
		h7.setCellStyle(style);
		h8.setCellStyle(style);
		
		h1.setCellValue("ID");
		h2.setCellValue("Naziv");
		h3.setCellValue("Datum početka");
		h4.setCellValue("Datum kraja");
		h5.setCellValue("Adresa");
		h6.setCellValue("Objekt");
		h7.setCellValue("Zarada");
		h8.setCellValue("Troškovi");
		
		int r = 1;
		
		for(PosaoENTITY posao: list) {
			HSSFRow row = sheet.createRow(r);
			
			HSSFCell c1 = row.createCell(0);
			HSSFCell c2 = row.createCell(1);
			HSSFCell c3 = row.createCell(2);
			HSSFCell c4 = row.createCell(3);
			HSSFCell c5 = row.createCell(4);
			HSSFCell c6 = row.createCell(5);
			HSSFCell c7 = row.createCell(6);
			HSSFCell c8 = row.createCell(7);
			
			c1.setCellValue(r);
			c2.setCellValue(posao.getNaziv());
			c3.setCellValue(posao.getDatumPocetka().toString());
			c4.setCellValue(posao.getDatumKraja().toString());
			c5.setCellValue(posao.getAdresa());
			c6.setCellValue(posao.getObjekt());
			c7.setCellValue(posao.getZarada());
			c8.setCellValue(posao.getTroškovi());
			
			r++;
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		OutputStream outputStream = response.getOutputStream();
		hwb.write(outputStream);
		hwb.close();
    }
}
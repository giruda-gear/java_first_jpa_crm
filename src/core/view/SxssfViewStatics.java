package com.aaihc.crm.core.view;

import asn.util.lang.StringUtil;
import com.aaihc.crm.core.domain.SheetStatics;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SxssfViewStatics extends AbstractXlsxStreamingView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String excelName = model.getOrDefault("filename", "excel").toString();
        String excelfileName = "";
        SXSSFRow row = null;

        List<SheetStatics> sheetStaticses = (List<SheetStatics>) model.get("sheetStaticses");
        List<SXSSFSheet> sheets = new ArrayList<>();

        for (SheetStatics sheetStatics : sheetStaticses) {
            sheets.add((SXSSFSheet) workbook.createSheet(sheetStatics.getNm()));
        }

        try {
            excelfileName = URLEncoder.encode(excelName.replaceAll(" ", "_"), "UTF-8");

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setFontName("맑은고딕");
            style.setFont(font);

            CellStyle style2 = workbook.createCellStyle();
            Font font2 = workbook.createFont();
            style2.setBorderBottom(CellStyle.BORDER_THIN);
            style2.setBorderLeft(CellStyle.BORDER_THIN);
            style2.setBorderRight(CellStyle.BORDER_THIN);
            style2.setBorderTop(CellStyle.BORDER_THIN);
            font2.setFontHeightInPoints((short) 10);
            font2.setFontName("맑은고딕");
            style2.setFont(font);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setFontName("맑은고딕");
            headerStyle.setFont(headerFont);

            for (int q = 0; q < sheetStaticses.size(); q++) {
                SXSSFSheet worksheet = sheets.get(q);
                SheetStatics sheetStatics = sheetStaticses.get(q);

                // 헤더
                Map hdrMap = sheetStatics.getHdrs();
                if (hdrMap != null) {
                    for (Object o : hdrMap.keySet()) {
                        row = (SXSSFRow) worksheet.createRow((Integer) o);
                        Cell headerCell = row.createCell(0);
                        headerCell.setCellStyle(headerStyle);
                        headerCell.setCellValue(StringUtil.toString(hdrMap.get(o)));
                    }
                }

                // 제목
                Map colNmMap = sheetStatics.getColNms();
                if (colNmMap != null) {
                    for (Object o : colNmMap.keySet()) {
                        row = (SXSSFRow) worksheet.createRow((Integer) o);
                        List<String> colNms = (List<String>) colNmMap.get(o);

                        for (int i = 0; i < colNms.size(); i++) {
                            Cell cell = row.createCell(i);
                            cell.setCellStyle(style);
                            cell.setCellValue(colNms.get(i));
                        }
                    }
                }

                // 내용
                Map contRowMap = sheetStatics.getContRows();
                if (contRowMap != null) {
                    for (Object o : contRowMap.keySet()) {
                        List<List<String>> contRows = (List<List<String>>) contRowMap.get(o);

                        for (int i = 0; i < contRows.size(); i++) {
                            row = (SXSSFRow) worksheet.createRow((Integer) o + i);
                            List<String> cols = contRows.get(i);
                            for (int j = 0; j < cols.size(); j++) {
                                Cell cell = row.createCell(j);
                                cell.setCellStyle(style2);
                                cell.setCellValue(cols.get(j));
                            }
                        }
                    }
                }

                // 셀병합
                if (sheetStatics.getMergedCells() != null) {
                    for (CellRangeAddress mergedCell : sheetStatics.getMergedCells()) {
                        worksheet.addMergedRegion(mergedCell);
                    }
                }
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment; filename=" + excelfileName + ".xlsx");

        } catch (Exception e) {
            logger.info("error : " + e);
        }
    }

}

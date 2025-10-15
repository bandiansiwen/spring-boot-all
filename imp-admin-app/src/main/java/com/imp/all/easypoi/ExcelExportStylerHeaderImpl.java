package com.imp.all.easypoi;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * @author Longlin
 * @date 2021/12/6 16:48
 * @description
 */
public class ExcelExportStylerHeaderImpl extends ExcelExportStylerDefaultImpl implements IExcelExportStyler {

    private static CellStyle headerStyle;

    public ExcelExportStylerHeaderImpl(Workbook workbook) {
        super(workbook);
        createHeaderCellStyler();
    }

    private void createHeaderCellStyler() {
        headerStyle = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short)12);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 背景色
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    @Override
    public CellStyle getHeaderStyle(short color) {
        return headerStyle;
    }

    @Override
    public CellStyle getTitleStyle(short i) {
        return headerStyle;
    }
}

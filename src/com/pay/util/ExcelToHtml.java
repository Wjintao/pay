package com.pay.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

/*import soja.base.SojaProperties;
import soja.transfer.DataErrorException;
import mvc.datamgr.DataManager;*/

/**
 * 
 * ClassName: ExcelToHtml 
 * @Description: TODO
 * @author wujintao
 * @date 2016-9-15
 */
public class ExcelToHtml {


    /**
     * SID.
     */
    private static final long serialVersionUID = -8344971443770122206L;


    /**
     * 读取 Excel 显示页面.
     * 
     * @param properties
     * @return
     * @throws Exception
     */

    public String read() throws Exception {
        HSSFSheet sheet = null;
        StringBuffer lsb = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormatFile = new SimpleDateFormat("yyyy-MM-dd");
        String excelFileName="C:\\ReportYunPay\\"+dateFormatFile.format(cal.getTime())+".xls";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFileName)); // 获整个Excel
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                sheet = workbook.getSheetAt(sheetIndex);// 获所有的sheet
                String sheetName = workbook.getSheetName(sheetIndex); // sheetName
                if (workbook.getSheetAt(sheetIndex) != null) {
                    sheet = workbook.getSheetAt(sheetIndex);// 获得不为空的这个sheet
                    if (sheet != null) {
                        int firstRowNum = sheet.getFirstRowNum(); // 第一行
                        int lastRowNum = sheet.getLastRowNum(); // 最后一行
                        // 构造Table
                        lsb.append("<h1 style=\"background-color:#678;color:white;\">"+sheetName+"</h1><hr/><hr/>"+"<table width=\"100%\" style=\"border:1px solid #000;border-width:1px 0 0 1px;margin:2px 0 2px 0;border-collapse:collapse;\">");
                        for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                            if (sheet.getRow(rowNum) != null) {// 如果行不为空，
                                HSSFRow row = sheet.getRow(rowNum);
                                short firstCellNum = row.getFirstCellNum(); // 该行的第一个单元格
                                short lastCellNum = row.getLastCellNum(); // 该行的最后一个单元格
                                int height = (int) (row.getHeight() / 15.625); // 行的高度
                                lsb.append("<tr height=\""
                                        + height
                                        + "\" style=\"border:1px solid #000;border-width:0 1px 1px 0;margin:2px 0 2px 0;\">");
                                for (short cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++) { // 循环该行的每一个单元格
                                    HSSFCell cell = row.getCell(cellNum);
                                    if (cell != null) {
                                        if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                            continue;
                                        } else {
                                            StringBuffer tdStyle =
                                                    new StringBuffer(
                                                            "<td style=\"border:1px solid #000; border-width:0 1px 1px 0;margin:2px 0 2px 0; ");
                                            HSSFCellStyle cellStyle = cell.getCellStyle();
                                            HSSFPalette palette = workbook.getCustomPalette(); // 类HSSFPalette用于求颜色的国际标准形式
                                            HSSFColor hColor =
                                                    palette.getColor(cellStyle
                                                            .getFillForegroundColor());
                                            HSSFColor hColor2 =
                                                    palette.getColor(cellStyle.getFont(workbook)
                                                            .getColor());

                                            //String bgColor = convertToStardColor(hColor);// 背景颜色
                                            String bgColor="#CCFFFF";
                                            short boldWeight =
                                                    cellStyle.getFont(workbook).getBoldweight(); // 字体粗细
                                            short fontHeight =
                                                    (short) (cellStyle.getFont(workbook)
                                                            .getFontHeight() / 2); // 字体大小
                                            //String fontColor = convertToStardColor(hColor2); // 字体颜色
                                            String fontColor = "#000033";
                                            if (bgColor != null && !"".equals(bgColor.trim())) {
                                                tdStyle.append(" background-color:" + bgColor
                                                        + "; ");
                                            }
                                            if (fontColor != null && !"".equals(fontColor.trim())) {
                                                tdStyle.append(" color:" + fontColor + "; ");
                                            }
                                            tdStyle.append(" font-weight:" + boldWeight + "; ");
                                            tdStyle.append(" font-size: " + fontHeight + "%;");
                                            lsb.append(tdStyle + "\"");

                                            int width =
                                                    (int) (sheet.getColumnWidth(cellNum) / 35.7); //
                                            int cellReginCol =
                                                    getMergerCellRegionCol(sheet, rowNum, cellNum); // 合并的列（solspan）
                                            int cellReginRow =
                                                    getMergerCellRegionRow(sheet, rowNum, cellNum);// 合并的行（rowspan）
                                            String align =
                                                    convertAlignToHtml(cellStyle.getAlignment()); //
                                            String vAlign =
                                                    convertVerticalAlignToHtml(cellStyle
                                                            .getVerticalAlignment());

                                            lsb.append(" align=\"" + align + "\" valign=\""
                                                    + vAlign + "\" width=\"" + width + "\" ");
                                            lsb.append(" colspan=\"" + cellReginCol
                                                    + "\" rowspan=\"" + cellReginRow + "\"");
                                            lsb.append(">" + getCellValue(cell) + "</td>");
                                        }
                                    }
                                }
                                lsb.append("</tr>"+"\n  ");
                            }
                        }
                        lsb.append("</table>"+"\n  <hr/><hr/>");
                    }
                }
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            System.out.println("文件 " + excelFileName + " 没有找到!");
        } catch (IOException e) {
            System.out.println("文件 " + excelFileName + " 处理错误(" + e.getMessage() + ")!");
        }finally{
            
        }
        //System.out.println(lsb);
        
        return lsb.toString();
    }



    /**
     * 取得单元格的值
     * 
     * @param cell
     * @return
     * @throws IOException
     */
    private static Object getCellValue(HSSFCell cell) throws IOException {
        Object value = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            value = cell.getRichStringCellValue().toString();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                value = sdf.format(date);
            } else {
                double value_temp = (double) cell.getNumericCellValue();
                BigDecimal bd = new BigDecimal(value_temp);
                BigDecimal bd1 = bd.setScale(3, bd.ROUND_HALF_UP);
                value = bd1.doubleValue();

                /*
                 * DecimalFormat format = new DecimalFormat("#0.###"); value =
                 * format.format(cell.getNumericCellValue());
                 */
            }
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
            value = "";
        }
        return value;
    }

    /**
     * 判断单元格在不在合并单元格范围内，如果是，获取其合并的列数。
     * 
     * @param sheet 工作表
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    private static int getMergerCellRegionCol(HSSFSheet sheet, int cellRow, int cellCol)
            throws IOException {
        int retVal = 0;
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress cra = (CellRangeAddress) sheet.getMergedRegion(i);
            int firstRow = cra.getFirstRow(); // 合并单元格CELL起始行
            int firstCol = cra.getFirstColumn(); // 合并单元格CELL起始列
            int lastRow = cra.getLastRow(); // 合并单元格CELL结束行
            int lastCol = cra.getLastColumn(); // 合并单元格CELL结束列
            if (cellRow >= firstRow && cellRow <= lastRow) { // 判断该单元格是否是在合并单元格中
                if (cellCol >= firstCol && cellCol <= lastCol) {
                    retVal = lastCol - firstCol + 1; // 得到合并的列数
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 判断单元格是否是合并的单格，如果是，获取其合并的行数。
     * 
     * @param sheet 表单
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    private static int getMergerCellRegionRow(HSSFSheet sheet, int cellRow, int cellCol)
            throws IOException {
        int retVal = 0;
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress cra = (CellRangeAddress) sheet.getMergedRegion(i);
            int firstRow = cra.getFirstRow(); // 合并单元格CELL起始行
            int firstCol = cra.getFirstColumn(); // 合并单元格CELL起始列
            int lastRow = cra.getLastRow(); // 合并单元格CELL结束行
            int lastCol = cra.getLastColumn(); // 合并单元格CELL结束列
            if (cellRow >= firstRow && cellRow <= lastRow) { // 判断该单元格是否是在合并单元格中
                if (cellCol >= firstCol && cellCol <= lastCol) {
                    retVal = lastRow - firstRow + 1; // 得到合并的行数
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 单元格背景色转换
     * 
     * @param hc
     * @return
     */
    private String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            int a = HSSFColor.AUTOMATIC.index;
            int b = hc.getIndex();
            if (a == b) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                String str;
                String str_tmp = Integer.toHexString(hc.getTriplet()[i]);
                if (str_tmp != null && str_tmp.length() < 2) {
                    str = "0" + str_tmp;
                } else {
                    str = str_tmp;
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 单元格小平对齐
     * 
     * @param alignment
     * @return
     */
    private String convertAlignToHtml(short alignment) {
        String align = "left";
        switch (alignment) {
            case HSSFCellStyle.ALIGN_LEFT:
                align = "left";
                break;
            case HSSFCellStyle.ALIGN_CENTER:
                align = "center";
                break;
            case HSSFCellStyle.ALIGN_RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 单元格垂直对齐
     * 
     * @param verticalAlignment
     * @return
     */
    private String convertVerticalAlignToHtml(short verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
            case HSSFCellStyle.VERTICAL_BOTTOM:
                valign = "bottom";
                break;
            case HSSFCellStyle.VERTICAL_CENTER:
                valign = "center";
                break;
            case HSSFCellStyle.VERTICAL_TOP:
                valign = "top";
                break;
            default:
                break;
        }

        return valign;
    }

}

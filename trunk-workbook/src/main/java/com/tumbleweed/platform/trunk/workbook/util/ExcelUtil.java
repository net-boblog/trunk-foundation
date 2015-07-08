package com.tumbleweed.platform.trunk.workbook.util;


import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2014年9月15日 下午10:19:12
 */
public class ExcelUtil {

    private static String english26Letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static Double getColumnValue(List<Object> rowData, int startIndex, String columnName, String columnFormat) {
        double value = 0d;
        String[] columns = columnFormat.split(",");
        for (int i = 0; i < columns.length; i++) {
            if (columnName.equals(columns[i])) {
                if (rowData.size() > startIndex + i) {
                    value += LogUtilDouble.parseDouble(rowData.get(startIndex + i).toString());
                } else {
                    continue;
                }
            }
        }
        return value;
    }

    public static void setPercentCellStyle(XSSFWorkbook workbook, XSSFSheet sheet, String[] columnNames,
                                           int startRowNum) {
        int[] columnIndexes = new int[columnNames.length];
        int i = 0;
        for (String columnName : columnNames) {
            columnIndexes[i] = getColumnIndex(columnName);
            i++;
        }
        setPercentCellStyle(workbook, sheet, columnIndexes, startRowNum);
    }

    public static void setPercentCellStyle(XSSFWorkbook workbook, XSSFSheet sheet, int[] columnIndexes,
                                           int startRowNum) {
        int startRowIndex = startRowNum - 1;
        XSSFRow row = null;
        XSSFCell cell = null;
        for (int rowIndex = startRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            for (int columnIndex : columnIndexes) {
                cell = row.getCell(columnIndex);
                XSSFCellStyle cellStyle = workbook.createCellStyle();
                XSSFDataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
                cell.setCellStyle(cellStyle);
            }
        }
    }

    public static Collection<String> read2007ExcelSheets(File file) throws FileNotFoundException, IOException {
        Collection<String> sheetNames = new ArrayList<String>();
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));

        int i = 0;
        while (true) {
            try {
                XSSFSheet sheet = xwb.getSheetAt(i++);
                sheetNames.add(sheet.getSheetName());
            } catch (Exception ex) {
                break;
            }
            i++;
        }

        return sheetNames;
    }

    public static List<Object> read2007ExcelSingleColumn(XSSFSheet sheet) {
        return read2007ExcelSingleColumn(sheet, 0);
    }

    public static List<Object> read2007ExcelSingleColumn(XSSFSheet sheet, int columnIndex) {
        return read2007ExcelSingleColumn(sheet, columnIndex, 0, Integer.MAX_VALUE);
    }

    public static List<Object> read2007ExcelSingleColumn(XSSFSheet sheet, int columnIndex, int startRow) {
        return read2007ExcelSingleColumn(sheet, columnIndex, startRow, Integer.MAX_VALUE);
    }

    public static List<Object> read2007ExcelSingleColumn(XSSFSheet sheet, String columnName, int startRowNum) {
        return read2007ExcelSingleColumn(sheet, getColumnIndex(columnName), startRowNum - 1);
    }

    public static int getColumnIndex(String columnEnglishName) {
        int columnIndex = 0;
        String uppers = columnEnglishName.toUpperCase();
        for (int i = 0; i < uppers.length(); i++) {
            columnIndex += (english26Letter.indexOf(uppers.charAt(i)) + 1) * Math.pow(26, uppers.length() - 1 - i);
        }

        return columnIndex - 1;
    }

    public static String getFormulaColumnName(int index) {
        if (index < 26) {
            return english26Letter.substring(index, index + 1);
        } else {
            int temp = index;
            StringBuffer buffer = new StringBuffer();
            buffer.append(english26Letter.charAt(temp % 26));
            return buffer.reverse().toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(getColumnIndex("A"));
        System.out.println(getColumnIndex("AA"));
        System.out.println(getColumnIndex("B"));
        System.out.println(getColumnIndex("C"));
        System.out.println(getColumnIndex("e"));
        System.out.println(getColumnIndex("AF"));
    }

    public static List<Object> read2007ExcelSingleColumn(XSSFSheet sheet, int columnIndex, int startRow, int maxRow) {
        List<Object> list = new LinkedList<Object>();
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum() && rowIndex < maxRow; rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            cell = row.getCell(columnIndex);
            if (cell == null) {
                list.add("");
            }
            DecimalFormat df = new DecimalFormat("0.00");// 格式化 number
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_FORMULA:
                    try {
                        value = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        value = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                case XSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue().trim();
                    break;
                case XSSFCell.CELL_TYPE_NUMERIC:
                    value = df.format(cell.getNumericCellValue());
                    break;
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case XSSFCell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                default:
                    value = cell.toString();
            }
            if (value == null || "".equals(value)) {
                value = "";
            }
            list.add(value);
        }

        return list;
    }

    public static List<List<Object>> read2007Excel(File file, String sheetName, int maxRow, int maxColumn) {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb;

        try {
            xwb = new XSSFWorkbook(new FileInputStream(file));
            xwb.setForceFormulaRecalculation(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("文件未找到");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取出错");
        }

        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheet(sheetName);
        System.out.println(sheet.getSheetName());

        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        for (int rowIndex = sheet.getFirstRowNum();
             rowIndex <= sheet.getLastRowNum() && rowIndex < maxRow; rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();

            if (row.getFirstCellNum() == -1) {
                list.add(linked);
                continue;
            }

            COLUMN:
            for (int column = row.getFirstCellNum(); column <= row.getLastCellNum() && column < maxColumn; column++) {

                cell = row.getCell(column);
                if (cell == null) {
                    linked.add("0");
                    continue COLUMN;
                }
                DecimalFormat df = new DecimalFormat("0.00");// 格式化 number
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_FORMULA:
                        try {
                            value = String.valueOf(cell.getNumericCellValue());
                        } catch (IllegalStateException e) {
                            value = String.valueOf(cell.getRichStringCellValue());
                        }
                        break;
                    case XSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue().trim();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        value = df.format(cell.getNumericCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                if (value == null || "".equals(value)) {
                    value = "0.00";
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }

}

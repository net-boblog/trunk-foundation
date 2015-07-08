package com.tumbleweed.platform.trunk.workbook.util;

import com.tumbleweed.platform.trunk.workbook.ColorPicker;
import com.tumbleweed.platform.trunk.workbook.model.ColumnDefinition;
import com.tumbleweed.platform.trunk.workbook.model.HeaderCellDefinition;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkbookUtil {
    //初始化列
    @SuppressWarnings("unused")
    public static void initColumns(List<HeaderCellDefinition> headerCellDefinitions,
            Map<Integer, ColumnDefinition> columnDefinitions, String[] columns, int rowCountOfHeader,
                        String splitStr) {
            int startColumnIndex = -1;
            int endColumnIndex = -1;
            Map<String, XSSFColor> colors = new HashMap<String, XSSFColor>();
            for (String column : columns) {
            String[] params = column.split("[" + splitStr + "]");
            String name = params[0];
            String colIndex = params[1];
            int width = Integer.parseInt(params[2]);
            String color = params[3];
            String type = params[4];
            boolean edit = params[5].equals("false") ? false : true;

            XSSFColor xColor = colors.get(color);
            if (xColor == null) {
            xColor = ColorPicker.getColor(color);
            colors.put(color, xColor);
            }

            startColumnIndex = endColumnIndex + 1;

            HeaderCellDefinition codeHeader = new HeaderCellDefinition(name, 0, rowCountOfHeader, startColumnIndex,
               ++endColumnIndex, xColor);
            headerCellDefinitions.add(codeHeader);
            if ("String".equals(type)) {
                ColumnDefinition cd = new ColumnDefinition();
                cd.setName(name);
                cd.setFieldNameOfValue(colIndex);
                cd.setFieldTypeOfValue(String.class.getName());
                cd.setEditable(edit);
                if (edit) {
                   cd.setEditableCellColor(ColorPicker.getColor("#FFEC8B"));
                }
                columnDefinitions.put(startColumnIndex, cd);
            } else if ("Integer".equals(type)) {
                ColumnDefinition cd = new ColumnDefinition();
                cd.setName(name);
                cd.setFieldNameOfValue(colIndex);
                cd.setFieldTypeOfValue(Integer.class.getName());
                cd.setEditable(edit);
                if (edit) {
                   cd.setEditableCellColor(ColorPicker.getColor("#FFEC8B"));
                }
                columnDefinitions.put(startColumnIndex, cd);
            } else if ("Long".equals(type)) {
                ColumnDefinition cd = new ColumnDefinition();
                cd.setName(name);
                cd.setFieldNameOfValue(colIndex);
                cd.setFieldTypeOfValue(Long.class.getName());
                cd.setEditable(edit);
                if (edit) {
                   cd.setEditableCellColor(ColorPicker.getColor("#FFEC8B"));
                }
                columnDefinitions.put(startColumnIndex, cd);
            } else if ("BigDecimal".equals(type)) {
                ColumnDefinition cd = new ColumnDefinition();
                cd.setName(name);
                cd.setFieldNameOfValue(colIndex);
                cd.setFieldTypeOfValue(BigDecimal.class.getName());
                cd.setEditable(edit);
                if (edit) {
                   cd.setEditableCellColor(ColorPicker.getColor("#FFEC8B"));
                }
                columnDefinitions.put(startColumnIndex, cd);
            } else if ("Double".equals(type)) {
                ColumnDefinition cd = new ColumnDefinition();
                cd.setName(name);
                cd.setFieldNameOfValue(colIndex);
                cd.setFieldTypeOfValue(Double.class.getName());
                cd.setEditable(edit);
                if (edit) {
                   cd.setEditableCellColor(ColorPicker.getColor("#FFEC8B"));
                }
                columnDefinitions.put(startColumnIndex, cd);
            }
            columnDefinitions.get(startColumnIndex).setSampleColumnModel(new HashMap<String, Object>());
            columnDefinitions.get(startColumnIndex).setDataColumnIndex(0);
            if (width >= 0) {
                columnDefinitions.get(startColumnIndex).setWidth(width);
            }
        }
    }

}

package com.tumbleweed.platform.trunk.workbook.util;


import com.mittop.platform.soupe.common.workbook.ColorPicker;
import com.mittop.platform.soupe.common.workbook.model.ColumnDefinition;
import com.mittop.platform.soupe.common.workbook.model.HeaderCellDefinition;
import com.mittop.platform.soupe.core.model.Organization;
import com.mittop.platform.soupe.core.model.User;
import com.mittop.product.bms.budget.core.model.dto.BudgetBaseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出工具类
 */
public class ExportUtil {

    public static final String FILE_SUFFIX_EXCEL = ".xlsx";

    public static final String CONTENT_TYPE_OF_DOWNLOAD =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static Log logger = LogFactory.getLog(ExportUtil.class);

    /**
     * 获取合法的Excel表页名称
     *
     * @param sheetName 输入
     * @return 合法的Excel表页名称
     */
    public static String getValidSheetName(String sheetName) {
        return sheetName.replace("/", "_");
    }

    public static ExportExcelColumn createColumn(String columnName, String dataIndex, Integer width,
                                                 String backgroundColor, String type, boolean isShow) {
        return new ExportExcelColumn(columnName, dataIndex, width, backgroundColor, type, isShow);
    }

    /**
     * 通过sortCode设置缩进
     *
     * @param dtos
     */
    public static void setItemNameIndentBySortCode(List<? extends BudgetBaseDTO> dtos) {
        int length = dtos.get(0).getSortCode().length();
        for (int i = 0; i < dtos.size(); i++) {
            length = length < dtos.get(i).getSortCode().length()
                    ? length : dtos.get(i).getSortCode().length();
        }

        for (int i = 0; i < dtos.size(); i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < (dtos.get(i).getSortCode().length() - length) / 4; j++) {
                s.append("  ");
            }
            dtos.get(i).setName(s.toString() + dtos.get(i).getName());
        }
    }

    /**
     * 获取导出文件的名称
     *
     * @param user
     * @param moduleName
     * @param organization
     * @param fileSuffix
     * @return
     */
    public static String getExportFileName(User user, String moduleName, Organization organization, String fileSuffix) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(moduleName);

        buffer.append(fileSuffix);

        return buffer.toString();
    }

    /**
     * 获取导出文件的名称
     *
     * @param user
     * @param moduleName
     * @param organization
     * @return
     */
    public static String getExportFileName(User user, String moduleName, Organization organization) {
        return getExportFileName(user, moduleName, organization, FILE_SUFFIX_EXCEL);
    }

    /**
     * 转换为百分数形式的字符串
     *
     * @param decimal
     * @return
     */
    public static String getPercentString(BigDecimal decimal) {
        //保证输出为 0.00% 10.00% 100.00%这种小数点后有两位的格式
        int precision = new Long(decimal.multiply(new BigDecimal(100)).longValue()).toString().length() + 2;
        return decimal.multiply(new BigDecimal(100), new MathContext(precision)) + "%";
    }

    public static void initColumns(
            List<HeaderCellDefinition> headerCellDefinitions, Map<Integer, ColumnDefinition> columnDefinitions,
            String[] columns, int rowCountOfHeader, String splitStr) {
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

    /**
     * 把workbook写入到response的outputStream中
     *
     * @param response
     * @param fileName 文件名称
     * @param workbook
     * @throws java.io.IOException
     */
    public static void writeToResponse(HttpServletResponse response, String fileName, XSSFWorkbook workbook)
            throws IOException {
        response.setContentType(ExportUtil.CONTENT_TYPE_OF_DOWNLOAD);
        response.setHeader("Content-Disposition", "attachment;"
                + "filename=" + URLEncoder.encode(fileName, "UTF-8"));

        OutputStream outputStream = response.getOutputStream();
        workbook.write(response.getOutputStream());
        outputStream.flush();
        outputStream.close();
    }

    public static ExportColumnDefinition getColumnDefinition(String[] columnDefinitionStrings) {
        return new ExportColumnDefinition(columnDefinitionStrings);
    }

    public static BigDecimal getNotNullValue(BigDecimal a) {
        return a != null ? a : BigDecimal.ZERO;
    }

    public static void fillDataMap(Map<String, Object> map, BudgetBaseDTO dto) {
        map.put("itemId", dto.getId().intValue());
        map.put("itemNO", dto.getItemNumber());
        map.put("itemCode", dto.getItemCode());
        map.put("sortCode", dto.getSortCode());

        map.put("name", dto.getName());
        map.put("leaf", dto.isLeaf());
        map.put("readOnly", dto.getReadOnly());
    }

    public static class ExportColumnDefinition {
        private List<ExportExcelColumn> list = new ArrayList<ExportExcelColumn>();

        public ExportColumnDefinition(String[] columnDefinitionStrings) {
            for (String columnDefinition : columnDefinitionStrings) {
                list.add(new ExportExcelColumn(columnDefinition));
            }
        }

        public void insertColumnByBeforeDataIndex(ExportExcelColumn exportExcelColumn, String dataIndex) {
            list.add(getColumnIndexByDataIndex(dataIndex), exportExcelColumn);
        }

        public void insertColumnByBeforeDataIndex(String columnString, String dataIndex) {
            insertColumnByBeforeDataIndex(new ExportExcelColumn(columnString), dataIndex);
        }

        public Integer getColumnIndexByDataIndex(String dataIndex) {
            int i = 0;
            for (ExportExcelColumn exportExcelColumn : list) {
                if (exportExcelColumn.getDataIndex().equals(dataIndex)) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public String[] getColumnStrings() {
            String[] strings = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                strings[i] = list.get(i).toString();
            }

            return strings;
        }
    }

    public static class ExportExcelColumn {

        private String name;
        private String dataIndex;
        private Integer width;
        private String backgroundColor;
        private boolean isShow = true;
        private String type = "String";

        public ExportExcelColumn(String columnDefinitionString) {
            String[] columns = columnDefinitionString.split(",");
            setName(columns[0]);
            setDataIndex(columns[1]);
            setWidth(Integer.parseInt(columns[2]));
            setBackgroundColor(columns[3]);
            setType(columns[4]);
            setShow(Boolean.parseBoolean(columns[5]));
        }

        public ExportExcelColumn(String name, String dataIndex, Integer width, String backgroundColor, String type,
                                 boolean isShow) {
            this.name = name;
            this.dataIndex = dataIndex;
            this.width = width;
            this.backgroundColor = backgroundColor;
            this.isShow = isShow;
            this.type = type;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(name);
            buffer.append(",");
            buffer.append(dataIndex);
            buffer.append(",");
            buffer.append(width);
            buffer.append(",");
            buffer.append(backgroundColor);
            buffer.append(",");
            buffer.append(type);
            buffer.append(",");
            buffer.append(isShow);
            return buffer.toString();
        }

        public String getName() {
            return name;
        }

        public ExportExcelColumn setName(String name) {
            this.name = name;
            return this;
        }

        public String getDataIndex() {
            return dataIndex;
        }

        public ExportExcelColumn setDataIndex(String dataIndex) {
            this.dataIndex = dataIndex;
            return this;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public ExportExcelColumn setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public boolean isShow() {
            return isShow;
        }

        public ExportExcelColumn setShow(boolean isShow) {
            this.isShow = isShow;
            return this;
        }

        public String getType() {
            return type;
        }

        public ExportExcelColumn setType(String type) {
            this.type = type;
            return this;
        }
    }

}

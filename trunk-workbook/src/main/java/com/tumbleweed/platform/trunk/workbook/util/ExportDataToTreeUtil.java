package com.tumbleweed.platform.trunk.workbook.util;

import com.mittop.product.bms.budget.core.model.dto.BudgetBaseDTO;

import java.util.List;

/**
 * 本工具类负责将导出的数据组成树状结构
 */

public  class ExportDataToTreeUtil {
    

    public static void dtoToTree(List<? extends BudgetBaseDTO> dtoList){
        int length = dtoList.get(0).getSortCode().length();
        for (int i = 0; i < dtoList.size(); i++) {
            length = length < dtoList.get(i).getSortCode().length()
                    ? length : dtoList.get(i).getSortCode().length();
        }

        for (int i = 0; i < dtoList.size(); i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < (dtoList.get(i).getSortCode().length() - length) / 4; j++) {
                s.append("  ");
            }
            dtoList.get(i).setItemName(s.toString() + dtoList.get(i).getItemName());
        }
    }
}

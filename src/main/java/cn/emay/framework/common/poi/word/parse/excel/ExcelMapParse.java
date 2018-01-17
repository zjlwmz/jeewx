/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.emay.framework.common.poi.word.parse.excel;


import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cn.emay.framework.common.poi.util.PoiElUtil;
import cn.emay.framework.common.poi.util.PoiPublicUtil;

import com.google.common.collect.Maps;

/**
 * 处理和生成Map 类型的数据变成表格
 * @author JueYue
 *  2014年8月9日 下午10:28:46
 */
public final class ExcelMapParse {

    /**
     * 解析参数行,获取参数列表
     * 
     * @author JueYue
     *  2013-11-18
     * @param currentRow
     * @return
     */
    private static String[] parseCurrentRowGetParams(XWPFTableRow currentRow) {
        List<XWPFTableCell> cells = currentRow.getTableCells();
        String[] params = new String[cells.size()];
        String text;
        for (int i = 0; i < cells.size(); i++) {
            text = cells.get(i).getText();
            params[i] = text == null ? ""
                : text.trim().replace(PoiElUtil.START_STR, PoiElUtil.EMPTY).replace(PoiElUtil.END_STR, PoiElUtil.EMPTY);
        }
        return params;
    }

    /**
     * 解析下一行,并且生成更多的行
     * @param table
     * @param index
     * @param list
     */
    public static void parseNextRowAndAddRow(XWPFTable table, int index,
                                             List<Object> list) throws Exception {
        XWPFTableRow currentRow = table.getRow(index);
        String[] params = parseCurrentRowGetParams(currentRow);
        String listname = params[0];
        boolean isCreate = !listname.contains(PoiElUtil.FOREACH_NOT_CREATE);
        listname = listname.replace(PoiElUtil.FOREACH_NOT_CREATE, PoiElUtil.EMPTY).replace(PoiElUtil.FOREACH_AND_SHIFT, PoiElUtil.EMPTY)
            .replace(PoiElUtil.FOREACH, PoiElUtil.EMPTY).replace(PoiElUtil.START_STR, PoiElUtil.EMPTY);
        String[] keys = listname.replaceAll("\\s{1,}", " ").trim().split(" ");
        params[0] = keys[1];
        table.removeRow(index);// 移除这一行
        int cellIndex = 0;// 创建完成对象一行好像多了一个cell
        Map<String, Object> tempMap = Maps.newHashMap();
        for (Object obj : list) {
            currentRow = isCreate ? table.insertNewTableRow(index++) : table.getRow(index++);
            tempMap.put("t", obj);
            for (cellIndex = 0; cellIndex < currentRow.getTableCells().size(); cellIndex++) {
                String val = PoiElUtil.eval(params[cellIndex], tempMap).toString();
                currentRow.getTableCells().get(cellIndex).setText("");
                PoiPublicUtil.setWordText(
                    currentRow.getTableCells().get(cellIndex).addParagraph().createRun(), val);
            }

            for (; cellIndex < params.length; cellIndex++) {
                String val = PoiElUtil.eval(params[cellIndex], tempMap).toString();
                PoiPublicUtil.setWordText(currentRow.createCell().addParagraph().createRun(), val);
            }
        }

    }

}

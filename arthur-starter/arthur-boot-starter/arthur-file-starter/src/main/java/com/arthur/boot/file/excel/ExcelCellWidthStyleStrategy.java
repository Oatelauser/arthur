package com.arthur.boot.file.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自适应列宽
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-21
 * @since 1.0
 */
public class ExcelCellWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

	/**
	 * 可以根据这里的最大宽度，按自己需要进行调整,搭配单元格样式实现类中的，自动换行，效果更好
	 */
	private static final int MAX_COLUMN_WIDTH = 50;

	/**
	 * key-SheetNo value-记录最大Widths
	 */
	private final Map<Integer, Map<Integer, Integer>> sheetColumnWidths = new HashMap<>();

	@Override
	protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList,
		Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		if (!isHead && CollectionUtils.isEmpty(cellDataList)) {
			return;
		}

		Map<Integer, Integer> maxColumnMap = sheetColumnWidths.computeIfAbsent(
			writeSheetHolder.getSheetNo(), k -> new HashMap<>());
		int columnWidth = this.calculateDataLength(cellDataList, cell, isHead);
		if (columnWidth < 0) {
			return;
		}

		int columnIndex = cell.getColumnIndex();
		columnWidth = Math.min(columnWidth, MAX_COLUMN_WIDTH);
		Integer maxColumnWidth = maxColumnMap.get(columnIndex);
		if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
			maxColumnMap.put(columnIndex, columnWidth);
			columnWidth += columnWidth / 2;
			writeSheetHolder.getSheet().setColumnWidth(columnIndex, columnWidth * 256);
		}
	}

	protected int calculateDataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
		if (isHead) {
			return cell.getStringCellValue().getBytes().length;
		}

		WriteCellData<?> cellData = cellDataList.get(0);
		return switch (cellData.getType()) {
			case STRING -> cellData.getStringValue().getBytes().length;
			case BOOLEAN -> cellData.getBooleanValue().toString().getBytes().length;
			case NUMBER -> cellData.getNumberValue().toString().getBytes().length;
			default -> -1;
		};
	}

}

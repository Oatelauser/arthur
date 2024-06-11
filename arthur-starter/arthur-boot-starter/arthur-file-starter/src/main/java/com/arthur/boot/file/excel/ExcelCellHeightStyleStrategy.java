package com.arthur.boot.file.excel;

import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

import static org.springframework.util.StringUtils.countOccurrencesOf;

/**
 * 自适应行高
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-21
 * @since 1.0
 */
class ExcelCellHeightStyleStrategy extends AbstractRowHeightStyleStrategy {

	/**
	 * 可以根据这里的最大宽度，按自己需要进行调整,搭配单元格样式实现类中的，自动换行，效果更好
	 */
	private static final short DEFAULT_HEIGHT = 300;


	@Override
	protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
	}

	@Override
	protected void setContentColumnHeight(Row row, int relativeRowIndex) {
		int maxHeight = 1;
		for (Iterator<Cell> iterator = row.cellIterator(); iterator.hasNext(); ) {
			Cell cell = iterator.next();
			if (CellType.STRING.equals(cell.getCellType())) {
				int height = countOccurrencesOf(cell.getStringCellValue(), "\n");
				maxHeight = Math.max(maxHeight, height);
			}
		}
		row.setHeight((short) (maxHeight * DEFAULT_HEIGHT));
	}
}

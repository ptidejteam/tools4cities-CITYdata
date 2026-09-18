package ca.concordia.encs.citydata.core.implementations;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This producer can load XLSX from a file or remotely via an HTTP request.
 * Each worksheet row is returned as a comma-separated String so XLSX data can
 * use the same operations currently used by CSV producers.
 *
 * @author Vinicius Mioto
 * @since 2026-09-17
 */
public non-sealed class XLSXProducer extends AbstractProducer<String>
		implements IProducer<String> {

	private int sheetIndex = 0;
	private String sheetName = null;

	public XLSXProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public XLSXProducer(final String filePath) {
		super(filePath);
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	@Override
	public void fetch() {
		beforeFetch();

		ArrayList<String> xlsxLines;

		try (InputStream inputStream = obtainInputStream();
			 Workbook workbook = WorkbookFactory.create(inputStream)) {

			Sheet sheet = sheetName != null && !sheetName.isBlank()
					? workbook.getSheet(sheetName)
					: workbook.getSheetAt(sheetIndex);

			if (sheet == null) {
				throw new MiddlewareException.DatasetNotFound(
						"Sheet not found in XLSX workbook");
			}

			xlsxLines = parseRows(sheet);
		} catch (MiddlewareException.DatasetNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MiddlewareException.DatasetNotFound(
					"Error processing XLSX data: " + e.getMessage());
		}

		this.setResult(xlsxLines);
		this.applyOperation();
	}

	protected void beforeFetch() {

	}

	protected InputStream obtainInputStream() {
		return this.fetchStream();
	}

	protected ArrayList<String> parseRows(Sheet sheet) {
		ArrayList<String> lines = new ArrayList<>();
		DataFormatter dataFormatter = new DataFormatter();

		for (Row row : sheet) {
			if (row == null || row.getLastCellNum() < 0) {
				continue;
			}

			List<String> cellValues = new ArrayList<>();

			for (int columnIndex = 0;
				 columnIndex < row.getLastCellNum();
				 columnIndex++) {

				Cell cell = row.getCell(columnIndex);

				String cellValue = formatCellValue(cell, dataFormatter);

				cellValues.add(cellValue);
			}

			boolean isEmptyRow = cellValues.stream().allMatch(String::isEmpty);

			if (!isEmptyRow) {
				lines.add(String.join(",", cellValues));
			}
		}

		return lines;
	}

	protected String formatCellValue(
			Cell cell,
			DataFormatter dataFormatter) {

		if (cell == null) {
			return "";
		}

		if (cell.getCellType() == CellType.NUMERIC
				&& !DateUtil.isCellDateFormatted(cell)) {
			return NumberToTextConverter.toText(cell.getNumericCellValue());
		}

		return dataFormatter.formatCellValue(cell).trim();
	}
}
package ca.concordia.encs.citydata.producers;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.implementations.CSVProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This Producer outputs metadata about a building, such as floors, zones and sensors.
 * @author Minette Zongo, Sikandar Ejaz
 * @since 2026-03-01
 */

public class CSVCalibrationProducer extends CSVProducer {

	public CSVCalibrationProducer(String filePath) {
		super(filePath);
	}

	public CSVCalibrationProducer(String filePath, RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	@Override
	public void fetch() {
		System.out.println("Fetching file from path: " + this.getFilePath());
		try {
			OutputStream outputStream = this.fetchFromPath();
			String csvString = outputStream.toString();
			ArrayList<String> csvLines = new ArrayList<>(Arrays.asList(csvString.split(System.lineSeparator())));
			this.setResult(csvLines);
			this.applyOperation();
		} catch (Exception e) {
			throw new MiddlewareException.DatasetNotFound("Error processing CSV data: " + e.getMessage());
		}
	}
}

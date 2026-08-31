package ca.concordia.encs.citydata.producers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.implementations.CSVProducer;

/**
 * This producer reads environmental sensor data from a CSV source, processes it line by line, and produces a list of sensor 
 * readings for further operations. It stores all non-empty lines, optionally applies a configured operation on the data, and 
 * makes the processed results available to consumers.
 * @author Minette Zongo M., Sikandar Ejaz
 * @date: 2025-10-03
 */

public class CSVEnvironmentalSensorProducer extends CSVProducer {

	public CSVEnvironmentalSensorProducer(String filePath) {
		super(filePath);
	}

	@Override
	public void fetch() {
		try {

			ByteArrayOutputStream outputStream = (ByteArrayOutputStream) this.fetchFromPath();

			String csvString = outputStream.toString(StandardCharsets.UTF_8);

			String[] lines = csvString.split("\\R");

			ArrayList<String> csvLines = new ArrayList<>();
			for (int i = 1; i < lines.length; i++) {
				String line = lines[i].trim();
				if (!line.isEmpty()) {
					csvLines.add(line);
				}
			}

			for (int i = 0; i < Math.min(3, csvLines.size()); i++) {
				System.out.println("[DEBUG] Line " + i + ": " + csvLines.get(i));
			}

			this.setResult(csvLines);
			this.applyOperation();

			if (this.getOperation() == null) {
				for (String line : csvLines) {
					System.out.println(line);
				}
			}

		} catch (Exception e) {
			throw new MiddlewareException.DatasetNotFound("Error processing temperature CSV data: " + e.getMessage());
		}
	}
}

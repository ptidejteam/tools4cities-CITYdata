package ca.concordia.encs.citydata.producers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.implementations.CSVProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This producer reads Flow data from a CSV source, processes it line by line, and produces a result set for the data 
 * readings for further operations. It stores all non-empty lines, optionally applies a configured operation on the data, and 
 * makes the processed results available to consumers.
 * @author Peter Yefi, Vinicius Mioto, Tahereh Bijani,  Mohamed Jendoubi  
 * @date: 2026-06-27
 * @author: Minette Z. Fixed the producer by adding the required constructor from CSVProducer to properly initialize the inherited base producer (CSVProducer)
 * @date: 2026-05-29
 */
public class CSVFCUProducer extends CSVProducer{

	public CSVFCUProducer(String filePath) {
		super(filePath);
	}
	
	public CSVFCUProducer(String filePath, RequestOptions fileOptions) {
		super(filePath, fileOptions);
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

			this.setResult(csvLines);
			this.applyOperation();


		} catch (Exception e) {
			throw new MiddlewareException.DatasetNotFound("Error processing FCU CSV data: " + e.getMessage());
		}
	}

}
